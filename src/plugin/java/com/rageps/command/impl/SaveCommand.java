package com.rageps.command.impl;

import com.rageps.content.scoreboard.ScoreboardManager;
import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.content.market.MarketCounter;
import com.rageps.content.market.MarketItem;
import com.rageps.net.host.HostListType;
import com.rageps.net.host.HostManager;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerSerialization;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.item.ItemDefinition;

import java.util.Iterator;

@CommandSignature(alias = {"save"}, rights = {Rights.ADMINISTRATOR}, syntax = "Saves, ::save type")
public final class SaveCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		switch(cmd[1].toLowerCase()) {
			case "players":
				Player other;
				Iterator<Player> it = World.get().getPlayers().iterator();
				while((other = it.next()) != null) {
					new PlayerSerialization(other).serialize();
				}
				player.message("Character files have been saved for everyone online!");
				break;
			case "shops":
				MarketCounter.serializeShops();
				player.message("Serialized shops!");
				break;
			case "market"://included in hook.
				MarketItem.serializeMarketItems();
				player.message("Serialized market prices!");
				break;
			case "clans"://included in hook.
				//todo save clans
				player.message("Saved clans!");
				break;
			case "board"://included in hook.
				ScoreboardManager.get().serializeIndividualScoreboard();
				player.message("Serialized scoreboard statistics!");
				break;
			case "itemdefs":
				ItemDefinition.serializeDefinitions();
				player.message("Serialized item definitions!");
				break;
			case "ips":
				HostManager.serialize(HostListType.BANNED_MAC);
				HostManager.serialize(HostListType.BANNED_IP);
				HostManager.serialize(HostListType.MUTED_IP);
				HostManager.serialize(HostListType.STARTER_RECEIVED);
				player.message("Serialized IP hosts!");
				break;
			default:
				player.message("Possible choices: players, drops, shops, market, clans, board, itemdefs, ips.");
				break;
		}
	}
}
