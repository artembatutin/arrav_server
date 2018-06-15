package net.arrav.content.skill.woodcutting;

import net.arrav.action.impl.ObjectAction;
import net.arrav.content.skill.SkillData;
import net.arrav.content.skill.action.TransformableObject;
import net.arrav.content.skill.action.impl.HarvestingSkillAction;
import net.arrav.content.skill.farming.patch.Patch;
import net.arrav.task.Task;
import net.arrav.world.Animation;
import net.arrav.world.Direction;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.locale.Position;
import net.arrav.world.entity.object.DynamicObject;
import net.arrav.world.entity.object.GameObject;

import java.util.Optional;

import static net.arrav.content.achievements.Achievement.LEAVES_AND_STUMPS;

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
	 * Tree patch if cutting farming tree.
	 */
	private final Patch patch;
	
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
	 */
	public Woodcutting(Player player, Tree tree, GameObject object, Patch patch) {
		super(player, Optional.of(object.getPosition()));
		this.objectName = object.getDefinition().getName().toLowerCase();
		this.tree = tree;
		this.hatchet = Hatchet.getDefinition(player).orElse(null);
		this.object = object.toDynamic();
		this.patch = patch;
	}
	
	public Woodcutting(Player player, Tree tree, GameObject object) {
		this(player, tree, object, null);
	}
	
	public static void action() {
		for(Tree tree : Tree.values()) {
			ObjectAction cut = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					Woodcutting woodcutting = new Woodcutting(player, tree, object);
					woodcutting.start();
					return true;
				}
			};
			for(TransformableObject o : tree.getObject()) {
				cut.registerFirst(o.getObjectId());
			}
		}
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
		if(patch == null && object.getElements() <= 0) {
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
			if(patch != null) {
				patch.setHarvestedItem(patch.getHarvestedItem().createAndIncrement(1));
			} else {
				object.setElements(object.getElements() - 1);
			}
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
				Position delta = Position.delta(object.getPosition(), player.getPosition());
				Direction dir = Direction.fromDeltas(delta);
				player.getMovementQueue().walk(-dir.getX() * 2, -dir.getY() * 2);
			}
			this.onStop();
			t.cancel();
		}
		if(object.getElements() <= 0 && !tree.isObstacle() && object.isReg()) {
			TransformableObject obj = null;
			for(TransformableObject ob : tree.getObject()) {
				if(ob.getObjectId() == object.getId()) {
					obj = ob;
					break;
				}
			}
			if(obj != null) {
				LEAVES_AND_STUMPS.inc(player);
				int id = object.getId();//saved tree id.
				object.setId(obj.getTransformable());
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
