package net.arrav.content.item;

import com.google.common.collect.ImmutableSet;
import net.arrav.content.skill.Skills;
import net.arrav.world.Animation;
import net.arrav.world.Graphic;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.ItemContainer;

import java.util.Optional;

public enum ItemCombine {
	
	ORANGE_DYE(new Item(1769), new Item(1765), new Item(1763)),
	GREEN_DYE(new Item(1771), new Item(1765), new Item(1767)),
	PURPLE_DYE(new Item(1773), new Item(1767), new Item(1763)),
	
	ARMADYL_GS(new Item(11694), new Item(11690), new Item(11702)),
	BANDOS_GS(new Item(11696), new Item(11690), new Item(11704)),
	SARADOMIN_GS(new Item(11698), new Item(11690), new Item(11706)),
	ZAMORAK_GS(new Item(11700), new Item(11690), new Item(11708)),
	
	SLAYER_HELM(new Item(13263), new Item(8921), new Item(4166), new Item(4164), new Item(4168), new Item(4551)) {
		@Override
		public boolean requirement(Player player) {
			int craft = player.getSkills()[Skills.CRAFTING].getRealLevel();
			if(craft < 55) {
				player.message("You need 55 crafting to create the slayer helmet.");
				return false;
			}
			if(player.getAttr().get("slayer_tasks").getInt() < 35) {
				player.message("You must complete at least 35 slayer tasks.");
				return false;
			}
			player.message("Don't attempt to color this item.");
			return true;
		}
	},
	FULL_SLAYER_HELMET(new Item(15492), new Item(15488), new Item(15490), new Item(13263)) {
		@Override
		public boolean requirement(Player player) {
			if(player.getAttr().get("slayer_tasks").getInt() < 35) {
				player.message("You must complete at least 35 slayer tasks.");
				return false;
			}
			player.message("Don't attempt to color this item.");
			return true;
		}
	},
	
	MITHRIL_GRAPPLE_HOOK(new Item(9419), new Item(9416), new Item(954)),
	
	DARKBOW_YELLOW(new Item(15701), new Item(11235), new Item(1765)),
	DARKBOW_BLUE(new Item(15702), new Item(11235), new Item(1767)),
	DARKBOW_WHITE(new Item(15703), new Item(11235), new Item(239)),
	DARKBOW_GREEN(new Item(15704), new Item(11235), new Item(1771)),
	CLEAN_DARKBOW_YELLOW(new Item(11235), new Item(15701), new Item(3188)),
	CLEAN_DARKBOW_BLUE(new Item(11235), new Item(15702), new Item(3188)),
	CLEAN_DARKBOW_WHITE(new Item(11235), new Item(15703), new Item(3188)),
	CLEAN_DARKBOW_GREEN(new Item(11235), new Item(15704), new Item(3188)),
	
	WHIP_YELLOW(new Item(15441), new Item(4151), new Item(1765)),
	WHIP_BLUE(new Item(15442), new Item(4151), new Item(1767)),
	WHIP_WHITE(new Item(15443), new Item(4151), new Item(239)),
	WHIP_GREEN(new Item(15444), new Item(4151), new Item(1771)),
	CLEAN_WHIP_YELLOW(new Item(4151), new Item(15441), new Item(3188)),
	CLEAN_WHIP_BLUE(new Item(4151), new Item(15442), new Item(3188)),
	CLEAN_WHIP_WHITE(new Item(4151), new Item(15443), new Item(3188)),
	CLEAN_WHIP_GREEN(new Item(4151), new Item(15444), new Item(3188));
	
	/**
	 * The product combining the ingredients.
	 */
	private final Item product;
	
	/**
	 * The ingredients for this process, removing them all and giving the {@link #product}.
	 */
	private final Item[] ingredients;
	
	/**
	 * The optional animation used on the combination.
	 */
	private final Optional<Animation> animation;
	
	/**
	 * The optional graphic used on the combination.
	 */
	private final Optional<Graphic> graphic;
	
	static final ImmutableSet<ItemCombine> VALUES = ImmutableSet.copyOf(values());
	
	/**
	 * Creates a new combination without any animation or graphic.
	 * @param product     the product being created.
	 * @param ingredients the ingredients used.
	 */
	ItemCombine(Item product, Item... ingredients) {
		this.product = product;
		this.ingredients = ingredients;
		this.animation = Optional.empty();
		this.graphic = Optional.empty();
	}
	
	/**
	 * Creates a new item combination with a {@link Animation} and {@link Graphic}.
	 * @param product     the product being created.
	 * @param anim        the animation being handled on combination.
	 * @param graphic     the graphic being handled on combination.
	 * @param ingredients the ingredients used.
	 */
	ItemCombine(Item product, Animation anim, Graphic graphic, Item... ingredients) {
		this.product = product;
		this.ingredients = ingredients;
		this.animation = Optional.of(anim);
		this.graphic = Optional.of(graphic);
	}
	
	public static boolean handle(Player player, ItemContainer container, Item use, Item used) {
		for(ItemCombine comb : VALUES) {
			boolean firstFound = false;
			boolean secondFound = false;
			for(Item item : comb.getIngredients()) {
				if(!firstFound && item.equals(use))
					firstFound = true;
				if(!secondFound && item.equals(used))
					secondFound = true;
				if(firstFound && secondFound) {
					comb.combine(player, container);
					return true;
				}
			}
		}
		return false;
	}
	
	public void combine(Player player, ItemContainer container) {
		if(!requirement(player))
			return;
		if(container.containsAll(ingredients)) {
			animation.ifPresent(player::animation);
			graphic.ifPresent(player::graphic);
			container.removeAll(ingredients);
			container.add(product);
		} else {
			player.message("You do not have all of the ingredients to do this.");
		}
	}
	
	/**
	 * An requirement check before combining the item.
	 * Checks for levels or anything else specific.
	 * @param player the player doing the action.
	 * @return {@code true} if the check passed, {@code false} otherwise.
	 */
	public boolean requirement(Player player) {
		return true;
	}
	
	/**
	 * Gets the {@link #product}
	 */
	public Item getProduct() {
		return product;
	}
	
	/**
	 * Gets the {@link #ingredients}
	 */
	public Item[] getIngredients() {
		return ingredients;
	}
	
	/**
	 * Gets the {@link #animation}
	 */
	public Optional<Animation> getAnimation() {
		return animation;
	}
	
	/**
	 * Gets the {@link #graphic}
	 */
	public Optional<Graphic> getGraphic() {
		return graphic;
	}
	
}
