package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.content.dialogue.impl.OptionDialogue;
import net.arrav.content.dialogue.impl.StatementDialogue;
import net.arrav.content.skill.construction.Construction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.object.GameObject;

public class HousePortal extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction e = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(true) {
					player.message("Construction coming soon.");
					return true;
				}
				player.getDialogueBuilder().append(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						if(!Construction.hasHouse(player)) {
							player.getDialogueBuilder().go(1);
						} else {
							Construction.enterHouse(player, false);
						}
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						if(!Construction.hasHouse(player)) {
							player.getDialogueBuilder().go(1);
						} else {
							Construction.enterHouse(player, true);
						}
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						//TODO
						player.closeWidget();
					}
				}, "Enter your house", " Enter your house (building mode)", "Enter friend's house"), new StatementDialogue("You currently don't own a house,", "want to buy one for 5M?"), new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						Construction.buyHouse(player);
					}
					player.closeWidget();
				}, "Yes I'll buy", " No, too expensive."));
				return true;
			}
		};
		e.registerFirst(15478);
	}
}
