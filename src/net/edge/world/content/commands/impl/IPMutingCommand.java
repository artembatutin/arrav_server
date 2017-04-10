package net.edge.world.content.commands.impl;

import net.edge.world.World;
import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;
import net.edge.net.PunishmentHandler;

@CommandSignature(alias = {"ipmute"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR}, syntax = "Use this command as ::ipmute username")
public final class IPMutingCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player ipMute = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		
		if(ipMute != null && (ipMute.getRights().less(Rights.ADMINISTRATOR) || player.getRights().equals(Rights.DEVELOPER)) && ipMute != player) {
			player.message("Successfully IP muted " + ipMute.getFormatUsername() + ".");
			ipMute.message("@red@You have been IP-muted by " + player.getFormatUsername() + ".");
			PunishmentHandler.addIPMute(ipMute.getSession().getHost(), ipMute.getUsername());
		}
	}
	
}
