package net.edge.action.npc;

import net.edge.content.skill.slayer.Slayer;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.NpcAction;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

public class SlayerMaster extends ActionInitializer {
	@Override
	public void init() {
		NpcAction e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				Slayer.openPanel(player);
				return true;
			}
		};
		e.registerSecond(8462);
	}
}
