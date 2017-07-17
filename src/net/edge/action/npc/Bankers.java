package net.edge.action.npc;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.NpcAction;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

public class Bankers extends ActionInitializer {
	@Override
	public void init() {
		NpcAction e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				player.getBank().open();
				return true;
			}
		};
		e.registerFirst(494);
		e.registerFirst(7605);
		e.registerSecond(494);
		e.registerSecond(375);
		e.registerSecond(2380);
		e.registerThird(494);
	}
}
