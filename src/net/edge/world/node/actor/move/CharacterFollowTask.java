package net.edge.world.node.actor.move;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.Combat;
import net.edge.content.skill.summoning.Summoning;
import net.edge.locale.Boundary;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.NodeState;
import net.edge.world.node.actor.Actor;
import net.edge.world.node.actor.move.path.Path;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

import java.util.Optional;

/**
 * The {@link Task} implementation that handles the entire following process.
 * @author lare96 <http://github.com/lare96>
 */
class CharacterFollowTask extends Task {
	
	/**
	 * The character this process is being executed for.
	 */
	private final Actor character;
	
	/**
	 * The character being followed in this process.
	 */
	private final Actor leader;
	
	/**
	 * The current destination of the path.
	 */
	private Position destination;
	
	/**
	 * Creates a new {@link CharacterFollowTask}.
	 * @param character the character this process is being executed for.
	 * @param leader    the character being followed in this process.
	 */
	CharacterFollowTask(Actor character, Actor leader) {
		super(1, true);
		this.character = character;
		this.leader = leader;
	}
	
	@Override
	public void execute() {
		//First checks.
		if(character.getState() != NodeState.ACTIVE || leader.getState() != NodeState.ACTIVE || !character.isFollowing() || character.isDead() || leader.isDead()) {//Death and away check.
			character.faceEntity(null);
			character.setFollowing(false);
			character.setFollowEntity(null);
			this.cancel();
			return;
		}
		
		//Familiar calling back.
		if(character.isNpc()) {
			Mob mob = character.toNpc();
			if(mob.isFamiliar() || mob.isPet()) {
				if(!character.getPosition().withinDistance(leader.getPosition(), 15)) {
					if(leader.isPlayer()) {
						if(mob.isFamiliar()) {
							Summoning.callFamiliar(leader.toPlayer());
						} else if(mob.isPet()) {
							Summoning.callPet(leader.toPlayer());
						}
					}
				}
			}
		}
		
		
		
		//Entity facing.
		if(character.getFaceIndex() == 65535)
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
		
		Boundary boundary = new Boundary(leader.getPosition(), leader.size());
		
		//Randomized walk away from leader's tile.
		if(boundary.inside(character.getPosition(), character.size())) {
			//Only moving when movement is done.
			if(character.getMovementQueue().isMovementDone()) {
				character.getMovementQueue().reset();
				ObjectList<Position> pos = World.getTraversalMap().getSurroundedTraversableTiles(leader.getPosition(), leader.size(), character.size());
				if(pos.size() > 0) {
					Position p = RandomUtils.random(pos);
					character.getMovementQueue().walk(p);
				}
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
		if(boundary.within(character.getPosition(), character.size(), 1)) {
			character.getMovementQueue().reset();
			//Combat diagonal fighting.
			/*if(character.getCombatBuilder().isAttacking()) {
				Direction facing = Direction.fromDeltas(Position.delta(character.getPosition(), leader.getPosition()));
				if(facing.isDiagonal()) {//Moving player if diagonal fighting
					Position pos = World.getTraversalMap().getRandomNearby(character.getPosition(), leader.getPosition(), character.size());
					if(pos != null)
						character.getMovementQueue().walk(pos);
				}
				return;
			}
			return;*/
		}
		
		//returns if path calculated is next to the leader.
		if(destination != null && boundary.within(destination, leader.size(), character.size())) {
			return;
		}
		
		
		//Setting new path depending on the follower's type.
		Path path = character.isPlayer() || (character.isNpc() && character.toNpc().isSmart()) ? character.getAStarPathFinder().find(leader.getPosition()) : World.getSimplePathFinder().find(character, leader.getPosition());
		if(path != null && path.isPossible()) {
			//removing the points overlapping the leader's boundaries.
			while(boundary.inside(path.poll(), leader.size()));
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
	
	public void setDestination(Position destination) {
		this.destination = destination;
	}
	
	@Override
	public void onException(Exception e) {
		onCancel();
	}
	
}
