package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"movedown"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR}, syntax = "Use this command as ::movedown")
public final class MoveDownCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.move(player.getPosition().move(0, 0, -1));
	}

}
