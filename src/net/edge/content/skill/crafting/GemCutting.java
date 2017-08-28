package net.edge.content.skill.crafting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.impl.ProducingSkillAction;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.ItemIdentifiers;

import java.util.EnumSet;
import java.util.Optional;

import static net.edge.content.achievements.Achievement.PRICELESS_GEM;

/**
 * Holds functionality for cutting gems.
 *
 * @author <a href="http://www.rune-server.org/members/Golang/">Jay</a>
 */
public final class GemCutting extends ProducingSkillAction {

	/**
	 * The definition we're currently creating items for.
	 */
	private final GemCraftingData data;

	/**
	 * Determines if the underlying skill action is a fletching skill.
	 */
	private final boolean fletching;

	/**
	 * Constructs a new {@link GemCutting}.
	 *
	 * @param player    {@link #getPlayer()}.
	 * @param data      {@link #data}.
	 * @param fletching {@link #fletching}.
	 */
	public GemCutting(Player player, GemCraftingData data) {
		super(player, Optional.empty());
		this.data = data;
		this.fletching = data.fletching;
	}

	/**
	 * Attempts to cut the gem for the specified {@code player}.
	 *
	 * @param player {@link #getPlayer()}.
	 * @param item   the item used.
	 * @param item2  the item used on.
	 * @return <true> if the skill action was started, <false> otherwise.
	 */
	public static boolean cut(Player player, Item item, Item item2) {
		Optional<GemCraftingData> data = GemCraftingData.getDefinition(item.getId(), item2.getId());

		if(!data.isPresent()) {
			return false;
		}

		GemCutting action = new GemCutting(player, data.get());
		action.start();
		return true;
	}

	@Override
	public void onStop() {
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			player.animation(data.animation);
			PRICELESS_GEM.inc(player);
		}
	}

	@Override
	public boolean canExecute() {
		return checkCrafting();
	}

	@Override
	public boolean init() {
		return checkCrafting();
	}

	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{data.gem});
	}

	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{data.produce});
	}

	@Override
	public double experience() {
		return data.experience;
	}

	@Override
	public int delay() {
		return 3;
	}

	@Override
	public boolean instant() {
		return true;
	}

	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(data.animation);
	}

	@Override
	public SkillData skill() {
		return fletching ? SkillData.FLETCHING : SkillData.CRAFTING;
	}

	private boolean checkCrafting() {
		if(!player.getSkills()[skill().getId()].reqLevel(data.requirement)) {
			player.message("You need a " + skill().toString() + " level of " + data.requirement + " to continue this action.");
			return false;
		}
		return true;
	}

	/**
	 * The enumerated type whose elements represent the data for gem
	 * cutting.
	 *
	 * @author <a href="http://www.rune-server.org/members/Golang/">Jay</a>
	 */
	private enum GemCraftingData {
		OPAL(new Item(ItemIdentifiers.UNCUT_OPAL), new Item(ItemIdentifiers.OPAL), 1, 15, 891),
		JADE(new Item(ItemIdentifiers.UNCUT_JADE), new Item(ItemIdentifiers.JADE), 13, 20, 891),
		RED_TOPAZ(new Item(ItemIdentifiers.UNCUT_RED_TOPAZ), new Item(ItemIdentifiers.RED_TOPAZ), 16, 25, 892),
		SAPPHIRE(new Item(ItemIdentifiers.UNCUT_SAPPHIRE), new Item(ItemIdentifiers.SAPPHIRE), 20, 50, 888),
		EMERALD(new Item(ItemIdentifiers.UNCUT_EMERALD), new Item(ItemIdentifiers.EMERALD), 27, 67.5, 889),
		RUBY(new Item(ItemIdentifiers.UNCUT_RUBY), new Item(ItemIdentifiers.RUBY), 34, 85, 887),
		DIAMOND(new Item(ItemIdentifiers.UNCUT_DIAMOND), new Item(ItemIdentifiers.DIAMOND), 43, 107.5, 890),
		DRAGONSTONE(new Item(ItemIdentifiers.UNCUT_DRAGONSTONE), new Item(ItemIdentifiers.DRAGONSTONE), 55, 137.5, 890),
		ONYX(new Item(ItemIdentifiers.UNCUT_ONYX), new Item(ItemIdentifiers.ONYX), 67, 167.5, 2717),
		OPAL_BOLT_TIPS(new Item(ItemIdentifiers.OPAL), new Item(ItemIdentifiers.OPAL_BOLT_TIPS, 12), 11, 1.5, 6702, true),
		JADE_BOLT_TIPS(new Item(ItemIdentifiers.JADE), new Item(ItemIdentifiers.JADE_BOLT_TIPS, 12), 26, 2.0, 6702, true),
		PEARL_BOLT_TIPS(new Item(ItemIdentifiers.OYSTER_PEARL), new Item(ItemIdentifiers.PEARL_BOLT_TIPS, 6), 41, 3.2, 6702, true),
		PEARL_BOLT_TIPS_2(new Item(ItemIdentifiers.OYSTER_PEARLS), new Item(ItemIdentifiers.PEARL_BOLT_TIPS, 24), 41, 3.2, 6702, true),
		TOPAZ_BOLT_TIPS(new Item(ItemIdentifiers.RED_TOPAZ), new Item(ItemIdentifiers.TOPAZ_BOLT_TIPS, 12), 48, 3.9, 6702, true),
		SAPPHIRE_BOLT_TIPS(new Item(ItemIdentifiers.SAPPHIRE), new Item(ItemIdentifiers.SAPPHIRE_BOLT_TIPS, 12), 56, 4.0, 6702, true),
		EMERALD_BOLT_TIPS(new Item(ItemIdentifiers.EMERALD), new Item(ItemIdentifiers.EMERALD_BOLT_TIPS, 12), 58, 5.5, 6702, true),
		RUBY_BOLT_TIPS(new Item(ItemIdentifiers.RUBY), new Item(ItemIdentifiers.RUBY_BOLT_TIPS, 12), 63, 6.3, 6702, true),
		DIAMOND_BOLT_TIPS(new Item(ItemIdentifiers.DIAMOND), new Item(ItemIdentifiers.DIAMOND_BOLT_TIPS, 12), 65, 7, 6702, true),
		DRAGONSTONE_BOLT_TIPS(new Item(ItemIdentifiers.DRAGONSTONE), new Item(ItemIdentifiers.DRAGON_BOLT_TIPS, 12), 71, 8.2, 6702, true),
		ONYX_BOLT_TIPS(new Item(ItemIdentifiers.ONYX), new Item(ItemIdentifiers.ONYX_BOLT_TIPS, 24), 73, 9.4, 6702, true),
		KEBBIT_BOLT(new Item(ItemIdentifiers.KEBBIT_SPIKE), new Item(ItemIdentifiers.KEBBIT_BOLTS, 6), 32, 5.8, 6702, true),
		LONG_KEBBIT_BOLT(new Item(ItemIdentifiers.LONG_KEBBIT_SPIKE), new Item(ItemIdentifiers.LONG_KEBBIT_BOLTS, 6), 83, 7.89, 6702, true),;

		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<GemCraftingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(GemCraftingData.class));

		/**
		 * The item required to register the produced form of.
		 */
		private final Item gem;

		/**
		 * The produced item.
		 */
		private final Item produce;

		/**
		 * The requirement for cutting this gem.
		 */
		private final int requirement;

		/**
		 * The experience gained upon cutting this gem.
		 */
		private final double experience;

		/**
		 * The animation for cutting this gem.
		 */
		private final Animation animation;

		/**
		 * Determines if this is a fletching skill action.
		 */
		private final boolean fletching;

		/**
		 * Constructs a new {@link GemCraftingData}.
		 *
		 * @param gem         {@link #gem}.
		 * @param produce     {@link #produce}.
		 * @param requirement {@link #requirement}.
		 * @param experience  {@link #experience}.
		 * @param animationId {@link #animation}.
		 * @param fletching   {@link #fletching}.
		 */
		GemCraftingData(Item gem, Item produce, int requirement, double experience, int animationId, boolean fletching) {
			this.gem = gem;
			this.produce = produce;
			this.requirement = requirement;
			this.experience = experience;
			this.animation = new Animation(animationId);
			this.fletching = fletching;
		}

		/**
		 * Constructs a new {@link GemCraftingData}.
		 *
		 * @param gem         {@link #gem}.
		 * @param produce     {@link #produce}.
		 * @param requirement {@link #requirement}.
		 * @param experience  {@link #experience}.
		 * @param animationId {@link #animation}.
		 */
		GemCraftingData(Item gem, Item produce, int requirement, double experience, int animationId) {
			this.gem = gem;
			this.produce = produce;
			this.requirement = requirement;
			this.experience = experience;
			this.animation = new Animation(animationId);
			this.fletching = false;
		}

		public static Optional<GemCraftingData> getDefinition(int itemUsed, int usedOn) {
			return VALUES.stream().filter($it -> $it.gem.getId() == itemUsed || $it.gem.getId() == usedOn).filter($it -> ItemIdentifiers.CHISEL == itemUsed || ItemIdentifiers.CHISEL == usedOn).findAny();
		}
	}
}

