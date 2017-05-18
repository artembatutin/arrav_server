package net.edge.world.content.skill.cooking;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.task.Task;
import net.edge.world.content.skill.SkillData;
import net.edge.world.content.skill.action.impl.ProducingSkillAction;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Holds functionality for creating all sorts of dough.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DoughCreation extends ProducingSkillAction {
	
	/**
	 * The dough data this skill action is dependent of.
	 */
	private final DoughData data;
	
	/**
	 * The amount of times to run this task for.
	 */
	private int counter;
	
	/**
	 * Constructs a new {@link DoughCreation}.
	 * @param player  {@link #getPlayer()}.
	 * @param data    {@link #data}.
	 * @param counter {@link #counter}.
	 */
	public DoughCreation(Player player, DoughData data, int counter) {
		super(player, Optional.empty());
		this.data = data;
		this.counter = counter;
	}
	
	/**
	 * Attempts to start the skill action.
	 * @param player   {@link #getPlayer()}.
	 * @param buttonId the button the player clicked.
	 * @return <true> if the skill action was started, <false> otherwise.
	 */
	public static boolean create(Player player, int buttonId) {
		Optional<DoughData> data = DoughData.getDefinition(buttonId);
		
		if(!data.isPresent() || !player.getAttr().get("creating_dough").getBoolean()) {
			return false;
		}
		
		if(data.get().amount == -1) {
			player.getAttr().get("creating_dough_data").set(data.get());
			player.getMessages().sendEnterAmount("How many you would like to make?", s -> () -> DoughCreation.create(player, (DoughData) player.getAttr().get("creating_dough_data").get(), Integer.parseInt(s)));
			return true;
		}
		create(player, data.get(), data.get().amount);
		return true;
	}
	
	/**
	 * Creates the item the player was creating dough for.
	 * @param player {@link #getPlayer()}.
	 * @param data   {@link #data}.
	 * @param amount {@link #amount}.
	 * @return <true> if the skill action was started, <false> otherwise.
	 */
	public static void create(Player player, DoughData data, int amount) {
		DoughCreation cooking = new DoughCreation(player, data, amount);
		cooking.start();
	}
	
	/**
	 * Attempts to open the dough creation interface.
	 * @param player the player using the dough creation interface.
	 * @param used   the item used.
	 * @param usedOn the item that was used on.
	 * @return {@code true} if the skill action was started, {@code false} otherwise.
	 */
	public static boolean openInterface(Player player, Item used, Item usedOn) {
		if(used.getId() != POT_OF_FLOUR.getId() && usedOn.getId() != POT_OF_FLOUR.getId()) {
			return false;
		}
		if(used.getId() != VIAL_OF_WATER.getId() && usedOn.getId() != VIAL_OF_WATER.getId()) {
			return false;
		}
		
		player.getAttr().get("creating_dough").set(true);
		
		player.getMessages().sendString("What sort of dough do you wish to make?", 8922);
		
		DoughData.VALUES.forEach(dough -> {
			if(dough.amount == 1) {
				player.getMessages().sendItemModelOnInterface(dough.modelFrame, 150, dough.produced.getId());
				player.getMessages().sendString("\\n\\n\\n\\n\\n" + dough.toString(), dough.nameFrame);
			}
		});
		player.getMessages().sendChatInterface(8899);
		return true;
	}
	
	/**
	 * A constant representing the pot of flour item.
	 */
	private static final Item POT_OF_FLOUR = new Item(1933);
	
	/**
	 * A constant representing the vial of water item.
	 */
	private static final Item VIAL_OF_WATER = new Item(227);
	
	/**
	 * A constant representing the empty pot item.
	 */
	private static final Item POT = new Item(1931);
	
	/**
	 * A constant representing the empty vial item.
	 */
	private static final Item VIAL = new Item(229);
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			if(--counter < 1) {
				t.cancel();
			}
		}
	}
	
	@Override
	public void onStop() {
		player.getAttr().get("creating_dough").set(false);
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{VIAL_OF_WATER, POT_OF_FLOUR});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{VIAL, POT, data.produced});
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
		return 1;
	}
	
	@Override
	public SkillData skill() {
		return SkillData.COOKING;
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants used to
	 * register dough with.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum DoughData {
		BREAD_DOUGH(34205, 8902, 8906, 2307, 1),
		BREAD_DOUGH_5(34204, BREAD_DOUGH, 5),
		BREAD_DOUGH_10(34203, BREAD_DOUGH, 10),
		BREAD_DOUGH_X(34202, BREAD_DOUGH, -1),
		
		PASTRY_DOUGH(34209, 8903, 8910, 1953, 1),
		PASTRY_DOUGH_5(34208, PASTRY_DOUGH, 5),
		PASTRY_DOUGH_10(34207, PASTRY_DOUGH, 10),
		PASTRY_DOUGH_X(34206, PASTRY_DOUGH, -1),
		
		PIZZA_DOUGH(34213, 8904, 8914, 2283, 1),
		PIZZA_DOUGH_5(34212, PIZZA_DOUGH, 5),
		PIZZA_DOUGH_10(34211, PIZZA_DOUGH, 10),
		PIZZA_DOUGH_X(34210, PIZZA_DOUGH, -1),
		
		PITTA_DOUGH(34217, 8905, 8918, 1863, 1),
		PITTA_DOUGH_5(34216, PITTA_DOUGH, 5),
		PITTA_DOUGH_10(34215, PITTA_DOUGH, 10),
		PITTA_DOUGH_X(34214, PITTA_DOUGH, -1);;
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<DoughData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(DoughData.class));
		
		/**
		 * The button identification.
		 */
		private final int buttonId;
		
		/**
		 * The frame the model of this item is drawed on.
		 */
		private final int modelFrame;
		
		/**
		 * The frame the name of this item is drawed on.
		 */
		private final int nameFrame;
		
		/**
		 * The item produced.
		 */
		private final Item produced;
		
		/**
		 * The amount to register.
		 */
		private final int amount;
		
		/**
		 * Constructs a new {@link DoughCreation}.
		 * @param buttonId   {@link #buttonId}.
		 * @param modelFrame {@link #modelFrame}.
		 * @param nameFrame  {@link #nameFrame}.
		 * @param produced   {@link #produced}.
		 * @param amount     {@link #amount}.
		 */
		DoughData(int buttonId, int modelFrame, int nameFrame, int produced, int amount) {
			this.buttonId = buttonId;
			this.modelFrame = modelFrame;
			this.nameFrame = nameFrame;
			this.produced = new Item(produced);
			this.amount = amount;
		}
		
		/**
		 * Constructs a new {@link DoughCreation}.
		 * @param buttonId {@link #buttonId}.
		 * @param data     the data to register the produced item from.
		 * @param amount   {@link #amount}.
		 */
		DoughData(int buttonId, DoughData data, int amount) {
			this.buttonId = buttonId;
			this.modelFrame = data.modelFrame;
			this.nameFrame = data.nameFrame;
			this.produced = data.produced;
			this.amount = amount;
		}
		
		public static Optional<DoughData> getDefinition(int buttonId) {
			return VALUES.stream().filter(def -> def.buttonId == buttonId).findAny();
		}
		
		@Override
		public String toString() {
			return name().toLowerCase().replaceAll("_", " ");
		}
	}
}
