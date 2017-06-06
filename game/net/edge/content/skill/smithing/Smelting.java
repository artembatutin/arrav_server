package net.edge.content.skill.smithing;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.event.impl.ObjectEvent;
import net.edge.task.Task;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.Skills;
import net.edge.content.skill.action.impl.ProducingSkillAction;
import net.edge.world.Animation;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.object.ObjectNode;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;

/**
 * Holds functionality for smelting ores.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Smelting extends ProducingSkillAction {
	
	/**
	 * The definition of the bar we're creating.
	 */
	private final SmeltingData definition;
	
	/**
	 * Determines if we're smelting dependant on the superheat spell.
	 */
	private final boolean spell;
	
	/**
	 * The amount we're producing.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@link Smelting}.
	 * @param player     {@link #getPlayer()}.
	 * @param definition {@link #definition}.
	 * @param amount     {@link #amount}.
	 * @param spell      {@link #spell}.
	 */
	public Smelting(Player player, SmeltingData definition, int amount, boolean spell) {
		super(player, Optional.empty());
		this.definition = definition;
		this.amount = amount;
		this.spell = spell;
	}
	
	/**
	 * Attempts to start smelting for the specified {@code player}.
	 * @param player   the player whom is smelting the bar.
	 * @param buttonId the button this player clicked.
	 * @return <true> if the player could smelt, <false> otherwise.
	 */
	public static boolean smelt(Player player, int buttonId) {
		Optional<SmeltingData> data = SmeltingData.getDefinition(buttonId);
		
		if(!data.isPresent()) {
			return false;
		}
		
		if(data.get().amount == -1) {
			player.getMessages().sendEnterAmount("How many you would like to melt?", s -> () -> Smelting.smelt(player, data.get(), Integer.parseInt(s)));
			return true;
		}
		smelt(player, data.get(), data.get().amount);
		return true;
	}
	
	/**
	 * Smelts the {@code data} for the specified {@code player} and produces the
	 * exact amount the player inputed if he has the requirements.
	 * @param player {@link #getPlayer()}.
	 * @param data   {@link #definition}.
	 * @param amount {@link #amount}.
	 */
	public static void smelt(Player player, SmeltingData data, int amount) {
		Smelting smithing = new Smelting(player, data, amount, false);
		player.getMessages().sendCloseWindows();
		smithing.start();
	}
	
	@Override
	public boolean isPrioritized() {
		return spell;
	}
	
	/**
	 * Sends the items on the smelting interface.
	 * @param player the player we're sending these values for.
	 */
	public static void clearInterfaces(Player player) {
		for(int j = 0; j < SMELT_FRAME.length; j++) {
			player.getMessages().sendItemModelOnInterface(SMELT_FRAME[j], 150, SMELT_BARS[j]);
		}
	}
	
	/**
	 * The array which holds all the possible furnace ids a player
	 * can smelt his bars in.
	 */
	private static final int[] FURNACE_IDS = {26814, 37651, 11666};
	
	/**
	 * The array which holds all the frames you can draw an item on.
	 */
	private static final int[] SMELT_FRAME = {2405, 2406, 2407, 2409, 2410, 2411, 2412, 2413};
	
	/**
	 * The bar identification to be drawed on each frame.
	 */
	private static final int[] SMELT_BARS = {2349, 2351, 2355, 2353, 2357, 2359, 2361, 2363};
	
	public static void event() {
		ObjectEvent smelt = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.getMessages().sendChatInterface(2400);
				return true;
			}
		};
		for(int o : FURNACE_IDS) {
			smelt.registerSecond(o);
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(definition.required);
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			if(!spell) {
				player.animation(new Animation(899));
			}
			amount--;
			
			if(amount < 1) {
				t.cancel();
			}
		}
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(definition.produced);
	}
	
	@Override
	public int delay() {
		return spell ? 2 : 3;
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean init() {
		return canSmelt();
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
	
	@Override
	public double experience() {
		return definition.experience;
	}
	
	@Override
	public SkillData skill() {
		return SkillData.SMITHING;
	}
	
	/**
	 * Checks if the player has the requirements to smelt.
	 * @return <true> if the player has the requirements, <false> otherwise.
	 */
	public boolean canSmelt() {
		if(!player.getSkills()[Skills.SMITHING].reqLevel(definition.requirement)) {
			player.message("You need a smithing level of " + definition.requirement + " to smelt this bar.");
			return false;
		}
		if(!player.getInventory().containsAll(definition.required)) {
			player.message("You don't have the required items to register this smelting bar.");
			return false;
		}
		return true;
	}
	
	/**
	 * The enumerated type whose elements represent definitions for each
	 * smeltable bar.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 * @author <a href="http://www.rune-server.org/members/royal/">Royal</a>
	 */
	public enum SmeltingData {
		BRONZE(15147, new Item[]{new Item(438), new Item(436)}, new Item[]{new Item(2349)}, 6.25, 1),
		BRONZE_5(15146, BRONZE, 5),
		BRONZE_10(10247, BRONZE, 10),
		BRONZE_X(9110, BRONZE, -1),
		
		IRON(15151, new Item[]{new Item(440)}, new Item[]{new Item(2351)}, 12.5, 15),
		IRON_5(15150, IRON, 5),
		IRON_10(15149, IRON, 10),
		IRON_X(15148, IRON, -1),
		
		SILVER(15155, new Item[]{new Item(442)}, new Item[]{new Item(2355)}, 13.67, 20),
		SILVER_5(15154, SILVER, 5),
		SILVER_10(15153, SILVER, 10),
		SILVER_X(15152, SILVER, -1),
		
		STEEL(15159, new Item[]{new Item(440), new Item(453, 2)}, new Item[]{new Item(2353)}, 17.5, 30),
		STEEL_5(15158, STEEL, 5),
		STEEL_10(15157, STEEL, 10),
		STEEL_X(15156, STEEL, -1),
		
		GOLD(15163, new Item[]{new Item(444)}, new Item[]{new Item(2357)}, 22.5, 40),
		GOLD_5(15162, GOLD, 5),
		GOLD_10(15161, GOLD, 10),
		GOLD_X(15160, GOLD, -1),
		
		MITHRIL(29017, new Item[]{new Item(447), new Item(453, 4)}, new Item[]{new Item(2359)}, 30, 50),
		MITHRIL_5(29016, MITHRIL, 5),
		MITHRIL_10(24253, MITHRIL, 10),
		MITHRIL_X(16062, MITHRIL, -1),
		
		ADAMANT(29022, new Item[]{new Item(449), new Item(453, 6)}, new Item[]{new Item(2361)}, 37.5, 70),
		ADAMANT_5(29020, ADAMANT, 5),
		ADAMANT_10(29019, ADAMANT, 10),
		ADAMANT_X(29018, ADAMANT, -1),
		
		RUNITE(29026, new Item[]{new Item(451), new Item(453, 8)}, new Item[]{new Item(2363)}, 50, 85),
		RUNITE_5(29025, RUNITE, 5),
		RUNITE_10(29024, RUNITE, 10),
		RUNITE_X(29023, RUNITE, -1);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<SmeltingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(SmeltingData.class));
		
		/**
		 * The button identification.
		 */
		private final int buttonId;
		
		/**
		 * The required items to smelt this bar.
		 */
		private final Item[] required;
		
		/**
		 * The produced items for smelting the required items.
		 */
		private final Item[] produced;
		
		/**
		 * The experience gained upon smelting one bar.
		 */
		private final double experience;
		
		/**
		 * The requirement required to smelt the bar.
		 */
		private final int requirement;
		
		/**
		 * The amount we're producing.
		 */
		private final int amount;
		
		/**
		 * Constructs a new {@link SmeltingData} enumerator.
		 * @param buttonId    {@link #buttonId}.
		 * @param required    {@link #required}.
		 * @param produced    {@link #produced}.
		 * @param experience  {@link #experience}.
		 * @param requirement {@link #requirement}.
		 */
		SmeltingData(int buttonId, Item[] required, Item[] produced, double experience, int requirement) {
			this.buttonId = buttonId;
			this.required = required;
			this.produced = produced;
			this.experience = experience;
			this.requirement = requirement;
			this.amount = 1;
		}
		
		/**
		 * Constructs a new {@link SmeltingData} enumerator.
		 * @param buttonId   {@link #buttonId}.
		 * @param definition the definition for this bar.
		 * @param amount     {@link #amount}.
		 */
		SmeltingData(int buttonId, SmeltingData definition, int amount) {
			this.buttonId = buttonId;
			this.required = definition.required;
			this.produced = definition.produced;
			this.experience = definition.experience;
			this.requirement = definition.requirement;
			this.amount = amount;
		}
		
		/**
		 * Searches for a match for the internal button identification.
		 * @param buttonId the button id to search for matches with.
		 * @return the smeltingdata wrapped in an optional, {@link Optional#empty()} otherwise.
		 */
		public static Optional<SmeltingData> getDefinition(int buttonId) {
			return VALUES.stream().filter(def -> def.buttonId == buttonId).findAny();
		}
		
		/**
		 * Searches for a match for the internal required items.
		 * @param itemId the required items to search for matches with.
		 * @return the smeltingdata wrapped in an optional, {@link Optional#empty()} otherwise.
		 */
		public static Optional<SmeltingData> getDefinitionByItem(int itemId) {
			for(SmeltingData data : VALUES) {
				for(Item item : data.required) {
					if(item.getId() == itemId) {
						return Optional.of(data);
					}
				}
			}
			return Optional.empty();
		}
	}
}
