package net.edge.world.content.skill.herblore;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.utils.TextUtils;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.skill.SkillData;
import net.edge.world.content.skill.action.impl.ProducingSkillAction;
import net.edge.task.Task;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Represents the procession for creating tars.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class TarCreation extends ProducingSkillAction {
	
	/**
	 * The {@link GuamTar} holding all the data required for processing
	 * the creation of {@link GuamTar}'s.
	 */
	private final GuamTar definition;
	
	/**
	 * Constructs a new {@link TarCreation}.
	 * @param player     {@link #getPlayer()}.
	 * @param firstItem  the first item that was used on the second item.
	 * @param secondItem the second item that was used on by the first item.
	 */
	public TarCreation(Player player, Item firstItem, Item secondItem) {
		super(player, Optional.of(player.getPosition()));
		
		Item item = firstItem.getId() == SWAMP_TAR.getId() ? secondItem : firstItem;
		definition = GuamTar.getDefinition(item.getId()).orElse(null);
	}
	
	/**
	 * Represents the identifier for the swamp tar.
	 */
	private static final Item SWAMP_TAR = new Item(1939, 15);
	
	/**
	 * Represents the identifier for the pestle and mortar.
	 */
	private static final Item PESTLE_MORTAR = new Item(233);
	
	/**
	 * Represents the animation for tar creation.
	 */
	private static final Animation ANIMATION = new Animation(364);
	
	/**
	 * Produces guam tars if the player has the requirements required.
	 * @param player     {@link #getPlayer()};
	 * @param firstItem  the first item that was used on the second item.
	 * @param secondItem the second item that was used on by the first item.
	 * @return <true> if the produce was successful, <false> otherwise.
	 */
	public static boolean produce(Player player, Item firstItem, Item secondItem) {
		TarCreation tar = new TarCreation(player, firstItem, secondItem);
		
		if(tar.definition == null) {
			return false;
		}
		
		if(firstItem.getId() == SWAMP_TAR.getId() && secondItem.equals(tar.definition.herb) || firstItem.equals(tar.definition.herb) && secondItem.getId() == SWAMP_TAR.getId()) {
			tar.start();
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			getPlayer().animation(ANIMATION);
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{definition.herb, SWAMP_TAR});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{definition.tar});
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
		if(!canProduce()) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canExecute() {
		if(!canProduce()) {
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
		return SkillData.HERBLORE;
	}
	
	private boolean canProduce() {
		if(definition == null) {
			return false;
		}
		if(!getPlayer().getSkills()[skill().getId()].reqLevel(definition.requirement)) {
			getPlayer().message("You need a herblore level of " + definition.requirement + " to register " + TextUtils.appendIndefiniteArticle(definition.toString()) + ".");
			return false;
		}
		if(!getPlayer().getInventory().contains(PESTLE_MORTAR)) {
			getPlayer().message("You need a pestle and mortar to do this.");
			return false;
		}
		if(!getPlayer().getInventory().contains(SWAMP_TAR)) {
			getPlayer().message("You need 15 swamp tars to register a " + definition.toString() + ".");
			return false;
		}
		return true;
	}
	
	/**
	 * The data required for processing the creation of guam tars.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum GuamTar {
		GUAM_TAR(249, 10142, 19, 30),
		MARRENTIL_TAR(251, 10143, 31, 42.5),
		TARROMIN_TAR(253, 10144, 39, 55),
		HARRALANDER_TAR(255, 10145, 44, 72.5);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<GuamTar> VALUES = Sets.immutableEnumSet(EnumSet.allOf(GuamTar.class));
		
		/**
		 * The identification for the herb item.
		 */
		private final Item herb;
		
		/**
		 * The identification for the produced item.
		 */
		private final Item tar;
		
		/**
		 * The identification for the level requirement.
		 */
		private final int requirement;
		
		/**
		 * The identification for the experience gained.
		 */
		private final double experience;
		
		/**
		 * Constructs a new {@link TarCreation} enumerator.
		 * @param tar         {@link #tar}.
		 * @param herb        {@link #herb}.
		 * @param requirement {@link #requirement}.
		 * @param experience  {@link #experience}.
		 */
		private GuamTar(int herb, int tar, int requirement, double experience) {
			this.herb = new Item(herb);
			this.tar = new Item(tar);
			this.requirement = requirement;
			this.experience = experience;
		}
		
		@Override
		public final String toString() {
			return name().toLowerCase().replaceAll("_", " ");
		}
		
		/**
		 * Gets the definition for this guam tar.
		 * @param identifier the identifier to check for.
		 * @return an optional holding the {@link GuamTar} value found,
		 * {@link Optional#empty} otherwise.
		 */
		public static Optional<GuamTar> getDefinition(int identifier) {
			return VALUES.stream().filter(def -> def.herb.getId() == identifier).findAny();
		}
	}
}
