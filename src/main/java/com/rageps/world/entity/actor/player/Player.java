package com.rageps.world.entity.actor.player;

import com.google.common.collect.ImmutableMap;
import com.rageps.GameConstants;
import com.rageps.RagePS;
import com.rageps.combat.strategy.CombatStrategy;
import com.rageps.combat.strategy.PlayerWeaponStrategyManager;
import com.rageps.combat.strategy.player.special.CombatSpecial;
import com.rageps.command.impl.UpdateCommand;
import com.rageps.content.PlayerPanel;
import com.rageps.content.ShieldAnimation;
import com.rageps.content.TabInterface;
import com.rageps.content.achievements.Achievement;
import com.rageps.content.clanchannel.channel.ClanChannel;
import com.rageps.content.clanchannel.content.ClanViewer;
import com.rageps.content.dialogue.Dialogue;
import com.rageps.content.dialogue.DialogueBuilder;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.test.DialogueAppender;
import com.rageps.content.item.OverloadEffectTask;
import com.rageps.content.item.PotionConsumable;
import com.rageps.content.item.pets.Pet;
import com.rageps.content.item.pets.PetManager;
import com.rageps.content.market.MarketShop;
import com.rageps.content.minigame.Minigame;
import com.rageps.content.minigame.MinigameContainer;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.content.object.BarChair;
import com.rageps.content.object.ViewingOrb;
import com.rageps.content.object.cannon.Multicannon;
import com.rageps.content.object.pit.FirepitManager;
import com.rageps.content.object.star.ShootingStarManager;
import com.rageps.content.quest.QuestManager;
import com.rageps.content.skill.Skill;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.action.SkillActionTask;
import com.rageps.content.skill.agility.AgilityCourseBonus;
import com.rageps.content.skill.construction.Construction;
import com.rageps.content.skill.construction.House;
import com.rageps.content.skill.farming.FarmingManager;
import com.rageps.content.skill.farming.patch.Patch;
import com.rageps.content.skill.farming.patch.PatchType;
import com.rageps.content.skill.magic.Spellbook;
import com.rageps.content.skill.prayer.Prayer;
import com.rageps.content.skill.prayer.PrayerBook;
import com.rageps.content.skill.prayer.curses.CurseManager;
import com.rageps.content.skill.slayer.Slayer;
import com.rageps.content.skill.smithing.Smelting;
import com.rageps.content.skill.summoning.Summoning;
import com.rageps.content.skill.summoning.familiar.Familiar;
import com.rageps.content.teleport.TeleportType;
import com.rageps.content.teleport.impl.DefaultTeleportSpell;
import com.rageps.content.trivia.TriviaTask;
import com.rageps.content.wilderness.WildernessActivity;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.refactor.packet.Packet;
import com.rageps.net.refactor.packet.out.model.*;
import com.rageps.net.refactor.session.impl.GameSession;
import com.rageps.net.sql.forum.account.MultifactorAuthentication;
import com.rageps.task.Task;
import com.rageps.util.*;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.World;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.EntityType;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.Combat;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.combat.effect.CombatEffect;
import com.rageps.world.entity.actor.combat.effect.CombatEffectTask;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.combat.magic.CombatSpell;
import com.rageps.world.entity.actor.combat.ranged.RangedAmmunition;
import com.rageps.world.entity.actor.combat.ranged.RangedWeaponDefinition;
import com.rageps.world.entity.actor.combat.weapon.WeaponAnimation;
import com.rageps.world.entity.actor.combat.weapon.WeaponInterface;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.mob.MobAggression;
import com.rageps.world.entity.actor.mob.MobSpawner;
import com.rageps.world.entity.actor.mob.drop.chance.NpcDropChanceHandler;
import com.rageps.world.entity.actor.player.assets.PlayerData;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.actor.player.assets.group.ExperienceRate;
import com.rageps.world.entity.actor.player.assets.group.GameMode;
import com.rageps.world.entity.actor.player.assets.relations.PlayerRelation;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.entity.item.container.impl.Bank;
import com.rageps.world.entity.item.container.impl.Equipment;
import com.rageps.world.entity.item.container.impl.Inventory;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;
import com.rageps.world.entity.item.container.session.test._ExchangeSessionManager;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Position;
import com.rageps.world.locale.loc.Locations;
import com.rageps.world.model.Graphic;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkState;

/**
 * The character implementation that represents a node that is operated by an
 * actual person. This type of node functions solely through communication with
 * the client, by reading data from and writing data to non-blocking sockets.
 * @author Artem Batutin
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
	 * The players {@link MultifactorAuthentication} secret.
	 */
	private String mfaSecret;

	/**
	 * The player credentials on login.
	 */
	public final PlayerCredentials credentials;
	
	/**
	 * The initial map update flag.
	 */
	public final AtomicBoolean initialUpdate = new AtomicBoolean();
	
	/**
	 * The combat instance of this {@link Player}.
	 */
	private final Combat<Player> combat = new Combat<>(this);
	
	/**
	 * The exchange session manager of this {@link Player}.
	 */
	public final _ExchangeSessionManager exchange_manager = new _ExchangeSessionManager(this);

	/**
	 * Whether or not he player has finished saving.
	 */
	public final AtomicBoolean saved = new AtomicBoolean(false);


	/**
	 * Determines if this player is playing in iron man mode.
	 * 0 - not
	 * 1 - iron man mode
	 * 2 - iron man maxed
	 */
	private int ironMan;


	public int wildernessLevel;
	public double weight;

	public int headIcon = -1, skullIcon = -1;
	public int votePoints, totalVotes, totalDonated, aggressionTick;
	private boolean specialActivated, updateRegion;
	public boolean banned, muted, ipMuted, autocasting, screenFocus, firstLogin;
	public BarChair sitting;
	public String lastKiller = null;

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
	 * The players InterfaceManager for handling, and managing their interfaces.
	 */
	private final InterfaceManager interfaceManager = new InterfaceManager(this);
	
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
//	private final PrivateMessage privateMessage = new PrivateMessage(this);

	/**
	 * Handles all relations with players like friends/ignore/clan options.
	 */
	public final PlayerRelation relations = new PlayerRelation(this);

	/**
	 * Handles interactions with clan interfaces.
	 */
	public final ClanViewer clanViewer = new ClanViewer(this);

	/**
	 * The dialogue builder for this player.
	 */
	private final DialogueBuilder dialogueChain = new DialogueBuilder(this);
	
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
	 * The container of appearance values for this player.
	 */
	private PlayerAppearance appearance = new PlayerAppearance();
	
	/**
	 * The flag that determines if this player is disabled.
	 */
	private final ActivityManager activityManager = new ActivityManager(this);
	
	/**
	 * The container class which holds functions of values which should be modified.
	 */
	private final MinigameContainer minigameContainer = new MinigameContainer();
	
	/**
	 * A map of interfaces texts.
	 */
	public final Int2ObjectArrayMap<String> interfaceTexts = new Int2ObjectArrayMap<>();
	
	/**
	 * The pet that this player has spawned.
	 */
	private final PetManager petManager = new PetManager(this);
	
	/**
	 * The array of skills that can be trained by this player.
	 */
	private Skill[] skills = new Skill[25];
	
	/**
	 * The I/O manager that manages I/O operations for this player.
	 */
	private GameSession session;
	
	/**
	 * The overload effect for this player.
	 */
	private OverloadEffectTask overloadEffect;
	
	/**
	 * The curse effect manager of this player.
	 */
	public CurseManager curseManager = new CurseManager();

	/**
	 * A utility for spawning npcs easily using a command.
	 */
	private final MobSpawner mobSpawner = new MobSpawner(this);

	/**
	 * Immutable map of different stopwatches regarding consumables..
	 */
	public final ImmutableMap<String, Stopwatch> consumeDelay = ImmutableMap.of("SPECIAL", new Stopwatch().reset(), "FOOD", new Stopwatch().reset(), "DRINKS", new Stopwatch().reset());
	
	/**
	 * The collection of stopwatches used for various timing operations.
	 */
	private final Stopwatch wildernessActivity = new Stopwatch().reset(), slashTimer = new Stopwatch().reset(), specRestorePotionTimer = new Stopwatch().reset(), tolerance = new Stopwatch(), lastEnergy = new Stopwatch().reset(), buryTimer = new Stopwatch(), logoutTimer = new Stopwatch(), diceTimer = new Stopwatch();

	/**
	 * A data class designed to be saved as json for less important things.
	 */
	public  PlayerData playerData = new PlayerData();

	/**
	 * Leech delay stopwatch.
	 * todo - implement
	 */
	public final Stopwatch leechDelay = new Stopwatch().reset();

	public final Stopwatch magicOnItemDelay = new Stopwatch().reset();


	
	/**
	 * The enter input listener which will execute code when a player submits an input.
	 */
	private Optional<Function<String, ActionListener>> enterInputListener = Optional.empty();
	
	/**
	 * The amount of authority this player has over others.
	 */
	private Rights rights = Rights.PLAYER;
	
	/**
	 * The combat special that has been activated.
	 */
	private CombatSpecial combatSpecial;
	
	/**
	 * The spell that has been selected to auto-cast.
	 */
	private CombatSpell autocastSpell;
	
	/**
	 * The spell that has been selected to single-cast.
	 */
	private CombatSpell singleCast;
	
	/**
	 * The current viewing orb that this player has openShop.
	 */
	private ViewingOrb viewingOrb;
	
	/**
	 * The current skill action that is going on for this player.
	 */
	private Optional<SkillActionTask> action = Optional.empty();
	
	/**
	 * The weapon animation for appearance updating.
	 */
	private WeaponAnimation weaponAnimation;
	
	/**
	 * The shield animation for appearance updating.
	 */
	private ShieldAnimation shieldAnimation;
	
	/**
	 * The task that handles combat prayer draining.
	 */
	private Task prayerDrain = null;
	
	/**
	 * The weapon interface this player currently has.
	 */
	private WeaponInterface weapon = WeaponInterface.UNARMED;
	
	/**
	 * The familiar this player has spawned.
	 */
	private Optional<Familiar> familiar = Optional.empty();
	
	/**
	 * The option value used for npc dialogues.
	 */
	private OptionDialogue.OptionType option;
	
	/**
	 * The current minigame this player is in.
	 */
	private Optional<Minigame> minigame = Optional.empty();
	
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
	//private Optional<ClanMember> clan = Optional.empty();


	public String lastClan = "";
	public ClanChannel clanChannel;
	public String clan = "";
	public String clanTag = "";
	public String clanTagColor = "";
	
	/**
	 * The player-npc identifier for updating.
	 */
	private int playerNpc = -1;
	
	/**
	 * The cached player update block for updating.
	 */
	private GamePacket cachedUpdateBlock;
	
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
	 * The result of his search in the market.
	 */
	private MarketShop marketShop;
	
	/**
	 * The house instance of this player.
	 */
	private House house = new House(this);
	
	/**
	 * The saved {@link Multicannon} instance.
	 */
	public Optional<Multicannon> cannon = Optional.empty();
	
	/**
	 * The ranged weapon definition of the player.
	 */
	public RangedWeaponDefinition rangedDefinition;
	
	/**
	 * The ranged ammunition definition of the player.
	 */
	public RangedAmmunition rangedAmmo;
	
	/**
	 * The cached achievement progress.
	 */
	public Object2IntArrayMap<Achievement> achievements = new Object2IntArrayMap<Achievement>(Achievement.VALUES.size()) {
		{
			for(final Achievement achievement : Achievement.VALUES) {
				put(achievement, 0);
			}
		}
	};
	
	/**
	 * The cached player's farming patches progress.
	 */
	public Object2ObjectArrayMap<PatchType, Patch> patches = new Object2ObjectArrayMap<>(PatchType.VALUES.size());

	//public Tuple<PatchType, Patch>[] patches;

	/**
	 * A {@link NpcDropChanceHandler} for handling the players drop chances.
	 */
	private NpcDropChanceHandler dropChanceHandler= new NpcDropChanceHandler(this);

	/**
	 * The {@link ExperienceRate} Associated with the players account.
	 */
	private ExperienceRate experienceRate = ExperienceRate.NORMAL;

	/**
	 * The player {@link GameMode}.
	 */
	private GameMode gameMode = GameMode.NORMAL;

	/**
	 * Creates a new {@link Player}.
	 */
	public Player(PlayerCredentials credentials) {
		super(GameConstants.STARTING_POSITION, EntityType.PLAYER);
		this.credentials = credentials;
	}
	
	public void sendDefaultSidebars() {
		interfaceManager.setSidebar(TabInterface.CLAN_CHAT, 33500);//50128
		interfaceManager.setSidebar(TabInterface.SKILL, 3917);
		interfaceManager.setSidebar(TabInterface.QUEST, 638);
		interfaceManager.setSidebar(TabInterface.INVENTORY, 3213);
		interfaceManager.setSidebar(TabInterface.EQUIPMENT, 1644);
		interfaceManager.setSidebar(TabInterface.PRAYER, getPrayerBook().getId());
		interfaceManager.setSidebar(TabInterface.MAGIC, getSpellbook().getId());
		interfaceManager.setSidebar(TabInterface.FRIEND, 5065);
		interfaceManager.setSidebar(TabInterface.IGNORE, 5715);
		interfaceManager.setSidebar(TabInterface.LOGOUT, 2449);
		interfaceManager.setSidebar(TabInterface.SETTING, 904);
		interfaceManager.setSidebar(TabInterface.EMOTE, 147);
		interfaceManager.setSidebar(TabInterface.ATTACK, weapon.getId());
	}
	
	public void resetSidebars() {
		for(TabInterface tab : TabInterface.VALUES)
			interfaceManager.setSidebar(tab, -1);
	}
	
	@Override
	public Combat<Player> getCombat() {
		return combat;
	}
	
	@Override
	public int getBonus(int index) {
		return getEquipment().getBonuses()[index];
	}
	
	@Override
	public void appendBonus(int index, int bonus) {
		getEquipment().getBonuses()[index] += bonus;
	}
	
	public void setBonus(int index, int bonus) {
		getEquipment().getBonuses()[index] = bonus;
	}
	
	@Override
	public int getSkillLevel(int skill) {
		return skills[skill].getCurrentLevel();
	}
	
	@Override
	public void register() {
		boolean bot = false;
		if(World.get().getEnvironment().isDebug() && getFormatUsername().startsWith("Bot")) {
			bot = true;
		}
		setLastRegion(getPosition().copy());
		setUpdates(true, false);
		setUpdateRegion(true);
		super.getFlags().flag(UpdateFlag.APPEARANCE);
		Smelting.clearInterfaces(this);
		//if(getAttributeMap().getInt(PlayerAttributes.INTRODUCTION_STAGE) == 3) {
			sendDefaultSidebars();

		//}
		Locations.login(this);
		move(super.getPosition());
		Skills.initializeSkills(this);
		equipment.updateBulk();
		inventory.updateBulk();
		relations.onLogin();
//		privateMessage.updateThisList();
//		privateMessage.updateOtherList(true);
		send( new ContextMenuPacket(3, false, "Follow"));
		send( new ContextMenuPacket(4, false, "Trade with"));
		CombatEffect.values().forEach($it -> {
			if($it.onLogin(this))
				World.get().submit(new CombatEffectTask(this, $it));
		});
		ExchangeSessionManager.get().resetRequests(this);
		message("@blu@Welcome to "+World.get().getEnvironment().getName()+"!");
		message("@blu@Report bugs with ::bug description");
		if(UpdateCommand.inProgess == 1) {
			message("@red@There is currently an update schedule in progress. You'll be kicked off soon.");
		}
		WeaponInterface.execute(this, equipment.get(Equipment.WEAPON_SLOT));
		WeaponAnimation.execute(this, equipment.get(Equipment.WEAPON_SLOT));
		ShieldAnimation.execute(this, equipment.get(Equipment.SHIELD_SLOT));
		send( new ConfigPacket(173, super.getMovementQueue().isRunning() ? 0 : 1));
		send( new ConfigPacket(174, super.isPoisoned() ? 1 : 0));
		send( new ConfigPacket(172, super.isAutoRetaliate() ? 1 : 0));
		send( new ConfigPacket(combat.getFightType().getParent(), combat.getFightType().getChild()));
		send( new ConfigPacket(427, getAttributeMap().getBoolean(PlayerAttributes.ACCEPT_AID) ? 0 : 1));
		send( new ConfigPacket(108, 0));
		send( new ConfigPacket(301, 0));
		interfaceText(149, (int) this.playerData.runEnergy + "%");
		send( new EnergyPacket((int) playerData.getRunEnergy()));
		Prayer.VALUES.forEach(c -> send( new ConfigPacket(c.getConfig(), 0)));
		if(getPetManager().getPet().isPresent()) {
			Pet.onLogin(this);
		}
		MinigameHandler.executeVoid(this, m -> m.onLogin(this));
		PlayerPanel.refreshAll(this);
//		if(!clan.isPresent()) {
//			ClanManager.get().clearOnLogin(this);
//		}
		//if(!bot && getAttributeMap().getInt(PlayerAttributes.INTRODUCTION_STAGE) != 3) {
		//	new IntroductionCutscene(this).prerequisites();
		//}
		rights = Rights.ADMINISTRATOR;
		if(FirepitManager.get().getFirepit().isActive()) {
			this.message("@red@[ANNOUNCEMENT]: Enjoy the double experience event for another " + Utility.convertTime(FirepitManager.get().getFirepit().getTime()) + ".");
		}
		if(ShootingStarManager.get().getShootingStar() != null && ShootingStarManager.get().getShootingStar().isReg()) {
			this.message("@red@[ANNOUNCEMENT]: " + ShootingStarManager.get().getShootingStar().getLocationData().getMessageWhenActive());
		}
		TriviaTask.getBot().onLogin(this);
		if(RagePS.UPDATING > 0) {
			send( new BroadcastPacket(0, (int) (RagePS.UPDATING * 60), ">?<"));//todo check this
		}
		Summoning.login(this);
		FarmingManager.login(this);
		if(getRights().isStaff()) {
			World.get().setStaffCount(World.get().getStaffCount() + 1);
			PlayerPanel.STAFF_ONLINE.refreshAll("@or3@ - Staff online: @yel@" + World.get().getStaffCount());
		}
		if(bot) {
			int x = RandomUtils.inclusive(0, 16);
			int y = RandomUtils.inclusive(0, 16);
			setPosition(new Position(3078 + x, 3494 + y));
			int t = RandomUtils.inclusive(2, 5);
			//this.getMovementQueue().setRunning(true);
			final Player p = this;
			new Task(t, false) {
				@Override
				protected void execute() {
					//if(!p.getMovementQueue().isMovementDone())
					//	return;
					/*if(p.combat.inCombat())
						return;
					if(p.wildernessWidget && !p.combat.inCombat()) {
						for(Player local : p.localPlayers) {
							if(local.getCombat().inCombat())
								continue;
							if(local.getFormatUsername().equals("Rogue"))
								continue;
							if(local.getPosition().getDistance(p.getPosition()) < 4) {
								int i = 0;
								for(Skill s : local.skills) {
									Skills.experience(local, Integer.MAX_VALUE, i);
									i++;
								}
								i = 0;
								for(Skill s : p.skills) {
									Skills.experience(p, Integer.MAX_VALUE, i);
									i++;
								}
								local.getCombat().attack(p);
								p.getCombat().attack(local);
								break;
							}
						}
						return;
					}*/
					int xx = RandomUtils.inclusive(-5, 5);
					int yy = RandomUtils.inclusive(-5, 5);
					p.getMovementQueue().smartWalk(getPosition().move(xx, yy));
					//if(true)
					//	return;
					switch(RandomUtils.inclusive(0, 3)) {
						case 0:
							p.animation(2108);
							break;
						case 1:
							p.forceChat("dope");
							break;
						case 2:
							//p.getMovementQueue().smartWalk(getPosition().move(xx, yy));
							break;
						case 3:
							p.graphic(60);
							break;
					}
				}
			}.submit();
		}
		System.out.println(this + " logged in.");
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Player) {
			Player other = (Player) obj;
			return getSlot() == other.getSlot() && credentials.usernameHash == other.credentials.usernameHash;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getSlot(), credentials.usernameHash);
	}
	
	@Override
	public String toString() {
		return credentials.username == null ? session.toString() : "PLAYER[username= " + credentials.username + ", host= " + credentials.getHostAddress() + ", rights= " + rights + "]";
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
		return getLocation().isMulti();
	}
	
	@Override
	public boolean inWilderness() {
		return getLocation().inWilderness();
	}
	
	@Override
	public boolean active() {
		return getState() == EntityState.ACTIVE;
	}
	
	@Override
	public void dispose() {
		setVisible(false);
		Locations.logout(this);
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
		relations.updateLists(false);
//		privateMessage.updateOtherList(false); todo check this
//		clan.ifPresent(c -> c.getClan().remove(this, true));
		cannon.ifPresent(c -> c.pickup(true));
		WildernessActivity.leave(this);
		save();
	}
	
	@Override
	public void preUpdate() {
		if(session != null) {
//			session.pollIncomingMessages(); todo check thsi
		}

		getAttributeMap().plus(PlayerAttributes.SESSION_DURATION, 600L);
		getMovementQueue().sequence();
		MobAggression.sequence(this);
		restoreRunEnergy();
		int deltaX = getPosition().getX() - getLastRegion().getRegionX() * 8;
		int deltaY = getPosition().getY() - getLastRegion().getRegionY() * 8;
		if(deltaX < 16 || deltaX >= 88 || deltaY < 16 || deltaY > 88 || isNeedsRegionUpdate() || getTeleportStage() == -1) {
			setLastRegion(getPosition().copy());
			setUpdates(true, false);
			setUpdateRegion(true);
			send(new MapRegionPacket(getLastRegion().copy()));
			if(getTeleportStage() == -1)
				setTeleportStage(0);
		}
		getCombat().tick();
		//if(getSession() != null)
		//	UpdateManager.prepare(this);
	}
	
	@Override
	public void update() {
		if(session != null) {
			//ensuring the player receives a map update first.
			if(!getInitialUpdate().get()) {
				//session.dispatchMessage();
				send(new SlotPacket(getSlot()));
				send(new MapRegionPacket(this.getLastRegion().copy()));
				getInitialUpdate().set(true);
			}
			//todo append player updating
			//session.writeUpdate(new SendPlayerUpdate(), new SendMobUpdate());
		}
	}
	
	@Override
	public void postUpdate() {
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
		if(hit.getDamage() > getCurrentHealth()) {
			hit.setDamage(getCurrentHealth());
			if(hit.getHitsplat() == Hitsplat.CRITICAL) {
				hit.set(Hitsplat.NORMAL);
			}
		}
		skills[Skills.HITPOINTS].decreaseLevel(hit.getDamage(), 0, true);
		Skills.refresh(this, Skills.HITPOINTS);
		closeWidget();
		if(getCurrentHealth() <= 0) {
			getSkills()[Skills.HITPOINTS].setLevel(0, false);
			if(!isDead()) {
				if(rights.equals(Rights.ADMINISTRATOR)) {
					healEntity(getMaximumHealth());
				} else
				appendDeath();
			}
		}
		return hit;
	}
	
	@Override
	public int getAttackDelay() {
		int speed = weapon.getSpeed();
		FightType fightType = combat.getFightType();
		if(fightType == FightType.CROSSBOW_RAPID || fightType == FightType.SHORTBOW_RAPID || fightType == FightType.LONGBOW_RAPID || fightType == FightType.DART_RAPID || fightType == FightType.KNIFE_RAPID || fightType == FightType.THROWNAXE_RAPID || fightType == FightType.JAVELIN_RAPID) {
			speed--;
		} else if(fightType == FightType.CROSSBOW_LONGRANGE || fightType == FightType.SHORTBOW_LONGRANGE || fightType == FightType.LONGBOW_LONGRANGE || fightType == FightType.DART_LONGRANGE || fightType == FightType.KNIFE_LONGRANGE || fightType == FightType.THROWNAXE_LONGRANGE || fightType == FightType.JAVELIN_LONGRANGE) {
			speed++;
		}
		return speed;
	}
	
	@Override
	public int getCurrentHealth() {
		return skills[Skills.HITPOINTS].getCurrentLevel();
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
		send( new ConfigPacket(172, super.isAutoRetaliate() ? 1 : 0));
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
	
	/**
	 * Moves this player to {@code position}.
	 * @param destination the position to move this player to.
	 */
	@Override
	public void move(Position destination) {
		dialogueChain.interrupt();
		if(getLocation() != null)
		Locations.process(this);
		getMovementQueue().reset();
		closeWidget();
		Region prev = getRegion();
		if(prev != null) {
			if(prev.getPosition() == getLastRegion())
				setUpdateRegion(false);
		}
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
		World.get().getLoginService().submitSaveRequest(session, this);
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
	 * Attempts to teleport to the {@code destination}.
	 * @param destination the destination to teleport to.
	 */
	public void teleport(Position destination) {
		DefaultTeleportSpell.startTeleport(this, destination);
	}
	
	/**
	 * Attempts to teleport to the {@code destination}.
	 * @param destination the destination to teleport to.
	 * @param type the type which this player will be teleported on.
	 */
	public void teleport(Position destination, TeleportType type) {
		DefaultTeleportSpell.startTeleport(this, destination, type);
	}
	
	/**
	 * Sends wilderness and multi-combat interfaces as needed.
	 * @deprecated - replace these checks with {@link Locations} process stuff.
	 */
	public void sendInterfaces() {

	}
	
	/**
	 * Restores run energy based on the last time it was restored.
	 */
	public void restoreRunEnergy() {
		if(lastEnergy.elapsed(3500) && this.playerData.runEnergy < 100 && (this.getMovementQueue().isMovementDone() || !getMovementQueue().isRunning())) {
			double restoreRate = 0.45D;
			double agilityFactor = 0.01 * skills[Skills.AGILITY].getCurrentLevel();
			setRunEnergy(this.playerData.runEnergy + (restoreRate + agilityFactor));
			lastEnergy.reset();
			send( new EnergyPacket((int) playerData.getRunEnergy()));
		}
	}
	
	@Override
	public CombatStrategy<Player> getStrategy() {
		return PlayerWeaponStrategyManager.getStrategy(this);
	}
	
	/**
	 * Gets the formatted version of the username for this player.
	 * @return the formatted username.
	 */
	public String getFormatUsername() {
		return credentials.formattedUsername;
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
	 * Gets the total amount of donations for the player.
	 * @param dollars whether you want to determine howmany dollars or tokens the player has purchased.
	 * @return the amount.
	 */
	public int getTotalDonated(boolean dollars) {
		return dollars ? totalDonated / 100 : totalDonated;
	}
	
//	/**
//	 * Gets the hash collection of friends.
//	 * @return the friends list.
//	 */
//	public Set<Long> getFriends() {
//		return friends;
//	}
//
//	/**
//	 * Gets the hash collection of ignores.
//	 * @return the ignores list.
//	 */
//	public Set<Long> getIgnores() {
//		return ignores;
//	}
//
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
	 * A temporary queue of messages sent during the login process.
	 */
	private final Deque<Packet> queuedPackets = new ArrayDeque<>();

	/**
	 * Sends a {@link Packet} to this player.
	 *
	 * @param packet The message..
	 */
	public void send(Packet packet) {
		if (!active()) {
			queuedPackets.add(packet);
			return;
		}

		if (!queuedPackets.isEmpty()) {
			CollectionUtil.pollAll(queuedPackets, session::dispatchMessage);
		}

		session.dispatchMessage(packet);
	}
	
	/**
	 * Opens up a new interface.
	 */
	public void widget(int widget) {
		interfaceManager.open(widget);
	}
	
	/**
	 * Opens up a new chat interface.
	 */
	public void chatWidget(int widget) {
		send( new ChatInterfacePacket(widget));
	}
	
	/**
	 * Closes player interface.
	 */
	public void closeWidget() {
		interfaceManager.close(false);
	}
	
	/**
	 * A shorter way of sending a player string on interface.
	 * Checks to see if this text has already been sent to the client
	 * and won't send it if the check isn't ignored.
	 * @param message the text to send.
	 */
	public void interfaceText(int id, String message, boolean skipCheck) {
		if(!skipCheck && interfaceTexts.containsKey(id)) {
			if(interfaceTexts.get(id).equals(message)) {
				return;
			}
		}
		interfaceTexts.put(id, message);
		send(new InterfaceStringPacket(id, message));
	}
	
	public void interfaceText(int id, String message) {
		interfaceText(id, message, false);
	}


	public void interfaceText(String message, int id) {
		interfaceText(id, message, false);
	}

	/**
	 * A shorter way of sending a player a message.
	 * @param message the text to send.
	 */
	public void message(String message) {
		send(new MessagePacket(message));
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
		return playerData.godwarsKillcount;
	}
	
	/**
	 * Sets the array of the godwars killcount.
	 * @param godwarsKillcount the array to set.
	 */
	public void setGodwarsKillcount(int[] godwarsKillcount) {
		playerData.godwarsKillcount = godwarsKillcount;
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

	public void setSkills(Skill[] skills) {
		this.skills = skills;
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
	 * Sets the run energy percentage counter value.
	 * @return the new value to set.
	 */
	public void setRunEnergy(double energy) {
		this.playerData.runEnergy = energy > 100 ? 100 : energy;
		interfaceText(149, (int) this.playerData.runEnergy + "%");
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
	 * Gets the auto-cast spell.
	 * @return the {@link CombatSpell} to auto-cast
	 */
	public CombatSpell getAutocastSpell() {
		return autocastSpell;
	}
	
	/**
	 * Sets the auto-cast spell.
	 * @param autocastSpell the {@link CombatSpell} to auto-cast
	 */
	public void setAutocastSpell(CombatSpell autocastSpell) {
		this.autocastSpell = autocastSpell;
		combat.reset(false, false);
	}
	
	/**
	 * Checks whether or not an auto-cast spell is set.
	 * @return {@code true} if there is an active auto-cast spell
	 */
	public boolean isAutocast() {
		return autocastSpell != null;
	}
	
	/**
	 * Checks whether or not an auto-cast spell is set.
	 * @return {@code true} if there is an active auto-cast spell
	 */
	public boolean isSingleCast() {
		return singleCast != null;
	}

	/**
	 * Gets the players spell being cast manually.
	 * @return the {@link CombatSpell} which is being casted.
	 */
	public CombatSpell getSingleCast() {
		return singleCast;
	}

	/**
	 * Checks whether or not an auto-cast spell is set.
	 * @return {@code true} if there is an active auto-cast spell
	 */
	public void setSingleCast(CombatSpell spell) {
		singleCast = spell;
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
	 * Determines if the special bar has been activated.
	 * @return {@code true} if it has been activated, {@code false} otherwise.
	 */
	public boolean isSpecialActivated() {
		return specialActivated && getCombatSpecial() != null;
	}
	
	/**
	 * Sets the value for {@link Player#specialActivated}.
	 * @param specialActivated the new value to set.
	 */
	public void setSpecialActivated(boolean specialActivated) {
		this.specialActivated = specialActivated;
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
	 * Sets the value for {@link PlayerData#prayerBook}.
	 * @param prayerBook the new value to set.
	 */
	public void setPrayerBook(PrayerBook prayerBook) {
		playerData.prayerBook = prayerBook;
	}
	
	/**
	 * Gets the current prayer type used by the player.
	 * @return the player's prayer type.
	 */
	public PrayerBook getPrayerBook() {
		return playerData.prayerBook;
	}
	
	/**
	 * Sets the value for {@link PlayerData#spellbook}.
	 * @param spellBook the new value to set.
	 */
	public void setSpellbook(Spellbook spellBook) {
		playerData.spellbook = spellBook;
	}
	
	/**
	 * Gets the current spellbook used by the player.
	 * @return the player's spellbook.
	 */
	public Spellbook getSpellbook() {
		return playerData.spellbook;
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
	 * Everything bank pin related
	 */
	public String bankPin = "";
	public int[] pinOrder = new int[10];
	public String enterPin = "";
	public boolean resetingPin = false;
	
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

//	/**
//	 * Gets the clan the player is in.
//	 * @return the clan wrapped in an optional.
//	 */
//	public Optional<ClanMember> getClan() {
//		return clan;
//	}
//
//	/**
//	 * Sets clan the player is in.
//	 * @param clan the clan to set.
//	 */
//	public void setClan(Optional<ClanMember> clan) {
//		this.clan = clan;
//	}

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
	public GamePacket getCachedUpdateBlock() {
		return cachedUpdateBlock;
	}
	
	/**
	 * Sets the value for {@link Player#cachedUpdateBlock}.
	 * @param cachedUpdateBlock the new value to set.
	 */
	public void setCachedUpdateBlock(GamePacket cachedUpdateBlock) {
		this.cachedUpdateBlock = cachedUpdateBlock;
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
	 * Set's the players appearance container.
	 * @param appearance
	 */
	public void setAppearance(PlayerAppearance appearance) {
		this.appearance = appearance;
	}
	
	/**
	 * Gets the disabled activities for this player.
	 * @return the disabled activity class.
	 */
	public ActivityManager getActivityManager() {
		return activityManager;
	}
	
	/**
	 * Gets the stopwatch that will time logouts.
	 * @return the logout stopwatch.
	 */
	public Stopwatch getLogoutTimer() {
		return logoutTimer;
	}
	
	/**
	 * @return {@link PlayerData#npcKills}.
	 */
	public MutableNumber getNpcKills() {
		return playerData.npcKills;
	}

	/**
	 * @return {@link PlayerData#npcDeaths}.
	 */
	public MutableNumber getDeathsByNpc() {
		return playerData.npcDeaths;
	}
	
	/**
	 * Updates the value for {@link PlayerData#pestPoints}.
	 * @param points the new value to update.
	 */
	public void updatePest(int points) {
		playerData.pestPoints += points;
		PlayerPanel.PEST_POINTS.refresh(this, "@or2@ - Pest points: @yel@" + playerData.pestPoints);
	}
	
	/**
	 * Gets the amount of pest points the player has.
	 * @return the pest points amount.
	 */
	public int getPest() {
		return playerData.pestPoints;
	}
	
	/**
	 * Updates the value for {@link PlayerData#slayerPoints}.
	 * @param points the new value to update.
	 */
	public void updateSlayers(int points) {
		playerData.slayerPoints += points;
		PlayerPanel.SLAYER_POINTS.refresh(this, "@or2@ - Slayer points: @yel@" + playerData.slayerPoints, true);
	}
	
	/**
	 * Gets the amount of slayer points the player has.
	 * @return the slayer points amount.
	 */
	public int getSlayerPoints() {
		return playerData.slayerPoints;
	}
	
	/**
	 * The current slayer instance.
	 * @return {@link PlayerData#slayer}.
	 */
	public Optional<Slayer> getSlayer() {
		return playerData.slayer;
	}
	
	/**
	 * Sets the slayer.
	 * @param slayer the slayer to set this current slayer to.
	 */
	public void setSlayer(Optional<Slayer> slayer) {
		playerData.slayer = slayer;
		PlayerPanel.SLAYER_TASK.refresh(this, "@or2@ - Slayer task: @yel@" + (slayer.isPresent() ? (slayer.get().getAmount() + " " + slayer.get().toString()) : "none"));
	}
	
	/**
	 * Gets the list of blocked slayer tasks.
	 * @return a list of the blocked slayer tasks.
	 */
	public String[] getBlockedTasks() {
		return playerData.blockedTasks;
	}
	
	/**
	 * Sets the blocked slayer tasks.
	 * @return sets the blocked slayer tasks.
	 */
	public void setBlockedTasks(String[] blockedTasks) {
		playerData.blockedTasks = blockedTasks;
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
	
	/**
	 * The initial map update flag.
	 */
	public AtomicBoolean getInitialUpdate() {
		return initialUpdate;
	}

    public NpcDropChanceHandler getDropChanceHandler() {
        return dropChanceHandler;
    }

    public void setDropChanceHandler(NpcDropChanceHandler dropChanceHandler) {
        this.dropChanceHandler = dropChanceHandler;
    }

	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public MobSpawner getMobSpawner() {
		return mobSpawner;
	}

	public ExperienceRate getExperienceRate() {
		return experienceRate;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public void setExperienceRate(ExperienceRate experienceRate) {
		this.experienceRate = experienceRate;
	}

	public String getMfaSecret() {
		return mfaSecret;
	}

	public void setMfaSecret(String mfaSecret) {
		this.mfaSecret = mfaSecret;
	}
}