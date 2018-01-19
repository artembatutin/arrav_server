package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.item.Item;

@CommandSignature(alias = {"tokens"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Give arrav tokens, ::tokens username amount")
public final class GiveTokensCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null)
			return;
		int amount = Integer.parseInt(cmd[2]);
		p.totalDonated += amount;
		p.getBank().deposit(new Item(7478, amount));
		p.message("You have been given " + amount + " arrav tokens, they are in your bank.");
		player.message("You've increased " + amount + " for " + p.getFormatUsername() + ". Total points = " + p.getTotalDonated(false));
	}
	
}
