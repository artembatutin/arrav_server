package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.skill.Skills;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"master"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "Sets all maxed skills, ::master")
public final class MasterCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(player.getRights().equal(Rights.ADMINISTRATOR)) {
			for(int i = 0; i < player.getSkills().length; i++) {
				Skills.experience(player, (Integer.MAX_VALUE - player.getSkills()[i].getExperience()), i);
			}
		} else {
			for(int i = 0; i < 6; i++) {
				Skills.experience(player, (Integer.MAX_VALUE - player.getSkills()[i].getExperience()), i);
			}
		}
	}
	
}
