package net.edge.action.npc;

import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.NpcAction;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

public class Thurgo extends ActionInitializer {
	@Override
	public void init() {
		NpcAction e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				player.getDialogueBuilder().append(new NpcDialogue(604, "Beautiful day in this dark cave, isn't it?"));
				return true;
			}
		};
		e.registerFirst(604);
	}
}
