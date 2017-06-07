package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.skill.firemaking.pits.PitFiring;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.node.entity.npc.drop.NpcDrop;
import net.edge.world.node.entity.npc.drop.NpcDropCache;
import net.edge.world.node.entity.npc.drop.NpcDropManager;
import net.edge.world.node.entity.npc.drop.NpcDropTable;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@CommandSignature(alias = {"firepit"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::firepit")
public final class FirepitCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(PitFiring.burning == null) {
			player.message("Nulled");
			return;
		}
		PitFiring.burning.setDelay(2);
	}

}
