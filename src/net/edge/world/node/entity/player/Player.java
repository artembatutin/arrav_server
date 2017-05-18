package net.edge.world.node.entity.player;

import net.edge.Server;
import net.edge.net.codec.ByteMessage;
import net.edge.net.message.OutputMessages;
import net.edge.net.session.GameSession;
import net.edge.task.Task;
import net.edge.utils.*;
import net.edge.world.GameConstants;
import net.edge.world.World;
import net.edge.world.content.PlayerPanel;
import net.edge.world.content.TabInterface;
import net.edge.world.content.ViewingOrb;
import net.edge.world.content.clanchat.ClanMember;
import net.edge.world.content.combat.Combat;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.effect.CombatEffect;
import net.edge.world.content.combat.effect.CombatEffectTask;
import net.edge.world.content.combat.effect.CombatPoisonEffect;
import net.edge.world.content.combat.magic.CombatSpell;
import net.edge.world.content.combat.magic.CombatWeaken;
import net.edge.world.content.combat.ranged.CombatRangedDetails;
import net.edge.world.content.combat.special.CombatSpecial;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.content.combat.weapon.FightType;
import net.edge.world.content.combat.weapon.WeaponAnimation;
import net.edge.world.content.combat.weapon.WeaponInterface;
import net.edge.world.content.commands.impl.UpdateCommand;
import net.edge.world.content.container.impl.Equipment;
import net.edge.world.content.container.impl.Inventory;
import net.edge.world.content.container.impl.bank.Bank;
import net.edge.world.content.dialogue.DialogueBuilder;
import net.edge.world.content.dialogue.impl.OptionDialogue;
import net.edge.world.content.item.PotionConsumable;
import net.edge.world.content.market.MarketShop;
import net.edge.world.content.minigame.Minigame;
import net.edge.world.content.minigame.MinigameContainer;
import net.edge.world.content.minigame.MinigameHandler;
import net.edge.world.content.pets.Pet;
import net.edge.world.content.pets.PetManager;
import net.edge.world.content.quest.QuestManager;
import net.edge.world.content.scene.impl.IntroductionCutscene;
import net.edge.world.content.skill.Skill;
import net.edge.world.content.skill.Skills;
import net.edge.world.content.skill.action.SkillActionTask;
import net.edge.world.content.skill.agility.AgilityCourseBonus;
import net.edge.world.content.skill.prayer.Prayer;
import net.edge.world.content.skill.slayer.Slayer;
import net.edge.world.content.skill.smithing.Smelting;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.teleport.impl.DefaultTeleportSpell;
import net.edge.world.locale.Location;
import net.edge.world.locale.Position;
import net.edge.world.node.NodeType;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.model.Graphic;
import net.edge.world.node.entity.model.Hit;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.NpcAggression;
import net.edge.world.node.entity.npc.impl.gwd.GodwarsFaction;
import net.edge.world.node.entity.player.assets.*;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.node.entity.update.UpdateFlag;
import net.edge.world.node.item.Item;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkState;

/**
 * The character implementation that represents a node that is operated by an
 * actual person. This type of node functions solely through communication with
 * the client, by reading data from and writing data to non-blocking sockets.
 * @author lare96 <http://github.com/lare96>
 */
public final class Player extends EntityNode {
	
	/**
	 * The player appearance update white skull identifier.
	 */
	public static final int WHITE_SKULL = 0;
	
	/**
	 * The player appearance update red skull identifier.
	 */
	public static final int RED_SKULL = 1;
	
	/**
	 * The logger that will print important information.
	 */
	private static Logger logger = LoggerUtils.getLogger(Player.class);
	
	/**
	 * Determines if this player is playing in nightmare mode.
	 */
	private boolean nightmareMode;
	
	/**
	 * The hash collection of the local players.
	 */
	private final Set<Player> localPlayers = new LinkedHashSet<>(255);
	
	/**
	 * The hash collection of the local npcs.
	 */
	private final Set<Npc> localNpcs = new LinkedHashSet<>(255);
	
	/**
	 * The hash collection of friends.
	 */
	private final Set<Long> friends = new HashSet<>(200);
	
	/**
	 * The hash collection of ignores.
	 */
	private final Set<Long> ignores = new HashSet<>(100);
	
	/**
	 * The quest manager for this player.
	 */
	private final QuestManager quest_manager = new QuestManager(this);
	
	/**
	 * The container that holds the inventory items.
	 */
	private final Inventory inventory = new Inventory(this);
	
	/**
	 * The container that holds the bank items.
	 */
	private final Bank bank = new Bank(this);
	
	/**
	 * The container that holds the equipment items.
	 */
	private final Equipment equipment = new Equipment(this);
	
	/**
	 * The private message manager that manages messages for this player.
	 */
	private final PrivateMessage privateMessage = new PrivateMessage(this);
	
	/**
	 * The dialogue builder for this player.
	 */
	private final DialogueBuilder dialogueChain = new DialogueBuilder(this);
	
	/**
	 * The I/O manager that manages I/O operations for this player.
	 */
	private GameSession session;
	
	/**
	 * The godwars killcount that can be increased by this player.
	 */
	private int[] godwarsKillcount = new int[4];
	
	/**
	 * The array of skills that can be trained by this player.
	 */
	private final Skill[] skills = new Skill[25];
	
	/**
	 * Gets the weight of this player.
	 * @return player's weight.
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Sets a new value for {@link #weight}.
	 * @param weight the new value to set.
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	/**
	 * The weight of the player.
	 */
	private double weight;
	
	/**
	 * The array of booleans determining which prayers are active.
	 */
	private final EnumSet<Prayer> prayerActive = EnumSet.noneOf(Prayer.class);
	
	/**
	 * The array determining which quick prayers id are stored.
	 */
	private final List<Prayer> quickPrayers = new ArrayList<>();
	
	/**
	 * The array determining which quick prayers id are selected on the interface.
	 */
	private final List<Prayer> selectedQuickPrayers = new ArrayList<>();
	
	/**
	 * Represents the class that holds functionality regarding completing agility obstacle laps.
	 */
	private final AgilityCourseBonus agility_bonus = new AgilityCourseBonus();
	
	/**
	 * The collection of stopwatches used for various timing operations.
	 */
	private final Stopwatch slashTimer = new Stopwatch().reset(), eatingTimer = new Stopwatch().reset(), potionTimer = new Stopwatch().reset(), tolerance = new Stopwatch(), lastEnergy = new Stopwatch().reset(), buryTimer = new Stopwatch(), logoutTimer = new Stopwatch();
	
	/**
	 * The collection of counters used for various counting operations.
	 */
	private final MutableNumber poisonImmunity = new MutableNumber(), teleblockTimer = new MutableNumber(), skullTimer = new MutableNumber(), specialPercentage = new MutableNumber(100);
	
	/**
	 * Holds an optional wrapped inside the Antifire details.
	 */
	private Optional<AntifireDetails> antifireDetails = Optional.empty();
	
	/**
	 * The running energy double.
	 */
	private double runEnergy = 100D;
	
	/**
	 * The encoder that will encode and send messages.
	 */
	private final OutputMessages messages;
	
	/**
	 * The enter input listener which will execute code when a player submits an input.
	 */
	private Optional<Function<String, ActionListener>> enterInputListener = Optional.empty();
	
	/**
	 * The container of appearance values for this player.
	 */
	private final PlayerAppearance appearance = new PlayerAppearance();
	
	/**
	 * The venged flag of this player.
	 */
	private boolean venged;
	
	/**
	 * The banning flag of this player.
	 */
	private boolean banned;
	
	/**
	 * The muted flag of this player.
	 */
	private boolean muted;
	
	/**
	 * The validation flag of this player.
	 */
	private boolean validated;
	
	/**
	 * The amount of authority this player has over others.
	 */
	private Rights rights = Rights.PLAYER;
	
	/**
	 * The current username of this player.
	 */
	private String username;
	
	/**
	 * The username hash for this player.
	 */
	private final long usernameHash;
	
	/**
	 * The current password of this player.
	 */
	private String password;
	
	/**
	 * The total amount of npcs this player has killed.
	 */
	private final MutableNumber npcKills = new MutableNumber();
	
	/**
	 * The total amount of players this player has killed.
	 */
	private final MutableNumber playerKills = new MutableNumber();
	
	/**
	 * The total amount of times this player died to a {@link Npc}
	 */
	private final MutableNumber npcDeaths = new MutableNumber();
	
	/**
	 * The total amount of times this player has died to a {@link Player}.
	 */
	private final MutableNumber playerDeaths = new MutableNumber();
	
	/**
	 * The current killstreak this player has ever had.
	 */
	private final MutableNumber currentKillstreak = new MutableNumber();
	
	/**
	 * The highest killstreak this player has ever had.
	 */
	private final MutableNumber highestKillstreak = new MutableNumber();
	
	/**
	 * The amount of slayer points the player has.
	 */
	private int slayerPoints;
	
	/**
	 * The slayer instance for this player.
	 */
	private Optional<Slayer> slayer = Optional.empty();
	
	/**
	 * A list containing all the blocked slayer tasks of the player.
	 */
	private String[] blockedTasks = new String[5];
	
	/**
	 * The combat spell currently selected.
	 */
	private CombatSpell castSpell;
	
	/**
	 * The flag that determines if the player is autocasting.
	 */
	private boolean autocast;
	
	/**
	 * The combat spell currently being autocasted.
	 */
	private CombatSpell autocastSpell;
	
	/**
	 * The combat special that has been activated.
	 */
	private CombatSpecial combatSpecial;
	
	/**
	 * The condition if the player's screen is on focus.
	 */
	private boolean focused;
	
	/**
	 * The ranged details for this player.
	 */
	private final CombatRangedDetails rangedDetails = new CombatRangedDetails(this);
	
	/**
	 * The current viewing orb that this player has openShop.
	 */
	private ViewingOrb viewingOrb;
	
	/**
	 * The flag that determines if this player is disabled.
	 */
	private final ActivityManager activityManager = new ActivityManager(this);
	
	/**
	 * The flag that determines if the special bar has been activated.
	 */
	private boolean specialActivated;
	
	/**
	 * The current skill action that is going on for this player.
	 */
	private Optional<SkillActionTask> action = Optional.empty();
	
	/**
	 * The current fight type the player is using.
	 */
	private FightType fightType = FightType.UNARMED_PUNCH;
	
	/**
	 * The weapon animation for appearance updating.
	 */
	private WeaponAnimation weaponAnimation;
	
	/**
	 * The shield animation for appearance updating.
	 */
	private ShieldAnimation shieldAnimation;
	
	/**
	 * The current prayer type used by the player.
	 */
	private PrayerBook prayerBook = PrayerBook.NORMAL;
	
	/**
	 * The current spellbook used by the player.
	 */
	private Spellbook spellbook = Spellbook.NORMAL;
	
	/**
	 * The task that handles combat prayer draining.
	 */
	private Task prayerDrain = null;
	
	/**
	 * The wilderness level this player is in.
	 */
	private int wildernessLevel;
	
	/**
	 * The weapon interface this player currently has openShop.
	 */
	private WeaponInterface weapon;
	
	/**
	 * The current teleport stage that this player is in.
	 */
	private int teleportStage;
	
	/**
	 * The pet that this player has spawned.
	 */
	private final PetManager petManager = new PetManager(this);
	
	/**
	 * The familiar this player has spawned.
	 */
	private Optional<Familiar> familiar = Optional.empty();
	
	/**
	 * The option value used for npc dialogues.
	 */
	private OptionDialogue.OptionType option;
	
	/**
	 * The identifier for the head icon of this player.
	 */
	private int headIcon = -1;
	
	/**
	 * The identifier for the skull icon of this player.
	 */
	private int skullIcon = -1;
	
	/**
	 * The flag that determines if a wilderness interface is present.
	 */
	private boolean wildernessInterface;
	
	/**
	 * The flag that determines if a multicombat interface is present.
	 */
	private boolean multicombatInterface;
	
	/**
	 * The flag that determines if a duel context is present.
	 */
	private boolean duelingContext;
	
	/**
	 * The flag that determines if a godwars context is present.
	 */
	private boolean godwarsInterface;
	
	/**
	 * The current minigame this player is in.
	 */
	private Optional<Minigame> minigame = Optional.empty();
	
	/**
	 * The container class which holds functions of values which should be modified.
	 */
	private final MinigameContainer minigameContainer = new MinigameContainer();
	
	/**
	 * The array of chat text packed into bytes.
	 */
	private byte[] chatText;
	
	/**
	 * The current chat color the player is using.
	 */
	private int chatColor;
	
	/**
	 * The current chat effects the player is using.
	 */
	private int chatEffects;
	/**
	 * The clan this player is in.
	 */
	private Optional<ClanMember> clan = Optional.empty();
	
	/**
	 * The player-npc identifier for updating.
	 */
	private int playerNpc = -1;
	
	/**
	 * The cached player update block for updating.
	 */
	private ByteMessage cachedUpdateBlock;
	
	/**
	 * The stand index for this player.
	 */
	private int standIndex = 0x328;
	
	/**
	 * The turn index for this player.
	 */
	private int turnIndex = 0x337;
	
	/**
	 * The walk index for this player.
	 */
	private int walkIndex = 0x333;
	
	/**
	 * The turn 180 index for this player.
	 */
	private int turn180Index = 0x334;
	
	/**
	 * The turn 90 clock-wise index for this player.
	 */
	private int turn90CWIndex = 0x335;
	
	/**
	 * The turn 90 counter clock-wise index for this player.
	 */
	private int turn90CCWIndex = 0x336;
	
	/**
	 * The run index for this player.
	 */
	private int runIndex = 0x338;
	
	/**
	 * If the region has been updated this sequence.
	 */
	private boolean updateRegion;
	
	/**
	 * The result of his search in the market.
	 */
	private MarketShop marketShop;
	
	/**
	 * Creates a new {@link Player}.
	 * @param usernameHash the username hash of this player.
	 */
	public Player(Long usernameHash) {
		super(GameConstants.STARTING_POSITION, NodeType.PLAYER);
		this.usernameHash = usernameHash;
		this.messages = new OutputMessages(this);
	}
	
	public void sendDefaultSidebars() {
		TabInterface.ATTACK.sendInterface(this, 2423);
		TabInterface.CLAN_CHAT.sendInterface(this, 50128);
		TabInterface.SKILL.sendInterface(this, 3917);
		TabInterface.QUEST.sendInterface(this, 638);
		TabInterface.INVENTORY.sendInterface(this, 3213);
		TabInterface.EQUIPMENT.sendInterface(this, 1644);
		TabInterface.PRAYER.sendInterface(this, getPrayerBook().getId());
		TabInterface.MAGIC.sendInterface(this, getSpellbook().getId());
		TabInterface.FRIEND.sendInterface(this, 5065);
		TabInterface.IGNORE.sendInterface(this, 5715);
		TabInterface.LOGOUT.sendInterface(this, 2449);
		TabInterface.SETTING.sendInterface(this, 904);
		TabInterface.EMOTE.sendInterface(this, 147);
		TabInterface.ATTACK.sendInterface(this, 2423);
	}
	
	@Override
	public void register() {
		OutputMessages encoder = getMessages();
		encoder.sendDetails();
		this.getMessages().sendMapRegion();
		super.getFlags().flag(UpdateFlag.APPEARANCE);
		Smelting.clearInterfaces(this);
		if((int) getAttr().get("introduction_stage").get() == 2) {
			sendDefaultSidebars();
		}
		move(super.getPosition());
		Skills.refreshAll(this);
		equipment.refresh(this, Equipment.EQUIPMENT_DISPLAY_ID);
		inventory.refresh(this, Inventory.INVENTORY_DISPLAY_ID);
		encoder.sendPrivateMessageListStatus(2);
		privateMessage.updateThisList();
		privateMessage.updateOtherList(true);
		encoder.sendContextMenu(3, false, "Follow");
		encoder.sendContextMenu(4, false, "Trade with");
		CombatEffect.values().forEach($it -> {
			if($it.onLogin(this))
				World.submit(new CombatEffectTask(this, $it));
		});
		World.getExchangeSessionManager().resetRequests(this);
		encoder.sendMessage(GameConstants.WELCOME_MESSAGE);
		if(UpdateCommand.inProgess) {
			encoder.sendMessage("@red@There is currently an update schedule in progress. You'll be kicked off soon.");
		}
		WeaponInterface.execute(this, equipment.get(Equipment.WEAPON_SLOT));
		WeaponAnimation.execute(this, equipment.get(Equipment.WEAPON_SLOT));
		ShieldAnimation.execute(this, equipment.get(Equipment.SHIELD_SLOT));
		encoder.sendConfig(173, super.getMovementQueue().isRunning() ? 0 : 1);
		encoder.sendConfig(174, super.isPoisoned() ? 1 : 0);
		encoder.sendConfig(172, super.isAutoRetaliate() ? 1 : 0);
		encoder.sendConfig(fightType.getParent(), fightType.getChild());
		encoder.sendConfig(427, ((Boolean) getAttr().get("accept_aid").get()) ? 0 : 1);
		encoder.sendConfig(108, 0);
		encoder.sendConfig(301, 0);
		encoder.sendString((int) runEnergy + "%", 149);
		encoder.sendRunEnergy();
		Prayer.VALUES.forEach(c -> encoder.sendConfig(c.getConfig(), 0));
		logger.info(this + " has logged in.");
		if(getFamiliar().isPresent())
			Summoning.summon(this, new Item(-1), true);
		if(getPetManager().getPet().isPresent()) {
			Pet.onLogin(this);
		}
		MinigameHandler.executeVoid(this, m -> m.onLogin(this));
		PlayerPanel.refreshAll(this);
		
		if(!clan.isPresent()) {
			World.getClanManager().join(this, "avro");
		}
		if((int) attr.get("introduction_stage").get() != 2) {
			activityManager.enable();
			new IntroductionCutscene(this).prerequisites();
		}
		if(World.getFirepitEvent().getFirepit().isActive()) {
			this.message("@red@[ANNOUNCEMENT]: Enjoy the double blood money event for another " + Utility.convertTime(World.getFirepitEvent().getFirepit().getTime()) + ".");
		}
		if(World.getShootingStarEvent().getShootingStar() != null && World.getRegions().getRegion(World.getShootingStarEvent().getShootingStar().getPosition()).getObject(World.getShootingStarEvent().getShootingStar().getId(), World.getShootingStarEvent().getShootingStar().getPosition()).isPresent()) {
			this.message("@red@[ANNOUNCEMENT]: " + World.getShootingStarEvent().getShootingStar().getLocationData().getMessageWhenActive());
		}
		if(Server.UPDATING > 0) {
			encoder.sendSystemUpdate((int) (Server.UPDATING * 50 / 30));
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Player) {
			Player other = (Player) obj;
			return getSlot() == other.getSlot() && getUsernameHash() == other.getUsernameHash();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getSlot(), getUsernameHash());
	}
	
	@Override
	public String toString() {
		return username == null ? session.toString() : "PLAYER[username= " + username + ", host= " + session.getHost() + ", rights= " + rights + "]";
	}
	
	@Override
	public void dispose() {
		setVisible(false);
		Pet.onLogout(this);
		Summoning.dismiss(this, -1, true);
		World.getExchangeSessionManager().reset(this);
		if(getMarketShop() != null)
			MarketShop.clearFromShop(this);
		World.getTaskManager().cancel(this);
		World.getExchangeSessionManager().reset(this);
		setSkillAction(Optional.empty());
		MinigameHandler.executeVoid(this, m -> m.onLogout(this));
		getPrivateMessage().updateOtherList(false);
		getClan().ifPresent(c -> c.getClan().remove(this, true));
		messages.sendLogout();
		save();
		getRegion().removeChar(this);
	}
	
	@Override
	public void sequence() {
		NpcAggression.sequence(this);
		restoreRunEnergy();
	}
	
	@Override
	public void appendDeath() {
		setDead(true);
		World.submit(new PlayerDeath(this));
	}
	
	@Override
	public Hit decrementHealth(Hit hit) {
		OutputMessages encoder = getMessages();
		if(hit.getDamage() > skills[Skills.HITPOINTS].getLevel()) {
			hit.setDamage(skills[Skills.HITPOINTS].getLevel());
			if(hit.getType() == Hit.HitType.CRITICAL) {
				hit.setType(Hit.HitType.NORMAL);
			}
		}
		skills[Skills.HITPOINTS].decreaseLevel(hit.getDamage());
		Skills.refresh(this, Skills.HITPOINTS);
		encoder.sendCloseWindows();
		if(getCurrentHealth() <= 0) {
			getSkills()[Skills.HITPOINTS].setLevel(0, false);
			if(!isDead()) {
				appendDeath();
			}
		}
		return hit;
	}
	
	@Override
	public CombatStrategy determineStrategy() {
		if(specialActivated && castSpell == null) {
			if(combatSpecial.getCombat() == CombatType.MELEE) {
				return Combat.newDefaultMeleeStrategy();
			} else if(combatSpecial.getCombat() == CombatType.RANGED) {
				return Combat.newDefaultRangedStrategy();
			} else if(combatSpecial.getCombat() == CombatType.MAGIC) {
				return Combat.newDefaultMagicStrategy();
			}
		}
		if(castSpell != null || autocastSpell != null) {
			return Combat.newDefaultMagicStrategy();
		}
		if(weapon == WeaponInterface.SHORTBOW || weapon == WeaponInterface.COMPOSITE_BOW || weapon == WeaponInterface.LONGBOW || weapon == WeaponInterface.CROSSBOW || weapon == WeaponInterface.DART || weapon == WeaponInterface.JAVELIN || weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.KNIFE || weapon == WeaponInterface.CHINCHOMPA || weapon == WeaponInterface.SALAMANDER) {
			return Combat.newDefaultRangedStrategy();
		}
		return Combat.newDefaultMeleeStrategy();
	}
	
	@Override
	public void onSuccessfulHit(EntityNode victim, CombatType type) {
		if(type == CombatType.MELEE || weapon == WeaponInterface.DART || weapon == WeaponInterface.KNIFE || weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.JAVELIN) {
			victim.poison(CombatPoisonEffect.getPoisonType(equipment.get(Equipment.WEAPON_SLOT)).orElse(null));
		} else if(type == CombatType.RANGED) {
			victim.poison(CombatPoisonEffect.getPoisonType(equipment.get(Equipment.ARROWS_SLOT)).orElse(null));
		}
	}
	
	@Override
	public int getAttackSpeed() {
		int speed = weapon.getSpeed();
		if(fightType == FightType.CROSSBOW_RAPID || fightType == FightType.SHORTBOW_RAPID || fightType == FightType.LONGBOW_RAPID || fightType == FightType.DART_RAPID || fightType == FightType.KNIFE_RAPID || fightType == FightType.THROWNAXE_RAPID || fightType == FightType.JAVELIN_RAPID) {
			speed--;
		} else if(fightType == FightType.CROSSBOW_LONGRANGE || fightType == FightType.SHORTBOW_LONGRANGE || fightType == FightType.LONGBOW_LONGRANGE || fightType == FightType.DART_LONGRANGE || fightType == FightType.KNIFE_LONGRANGE || fightType == FightType.THROWNAXE_LONGRANGE || fightType == FightType.JAVELIN_LONGRANGE) {
			speed++;
		}
		return speed;
	}
	
	@Override
	public int getCurrentHealth() {
		return skills[Skills.HITPOINTS].getLevel();
	}
	
	public int getMaximumHealth() {
		int hitpoints = skills[Skills.HITPOINTS].getRealLevel();
		if(Arrays.stream(new int[]{20135, 20147, 20159}).anyMatch(v -> equipment.contains(v))) {//nex helms
			hitpoints += 13;
		}
		if(Arrays.stream(new int[]{20139, 20151, 20163}).anyMatch(v -> equipment.contains(v))) {//nex bodies
			hitpoints += 20;
		}
		if(Arrays.stream(new int[]{20143, 20155, 20167}).anyMatch(v -> equipment.contains(v))) {//nex platelegs
			hitpoints += 7;
		}
		return hitpoints * 10;
	}
	
	@Override
	public int getBaseAttack(CombatType type) {
		if(type == CombatType.RANGED)
			return skills[Skills.RANGED].getLevel();
		else if(type == CombatType.MAGIC)
			return skills[Skills.MAGIC].getLevel();
		return skills[Skills.ATTACK].getLevel();
	}
	
	@Override
	public int getBaseDefence(CombatType type) {
		if(type == CombatType.MAGIC)
			return skills[Skills.MAGIC].getLevel();
		return skills[Skills.DEFENCE].getLevel();
	}
	
	@Override
	public void healEntity(int amount) {
		int level = skills[Skills.HITPOINTS].getRealLevel() * 10;
		skills[Skills.HITPOINTS].increaseLevel(amount, level);
		Skills.refresh(this, Skills.HITPOINTS);
	}
	
	/**
	 * Player being completely healed by a healer.
	 */
	public void heal() {
		int level = skills[Skills.HITPOINTS].getRealLevel() * 10;
		skills[Skills.HITPOINTS].setLevel(level, false);
		Skills.refresh(this, Skills.HITPOINTS);
		graphic(new Graphic(1684));
		if(isPoisoned())
			PotionConsumable.onAntiPoisonEffect(this, false, 0);
		message("You have been healed.");
	}
	
	@Override
	public boolean weaken(CombatWeaken effect) {
		OutputMessages encoder = getMessages();
		int id = (effect == CombatWeaken.ATTACK_LOW || effect == CombatWeaken.ATTACK_HIGH ? Skills.ATTACK : effect == CombatWeaken.STRENGTH_LOW || effect == CombatWeaken.STRENGTH_HIGH ? Skills.STRENGTH : Skills.DEFENCE);
		if(skills[id].getLevel() < skills[id].getRealLevel())
			return false;
		skills[id].decreaseLevel((int) ((effect.getRate()) * (skills[id].getLevel())));
		encoder.sendMessage("You feel slightly weakened.");
		return true;
	}
	
	/**
	 * Attempts to teleport to the {@code destination}.
	 * @param destination the destination to teleport to.
	 */
	public void teleport(Position destination) {
		DefaultTeleportSpell.startTeleport(this, destination);
	}
	
	/**
	 * Attempts to teleport to the {@code destination}.
	 * @param destination the destination to teleport to.
	 * @param type        the type which this player will be teleported on.
	 */
	public void teleport(Position destination, DefaultTeleportSpell.TeleportType type) {
		DefaultTeleportSpell.startTeleport(this, destination, type);
	}
	
	/**
	 * Moves this player to {@code position}.
	 * @param destination the position to move this player to.
	 */
	@Override
	public void move(Position destination) {
		OutputMessages encoder = getMessages();
		dialogueChain.interrupt();
		getMovementQueue().reset();
		encoder.sendCloseWindows();
		if(getRegion().getPosition() == getLastRegion())
			setUpdateRegion(false);
		if(getLastRegion() == null)
			setLastRegion(getPosition().copy());
		super.setPosition(destination.copy());
		setNeedsPlacement(true);
	}
	
	/**
	 * Saves the character file for this player.
	 */
	private void save() {
		World.getService().submit(() -> new PlayerSerialization(this).serialize());
	}
	
	/**
	 * Calculates and returns the combat level for this player.
	 * @return the combat level.
	 */
	public int determineCombatLevel() {
		int magLvl = skills[Skills.MAGIC].getRealLevel();
		int ranLvl = skills[Skills.RANGED].getRealLevel();
		int attLvl = skills[Skills.ATTACK].getRealLevel();
		int strLvl = skills[Skills.STRENGTH].getRealLevel();
		int defLvl = skills[Skills.DEFENCE].getRealLevel();
		int hitLvl = skills[Skills.HITPOINTS].getRealLevel();
		int prayLvl = skills[Skills.PRAYER].getRealLevel();
		double mag = magLvl * 1.5;
		double ran = ranLvl * 1.5;
		double attstr = attLvl + strLvl;
		double combatLevel = 0;
		if(ran > attstr && ran > mag) { // player is ranged class
			combatLevel = ((defLvl) * 0.25) + ((hitLvl) * 0.25) + ((prayLvl / 2) * 0.25) + ((ranLvl) * 0.4875);
		} else if(mag > attstr) { // player is mage class
			combatLevel = (((defLvl) * 0.25) + ((hitLvl) * 0.25) + ((prayLvl / 2) * 0.25) + ((magLvl) * 0.4875));
		} else {
			combatLevel = (((defLvl) * 0.25) + ((hitLvl) * 0.25) + ((prayLvl / 2) * 0.25) + ((attLvl) * 0.325) + ((strLvl) * 0.325));
		}
		return (int) combatLevel;
	}
	
	/**
	 * Sends wilderness and multi-combat interfaces as needed.
	 */
	public void sendInterfaces() {
		OutputMessages encoder = getMessages();
		if(Location.inDuelArena(this)) {
			if(!duelingContext) {
				encoder.sendContextMenu(2, false, "Challenge");
				duelingContext = true;
			}
		} else if(duelingContext && !minigame.isPresent()) {
			encoder.sendContextMenu(2, false, "null");
			duelingContext = false;
		}
		if(Location.inGodwars(this)) {
			if(!godwarsInterface) {
				GodwarsFaction.refreshInterface(this);
				encoder.sendWalkable(16210);
				godwarsInterface = true;
			}
		} else if(godwarsInterface) {
			encoder.sendWalkable(-1);
			godwarsInterface = false;
		}
		if(Location.inWilderness(this)) {
			int calculateY = this.getPosition().getY() > 6400 ? super.getPosition().getY() - 6400 : super.getPosition().getY();
			wildernessLevel = (((calculateY - 3520) / 8) + 1);
			if(!wildernessInterface) {
				encoder.sendWalkable(197);
				encoder.sendContextMenu(2, true, "Attack");
				wildernessInterface = true;
			}
			encoder.sendString("@yel@Level: " + wildernessLevel, 199);
		} else if(Location.inFunPvP(this)) {
			if(!wildernessInterface) {
				encoder.sendWalkable(197);
				encoder.sendContextMenu(2, true, "Attack");
				wildernessInterface = true;
			}
			encoder.sendString("@yel@Fun PvP", 199);
		} else if(wildernessInterface) {
			encoder.sendContextMenu(2, false, "null");
			encoder.sendWalkable(-1);
			wildernessInterface = false;
			wildernessLevel = 0;
		}
		if(Location.inMultiCombat(this)) {
			if(!multicombatInterface) {
				encoder.sendMultiIcon(false);
				multicombatInterface = true;
			}
		} else {
			encoder.sendMultiIcon(true);
			multicombatInterface = false;
		}
	}
	
	/**
	 * Restores run energy based on the last time it was restored.
	 */
	public void restoreRunEnergy() {
		OutputMessages encoder = getMessages();
		if(lastEnergy.elapsed(3500) && runEnergy < 100 && this.getMovementQueue().isMovementDone()) {
			double restoreRate = 0.45D;
			double agilityFactor = 0.01 * skills[Skills.AGILITY].getLevel();
			setRunEnergy(runEnergy + (restoreRate + agilityFactor));
			lastEnergy.reset();
			encoder.sendRunEnergy();
		}
	}
	
	/**
	 * Gets the formatted version of the username for this player.
	 * @return the formatted username.
	 */
	public String getFormatUsername() {
		return TextUtils.capitalize(username);
	}
	
	/**
	 * Determines whether this player is in nightmare mode.
	 * @return {@link #nightmareMode}.
	 */
	public boolean isNightmareMode() {
		return nightmareMode;
	}
	
	/**
	 * Sets the {@link #nightmareMode} to true.
	 */
	public void setNightmareMode() {
		this.nightmareMode = true;
	}
	
	/**
	 * Gets the hash collection of the local players.
	 * @return the local players.
	 */
	public Set<Player> getLocalPlayers() {
		return localPlayers;
	}
	
	/**
	 * Gets the hash collection of the local npcs.
	 * @return the local npcs.
	 */
	public Set<Npc> getLocalNpcs() {
		return localNpcs;
	}
	
	/**
	 * Gets the hash collection of friends.
	 * @return the friends list.
	 */
	public Set<Long> getFriends() {
		return friends;
	}
	
	/**
	 * Gets the hash collection of ignores.
	 * @return the ignores list.
	 */
	public Set<Long> getIgnores() {
		return ignores;
	}
	
	/**
	 * Gets the container that holds the inventory items.
	 * @return the container for the inventory.
	 */
	public Inventory getInventory() {
		return inventory;
	}
	
	/**
	 * Gets the container that holds the bank items.
	 * @return the container for the bank.
	 */
	public Bank getBank() {
		return bank;
	}
	
	/**
	 * Gets the container that holds the equipment items.
	 * @return the container for the equipment.
	 */
	public Equipment getEquipment() {
		return equipment;
	}
	
	/**
	 * Gets the private message manager that manages messages for this player.
	 * @return the private message manager.
	 */
	public PrivateMessage getPrivateMessage() {
		return privateMessage;
	}
	
	/**
	 * @return The {@link GameSession} assigned to this {@code Player}.
	 */
	public GameSession getSession() {
		return session;
	}
	
	/**
	 * Sets the value for {@link #session}.
	 */
	public void setSession(GameSession session) {
		checkState(this.session == null, "session already set!");
		this.session = session;
	}
	
	/**
	 * Gets the encoder that will encode and send messages.
	 * @return the message encoder.
	 */
	public OutputMessages getMessages() {
		return messages;
	}
	
	/**
	 * @return {@link #enterInputListener}.
	 */
	public Optional<Function<String, ActionListener>> getEnterInputListener() {
		return enterInputListener;
	}
	
	/**
	 * @param enterInputListener {@link #enterInputListener}.
	 */
	public void setEnterInputListener(Optional<Function<String, ActionListener>> enterInputListener) {
		this.enterInputListener = enterInputListener;
	}
	
	/**
	 * A shortcut function to {@link GameSession#queue(ByteMessage)}.
	 */
	public void queue(ByteMessage msg) {
		if(session != null)
			session.queue(msg);
	}
	
	/**
	 * Gets the array of the godwars killcount that can be increased by this player.
	 * @return the killcount that can be increased.
	 */
	public int[] getGodwarsKillcount() {
		return godwarsKillcount;
	}
	
	/**
	 * Sets the array of the godwars killcount.
	 * @param godwarsKillcount the array to set.
	 */
	public void setGodwarsKillcount(int[] godwarsKillcount) {
		this.godwarsKillcount = godwarsKillcount;
	}
	
	/**
	 * Gets the array of skills that can be trained by this player.
	 * @return the skills that can be trained.
	 */
	public Skill[] getSkills() {
		return skills;
	}
	
	/**
	 * Gets the array of booleans determining which prayers are active.
	 * @return the active prayers.
	 */
	public EnumSet<Prayer> getPrayerActive() {
		return prayerActive;
	}
	
	/**
	 * Returns the array determining which quick prayer ids are stored.
	 * @return the ids that have been stored.
	 */
	public List<Prayer> getQuickPrayers() {
		return quickPrayers;
	}
	
	/**
	 * Returns the array determining which quick prayer ids are stored.
	 * @return the ids that have been stored.
	 */
	public List<Prayer> getSelectedQuickPrayers() {
		return selectedQuickPrayers;
	}
	
	/**
	 * The current skill this player is training.
	 * @return {@link #action}.
	 */
	public Optional<SkillActionTask> getSkillActionTask() {
		return action;
	}
	
	/**
	 * Sets the skill action.
	 * @param action the action to set this skill action to.
	 */
	public void setSkillAction(SkillActionTask action) {
		this.action = Optional.of(action);
	}
	
	/**
	 * Sets the skill action.
	 * @param action the action to set this skill action to.
	 */
	public void setSkillAction(Optional<SkillActionTask> action) {
		this.action = action;
	}
	
	/**
	 * Gets the slashing stopwatch timer.
	 * @return the slashing timer.
	 */
	public Stopwatch getWebSlashingTimer() {
		return slashTimer;
	}
	
	/**
	 * Gets the eating stopwatch timer.
	 * @return the eating timer.
	 */
	public Stopwatch getEatingTimer() {
		return eatingTimer;
	}
	
	/**
	 * Gets the potion stopwatch timer.
	 * @return the potion timer.
	 */
	public Stopwatch getPotionTimer() {
		return potionTimer;
	}
	
	/**
	 * Gets the npc tolerance stopwatch timer.
	 * @return the tolerance timer.
	 */
	public Stopwatch getTolerance() {
		return tolerance;
	}
	
	/**
	 * Gets the last energy increment stopwatch timer.
	 * @return the last energy increment timer.
	 */
	public Stopwatch getLastEnergy() {
		return lastEnergy;
	}
	
	/**
	 * Gets the bone bury stopwatch timer.
	 * @return the bone bury timer.
	 */
	public Stopwatch getBuryTimer() {
		return buryTimer;
	}
	
	/**
	 * Gets the poison immunity counter value.
	 * @return the poison immunity counter.
	 */
	public MutableNumber getPoisonImmunity() {
		return poisonImmunity;
	}
	
	/**
	 * Gets the anti-fire details instance for this player.
	 * @return the {@link AntifireDetails} as an optional.
	 */
	public Optional<AntifireDetails> getAntifireDetails() {
		return antifireDetails;
	}
	
	/**
	 * Sets a new anti-fire instance for this class.
	 * @param details the anti-fire instance to set.
	 */
	public void setAntifireDetail(Optional<AntifireDetails> details) {
		this.antifireDetails = details;
	}
	
	/**
	 * Sets the new anti-fire instance for this class directly.
	 * @param details the anti-fire instance to set.
	 */
	public void setAntifireDetail(AntifireDetails details) {
		setAntifireDetail(Optional.of(details));
	}
	
	/**
	 * Gets the teleblock counter value.
	 * @return the teleblock counter.
	 */
	public MutableNumber getTeleblockTimer() {
		return teleblockTimer;
	}
	
	/**
	 * Gets the skull timer counter value.
	 * @return the skull timer counter.
	 */
	public MutableNumber getSkullTimer() {
		return skullTimer;
	}
	
	/**
	 * Sets the run energy percentage counter value.
	 * @return the new value to set.
	 */
	public void setRunEnergy(double energy) {
		boolean maxed = (this.runEnergy + energy) > 100;
		this.runEnergy = maxed ? 100 : energy;
		getMessages().sendString((int) runEnergy + "%", 149);
	}
	
	/**
	 * Gets the run energy percentage counter value.
	 * @return the run energy percentage counter.
	 */
	public double getRunEnergy() {
		return runEnergy;
	}
	
	/**
	 * Gets the special percentage counter value.
	 * @return the special percentage counter.
	 */
	public MutableNumber getSpecialPercentage() {
		return specialPercentage;
	}
	
	/**
	 * Returns the flag if the player has been muted.
	 * @return {@code true} if the player has been muted, {@code false} otherwise.
	 */
	public boolean isMuted() {
		return muted;
	}
	
	/**
	 * Sets the value for {@link Player#muted}.
	 * @param muted the new value to set.
	 */
	public void setMuted(boolean muted) {
		this.muted = muted;
	}
	
	/**
	 * Returns the flag if the player is venged.
	 * @return {@code true} if the player is venged, {@code false} otherwise.
	 */
	public boolean isVenged() {
		return venged;
	}
	
	/**
	 * Sets the value for {@link Player#venged}.
	 * @param venged the new value to set.
	 */
	public void setVenged(boolean venged) {
		this.venged = venged;
	}
	
	/**
	 * Returns the flag if the player has been banned.
	 * @return {@code true} if the player has been banned, {@code false} otherwise.
	 */
	public boolean isBanned() {
		return banned;
	}
	
	/**
	 * Sets the value for {@link Player#banned}.
	 * @param banned the new value to set.
	 */
	public void setBanned(boolean banned) {
		this.banned = banned;
	}
	
	/**
	 * Returns the flag if the player has been validated.
	 */
	public boolean isValidated() {
		return validated;
	}
	
	/**
	 * Sets the value for {@link Player#validated}.
	 * @param validated the new value to set.
	 */
	public boolean setValidated(boolean validated) {
		return this.validated = validated;
	}
	
	/**
	 * Gets the amount of authority this player has over others.
	 * @return the authority this player has.
	 */
	public Rights getRights() {
		return rights;
	}
	
	/**
	 * Sets the value for {@link Player#rights}.
	 * @param rights the new value to set.
	 */
	public void setRights(Rights rights) {
		this.rights = rights;
	}
	
	/**
	 * Gets the quest manager for this player.
	 */
	public QuestManager getQuestManager() {
		return quest_manager;
	}
	
	/**
	 * Gets the current username of this player.
	 * @return the username of this player.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the value for {@link Player#username}.
	 * @param username the new value to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Gets the current password of this player.
	 * @return the password of this player.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the value for {@link Player#password}.
	 * @param password the new value to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Gets the combat spell currently selected.
	 * @return the selected combat spell.
	 */
	public CombatSpell getCastSpell() {
		return castSpell;
	}
	
	/**
	 * Sets the value for {@link Player#castSpell}.
	 * @param castSpell the new value to set.
	 */
	public void setCastSpell(CombatSpell castSpell) {
		this.castSpell = castSpell;
	}
	
	/**
	 * Determines if the player is autocasting.
	 * @return {@code true} if they are autocasting, {@code false} otherwise.
	 */
	public boolean isAutocast() {
		return autocast;
	}
	
	/**
	 * Sets the value for {@link Player#autocast}.
	 * @param autocast the new value to set.
	 */
	public void setAutocast(boolean autocast) {
		this.autocast = autocast;
	}
	
	/**
	 * Gets the combat spell currently being autocasted.
	 * @return the autocast spell.
	 */
	public CombatSpell getAutocastSpell() {
		return autocastSpell;
	}
	
	/**
	 * Sets the value for {@link Player#autocastSpell}.
	 * @param autocastSpell the new value to set.
	 */
	public void setAutocastSpell(CombatSpell autocastSpell) {
		this.autocastSpell = autocastSpell;
	}
	
	/**
	 * Gets the combat special that has been activated.
	 * @return the activated combat special.
	 */
	public CombatSpecial getCombatSpecial() {
		return combatSpecial;
	}
	
	/**
	 * Sets the value for {@link Player#combatSpecial}.
	 * @param combatSpecial the new value to set.
	 */
	public void setCombatSpecial(CombatSpecial combatSpecial) {
		this.combatSpecial = combatSpecial;
	}
	
	/**
	 * Gets the ranged details for this player.
	 * @return the ranged details.
	 */
	public CombatRangedDetails getRangedDetails() {
		return rangedDetails;
	}
	
	/**
	 * Determines if the special bar has been activated.
	 * @return {@code true} if it has been activated, {@code false} otherwise.
	 */
	public boolean isSpecialActivated() {
		return specialActivated;
	}
	
	/**
	 * Sets the value for {@link Player#specialActivated}.
	 * @param specialActivated the new value to set.
	 */
	public void setSpecialActivated(boolean specialActivated) {
		this.specialActivated = specialActivated;
	}
	
	/**
	 * Gets the current fight type the player is using.
	 * @return the current fight type.
	 */
	public FightType getFightType() {
		return fightType;
	}
	
	/**
	 * Sets the value for {@link Player#fightType}.
	 * @param fightType the new value to set.
	 */
	public void setFightType(FightType fightType) {
		this.fightType = fightType;
	}
	
	/**
	 * Gets the weapon animation for appearance updating.
	 * @return the weapon animation.
	 */
	public WeaponAnimation getWeaponAnimation() {
		return weaponAnimation;
	}
	
	/**
	 * Gets the shield animation for appearance updating.
	 * @return the shield animation.
	 */
	public ShieldAnimation getShieldAnimation() {
		return shieldAnimation;
	}
	
	/**
	 * Sets the value for {@link Player#prayerBook}.
	 * @param prayerBook the new value to set.
	 */
	public void setPrayerBook(PrayerBook prayerBook) {
		this.prayerBook = prayerBook;
	}
	
	/**
	 * Gets the current prayer type used by the player.
	 * @return the player's prayer type.
	 */
	public PrayerBook getPrayerBook() {
		return prayerBook;
	}
	
	/**
	 * Sets the value for {@link Player#spellbook}.
	 * @param spellBook the new value to set.
	 */
	public void setSpellbook(Spellbook spellBook) {
		this.spellbook = spellBook;
	}
	
	/**
	 * Gets the current spellbook used by the player.
	 * @return the player's spellbook.
	 */
	public Spellbook getSpellbook() {
		return spellbook;
	}
	
	/**
	 * Sets the value for {@link Player#weaponAnimation}.
	 * @param weaponAnimation the new value to set.
	 */
	public void setWeaponAnimation(WeaponAnimation weaponAnimation) {
		this.weaponAnimation = weaponAnimation;
	}
	
	/**
	 * Sets the value for {@link Player#shieldAnimation}.
	 * @param shieldAnimation the new value to set.
	 */
	public void setShieldAnimation(ShieldAnimation shieldAnimation) {
		this.shieldAnimation = shieldAnimation;
	}
	
	/**
	 * Gets the task that handles combat prayer draining.
	 * @return the prayer drain task.
	 */
	public Task getPrayerDrain() {
		return prayerDrain;
	}
	
	/**
	 * Sets the value for {@link Player#prayerDrain}.
	 * @param prayerDrain the new value to set.
	 */
	public void setPrayerDrain(Task prayerDrain) {
		this.prayerDrain = prayerDrain;
	}
	
	/**
	 * Gets the wilderness level this player is in.
	 * @return the wilderness level.
	 */
	public int getWildernessLevel() {
		return wildernessLevel;
	}
	
	/**
	 * Sets the value for {@link Player#wildernessLevel}.
	 * @param wildernessLevel the new value to set.
	 */
	public void setWildernessLevel(int wildernessLevel) {
		this.wildernessLevel = wildernessLevel;
	}
	
	/**
	 * Gets the weapon interface this player currently has openShop.
	 * @return the weapon interface.
	 */
	public WeaponInterface getWeapon() {
		return weapon;
	}
	
	/**
	 * Sets the value for {@link Player#weapon}.
	 * @param weapon the new value to set.
	 */
	public void setWeapon(WeaponInterface weapon) {
		this.weapon = weapon;
	}
	
	/**
	 * Gets the current teleport stage that this player is in.
	 * @return the teleport stage.
	 */
	public int getTeleportStage() {
		return teleportStage;
	}
	
	/**
	 * Checks if the player is teleporting.
	 * @return <true> if the player is, <false> otherwise.
	 */
	public boolean isTeleporting() {
		return teleportStage > 0;
	}
	
	/**
	 * Sets the value for {@link Player#teleportStage}.
	 * @param teleportStage the new value to set.
	 */
	public void setTeleportStage(int teleportStage) {
		this.teleportStage = teleportStage;
	}
	
	/**
	 * Gets the familiar the player has summoned.
	 * @return a familiar wrapped in an optional, {@link Optional#empty()} otherwise.
	 */
	public Optional<Familiar> getFamiliar() {
		return familiar;
	}
	
	/**
	 * Sets the familiar the player has summoned.
	 * @param familiar the familiar to set.
	 */
	public void setFamiliar(Optional<Familiar> familiar) {
		this.familiar = familiar;
	}
	
	/**
	 * Gets the pet manager for this player.
	 * @return the pet manager.
	 */
	public PetManager getPetManager() {
		return petManager;
	}
	
	/**
	 * Gets the option value used for npc dialogues.
	 * @return the option value.
	 */
	public OptionDialogue.OptionType getOption() {
		return option;
	}
	
	/**
	 * Sets the value for {@link Player#option}.
	 * @param option the new value to set.
	 */
	public void setOption(OptionDialogue.OptionType option) {
		this.option = option;
	}
	
	/**
	 * Gets the identifier for the head icon of this player.
	 * @return the head icon.
	 */
	public int getHeadIcon() {
		return headIcon;
	}
	
	/**
	 * Sets the value for {@link Player#headIcon}.
	 * @param headIcon the new value to set.
	 */
	public void setHeadIcon(int headIcon) {
		this.headIcon = headIcon;
	}
	
	/**
	 * Gets the identifier for the skull icon of this player.
	 * @return the skull icon.
	 */
	public int getSkullIcon() {
		return skullIcon;
	}
	
	/**
	 * Sets the value for {@link Player#skullIcon}.
	 * @param skullIcon the new value to set.
	 */
	public void setSkullIcon(int skullIcon) {
		this.skullIcon = skullIcon;
	}
	
	/**
	 * Determines if a wilderness interface is present.
	 * @return {@code true} if a wilderness interface is present, {@code false}
	 * otherwise.
	 */
	public boolean isWildernessInterface() {
		return wildernessInterface;
	}
	
	/**
	 * Sets the value for {@link Player#wildernessInterface}.
	 * @param wildernessInterface the new value to set.
	 */
	public void setWildernessInterface(boolean wildernessInterface) {
		this.wildernessInterface = wildernessInterface;
	}
	
	/**
	 * Determines if a multicombat interface is present.
	 * @return {@code true} if a multicombat interface is present, {@code false}
	 * otherwise.
	 */
	public boolean isMulticombatInterface() {
		return multicombatInterface;
	}
	
	/**
	 * Sets the value for {@link Player#multicombatInterface}.
	 * @param multicombatInterface the new value to set.
	 */
	public void setMulticombatInterface(boolean multicombatInterface) {
		this.multicombatInterface = multicombatInterface;
	}
	
	/**
	 * @return the minigame the player is in, {@link Optional#empty()} otherwise.
	 */
	public Optional<Minigame> getMinigame() {
		return minigame;
	}
	
	/**
	 * @param minigame the minigame to set.
	 */
	public void setMinigame(Optional<Minigame> minigame) {
		this.minigame = minigame;
	}
	
	/**
	 * @param minigame the minigame to set.
	 */
	public void setMinigame(Minigame minigame) {
		setMinigame(Optional.of(minigame));
	}
	
	/**
	 * @return the minigame container.
	 */
	public MinigameContainer getMinigameContainer() {
		return minigameContainer;
	}
	
	/**
	 * Gets the array of chat text packed into bytes.
	 * @return the array of chat text.
	 */
	public byte[] getChatText() {
		return chatText;
	}
	
	/**
	 * Sets the value for {@link Player#chatText}.
	 * @param chatText the new value to set.
	 */
	public void setChatText(byte[] chatText) {
		this.chatText = chatText;
	}
	
	/**
	 * Gets the current chat color the player is using.
	 * @return the chat color.
	 */
	public int getChatColor() {
		return chatColor;
	}
	
	/**
	 * Sets the value for {@link Player#chatColor}.
	 * @param chatColor the new value to set.
	 */
	public void setChatColor(int chatColor) {
		this.chatColor = chatColor;
	}
	
	/**
	 * Gets the current chat effects the player is using.
	 * @return the chat effects.
	 */
	public int getChatEffects() {
		return chatEffects;
	}
	
	/**
	 * Sets the value for {@link Player#chatEffects}.
	 * @param chatEffects the new value to set.
	 */
	public void setChatEffects(int chatEffects) {
		this.chatEffects = chatEffects;
	}
	
	/**
	 * Gets the clan the player is in.
	 * @return the clan wrapped in an optional.
	 */
	public Optional<ClanMember> getClan() {
		return clan;
	}
	
	/**
	 * Sets clan the player is in.
	 * @param clan the clan to set.
	 */
	public void setClan(Optional<ClanMember> clan) {
		this.clan = clan;
	}
	
	/**
	 * Gets the player-npc identifier for updating.
	 * @return the player npc identifier.
	 */
	public int getPlayerNpc() {
		return playerNpc;
	}
	
	/**
	 * Sets the value for {@link Player#playerNpc}.
	 * @param playerNpc the new value to set.
	 */
	public void setPlayerNpc(int playerNpc) {
		this.playerNpc = playerNpc;
	}
	
	/**
	 * Gets the cached player update block for updating.
	 * @return the cached update block.
	 */
	public ByteMessage getCachedUpdateBlock() {
		return cachedUpdateBlock;
	}
	
	/**
	 * Sets the value for {@link Player#cachedUpdateBlock}.
	 * @param cachedUpdateBlock the new value to set.
	 */
	public void setCachedUpdateBlock(ByteMessage cachedUpdateBlock) {
		// Release reference to currently cached block, if we have one.
		if(this.cachedUpdateBlock != null) {
			this.cachedUpdateBlock.release();
		}
		
		// If we need to set a new cached block, retain a reference to it. Otherwise it means that the current block
		// reference was just being cleared.
		if(cachedUpdateBlock != null) {
			cachedUpdateBlock.retain();
		}
		this.cachedUpdateBlock = cachedUpdateBlock;
	}
	
	/**
	 * Gets the username hash for this player.
	 * @return the username hash.
	 */
	public long getUsernameHash() {
		return usernameHash;
	}
	
	/**
	 * @return the standIndex
	 */
	public final int getStandIndex() {
		return standIndex;
	}
	
	/**
	 * @param standIndex the standIndex to set
	 */
	public final void setStandIndex(int standIndex) {
		if(standIndex == -1) {
			standIndex = 0x328;
		}
		this.standIndex = standIndex;
	}
	
	/**
	 * @return the turnIndex
	 */
	public final int getTurnIndex() {
		return turnIndex;
	}
	
	/**
	 * @param turnIndex the turnIndex to set
	 */
	public final void setTurnIndex(int turnIndex) {
		if(turnIndex == -1) {
			turnIndex = 0x337;
		}
		this.turnIndex = turnIndex;
	}
	
	/**
	 * @return the walkIndex
	 */
	public final int getWalkIndex() {
		return walkIndex;
	}
	
	/**
	 * @param walkIndex the walkIndex to set
	 */
	public final void setWalkIndex(int walkIndex) {
		if(walkIndex == -1) {
			walkIndex = 0x333;
		}
		this.walkIndex = walkIndex;
	}
	
	/**
	 * @return the turn180Index
	 */
	public final int getTurn180Index() {
		return turn180Index;
	}
	
	/**
	 * @param turn180Index the turn180Index to set
	 */
	public final void setTurn180Index(int turn180Index) {
		if(turn180Index == -1) {
			turn180Index = 0x334;
		}
		this.turn180Index = turn180Index;
	}
	
	/**
	 * @return the turn90CWIndex
	 */
	public final int getTurn90CWIndex() {
		return turn90CWIndex;
	}
	
	/**
	 * @param turn90CWIndex the turn90CWIndex to set
	 */
	public final void setTurn90CWIndex(int turn90CWIndex) {
		if(turn90CWIndex == -1) {
			turn90CWIndex = 0x335;
		}
		this.turn90CWIndex = turn90CWIndex;
	}
	
	/**
	 * @return the turn90CCWIndex
	 */
	public final int getTurn90CCWIndex() {
		return turn90CCWIndex;
	}
	
	/**
	 * @param turn90CCWIndex the turn90CCWIndex to set
	 */
	public final void setTurn90CCWIndex(int turn90CCWIndex) {
		if(turn90CCWIndex == -1) {
			turn90CCWIndex = 0x336;
		}
		this.turn90CCWIndex = turn90CCWIndex;
	}
	
	/**
	 * @return the runIndex
	 */
	public final int getRunIndex() {
		return runIndex;
	}
	
	/**
	 * @param runIndex the runIndex to set
	 */
	public final void setRunIndex(int runIndex) {
		if(runIndex == -1) {
			runIndex = 0x338;
		}
		this.runIndex = runIndex;
	}
	
	/**
	 * Gets the current dialogue chain we are in.
	 * @return the dialogue chain.
	 */
	public DialogueBuilder getDialogueBuilder() {
		return dialogueChain;
	}
	
	/**
	 * Determines if the region has been updated this sequence.
	 * @return {@code true} if the region has been updated, {@code false}
	 * otherwise.
	 */
	public boolean isUpdateRegion() {
		return updateRegion;
	}
	
	/**
	 * Sets the value for {@link Player#updateRegion}.
	 * @param updateRegion the new value to set.
	 */
	public void setUpdateRegion(boolean updateRegion) {
		this.updateRegion = updateRegion;
	}
	
	/**
	 * Gets the current viewing orb that this player has openShop.
	 * @return the current viewing orb.
	 */
	public ViewingOrb getViewingOrb() {
		return viewingOrb;
	}
	
	/**
	 * Sets the value for {@link Player#viewingOrb}.
	 * @param viewingOrb the new value to set.
	 */
	public void setViewingOrb(ViewingOrb viewingOrb) {
		this.viewingOrb = viewingOrb;
	}
	
	/**
	 * Gets the container of appearance values for this player.
	 * @return the container of appearance values.
	 */
	public PlayerAppearance getAppearance() {
		return appearance;
	}
	
	/**
	 * Gets the disabled activities for this player.
	 * @return the disabled activity class.
	 */
	public ActivityManager getActivityManager() {
		return activityManager;
	}
	
	/**
	 * Determines if the player's window is on focus.
	 * @return {@code true} if the player's window is on focus, {@code false} otherwise.
	 */
	public boolean isFocused() {
		return focused;
	}
	
	/**
	 * Sets the value for {@link Player#focused}.
	 * @param focused the new value to set.
	 */
	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	/**
	 * Gets the stopwatch that will time logouts.
	 * @return the logout stopwatch.
	 */
	public Stopwatch getLogoutTimer() {
		return logoutTimer;
	}
	
	/**
	 * @return {@link #npcKills}.
	 */
	public MutableNumber getNpcKills() {
		return npcKills;
	}
	
	/**
	 * @return {@link #playerKills}.
	 */
	public MutableNumber getPlayerKills() {
		return playerKills;
	}
	
	/**
	 * @return {@link #npcDeaths}.
	 */
	public MutableNumber getDeathsByNpc() {
		return npcDeaths;
	}
	
	/**
	 * @return {@link #playerDeaths}.
	 */
	public MutableNumber getDeathsByPlayer() {
		return playerDeaths;
	}
	
	/**
	 * @return the currentKillstreak
	 */
	public MutableNumber getCurrentKillstreak() {
		return currentKillstreak;
	}
	
	/**
	 * @return {@link #highestKillstreak}.
	 */
	public MutableNumber getHighestKillstreak() {
		return highestKillstreak;
	}
	
	/**
	 * Updates the value for {@link Player#credits}.
	 * @param points the new value to update.
	 */
	public void updateSlayers(int points) {
		this.slayerPoints += points;
		PlayerPanel.SLAYER_POINTS.refresh(this, "@or2@ - Slayer points: @yel@" + slayerPoints);
		messages.sendString(slayerPoints + "", 252);
	}
	
	/**
	 * Gets the amount of credits the player has.
	 * @return the credit's amount.
	 */
	public int getSlayerPoints() {
		return slayerPoints;
	}
	
	/**
	 * The current slayer instance.
	 * @return {@link #slayer}.
	 */
	public Optional<Slayer> getSlayer() {
		return slayer;
	}
	
	/**
	 * Sets the slayer.
	 * @param slayer the slayer to set this current slayer to.
	 */
	public void setSlayer(Optional<Slayer> slayer) {
		this.slayer = slayer;
		PlayerPanel.SLAYER_TASK.refresh(this, "@or2@ - Slayer task: @yel@" + (slayer.isPresent() ? (slayer.get().getAmount() + " " + slayer.get().toString()) : "none"));
		getMessages().sendString(slayer.isPresent() ? (TextUtils.capitalize(slayer.get().getKey().toLowerCase() + " x " + slayer.get().getAmount())) : "none", 253);
	}
	
	/**
	 * Gets the list of blocked slayer tasks.
	 * @return a list of the blocked slayer tasks.
	 */
	public String[] getBlockedTasks() {
		return blockedTasks;
	}
	
	/**
	 * Sets the blocked slayer tasks.
	 * @return sets the blocked slayer tasks.
	 */
	public void setBlockedTasks(String[] blockedTasks) {
		this.blockedTasks = blockedTasks;
	}
	
	/**
	 * @return {@link #agility_bonus}.
	 */
	public AgilityCourseBonus getAgilityBonus() {
		return agility_bonus;
	}
	
	/**
	 * A shorter way of sending a player a message.
	 * @param text the text to send.
	 */
	public void message(String text) {
		getMessages().sendMessage(text);
	}
	
	/**
	 * Gets the exchange market result based on search if any.
	 * @return the exchange result instance.
	 */
	public MarketShop getMarketShop() {
		return marketShop;
	}
	
	/**
	 * Sets the new value for {@link #marketShop}.
	 * @param marketShop new value
	 */
	public void setMarketShop(MarketShop marketShop) {
		this.marketShop = marketShop;
	}
}
