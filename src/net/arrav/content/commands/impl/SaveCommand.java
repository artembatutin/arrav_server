package net.arrav.content.commands.impl;

import net.arrav.content.clanchat.ClanManager;
import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.content.market.MarketCounter;
import net.arrav.content.market.MarketItem;
import net.arrav.content.scoreboard.ScoreboardManager;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.net.packet.in.MobInformationPacket;
import net.arrav.world.World;
import net.arrav.world.entity.actor.mob.drop.Drop;
import net.arrav.world.entity.actor.mob.drop.DropManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.PlayerSerialization;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.item.ItemDefinition;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
				ClanManager.get().save();
				player.message("Serialized shops!");
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
