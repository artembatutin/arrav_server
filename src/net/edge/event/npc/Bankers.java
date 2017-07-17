package net.edge.event.npc;

import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

public class Bankers extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
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
