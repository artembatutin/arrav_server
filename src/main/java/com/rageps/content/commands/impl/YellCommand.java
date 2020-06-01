package com.rageps.content.commands.impl;

import com.rageps.world.World;
import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.util.TextUtils;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"yell", "shout"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.DESIGNER, Rights.YOUTUBER, Rights.PLAYER}, syntax = "Use this command as ::yell or ::shout message")
public final class YellCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(player.getRights().equals(Rights.PLAYER)) {
			player.message("Regular players can't yell or shout worldwide.");
			return;
		}
		if(player.muted || player.ipMuted) {
			player.message("You cannot yell while being muted.");
			return;
		}
		String c = cmd[0];
		String message = TextUtils.capitalize(command.substring(c.length(), command.length()).substring(1));
		World.get().yell(player.getFormatUsername(), message, player.getRights());
	}
	
}
