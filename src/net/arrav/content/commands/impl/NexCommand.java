package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.content.minigame.nexchamber.NexMinigame;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"nex"}, rights = {Rights.ADMINISTRATOR}, syntax = "Nex testing command ::nex")
public final class NexCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		NexMinigame.game.onEnter(player);
	}
	
}
