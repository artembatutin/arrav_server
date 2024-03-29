package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.content.skill.SkillData;
import net.arrav.content.skill.Skills;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"setlevel"}, rights = {Rights.ADMINISTRATOR}, syntax = "Sets a new level, ::setlevel skill level")
public final class SetlevelCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		String skill = cmd[1].toUpperCase();
		int level = Integer.parseInt(cmd[2]);
		int skillId = SkillData.valueOf(skill).getId();
		if(skillId == Skills.HITPOINTS) {
			player.getSkills()[skillId].setRealLevel(level < 10 ? 10 : level);
			player.getSkills()[skillId].setLevel(level < 10 ? 100 : level > 99 ? 990 : level * 10, false);
			Skills.refresh(player, skillId);
			player.message("You've successfully set " + skill + " to level " + (level < 10 ? 10 : level) + ".");
			return;
		}
		player.getSkills()[skillId].setRealLevel(level);
		Skills.refresh(player, skillId);
		player.message("You've successfully set " + skill + " to level " + level + ".");
	}
	
}
