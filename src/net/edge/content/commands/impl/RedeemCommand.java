package net.edge.content.commands.impl;

import com.motiservice.vote.Result;
import com.motiservice.vote.SearchField;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * The assist command for staff members.
 */
@CommandSignature(alias = {"redeem"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR, Rights.SUPER_MODERATOR, Rights.MODERATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.RESPECTED_MEMBER, Rights.DESIGNER, Rights.PLAYER}, syntax = "Use this command as ::redeem")
public final class RedeemCommand implements Command {
	
	/**
	 * Voting platform.
	 */
	private static final com.motiservice.Motivote platform = new com.motiservice.Motivote("edgeville", "ffc180fe2ae2189f64a87f281d74d222");
	
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Result r2 = platform.redeem(SearchField.USER_NAME, player.getUsername());
		if (r2.success()) {
			int total = r2.votes().size();
			player.message("You received " + (total * 25_000) + " coins for voting!");
			player.getInventory().addOrBank(new Item(995, total * 25_000));
		} else {
			player.message("Nothing found, you can vote by clicking the link in the quest tab.");
		}
	}
	
}
