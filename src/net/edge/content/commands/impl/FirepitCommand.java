package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.object.pit.PitFiring;
import net.edge.content.skill.firemaking.Bonfire;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"firepit"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::firepit")
public final class FirepitCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(PitFiring.burning != null) {
			PitFiring.burning.setDelay(20);
		}
	}

}
