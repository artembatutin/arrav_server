package com.rageps.content.commands.impl;

import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.object.DynamicObject;
import com.rageps.world.entity.object.ObjectDirection;
import com.rageps.world.entity.object.ObjectType;

@CommandSignature(alias = {"obj", "object", "spawnobject"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawns an object, ::obj id")
public final class SpawnObjectCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		new DynamicObject(id, player.getPosition(), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, 0).publish();
	}
	
}
