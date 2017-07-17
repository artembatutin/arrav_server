package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;

@CommandSignature(alias = {"npc"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::npc id")
public final class SpawnNpcCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Mob n = Mob.getNpc(Integer.parseInt(cmd[1]), player.getPosition());
		boolean coordinate = true;
		n.setOwner(player);
		n.setOriginalRandomWalk(coordinate);
		n.getMovementCoordinator().setCoordinate(coordinate);
		n.getMovementCoordinator().setRadius(3);
		n.setRespawn(false);
		World.get().getNpcs().add(n);
	}
	
}
