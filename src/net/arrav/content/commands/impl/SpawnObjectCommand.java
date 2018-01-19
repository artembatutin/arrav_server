package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.object.DynamicObject;
import net.arrav.world.object.ObjectDirection;
import net.arrav.world.object.ObjectType;

@CommandSignature(alias = {"obj", "object", "spawnobject"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawns an object, ::obj id")
public final class SpawnObjectCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		new DynamicObject(id, player.getPosition(), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, 0).publish();
	}
	
}
