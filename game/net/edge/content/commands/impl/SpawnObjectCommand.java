package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectType;

@CommandSignature(alias = {"object", "spawnobject"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::object or ::spawnobject id")
public final class SpawnObjectCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		new DynamicObject(id, player.getPosition(), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, 0).register();
	}
	
}
