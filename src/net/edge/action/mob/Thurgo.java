package net.edge.action.mob;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.MobAction;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

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
