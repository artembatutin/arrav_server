package net.arrav.content;

import com.google.common.collect.ImmutableSet;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.entity.item.Item;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 13-6-2017.
 */
public enum VoteRewards {
	ONYX(0.4, 6571, 6573, 6575, 6585),
	ONYX_ENCHANTED(0.1, 15017, 19335),
	FREMENNIK_RINGS(0.1, 6731, 6733, 6735, 6737),
	FREMENNIK_RINGS_I(0.01, 13426, 13427, 15020, 15220),
	DEFENDERS(0.2, 8844, 8845, 8846, 8847, 8848, 8849, 8850),
	DRAGON_PICK(0.2, 15259, 20786),
	FOOD(0.8, new Item(384, 75), new Item(390, 50), new Item(396, 50), new Item(15271, 25));
	
	private static Item[] OTHERS = {new Item(12183, 900), new Item(454, 30), new Item(537, 30), new Item(961, 30), new Item(8779, 20), new Item(8781, 20), new Item(8783, 20), new Item(2358, 20), new Item(961, 20), new Item(1754, 20), new Item(1752, 20), new Item(1750, 20), new Item(1748, 20), new Item(2284, 20), new Item(2286, 20), new Item(2288, 20), new Item(1890, 20),};
	
	private static final ImmutableSet<VoteRewards> REWARDS = ImmutableSet.copyOf(values());
	
	public final Item[] items;
	
	public final double chance;
	
	VoteRewards(double chance, Item... items) {
		this.chance = chance;
		this.items = items;
	}
	
	VoteRewards(double chance, int... items) {
		this.chance = chance;
		this.items = Item.convert(items);
	}
	
	public static Optional<Item> getReward() {
		double baseChance = RandomUtils.nextDouble();
		if(!RandomUtils.success(baseChance)) {
			return Optional.empty();
		}
		VoteRewards reward = RandomUtils.random(REWARDS.asList());
		if(RandomUtils.success(reward.chance)) {
			return Optional.of(RandomUtils.random(reward.items));
		} else if(RandomUtils.success(0.5)) {
			Item item = RandomUtils.random(OTHERS);
			return Optional.of(item.createWithAmount(RandomUtils.inclusive(item.getAmount())));
		}
		
		return Optional.empty();
	}
}
