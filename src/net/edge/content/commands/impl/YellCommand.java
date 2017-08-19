package net.edge.content.commands.impl;

import net.edge.net.PunishmentHandler;
import net.edge.util.TextUtils;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"yell", "shout"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.DESIGNER, Rights.YOUTUBER}, syntax = "Use this command as ::yell or ::shout message")
public final class YellCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(player.isMuted() || PunishmentHandler.isIPMuted(player)) {
			player.message("You cannot yell while being muted.");
			return;
		}
		String c = cmd[0];
		String message = TextUtils.capitalize(command.substring(c.length(), command.length()).substring(1));
		World.get().yell(player.getFormatUsername(), message, player.getRights());
	}

}
