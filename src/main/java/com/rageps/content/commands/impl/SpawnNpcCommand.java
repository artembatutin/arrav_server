package com.rageps.content.commands.impl;

import com.rageps.world.World;
import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

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
