package com.rageps.content.skill.magic;

import com.google.common.collect.ImmutableMap;
import com.rageps.content.achievements.Achievement;
import com.rageps.content.skill.Skill;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.action.impl.ProducingSkillAction;
import com.rageps.task.Task;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Inventory;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The class which is responsible for enchanting crossbow bolts.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class EnchantCrossbowBolts extends ProducingSkillAction {
	
	/**
	 * The data this skill action is dependent of.
	 */
	private final BoltData data;
	
	/**
	 * Constructs a new {@link EnchantCrossbowBolts}.
	 * @param player {@link #getPlayer()}.
	 */
	public EnchantCrossbowBolts(Player player, BoltData data) {
		super(player, Optional.empty());
		this.data = data;
	}
	
	/**
	 * Attempts to enchant our bolts.
	 * @param player {@link #getPlayer()}.
	 * @param buttonId the button clicked.
	 * @return {@code true} if any of our bolts are enchanted, {@code false} otherwise.
	 */
	public static boolean enchant(Player player, int buttonId) {
		BoltData data = BoltData.VALUES.get(buttonId);
		if(data == null) {
			return false;
		}
		EnchantCrossbowBolts magic = new EnchantCrossbowBolts(player, data);
		magic.start();
		return true;
	}
	
	/**
	 * Attempts to open the enchant crossbow bolts interface.
	 * @param player {@link #getPlayer()}.
	 * @param buttonId the button the player clicked.
	 * @return {@code true} if the interface was opened, {@code false} otherwise.
	 */
	public static boolean openInterface(Player player, int buttonId) {
		if(buttonId != 75007) {
			return false;
		}
		Skill magic = player.getSkills()[Skills.MAGIC];
		if(!magic.reqLevel(4)) {
			player.message("You need a magic level of 4 to cast this spell.");
			return false;
		}
		
		Inventory inventory = player.getInventory();
		player.interfaceText(49009, "@gre@Magic 4");
		player.interfaceText(49012, (inventory.contains(new Item(564, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49013, (inventory.contains(new Item(556, 20)) ? "@gre@" : "@red@") + "20x");
		player.interfaceText(49017, (magic.reqLevel(7) ? "@gre@" : "@red@") + "Magic 7");
		player.interfaceText(49020, (inventory.contains(new Item(564, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49021, (inventory.contains(new Item(558, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49088, (inventory.contains(new Item(555, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49025, (magic.reqLevel(14) ? "@gre@" : "@red@") + "Magic 14");
		player.interfaceText(49028, (inventory.contains(new Item(564, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49029, (inventory.contains(new Item(557, 20)) ? "@gre@" : "@red@") + "20x");
		player.interfaceText(49033, (magic.reqLevel(24) ? "@gre@" : "@red@") + "Magic 24");
		player.interfaceText(49036, (inventory.contains(new Item(564, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49037, (inventory.contains(new Item(555, 20)) ? "@gre@" : "@red@") + "20x");
		player.interfaceText(49041, (magic.reqLevel(27) ? "@gre@" : "@red@") + "Magic 27");
		player.interfaceText(49044, (inventory.contains(new Item(564, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49089, (inventory.contains(new Item(556, 30)) ? "@gre@" : "@red@") + "30x");
		player.interfaceText(49045, (inventory.contains(new Item(561, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49049, (magic.reqLevel(29) ? "@gre@" : "@red@") + "Magic 29");
		player.interfaceText(49052, (inventory.contains(new Item(564, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49053, (inventory.contains(new Item(554, 20)) ? "@gre@" : "@red@") + "20x");
		player.interfaceText(49057, (magic.reqLevel(49) ? "@gre@" : "@red@") + "Magic 49");
		player.interfaceText(49090, (inventory.contains(new Item(564, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49060, (inventory.contains(new Item(554, 50)) ? "@gre@" : "@red@") + "50x");
		player.interfaceText(49061, (inventory.contains(new Item(565, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49065, (magic.reqLevel(57) ? "@gre@" : "@red@") + "Magic 57");
		player.interfaceText(49068, (inventory.contains(new Item(557, 100)) ? "@gre@" : "@red@") + "100x");
		player.interfaceText(49069, (inventory.contains(new Item(563, 20)) ? "@gre@" : "@red@") + "20x");
		player.interfaceText(49073, (magic.reqLevel(68) ? "@gre@" : "@red@") + "Magic 68");
		player.interfaceText(49076, (inventory.contains(new Item(563, 150)) ? "@gre@" : "@red@") + "150x");
		player.interfaceText(49077, (inventory.contains(new Item(566, 10)) ? "@gre@" : "@red@") + "10x");
		player.interfaceText(49081, (magic.reqLevel(87) ? "@gre@" : "@red@") + "Magic 87");
		player.interfaceText(49084, (inventory.contains(new Item(554, 200)) ? "@gre@" : "@red@") + "200x");
		player.interfaceText(49085, (inventory.contains(new Item(560, 10)) ? "@gre@" : "@red@") + "10x");
		player.widget(49000);
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			Achievement.ENCHANTER.inc(player, data.produced.getAmount());
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(data.required);
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{data.produced});
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
		player.closeWidget();
		return checkMagic();
	}
	
	@Override
	public boolean canExecute() {
		return checkMagic();
	}
	
	@Override
	public double experience() {
		return data.experience;
	}
	
	@Override
	public SkillData skill() {
		return SkillData.MAGIC;
	}
	
	private boolean checkMagic() {
		if(!player.getSkills()[skill().getId()].reqLevel(data.level)) {
			player.message("You need a magic level of " + data.level + " to cast this spell.");
			return false;
		}
		return true;
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants used to
	 * define the bolts that can be enchanted.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum BoltData {
		OPAL(191110, new Item[]{new Item(564, 10), new Item(556, 20), new Item(879, 10)}, 9236, 4, 9),
		SAPPHIRE(191119, new Item[]{new Item(564, 10), new Item(558, 10), new Item(555, 10), new Item(9337, 10)}, 9240, 7, 17),
		JADE(191127, new Item[]{new Item(564, 10), new Item(557, 20), new Item(9335, 10)}, 9237, 14, 19),
		PEARL(191135, new Item[]{new Item(564, 10), new Item(555, 20), new Item(880, 10)}, 9238, 24, 29),
		EMERALD(191143, new Item[]{new Item(564, 10), new Item(561, 10), new Item(556, 30), new Item(9338, 10)}, 9241, 27, 37),
		TOPAZ(191151, new Item[]{new Item(564, 10), new Item(554, 20), new Item(9336, 10)}, 9239, 29, 33),
		RUBY(191159, new Item[]{new Item(564, 10), new Item(565, 10), new Item(554, 50), new Item(9339, 10)}, 9242, 49, 59),
		DIAMOND(191167, new Item[]{new Item(564, 10), new Item(563, 20), new Item(557, 100), new Item(9340, 10)}, 9243, 57, 67),
		DRAGONSTONE(191175, new Item[]{new Item(564, 10), new Item(566, 10), new Item(557, 150), new Item(9341, 10)}, 9244, 68, 78),
		ONYX(191183, new Item[]{new Item(564, 10), new Item(554, 200), new Item(560, 10), new Item(9342, 10)}, 9245, 87, 97);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableMap<Integer, BoltData> VALUES = ImmutableMap.copyOf(Stream.of(values()).collect(Collectors.toMap(t -> t.buttonId, Function.identity())));
		
		/**
		 * The button identification for this bolt.
		 */
		private final int buttonId;
		
		/**
		 * The items required.
		 */
		private final Item[] required;
		
		/**
		 * The item produced.
		 */
		private final Item produced;
		
		/**
		 * The level required to cast this spell.
		 */
		private final int level;
		
		/**
		 * The experience gained upon casting this spell.
		 */
		private final double experience;
		
		/**
		 * Constructs a new {@link BoltData}.
		 * @param buttonId {@link #buttonId}
		 * @param required {@link #required}.
		 * @param produced {@link #produced}.
		 * @param level {@link #level}.
		 * @param experience {@link #experience}.
		 */
		private BoltData(int buttonId, Item[] required, int produced, int level, double experience) {
			this.buttonId = buttonId;
			this.required = required;
			this.produced = new Item(produced);
			this.produced.setAmount(10);
			this.level = level;
			this.experience = experience;
		}
	}
}
