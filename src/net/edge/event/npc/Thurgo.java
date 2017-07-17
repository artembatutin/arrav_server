package net.edge.event.npc;

import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

public class Thurgo extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				player.getDialogueBuilder().append(new NpcDialogue(604, "Beautiful day in this dark cave, isn't it?"));
				return true;
			}
		};
		e.registerFirst(604);
	}
}
