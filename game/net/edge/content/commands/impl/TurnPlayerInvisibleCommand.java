package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"phide", "pinvisible"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Use this command as just ::phide or ::ipnvisible player")
public final class TurnPlayerInvisibleCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null)
			return;
		player.setVisible(!p.isVisible());
		player.message(p.isVisible() ? "You've turned visible, players can see you now." : "You've turned invisible, players cannot see you now.");
	}
	
}
