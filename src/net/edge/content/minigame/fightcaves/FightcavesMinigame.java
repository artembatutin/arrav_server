package net.edge.content.minigame.fightcaves;

import net.edge.event.impl.ObjectEvent;
import net.edge.game.GameConstants;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.item.FoodConsumable;
import net.edge.content.minigame.SequencedMinigame;
import net.edge.locale.Position;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.impl.DefaultNpc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.object.ObjectNode;

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
	private static final int[][] WAVES = {
			{TZ_KIH, TZ_KEK},
			{TOK_XIL, TZ_KIH},
			{TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH},
			{YT_MEJKOT, TZ_KIH},
			{YT_MEJKOT, TZ_KEK, TZ_KIH, TZ_KIH},
			{YT_MEJKOT, TOK_XIL, TZ_KIH, TZ_KIH},
			{YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KEK},
			{KET_ZEK, TZ_KIH},
			{KET_ZEK, TZ_KEK, TZ_KIH, TZ_KIH},
			{KET_ZEK, TOK_XIL, TZ_KIH, TZ_KIH},
			{KET_ZEK, TOK_XIL, TZ_KEK, TZ_KEK},
			{KET_ZEK, YT_MEJKOT, TZ_KIH, TZ_KIH},
			{KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH},
			{KET_ZEK, KET_ZEK}
	};
	
	/**
	 * Wave spawns enumeration.
	 */
	private final static Position[] SPAWNS = {
			new Position(2403, 5094),
			new Position(2390, 5096),
			new Position(2392, 5077),
			new Position(2408, 5080),
			new Position(2413, 5108),
			new Position(2381, 5106),
			new Position(2379, 5072),
			new Position(2420, 5082)
	};
	
	/**
	 * The current timer.
	 */
	private int timer;
	
	/**
	 * The instance of the current fightcave minigame.
	 */
	private final int instance = World.getInstanceManager().closeNext();
	
	/**
	 * The array of active monsters.
	 */
	private Npc[] monsters;
	
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
	
	public static void event() {
		ObjectEvent e = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.getDialogueBuilder().append(
		        new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getAttr().get("fight_caves_wave").set(0);
						player.getAttr().get("fight_caves_advanced").set(false);
						new FightcavesMinigame().onEnter(player);
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.getAttr().get("fight_caves_wave").set(0);
						player.getAttr().get("fight_caves_advanced").set(true);
						new FightcavesMinigame().onEnter(player);
					}
					player.getMessages().sendCloseWindows();
				}, "Fire cape", "TokHaar-kal cape (two jads)", "I'll wait here")
				);
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
				int current = player.getAttr().get("fight_caves_wave").getInt();
				if(current >= WAVES.length) {
					if(player.getAttr().get("fight_caves_advanced").getBoolean())
						wave = new int[] { TZTOK_JAD, TZTOK_JAD };//two jads.
					else
						wave = new int[] { TZTOK_JAD };
				} else {
					wave = WAVES[current];
				}
				monsters = new Npc[wave.length];
				for(int i = 0; i < wave.length; i++) {
					monsters[i] = new DefaultNpc(wave[i], RandomUtils.random(SPAWNS));
					monsters[i].setRespawn(false);
					monsters[i].setOwner(player);
					World.getInstanceManager().isolate(monsters[i], instance);
					World.get().getNpcs().add(monsters[i]);
					monsters[i].getCombatBuilder().attack(player);
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
	public void onKill(Player player, EntityNode victim) {
		if(victim.isPlayer()) {
			over(player, true);
			return;
		}
		Npc npc = victim.toNpc();
		boolean empty = true;
		for(int i = 0; i < monsters.length; i++) {
			if(monsters[i] == null)
				continue;
			if(monsters[i].getId() == npc.getId() && monsters[i].getSlot() == npc.getSlot()) {
				monsters[i] = null;
				continue;
			}
			empty = false;
		}
		int current = player.getAttr().get("fight_caves_wave").getInt();
		if(current == 15 && empty) {
			player.setMinigame(Optional.empty());
			player.message("You have successfully completed the minigame...");
			int reward = player.getAttr().get("fight_caves_advanced").getBoolean() ? 19111 : 6570;
			player.getInventory().addOrBank(new Item(reward, 1));
			player.move(new Position(2436, 5169));
			this.destruct();
		} else if(empty) {
			player.getAttr().get("fight_caves_wave").set(current + 1);
			started = false;
			timer = DELAY;
			player.getDialogueBuilder().append(new NpcDialogue(2617, (current + 1 == 15 ? "Prepare to fight for your life!" : "Prepare for wave " + (current + 1) + "!")));
		}
	}
	
	@Override
	public int delay() {
		return 1;
	}
	
	@Override
	public void login(Player player) {
		if(contains(player))
			new FightcavesMinigame().onEnter(player);
	}
	
	@Override
	public void enter(Player player) {
		World.getInstanceManager().isolate(player, instance);
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
		for(Npc monster : monsters) {
			if(monster == null)
				continue;
			World.get().getNpcs().remove(monster);
		}
		World.getInstanceManager().open(instance);
		player.setInstance(0);
		this.destruct();
	}
	
	@Override
	public boolean canEat(Player player, FoodConsumable food) {
		return true;
	}
	
	@Override
	public boolean onFirstClickObject(Player player, ObjectNode object) {
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
	
	public void over(Player player, boolean out) {
		logout(player);
		if(out) {
			player.setMinigame(Optional.empty());
			player.move(new Position(2436, 5169));
			player.message("You failed to complete the fight cave...");
		}
	}
	
}
