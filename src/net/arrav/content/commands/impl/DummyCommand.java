package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.World;
import net.arrav.world.entity.actor.mob.DefaultMob;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"dummy"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawns a dummy mob, ::dummy id")
public final class DummyCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int npcId = Integer.parseInt(cmd[1]);
		Mob mob = new DefaultMob(npcId, player.getPosition().copy());
		mob.setCurrentHealth(1000);
		mob.setOwner(player);
		mob.getMovementQueue().setLockMovement(true);
		World.get().getMobs().add(mob);
	}
	
}
