package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.npc.impl.DefaultNpc;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;

@CommandSignature(alias = {"dummy"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR}, syntax = "Use this command as ::dummy npcId")
public final class DummyCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int npcId = Integer.parseInt(cmd[1]);
		Npc npc = new DefaultNpc(npcId, player.getPosition().copy());
		npc.setCurrentHealth(1000);
		npc.setOwner(player);
		npc.getMovementQueue().setLockMovement(true);
		World.get().getNpcs().add(npc);
	}
	
}
