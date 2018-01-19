package net.arrav.action.mob;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.MobAction;
import net.arrav.content.dialogue.impl.NpcDialogue;
import net.arrav.content.dialogue.impl.OptionDialogue;
import net.arrav.content.dialogue.impl.PlayerDialogue;
import net.arrav.content.dialogue.test.DialogueAppender;
import net.arrav.content.market.MarketCounter;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

public class Hazelmere extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				DialogueAppender a = new DialogueAppender(player);
				a.chain(new NpcDialogue(669, "Hey " + player.getFormatUsername() + ", what do you need?"));
				a.chain(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						a.getBuilder().skip();
					} else {
						a.getBuilder().advance();
					}
				}, "Open shop", "Nevermind"));
				a.chain(new PlayerDialogue("Nevermind, I forgot what I wanted to ask.").attachAfter(() -> player.closeWidget()));
				a.chain(new PlayerDialogue("Can I see your shop?"));
				a.chain(new NpcDialogue(669, "Ofcourse!").attachAfter(() -> MarketCounter.getShops().get(0).openShop(player)));
				a.start();
				return true;
			}
		};
		e.registerFirst(669);
	}
}
