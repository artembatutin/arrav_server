package net.edge.event.npc;

import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.content.market.MarketCounter;
import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

public class Hazelmere extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
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
