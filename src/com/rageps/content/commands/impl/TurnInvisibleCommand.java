package com.rageps.content.commands.impl;

import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"hide", "invisible"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Become invisible to other players, ::invisible")
public final class TurnInvisibleCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.setVisible(!player.isVisible());
		player.message(player.isVisible() ? "You've turned visible, players can see you now." : "You've turned invisible, players cannot see you now.");
	}
	
}
