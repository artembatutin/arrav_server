package com.rageps.content.commands.impl;

import com.rageps.content.minigame.nexchamber.NexMinigame;
import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"nex"}, rights = {Rights.ADMINISTRATOR}, syntax = "Nex testing command ::nex")
public final class NexCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		NexMinigame.game.onEnter(player);
	}
	
}
