package net.arrav.action.mob;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.MobAction;
import net.arrav.content.dialogue.Expression;
import net.arrav.content.dialogue.impl.NpcDialogue;
import net.arrav.content.dialogue.impl.OptionDialogue;
import net.arrav.content.dialogue.impl.PlayerDialogue;
import net.arrav.content.dialogue.test.DialogueAppender;
import net.arrav.content.market.MarketCounter;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

public class PetOwner extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				DialogueAppender pets = new DialogueAppender(player);
				pets.chain(new NpcDialogue(6892, "Hello " + player.getFormatUsername() + ", lovely day isn't it?"));
				pets.chain(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.closeWidget();
						MarketCounter.getShops().get(25).openShop(player);
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						pets.getBuilder().advance();
					} else {
						pets.getBuilder().last();
					}
				}, "Yes, can i buy a pet?", "You don't have what I'm looking for", "Nevermind"));
				pets.chain(new PlayerDialogue(Expression.QUESTIONING, "You don't have much choices", "in your shop do you?"));
				pets.chain(new NpcDialogue(6892, "Suggest other pets on the forums", "if you wish!"));
				pets.chain(new PlayerDialogue("Nevermind..."));
				pets.start();
				return true;
			}
		};
		e.registerFirst(6892);
	}
}
