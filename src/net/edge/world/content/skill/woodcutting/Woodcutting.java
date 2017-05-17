package net.edge.world.content.skill.woodcutting;

import net.edge.task.Task;
import net.edge.utils.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.content.skill.SkillData;
import net.edge.world.content.skill.action.TransformableObject;
import net.edge.world.content.skill.action.impl.HarvestingSkillAction;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.model.Direction;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.world.model.node.region.Region;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents the procession for cutting logs.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Woodcutting extends HarvestingSkillAction {
	
	/**
	 * The definition for the hatchet being used.
	 */
	private final Hatchet hatchet;
	
	/**
	 * The definition for the tree being cut.
	 */
	private final Tree tree;
	
	/**
	 * The object we're interfering with.
	 */
	private final ObjectNode object;
	
	/**
	 * The object's name.
	 */
	private final String objectName;
	
	/**
	 * Constructs a new {@link Woodcutting} skill.
	 * @param player {@link #player}.
	 * @param object the object the {@code player} is interacting with.
	 */
	public Woodcutting(Player player, Tree tree, ObjectNode object) {
		super(player, Optional.of(object.getPosition()));
		
		this.object = object;
		this.objectName = object.getDefinition().getName().toLowerCase();
		this.tree = tree;
		this.hatchet = Hatchet.getDefinition(player).orElse(null);
	}
	
	/**
	 * Starts the skill action for the woodcutting skill.
	 * @param player the player we are starting the action for.
	 * @param object the object that we're getting the definition from.
	 * @return <true> if the skill action started, <false> otherwise.
	 */
	public static boolean produce(Player player, ObjectNode object) {
		Optional<Tree> tree = Tree.getDefinition(object.getId());
		
		if(!tree.isPresent()) {
			return false;
		}
		
		Woodcutting woodcutting = new Woodcutting(player, tree.get(), object);
		woodcutting.start();
		return true;
	}
	
	@Override
	public double successFactor() {
		return tree.getSuccess() * hatchet.getSpeed();
	}
	
	@Override
	public Optional<Item[]> removeItems() {
		return Optional.empty();
	}
	
	@Override
	public Item[] harvestItems() {
		if(tree.getItem().getId() != -1)
			return new Item[]{tree.getItem()};
		else
			return new Item[]{};
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean init() {
		if(!checkWoodcutting()) {
			return false;
		}
		if(object.getProducingCount() <= 0) {
			object.setProducingCount(tree.getLogCount());
		}
		getPlayer().message("You begin to cut the " + objectName + "...");
		getPlayer().animation(hatchet.getAnimation());
		return true;
	}
	
	@Override
	public void onSequence(Task t) {
		if(object.isDisabled()) {
			this.onStop();
			t.cancel();
		}
	}
	
	@Override
	public void onHarvest(Task t, Item[] items, boolean success) {
		if(!tree.isObstacle() && success) {
			BirdNest.drop(getPlayer());
			object.setProducingCount(RandomUtils.inclusive(1, object.getProducingCount()));
		}
		
		if(tree.isObstacle() && success && !object.isDisabled()) {
			Region reg = World.getRegions().getRegion(object.getPosition());
			reg.unregister(object);
			object.setDisabled(true);
			World.getTaskManager().submit(new Task(2, false) {
				@Override
				public void execute() {
					World.getRegions().getRegion(object.getPosition()).register(object);
					object.setDisabled(false);
				}
			});
			if(tree.equals(Tree.VINES)) {//Vines
				Position delta = Position.delta(object.getPosition(), player.getPosition());
				Direction dir = Direction.fromDeltas(delta);
				player.getMovementQueue().walk(-dir.getX() * 2, -dir.getY() * 2);
			}
			this.onStop();
			t.cancel();
		}
		if(object.getProducingCount() <= 0 && !tree.isObstacle() && !object.isDisabled()) {
			Optional<TransformableObject> filter = Arrays.stream(tree.getObject()).filter(p -> p.getObjectId() == object.getId()).findFirst();
			
			if(filter.isPresent()) {
				ObjectNode stump = new ObjectNode(filter.get().getTransformable(), position.get(), object.getDirection(), object.getObjectType());
				Region reg = World.getRegions().getRegion(object.getPosition());
				reg.unregister(object);
				object.setDisabled(true);
				reg.register(stump, tree.getRespawnTime(), n -> {
					reg.unregister(stump);
					reg.register(object);
					object.setDisabled(false);
				});
			}
		}
	}
	
	@Override
	public boolean canExecute() {
		return !object.isDisabled() && checkWoodcutting();
	}
	
	@Override
	public void onStop() {
		getPlayer().animation(null);
	}
	
	@Override
	public double experience() {
		return fullLumberJack(getPlayer()) ? (tree.getExperience() * 1.05) : tree.getExperience();
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(hatchet.getAnimation());
	}
	
	@Override
	public SkillData skill() {
		return SkillData.WOODCUTTING;
	}
	
	private boolean checkWoodcutting() {
		if(tree == null) {
			return false;
		}
		if(Hatchet.getDefinition(player).orElse(null) == null) {
			getPlayer().message("You don't have a hatchet.");
			return false;
		}
		if(!getPlayer().getSkills()[skill().getId()].reqLevel(tree.getRequirement())) {
			getPlayer().message("You need a level of " + tree.getRequirement() + " to cut this " + objectName + "!");
			return false;
		}
		if(!getPlayer().getSkills()[skill().getId()].reqLevel(hatchet.getRequirement())) {
			getPlayer().message("You need a level of " + hatchet.getRequirement() + " to use this hatchet!");
			return false;
		}
		if(getPlayer().getInventory().remaining() < 1 && !tree.isObstacle()) {
			getPlayer().message("You do not have any space left in your inventory.");
			return false;
		}
		
		return true;
	}
	
	private static boolean fullLumberJack(Player player) {
		return player.getEquipment() != null && player.getEquipment().containsAll(10933, 10939, 10940, 10941);
	}
	
}
