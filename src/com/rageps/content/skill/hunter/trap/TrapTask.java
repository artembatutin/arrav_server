package com.rageps.content.skill.hunter.trap;

import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.content.skill.hunter.Hunter;
import com.rageps.task.Task;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.player.Player;

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
	 * Constructs a new {@link TrapTask}.
	 * @param player {@link #player}.
	 */
	public TrapTask(Player player) {
		super(10, false);
		this.player = player;
	}
	
	@Override
	public void execute() {
		if(Hunter.GLOBAL_TRAPS.get(player) == null || !Hunter.GLOBAL_TRAPS.get(player).getTask().isPresent() || Hunter.GLOBAL_TRAPS.get(player).getTraps().isEmpty()) {
			this.cancel();
			return;
		}
		ObjectList<Trap> traps = Hunter.GLOBAL_TRAPS.get(player).getTraps();
		//close if no traps.
		if(traps.isEmpty()) {
			this.cancel();
		} else {
			//distance check.
			traps.forEach(t -> {
				boolean withinDistance = player.getPosition().withinDistance(t.getObject().getPosition(), 25);
				if(!withinDistance && !t.isAbandoned()) {
					Hunter.abandon(player, t, false);
				}
			});
			//might have abandoned some traps.
			if(traps.isEmpty()) {
				this.cancel();
			} else {
				Trap trap = RandomUtils.random(traps);
				if(!Hunter.getTrap(player, trap.getObject()).isPresent() || !trap.getState().equals(Trap.TrapState.PENDING)) {
					return;
				}
				trap.onSequence(this);
			}
		}
	}
}
