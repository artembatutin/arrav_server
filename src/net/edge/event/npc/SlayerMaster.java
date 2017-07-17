package net.edge.event.npc;

import net.edge.content.skill.slayer.Slayer;
import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

public class SlayerMaster extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				Slayer.openPanel(player);
				return true;
			}
		};
		e.registerSecond(8462);
	}
}
