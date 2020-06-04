package com.rageps.content;

import com.rageps.world.World;
import com.rageps.task.Task;
import com.rageps.world.entity.EntityState;
import com.rageps.combat.strategy.player.special.CombatSpecial;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

/**
 * The class that handles the restoration special percentages.
 * @author Artem Batutin
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
