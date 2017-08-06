package net.edge.action.mob;

import net.edge.content.skill.crafting.Tanning;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.MobAction;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

public class CraftTanning extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				Tanning.openInterface(player);
				return true;
			}
		};
		e.registerFourth(805);
	}
}
