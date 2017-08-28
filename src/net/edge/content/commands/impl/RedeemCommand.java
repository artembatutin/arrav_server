package net.edge.content.commands.impl;

import com.motiservice.vote.Result;
import com.motiservice.vote.SearchField;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.item.Item;

import static net.edge.content.achievements.Achievement.VOTE;

@CommandSignature(alias = {"redeem"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "Redeem a vote auth, ::redeem auth")
public final class RedeemCommand implements Command {

	/**
	 * Voting platform.
	 */
	private static final com.motiservice.Motivote platform = new com.motiservice.Motivote("edgeville", "ffc180fe2ae2189f64a87f281d74d222");

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Result r1 = platform.redeem(SearchField.AUTH_CODE, cmd[1]);
		if(r1.success()) {
			int voted = r1.votes().size();
			player.getInventory().addOrBank(new Item(6829, voted));
			VOTE.inc(player);
		} else {
			player.message("Nothing found, please verify the auth code.");
			player.message("You can also vote by clicking the link in the quest tab.");
		}
	}

}
