package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.item.ItemNodeManager;

@CommandSignature(alias = {"clearitems"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::clearitems")
public final class ClearItemsCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		//TODO
	}
	
}
