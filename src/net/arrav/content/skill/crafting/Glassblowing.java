package net.arrav.content.skill.crafting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.arrav.content.skill.SkillData;
import net.arrav.content.skill.action.impl.ProducingSkillAction;
import net.arrav.net.packet.out.SendEnterAmount;
import net.arrav.task.Task;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.ItemIdentifiers;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Holds functionality for glassblowing.
 * @author <a href="http://www.rune-server.org/members/Golang/">Jay</a>
 */
public final class Glassblowing extends ProducingSkillAction {
	
	/**
	 * The definition we're currently creating items for.
	 */
	private final GlassblowingData data;
	
	/**
	 * The amount we're creating.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@link Glassblowing}.
	 * @param player {@link #getPlayer()}.
	 * @param data   {@link #data}.
	 * @param amount {@link #amount}.
	 */
	public Glassblowing(Player player, GlassblowingData data, int amount) {
		super(player, Optional.empty());
		this.data = data;
		this.amount = amount;
	}
	
	/**
	 * Attempts to start the skill action.
	 * @param player   {@link #getPlayer()}.
	 * @param buttonId the button the player clicked.
	 * @return <true> if the skill action was started, <false> otherwise.
	 */
	public static boolean blow(Player player, int buttonId) {
		Optional<GlassblowingData> data = GlassblowingData.getDefinition(buttonId);
		
		if(!data.isPresent() || !player.getAttr().get("crafting_glass").getBoolean()) {
			return false;
		}
		
		if(data.get().amount == -1) {
			player.out(new SendEnterAmount("How many you would like to blow?", s -> () -> Glassblowing.blow(player, data.get(), Integer.parseInt(s))));
			return true;
		}
		blow(player, data.get(), data.get().amount);
		return true;
	}
	
	/**
	 * Creates the item the player was glassblowing for.
	 * @param player {@link #getPlayer()}.
	 * @param data   {@link #data}.
	 * @param amount {@link #amount}.
	 * @return <true> if the skill action was started, <false> otherwise.
	 */
	public static void blow(Player player, GlassblowingData data, int amount) {
		Glassblowing crafting = new Glassblowing(player, data, amount);
		crafting.start();
	}
	
	/**
	 * Attempts to use the {@code item}, on the other {@code item2} and open the
	 * glassblowing interface if the correct items were used.
	 * @param player {@link #getPlayer()}.
	 * @param item   the item used.
	 * @param item2  the item used on.
	 * @return <true> if the interface was opened, <false> otherwise.
	 */
	public static boolean openInterface(Player player, Item item, Item item2) {
		if(item2.getId() != ItemIdentifiers.GLASSBLOWING_PIPE && item.getId() != ItemIdentifiers.GLASSBLOWING_PIPE) {
			return false;
		}
		if(item2.getId() != ItemIdentifiers.MOLTEN_GLASS && item.getId() != ItemIdentifiers.MOLTEN_GLASS) {
			return false;
		}
		
		player.getAttr().get("crafting_glass").set(true);
		player.widget(11462);
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			amount--;
			
			if(amount <= 0)
				t.cancel();
		}
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(new Animation(884));
	}
	
	@Override
	public boolean canExecute() {
		if(!checkCrafting())
			return false;
		return true;
	}
	
	@Override
	public boolean init() {
		player.closeWidget();
		return checkCrafting();
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(ItemIdentifiers.MOLTEN_GLASS)});
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
	public SkillData skill() {
		return SkillData.CRAFTING;
	}
	
	@Override
	public void onStop() {
		player.getAttr().get("crafting_glass").set(false);
	}
	
	/**
	 * Checks if the skill action can be started.
	 * @return <true> if it can, <false> otherwise.
	 */
	private boolean checkCrafting() {
		if(!player.getInventory().contains(ItemIdentifiers.GLASSBLOWING_PIPE)) {
			player.message("You need a glassblowing pipe.");
			return false;
		}
		if(!player.getSkills()[skill().getId()].reqLevel(data.requirement)) {
			player.message("You need a crafting level of " + data.requirement + " to continue this action.");
			return false;
		}
		return true;
	}
	
	/**
	 * The enumerated type whose elements represent the data for blowing
	 * glass.
	 * @author <a href="http://www.rune-server.org/members/Golang/">Jay</a>
	 */
	public enum GlassblowingData {
		VIAL(44210, new Item(ItemIdentifiers.VIAL, 1), 33, 35),
		VIAL5(44209, VIAL, 5),
		VIAL10(44208, VIAL, 10),
		VIALX(44207, VIAL, -1),
		
		ORB(48108, new Item(ItemIdentifiers.ORB, 1), 46, 52.5),
		ORB5(48107, ORB, 5),
		ORB10(48106, ORB, 10),
		ORBX(44211, ORB, -1),
		
		BEER(48112, new Item(ItemIdentifiers.BEER_GLASS, 1), 1, 17.5),
		BEER5(48111, BEER, 5),
		BEER10(48110, BEER, 10),
		BEERX(48109, BEER, -1),
		
		CANDLE(48116, new Item(ItemIdentifiers.CANDLE_LANTERN, 1), 1, 17.5),
		CANDLE5(48115, CANDLE, 5),
		CANDLE10(48114, CANDLE, 10),
		CANDLEX(48113, CANDLE, -1),
		
		OIL_LAMP(48120, new Item(ItemIdentifiers.OIL_LAMP, 1), 12, 25),
		OIL_LAMP5(48119, OIL_LAMP, 5),
		OIL_LAMP10(48118, OIL_LAMP, 10),
		OIL_LAMPX(48117, OIL_LAMP, -1),
		
		FISHBOWL(24059, new Item(ItemIdentifiers.FISHBOWL_HELMET, 1), 42, 42.5),
		FISHBOWL5(24058, FISHBOWL, 5),
		FISHBOWL10(24057, FISHBOWL, 10),
		FISHBOWLX(24056, FISHBOWL, -1),
		
		LANTERN_LENS(48124, new Item(ItemIdentifiers.LANTERN_LENS, 1), 49, 55),
		LANTERN_LENS5(48123, LANTERN_LENS, 5),
		LANTERN_LENS10(48122, LANTERN_LENS, 10),
		LANTERN_LENSX(48121, LANTERN_LENS, -1);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<GlassblowingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(GlassblowingData.class));
		
		/**
		 * The item produced.
		 */
		private final Item produce;
		
		/**
		 * The requirement for creating the {@code produce}d item.
		 */
		private final int requirement;
		
		/**
		 * The experience gained upon creating the produced item.
		 */
		private final double experience;
		
		/**
		 * The button identification chained to producing this item.
		 */
		private final int buttonId;
		
		/**
		 * The amount we're creating for this produced item.
		 */
		private final int amount;
		
		/**
		 * Constructs a new {@link GlassblowingData} enumerator.
		 * @param buttonId    {@link #buttonId}.
		 * @param produce     {@link #produce}.
		 * @param requirement {@link #requirement}.
		 * @param experience  {@link #experience}.
		 */
		private GlassblowingData(int buttonId, Item produce, int requirement, double experience) {
			this.buttonId = buttonId;
			this.produce = produce;
			this.requirement = requirement;
			this.experience = experience;
			this.amount = 1;
		}
		
		/**
		 * Constructs a new {@link GlassblowingData} enumerator.
		 * @param buttonId {@link #buttonId}.
		 * @param data     the data for what we're creating.
		 * @param amount   {@link #amount}.
		 */
		private GlassblowingData(int buttonId, GlassblowingData data, int amount) {
			this.buttonId = buttonId;
			this.produce = data.produce;
			this.requirement = data.requirement;
			this.experience = data.experience;
			this.amount = amount;
			
		}
		
		/**
		 * Searches for a {@code button} which matches the enumerators button id.
		 * @param button the button to check for a match.
		 * @return the glassblowing data enumerator wrapped in an optional, {@link Optional#empty()}
		 * otherwise.
		 */
		public static Optional<GlassblowingData> getDefinition(int button) {
			return VALUES.stream().filter(def -> def.buttonId == button).findAny();
		}
	}
}

