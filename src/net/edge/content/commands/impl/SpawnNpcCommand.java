package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"npc"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::npc id")
public final class SpawnNpcCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Npc n = Npc.getNpc(Integer.parseInt(cmd[1]), player.getPosition());
		n.setSpawnedFor(player.getUsername());
		boolean coordinate = true;
		n.setOriginalRandomWalk(coordinate);
		n.getMovementCoordinator().setCoordinate(coordinate);
		n.getMovementCoordinator().setRadius(3);
		n.setRespawn(false);
		World.get().getNpcs().add(n);
		n.setSpecial(20);
	}
	
}
