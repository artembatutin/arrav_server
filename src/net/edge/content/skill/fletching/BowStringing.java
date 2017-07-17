package net.edge.content.skill.fletching;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.task.Task;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.Skills;
import net.edge.content.skill.action.impl.ProducingSkillAction;
import net.edge.world.Animation;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Holds functionality for stringing bows.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BowStringing extends ProducingSkillAction {
	
	/**
	 * The definition of this log.
	 */
	private final StringingData definition;
	
	/**
	 * Constructs a new {@link BowStringing}.
	 * @param player     {@link #getPlayer()}
	 * @param definition the definition we're currently handling.
	 */
	public BowStringing(Player player, StringingData definition) {
		super(player, Optional.empty());
		
		this.definition = definition;
	}
	
	public static boolean string(Player player, Item firstItem, Item secondItem) {
		Optional<StringingData> bow = StringingData.getDefinition(firstItem.getId(), secondItem.getId());
		
		if(!bow.isPresent()) {
			return false;
		}
		if(firstItem.getId() == bow.get().unstrung.getId() && secondItem.getId() == 1777 || firstItem.getId() == 1777 && secondItem.getId() == bow.get().unstrung.getId()) {
			BowStringing fletching = new BowStringing(player, bow.get());
			fletching.start();
			return true;
		}
		return false;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			player.animation(definition.animation);
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{definition.unstrung, new Item(1777)});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{definition.strung});
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
		if(!checkFletching()) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canExecute() {
		if(!checkFletching()) {
			return false;
		}
		return true;
	}
	
	@Override
	public double experience() {
		return definition.experience;
	}
	
	@Override
	public SkillData skill() {
		return SkillData.FLETCHING;
	}
	
	private boolean checkFletching() {
		if(!player.getSkills()[Skills.FLETCHING].reqLevel(definition.requirement)) {
			player.message("You need a fletching level of " + definition.requirement + " to string this bow.");
			return false;
		}
		return true;
	}
	
	/**
	 * Holds data for stringing bows.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum StringingData {
		SHORT_BOW(50, 841, 5, 5, 6678),
		LONG_BOW(48, 839, 10, 10, 6684),
		OAK_SHORT_BOW(54, 843, 20, 16.5, 6679),
		OAK_LONG_BOW(56, 845, 25, 25, 6685),
		COMPOSITE_BOW(4825, 4827, 30, 45, 6686),
		WILLOW_SHORT_BOW(60, 849, 35, 33.3, 6680),
		WILLOW_LONG_BOW(58, 847, 40, 41.5, 6686),
		MAPLE_SHORT_BOW(64, 853, 50, 50, 6681),
		MAPLE_LONG_BOW(62, 851, 55, 58.3, 6687),
		YEW_SHORT_BOW(68, 857, 65, 68.5, 6682),
		YEW_LONG_BOW(66, 855, 70, 75, 6688),
		MAGIC_SHORT_BOW(72, 861, 80, 83.3, 6683),
		MAGIC_LONG_BOW(70, 859, 85, 91.5, 6689);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<StringingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(StringingData.class));
		
		/**
		 * The item for the unstrung bow.
		 */
		private final Item unstrung;
		
		/**
		 * The item for the strung bow.
		 */
		private final Item strung;
		
		/**
		 * The requirement for stringing this bow.
		 */
		private final int requirement;
		
		/**
		 * The experience gained upon stringing this bow.
		 */
		private final double experience;
		
		/**
		 * The animation performed upon stringing this bow.
		 */
		private final Animation animation;
		
		/**
		 * Constructs a new {@link StringingData} enumerator.
		 * @param unstrung    {@link #unstrung}.
		 * @param strung      {@link #strung}.
		 * @param requirement {@link #requirement}.
		 * @param experience  {@link #experience}.
		 * @param animation   {@link #animation}.
		 */
		private StringingData(int unstrung, int strung, int requirement, double experience, int animation) {
			this.unstrung = new Item(unstrung);
			this.strung = new Item(strung);
			this.requirement = requirement;
			this.experience = experience;
			this.animation = new Animation(animation);
		}
		
		public static Optional<StringingData> getDefinition(int firstItem, int secondItem) {
			return VALUES.stream().filter(def -> def.unstrung.getId() == firstItem || def.unstrung.getId() == secondItem).findAny();
		}
	}
	
}
