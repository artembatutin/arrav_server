package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.impl.DefaultMob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"dummy"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::dummy npcId")
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
