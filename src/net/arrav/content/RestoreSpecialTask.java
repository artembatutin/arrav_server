package net.arrav.content;

import net.arrav.task.Task;
import net.arrav.world.World;
import net.arrav.world.entity.EntityState;
import net.arrav.world.entity.actor.combat.strategy.player.special.CombatSpecial;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

/**
 * The class that handles the restoration special percentages.
 * @author Artem Batutin <artembatutin@gmail.com></artembatutin@gmail.com>
 */
public final class RestoreSpecialTask extends Task {
	
	
	/**
	 * Creates a new {@link RestoreSpecialTask}.
	 */
	public RestoreSpecialTask() {
		super(15, false);
	}
	
	@Override
	public void execute() {
		if(World.get().getPlayers().isEmpty())
			return;
		for(Player player : World.get().getPlayers()) {
			if(player == null) {
				continue;
			}
			if(player.getState() != EntityState.ACTIVE) {
				continue;
			}
			if(player.isDead()) {
				continue;
			}
			if(player.getSpecialPercentage().get() < 100) {
				if(player.getRights().equal(Rights.ADMINISTRATOR)) {
					CombatSpecial.restore(player, 100);
					return;
				}
				CombatSpecial.restore(player, 5);
			}
		}
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new RestoreSpecialTask());
	}
}
