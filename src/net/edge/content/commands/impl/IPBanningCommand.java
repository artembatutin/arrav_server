package net.edge.content.commands.impl;

import net.edge.net.PunishmentHandler;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ipban"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::ipban username")
public final class IPBanningCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player ipban = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		
		if(ipban != null && (ipban.getRights().less(Rights.ADMINISTRATOR) || player.getRights().equals(Rights.ADMINISTRATOR)) && ipban != player) {
			player.message("Successfully IP banned " + ipban.getFormatUsername() + ".");
			PunishmentHandler.addIPBan(ipban.getSession().getHost(), ipban.getCredentials().getUsername());
			World.get().queueLogout(ipban);
		}
	}
	
}
