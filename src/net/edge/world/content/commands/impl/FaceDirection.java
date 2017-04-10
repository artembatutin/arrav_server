package net.edge.world.content.commands.impl;

import net.edge.world.model.node.entity.model.Direction;
import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;

@CommandSignature(alias = {"face", "facedir", "facedirection"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::face, ::facedir or ::facedirection direction")
public final class FaceDirection implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Direction direction = Direction.valueOf(cmd[1]);
		player.faceDirection(direction);
	}
	
}
