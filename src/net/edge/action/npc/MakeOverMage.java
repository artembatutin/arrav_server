package net.edge.action.npc;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.NpcAction;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

public class MakeOverMage extends ActionInitializer {
	@Override
	public void init() {
		NpcAction e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				player.widget(3559);
				return true;
			}
		};
		e.registerFirst(599);
	}
}
