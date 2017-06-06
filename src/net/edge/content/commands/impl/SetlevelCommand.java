package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.Skills;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"setlevel"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::setlevel skill level")
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
