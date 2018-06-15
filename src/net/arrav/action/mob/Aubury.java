package net.arrav.action.mob;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.MobAction;
import net.arrav.content.dialogue.impl.NpcDialogue;
import net.arrav.content.dialogue.impl.OptionDialogue;
import net.arrav.content.dialogue.impl.PlayerDialogue;
import net.arrav.content.teleport.impl.AuburyTeleport;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

public class Aubury extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				if(click == 1) {
					player.getDialogueBuilder().append(new NpcDialogue(5913, "Hello " + player.getFormatUsername() + ", I am Aubury."), new OptionDialogue(t -> {
						if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
							player.getDialogueBuilder().advance();
						} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
							player.getDialogueBuilder().go(3);
						} else {
							player.getDialogueBuilder().last();
						}
					}, "What do you do?", "Teleport me to the Essence Mine.", "Nevermind."), new PlayerDialogue("What do you do?"), new NpcDialogue(5913, "What I do you ask? I can do anything, but my powers are", "limited for Avarrockians, for you all I can do is teleport", "you to the Essence mine or sell you the mastery skillcape", "of Runecrafting.").attachAfter(() -> player.closeWidget()), new PlayerDialogue("Can you teleport me to the essence mine?"), new NpcDialogue(5913, "Ofcourse I can!").attachAfter(() -> AuburyTeleport.move(player, npc)), new PlayerDialogue("Nevermind."));
				} else if(click == 3) {
					AuburyTeleport.move(player, npc);
					return true;
				}
				return true;
			}
		};
		e.registerFirst(5913);
		e.registerThird(5913);
	}
}
