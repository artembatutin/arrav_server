package net.edge.content.commands.impl;

import net.edge.net.PunishmentHandler;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"ipmute"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR}, syntax = "Use this command as ::ipmute username")
public final class IPMutingCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player ipMute = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		
		if(ipMute != null && (ipMute.getRights().less(Rights.ADMINISTRATOR) || player.getRights().equals(Rights.ADMINISTRATOR)) && ipMute != player) {
			player.message("Successfully IP muted " + ipMute.getFormatUsername() + ".");
			ipMute.message("@red@You have been IP-muted by " + player.getFormatUsername() + ".");
			PunishmentHandler.addIPMute(ipMute.getSession().getHost(), ipMute.getUsername());
		}
	}
	
}
