package net.edge.world.content.commands.impl;

import net.edge.world.World;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.content.commands.Command;
import net.edge.world.model.node.entity.player.assets.Rights;
import net.edge.net.PunishmentHandler;

@CommandSignature(alias = {"ipban"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR}, syntax = "Use this command as ::ipban username")
public final class IPBanningCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player ipban = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		
		if(ipban != null && (ipban.getRights().less(Rights.ADMINISTRATOR) || player.getRights().equals(Rights.DEVELOPER)) && ipban != player) {
			player.message("Successfully IP banned " + ipban.getFormatUsername() + ".");
			PunishmentHandler.addIPBan(ipban.getSession().getHost(), ipban.getUsername());
			World.queueLogout(ipban);
		}
	}
	
}
