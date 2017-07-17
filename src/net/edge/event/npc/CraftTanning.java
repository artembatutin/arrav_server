package net.edge.event.npc;

import net.edge.content.skill.crafting.Tanning;
import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

public class CraftTanning extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				Tanning.openInterface(player);
				return true;
			}
		};
		e.registerFourth(805);
	}
}
