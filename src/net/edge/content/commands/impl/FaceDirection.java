package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.Direction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"face", "facedir", "facedirection"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::face, ::facedir or ::facedirection direction")
public final class FaceDirection implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Direction direction = Direction.valueOf(cmd[1]);
		player.faceDirection(direction);
	}
	
}
