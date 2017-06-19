package net.edge.event.obj;

import net.edge.content.dialogue.impl.*;
import net.edge.content.skill.construction.Construction;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import java.util.Optional;

public class HousePortal extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent e = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
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
						player.getMessages().sendCloseWindows();
					}
				}, "Enter your house", " Enter your house (building mode)", "Enter friend's house"),
				new StatementDialogue("You currently don't own a house,", "want to buy one for 5M?"),
				new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						Construction.buyHouse(player);
					}
					player.getMessages().sendCloseWindows();
				}, "Yes I'll buy", " No, too expensive."));
				return true;
			}
		};
		e.registerFirst(15478);
	}
}
