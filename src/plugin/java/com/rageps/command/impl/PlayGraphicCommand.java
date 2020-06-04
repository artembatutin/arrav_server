package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"gfx", "graphic", "graph"}, rights = {Rights.ADMINISTRATOR}, syntax = "Plays a graphic, ::gfx id")
public final class PlayGraphicCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		player.graphic(new Graphic(id));
	}
	
}
