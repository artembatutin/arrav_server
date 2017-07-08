package net.edge.event.npc;

import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

public class MakeOverMage extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				player.widget(3559);
				return true;
			}
		};
		e.registerFirst(599);
	}
}
