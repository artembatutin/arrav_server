package com.rageps.content.skill.crafting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.action.impl.ProducingSkillAction;
import com.rageps.task.Task;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Holds functionality for stringing amulets.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AmuletStringing extends ProducingSkillAction {
	
	/**
	 * The amulet data this skill action is dependent of.
	 */
	private final AmuletData data;
	
	/**
	 * Determines if we're stringing by spell.
	 */
	private final boolean spell;
	
	/**
	 * Constructs a new {@link AmuletStringing}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 * @param spell {@link #spell}.
	 */
	public AmuletStringing(Player player, AmuletData data, boolean spell) {
		super(player, Optional.empty());
		this.data = data;
		this.spell = spell;
	}
	
	/**
	 * A constant representing the ball of wool item.
	 */
	private static final Item BALL_OF_WOOL = new Item(1759);
	
	/**
	 * Attempts to start stringing any amulets.
	 * @param player the player attempting the skill action.
	 * @param used the item used.
	 * @param usedOn the item that got used on.
	 * @return {@code true} if the skill action started, {@code false} otherwise.
	 */
	public static boolean create(Player player, Item used, Item usedOn) {
		AmuletData data = AmuletData.getDefinition(used.getId(), usedOn.getId()).orElse(null);
		
		if(data == null) {
			return false;
		}
		
		create(player, data, false);
		return true;
	}
	
	public static void create(Player player, AmuletData data, boolean spell) {
		AmuletStringing crafting = new AmuletStringing(player, data, spell);
		crafting.start();
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success && spell) {
			player.animation(new Animation(4412));
			player.graphic(new Graphic(728, 100));
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(spell ? new Item[]{data.item} : new Item[]{data.item, BALL_OF_WOOL});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{data.stringed});
	}
	
	@Override
	public int delay() {
		return spell ? 5 : 3;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public boolean init() {
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
	
	@Override
	public double experience() {
		return 3;
	}
	
	@Override
	public SkillData skill() {
		return SkillData.CRAFTING;
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants used
	 * to string amulets.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum AmuletData {
		GOLD_AMULET(1673, 1692),
		SAPPHIRE_AMULET(1675, 1694),
		EMERALD_AMULET(1677, 1696),
		RUBY_AMULET(1679, 1698),
		DIAMOND_AMULET(1681, 1700),
		DRAGONSTONE_AMULET(1683, 1702),
		ONYX_AMULET(6579, 6581);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<AmuletData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(AmuletData.class));
		
		/**
		 * The item which needs to be stringed.
		 */
		private final Item item;
		
		/**
		 * The item which is stringed.
		 */
		private final Item stringed;
		
		/**
		 * Constructs a new {@link AmuletData}.
		 * @param itemId {@link #item}.
		 * @param stringed {@link #stringed}.
		 */
		AmuletData(int itemId, int stringed) {
			this.item = new Item(itemId);
			this.stringed = new Item(stringed);
		}
		
		public static Optional<AmuletData> getDefinition(Player player) {
			return VALUES.stream().filter($it -> player.getInventory().contains($it.item)).findAny();
		}
		
		public static Optional<AmuletData> getDefinition(int itemUsed, int usedOn) {
			return VALUES.stream().filter($it -> $it.item.getId() == itemUsed || $it.item.getId() == usedOn).filter($it -> BALL_OF_WOOL.getId() == itemUsed || BALL_OF_WOOL.getId() == usedOn).findAny();
		}
	}
}
