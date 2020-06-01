package com.rageps.content.commands.impl;

import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.content.skill.Skills;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"master"}, rights = {Rights.ADMINISTRATOR}, syntax = "Sets all maxed skills, ::master")
public final class MasterCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(player.getRights().equal(Rights.ADMINISTRATOR)) {
			for(int i = 0; i < player.getSkills().length; i++) {
				Skills.experience(player, 14000000, i);
			}
		} else {
			for(int i = 0; i < 6; i++) {
				Skills.experience(player, 14000000, i);
			}
		}
	}
	
}
