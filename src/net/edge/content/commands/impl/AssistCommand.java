package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.region.TraversalMap;
import net.edge.world.locale.Position;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

/**
 * The assist command for staff members.
 */
@CommandSignature(alias = {"assist", "help"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR}, syntax = "Use this command as ::assist username")
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
