package net.edge.action.mob;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.MobAction;
import net.edge.content.dialogue.Expression;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.impl.RequestItemDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.content.market.MarketCounter;
import net.edge.content.minigame.rfd.RFDData;
import net.edge.content.minigame.rfd.RFDMinigame;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

public class Culinaromancer extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				DialogueAppender ap = new DialogueAppender(player);

				ap.chain(new NpcDialogue(3400, Expression.MAD, "Ergh... What do you want?"));
				ap.chain(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getDialogueBuilder().advance();
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.getDialogueBuilder().go(8);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.getDialogueBuilder().go(10);
					} else {
						player.getDialogueBuilder().last();
					}
				}, "Who are you?", "Can I see your shop?", "Can I teleport to your minigame?", "Nevermind."));
				ap.chain(new PlayerDialogue("Who are you?"));
				ap.chain(new NpcDialogue(3400, Expression.MAD, "I am the Culinaromancer! I absorb my power from food!", "I will destroy the council and that son of a", "b!#$* who sealed me away!"));
				ap.chain(new PlayerDialogue("This is just a remake, take it easy!"));
				ap.chain(new NpcDialogue(3400, "Ugh... I guess you are right, I shouldn't take my", "role so seriously."));
				ap.chain(new PlayerDialogue("So... what do you do?"));
				ap.chain(new NpcDialogue(3400, "I sell gloves, untradable should I mention, to players.", "However one must participate to my minigame and for", "each wave one completes, 2 types of gloves will be unlocked."));
				ap.chain(new PlayerDialogue(Expression.HAPPY, "Ah cool, i'll participate sometime soon...").attachAfter(() -> player.closeWidget()));
				ap.chain(new PlayerDialogue("Can I see your shop?"));
				ap.chain(new NpcDialogue(3400, "Ofcourse you can!").attachAfter(() -> {
					player.closeWidget();
					MarketCounter.getShops().get(3).openShop(player);
				}));
				ap.chain(new PlayerDialogue("Can I teleport to your minigame?"));
				if(player.getAttr().get("rfd_wave").get().equals(RFDData.WAVE_SIX)) {
					ap.chain(new NpcDialogue(3400, "You have already completed my minigame.").attachAfter(() -> player.closeWidget()));
				} else {
					ap.chain(new NpcDialogue(3400, "Yeah, but it will cost you 10k to be teleported."));
					ap.chain(new PlayerDialogue("Ehhh let me think about it..."));
					ap.chain(new OptionDialogue(t -> {
						if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
							player.getDialogueBuilder().advance();
						} else {
							player.getDialogueBuilder().go(4);
						}
					}, "Pay the fee.", "Don't pay the fee."));
					ap.chain(new PlayerDialogue("Alright, i'll pay the fee."));
					ap.chain(new NpcDialogue(3400, "Very well, goodluck in there!"));
					ap.chain(new RequestItemDialogue(new Item(995, 10_000), "You hand the Culinaromancer 10,000gp to be \\nteleported to the minigame", Optional.of(() -> {
						RFDMinigame.enterRFD(player);
					})).attachAfter(() -> player.closeWidget()));
					ap.chain(new PlayerDialogue("Nah, that's too expensive...").attachAfter(() -> player.closeWidget()));
				}
				ap.chain(new PlayerDialogue("Nevermind."));
				ap.start();
				return true;
			}
		};
		e.registerFirst(3400);
	}
}
