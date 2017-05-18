package net.edge.world.content.commands.impl;

import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.node.entity.model.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"gfx", "graphic", "graph"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::gfx, ::graphic or ::graph graphicId")
public final class PlayGraphicCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		player.graphic(new Graphic(id));
	}
	
}
