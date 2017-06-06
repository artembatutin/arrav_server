package net.edge.content.commands.impl;

import net.edge.net.PunishmentHandler;
import net.edge.game.GameConstants;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.container.impl.Inventory;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"starter"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR, Rights.SUPER_MODERATOR, Rights.MODERATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.RESPECTED_MEMBER, Rights.DESIGNER, Rights.PLAYER}, syntax = "Use this command as ::starter")
public final class GetStarterCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(player.getInventory().size() > 0) {
			player.message("Please clear your inventory first.");
			return;
		}
		if(!PunishmentHandler.recievedStarter(player.getSession().getHost())) {
			player.getInventory().setItems(GameConstants.STARTER_PACKAGE);
			PunishmentHandler.addStarter(player.getSession().getHost());
			player.getInventory().refresh(player, Inventory.INVENTORY_DISPLAY_ID);
		} else {
			player.message("You already received a starter package before.");
		}
	}
	
}
