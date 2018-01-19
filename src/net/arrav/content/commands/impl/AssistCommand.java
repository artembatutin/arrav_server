package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.region.TraversalMap;
import net.arrav.world.locale.Position;

/**
 * The assist command for staff members.
 */
@CommandSignature(alias = {"assist", "help"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.HELPER}, syntax = "Assisting a player by ::assist username")
public final class AssistCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player assisted = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(assisted != null && assisted != player) {
			Position pos = TraversalMap.getRandomNearby(assisted.getPosition(), player.getPosition(), 1);
			if(pos != null) {
				player.move(pos);
			} else {
				player.move(assisted.getPosition());
			}
			player.forceChat("Hello, my name is " + player.getFormatUsername() + ", I'm here to assist you.");
		}
	}
	
}
