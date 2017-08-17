package net.edge.content.skill.hunter.trap.mammal;

import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.content.skill.Skills;
import net.edge.content.skill.hunter.trap.Trap;
import net.edge.world.entity.actor.mob.MobType;
import net.edge.world.entity.region.TraversalMap;
import net.edge.world.locale.Position;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.object.GameObject;

import java.util.Optional;

import static net.edge.content.achievements.Achievement.BOX_TRAPPER;

/**
 * The box trap implementation of the {@link Trap} class which represents a single box trap.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BoxTrap extends Trap {
	
	/**
	 * Constructs a new {@link BoxTrap}.
	 * @param player {@link #getPlayer()}.
	 */
	public BoxTrap(Player player) {
		super(player, TrapType.BOX_TRAP);
	}
	
	/**
	 * The npc trapped inside this box.
	 */
	private Optional<Mammal> trapped = Optional.empty();
	
	/**
	 * Determines if an animal is going to the trap.
	 */
	private Optional<Task> event = Optional.empty();
	
	/**
	 * The object identification for a caught box trap.
	 */
	private static final int CAUGHT_ID = 19190;
	
	/**
	 * The distance the npc has to have from the box trap before it gets triggered.
	 */
	private static final int DISTANCE_PORT = 2;
	
	/**
	 * Kills the specified {@code mob}.
	 * @param mammal the mob to kill.
	 */
	private void kill(Mammal mammal) {
		mammal.move(new Position(0, 0, 0));
		mammal.appendDeath();
		trapped = Optional.of(mammal);
	}
	
	private void skip(Mob mob) {
		Position pos = TraversalMap.getRandomNearby(mob.getPosition(), mob.getPosition(), 1);
		if(pos != null) {
			mob.getMovementQueue().walk(pos);
		}
	}
	
	@Override
	public boolean canCatch(Mob mob) {
		return mob.getMobType() == MobType.HUNTING_MAMMAL;
	}
	
	@Override
	public void onPickUp() {
		player.message("You pick up your box trap.");
	}
	
	@Override
	public void onSetup() {
		player.message("You set-up your box trap.");
	}
	
	@Override
	public void onCatch(Mob mob) {
		if(event.isPresent()) {
			return;
		}
		Mammal mammal = (Mammal) mob;
		setState(TrapState.CATCHING, mob);
		event = Optional.of(new Task(1, false) {
			
			@Override
			public void execute() {
				mob.getMovementQueue().walk(getObject().getGlobalPos().copy());
				if(isAbandoned()) {
					this.cancel();
					return;
				}
				if(mob.getPosition().same(getObject().getGlobalPos())) {
					this.cancel();
					if(player.getSkills()[Skills.HUNTER].getLevel() < mammal.getData().requirement) {
						setState(TrapState.FALLEN, mob);
						return;
					}
					int count = RandomUtils.inclusive(180);
					int formula = successFormula(mob);
					if(count > formula) {
						setState(TrapState.FALLEN, mob);
						return;
					}
					kill(mammal);
					updateObject(CAUGHT_ID);
					setState(TrapState.CAUGHT, mob);
					BOX_TRAPPER.inc(player, 1);
				}
			}
			
			@Override
			public void onCancel() {
				event = Optional.empty();
			}
		});
		World.get().submit(event.get());
	}
	
	@Override
	public void onSequence(Task t) {
		for(Mob mob : player.getLocalMobs()) {
			if(mob == null || mob.isDead()) {
				continue;
			}
			if(this.getObject().getGlobalPos().withinDistance(mob.getPosition(), DISTANCE_PORT)) {
				if(this.isAbandoned()) {
					return;
				}
				trap(mob);
			}
		}
	}
	
	@Override
	public Item[] reward() {
		if(!trapped.isPresent()) {
			throw new IllegalStateException("No npc is trapped.");
		}
		return trapped.get().getData().reward;
	}
	
	@Override
	public double experience() {
		if(!trapped.isPresent()) {
			throw new IllegalStateException("No npc is trapped.");
		}
		return trapped.get().getData().experience;
	}
	
	@Override
	public boolean canClaim(GameObject object) {
		return trapped.isPresent();
		
	}
	
	@Override
	public void setState(TrapState state, Mob mob) {
		if(state.equals(TrapState.CATCHING)) {
			super.setState(state, mob);
			return;
		}
		if(state.equals(TrapState.PENDING)) {
			throw new IllegalArgumentException("Cannot set trap state back to pending.");
		}
		if(state.equals(TrapState.FALLEN)) {
			this.updateObject(TrapType.FAILED_BOX_TRAP.getObjectId());
			skip(mob);
		}
		player.message("Your trap has been triggered by something...");
		super.setState(state, mob);
	}
	
}