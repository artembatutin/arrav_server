package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.content.skill.Skills;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

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
