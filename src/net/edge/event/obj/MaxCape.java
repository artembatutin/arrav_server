package net.edge.event.obj;

import net.edge.content.dialogue.Dialogue;
import net.edge.content.dialogue.Expression;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.impl.RequestItemDialogue;
import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.content.skill.Skills;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.object.ObjectNode;

import java.util.Optional;

public class MaxCape extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent e = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				DialogueAppender app = new DialogueAppender(player);
				app.chain(new StatementDialogue("You investigate the mysterious cape that is hanging on the rack..."));
				boolean maxed = Skills.maxed(player);
				Dialogue dialogue = maxed ? new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						app.getBuilder().advance();
					} else {
						player.closeWidget();
					}
				}, "Claim max cape for 100,000 coins", "Nevermind") : new PlayerDialogue(Expression.CONFUSED, "A strange feeling is telling me I do not meet", "the requirements for this cape.").attachAfter(() -> player.closeWidget());
				app.chain(dialogue);
				app.chain(new RequestItemDialogue(new Item(995, 100_000), Optional.of(new Item(20769)), "You offer your coins up and receive the cape in return.", Optional.empty(), true));
				app.start();
				return true;
			}
		};
		e.registerFirst(44566);
	}
}
