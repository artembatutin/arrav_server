package net.edge.content.skill.hunter;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.content.skill.hunter.trap.Trap;
import net.edge.content.skill.hunter.trap.TrapProcessor;
import net.edge.content.skill.hunter.trap.TrapTask;
import net.edge.locale.Position;
import net.edge.world.Animation;
import net.edge.world.Direction;
import net.edge.world.World;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNode;
import net.edge.world.object.ObjectNode;

import java.util.Arrays;
import java.util.Optional;

/**
 * The class which holds static functionality for the hunter skill.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Hunter {
	
	/**
	 * The mappings which contain each trap by player on the world.
	 */
	public static final Object2ObjectOpenHashMap<Player, TrapProcessor> GLOBAL_TRAPS = new Object2ObjectOpenHashMap<>();
	
	/**
	 * Retrieves the maximum amount of traps a player can lay.
	 * @param player the player to lay a trap down for.
	 * @return a numerical value determining the amount a player can lay.
	 */
	private static int getMaximumTraps(Player player) {
		Skill hunter = player.getSkills()[Skills.HUNTER];
		return hunter.getLevel() / 20 + 1;
	}
	
	/**
	 * Attempts to abandon the specified {@code trap} for the player.
	 * @param trap   the trap that was abandoned.
	 * @param logout if the abandon was due to the player logging out.
	 */
	public static void abandon(Player player, Trap trap, boolean logout) {
		if(GLOBAL_TRAPS.get(player) == null) {
			return;
		}
		
		if(logout) {
			GLOBAL_TRAPS.get(player).getTraps().forEach(t -> {
				t.setAbandoned(true);
				t.getObject().publish();
				t.getObject().getRegion().register(new ItemNode(new Item(t.getType().getItemId()), t.getObject().getGlobalPos().copy(), player));
			});
			GLOBAL_TRAPS.get(player).getTraps().clear();
		} else {
			GLOBAL_TRAPS.get(player).getTraps().remove(trap);
			trap.setAbandoned(true);
			trap.getObject().remove();
			player.message("You have abandoned your trap...");
			trap.getObject().getRegion().register(new ItemNode(new Item(trap.getType().getItemId()), trap.getObject().getGlobalPos().copy(), player));
		}
		
		if(GLOBAL_TRAPS.get(player).getTraps().isEmpty()) {
			GLOBAL_TRAPS.get(player).setTask(Optional.empty());
			GLOBAL_TRAPS.remove(player);
		}
	}
	
	/**
	 * Attempts to lay down the specified {@code trap} for the specified {@code player}.
	 * @param player the player to lay the trap for.
	 * @param trap   the trap to lay down for the player.
	 * @return {@code true} if the trap was laid, {@code false} otherwise.
	 */
	public static boolean lay(Player player, Trap trap) {
		GLOBAL_TRAPS.putIfAbsent(player, new TrapProcessor());
		
		if(!GLOBAL_TRAPS.get(player).getTask().isPresent()) {
			GLOBAL_TRAPS.get(player).setTask(new TrapTask(player));
			World.get().submit(GLOBAL_TRAPS.get(player).getTask().get());
		}
		
		if(GLOBAL_TRAPS.get(player).getTraps().size() >= getMaximumTraps(player)) {
			player.message("You cannot lay more then " + getMaximumTraps(player) + " with your hunter level.");
			return false;
		}
		
		Position p = player.getPosition();
		
		//if(player.getRegion().interactAction().stream().anyMatch(o -> o.getObjectType() == ObjectType.GENERAL_PROP) || !World.getTraversalMap().isTraversable(p, Direction.WEST, player.size()) && !World.getTraversalMap().isTraversable(p, Direction.EAST, player.size()) || Location.isAtHome(player)) {
		//	player.message("You can't set-up your trap here.");
		//	return false;
		//}
		
		for(Npc npc : player.getLocalNpcs()) {
			if(npc == null) {
				continue;
			}
			if(npc.getPosition().same(player.getPosition()) || npc.getOriginalPosition().same(player.getPosition())) {
				player.message("You can't set-up your trap here.");
				return false;
			}
		}
		
		GLOBAL_TRAPS.get(player).getTraps().add(trap);
		
		trap.submit();
		player.animation(new Animation(827));
		player.getInventory().remove(new Item(trap.getType().getItemId(), 1));
		trap.getObject().publish();
		if(World.getTraversalMap().isTraversable(p, Direction.WEST, player.size())) {
			player.getMovementQueue().walk(Direction.WEST.getX(), Direction.WEST.getY());
		} else if(World.getTraversalMap().isTraversable(p, Direction.EAST, player.size())) {
			player.getMovementQueue().walk(Direction.EAST.getX(), Direction.EAST.getY());
		}
		player.facePosition(p);
		return true;
	}
	
	/**
	 * Attempts to pick up the trap for the specified {@code player}.
	 * @param player the player to pick this trap up for.
	 * @param object the object that was clicked.
	 * @return {@code true} if the trap was picked up, {@code false} otherwise.
	 */
	public static boolean pickup(Player player, ObjectNode object) {
		Optional<Trap.TrapType> type = Trap.TrapType.getTrapByObjectId(object.getId());
		
		if(!type.isPresent()) {
			return false;
		}
		
		Trap trap = getTrap(player, object).orElse(null);
		
		if(trap == null) {
			return false;
		}
		
		if(trap.getState().equals(Trap.TrapState.CAUGHT)) {
			return false;
		}
		
		if(!trap.getPlayer().getFormatUsername().equals(player.getFormatUsername())) {
			player.message("You can't pickup someone elses trap...");
			return false;
		}
		
		GLOBAL_TRAPS.get(player).getTraps().remove(trap);
		
		if(GLOBAL_TRAPS.get(player).getTraps().isEmpty()) {
			GLOBAL_TRAPS.get(player).setTask(Optional.empty());
			GLOBAL_TRAPS.remove(player);
		}
		
		trap.onPickUp();
		object.remove();
		player.getInventory().add(new Item(trap.getType().getItemId(), 1));
		player.animation(new Animation(827));
		return true;
	}
	
	/**
	 * Attempts to claim the rewards of this trap.
	 * @param player the player attempting to claim the items.
	 * @param object the object being interacted with.
	 * @return {@code true} if the trap was claimed, {@code false} otherwise.
	 */
	public static boolean claim(Player player, ObjectNode object) {
		Trap trap = getTrap(player, object).orElse(null);
		
		if(trap == null) {
			return false;
		}
		
		if(!trap.getState().equals(Trap.TrapState.CAUGHT)) {
			return false;
		}
		
		if(!trap.getPlayer().getFormatUsername().equals(player.getFormatUsername())) {
			player.message("You can't claim the rewards of someone elses trap...");
			return false;
		}
		
		if(!trap.canClaim(object)) {
			return false;
		}
		
		Arrays.stream(trap.reward()).forEach(reward -> player.getInventory().add(reward));
		
		Skills.experience(player, (int) trap.experience() * 5, Skills.HUNTER);
		
		GLOBAL_TRAPS.get(player).getTraps().remove(trap);
		
		if(GLOBAL_TRAPS.get(player).getTraps().isEmpty()) {
			GLOBAL_TRAPS.get(player).setTask(Optional.empty());
			GLOBAL_TRAPS.remove(player);
		}
		trap.getObject().remove();
		player.getInventory().add(new Item(trap.getType().getItemId(), 1));
		player.animation(new Animation(827));
		return true;
	}
	
	/**
	 * Gets a trap for the specified global object given.
	 * @param player the player to return a trap for.
	 * @param object the object to compare.
	 * @return a trap wrapped in an optional, {@link Optional#empty()} otherwise.
	 */
	public static Optional<Trap> getTrap(Player player, ObjectNode object) {
		return !GLOBAL_TRAPS.containsKey(player) ? Optional.empty() : GLOBAL_TRAPS.get(player).getTraps().stream().filter(trap -> trap.getObject().getId() == object.getId() && trap.getObject().getX() == object.getX() && trap.getObject().getY() == object.getY() && trap.getObject().getZ() == object.getZ()).findAny();
	}
}
