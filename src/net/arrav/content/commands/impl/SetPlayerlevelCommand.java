package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.content.skill.SkillData;
import net.arrav.content.skill.Skills;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"setplevel"}, rights = {Rights.ADMINISTRATOR}, syntax = "Sets a player level, ::setplevel playername skill level")
public final class SetPlayerlevelCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null)
			return;
		String skill = cmd[2].toUpperCase();
		int level = Integer.parseInt(cmd[3]);
		
		int skillId = SkillData.valueOf(skill).getId();
		
		p.getSkills()[skillId].setRealLevel(level);
		Skills.refresh(p, skillId);
		player.message("You've successfully set " + skill + " to level " + level + ".");
		p.message(player.getFormatUsername() + " has set " + skill + " to level " + level + ".");
	}
	
}
