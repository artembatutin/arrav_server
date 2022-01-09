package com.rageps.action.mob;

import com.rageps.action.impl.MobAction;
import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

public class Thurgo extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				player.getDialogueBuilder().append(new NpcDialogue(604, "Beautiful day in this dark cave, isn't it?"));
				return true;
			}
		};
		e.registerFirst(604);
	}
}
