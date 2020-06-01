package com.rageps.content.commands.impl;

import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"players"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "Gives a player online count, ::players")
public final class PlayersOnlineCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.message(World.get().getPlayers().size() == 1 ? "There is currently 1 player online." : "There are currently " + World.get().getPlayers().size() + " players online!");
	}
	
}
