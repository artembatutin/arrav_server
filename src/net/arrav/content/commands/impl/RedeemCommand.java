package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.item.Item;

import static net.arrav.content.achievements.Achievement.VOTE;

@CommandSignature(alias = {"redeem"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "Redeem a vote auth, ::redeem auth")
public final class RedeemCommand implements Command {
	

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		throw new UnsupportedOperationException("Not implemented");
	}
}
