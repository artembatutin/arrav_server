package com.rageps.action.mob;

import com.rageps.action.impl.MobAction;
import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.impl.PlayerDialogue;
import com.rageps.content.teleport.impl.AuburyTeleport;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

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
