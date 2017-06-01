package net.edge.content.commands.impl;

import net.edge.world.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"slayer"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR}, syntax = "Use this command as ::slayer player amount")
public final class GiveSlayerCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player slayer = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		int amount = Integer.parseInt(cmd[2]);
		slayer.updateSlayers(amount);
		player.message("You gave " + amount + " slayer points to " + slayer.getFormatUsername());
		slayer.message("You received " + amount + " slayer points from " + player.getFormatUsername());
	}
	
}
