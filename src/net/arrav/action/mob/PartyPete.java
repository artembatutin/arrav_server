package net.arrav.action.mob;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.MobAction;
import net.arrav.content.dialogue.Expression;
import net.arrav.content.dialogue.impl.NpcDialogue;
import net.arrav.content.dialogue.impl.OptionDialogue;
import net.arrav.content.dialogue.impl.PlayerDialogue;
import net.arrav.content.market.MarketCounter;
import net.arrav.net.database.connection.use.Donating;
import net.arrav.net.packet.out.SendLink;
import net.arrav.world.World;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

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
						player.out(new SendLink("store/"));
						player.getDialogueBuilder().last();
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						new Donating(player, World.getDonation()).submit();
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
