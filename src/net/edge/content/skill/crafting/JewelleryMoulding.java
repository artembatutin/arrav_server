package net.edge.content.skill.crafting;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.edge.action.impl.ItemOnObjectAction;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.Skills;
import net.edge.content.skill.action.impl.ProducingSkillAction;
import net.edge.net.packet.out.SendItemModelInterface;
import net.edge.net.packet.out.SendItemOnInterfaceSlot;
import net.edge.task.Task;
import net.edge.util.TextUtils;
import net.edge.world.Animation;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.object.GameObject;

import java.util.Optional;

import static net.edge.content.achievements.Achievement.GOLD_AND_RIGNS;

/**
 * Holds functionality for moulding jewellery.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class JewelleryMoulding extends ProducingSkillAction {
	
	/**
	 * The data this skill action is dependent of.
	 */
	private final JewelleryData data;
	
	/**
	 * The amount of times this task should run for.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@link JewelleryMoulding}.
	 * @param player {@link #getPlayer()}.
	 * @param data   {@link #data}.
	 * @param amount {@link #amount}.
	 */
	public JewelleryMoulding(Player player, JewelleryData data, int amount) {
		super(player, Optional.empty());
		this.data = data;
		this.amount = amount;
	}
	
	/**
	 * A constant representing the gold bar item.
	 */
	private static final Item GOLD_BAR = new Item(2357);
	
	/**
	 * A constant representing the ring mould item.
	 */
	private static final Item RING_MOULD = new Item(1592);
	
	/**
	 * A constant representing the necklace mould item.
	 */
	private static final Item NECKLACE_MOULD = new Item(1597);
	
	/**
	 * A constant representing the amulet mould item.
	 */
	private static final Item AMULET_MOULD = new Item(1595);
	
	/**
	 * Attempts to mould jewellery.
	 * @param player {@link #getPlayer()}.
	 * @param item   the item to mould.
	 * @param amount the amount to mould.
	 * @return {@code true} if the item was moulded, {@code false} otherwise.
	 */
	public static final boolean mould(Player player, int item, int amount) {
		JewelleryData data = getDefinition(item).orElse(null);
		
		if(data == null) {
			return false;
		}
		
		JewelleryMoulding crafting = new JewelleryMoulding(player, data, amount);
		crafting.start();
		return true;
	}
	
	public static void action() {
		ItemOnObjectAction a = new ItemOnObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, Item item, int container, int slot) {
				player.message("item on object");
				if(!object.getDefinition().getName().contains("Pottery Oven") && !object.getDefinition().getName().contains("Furnace"))
					return false;
				JewelleryData[] rings = GROUP.get(RING_MOULD.getId());
				if(player.getInventory().contains(RING_MOULD)) {
					for(int i = 0; i < rings.length; i++) {
						boolean check = rings[i].required.isPresent() ? player.getInventory()
								.contains(rings[i].required.get()) && player.getSkills()[Skills.CRAFTING].reqLevel(rings[i].requirement) : player.getSkills()[Skills.CRAFTING].reqLevel(rings[i].requirement);
						if(check && player.getInventory().contains(GOLD_BAR)) {
							player.out(new SendItemOnInterfaceSlot(4233, rings[i].product, i));
						} else {
							player.out(new SendItemOnInterfaceSlot(4233, new Item(-1, 1), i));
						}
						
						player.text(4230, player.getInventory().contains(GOLD_BAR) ? "" : "You need a gold bar to craft rings.");
						player.out(new SendItemModelInterface(4229, player.getInventory().contains(GOLD_BAR) ? 0 : 120, player.getInventory().contains(GOLD_BAR) ? -1 : GOLD_BAR.getId()));
					}
					;
				} else {
					player.out(new SendItemModelInterface(4229, 120, RING_MOULD.getId()));
					for(int i = 0; i < rings.length; i++) {
						player.out(new SendItemOnInterfaceSlot(4233, new Item(-1), i));
					}
					player.text(4230, "You need a ring mould to craft rings.");
				}
				
				JewelleryData[] necklaces = GROUP.get(NECKLACE_MOULD.getId());
				
				if(player.getInventory().contains(NECKLACE_MOULD)) {
					for(int i = 0; i < necklaces.length; i++) {
						boolean check = necklaces[i].required.isPresent() ? player.getInventory()
								.contains(necklaces[i].required.get()) && player.getSkills()[Skills.CRAFTING].reqLevel(necklaces[i].requirement) : player.getSkills()[Skills.CRAFTING].reqLevel(necklaces[i].requirement);
						if(check && player.getInventory().contains(GOLD_BAR)) {
							player.out(new SendItemOnInterfaceSlot(4239, necklaces[i].product, i));
						} else {
							player.out(new SendItemOnInterfaceSlot(4239, new Item(-1, 1), i));
						}
						
						player.text(4236, player.getInventory().contains(GOLD_BAR) ? "" : "You need a gold bar to craft necklaces.");
						player.out(new SendItemModelInterface(4235, player.getInventory().contains(GOLD_BAR) ? 0 : 120, player.getInventory().contains(GOLD_BAR) ? -1 : GOLD_BAR.getId()));
					}
				} else {
					player.out(new SendItemModelInterface(4235, 120, NECKLACE_MOULD.getId()));
					for(int i = 0; i < rings.length; i++) {
						player.out(new SendItemOnInterfaceSlot(4239, new Item(-1), i));
					}
					
					player.text(4236, "You need a necklace mould to craft necklaces.");
				}
				
				JewelleryData[] amulets = GROUP.get(AMULET_MOULD.getId());
				if(player.getInventory().contains(AMULET_MOULD)) {
					for(int i = 0; i < amulets.length; i++) {
						boolean check = amulets[i].required.isPresent() ? player.getInventory()
								.contains(amulets[i].required.get()) && player.getSkills()[Skills.CRAFTING].reqLevel(amulets[i].requirement) : player.getSkills()[Skills.CRAFTING].reqLevel(amulets[i].requirement);
						if(check && player.getInventory().contains(GOLD_BAR)) {
							player.out(new SendItemOnInterfaceSlot(4245, amulets[i].product, i));
						} else {
							player.out(new SendItemOnInterfaceSlot(4245, new Item(-1, 1), i));
						}
						
						player.text(4242, player.getInventory().contains(GOLD_BAR) ? "" : "You need a gold bar to craft necklaces.");
						player.out(new SendItemModelInterface(4241, player.getInventory().contains(GOLD_BAR) ? 0 : 120, player.getInventory().contains(GOLD_BAR) ? -1 : GOLD_BAR.getId()));
					}
				} else {
					player.out(new SendItemModelInterface(4241, 120, AMULET_MOULD.getId()));
					for(int i = 0; i < rings.length; i++) {
						player.out(new SendItemOnInterfaceSlot(4245, new Item(-1), i));
					}
					
					player.text(4242, "You need an amulet mould to craft amulets.");
				}
				
				player.widget(4161);
				return true;
			}
		};
		a.registerObj(2643);
		a.registerItem(GOLD_BAR.getId());
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			amount--;
			if(amount <= 0)
				t.cancel();
			GOLD_AND_RIGNS.inc(player);
		}
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(new Animation(899));
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(data.required.isPresent() ? new Item[]{data.required.get(), GOLD_BAR} : new Item[]{GOLD_BAR});
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
		return true;
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
	
	private boolean checkCrafting() {
		if(!player.getSkills()[skill().getId()].reqLevel(data.requirement)) {
			player.message("You need a crafting level of " + data.requirement + " to craft " + TextUtils.appendIndefiniteArticle(data.product.getDefinition().getName()) + ".");
			return false;
		}
		
		return true;
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants defining
	 * data that can be used to register products from gold bars.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum JewelleryData {
		GOLD_RING(1635, 5, 15D),
		GOLD_NECKLACE(1654, 6, 20D),
		GOLD_AMULET(1673, 8, 30D),
		
		SAPPHIRE_RING(1607, 1637, 20, 40D),
		SAPPHIRE_NECKLACE(1607, 1656, 22, 55D),
		SAPPHIRE_AMULET(1607, 1675, 24, 65D),
		
		EMERALD_RING(1605, 1639, 27, 55D),
		EMERALD_NECKLACE(1605, 1658, 29, 60D),
		EMERALD_AMULET(1605, 1677, 31, 70D),
		
		RUBY_RING(1603, 1641, 34, 70D),
		RUBY_NECKLACE(1603, 1660, 40, 75D),
		RUBY_AMULET(1603, 1679, 50, 85D),
		
		DIAMOND_RING(1601, 1643, 43, 85D),
		DIAMOND_NECKLACE(1601, 1662, 56, 90D),
		DIAMOND_AMULET(1601, 1681, 70, 100D),
		
		DRAGONSTONE_RING(1615, 1645, 55, 100D),
		DRAGON_NECKLACE(1615, 1664, 72, 105D),
		DRAGONSTONE_AMULET(1615, 1683, 80, 150D),
		
		ONYX_RING(6573, 6575, 67, 115D),
		ONYX_NECKLACE(6573, 6577, 82, 120D),
		ONYX_AMULET(6573, 6579, 90, 165D);
		
		/**
		 * The item required.
		 */
		private final Optional<Item> required;
		
		/**
		 * The item produced.
		 */
		private final Item product;
		
		/**
		 * The requirement required.
		 */
		private final int requirement;
		
		/**
		 * The experience gained.
		 */
		private final double experience;
		
		/**
		 * Constructs a new {@link JewelleryData}.
		 * @param required    {@link #required}.
		 * @param product     {@link #product}.
		 * @param requirement {@link #requirement}.
		 * @param experience  {@link #experience}.
		 */
		JewelleryData(int required, int product, int requirement, double experience) {
			this.required = Optional.of(new Item(required));
			this.product = new Item(product);
			this.requirement = requirement;
			this.experience = experience;
		}
		
		/**
		 * Constructs a new {@link JewelleryData}.
		 * @param product     {@link #product}.
		 * @param requirement {@link #requirement}.
		 * @param experience  {@link #experience}.
		 */
		JewelleryData(int product, int requirement, double experience) {
			this.required = Optional.empty();
			this.product = new Item(product);
			this.requirement = requirement;
			this.experience = experience;
		}
	}
	
	private static Optional<JewelleryData> getDefinition(int item) {
		for(JewelleryData[] jewels : GROUP.values()) {
			for(JewelleryData data : jewels) {
				if(data.product.getId() == item) {
					return Optional.of(data);
				}
			}
		}
		return Optional.empty();
	}
	
	private static final Int2ObjectArrayMap<JewelleryData[]> GROUP = new Int2ObjectArrayMap<>(ImmutableMap.<Integer, JewelleryData[]>builder().put(RING_MOULD.getId(), new JewelleryData[]{JewelleryData.GOLD_RING, JewelleryData.SAPPHIRE_RING, JewelleryData.EMERALD_RING, JewelleryData.RUBY_RING, JewelleryData.DIAMOND_RING, JewelleryData.DRAGONSTONE_RING, JewelleryData.ONYX_RING})
			.put(NECKLACE_MOULD.getId(), new JewelleryData[]{JewelleryData.GOLD_NECKLACE, JewelleryData.SAPPHIRE_NECKLACE, JewelleryData.EMERALD_NECKLACE, JewelleryData.RUBY_NECKLACE, JewelleryData.DIAMOND_NECKLACE, JewelleryData.DRAGON_NECKLACE, JewelleryData.ONYX_NECKLACE})
			.put(AMULET_MOULD.getId(), new JewelleryData[]{JewelleryData.GOLD_AMULET, JewelleryData.SAPPHIRE_AMULET, JewelleryData.EMERALD_AMULET, JewelleryData.RUBY_AMULET, JewelleryData.DIAMOND_AMULET, JewelleryData.DRAGONSTONE_AMULET, JewelleryData.ONYX_AMULET})
			.build());
	
}
