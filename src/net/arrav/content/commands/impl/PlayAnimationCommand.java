package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"emote", "animation", "anim"}, rights = {Rights.ADMINISTRATOR}, syntax = "Plays an animation, ::anim id")
public final class PlayAnimationCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Animation animation = new Animation(Integer.parseInt(cmd[1]));
		player.animation(animation);
	}
	
}
