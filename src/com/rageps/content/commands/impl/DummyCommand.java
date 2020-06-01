package com.rageps.content.commands.impl;

import com.rageps.world.World;
import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.world.entity.actor.mob.DefaultMob;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

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
