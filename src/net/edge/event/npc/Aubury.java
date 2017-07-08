package net.edge.event.npc;

import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.teleport.impl.AuburyTeleport;
import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

public class Aubury extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				if(click == 1) {
					player.getDialogueBuilder().append(new NpcDialogue(5913, "Hello " + player.getFormatUsername() + ", I am Aubury."), new OptionDialogue(t -> {
						if (t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
							player.getDialogueBuilder().advance();
						} else if (t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
							player.getDialogueBuilder().go(3);
						} else {
							player.getDialogueBuilder().last();
						}
					}, "What do you do?", "Teleport me to the Essence Mine.", "Nevermind."), new PlayerDialogue("What do you do?"), new NpcDialogue(5913, "What I do you ask? I can do anything, but my powers are", "limited for Avarrockians, for you all I can do is teleport", "you to the Essence mine or sell you the mastery skillcape", "of Runecrafting.").attachAfter(() -> player.getMessages().sendCloseWindows()), new PlayerDialogue("Can you teleport me to the essence mine?"), new NpcDialogue(5913, "Ofcourse I can!").attachAfter(() -> AuburyTeleport.move(player, npc)), new PlayerDialogue("Nevermind."));
				} else if(click == 3) {
					AuburyTeleport.move(player, npc);return true;
				}
				return true;
			}
		};
		e.registerFirst(5913);
		e.registerThird(5913);
	}
}
