package net.edge.content.skill.woodcutting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.util.rand.RandomUtils;
import net.edge.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNode;
import net.edge.world.node.region.Region;

import java.util.EnumSet;

public enum BirdNest {
	RED_EGG_BIRD_NEST(5070, 4, 5076),
	GREEN_EGG_NEST(5071, 4, 5077),
	PURPLE_EGG_NEST(5072, 4, 5078),
	SEED_NEST(5073, 22, 5312, 5313, 5314, 5315, 5316, 5283, 5284, 5285, 5286, 5287, 5288, 5289, 5290, 5317),
	RING_NEST(5074, 18, 1635, 1637, 1639, 1641, 1643),
	RAVENS_NEST(11966, 8, 1196),
	EMPTY_NEST(7413, 40);
	
	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<BirdNest> VALUES = Sets.immutableEnumSet(EnumSet.allOf(BirdNest.class));
	
	/**
	 * Represents an item which boosts the rarity of the drop.
	 */
	private static final Item STRUNG_RABBIT_FEET = new Item(10132);
	
	/**
	 * The identification for the nest.
	 */
	private final int nest;
	
	/**
	 * The rarity for this nest.
	 */
	private final int rarity;
	
	/**
	 * The reward for this nest.
	 */
	private final Item[] reward;
	
	/**
	 * Constructs a new {@link BirdNest} enum.
	 * @param nest   {@link #nest}.
	 * @param rarity {@link #rarity}.
	 * @param reward {@link #reward}.
	 */
	private BirdNest(int nest, int rarity, int... reward) {
		this.nest = nest;
		this.rarity = rarity;
		this.reward = Item.convert(reward);
	}
	
	/**
	 * Drops a {@link BirdNest} for the specified {@code player}.
	 * @param player the player we're dropping the bird nest for.
	 * @return <true> if the bird nest was dropped, false otherwise.
	 */
	public static void drop(Player player) {
		if(RandomUtils.inclusive(1000) > 5) {
			return;
		}
		boolean modifier = player.getEquipment().contains(STRUNG_RABBIT_FEET);
		BirdNest randomNest = RandomUtils.random(VALUES.asList());
		
		if(RandomUtils.inclusive(100) <= randomNest.rarity + (modifier ? 10 : 0)) {
			player.message("A bird's nest falls out of the tree.");
			Region region = World.getRegions().getRegion(player.getPosition());
			if(region != null) {
				ItemNode ground = new ItemNode(new Item(randomNest.nest), player.getPosition(), player);
				region.register(ground);
			}
		}
	}
	
	/**
	 * @return {@link #nest}.
	 */
	public int getNest() {
		return nest;
	}
	
	/**
	 * @return {@link #rarity}.
	 */
	public int getRarity() {
		return rarity;
	}
	
	/**
	 * @return {@link #reward}.
	 */
	public Item[] getReward() {
		return reward;
	}
}
