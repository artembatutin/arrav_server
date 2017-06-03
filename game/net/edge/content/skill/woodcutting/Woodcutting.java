package net.edge.content.skill.woodcutting;

import net.edge.task.Task;
import net.edge.World;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.TransformableObject;
import net.edge.content.skill.action.impl.HarvestingSkillAction;
import net.edge.locale.Position;
import net.edge.world.Animation;
import net.edge.world.Direction;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectNode;

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
	private final DynamicObject object;
	
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
		super(player, Optional.of(object.getGlobalPos()));
		this.objectName = object.getDefinition().getName().toLowerCase();
		this.tree = tree;
		this.hatchet = Hatchet.getDefinition(player).orElse(null);
		this.object = object.toDynamic();
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
		if(object.getElements() <= 0) {
			object.setElements(tree.getLogCount());
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
			object.setElements(object.getElements() - 1);
		}
		if(tree.isObstacle() && success && object.isReg()) {
			object.remove();
			object.setDisabled(true);
			World.get().getTask().submit(new Task(2, false) {
				@Override
				public void execute() {
					object.publish();
					object.setDisabled(false);
				}
			});
			if(tree.equals(Tree.VINES)) {//Vines
				Position delta = Position.delta(object.getGlobalPos(), player.getPosition());
				Direction dir = Direction.fromDeltas(delta);
				player.getMovementQueue().walk(-dir.getX() * 2, -dir.getY() * 2);
			}
			this.onStop();
			t.cancel();
		}
		if(object.getElements() <= 0 && !tree.isObstacle() && object.isReg()) {
			Optional<TransformableObject> filter = Arrays.stream(tree.getObject()).filter(p -> p.getObjectId() == object.getId()).findFirst();
			
			if(filter.isPresent()) {
				int id = object.getId();//saved tree id.
				object.setId(filter.get().getTransformable());
				object.setDisabled(true);
				object.publish(tree.getRespawnTime(), n -> {
					object.setId(id);
					object.setDisabled(false);
					object.publish();
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
