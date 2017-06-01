package net.edge.content.commands.impl;

import net.edge.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.PlayerSerialization;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"save"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR}, syntax = "Use this command as just ::save")
public final class SaveWorldCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		for(Player world : World.getPlayers()) {
			if(world != null) {
				World.getService().submit(() -> new PlayerSerialization(world).serialize());
			}
		}
		World.getClanManager().save();
		player.message("Character files have been saved for everyone online!");
	}
}
