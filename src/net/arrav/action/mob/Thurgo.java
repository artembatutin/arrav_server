package net.arrav.action.mob;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.MobAction;
import net.arrav.content.dialogue.impl.NpcDialogue;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

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
