package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"movedown"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Moves a plain down, ::moveup")
public final class MoveDownCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.move(player.getPosition().move(0, 0, -1));
	}
	
}
