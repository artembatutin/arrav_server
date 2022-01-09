package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"emote", "animation", "anim"}, rights = {Rights.ADMINISTRATOR}, syntax = "Plays an animation, ::anim id")
public final class PlayAnimationCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Animation animation = new Animation(Integer.parseInt(cmd[1]));
		player.animation(animation);
	}
	
}
