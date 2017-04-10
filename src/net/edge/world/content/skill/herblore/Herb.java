package net.edge.world.content.skill.herblore;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.skill.SkillData;
import net.edge.world.content.skill.action.impl.ProducingSkillAction;
import net.edge.task.Task;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Represents procession for identifying of grimy herbs.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Herb extends ProducingSkillAction {
	
	/**
	 * The {@link GrimyHerb} holding all the data required for processing
	 * the identification for herbs.
	 */
	private final GrimyHerb definition;
	
	/**
	 * Constructs a new {@link Herb}.
	 * @param player {@link #getPlayer()}.
	 * @param item   the item that was clicked.
	 */
	public Herb(Player player, Item item) {
		super(player, Optional.of(player.getPosition()));
		
		definition = GrimyHerb.getDefinition(item.getId()).orElse(null);
	}
	
	/**
	 * Identify's the current herb.
	 * @param player {@link #getPlayer()}.
	 * @param item   the current item that was clicked.
	 * @return <true> if the herb was identified, <false> otherwise.
	 */
	public static boolean identify(Player player, Item item) {
		Herb herb = new Herb(player, item);
		
		if(herb.definition == null) {
			return false;
		}
		
		herb.start();
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		t.cancel();
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{definition.grimy});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{definition.clean});
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
	
	public boolean canProduce() {
		if(!getPlayer().getSkills()[skill().getId()].reqLevel(definition.level)) {
			getPlayer().message("You need a herblore level of " + definition.level + " to clean this herb.");
			return false;
		}
		return true;
	}
	
	/**
	 * The data required for processing the identifying of grimy herbs.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum GrimyHerb {
		GUAM(1, 199, 249, 2.5),
		ROGUESPURSE(3, 1533, 1534, 2.5),
		SNAKEWEED(3, 1525, 1526, 2.5),
		MARRENTILL(5, 201, 251, 3.8),
		TARROMIN(11, 203, 253, 5),
		HARRALANDER(20, 205, 255, 6.3),
		RANNAR(25, 207, 257, 7.5),
		TOADFLAX(30, 3049, 2998, 8),
		IRIT(40, 209, 259, 8.8),
		AVANTOE(48, 211, 261, 10),
		KWUARM(54, 213, 263, 11.3),
		SNAPDRAGON(59, 3051, 3000, 11.8),
		CADANTINE(65, 215, 265, 12.5),
		LANTADYME(67, 2485, 2481, 13.1),
		DWARFWEED(70, 217, 267, 13.8),
		TORSTOL(75, 219, 269, 15);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<GrimyHerb> VALUES = Sets.immutableEnumSet(EnumSet.allOf(GrimyHerb.class));
		
		/**
		 * The required level for creating this potion.
		 */
		private final int level;
		
		/**
		 * The identification for this grimy item.
		 */
		private final Item grimy;
		
		/**
		 * The identification for this clean grimy item.
		 */
		private final Item clean;
		
		/**
		 * The identifier which identifies the amount of experience given for this potion.
		 */
		private final double experience;
		
		/**
		 * Constructs a new {@link GrimyHerb} enum.
		 * @param level      {@link #level}.
		 * @param grimy      {@link #grimy}.
		 * @param clean      {@link #clean}.
		 * @param level      {@link #level}.
		 * @param experience {@link #experience}.
		 */
		GrimyHerb(int level, int grimy, int clean, double experience) {
			this.level = level;
			this.grimy = new Item(grimy);
			this.clean = new Item(clean);
			this.experience = experience;
		}
		
		/**
		 * Gets the definition for this grimy herb.
		 * @param identifier the identifier to check for.
		 * @return an optional holding the {@link GrimyHerb} value found,
		 * {@link Optional#empty} otherwise.
		 */
		public static Optional<GrimyHerb> getDefinition(int identifier) {
			return VALUES.stream().filter(herb -> herb.grimy.getId() == identifier).findAny();
		}
	}
}
