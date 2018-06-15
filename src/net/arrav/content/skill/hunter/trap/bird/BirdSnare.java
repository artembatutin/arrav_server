package net.arrav.content.skill.hunter.trap.bird;

import net.arrav.content.skill.Skills;
import net.arrav.content.skill.hunter.trap.Trap;
import net.arrav.task.Task;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.World;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.mob.MobType;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.region.TraversalMap;
import net.arrav.world.locale.Position;
import net.arrav.world.entity.object.GameObject;

import java.util.Optional;

import static net.arrav.content.achievements.Achievement.FEATHERING;

/**
 * The bird snare implementation of the {@link Trap} class which represents a single bird snare.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BirdSnare extends Trap {

	/**
	 * Constructs a new {@link BirdSnare}.
	 * @param player {@link #getPlayer()}.
	 */
	public BirdSnare(Player player) {
		super(player, TrapType.BIRD_SNARE);
	}

	/**
	 * The npc trapped inside this box.
	 */
	private Optional<Bird> trapped = Optional.empty();

	/**
	 * Determines if a bird is going to the trap.
	 */
	private Optional<Task> event = Optional.empty();

	/**
	 * The distance the npc has to have from the snare before it gets triggered.
	 */
	private static final int DISTANCE_PORT = 3;

	/**
	 * Kills the specified {@code mob}.
	 * @param mob the mob to kill.
	 */
	private void kill(Bird mob) {
		mob.move(new Position(0, 0, 0));
		mob.appendDeath();
		trapped = Optional.of(mob);
	}

	private void skip(Mob mob) {
		Position pos = TraversalMap.getRandomNearby(mob.getPosition(), mob.getPosition(), 1);
		if(pos != null) {
			mob.getMovementQueue().walk(pos);
		}
	}

	@Override
	public boolean canCatch(Mob mob) {
		return mob.getMobType() == MobType.HUNTING_BIRD;
	}

	@Override
	public void onPickUp() {
		player.message("You pick up your bird snare.");
	}

	@Override
	public void onSetup() {
		player.message("You set-up your bird snare.");
	}

	@Override
	public void onCatch(Mob mob) {
		if(RandomUtils.inclusive(100) < 20) {
			return;
		}
		if(event.isPresent()) {
			return;
		}

		Bird bird = (Bird) mob;
		setState(TrapState.CATCHING, mob);
		event = Optional.of(new Task(1, false) {

			@Override
			public void execute() {
				if(!getState().equals(TrapState.CATCHING)) {
					this.cancel();
					return;
				}
				mob.getMovementQueue().walk(getObject().getPosition().copy());
				if(isAbandoned()) {
					this.cancel();
					return;
				}
				if(mob.getPosition().getX() == getObject().getX() && mob.getPosition().getY() == getObject().getY()) {
					this.cancel();
					if(player.getSkills()[Skills.HUNTER].getLevel() < bird.getData().requirement) {
						setState(TrapState.FALLEN, mob);
						return;
					}
					int count = RandomUtils.inclusive(150);
					int formula = successFormula(mob);
					if(count > formula) {
						setState(TrapState.FALLEN, mob);
						return;
					}
					kill(bird);
					updateObject(bird.getData().objectId);
					setState(TrapState.CAUGHT, mob);
					FEATHERING.inc(player);
				}
			}

			@Override
			public void onCancel() {
				event = Optional.empty();
			}
		});

		event.ifPresent(task -> World.get().submit(task));
	}

	@Override
	public void onSequence(Task t) {
		if(this.isAbandoned()) {
			return;
		}
		for(Mob mob : player.getLocalMobs()) {
			if(mob == null || mob.isDead()) {
				continue;
			}
			if(this.getObject().getPosition().withinDistance(mob.getPosition(), DISTANCE_PORT)) {
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
			this.updateObject(TrapType.FAILED_BIRD_SNARE.getObjectId());
			skip(mob);
		}
		player.message("Your trap has been triggered by something...");
		super.setState(state, mob);
	}

}
