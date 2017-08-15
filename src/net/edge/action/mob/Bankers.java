package net.edge.action.mob;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.MobAction;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

public class Bankers extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				player.getBank().open();
				return true;
			}
		};
		e.registerFirst(953);
		e.registerFirst(494);
		e.registerFirst(7605);
		e.registerSecond(494);
		e.registerSecond(375);
		e.registerSecond(2380);
		e.registerThird(494);
	}
}
