package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.object.ObjectNode;

@CommandSignature(alias = {"clearobj"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::clearobj")
public final class ClearObjectsCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		World.getRegions().getRegions().forEach((i, r) -> r.getDynamicObjects().forEach(ObjectNode::remove));
	}
	
}
