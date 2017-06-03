package net.edge.content.minigame.fightcaves;

import net.edge.game.GameConstants;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.item.FoodConsumable;
import net.edge.content.minigame.SequencedMinigame;
import net.edge.locale.Position;
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
 */
public final class FightcavesMinigame extends SequencedMinigame {
	
	/**
	 * The current timer.
	 */
	private int timer;
	
	/**
	 * The count of jads killed.
	 */
	private int killed;
	
	/**
	 * The instance of the current fightcave minigame.
	 */
	private final int instance = World.getInstanceManager().closeNext();
	
	/**
	 * The flag to determines if the fight is started.
	 */
	private boolean started;
	
	/**
	 * The first jad which is set to be killed.
	 */
	private final Npc jad = new DefaultNpc(2745, new Position(2408, 5092));
	
	/**
	 * The second jad if it's in advanced mode.
	 */
	private final Npc otherJad = new DefaultNpc(2745, new Position(2398, 5092));
	
	/**
	 * The delay before the wave spawns.
	 */
	private static final int DELAY = 10;
	
	/**
	 * Constructs a new {@link FightcavesMinigame} minigame.
	 */
	public FightcavesMinigame() {
		super("FIGHT_CAVES", MinigameSafety.SAFE);
	}
	
	public static boolean enter(Player player, ObjectNode object) {
		if(object.getId() != 9356) {
			return false;
		}
		player.getDialogueBuilder().append(new OptionDialogue(t -> {
			if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
				new FightcavesMinigame().onEnter(player);
			}
			player.getMessages().sendCloseWindows();
		}, "Kill one Jad for a Fire cape.", "I'll wait here."));
		/*player.getDialogueChain().append(
		        new OptionDialogue(t -> {
					if(t.equals(OptionType.FIRST_OPTION)) {
						MinigameContainer.FIGHT_CAVES.onEnter(player);
					} else if(t.equals(OptionType.SECOND_OPTION)) {
						MinigameContainer.FIGHT_CAVES.onEnter(player);
						player.getAttr().get("fight_caves_advanced").set(true);
					} 
					player.getMessages().sendCloseWindows();
				}, "Fire cape", "TokHaar-kal cape", "I'll wait here")
				);*/
		return true;
	}
	
	@Override
	public boolean contains(Player player) {
		return player.getPosition().within(2366, 5119, 2431, 5057);
	}
	
	@Override
	public void onSequence(Player player) {
		if(timer-- < 1 && !started) {
			if(player.getAttr().get("fight_caves_advanced").getBoolean()) {
				otherJad.setRespawn(false);
				otherJad.setSpawnedFor(player.getUsername());
				World.get().getNpcs().add(otherJad);
				World.getInstanceManager().isolate(otherJad, instance);
			}
			jad.setRespawn(false);
			jad.setSpawnedFor(player.getUsername());
			World.get().getNpcs().add(jad);
			World.getInstanceManager().isolate(jad, instance);
			started = true;
		} else if(timer == 8) {
			player.getDialogueBuilder().append(new NpcDialogue(2617, "You're on your own, JalYt", "Prepare to fight for your life!"));
		}
	}
	
	@Override
	public void onDeath(Player player) {
		logout(player);
	}
	
	@Override
	public void onKill(Player player, EntityNode victim) {
		if(victim.isPlayer()) {
			logout(player);
			return;
		}
		Npc npc = victim.toNpc();
		if(npc.getId() != 2745) {
			return;
		}
		killed += 1;
		if(player.getAttr().get("fight_caves_advanced").getBoolean() ? killed == 2 : killed == 1) {
			this.destruct(player);
			player.setMinigame(Optional.empty());
			player.message("You have successfully completed the minigame...");
			int reward = player.getAttr().get("fight_caves_advanced").getBoolean() ? 19111 : 6570;
			player.getInventory().addOrBank(new Item(reward, 1));
			player.move(new Position(2436, 5169, 0));
			World.get().getNpcs().remove(jad);
			World.get().getNpcs().remove(otherJad);
		}
	}
	
	@Override
	public int delay() {
		return 1;
	}
	
	@Override
	public void login(Player player) {
		this.logout(player);
	}
	
	@Override
	public void enter(Player player) {
		World.getInstanceManager().isolate(player, instance);
		player.move(new Position(2413, 5117));
		timer = DELAY;
		started = false;
		killed = 0;
	}
	
	@Override
	public boolean canLogout(Player player) {
		return true;
	}
	
	@Override
	public void logout(Player player) {
		World.get().getNpcs().remove(jad);
		World.get().getNpcs().remove(otherJad);
		player.move(GameConstants.STARTING_POSITION);
		player.message("You failed to complete the fight cave...");
		this.destruct(player);
		player.setMinigame(Optional.empty());
		player.setInstance(0);
		World.getInstanceManager().open(instance);
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
}
