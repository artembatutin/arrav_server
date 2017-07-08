package net.edge.event.item;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ItemEvent;
import net.edge.util.rand.Chance;
import net.edge.util.rand.RandomUtils;
import net.edge.world.node.entity.npc.drop.NpcDrop;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.concurrent.ThreadLocalRandom;

public class MysteryBox extends EventInitializer {
	public static final NpcDrop[] ITEMS = new NpcDrop[] {
			//ahrims
			new NpcDrop(4708, 1, 1, Chance.COMMON),
			new NpcDrop(4710, 1, 1, Chance.COMMON),
			new NpcDrop(4712, 1, 1, Chance.COMMON),
			new NpcDrop(4714, 1, 1, Chance.COMMON),
			//dharoks
			new NpcDrop(4716, 1, 1, Chance.COMMON),
			new NpcDrop(4718, 1, 1, Chance.COMMON),
			new NpcDrop(4720, 1, 1, Chance.COMMON),
			new NpcDrop(4722, 1, 1, Chance.COMMON),
			//karils
			new NpcDrop(4732, 1, 1, Chance.COMMON),
			new NpcDrop(4734, 1, 1, Chance.COMMON),
			new NpcDrop(4736, 1, 1, Chance.COMMON),
			new NpcDrop(4738, 1, 1, Chance.COMMON),
			//torags
			new NpcDrop(4745, 1, 1, Chance.COMMON),
			new NpcDrop(4747, 1, 1, Chance.COMMON),
			new NpcDrop(4749, 1, 1, Chance.COMMON),
			new NpcDrop(4751, 1, 1, Chance.COMMON),
			//veracs
			new NpcDrop(4753, 1, 1, Chance.COMMON),
			new NpcDrop(4755, 1, 1, Chance.COMMON),
			new NpcDrop(4757, 1, 1, Chance.COMMON),
			new NpcDrop(4759, 1, 1, Chance.COMMON),
			//dagannoth rings
			new NpcDrop(6737, 1, 1, Chance.COMMON),
			new NpcDrop(6735, 1, 1, Chance.COMMON),
			new NpcDrop(6733, 1, 1, Chance.COMMON),
			new NpcDrop(6731, 1, 1, Chance.COMMON),
			//gmaul
			new NpcDrop(4153, 1, 1, Chance.COMMON),
			//fury
			new NpcDrop(6585, 1, 1, Chance.COMMON),
			//dagannoth rings i
			new NpcDrop(15220, 1, 1, Chance.UNCOMMON),
			new NpcDrop(15020, 1, 1, Chance.UNCOMMON),
			new NpcDrop(15019, 1, 1, Chance.UNCOMMON),
			new NpcDrop(15018, 1, 1, Chance.UNCOMMON),
			//whip
			new NpcDrop(4151, 1, 1, Chance.UNCOMMON),
			//colored whips
			new NpcDrop(15441, 1, 1, Chance.UNCOMMON),
			new NpcDrop(15442, 1, 1, Chance.UNCOMMON),
			new NpcDrop(15443, 1, 1, Chance.UNCOMMON),
			new NpcDrop(15444, 1, 1, Chance.UNCOMMON),
			//colored staff of lights
			new NpcDrop(22207, 1, 1, Chance.UNCOMMON),
			new NpcDrop(22209, 1, 1, Chance.UNCOMMON),
			new NpcDrop(22211, 1, 1, Chance.UNCOMMON),
			new NpcDrop(22213, 1, 1, Chance.UNCOMMON),
			//bandos
			new NpcDrop(11724, 1, 1, Chance.RARE),
			new NpcDrop(11726, 1, 1, Chance.RARE),
			new NpcDrop(11728, 1, 1, Chance.RARE),
			//bandos, zamorak, saradomin godsword
			new NpcDrop(11696, 1, 1, Chance.RARE),
			new NpcDrop(11700, 1, 1, Chance.RARE),
			new NpcDrop(11698, 1, 1, Chance.RARE),
			//vestas
			new NpcDrop(13887, 1, 1, Chance.RARE),
			new NpcDrop(13893, 1, 1, Chance.RARE),
			new NpcDrop(13899, 1, 1, Chance.RARE),
			new NpcDrop(13905, 1, 1, Chance.RARE),
			//zuriels
			new NpcDrop(13858, 1, 1, Chance.RARE),
			new NpcDrop(13861, 1, 1, Chance.RARE),
			new NpcDrop(13864, 1, 1, Chance.RARE),
			new NpcDrop(13867, 1, 1, Chance.RARE),
			//statius
			new NpcDrop(13884, 1, 1, Chance.RARE),
			new NpcDrop(13890, 1, 1, Chance.RARE),
			new NpcDrop(13896, 1, 1, Chance.RARE),
			new NpcDrop(13902, 1, 1, Chance.RARE),
			//morrigans
			new NpcDrop(13870, 1, 1, Chance.RARE),
			new NpcDrop(13873, 1, 1, Chance.RARE),
			new NpcDrop(13876, 1, 1, Chance.RARE),
			new NpcDrop(21371, 1, 1, Chance.RARE),
			//vine whips colours
			new NpcDrop(21371, 1, 1, Chance.RARE),
			new NpcDrop(21372, 1, 1, Chance.RARE),
			new NpcDrop(21373, 1, 1, Chance.RARE),
			new NpcDrop(21374, 1, 1, Chance.RARE),
			new NpcDrop(21375, 1, 1, Chance.RARE),
			//party hat
			new NpcDrop(1038, 1, 1, Chance.VERY_RARE),
			new NpcDrop(1040, 1, 1, Chance.VERY_RARE),
			new NpcDrop(1042, 1, 1, Chance.VERY_RARE),
			new NpcDrop(1044, 1, 1, Chance.VERY_RARE),
			new NpcDrop(1046, 1, 1, Chance.VERY_RARE),
			new NpcDrop(1048, 1, 1, Chance.VERY_RARE),
			//dragon claws
			new NpcDrop(14484, 1, 1, Chance.VERY_RARE),
			//armadyl godsword
			new NpcDrop(11694, 1, 1, Chance.VERY_RARE),
			//third age pieces
			new NpcDrop(19308, 1, 1, Chance.VERY_RARE),
			new NpcDrop(19311, 1, 1, Chance.VERY_RARE),
			new NpcDrop(19314, 1, 1, Chance.VERY_RARE),
			new NpcDrop(19317, 1, 1, Chance.VERY_RARE),
			new NpcDrop(19320, 1, 1, Chance.VERY_RARE),
			//torva
			new NpcDrop(20135, 1, 1, Chance.EXTREMELY_RARE),
			new NpcDrop(20139, 1, 1, Chance.EXTREMELY_RARE),
			new NpcDrop(20143, 1, 1, Chance.EXTREMELY_RARE),
			//pernix
			new NpcDrop(20147, 1, 1, Chance.EXTREMELY_RARE),
			new NpcDrop(20151, 1, 1, Chance.EXTREMELY_RARE),
			new NpcDrop(20155, 1, 1, Chance.EXTREMELY_RARE),
			//virtus
			new NpcDrop(20159, 1, 1, Chance.EXTREMELY_RARE),
			new NpcDrop(20163, 1, 1, Chance.EXTREMELY_RARE),
			new NpcDrop(20167, 1, 1, Chance.EXTREMELY_RARE),
	};
	
	@Override
	public void init() {
		ItemEvent e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				while(true) {
					NpcDrop attempt = RandomUtils.random(ITEMS);
					if(attempt.roll(ThreadLocalRandom.current())) {
						player.getInventory().remove(item, slot);
						player.getInventory().add(new Item(attempt.getId(), attempt.getMinimum()), slot, true);
						break;
					}
				}
				return false;
			}
		};
		e.register(6199);
	}
}
