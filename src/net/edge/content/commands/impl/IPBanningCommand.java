package net.edge.content.commands.impl;

import net.edge.net.PunishmentHandler;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"ipban"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR}, syntax = "Use this command as ::ipban username")
public final class IPBanningCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player ipban = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		
		if(ipban != null && (ipban.getRights().less(Rights.ADMINISTRATOR) || player.getRights().equals(Rights.DEVELOPER)) && ipban != player) {
			player.message("Successfully IP banned " + ipban.getFormatUsername() + ".");
			PunishmentHandler.addIPBan(ipban.getSession().getHost(), ipban.getUsername());
			World.get().queueLogout(ipban);
		}
	}
	
}
