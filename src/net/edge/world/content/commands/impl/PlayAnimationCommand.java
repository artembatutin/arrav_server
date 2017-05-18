package net.edge.world.content.commands.impl;

import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"emote", "animation", "anim"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::emote, ::animation or ::anim animationId")
public final class PlayAnimationCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Animation animation = new Animation(Integer.parseInt(cmd[1]));
		player.animation(animation);
	}
	
}
