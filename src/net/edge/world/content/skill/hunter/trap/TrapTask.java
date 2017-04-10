package net.edge.world.content.skill.hunter.trap;

import net.edge.utils.rand.RandomUtils;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.content.skill.hunter.Hunter;
import net.edge.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single task which will run for each trap.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class TrapTask extends Task {

	/**
	 * The player this task is dependant of.
	 */
	private final Player player;

	/**
	 * The trap this task is running for.
	 */
	private List<Trap> trap = new ArrayList<>();

	/**
	 * Constructs a new {@link TrapTask}.
	 * @param player {@link #player}.
	 */
	public TrapTask(Player player) {
		super(10, false);
		this.player = player;
	}

	@Override
	public void onSequence() {
		if(Hunter.GLOBAL_TRAPS.get(player) == null || !Hunter.GLOBAL_TRAPS.get(player).getTask().isPresent() || Hunter.GLOBAL_TRAPS.get(player).getTraps().isEmpty()) {
			this.cancel();
			return;
		}

		this.trap.forEach(t -> {
			boolean withinDistance = player.getPosition().withinDistance(t.getObject().getPosition(), 25);
			if(!withinDistance && !t.isAbandoned()) {
				Hunter.abandon(player, t, false);
			}
		});

	}

	@Override
	public void execute() {
		this.trap.clear();
		this.trap.addAll(Hunter.GLOBAL_TRAPS.get(player).getTraps());

		Trap trap = RandomUtils.random(this.trap);
		if(!Hunter.getTrap(player, trap.getObject()).isPresent() || !trap.getState().equals(Trap.TrapState.PENDING)) {
			return;
		}

		trap.onSequence(this);
	}
}
