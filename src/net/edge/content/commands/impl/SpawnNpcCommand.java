package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"npc", "mob"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawns a mob, ::mob")
public final class SpawnNpcCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Mob n = Mob.getNpc(Integer.parseInt(cmd[1]), player.getPosition());
		n.setOwner(player);
		n.setOriginalRandomWalk(true);
		n.getMovementCoordinator().setCoordinate(true);
		n.getMovementCoordinator().setRadius(3);
		n.setRespawn(false);
		World.get().getMobs().add(n);
	}

}
