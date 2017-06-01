package net.edge.content.commands.impl;

import net.edge.net.PunishmentHandler;
import net.edge.util.TextUtils;
import net.edge.world.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"yell", "shout"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR, Rights.SUPER_MODERATOR, Rights.MODERATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.RESPECTED_MEMBER, Rights.DESIGNER}, syntax = "Use this command as ::yell or ::shout message")
public final class YellCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(player.isMuted() || PunishmentHandler.isIPMuted(player.getSession().getHost())) {
			player.message("You cannot yell while being muted.");
			return;
		}

		String c = cmd[0];
		String message = TextUtils.capitalize(command.substring(c.length(), command.length()).substring(1));
		World.message("@blu@[Global Chat] " + "@cr" + player.getRights().getProtocolValue() + "@" + player.getRights().getYellPrefix() + player.getFormatUsername() + "@blu@: " + message);
	}

}
