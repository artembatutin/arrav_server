package com.rageps.action.mob;

import com.rageps.action.impl.MobAction;
import com.rageps.content.skill.slayer.Slayer;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

public class SlayerMaster extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				Slayer.openPanel(player);
				return true;
			}
		};
		e.registerSecond(8462);
	}
}
