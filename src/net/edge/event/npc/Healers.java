package net.edge.event.npc;

import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

public class Healers extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
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
