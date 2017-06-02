package net.edge.content.commands.impl;

import net.edge.content.market.MarketCounter;
import net.edge.content.market.MarketItem;
import net.edge.net.message.impl.NpcInformationMessage;
import net.edge.world.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.npc.drop.NpcDrop;
import net.edge.world.node.entity.npc.drop.NpcDropManager;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.PlayerSerialization;
import net.edge.world.node.entity.player.assets.Rights;

import java.io.BufferedWriter;
import java.io.FileWriter;

@CommandSignature(alias = {"save"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR}, syntax = "Use this command as ::save type")
public final class SaveCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		switch(cmd[1].toLowerCase()) {
			case "players":
				for(Player world : World.getPlayers()) {
					if(world != null) {
						World.getService().submit(() -> new PlayerSerialization(world).serialize());
					}
				}
				World.getClanManager().save();
				player.message("Character files have been saved for everyone online!");
				break;
			case "drops":
				NpcDropManager.dump();
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter("./data/suggested_drops.txt", true));
					for(NpcDrop d : NpcInformationMessage.SUGGESTED) {
						out.write(d.toString());
						out.newLine();
					}
					NpcInformationMessage.SUGGESTED.clear();
					out.close();
				} catch(Exception e) {
				}
				player.message("Serialized shops!");
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
			default:
				player.message("Possible choices: players, drops, shops, market, clans, board.");
				break;
		}
	}
}
