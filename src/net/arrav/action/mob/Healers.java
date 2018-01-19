package net.arrav.action.mob;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.MobAction;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

public class Healers extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				player.heal();
				return true;
			}
		};
		e.registerFirst(960);
		e.registerFirst(959);
		e.registerFirst(962);
		e.registerSecond(960);
		e.registerSecond(959);
		e.registerSecond(962);
		e.registerFourth(961);
	}
}
