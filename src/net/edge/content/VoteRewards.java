package net.edge.content;

import com.google.common.collect.ImmutableSet;
import net.edge.util.rand.RandomUtils;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 13-6-2017.
 */
public enum VoteRewards {
    ONYX(0.10, 6571, 6573, 6575, 6585),
    ONYX_ENCHANTED(0.05, 15017, 19335),
    FREMENNIK_RINGS(0.15, 6731, 6733, 6735, 6737),
    FREMENNIK_RINGS_I(0.01, 13426, 13427, 15020, 15220),
    DEFENDERS(0.5, 8844, 8845, 8846, 8847, 8848, 8849, 8850),
    FOOD(0.8, new Item(384, 75), new Item(390, 50), new Item(396, 50), new Item(15271, 25));

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
        }

        return Optional.empty();
    }
}
