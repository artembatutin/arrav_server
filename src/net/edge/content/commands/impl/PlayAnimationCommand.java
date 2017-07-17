package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.Animation;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"emote", "animation", "anim"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::emote, ::animation or ::anim animationId")
public final class PlayAnimationCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Animation animation = new Animation(Integer.parseInt(cmd[1]));
		player.animation(animation);
	}
	
}
