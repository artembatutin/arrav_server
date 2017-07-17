package net.edge.action.obj;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.ObjectNode;

public class HousePortal extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction e = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.message("Construction to be released soon.");
				/*player.getDialogueBuilder().append(new OptionDialogue(t -> {
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
				}, "Enter your house", " Enter your house (building mode)", "Enter friend's house"),
				new StatementDialogue("You currently don't own a house,", "want to buy one for 5M?"),
				new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						Construction.buyHouse(player);
					}
					player.closeWidget();
				}, "Yes I'll buy", " No, too expensive."));*/
				return true;
			}
		};
		e.registerFirst(15478);
	}
}
