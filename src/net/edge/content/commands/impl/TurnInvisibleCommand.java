package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"hide", "invisible"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Use this command as just ::hide or ::invisible")
public final class TurnInvisibleCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.setVisible(!player.isVisible());
		player.message(player.isVisible() ? "You've turned visible, players can see you now." : "You've turned invisible, players cannot see you now.");
	}
	
}
