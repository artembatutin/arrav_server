package net.edge.content.commands.impl;

import net.edge.content.market.MarketCounter;
import net.edge.content.market.MarketItem;
import net.edge.net.packet.in.NpcInformationPacket;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.node.actor.npc.drop.NpcDrop;
import net.edge.world.node.actor.npc.drop.NpcDropManager;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.PlayerSerialization;
import net.edge.world.node.actor.player.assets.Rights;
import net.edge.world.node.item.ItemDefinition;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;

@CommandSignature(alias = {"save"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR}, syntax = "Use this command as ::save type")
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
				World.getClanManager().save();
				player.message("Character files have been saved for everyone online!");
				break;
			case "drops":
				NpcDropManager.serializeDrops();
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter("./data/suggested_drops.txt", true));
					for(NpcDrop d : NpcInformationPacket.SUGGESTED) {
						out.write(d.toString());
						out.newLine();
					}
					NpcInformationPacket.SUGGESTED.clear();
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
				World.getClanManager().save();
				player.message("Serialized shops!");
				break;
			case "board"://included in hook.
				World.getScoreboardManager().serializeIndividualScoreboard();
				player.message("Serialized scoreboard statistics!");
				break;
			case "itemdefs":
				ItemDefinition.serializeDefinitions();
				player.message("Serialized item definitions!");
				break;
			default:
				player.message("Possible choices: players, drops, shops, market, clans, board, itemdefs.");
				break;
		}
	}
}
