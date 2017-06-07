package net.edge.world.node.entity.move;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.Combat;
import net.edge.content.skill.summoning.Summoning;
import net.edge.locale.Boundary;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Direction;
import net.edge.world.node.entity.move.path.Path;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

import java.util.List;
import java.util.Optional;

/**
 * The {@link Task} implementation that handles the entire following process.
 * @author lare96 <http://github.com/lare96>
 */
class CharacterFollowTask extends Task {
	
	/**
	 * The character this process is being executed for.
	 */
	private final EntityNode character;
	
	/**
	 * The character being followed in this process.
	 */
	private final EntityNode leader;
	
	/**
	 * The current destination of the path.
	 */
	private Position destination;
	
	/**
	 * Creates a new {@link CharacterFollowTask}.
	 * @param character the character this process is being executed for.
	 * @param leader    the character being followed in this process.
	 */
	CharacterFollowTask(EntityNode character, EntityNode leader) {
		super(1, true);
		this.character = character;
		this.leader = leader;
	}
	
	@Override
	public void execute() {
		//Familiar calling back.
		boolean familiar = false;
		if(character.isNpc()) {
			Npc npc = (Npc) character;
			if(npc.isFamiliar() || npc.isPet()) {
				if(!character.getPosition().withinDistance(leader.getPosition(), 15)) {
					if(leader.isPlayer()) {
						if(npc.isFamiliar()) {
							Summoning.callFamiliar(leader.toPlayer());
						} else if(npc.isPet()) {
							Summoning.callPet(leader.toPlayer());
						}
					}
				}
				familiar = true;
			}
		}
		
		//First checks.
		if(!character.isFollowing() || (!character.getPosition().withinDistance(leader.getPosition(), 30) && !familiar) || character.isDead() || leader.isDead()) {//Death and away check.
			character.faceEntity(null);
			character.setFollowing(false);
			character.setFollowEntity(null);
			this.cancel();
			return;
		}
		
		//Entity facing.
		character.faceEntity(leader);
		
		//Movement locks.
		if(character.getMovementQueue().isLockMovement() || character.isFrozen() || character.isStunned()) {//Requirement check.
			if(character.isPlayer()) {
				Player player = character.toPlayer();
				if(player.isFrozen())
					player.message("You're currently frozen and you cannot move.");
				if(player.isStunned())
					player.message("You're currently stunned and you cannot move.");
			}
			this.cancel();
			return;
		}
		
		//Randomized walk away from leader's tile.
		if(new Boundary(leader.getPosition(), leader.size()).inside(character.getPosition(), character.size())) {
			character.getMovementQueue().reset();
			ObjectList<Position> pos = World.getTraversalMap().getSurroundedTraversableTiles(leader.getPosition(), leader.size(), character.size());
			if(pos.size() > 0) {
				Position p = RandomUtils.random(pos);
				character.getMovementQueue().walk(p);
			}
			return;
		}
		
		//Combat distance check.
		if(character.getCombatBuilder().isAttacking()) {
			if(character.isPlayer()) {
				character.getCombatBuilder().determineStrategy();
				
				if(Combat.checkAttackDistance(character.getCombatBuilder())) {
					return;
				}
			}
		}
		
		//Resets if character next to the player.
		if(new Boundary(leader.getPosition(), leader.size()).within(character.getPosition(), character.size(), 1)) {
			character.getMovementQueue().reset();
			//Combat diagonal fighting.
			if(character.getCombatBuilder().isAttacking()) {
				Direction facing = Direction.fromDeltas(Position.delta(character.getPosition(), leader.getPosition()));
				if(facing.toString().contains("_")) {//Diagonal
					Optional<Position> pos = World.getTraversalMap().getNonDiagonalNearbyTraversableTiles(character.getPosition(), character.size()).stream().filter(p -> new Boundary(leader.getPosition(), leader.size()).within(p, character.size(), 1)).findFirst();
					if(pos.isPresent())
						character.getMovementQueue().walk(pos.get());
				}
				return;
			}
			return;
		}
		
		//returns if path calculate is next to the leader.
		if(destination != null && new Boundary(leader.getPosition(), leader.size()).inside(destination, character.size())) {
			return;
		}
		
		//Setting new path.
		Path path = character.isPlayer() || (character.isNpc() && character.toNpc().isSmart()) ? World.getAStarPathFinder().find(character, leader.getPosition()) : World.getSimplePathFinder().find(character, leader.getPosition());
		if(path.isPossible()) {
			character.getMovementQueue().walk(path.getMoves());
			destination = path.getDestination();
		} else {
			character.getMovementQueue().reset();
			destination = null;
		}
	}
	
	@Override
	public void onCancel() {
		destination = null;
		character.setFollowing(false);
		character.setFollowEntity(null);
		character.getMovementQueue().setFollowTask(Optional.empty());
	}
	
	@Override
	public void onException(Exception e) {
		onCancel();
	}
	
}
