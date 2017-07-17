package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;

@CommandSignature(alias = {"bank", "openbank"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR}, syntax = "Use this command as just ::bank or ::openbank")
public final class OpenBankCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.getBank().open();
	}
	
}
