package net.edge.content.minigame.fightcaves;

import net.edge.GameConstants;
import net.edge.action.impl.ObjectAction;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.item.FoodConsumable;
import net.edge.content.item.PotionConsumable;
import net.edge.content.minigame.SequencedMinigame;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.DefaultMob;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.locale.InstanceManager;
import net.edge.world.locale.Position;
import net.edge.world.object.GameObject;

import java.util.Optional;

/**
 * Holds functionality for the fight caves minigame.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class FightcavesMinigame extends SequencedMinigame {
	
	/*
	 * Fight cave monsters identifiers.
	 */
	private static final int TZ_KIH = 2627;
	private static final int TZ_KEK = 2629;
	private static final int TOK_XIL = 2631;
	private static final int YT_MEJKOT = 2741;
	private static final int KET_ZEK = 2743;
	private static final int TZTOK_JAD = 2745;
	
	/**
	 * The wave enumeration.
	 */
	private static final int[][] WAVES = {{TZ_KIH, TZ_KEK}, {TOK_XIL, TZ_KIH}, {TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH}, {YT_MEJKOT, TZ_KIH}, {YT_MEJKOT, TZ_KEK, TZ_KIH, TZ_KIH}, {YT_MEJKOT, TOK_XIL, TZ_KIH, TZ_KIH}, {YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KEK}, {KET_ZEK, TZ_KIH}, {KET_ZEK, TZ_KEK, TZ_KIH, TZ_KIH}, {KET_ZEK, TOK_XIL, TZ_KIH, TZ_KIH}, {KET_ZEK, TOK_XIL, TZ_KEK, TZ_KEK}, {KET_ZEK, YT_MEJKOT, TZ_KIH, TZ_KIH}, {KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH}, {KET_ZEK, KET_ZEK}};
	
	/**
	 * Wave spawns enumeration.
	 */
	private final static Position[] SPAWNS = {new Position(2403, 5094), new Position(2390, 5096), new Position(2392, 5077), new Position(2408, 5080), new Position(2413, 5108), new Position(2381, 5106), new Position(2379, 5072), new Position(2420, 5082)};
	
	/**
	 * The current timer.
	 */
	private int timer;
	
	/**
	 * Current fighting wave.
	 */
	private int wave;
	
	/**
	 * The instance of the current fightcave minigame.
	 */
	private final int instance = InstanceManager.get().closeNext();
	
	/**
	 * The array of active monsters.
	 */
	private Mob[] monsters;
	
	/**
	 * The flag to determines if the fight is started.
	 */
	private boolean started;
	
	/**
	 * The delay before the wave spawns.
	 */
	private static final int DELAY = 10;
	
	/**
	 * Constructs a new {@link FightcavesMinigame} minigame.
	 */
	private FightcavesMinigame() {
		super("FIGHT_CAVES", MinigameSafety.SAFE);
	}
	
	public static void action() {
		ObjectAction e = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.getDialogueBuilder().append(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getAttr().get("fight_caves_advanced").set(false);
						new FightcavesMinigame().onEnter(player);
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.getAttr().get("fight_caves_advanced").set(true);
						new FightcavesMinigame().onEnter(player);
					}
					player.closeWidget();
				}, "Fire cape", "TokHaar-kal cape (two jads)", "I'll wait here"));
				return true;
			}
		};
		e.registerFirst(9356);
	}
	
	@Override
	public boolean contains(Player player) {
		return player.getPosition().within(2366, 5119, 2431, 5057);
	}
	
	@Override
	public void onSequence() {
		for(Player player : getPlayers()) {
			if(!started && timer-- < 1) {
				int[] wave;
				if(this.wave >= WAVES.length) {
					if(player.getAttr().get("fight_caves_advanced").getBoolean())
						wave = new int[]{TZTOK_JAD, TZTOK_JAD};//two jads.
					else
						wave = new int[]{TZTOK_JAD};
				} else {
					wave = WAVES[this.wave];
				}
				monsters = new Mob[wave.length];
				for(int i = 0; i < wave.length; i++) {
					monsters[i] = new DefaultMob(wave[i], RandomUtils.random(SPAWNS));
					monsters[i].setRespawn(false);
					monsters[i].setOwner(player);
					InstanceManager.get().isolate(monsters[i], instance);
					World.get().getMobs().add(monsters[i]);
					monsters[i].setViewingDistance(100);
					monsters[i].getCombat().attack(player);
				}
				started = true;
				timer = DELAY;
			}
		}
	}
	
	@Override
	public void onDeath(Player player) {
		logout(player);
	}
	
	@Override
	public void onKill(Player player, Actor victim) {
		if(victim.isPlayer()) {
			logout(player);
			return;
		}
		Mob mob = victim.toMob();
		boolean empty = true;
		for(int i = 0; i < monsters.length; i++) {
			if(monsters[i] == null)
				continue;
			if(monsters[i].getId() == mob.getId() && monsters[i].getSlot() == mob.getSlot()) {
				monsters[i] = null;
				continue;
			}
			empty = false;
		}
		if(this.wave == 14 && empty) {
			player.setMinigame(Optional.empty());
			player.message("You have successfully completed the minigame...");
			int reward = player.getAttr().get("fight_caves_advanced").getBoolean() ? 19111 : 6570;
			player.getInventory().addOrBank(new Item(reward, 1));
			player.move(new Position(2436, 5169));
			if(RandomUtils.inclusive(100) == 0) {
				player.getInventory().addOrBank(new Item(3906));
				player.message("You just got yourself a trapped Jadiku!");
			}
			this.destruct();
		} else if(empty) {
			started = false;
			timer = DELAY;
			this.wave += 1;
			player.getDialogueBuilder().append(new NpcDialogue(2617, (this.wave == 13 ? "Prepare to fight for your life!" : "Prepare for wave " + (this.wave + 1) + "!")));
		}
	}
	
	@Override
	public int delay() {
		return 1;
	}
	
	@Override
	public void login(Player player) {

	}
	
	@Override
	public void enter(Player player) {
		InstanceManager.get().isolate(player, instance);
		player.move(new Position(2413, 5117));
		timer = DELAY;
		started = false;
	}
	
	@Override
	public boolean canLogout(Player player) {
		return true;
	}
	
	@Override
	public void logout(Player player) {
		for(Mob monster : monsters) {
			if(monster == null)
				continue;
			World.get().getMobs().remove(monster);
		}
		player.setInstance(0);
		InstanceManager.get().open(instance);
		player.setMinigame(Optional.empty());
		player.move(new Position(2436, 5169));
		player.message("You failed to complete the fight cave...");
		this.destruct();
	}
	
	@Override
	public boolean canEat(Player player, FoodConsumable food) {
		return true;
	}
	
	@Override
	public boolean canPot(Player player, PotionConsumable potion) {
		return true;
	}
	
	@Override
	public boolean onFirstClickObject(Player player, GameObject object) {
		if(object.getId() == 9357) {//Exit
			onDeath(player);
			return true;
		}
		player.message("You cannot interact with this object in here!");
		return false;
	}
	
	@Override
	public Position deathPosition(Player player) {
		return GameConstants.STARTING_POSITION;
	}
	
}
