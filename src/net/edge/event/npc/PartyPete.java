package net.edge.event.npc;

import net.edge.content.dialogue.Expression;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.market.MarketCounter;
import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.net.database.connection.use.Donating;
import net.edge.net.packet.out.SendLink;
import net.edge.world.World;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

public class PartyPete extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				player.getDialogueBuilder().append(new NpcDialogue(659, Expression.HAPPY, "Hello Adventurer, what do you seek from me", "today?"), new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getDialogueBuilder().advance();
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.out(new SendLink("store/"));
						player.getDialogueBuilder().last();
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						new Donating(player, World.getDonation());
						player.closeWidget();
					} else {
						player.closeWidget();
					}
				}, "I'd like to see the donator shop.", "I would like to buy some edge tokens", "I wan't to claim my edge tokens", "Nevermind."), new PlayerDialogue("I'd like to see the donator shop.").attachAfter(() -> {
					player.closeWidget();
					MarketCounter.getShops().get(1).openShop(player);
				}), new NpcDialogue(659, "You can buy credits from here.", "Come back to me to claim the credits once", "payment is done. Thank you."));
				return true;
			}
		};
		e.registerFirst(659);
	}
}
