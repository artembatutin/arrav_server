package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
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

@CommandSignature(alias = {"core"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::core")
public final class CoreCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.message("Took " + World.millis + "ms on sync.");
		
		//TEST
		NpcDropTable table = NpcDropManager.TABLES.get(-1);//barrows custom.
		int expected = RandomUtils.inclusive(4, 8);
		List<Item> loot = new ArrayList<>();
		int items = 0;
		while(items < expected) {
			NpcDropCache cache = RandomUtils.random(table.getCommon());
			NpcDrop drop = RandomUtils.random(NpcDropManager.COMMON.get(cache));
			if(drop.roll(ThreadLocalRandom.current())) {
				loot.add(new Item(drop.getId(), RandomUtils.inclusive(drop.getMinimum(), drop.getMaximum())));
				items++;
			}
		}
		player.getInventory().addOrDrop(loot);
	}

}
