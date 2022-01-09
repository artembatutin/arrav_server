package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"feed"}, rights = {Rights.ADMINISTRATOR}, syntax = "Opens an interface, ::interface id")
public final class TestBankCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		World.get().getWorldUtil().sendBroadcast(100, "This is a test!", true);
	}
	
}
