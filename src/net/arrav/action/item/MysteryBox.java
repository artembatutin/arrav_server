package net.arrav.action.item;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ItemAction;
import net.arrav.util.rand.Chance;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.entity.actor.mob.drop.Drop;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

import java.util.concurrent.ThreadLocalRandom;

public class MysteryBox extends ActionInitializer {
	public static final Drop[] ITEMS = new Drop[]{
			//ahrims
			new Drop(4708, 1, 1, Chance.COMMON), new Drop(4710, 1, 1, Chance.COMMON), new Drop(4712, 1, 1, Chance.COMMON), new Drop(4714, 1, 1, Chance.COMMON),
			//dharoks
			new Drop(4716, 1, 1, Chance.COMMON), new Drop(4718, 1, 1, Chance.COMMON), new Drop(4720, 1, 1, Chance.COMMON), new Drop(4722, 1, 1, Chance.COMMON),
			//karils
			new Drop(4732, 1, 1, Chance.COMMON), new Drop(4734, 1, 1, Chance.COMMON), new Drop(4736, 1, 1, Chance.COMMON), new Drop(4738, 1, 1, Chance.COMMON),
			//torags
			new Drop(4745, 1, 1, Chance.COMMON), new Drop(4747, 1, 1, Chance.COMMON), new Drop(4749, 1, 1, Chance.COMMON), new Drop(4751, 1, 1, Chance.COMMON),
			//veracs
			new Drop(4753, 1, 1, Chance.COMMON), new Drop(4755, 1, 1, Chance.COMMON), new Drop(4757, 1, 1, Chance.COMMON), new Drop(4759, 1, 1, Chance.COMMON),
			//dagannoth rings
			new Drop(6737, 1, 1, Chance.COMMON), new Drop(6735, 1, 1, Chance.COMMON), new Drop(6733, 1, 1, Chance.COMMON), new Drop(6731, 1, 1, Chance.COMMON),
			//gmaul
			new Drop(4153, 1, 1, Chance.COMMON),
			//fury
			new Drop(6585, 1, 1, Chance.COMMON),
			//dagannoth rings i
			new Drop(15220, 1, 1, Chance.UNCOMMON), new Drop(15020, 1, 1, Chance.UNCOMMON), new Drop(15019, 1, 1, Chance.UNCOMMON), new Drop(15018, 1, 1, Chance.UNCOMMON),
			//whip
			new Drop(4151, 1, 1, Chance.UNCOMMON),
			//colored whips
			new Drop(15441, 1, 1, Chance.UNCOMMON), new Drop(15442, 1, 1, Chance.UNCOMMON), new Drop(15443, 1, 1, Chance.UNCOMMON), new Drop(15444, 1, 1, Chance.UNCOMMON),
			//colored staff of lights
			new Drop(22207, 1, 1, Chance.UNCOMMON), new Drop(22209, 1, 1, Chance.UNCOMMON), new Drop(22211, 1, 1, Chance.UNCOMMON), new Drop(22213, 1, 1, Chance.UNCOMMON),
			//bandos
			new Drop(11724, 1, 1, Chance.RARE), new Drop(11726, 1, 1, Chance.RARE), new Drop(11728, 1, 1, Chance.RARE),
			//bandos, zamorak, saradomin godsword
			new Drop(11696, 1, 1, Chance.RARE), new Drop(11700, 1, 1, Chance.RARE), new Drop(11698, 1, 1, Chance.RARE),
			//vestas
			new Drop(13887, 1, 1, Chance.RARE), new Drop(13893, 1, 1, Chance.RARE), new Drop(13899, 1, 1, Chance.RARE), new Drop(13905, 1, 1, Chance.RARE),
			//zuriels
			new Drop(13858, 1, 1, Chance.RARE), new Drop(13861, 1, 1, Chance.RARE), new Drop(13864, 1, 1, Chance.RARE), new Drop(13867, 1, 1, Chance.RARE),
			//statius
			new Drop(13884, 1, 1, Chance.RARE), new Drop(13890, 1, 1, Chance.RARE), new Drop(13896, 1, 1, Chance.RARE), new Drop(13902, 1, 1, Chance.RARE),
			//morrigans
			new Drop(13870, 1, 1, Chance.RARE), new Drop(13873, 1, 1, Chance.RARE), new Drop(13876, 1, 1, Chance.RARE), new Drop(21371, 1, 1, Chance.RARE),
			//vine whips colours
			new Drop(21371, 1, 1, Chance.RARE), new Drop(21372, 1, 1, Chance.RARE), new Drop(21373, 1, 1, Chance.RARE), new Drop(21374, 1, 1, Chance.RARE), new Drop(21375, 1, 1, Chance.RARE),
			//dragon claws
			new Drop(14484, 1, 1, Chance.VERY_RARE),
			//armadyl godsword
			new Drop(11694, 1, 1, Chance.VERY_RARE),
			//third age pieces
			new Drop(19308, 1, 1, Chance.VERY_RARE), new Drop(19311, 1, 1, Chance.VERY_RARE), new Drop(19314, 1, 1, Chance.VERY_RARE), new Drop(19317, 1, 1, Chance.VERY_RARE), new Drop(19320, 1, 1, Chance.VERY_RARE),
			//party hat
			new Drop(1038, 1, 1, Chance.EXTREMELY_RARE), new Drop(1040, 1, 1, Chance.EXTREMELY_RARE), new Drop(1042, 1, 1, Chance.EXTREMELY_RARE), new Drop(1044, 1, 1, Chance.EXTREMELY_RARE), new Drop(1046, 1, 1, Chance.EXTREMELY_RARE), new Drop(1048, 1, 1, Chance.EXTREMELY_RARE),
			//torva
			new Drop(20135, 1, 1, Chance.FUCKING_RARE), new Drop(20139, 1, 1, Chance.FUCKING_RARE), new Drop(20143, 1, 1, Chance.FUCKING_RARE),
			//pernix
			new Drop(20147, 1, 1, Chance.FUCKING_RARE), new Drop(20151, 1, 1, Chance.FUCKING_RARE), new Drop(20155, 1, 1, Chance.FUCKING_RARE),
			//virtus
			new Drop(20159, 1, 1, Chance.FUCKING_RARE), new Drop(20163, 1, 1, Chance.FUCKING_RARE), new Drop(20167, 1, 1, Chance.FUCKING_RARE),};
	
	@Override
	public void init() {
		ItemAction e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				while(true) {
					Drop attempt = RandomUtils.random(ITEMS);
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
