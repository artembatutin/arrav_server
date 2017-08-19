package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectType;

@CommandSignature(alias = {"obj", "object", "spawnobject"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawns an object, ::obj id")
public final class SpawnObjectCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		new DynamicObject(id, player.getPosition(), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, 0).publish();
	}
	
}
