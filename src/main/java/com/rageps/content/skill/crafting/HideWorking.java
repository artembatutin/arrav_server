package com.rageps.content.skill.crafting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rageps.content.achievements.Achievement;
import com.rageps.net.packet.out.SendItemModelInterface;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.action.impl.ProducingSkillAction;
import com.rageps.net.packet.out.SendEnterAmount;
import com.rageps.task.Task;
import com.rageps.util.TextUtils;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Holds functionality for creating products from hides.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class HideWorking extends ProducingSkillAction {
	
	/**
	 * The hide data this skill action is dependent of.
	 */
	private final HideData data;
	
	/**
	 * The amount of times this task should run for.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@link HideWorking} skill action.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 * @param amount {@link #amount}.
	 */
	private HideWorking(Player player, HideData data, int amount) {
		super(player, Optional.empty());
		this.data = data;
		this.amount = amount;
	}
	
	/**
	 * A constant defining the needle item.
	 */
	private static final Item NEEDLE = new Item(1733);
	
	/**
	 * A constant defining the thread item.
	 */
	private static final Item THREAD = new Item(1734);
	
	/**
	 * Attempts to register a certain amount of products from hides.
	 * @param player the player to register this for.
	 * @param buttonId the button interacted with.
	 * @return {@code true} if the skill action was started, {@code false} otherwise.
	 */
	public static boolean create(Player player, int buttonId) {
		HideData data = HideData.getDefinitionByButton(player.getAttributeMap().getInt(PlayerAttributes.CRAFTING_HIDE), buttonId).orElse(null);
		
		if(data == null || !player.getAttributeMap().getBoolean(PlayerAttributes.CRAFTING_HIDES)) {
			return false;
		}
		
		if(data.amount == -1) {
			player.out(new SendEnterAmount("Howmany hides would you like to register?", s -> () -> HideWorking.create(player, data, Integer.parseInt(s))));
			return true;
		}
		create(player, data, data.amount);
		return true;
	}
	
	/**
	 * Creates the item the player was hide working for.
	 * @param player the player to register this for.
	 * @param data the data to register.
	 * @param amount the amount to register.
	 */
	public static void create(Player player, HideData data, int amount) {
		HideWorking crafting = new HideWorking(player, data, amount);
		crafting.start();
	}
	
	/**
	 * Attempts to open the interface for the specified player.
	 * @param player the player to open this interface for.
	 * @param itemUsed the item that was used on another item.
	 * @param usedOn the other item that got used on by the first item.
	 */
	public static boolean openInterface(Player player, Item itemUsed, Item usedOn) {
		HideData data = HideData.getDefinition(itemUsed.getId(), usedOn.getId()).orElse(null);
		
		if(data == null) {
			return false;
		}

		player.getAttributeMap().set(PlayerAttributes.CRAFTING_HIDES, true);
		player.getAttributeMap().set(PlayerAttributes.CRAFTING_HIDE, data.required.getId());

		HideData[] group = GROUP.get(data.required.getId());
		
		if(group.length == 3) {//dragonhide
			player.interfaceText(8898, "What dragonhide would you like to make?");
			player.out(new SendItemModelInterface(8884, 200, group[2].product.getId()));
			player.out(new SendItemModelInterface(8883, 200, group[0].product.getId()));
			player.out(new SendItemModelInterface(8885, 200, group[1].product.getId()));
			player.interfaceText(8889, "\\n\\n\\n\\n\\n" + group[0].product.getDefinition().getName());
			player.interfaceText(8893, "\\n\\n\\n\\n\\n" + group[2].product.getDefinition().getName());
			player.interfaceText(8897, "\\n\\n\\n\\n\\n" + group[1].product.getDefinition().getName());
			player.chatWidget(8880);
		} else if(group.length == 5) {//snakeskin
			player.interfaceText(8966, "What snakeskin item would you like to make?");
			player.out(new SendItemModelInterface(8941, 180, 6322));
			player.out(new SendItemModelInterface(8942, 180, 6324));
			player.out(new SendItemModelInterface(8943, 180, 6330));
			player.out(new SendItemModelInterface(8944, 180, 6326));
			player.out(new SendItemModelInterface(8945, 180, 6328));
			player.interfaceText(8949, "\\n\\n\\n\\n\\nBody");
			player.interfaceText(8953, "\\n\\n\\n\\n\\nChaps");
			player.interfaceText(8957, "\\n\\n\\n\\n\\nVambraces");
			player.interfaceText(8961, "\\n\\n\\n\\n\\nBandana");
			player.interfaceText(8965, "\\n\\n\\n\\n\\nBoots");
			player.chatWidget(8938);
		}
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			amount--;
			if(amount <= 0)
				t.cancel();
			Achievement.QUALITY_HIDES.inc(player);
		}
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(new Animation(1249));
	}
	
	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(new Animation(1249));
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{data.required, THREAD});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{data.product});
	}
	
	@Override
	public int delay() {
		return 4;
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean init() {
		player.closeWidget();
		return checkCrafting();
	}
	
	@Override
	public boolean canExecute() {
		return checkCrafting();
	}
	
	@Override
	public double experience() {
		return data.experience;
	}
	
	@Override
	public SkillData skill() {
		return SkillData.CRAFTING;
	}
	
	@Override
	public void onStop() {
		player.getAttributeMap().reset(PlayerAttributes.CRAFTING_HIDES);
	}
	
	private boolean checkCrafting() {
		if(!player.getSkills()[skill().getId()].reqLevel(data.requirement)) {
			player.message("You need a crafting level of " + data.requirement + " to register " + TextUtils.appendIndefiniteArticle(data.product.getDefinition().getName()));
			return false;
		}
		return true;
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants used to
	 * produce items from hides.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum HideData {
		SNAKESKIN_BOOTS(35005, new Item(6289, 4), 6328, 45, 30D, 1),
		SNAKESKIN_BOOTS5(35004, SNAKESKIN_BOOTS, 5),
		SNAKESKIN_BOOTS10(35003, SNAKESKIN_BOOTS, 10),
		SNAKESKIN_BOOTSX(35002, SNAKESKIN_BOOTS, -1),
		
		SNAKESKIN_VAMBRACES(34253, new Item(6289, 8), 6330, 47, 35D, 1),
		SNAKESKIN_VAMBRACES5(34252, SNAKESKIN_VAMBRACES, 5),
		SNAKESKIN_VAMBRACES10(34251, SNAKESKIN_VAMBRACES, 10),
		SNAKESKIN_VAMBRACESX(35250, SNAKESKIN_VAMBRACES, -1),
		
		SNAKESKIN_BANDANA(35001, new Item(6289, 5), 6326, 48, 45D, 1),
		SNAKESKIN_BANDANA5(35000, SNAKESKIN_BANDANA, 5),
		SNAKESKIN_BANDANA10(34255, SNAKESKIN_BANDANA, 10),
		SNAKESKIN_BANDANAX(34254, SNAKESKIN_BANDANA, -1),
		
		SNAKESKIN_CHAPS(34249, new Item(6289, 12), 6324, 51, 50D, 1),
		SNAKESKIN_CHAPS5(34248, SNAKESKIN_CHAPS, 5),
		SNAKESKIN_CHAPS10(34247, SNAKESKIN_CHAPS, 10),
		SNAKESKIN_CHAPSX(34246, SNAKESKIN_CHAPS, -1),
		
		SNAKESKIN_BODY(34245, new Item(6289, 15), 6322, 53, 55D, 1),
		SNAKESKIN_BODY5(34244, SNAKESKIN_BODY, 5),
		SNAKESKIN_BODY10(34243, SNAKESKIN_BODY, 10),
		SNAKESKIN_BODYX(34242, SNAKESKIN_BODY, -1),
		
		GREEN_DHIDE_VAMBRACES(34189, new Item(1745, 1), 1065, 57, 62D, 1),
		GREEN_DHIDE_VAMBRACES5(34188, GREEN_DHIDE_VAMBRACES, 5),
		GREEN_DHIDE_VAMBRACES10(34187, GREEN_DHIDE_VAMBRACES, 10),
		GREEN_DHIDE_VAMBRACESX(34186, GREEN_DHIDE_VAMBRACES, -1),
		
		GREEN_DHIDE_CHAPS(34193, new Item(1745, 2), 1099, 60, 124D, 1),
		GREEN_DHIDE_CHAPS5(34192, GREEN_DHIDE_CHAPS, 5),
		GREEN_DHIDE_CHAPS10(34191, GREEN_DHIDE_CHAPS, 10),
		GREEN_DHIDE_CHAPSX(34190, GREEN_DHIDE_CHAPS, -1),
		
		GREEN_DHIDE_BODY(34185, new Item(1745, 3), 1135, 63, 186D, 1),
		GREEN_DHIDE_BODY5(34184, GREEN_DHIDE_BODY, 5),
		GREEN_DHIDE_BODY10(34183, GREEN_DHIDE_BODY, 10),
		GREEN_DHIDE_BODYX(34182, GREEN_DHIDE_BODY, -1),
		
		BLUE_DHIDE_VAMBRACES(34189, new Item(2505, 1), 2487, 66, 70D, 1),
		BLUE_DHIDE_VAMBRACES5(34188, BLUE_DHIDE_VAMBRACES, 5),
		BLUE_DHIDE_VAMBRACES10(34187, BLUE_DHIDE_VAMBRACES, 10),
		BLUE_DHIDE_VAMBRACESX(34186, BLUE_DHIDE_VAMBRACES, -1),
		
		BLUE_DHIDE_CHAPS(34193, new Item(2505, 2), 2493, 68, 140D, 1),
		BLUE_DHIDE_CHAPS5(34192, BLUE_DHIDE_CHAPS, 5),
		BLUE_DHIDE_CHAPS10(34191, BLUE_DHIDE_CHAPS, 10),
		BLUE_DHIDE_CHAPSX(34190, BLUE_DHIDE_CHAPS, -1),
		
		BLUE_DHIDE_BODY(34185, new Item(2505, 3), 2499, 71, 210D, 1),
		BLUE_DHIDE_BODY5(34184, BLUE_DHIDE_BODY, 5),
		BLUE_DHIDE_BODY10(34183, BLUE_DHIDE_BODY, 10),
		BLUE_DHIDE_BODYX(34182, BLUE_DHIDE_BODY, -1),
		
		RED_DHIDE_VAMBRACES(34189, new Item(2507, 1), 2489, 73, 78D, 1),
		RED_DHIDE_VAMBRACES5(34188, RED_DHIDE_VAMBRACES, 5),
		RED_DHIDE_VAMBRACES10(34187, RED_DHIDE_VAMBRACES, 10),
		RED_DHIDE_VAMBRACESX(34186, RED_DHIDE_VAMBRACES, -1),
		
		RED_DHIDE_CHAPS(34193, new Item(2507, 2), 2495, 75, 156D, 1),
		RED_DHIDE_CHAPS5(34192, RED_DHIDE_CHAPS, 5),
		RED_DHIDE_CHAPS10(34191, RED_DHIDE_CHAPS, 10),
		RED_DHIDE_CHAPSX(34190, RED_DHIDE_CHAPS, -1),
		
		RED_DHIDE_BODY(34185, new Item(2507, 3), 2501, 77, 234D, 1),
		RED_DHIDE_BODY5(34184, RED_DHIDE_BODY, 5),
		RED_DHIDE_BODY10(34183, RED_DHIDE_BODY, 10),
		RED_DHIDE_BODYX(34182, RED_DHIDE_BODY, -1),
		
		BLACK_DHIDE_VAMBRACES(34189, new Item(2509, 1), 2491, 79, 86D, 1),
		BLACK_DHIDE_VAMBRACES5(34188, BLACK_DHIDE_VAMBRACES, 5),
		BLACK_DHIDE_VAMBRACES10(34187, BLACK_DHIDE_VAMBRACES, 10),
		BLACK_DHIDE_VAMBRACESX(34186, BLACK_DHIDE_VAMBRACES, -1),
		
		BLACK_DHIDE_CHAPS(34193, new Item(2509, 2), 2497, 82, 172D, 1),
		BLACK_DHIDE_CHAPS5(34192, BLACK_DHIDE_CHAPS, 5),
		BLACK_DHIDE_CHAPS10(34191, BLACK_DHIDE_CHAPS, 10),
		BLACK_DHIDE_CHAPSX(34190, BLACK_DHIDE_CHAPS, -1),
		
		BLACK_DHIDE_BODY(34185, new Item(2509, 3), 2503, 84, 258D, 1),
		BLACK_DHIDE_BODY5(34184, BLACK_DHIDE_BODY, 5),
		BLACK_DHIDE_BODY10(34183, BLACK_DHIDE_BODY, 10),
		BLACK_DHIDE_BODYX(34182, BLACK_DHIDE_BODY, -1);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<HideData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(HideData.class));
		
		/**
		 * The button identification.
		 */
		private final int buttonId;
		
		/**
		 * The required item.
		 */
		private final Item required;
		
		/**
		 * The product produced.
		 */
		private final Item product;
		
		/**
		 * The requirement required;
		 */
		private final int requirement;
		
		/**
		 * The experience gained.
		 */
		private final double experience;
		
		/**
		 * The amount to register.
		 */
		private final int amount;
		
		/**
		 * Constructs a new {@link HideData}.
		 * @param buttonId {@link #buttonId}.
		 * @param required {@link #required}.
		 * @param product {@link #product}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 * @param amount {@link #amount}.
		 */
		HideData(int buttonId, int required, int product, int requirement, double experience, int amount) {
			this.buttonId = buttonId;
			this.required = new Item(required);
			this.product = new Item(product);
			this.requirement = requirement;
			this.experience = experience;
			this.amount = amount;
		}
		
		/**
		 * Constructs a new {@link HideData}.
		 * @param buttonId {@link #buttonId}.
		 * @param required {@link #required}.
		 * @param product {@link #product}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 * @param amount {@link #amount}.
		 */
		HideData(int buttonId, Item required, int product, int requirement, double experience, int amount) {
			this.buttonId = buttonId;
			this.required = required;
			this.product = new Item(product);
			this.requirement = requirement;
			this.experience = experience;
			this.amount = amount;
		}
		
		/**
		 * Constructs a new {@link HideData}.
		 * @param buttonId {@link #buttonId}.
		 * @param data the data to construct a new {@link HideData} from.
		 * @param amount {@link #amount}.
		 */
		HideData(int buttonId, HideData data, int amount) {
			this.buttonId = buttonId;
			this.required = data.required;
			this.product = data.product;
			this.requirement = data.requirement;
			this.experience = data.experience;
			this.amount = amount;
		}
		
		public static Optional<HideData> getDefinitionByButton(int source, int buttonId) {
			return VALUES.stream().filter($it -> $it.required.getId() == source && $it.buttonId == buttonId).findAny();
		}
		
		public static Optional<HideData> getDefinition(int itemUsed, int usedOn) {
			return VALUES.stream().filter($it -> $it.required.getId() == itemUsed || $it.required.getId() == usedOn).filter($it -> NEEDLE.getId() == itemUsed || NEEDLE.getId() == usedOn).findAny();
		}
	}
	
	private static final Int2ObjectArrayMap<HideData[]> GROUP = new Int2ObjectArrayMap<>(ImmutableMap.<Integer, HideData[]>builder().put(6289, new HideData[]{HideData.SNAKESKIN_BODY, HideData.SNAKESKIN_CHAPS, HideData.SNAKESKIN_VAMBRACES, HideData.SNAKESKIN_BANDANA, HideData.SNAKESKIN_BOOTS}).put(1745, new HideData[]{HideData.GREEN_DHIDE_BODY, HideData.GREEN_DHIDE_CHAPS, HideData.GREEN_DHIDE_VAMBRACES}).put(2505, new HideData[]{HideData.BLUE_DHIDE_BODY, HideData.BLUE_DHIDE_CHAPS, HideData.BLUE_DHIDE_VAMBRACES}).put(2507, new HideData[]{HideData.RED_DHIDE_BODY, HideData.RED_DHIDE_CHAPS, HideData.RED_DHIDE_VAMBRACES}).put(2509, new HideData[]{HideData.BLACK_DHIDE_BODY, HideData.BLACK_DHIDE_CHAPS, HideData.BLACK_DHIDE_VAMBRACES}).build());
	
}
