package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.skill.Skill;
import com.rageps.content.teleport.TeleportType;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

public class HomePortal extends ActionInitializer {
	@Override
	public void init() {
		//Green portal at home
		ObjectAction portal = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.getDialogueBuilder().append(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.widget(-4);
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.widget(-6);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.widget(-9);
					} else if(t.equals(OptionDialogue.OptionType.FOURTH_OPTION)) {
						player.widget(-7);
					}
				}, "Skills", "Minigames", "Monsters", "Bosses"));
				return true;
			}
		};
		portal.registerFirst(28139);
		
		//Home staires
		ObjectAction staires = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(object.getPosition().same(new Position(3079, 3503, 0))) {
					if(!player.getRights().isStaff() && !player.getRights().isDonator()) {
						int total = 0;
						for(Skill s : player.getSkills())
							total += s.getRealLevel();
						if(total < 1000) {
							player.message("You need total level greater than 1000 to go up, or be a donator.");
							return true;
						}
					}
					player.teleport(new Position(3078, 3504, 1), TeleportType.LADDER);
				}
				return true;
			}
		};
		staires.registerFirst(2711);
		
		//Bossing hub staires
		ObjectAction hub = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.getDialogueBuilder().append(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.widget(-9);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.widget(-7);
					} else {
						player.closeWidget();
					}
				}, "@red@This will be clan bossing hub soon!", "Monsters", "Bosses"));
				return true;
			}
		};
		hub.registerFirst(2522);
		
	}
}
