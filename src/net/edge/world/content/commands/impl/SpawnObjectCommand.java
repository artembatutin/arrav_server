package net.edge.world.content.commands.impl;

import net.edge.world.World;
import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;
import net.edge.world.model.node.object.ObjectDirection;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.world.model.node.object.ObjectType;

@CommandSignature(alias = {"object", "spawnobject"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::object or ::spawnobject id")
public final class SpawnObjectCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		ObjectNode obj = new ObjectNode(id, player.getPosition(), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP);
		World.getRegions().getRegion(player.getPosition()).register(obj);
	}
	
}
