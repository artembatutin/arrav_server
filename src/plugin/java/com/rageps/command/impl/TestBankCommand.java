package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.net.packet.out.SendContainer;
import com.rageps.net.packet.out.SendFeedMessage;
import com.rageps.net.packet.out.SendInterface;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.item.container.impl.Bank;
import com.rageps.world.text.ColorConstants;

@CommandSignature(alias = {"feed"}, rights = {Rights.ADMINISTRATOR}, syntax = "Opens an interface, ::interface id")
public final class TestBankCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.out(new SendFeedMessage(cmd[1], ColorConstants.LIME_GREEN));
	}
	
}
