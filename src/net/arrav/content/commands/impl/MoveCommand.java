package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.locale.Position;

@CommandSignature(alias = {"move"}, rights = {Rights.ADMINISTRATOR,}, syntax = "Moves to a position, ::move x y z")
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
