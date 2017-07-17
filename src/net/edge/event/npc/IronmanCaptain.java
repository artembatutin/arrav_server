package net.edge.event.npc;

import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.content.market.MarketCounter;
import net.edge.content.skill.Skills;
import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.world.Graphic;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

public class IronmanCaptain extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				if(click == 1) {
					DialogueAppender night = new DialogueAppender(player);
					night.chain(new NpcDialogue(3705, "Evening " + player.getFormatUsername() + ", what do you want?"));
					night.chain(new OptionDialogue(t -> {
						if (t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
							night.getBuilder().skip();
						} else if (t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
							night.getBuilder().advance();
						} else {
							night.getBuilder().last();
						}
					}, "Who are you?", "I'd like to see your shop", "Nevermind"));
					night.chain(new PlayerDialogue("I'd like to see your shop.").attachAfter(() -> {
						player.closeWidget();
						MarketCounter.getShops().get(24).openShop(player);
					}));
					night.chain(new PlayerDialogue("Who are you?"));
					night.chain(new NpcDialogue(3705, "I am the Night's watch captain, it is thanks to me", "that the nightmare mode is available for players like you.", "Oh and I also sell interesting items in my shop..."));
					night.chain(new NpcDialogue(3705, "Would you perhaps want to take a look?"));
					night.chain(new OptionDialogue(t -> {
						if (t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
							night.getBuilder().skip();
						} else {
							night.getBuilder().advance();
						}
					}, "Sure", "No"));
					night.chain(new PlayerDialogue("No thank you...").attachAfter(() -> player.closeWidget()));
					night.chain(new PlayerDialogue("Yeah sure...").attachAfter(() -> {
						player.closeWidget();
						MarketCounter.getShops().get(24).openShop(player);
					}));
					night.chain(new PlayerDialogue("Nevermind..."));
					night.start();
				} else if(click == 2) {
					MarketCounter.getShops().get(24).openShop(player);
				} else if(click == 4) {
					DialogueAppender ap = new DialogueAppender(player);
					boolean maxed = Skills.maxed(player);
					String[] message = maxed ? new String[]{"Very well warrior, I see that you've accomplished maxing", "out all of your skills. Would you like to prestige to remove", "your restrictions?"} : new String[]{"You have not yet maxed all of your skills warrior."};
					ap.chain(new NpcDialogue(3705, message).attachAfter(() -> {
						if(!maxed) {
							player.closeWidget();
						}
					}));
					ap.chain(new OptionDialogue(t -> {
						if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
							ap.getBuilder().skip();
						} else {
							ap.getBuilder().advance();
						}
					}, "Yes please", "No thanks"));
					ap.chain(new PlayerDialogue("No thanks...").attachAfter(() -> player.closeWidget()));
					ap.chain(new PlayerDialogue("Yes, please..."));
					ap.chain(new NpcDialogue(3705, "Very well, you've been prestiged and your restrictions have", "been removed...").attach(() -> {
						player.graphic(new Graphic(2189));
						player.setIron(2, true);
					}));
					ap.start();
				}
				return true;
			}
		};
		e.registerFirst(3705);
		e.registerSecond(3705);
		e.registerFourth(3705);
	}
}
