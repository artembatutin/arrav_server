package com.rageps.action.mob;

import com.rageps.action.impl.MobAction;
import com.rageps.content.dialogue.Expression;
import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.impl.PlayerDialogue;
import com.rageps.content.market.MarketCounter;
import com.rageps.action.ActionInitializer;
import com.rageps.net.packet.out.SendLink;
import com.rageps.net.refactor.packet.out.model.LinkPacket;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

public class PartyPete extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				player.getDialogueBuilder().append(new NpcDialogue(659, Expression.HAPPY, "Hello Adventurer, what do you seek from me", "today?"), new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getDialogueBuilder().advance();
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.send(new LinkPacket("store/"));
						player.getDialogueBuilder().last();
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.closeWidget();
					} else {
						player.closeWidget();
					}
				}, "I'd like to see the donator shop.", "I would like to buy some arrav tokens", "I wan't to claim my arrav tokens", "Nevermind."), new PlayerDialogue("I'd like to see the donator shop.").attachAfter(() -> {
					player.closeWidget();
					MarketCounter.getShops().get(1).openShop(player);
				}), new NpcDialogue(659, "You can buy credits from here.", "Come back to me to claim the credits once", "payment is done. Thank you."));
				return true;
			}
		};
		e.registerFirst(659);
	}
}
