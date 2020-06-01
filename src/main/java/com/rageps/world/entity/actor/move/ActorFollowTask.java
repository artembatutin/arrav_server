package com.rageps.world.entity.actor.move;

import com.rageps.content.skill.summoning.Summoning;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.move.path.Path;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.region.TraversalMap;
import com.rageps.world.locale.Boundary;
import com.rageps.world.locale.Position;
import com.rageps.task.Task;
import com.rageps.world.Direction;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.mob.Mob;

import java.util.Optional;

/**
 * The {@link Task} implementation that handles the entire following process.
 * @author lare96 <http://github.com/lare96>
 */
class ActorFollowTask extends Task {
	
	/**
	 * The actor this process is being executed for.
	 */
	private final Actor actor;
	
	/**
	 * The actor being followed in this process.
	 */
	private final Actor leader;
	
	/**
	 * The current destination of the path.
	 */
	private Position destination;
	
	/**
	 * Creates a new {@link ActorFollowTask}.
	 * @param actor the actor this process is being executed for.
	 * @param leader the actor being followed in this process.
	 */
	ActorFollowTask(Actor actor, Actor leader) {
		super(1, true);
		this.actor = actor;
		this.leader = leader;
	}
	
	@Override
	public void execute() {
		//First checks.
		boolean combat = actor.getCombat() != null && actor.getCombat().getLastDefender() != null && actor.getCombat().getLastDefender().same(leader);
		if(actor.getState() != EntityState.ACTIVE || leader.getState() != EntityState.ACTIVE || !actor.isFollowing() || actor.isDead() || leader.isDead()) {//Death and away check.
			if(!combat) {
				actor.faceEntity(null);
			}
			actor.setFollowing(false);
			actor.setFollowEntity(null);
			this.cancel();
			return;
		}
		
		//Familiar calling back.
		if(actor.isMob()) {
			Mob mob = actor.toMob();
			if(mob.isFamiliar() || mob.isPet()) {
				if(!actor.getPosition().withinDistance(leader.getPosition(), 15)) {
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
		actor.faceEntity(leader);
		
		//Movement locks.
		if(actor.getMovementQueue().isLockMovement() || actor.isFrozen() || actor.isStunned()) {//Requirement check.
			if(actor.isPlayer()) {
				Player player = actor.toPlayer();
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
		if(boundary.inside(actor.getPosition(), actor.size())) {
			//Only moving when movement is done.
			if(actor.getMovementQueue().isMovementDone()) {
				actor.getMovementQueue().reset();
				//outside of the leader's boundary but still nearby
				TraversalMap.traversablesNextToBoundary(leader.getPosition(), leader.size(), actor.size(), new Boundary(actor.getPosition(), actor.size()), p -> actor.getMovementQueue().walk(p));
			}
			return;
		}
		
		//combat within boundary
		CombatType type = CombatType.NONE;
		if(combat) {
			if(!actor.getPosition().withinDistance(leader.getPosition(), 45)) {
				actor.getCombat().reset(true, true);
				return;
			}
			if(actor.getStrategy() != null) {
				type = actor.getStrategy().getCombatType();
			}
			if(actor.getCombat().checkWithin() && (type == CombatType.RANGED || type == CombatType.MAGIC)) {
				actor.getMovementQueue().within = true;
				return;
			} else {
				actor.getMovementQueue().within = false;
			}
		}
		
		//Resets if actor next to the player.
		if((type == CombatType.NONE || type == CombatType.MELEE) && boundary.within(actor.getPosition(), actor.size(), 1)) {
			//Combat diagonal fighting.
			if(type == CombatType.MELEE) {
				Direction facing = Direction.fromDeltas(Position.delta(actor.getPosition(), leader.getPosition()));
				if(facing.isDiagonal()) {//Moving player if diagonal fighting
					Position pos = TraversalMap.getRandomNearby(leader.getPosition(), actor.getPosition(), actor.size());
					if(pos != null && pos.withinDistance(actor.getPosition(), 1)) {
						actor.getMovementQueue().within = false;
						actor.getMovementQueue().walk(pos);
						return;
					}
				}
				return;
			}
			actor.getMovementQueue().within = true;
			return;
		} else {
			actor.getMovementQueue().within = false;
		}
		//returns if path calculated is next to the leader.
		if(destination != null && boundary.within(destination, leader.size(), actor.size())) {
			return;
		}
		//Setting new path depending on the follower's type.
		Path path = actor.isPlayer() || (actor.isMob() && actor.toMob().isSmart()) ? World.getSmartPathfinder().find(actor.getPosition(), leader.getPosition(), actor.size()) : World.getSimplePathfinder().find(actor, leader.getPosition());
		if(path != null && path.isPossible()) {
			//removing the points overlapping the leader's boundaries. //TODO: fix or remove.
			//while(boundary.inside(path.poll(), leader.size()));
			actor.getMovementQueue().walk(path.getMoves());
			destination = path.getDestination();
		} else {
			actor.getMovementQueue().reset();
			destination = null;
		}
	}
	
	@Override
	public void onCancel() {
		destination = null;
		actor.setFollowing(false);
		actor.setFollowEntity(null);
		actor.getMovementQueue().within = false;
		actor.getMovementQueue().setFollowTask(Optional.empty());
	}
	
	public void setDestination(Position destination) {
		this.destination = destination;
	}
	
	@Override
	public void onException(Exception e) {
		e.printStackTrace();
		onCancel();
	}
	
}
