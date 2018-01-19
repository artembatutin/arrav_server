package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.entity.actor.combat.CombatUtil;
import net.arrav.world.entity.actor.combat.effect.CombatEffectType;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"skull", "skulled"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "The correct syntax is ::skull")
public final class SkullCommand implements Command {
	
	/**
	 * The functionality to be executed as soon as this command is called.
	 * @param player the player we are executing this command for.
	 * @param cmd    the command that we are executing for this player.
	 */
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		CombatUtil.effect(player, CombatEffectType.SKULL);
		player.message("You have been skulled!");
	}
}
