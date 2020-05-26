package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.net.packet.out.SendContainer;
import net.arrav.net.packet.out.SendInterface;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.item.container.impl.Bank;

import java.util.Arrays;

@CommandSignature(alias = {"testbank"}, rights = {Rights.ADMINISTRATOR}, syntax = "Opens an interface, ::interface id")
public final class TestBankCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.out(new SendInterface(Bank.BANK_WINDOW_ID));
		player.out(new SendContainer(Bank.BANK_INVENTORY_ID, player.getBank().getTabs()[0]));
	}
	
}
