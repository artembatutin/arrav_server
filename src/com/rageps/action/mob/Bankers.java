package com.rageps.action.mob;

import com.rageps.action.impl.MobAction;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

public class Bankers extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				player.getBank().open(false);
				return true;
			}
		};
		e.registerFirst(953);
		e.registerFirst(494);
		e.registerFirst(7605);
		e.registerSecond(494);
		e.registerSecond(375);
		e.registerSecond(2380);
		e.registerThird(494);
	}
}
