package net.edge.action.item;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ItemAction;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 7-7-2017.
 */
public class BookOfDiplomacy extends ActionInitializer {
	@Override
	public void init() {
		ItemAction e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(!player.isIronMan()) {
					return true;
				}
				player.getDialogueBuilder().append(new StatementDialogue("You want to quit the iron man mode?"), new OptionDialogue(t -> {
					if(t == OptionDialogue.OptionType.FIRST_OPTION) {
						player.setIron(0, true);
//                        player.teleport(GameConstants.STARTING_POSITION); TODO: add teleport
					}
					player.closeWidget();
				}, "Yes, want to be a regular player.", "No, I want to keep the iron man mode."));
				return true;
			}
		};
		e.register(21432);
	}
}
