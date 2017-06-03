package net.edge.content.commands.impl;

import net.edge.world.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"players"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR, Rights.SUPER_MODERATOR, Rights.MODERATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.RESPECTED_MEMBER, Rights.DESIGNER, Rights.PLAYER}, syntax = "Use this command as just ::players")
public final class PlayersOnlineCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.message(World.get().getPlayers().size() == 1 ? "There is currently 1 player online!" : "There are currently " + World.get().getPlayers().size() + " players online!");
	}
	
}
