package net.edge.content.skill.hunter.butterfly;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.edge.action.impl.NpcAction;
import net.edge.task.LinkedTaskSequence;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.mob.impl.DefaultMob;
import net.edge.world.node.item.container.impl.Equipment;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.Skills;
import net.edge.content.skill.action.impl.ProducingSkillAction;
import net.edge.locale.Position;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.World;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents and holds functionality for catching butterflies.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ButterflyCatching extends ProducingSkillAction {
	
	/**
	 * The animation for catching a butterfly with a org.
	 */
	private static final Animation NETTING_ANIMATION = new Animation(6606);
	
	/**
	 * The animation for catching a butterfly bare handed.
	 */
	private static final Animation BAREHAND_ANIMATION = new Animation(12832);
	
	/**
	 * The data this skill action is dependent of.
	 */
	private final ButterflyData data;
	
	/**
	 * The mob that represents the butterfly
	 */
	private final Mob mob;
	
	/**
	 * Determines if this skill action is bare handed or not.
	 */
	private final boolean barehanded;
	
	/**
	 * Constructs a new {@link ButterflyCatching}.
	 * @param player     {@link #getPlayer()}.
	 * @param data       {@link #data}.
	 * @param mob        {@link #mob}.
	 * @param barehanded {@link #barehanded}.
	 */
	public ButterflyCatching(Player player, ButterflyData data, Mob mob, boolean barehanded) {
		super(player, Optional.of(mob.getPosition()));
		this.data = data;
		this.mob = mob;
		this.barehanded = barehanded;
	}
	
	public static void event() {
		for(ButterflyData data : ButterflyData.values()) {
			NpcAction e = new NpcAction() {
				@Override
				public boolean click(Player player, Mob npc, int click) {
					boolean barehanded = !player.getEquipment().contains(10010);
					if(barehanded && (player.getEquipment().get(Equipment.WEAPON_SLOT) != null || player.getEquipment().get(Equipment.SHIELD_SLOT) != null)) {
						player.message("Your hands need to be free to catch a butterfly.");
						return false;
					}
					ButterflyCatching hunter = new ButterflyCatching(player, data, npc, barehanded);
					hunter.start();
					return true;
				}
			};
			e.registerFirst(data.npc);
		}
	}
	
	public static boolean releaseButterfly(Player player, Item item) {
		ButterflyData data = ButterflyData.getDefinitionItem(item.getId());
		
		if(data == null) {
			return false;
		}
		
		player.getInventory().remove(data.reward);
		player.getInventory().add(new Item(10012));
		player.graphic(data.releasedGraphic);
		
		//FIXME for some reason the code below returns into an index out of bounds exception
		Position position = RandomUtils.random(World.getTraversalMap().getNearbyTraversableTiles(player.getPosition(), 3));
		Mob mob = new DefaultMob(data.npc, position);
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(2, () -> World.get().getNpcs().add(mob));
		seq.start();
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			player.getMovementQueue().smartWalk(mob.getPosition());
			mob.move(new Position(0, 0));
			mob.appendDeath();
			player.animation(barehanded ? BAREHAND_ANIMATION : NETTING_ANIMATION);
			Skills.experience(player, barehanded ? data.barehandRequirement.getExperience() : data.regularRequirement.getExperience(), skill().getId());
			
			if(barehanded) {
				player.message("You manage to catch the bufferly and release it back into the wild.");
				Skills.experience(player, data.agilityRequirement.getExperience(), Skills.AGILITY);
				Position position = RandomUtils.random(World.getTraversalMap().getNearbyTraversableTiles(player.getPosition(), 3));
				Mob mob = new DefaultMob(data.npc, position);
				LinkedTaskSequence seq = new LinkedTaskSequence();
				seq.connect(2, () -> player.graphic(data.barehandGraphic));
				seq.connect(4, () -> World.get().getNpcs().add(mob));
				seq.start();
			}
			
			t.cancel();
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return barehanded ? Optional.empty() : Optional.of(new Item[]{new Item(10012)});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return barehanded ? Optional.empty() : Optional.of(new Item[]{data.reward});
	}
	
	@Override
	public int delay() {
		return barehanded ? 0 : 1;
	}
	
	@Override
	public boolean instant() {
		return barehanded;
	}
	
	@Override
	public boolean init() {
		return checkHunter();
	}
	
	@Override
	public boolean canExecute() {
		return checkHunter();
	}
	
	@Override
	public double experience() {
		return 0;
	}
	
	@Override
	public SkillData skill() {
		return SkillData.HUNTER;
	}
	
	private boolean checkHunter() {
		if(barehanded) {
			if(!player.getSkills()[skill().getId()].reqLevel(data.barehandRequirement.getRequirement())) {
				player.message("You need a hunter level of " + data.barehandRequirement.getRequirement() + " to catch a " + mob
						.getDefinition().getName() + " barehanded.");
				return false;
			}
			
			if(!player.getSkills()[Skills.AGILITY].reqLevel(data.agilityRequirement.getRequirement())) {
				player.message("You need an agility level of " + data.agilityRequirement.getRequirement() + " to catch a " + mob
						.getDefinition().getName() + " barehanded.");
				return false;
			}
		} else {
			if(!player.getSkills()[skill().getId()].reqLevel(data.regularRequirement.getRequirement())) {
				player.message("You need an agility level of " + data.regularRequirement.getRequirement() + " to catch a " + mob
						.getDefinition().getName() + " with a butterfly org.");
				return false;
			}
			
			if(!player.getInventory().contains(10012)) {
				player.message("You need a butterfly jar to catch butterflies.");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants used to define the data
	 * of various butterflies.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum ButterflyData {
		RUBY_HARVEST(5085, 10020, 913, 917, new ButterflyPolicy(15, 24), new ButterflyPolicy(80, 300), new ButterflyPolicy(75, 50)),
		SAPPHIRE_GLACIALIS(5084, 10018, 912, 916, new ButterflyPolicy(25, 34), new ButterflyPolicy(85, 400), new ButterflyPolicy(80, 70)),
		SNOWY_KNIGHT(5083, 10016, 911, 915, new ButterflyPolicy(35, 44), new ButterflyPolicy(90, 500), new ButterflyPolicy(85, 100)),
		BLACK_WARLOCK(5082, 10014, 910, 914, new ButterflyPolicy(45, 54), new ButterflyPolicy(95, 650), new ButterflyPolicy(90, 125));
		
		/**
		 * Caches our mob values.
		 */
		private static final ImmutableSet<ButterflyData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(ButterflyData.class));
		
		/**
		 * Represents the mob id.
		 */
		private final int npc;
		
		/**
		 * Represents the reward item.
		 */
		private final Item reward;
		
		/**
		 * The graphic played when releasing the butterfly.
		 */
		private final Graphic barehandGraphic;
		
		/**
		 * The graphic played when releasing the graphic
		 */
		private final Graphic releasedGraphic;
		
		/**
		 * The regular requirement for catching with a butterfly org.
		 */
		private final ButterflyPolicy regularRequirement;
		
		/**
		 * The barehand requirement for catching with bare hands.
		 */
		private final ButterflyPolicy barehandRequirement;
		
		/**
		 * The agility requirement to catch bare handed.
		 */
		private final ButterflyPolicy agilityRequirement;
		
		/**
		 * Constructs a new {@link ButterflyData}.
		 * @param npc                 {@link #npc}.
		 * @param reward              {@link #reward}.
		 * @param barehandGraphic     {@link #barehandGraphic}.
		 * @param releaseGraphic      {@link #releasedGraphic}.
		 * @param regularRequirement  {@link #regularRequirement}.
		 * @param barehandRequirement {@link #barehandRequirement}.
		 * @param agilityRequirement  {@link #agilityRequirement}.
		 */
		ButterflyData(int npc, int reward, int barehandGraphic, int releaseGraphic, ButterflyPolicy regularRequirement, ButterflyPolicy barehandRequirement, ButterflyPolicy agilityRequirement) {
			this.npc = npc;
			this.reward = new Item(reward);
			this.barehandGraphic = new Graphic(barehandGraphic, 100);
			this.releasedGraphic = new Graphic(releaseGraphic, 100);
			this.regularRequirement = regularRequirement;
			this.barehandRequirement = barehandRequirement;
			this.agilityRequirement = agilityRequirement;
		}
		
		public static final ButterflyData getDefinitionId(int id) {
			return BUTTERFLY_DATA.get(id);
		}
		
		public static ButterflyData getDefinitionItem(int id) {
			return VALUES.stream().filter(butterfly -> butterfly.reward.getId() == id).findAny().orElse(null);
		}
	}
	
	private static final Int2ObjectArrayMap<ButterflyData> BUTTERFLY_DATA = new Int2ObjectArrayMap<>(ImmutableMap.copyOf(Stream.of(ButterflyData.values()).collect(Collectors.toMap(t -> t.npc, Function.identity()))));
	
}