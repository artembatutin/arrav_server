package net.edge.world.entity.actor.player;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.Application;
import net.edge.GameConstants;
import net.edge.content.PlayerPanel;
import net.edge.content.TabInterface;
import net.edge.content.newcombat.Combat;
import net.edge.content.object.cannon.Multicannon;
import net.edge.content.object.ViewingOrb;
import net.edge.content.achievements.Achievement;
import net.edge.content.clanchat.ClanManager;
import net.edge.content.clanchat.ClanMember;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.effect.CombatEffect;
import net.edge.content.combat.effect.CombatEffectTask;
import net.edge.content.combat.effect.CombatPoisonEffect;
import net.edge.content.combat.magic.CombatSpell;
import net.edge.content.combat.magic.CombatWeaken;
import net.edge.content.combat.ranged.CombatRangedDetails;
import net.edge.content.combat.special.CombatSpecial;
import net.edge.content.combat.strategy.Strategy;
import net.edge.content.combat.weapon.FightType;
import net.edge.content.combat.weapon.WeaponAnimation;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.content.commands.impl.UpdateCommand;
import net.edge.content.dialogue.Dialogue;
import net.edge.content.dialogue.DialogueBuilder;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.content.item.OverloadEffectTask;
import net.edge.content.item.PotionConsumable;
import net.edge.content.market.MarketShop;
import net.edge.content.minigame.Minigame;
import net.edge.content.minigame.MinigameContainer;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.item.pets.Pet;
import net.edge.content.item.pets.PetManager;
import net.edge.content.quest.QuestManager;
import net.edge.content.scene.impl.IntroductionCutscene;
import net.edge.content.object.star.ShootingStarManager;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.content.skill.action.SkillActionTask;
import net.edge.content.skill.agility.AgilityCourseBonus;
import net.edge.content.skill.construction.Construction;
import net.edge.content.skill.construction.House;
import net.edge.content.skill.farming.FarmingManager;
import net.edge.content.skill.farming.patch.Patch;
import net.edge.content.skill.farming.patch.PatchType;
import net.edge.content.object.pit.FirepitManager;
import net.edge.content.skill.prayer.Prayer;
import net.edge.content.skill.slayer.Slayer;
import net.edge.content.skill.smithing.Smelting;
import net.edge.content.skill.summoning.Summoning;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.content.trivia.TriviaTask;
import net.edge.content.wilderness.WildernessActivity;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.net.packet.out.*;
import net.edge.net.session.GameSession;
import net.edge.task.Task;
import net.edge.util.*;
import net.edge.world.Graphic;
import net.edge.world.Hit;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.EntityType;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.MobAggression;
import net.edge.world.entity.actor.mob.impl.gwd.GodwarsFaction;
import net.edge.world.entity.actor.player.assets.*;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.actor.update.UpdateFlag;
import net.edge.world.entity.item.container.impl.Bank;
import net.edge.world.entity.item.container.impl.Equipment;
import net.edge.world.entity.item.container.impl.Inventory;
import net.edge.world.entity.item.container.session.ExchangeSessionManager;
import net.edge.world.locale.Position;
import net.edge.world.locale.loc.Location;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkState;

/**
 * The character implementation that represents a node that is operated by an
 * actual person. This type of node functions solely through communication with
 * the client, by reading data from and writing data to non-blocking sockets.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class Player extends Actor {
	
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
	 * The player credentials on login.
	 */
	private final PlayerCredentials credentials;
	
	/**
	 * Experience lock.
	 */
	public boolean xpLock;
	
	/**
	 * Determines if this player is playing in iron man mode.
	 * 0 - not
	 * 1 - iron man mode
	 * 2 - iron man maxed
	 */
	private int ironMan;
	
	/**
	 * The total amount of times this player has voted.
	 */
	private int totalVotes;

	/**
	 * The total amount of tokens the player has donated for.
	 */
	private int totalDonated;

	/**
	 * Player's voting points.
	 */
	private int votePoints;
	
	/**
	 * The aggression tick timer to not check npc aggression each tick.
	 */
	private int aggressionTick;
	
	/**
	 * The last username that killed this player.
	 */
	private String lastKiller = "[NOBODY]";
	
	/**
	 * Uniquely spawned mob/npcs for this player.
	 */
	private final ObjectList<Mob> mobs = new ObjectArrayList<>();
	
	/**
	 * The hash collection of the local players.
	 */
	private final Set<Player> localPlayers = new LinkedHashSet<>(255);
	
	/**
	 * The hash collection of the local npcs.
	 */
	private final Set<Mob> localMobs = new LinkedHashSet<>(255);
	
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
	 * The cached achievement progress.
	 */
	private Object2IntArrayMap<Achievement> playerAchievements = new Object2IntArrayMap<Achievement>(Achievement.VALUES.size()) {
		{
			for(final Achievement achievement : Achievement.VALUES) {
				put(achievement, 0);
			}
		}
	};
	
	/**
	 * The cached player's farming patches progress.
	 */
	private Object2ObjectArrayMap<PatchType, Patch> playerPatches = new Object2ObjectArrayMap<>(PatchType.VALUES.size());
	
	/**
	 * The I/O manager that manages I/O operations for this player.
	 */
	private GameSession session;
	
	/**
	 * The godwars killcount that can be increased by this player.
	 */
	private int[] godwarsKillcount = new int[4];
	
	/**
	 * The overload effect for this player.
	 */
	private OverloadEffectTask overloadEffect;
	
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
	private final ObjectList<Prayer> quickPrayers = new ObjectArrayList<>();
	
	/**
	 * The array determining which quick prayers id are selected on the interface.
	 */
	private final ObjectList<Prayer> selectedQuickPrayers = new ObjectArrayList<>();
	
	/**
	 * Represents the class that holds functionality regarding completing agility obstacle laps.
	 */
	private final AgilityCourseBonus agility_bonus = new AgilityCourseBonus();
	
	/**
	 * The collection of stopwatches used for various timing operations.
	 */
	private final Stopwatch wildernessActivity = new Stopwatch().reset(), slashTimer = new Stopwatch().reset(), eatingTimer = new Stopwatch()
			.reset(), potionTimer = new Stopwatch().reset(), specRestorePotionTimer = new Stopwatch().reset(), tolerance = new Stopwatch(), lastEnergy = new Stopwatch()
			.reset(), buryTimer = new Stopwatch(), logoutTimer = new Stopwatch(), diceTimer = new Stopwatch();
	
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
	 * The amount of authority this player has over others.
	 */
	private Rights rights = Rights.PLAYER;
	
	/**
	 * The total amount of npcs this player has killed.
	 */
	private final MutableNumber npcKills = new MutableNumber();
	
	/**
	 * The total amount of players this player has killed.
	 */
	private final MutableNumber playerKills = new MutableNumber();
	
	/**
	 * The total amount of times this player died to a {@link Mob}
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
	 * The amount of pest points the player has.
	 */
	private int pestPoints;
	
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
	 * The weapon interface this player currently has.
	 */
	private WeaponInterface weapon = WeaponInterface.UNARMED;
	
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
	 * A map of interfaces texts.
	 */
	public final Int2ObjectArrayMap<String> interfaceTexts = new Int2ObjectArrayMap<>();
	
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
	private GameBuffer cachedUpdateBlock;
	
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
	 * The house instance of this player.
	 */
	private House house = new House(this);
	
	/**
	 * Flag determining if the player is a human.
	 */
	private final boolean human;
	
	/**
	 * The saved {@link Multicannon} instance.
	 */
	public Optional<Multicannon> cannon = Optional.empty();
	
	/**
	 * Creates a new {@link Player}.
	 */
	public Player(PlayerCredentials credentials, boolean human) {
		super(GameConstants.STARTING_POSITION, EntityType.PLAYER);
		this.credentials = credentials;
		this.human = human;
	}
	
	public PlayerCredentials getCredentials() {
		return credentials;
	}
	
	public void sendDefaultSidebars() {
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
		TabInterface.ATTACK.sendInterface(this, weapon.getId());
	}
	
	public void resetSidebars() {
		TabInterface.ATTACK.sendInterface(this, -1);
		TabInterface.CLAN_CHAT.sendInterface(this, -1);
		TabInterface.SKILL.sendInterface(this, -1);
		TabInterface.QUEST.sendInterface(this, -1);
		TabInterface.INVENTORY.sendInterface(this, -1);
		TabInterface.EQUIPMENT.sendInterface(this, -1);
		TabInterface.PRAYER.sendInterface(this, -1);
		TabInterface.MAGIC.sendInterface(this, -1);
		TabInterface.FRIEND.sendInterface(this, -1);
		TabInterface.IGNORE.sendInterface(this, -1);
		TabInterface.LOGOUT.sendInterface(this, -1);
		TabInterface.SETTING.sendInterface(this, -1);
		TabInterface.EMOTE.sendInterface(this, -1);
		TabInterface.ATTACK.sendInterface(this, -1);
	}
	
	@Override
	public void register() {
		setLastRegion(getPosition().copy());
		setUpdates(true, false);
		setUpdateRegion(true);
		write(new SendSlot());
		write(new SendMapRegion(getLastRegion().copy()));
		write(new SendCameraReset());
		super.getFlags().flag(UpdateFlag.APPEARANCE);
		Smelting.clearInterfaces(this);
		if(getAttr().get("introduction_stage").getInt() == 3) {
			sendDefaultSidebars();
		}
		move(super.getPosition());
		Skills.refreshAll(this);
		equipment.updateBulk();
		inventory.updateBulk();
		out(new SendPrivateMessageStatus(2));
		privateMessage.updateThisList();
		privateMessage.updateOtherList(true);
		out(new SendContextMenu(3, false, "Follow"));
		out(new SendContextMenu(4, false, "Trade with"));
		CombatEffect.values().forEach($it -> {
			if($it.onLogin(this))
				World.get().submit(new CombatEffectTask(this, $it));
		});
		ExchangeSessionManager.get().resetRequests(this);
		message(GameConstants.WELCOME_MESSAGE);
		if(UpdateCommand.inProgess == 1) {
			message("@red@There is currently an update schedule in progress. You'll be kicked off soon.");
		}
		WeaponInterface.execute(this, equipment.get(Equipment.WEAPON_SLOT));
		WeaponAnimation.execute(this, equipment.get(Equipment.WEAPON_SLOT));
		ShieldAnimation.execute(this, equipment.get(Equipment.SHIELD_SLOT));
		equipment.updateRange();
		out(new SendConfig(173, super.getMovementQueue().isRunning() ? 0 : 1));
		out(new SendConfig(174, super.isPoisoned() ? 1 : 0));
		out(new SendConfig(172, super.isAutoRetaliate() ? 1 : 0));
		out(new SendConfig(fightType.getParent(), fightType.getChild()));
		out(new SendConfig(427, ((Boolean) getAttr().get("accept_aid").get()) ? 0 : 1));
		out(new SendConfig(108, 0));
		out(new SendConfig(301, 0));
		text(149, (int) runEnergy + "%");
		out(new SendEnergy());
		Prayer.VALUES.forEach(c -> out(new SendConfig(c.getConfig(), 0)));
		logger.info(this + " has logged in.");
		if(getPetManager().getPet().isPresent()) {
			Pet.onLogin(this);
		}
		MinigameHandler.executeVoid(this, m -> m.onLogin(this));
		PlayerPanel.refreshAll(this);
		if(!clan.isPresent() && isHuman()) {
			ClanManager.get().clearOnLogin(this);
		}
		if(attr.get("introduction_stage").getInt() != 3 && isHuman()) {
			new IntroductionCutscene(this).prerequisites();
		}
		if(FirepitManager.get().getFirepit().isActive()) {
			this.message("@red@[ANNOUNCEMENT]: Enjoy the double experience event for another " + Utility.convertTime(FirepitManager.get().getFirepit().getTime()) + ".");
		}
		if(ShootingStarManager.get().getShootingStar() != null && ShootingStarManager.get().getShootingStar().isReg()) {
			this.message("@red@[ANNOUNCEMENT]: " + ShootingStarManager.get().getShootingStar().getLocationData().getMessageWhenActive());
		}
		TriviaTask.getBot().onLogin(this);
		if(Application.UPDATING > 0) {
			out(new SendUpdateTimer((int) (Application.UPDATING * 50 / 30)));
		}
		Summoning.login(this);
		FarmingManager.login(this);
		if(getRights().isStaff()) {
			World.get().setStaffCount(World.get().getStaffCount() + 1);
			PlayerPanel.STAFF_ONLINE.refreshAll("@or3@ - Staff online: @yel@" + World.get().getStaffCount());
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Player) {
			Player other = (Player) obj;
			return getSlot() == other.getSlot() && credentials.getUsernameHash() == other.credentials.getUsernameHash();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getSlot(), credentials.getUsernameHash());
	}
	
	@Override
	public String toString() {
		return getCredentials().getUsername() == null ? session.toString() : "PLAYER[username= " + getCredentials().getUsername() + ", host= " + session
				.getHost() + ", rights= " + rights + "]";
	}
	
	@Override
	public Set<Player> getLocalPlayers() {
		return localPlayers;
	}
	
	@Override
	public Set<Mob> getLocalMobs() {
		return localMobs;
	}
	
	@Override
	public boolean inMulti() {
		return multicombatInterface;
	}
	
	@Override
	public boolean inWilderness() {
		return wildernessInterface;
	}
	
	@Override
	public boolean active() {
		return getState() == EntityState.ACTIVE;
	}
	
	@Override
	public void dispose() {
		setVisible(false);
		Pet.onLogout(this);
		Construction.onLogout(this);
		Summoning.dismiss(this, true);
		ExchangeSessionManager.get().reset(this);
		if(getMarketShop() != null)
			MarketShop.clearFromShop(this);
		World.get().getTask().cancel(this);
		setSkillAction(Optional.empty());
		resetOverloadEffect(true);
		MinigameHandler.executeVoid(this, m -> m.onLogout(this));
		privateMessage.updateOtherList(false);
		clan.ifPresent(c -> c.getClan().remove(this, true));
		cannon.ifPresent(c -> c.pickup(true));
		WildernessActivity.leave(this);
		save();
	}
	
	@Override
	public void preUpdate() {
		getMovementQueue().sequence();
		getNewCombat().tick();
		MobAggression.sequence(this);
		restoreRunEnergy();
		int deltaX = getPosition().getX() - getLastRegion().getRegionX() * 8;
		int deltaY = getPosition().getY() - getLastRegion().getRegionY() * 8;
		if(deltaX < 16 || deltaX >= 88 || deltaY < 16 || deltaY > 88 || isNeedsRegionUpdate() || getTeleportStage() == -1) {
			setLastRegion(getPosition().copy());
			setUpdates(true, false);
			setUpdateRegion(true);
			write(new SendMapRegion(getLastRegion().copy()));
			if(getTeleportStage() == -1)
				setTeleportStage(0);
		}
		//if(getSession() != null)
		//	UpdateManager.prepare(this);
	}
	
	@Override
	public void update() {
		write(new SendPlayerUpdate());
		write(new SendMobUpdate());
		getSession().pollOutgoingMessages();
	}
	
	@Override
	public void postUpdate() {
		if(isHuman()) {
			getSession().flushQueue();
		}
		super.postUpdate();
		cachedUpdateBlock = null;
	}
	
	@Override
	public void appendDeath() {
		setDead(true);
		World.get().submit(new PlayerDeath(this));
	}
	
	@Override
	public Hit decrementHealth(Hit hit) {
		if(hit.getDamage() > skills[Skills.HITPOINTS].getLevel()) {
			hit.setDamage(skills[Skills.HITPOINTS].getLevel());
			if(hit.getType() == Hit.HitType.CRITICAL) {
				hit.setType(Hit.HitType.NORMAL);
			}
		}
		skills[Skills.HITPOINTS].decreaseLevel(hit.getDamage());
		Skills.refresh(this, Skills.HITPOINTS);
		closeWidget();
		if(getCurrentHealth() <= 0) {
			getSkills()[Skills.HITPOINTS].setLevel(0, false);
			if(!isDead()) {
				appendDeath();
			}
		}
		return hit;
	}
	
	@Override
	public Strategy determineStrategy() {
		if(specialActivated && castSpell == null) {
			if(combatSpecial.getCombat() == CombatType.MELEE) {
				return CombatUtil.newDefaultMeleeStrategy();
			} else if(combatSpecial.getCombat() == CombatType.RANGED) {
				return CombatUtil.newDefaultRangedStrategy();
			} else if(combatSpecial.getCombat() == CombatType.MAGIC) {
				return CombatUtil.newDefaultMagicStrategy();
			}
		}
		if(castSpell != null || autocastSpell != null) {
			return CombatUtil.newDefaultMagicStrategy();
		}
		if(weapon == WeaponInterface.SHORTBOW || weapon == WeaponInterface.COMPOSITE_BOW || weapon == WeaponInterface.LONGBOW || weapon == WeaponInterface.CROSSBOW || weapon == WeaponInterface.DART || weapon == WeaponInterface.JAVELIN || weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.KNIFE || weapon == WeaponInterface.CHINCHOMPA || weapon == WeaponInterface.SALAMANDER) {
			return CombatUtil.newDefaultRangedStrategy();
		}
		return CombatUtil.newDefaultMeleeStrategy();
	}
	
	@Override
	public void onSuccessfulHit(Actor victim, CombatType type) {
		if(type == CombatType.MELEE || weapon == WeaponInterface.DART || weapon == WeaponInterface.KNIFE || weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.JAVELIN) {
			victim.poison(CombatPoisonEffect.getPoisonType(equipment.get(Equipment.WEAPON_SLOT)).orElse(null));
		} else if(type == CombatType.RANGED) {
			victim.poison(CombatPoisonEffect.getPoisonType(equipment.get(Equipment.ARROWS_SLOT)).orElse(null));
		}
	}
	
	@Override
	public int getAttackDelay() {
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
		if(equipment.containsAny(20135, 20147, 20159)) {//nex helms
			hitpoints += 13;
		}
		if(equipment.containsAny(20139, 20151, 20163)) {//nex bodies
			hitpoints += 20;
		}
		if(equipment.containsAny(20143, 20155, 20167)) {//nex platelegs
			hitpoints += 7;
		}
		return hitpoints * 10;
	}
	
	@Override
	public void healEntity(int amount) {
		int level = skills[Skills.HITPOINTS].getRealLevel() * 10;
		skills[Skills.HITPOINTS].increaseLevel(amount, level);
		Skills.refresh(this, Skills.HITPOINTS);
	}
	
	@Override
	public void setAutoRetaliate(boolean autoRetaliate) {
		super.setAutoRetaliate(autoRetaliate);
		out(new SendConfig(172, super.isAutoRetaliate() ? 1 : 0));
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
		int id = (effect == CombatWeaken.ATTACK_LOW || effect == CombatWeaken.ATTACK_HIGH ? Skills.ATTACK : effect == CombatWeaken.STRENGTH_LOW || effect == CombatWeaken.STRENGTH_HIGH ? Skills.STRENGTH : Skills.DEFENCE);
		if(skills[id].getLevel() < skills[id].getRealLevel())
			return false;
		skills[id].decreaseLevel((int) ((effect.getRate()) * (skills[id].getLevel())));
		message("You feel slightly weakened.");
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
		dialogueChain.interrupt();
		getMovementQueue().reset();
		closeWidget();
		getRegion().ifPresent(prev -> {
			if(prev.getPosition() == getLastRegion())
				setUpdateRegion(false);
		});
		if(getLastRegion() == null)
			setLastRegion(getPosition().copy());
		super.setPosition(destination.copy());
		setNeedsPlacement(true);
	}
	
	/**
	 * Sends a delayed task for this player.
	 */
	public void task(int delay, Consumer<Player> action) {
		Player p = this;
		new Task(delay, false) {
			@Override
			protected void execute() {
				action.accept(p);
				cancel();
			}
		}.submit();
	}
	
	/**
	 * Saves the character file for this player.
	 */
	private void save() {
		if(isHuman())
			new PlayerSerialization(this).serialize();
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
		if(Location.inDuelArena(this)) {
			if(!duelingContext) {
				out(new SendContextMenu(2, false, "Challenge"));
				duelingContext = true;
			}
		} else if(duelingContext && !minigame.isPresent()) {
			out(new SendContextMenu(2, false, "null"));
			duelingContext = false;
		}
		if(Location.inGodwars(this)) {
			if(!godwarsInterface) {
				GodwarsFaction.refreshInterface(this);
				out(new SendWalkable(16210));
				godwarsInterface = true;
			}
		} else if(godwarsInterface) {
			out(new SendWalkable(-1));
			godwarsInterface = false;
		}
		if(Location.inWilderness(this)) {
			int calculateY = this.getPosition().getY() > 6400 ? super.getPosition().getY() - 6400 : super.getPosition().getY();
			wildernessLevel = (((calculateY - 3520) / 8) + 1);
			if(!wildernessInterface) {
				out(new SendWalkable(197));
				out(new SendContextMenu(2, true, "Attack"));
				wildernessInterface = true;
				WildernessActivity.enter(this);
			}
			text(199, "@yel@Level: " + wildernessLevel);
		} else if(Location.inFunPvP(this)) {
			if(!wildernessInterface) {
				out(new SendWalkable(197));
				out(new SendContextMenu(2, true, "Attack"));
				wildernessInterface = true;
				WildernessActivity.enter(this);
			}
			text(199, "@yel@Fun PvP");
		} else if(wildernessInterface) {
			out(new SendContextMenu(2, false, "null"));
			out(new SendWalkable(-1));
			wildernessInterface = false;
			wildernessLevel = 0;
			WildernessActivity.leave(this);
		}
		if(Location.inMultiCombat(this)) {
			if(!multicombatInterface) {
				out(new SendMultiIcon(false));
				multicombatInterface = true;
			}
		} else {
			out(new SendMultiIcon(true));
			multicombatInterface = false;
		}
	}
	
	/**
	 * Restores run energy based on the last time it was restored.
	 */
	public void restoreRunEnergy() {
		if(lastEnergy.elapsed(3500) && runEnergy < 100 && (this.getMovementQueue().isMovementDone() || !getMovementQueue().isRunning())) {
			double restoreRate = 0.45D;
			double agilityFactor = 0.01 * skills[Skills.AGILITY].getLevel();
			setRunEnergy(runEnergy + (restoreRate + agilityFactor));
			lastEnergy.reset();
			out(new SendEnergy());
		}
	}
	
	/**
	 * Gets the formatted version of the username for this player.
	 * @return the formatted username.
	 */
	public String getFormatUsername() {
		return TextUtils.capitalize(getCredentials().getUsername());
	}
	
	/**
	 * Gets this player iron man mode stage.
	 * @return {@link #ironMan}.
	 */
	public int getIronMan() {
		return ironMan;
	}
	
	/**
	 * Determines whether this player is in the iron man mode.
	 * @return {@link #ironMan}.
	 */
	public boolean isIronMan() {
		return ironMan == 1 || ironMan == 2;
	}
	
	/**
	 * Determines whether this player is maxed out in the iron man mode.
	 * @return {@link #ironMan} maxed out.
	 */
	public boolean isIronMaxed() {
		return ironMan == 2;
	}
	
	/**
	 * Sets the {@link #ironMan} to the new value.
	 */
	public void setIron(int value, boolean update) {
		this.ironMan = value;
		if(update)
			PlayerPanel.IRON.refresh(this, "@or3@ - Iron man: @yel@" + (value == 0 ? "@red@no" : "@gre@yes"));
	}
	
	/**
	 * Gets the total amount of times this player has voted.
	 * @return total amount of votes.
	 */
	public int getTotalVotes() {
		return totalVotes;
	}

	/**
	 * Gets the total amount of donations for the player.
	 * @param dollars	whether you want to determine howmany dollars or tokens the player has purchased.
	 * @return the amount.
	 */
	public int getTotalDonated(boolean dollars) {
		return dollars ? totalDonated / 100 : totalDonated;
	}

	public void setTotalDonated(int amount) {
		this.totalDonated = amount;
	}

	/**
	 * Increases the total donate for this player.
	 * @param tokens	the amount of tokens to increase with.
	 */
	public void increaseTotalDonated(int tokens) {
		this.totalDonated += tokens;
	}

	/**
	 * Sets the total amount of times this player has voted.
	 * @param vote the amount to set.
	 */
	public void setTotalVotes(int vote) {
		this.totalVotes += vote;
		PlayerPanel.TOTAL_VOTES.refresh(this, "@or2@ - Total votes: @yel@" + this.getTotalVotes());
	}
	
	/**
	 * Gets the amount of vote points this player has.
	 * @return votes
	 */
	public int getVotePoints() {
		return votePoints;
	}
	
	/**
	 * Sets the vote point amount for this player.
	 */
	public void setVotePoints(int vote) {
		this.votePoints += vote;
		PlayerPanel.VOTE.refresh(this, "@or3@ - Vote points: @yel@" + this.getVotePoints() + " points", true);
	}
	
	public String getLastKiller() {
		return lastKiller;
	}
	
	public void setLastKiller(String lastKiller) {
		this.lastKiller = lastKiller;
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
	 * Queues a {@link OutgoingPacket}.
	 * @param packet packet to be queued.
	 */
	public void out(OutgoingPacket packet) {
		if(packet.onSent(this))
			getSession().equeue(packet);
	}
	
	/**
	 * Writes a {@link OutgoingPacket}.
	 * @param packet packet to be written.
	 */
	public void write(OutgoingPacket packet) {
		getSession().write(packet);
	}
	
	/**
	 * Opens up a new interface.
	 */
	public void widget(int widget) {
		out(new SendInterface(widget));
	}
	
	/**
	 * Opens up a new chat interface.
	 */
	public void chatWidget(int widget) {
		out(new SendChatInterface(widget));
	}
	
	/**
	 * Closes player interface.
	 */
	public void closeWidget() {
		out(new SendCloseInterface());
	}
	
	/**
	 * A shorter way of sending a player string on interface.
	 * @param message the text to send.
	 */
	public void text(int id, String message, boolean skipCheck) {
		if(!skipCheck && interfaceTexts.containsKey(id)) {
			if(interfaceTexts.get(id).equals(message)) {
				return;
			}
		}
		interfaceTexts.put(id, message);
		out(new SendText(id, message));
	}
	
	public void text(int id, String message) {
		text(id, message, false);
	}
	
	/**
	 * A shorter way of sending a player a message.
	 * @param message the text to send.
	 */
	public void message(String message) {
		out(new SendMessage(message));
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
	 * Gets the overload effect if there is any.
	 * @return the overload effect.
	 */
	public OverloadEffectTask getOverloadEffect() {
		return overloadEffect;
	}
	
	/**
	 * Applies the overload effect for the specified player.
	 */
	public void applyOverloadEffect() {
		OverloadEffectTask effect = new OverloadEffectTask(this);
		overloadEffect = effect;
		effect.submit();
	}
	
	/**
	 * Resets the overload effect.
	 */
	public void resetOverloadEffect(boolean stopTask) {
		if(overloadEffect != null) {
			if(overloadEffect.isRunning() && stopTask) {
				overloadEffect.cancel();
			}
			
			overloadEffect = null;
		}
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
	 * Gets the activity watcher stopwatch timer.
	 * @return the activity timer.
	 */
	public Stopwatch getWildernessActivity() {
		return wildernessActivity;
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
	 * Gets the special attack restore timer.
	 * @return the special attack restore timer.
	 */
	public Stopwatch getSpecialAttackRestoreTimer() {
		return specRestorePotionTimer;
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
	
	public Stopwatch getDiceTimer() {
		return diceTimer;
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
		this.runEnergy = energy > 100 ? 100 : energy;
		text(149, (int) runEnergy + "%");
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
	public GameBuffer getCachedUpdateBlock() {
		return cachedUpdateBlock;
	}
	
	/**
	 * Sets the value for {@link Player#cachedUpdateBlock}.
	 * @param cachedUpdateBlock the new value to set.
	 */
	public void setCachedUpdateBlock(GameBuffer cachedUpdateBlock) {
		this.cachedUpdateBlock = cachedUpdateBlock;
	}
	
	/**
	 * Getting the {@Link #human} flag.
	 * @return human flag.
	 */
	public boolean isHuman() {
		return human;
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
	 * Gets the achievements for the player.
	 * @return the map containing the player achievements.
	 */
	public Object2IntArrayMap<Achievement> getAchievements() {
		return playerAchievements;
	}
	
	/**
	 * Gets the farming patches for the player.
	 * @return the map containing the player patches.
	 */
	public Object2ObjectArrayMap<PatchType, Patch>  getPatches() {
		return playerPatches;
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
	 * Updates the value for {@link Player#pestPoints}.
	 * @param points the new value to update.
	 */
	public void updatePest(int points) {
		this.pestPoints += points;
		PlayerPanel.PEST_POINTS.refresh(this, "@or2@ - Pest points: @yel@" + pestPoints);
	}
	
	/**
	 * Gets the amount of pest points the player has.
	 * @return the pest points amount.
	 */
	public int getPest() {
		return pestPoints;
	}
	
	/**
	 * Updates the value for {@link Player#slayerPoints}.
	 * @param points the new value to update.
	 */
	public void updateSlayers(int points) {
		this.slayerPoints += points;
		PlayerPanel.SLAYER_POINTS.refresh(this, "@or2@ - Slayer points: @yel@" + slayerPoints, true);
	}
	
	/**
	 * Gets the amount of slayer points the player has.
	 * @return the slayer points amount.
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
		PlayerPanel.SLAYER_TASK.refresh(this, "@or2@ - Slayer task: @yel@" + (slayer.isPresent() ? (slayer.get()
				.getAmount() + " " + slayer.get().toString()) : "none"));
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
	 * Sends a single dialogue, this method is used for ease.
	 * @param dialogue the dialogue to send.
	 */
	public void dialogue(Dialogue dialogue) {
		DialogueAppender ap = new DialogueAppender(this);
		ap.chain(dialogue);
		ap.start();
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
	
	/**
	 * Gets the house instance of this player.
	 * @return house instance.
	 */
	public House getHouse() {
		return house;
	}
	
	/**
	 * Sets the house instance of this player.
	 * @param house this player's house instance.
	 */
	public void setHouse(House house) {
		this.house = house;
	}
	
	/**
	 * Gets the list of spawned npcs specifically for this player.
	 * @return mobs list.
	 */
	public ObjectList<Mob> getMobs() {
		return mobs;
	}
	
	public int processAgressiveTick() {
		aggressionTick++;
		if(aggressionTick == 5) {
			aggressionTick = 0;
		}
		return aggressionTick;
	}
	
	public void setAggressionTick(int aggressionTick) {
		this.aggressionTick = aggressionTick;
	}

	private final Combat<Player> newCombat = new Combat<>(this);

	@Override
	public Combat<Player> getNewCombat() {
		return newCombat;
	}

	@Override
	public int getBonus(int index) {
		return getEquipment().getBonuses()[index];
	}

	@Override
	public void appendBonus(int index, int bonus) {
		getEquipment().getBonuses()[index] += bonus;
	}

	@Override
	public int getSkillLevel(int skill) {
		return skills[skill].getLevel();
	}

}
