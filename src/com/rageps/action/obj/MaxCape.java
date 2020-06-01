package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.dialogue.Dialogue;
import com.rageps.content.dialogue.Expression;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.impl.PlayerDialogue;
import com.rageps.content.dialogue.impl.RequestItemDialogue;
import com.rageps.content.dialogue.impl.StatementDialogue;
import com.rageps.content.dialogue.test.DialogueAppender;
import com.rageps.content.skill.Skills;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.object.GameObject;

import java.util.Optional;

public class MaxCape extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction e = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
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
