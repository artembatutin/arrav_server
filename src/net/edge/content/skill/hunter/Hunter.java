package net.edge.content.skill.hunter;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.edge.action.impl.ObjectAction;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.content.skill.hunter.trap.Trap;
import net.edge.content.skill.hunter.trap.TrapProcessor;
import net.edge.content.skill.hunter.trap.TrapTask;
import net.edge.content.skill.hunter.trap.bird.BirdData;
import net.edge.world.entity.region.TraversalMap;
import net.edge.world.locale.Position;
import net.edge.world.Animation;
import net.edge.world.Direction;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.Item;
import net.edge.world.object.GameObject;

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
				t.getObject().getRegion().ifPresent(r -> r.register(new GroundItem(new Item(t.getType().getItemId()), t.getObject().getGlobalPos().copy(), player)));
			});
			GLOBAL_TRAPS.get(player).getTraps().clear();
		} else {
			GLOBAL_TRAPS.get(player).getTraps().remove(trap);
			trap.setAbandoned(true);
			trap.getObject().remove();
			player.message("You have abandoned your trap...");
			trap.getObject().getRegion().ifPresent(r -> r.register(new GroundItem(new Item(trap.getType().getItemId()), trap.getObject().getGlobalPos().copy(), player)));
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
		
		//if(player.getRegion().interactAction().stream().anyMatch(o -> o.getObjectType() == ObjectType.GENERAL_PROP) || !TraversalMap.isTraversable(p, Direction.WEST, player.size()) && !TraversalMap.isTraversable(p, Direction.EAST, player.size()) || Location.isAtHome(player)) {
		//	player.message("You can't set-up your trap here.");
		//	return false;
		//}
		
		for(Mob mob : player.getLocalMobs()) {
			if(mob == null) {
				continue;
			}
			if(mob.getPosition().same(player.getPosition()) || mob.getOriginalPosition().same(player.getPosition())) {
				player.message("You can't set-up your trap here.");
				return false;
			}
		}
		
		GLOBAL_TRAPS.get(player).getTraps().add(trap);
		
		trap.submit();
		player.animation(new Animation(827));
		player.getInventory().remove(new Item(trap.getType().getItemId(), 1));
		trap.getObject().publish();
		if(TraversalMap.isTraversable(p, Direction.WEST, player.size())) {
			player.getMovementQueue().walk(Direction.WEST.getX(), Direction.WEST.getY());
		} else if(TraversalMap.isTraversable(p, Direction.EAST, player.size())) {
			player.getMovementQueue().walk(Direction.EAST.getX(), Direction.EAST.getY());
		}
		player.facePosition(p);
		return true;
	}
	
	public static void action() {
		ObjectAction action = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(click == 1) {
					Trap trap = getTrap(player, object).orElse(null);
					if(trap == null) {
						return false;
					}
					if(trap.getState().equals(Trap.TrapState.CATCHING)) {
						return true;
					}
					if(trap.getState().equals(Trap.TrapState.CAUGHT)) {
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
				return false;
			}
		};
		for(Trap.TrapType type : Trap.TrapType.values()) {
			action.registerFirst(type.getObjectId());
		}
		for(BirdData data : BirdData.values()) {
			action.registerFirst(data.objectId);
		}
		action.registerFirst(19190);
	}
	
	
	/**
	 * Gets a trap for the specified global object given.
	 * @param player the player to return a trap for.
	 * @param object the object to compare.
	 * @return a trap wrapped in an optional, {@link Optional#empty()} otherwise.
	 */
	public static Optional<Trap> getTrap(Player player, GameObject object) {
		if(!GLOBAL_TRAPS.containsKey(player))
			return Optional.empty();
		for(Trap t : GLOBAL_TRAPS.get(player).getTraps()) {
			if(t.getObject().getId() == object.getId() && t.getObject().getGlobalPos().same(object.getGlobalPos())) {
				return Optional.of(t);
			}
		}
		return Optional.empty();
	}
}
