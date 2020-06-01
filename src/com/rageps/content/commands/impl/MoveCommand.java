package com.rageps.content.commands.impl;

import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.locale.Position;

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
