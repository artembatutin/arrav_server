package net.edge.world.content.skill.crafting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.utils.TextUtils;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.skill.SkillData;
import net.edge.world.content.skill.action.impl.ProducingSkillAction;
import net.edge.task.Task;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Holds functionality for tanning items.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Tanning extends ProducingSkillAction {
	
	/**
	 * The tanning data this skill action is dependent of.
	 */
	private final TanningData data;
	
	/**
	 * Determines if were tanning by the lunar spell.
	 */
	private final boolean spell;
	
	/**
	 * The amount of times this task should run for..
	 */
	private int amount;
	
	/**
	 * Constructs a new {@link Tanning} skill action.
	 * @param player {@link #getPlayer()}.
	 * @param data   {@link #data}.
	 * @param amount {@link #amount}.
	 * @param spell  {@link #spell}.
	 */
	public Tanning(Player player, TanningData data, int amount, boolean spell) {
		super(player, Optional.empty());
		this.data = data;
		this.amount = amount;
		this.spell = spell;
	}
	
	/**
	 * Attempts to register a certain amount of tanning products.
	 * @param player   the player creating the tanning products.
	 * @param buttonId the button the player interacted with.
	 * @return {@code true} if the player created any products, {@code false} otherwise.
	 */
	public static boolean create(Player player, int buttonId) {
		TanningData data = VALUES.get(buttonId);
		
		if(data == null) {
			return false;
		}
		
		if(data.count == -1) {
			player.getMessages().sendEnterAmount("How many would you like to tan?", s -> () -> Tanning.create(player, data, Integer.parseInt(s), false));
			return true;
		}
		
		int amount = data.count;
		if(data.count == -2) {
			amount = player.getInventory().computeAmountForId(data.required.getId());
		}
		
		create(player, data, amount, false);
		return true;
	}
	
	/**
	 * Creates the item the player was tanning for.
	 * @param player {@link #getPlayer()}.
	 * @param data   {@link #data}.
	 * @param amount {@link #amount}.
	 * @param spell  {@link #spell}.
	 * @return <true> if the skill action was started, <false> otherwise.
	 */
	public static void create(Player player, TanningData data, int amount, boolean spell) {
		Tanning crafting = new Tanning(player, data, amount, spell);
		crafting.start();
	}
	
	/**
	 * Attempts to openShop the interface for the specified player.
	 * @param player the player to openShop the interface for.
	 * @param item   the item being used on the npc.
	 * @param npc    the npc that the item was used on.
	 * @return {@code true} if the interface was opened, {@code false} otherwise.
	 */
	public static boolean openInterface(Player player, Item item, Npc npc) {
		if(npc.getId() != 805) {
			return false;
		}
		
		if(!TanningData.getByItem(item.getId()).isPresent()) {
			player.message("You can't use " + TextUtils.appendIndefiniteArticle(item.getDefinition().getName()) + " on " + npc.getDefinition().getName());
			return false;
		}
		
		openInterface(player);
		return true;
	}
	
	public static void openInterface(Player player) {
		VALUES.forEach((b, t) -> {
			if(t.nameFrame != -1) {
				player.getMessages().sendString("@or1@" + t.required.getDefinition().getName(), t.nameFrame);
			}
			if(t.priceFrame != -1) {
				String color = player.getInventory().contains(t.required) && player.getInventory().contains(t.cost) ? "@gre@" : "@red@";
				player.getMessages().sendString(color + t.cost.getAmount() + " Coins", t.priceFrame);
			}
			if(t.modelFrame != -1) {
				player.getMessages().sendItemModelOnInterface(t.modelFrame, 250, t.required.getId());
			}
		});
		player.getMessages().sendInterface(14670);
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			t.cancel();
			
			if(!spell) {
				player.message("The crafting master tans your hides...");
			}
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		int amount = player.getInventory().computeAmountForId(data.required.getId());
		if(this.amount > amount) {
			this.amount = amount;
		}
		return Optional.of(spell ? new Item[]{new Item(data.required.getId(), this.amount)} : new Item[]{new Item(data.required.getId(), this.amount), data.cost});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{new Item(data.product.getId(), amount)});
	}
	
	@Override
	public int delay() {
		return 0;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public boolean init() {
		player.getMessages().sendCloseWindows();
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
	
	@Override
	public double experience() {
		return 0;
	}
	
	@Override
	public SkillData skill() {
		return SkillData.CRAFTING;
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants used to
	 * define the data required to tan hides.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum TanningData {
		SOFT_LEATHER(57225, 1, 14777, 14785, 14769, 1739, 1741, 1),
		SOFT_LEATHER5(57217, SOFT_LEATHER, 5),
		SOFT_LEATHERX(57209, SOFT_LEATHER, -1),
		SOFT_LEATHERALL(57201, SOFT_LEATHER, -2),
		
		HARD_LEATHER(57226, 3, 14778, 14786, 14770, 1739, 1743, 1),
		HARD_LEATHER5(57218, HARD_LEATHER, 5),
		HARD_LEATHERX(57210, HARD_LEATHER, -1),
		HARD_LEATHERALL(57202, HARD_LEATHER, -2),
		
		SNAKESKIN(57227, 15, 14779, 14787, 14771, 6287, 6289, 1),
		SNAKESKIN5(57219, SNAKESKIN, 5),
		SNAKESKINX(57211, SNAKESKIN, -1),
		SNAKESKINALL(57203, SNAKESKIN, -2),
		
		SWAMP_SNAKESKIN(57228, 20, 14780, 14788, 14772, 7801, 6289, 1),
		SWAMP_SNAKESKIN5(57220, SWAMP_SNAKESKIN, 5),
		SWAMP_SNAKESKINX(57212, SWAMP_SNAKESKIN, -1),
		SWAMP_SNAKESKINALL(57204, SWAMP_SNAKESKIN, -2),
		
		GREEN_DRAGONHIDE(57229, 20, 14781, 14789, 14773, 1753, 1745, 1),
		GREEN_DRAGONHIDE5(57221, GREEN_DRAGONHIDE, 5),
		GREEN_DRAGONHIDEX(57213, GREEN_DRAGONHIDE, -1),
		GREEN_DRAGONHIDEALL(57205, GREEN_DRAGONHIDE, -2),
		
		BLUE_DRAGONHIDE(57230, 20, 14782, 14790, 14774, 1751, 2505, 1),
		BLUE_DRAGONHIDE5(57222, BLUE_DRAGONHIDE, 5),
		BLUE_DRAGONHIDEX(57214, BLUE_DRAGONHIDE, -1),
		BLUE_DRAGONHIDEALL(57206, BLUE_DRAGONHIDE, -2),
		
		RED_DRAGONHIDE(57231, 20, 14783, 14791, 14775, 1749, 2507, 1),
		RED_DRAGONHIDE5(57223, RED_DRAGONHIDE, 5),
		RED_DRAGONHIDEX(57215, RED_DRAGONHIDE, -1),
		RED_DRAGONHIDEALL(57207, RED_DRAGONHIDE, -2),
		
		BLACK_DRAGONHIDE(57232, 20, 14784, 14792, 14776, 1747, 2509, 1),
		BLACK_DRAGONHIDE5(57224, BLACK_DRAGONHIDE, 5),
		BLACK_DRAGONHIDEX(57216, BLACK_DRAGONHIDE, -1),
		BLACK_DRAGONHIDEALL(57208, BLACK_DRAGONHIDE, -2);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<TanningData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(TanningData.class));
		
		/**
		 * The button identification.
		 */
		private final int buttonId;
		
		/**
		 * The price cost.
		 */
		private final Item cost;
		
		/**
		 * The frame id where the name is drawed.
		 */
		private final int nameFrame;
		
		/**
		 * The frame id where the price is drawed.
		 */
		private final int priceFrame;
		
		/**
		 * The frame id where the item model is drawed.
		 */
		private final int modelFrame;
		
		/**
		 * The required item.
		 */
		private final Item required;
		
		/**
		 * The product produced.
		 */
		private final Item product;
		
		/**
		 * The amount of times to register.
		 */
		private final int count;
		
		/**
		 * Constructs a new {@link TanningData}.
		 * @param buttonId   {@link #buttonId}.
		 * @param cost       {@link #cost}.
		 * @param nameFrame  {@link #nameFrame}.
		 * @param priceFrame {@link #priceFrame}.
		 * @param modelFrame {@link #modelFrame}.
		 * @param required   {@link #required}.
		 * @param product    {@link #product}.
		 * @param count      {@link #count}.
		 */
		TanningData(int buttonId, int cost, int nameFrame, int priceFrame, int modelFrame, int required, int product, int count) {
			this.buttonId = buttonId;
			this.cost = new Item(995, cost);
			this.nameFrame = nameFrame;
			this.priceFrame = priceFrame;
			this.modelFrame = modelFrame;
			this.required = new Item(required);
			this.product = new Item(product);
			this.count = count;
		}
		
		/**
		 * Constructs a new {@link TanningData}.
		 * @param buttonId {@link #buttonId}.
		 * @param data     the tanning data to construct a new one from.
		 * @param count    {@link #count}.
		 */
		TanningData(int buttonId, TanningData data, int count) {
			this.buttonId = buttonId;
			this.cost = data.cost;
			this.nameFrame = -1;
			this.priceFrame = -1;
			this.modelFrame = -1;
			this.required = data.required;
			this.product = data.product;
			this.count = count;
		}
		
		public static Optional<TanningData> getByButton(int buttonId) {
			return VALUES.stream().filter(d -> d.buttonId == buttonId).findAny();
		}
		
		public static Optional<TanningData> getByItem(int item) {
			return VALUES.stream().filter(d -> d.required.getId() == item).findAny();
		}
		
		public static Optional<TanningData> getByPlayer(Player player) {
			return VALUES.stream().filter(d -> player.getInventory().contains(d.required)).findAny();
		}
		
	}
	
	/**
	 * Caches our enum values.
	 */
	private static final ImmutableMap<Integer, TanningData> VALUES = ImmutableMap.copyOf(Stream.of(TanningData.values()).collect(Collectors.toMap(t -> t.buttonId, Function.identity())));
}
