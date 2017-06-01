package net.edge.world.content.commands.impl;

import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"move"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR}, syntax = "Use this command as ::move position [z/height axis is optional]")
public final class MoveCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(cmd.length > 3) {
			player.move(new Position(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3])));
		} else if(cmd.length == 3) {
			player.move(new Position(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), player.getPosition().getZ()));
		}
	}

}
