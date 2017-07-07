package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"night"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR}, syntax = "Use this command as ::night player")
public final class GiveNightCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player night = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(night == null)
			return;
		night.setIron(1, true);
		night.message(player.getFormatUsername() + " set you to nightmare mode.");
	}
	
}
