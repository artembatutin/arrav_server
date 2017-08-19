package net.edge.content.commands.impl;

import net.edge.content.clanchat.ClanManager;
import net.edge.content.market.MarketCounter;
import net.edge.content.market.MarketItem;
import net.edge.content.scoreboard.ScoreboardManager;
import net.edge.net.PunishmentHandler;
import net.edge.net.packet.in.MobInformationPacket;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.drop.Drop;
import net.edge.world.entity.actor.mob.drop.DropManager;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.PlayerSerialization;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.item.ItemDefinition;

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
			case "drops":
				DropManager.serializeDrops();
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter("./data/suggested_drops.txt", true));
					for(Drop d : MobInformationPacket.SUGGESTED) {
						out.write(d.toString());
						out.newLine();
					}
					MobInformationPacket.SUGGESTED.clear();
					out.close();
				} catch(Exception e) {
				}
				player.message("Serialized drops!");
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
				PunishmentHandler.saveIpBan();
				PunishmentHandler.saveIPMute();
				break;
			default:
				player.message("Possible choices: players, drops, shops, market, clans, board, itemdefs, ips.");
				break;
		}
	}
}
