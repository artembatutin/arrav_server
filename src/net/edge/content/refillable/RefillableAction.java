package net.edge.content.refillable;

import net.edge.action.impl.ItemOnObjectAction;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.impl.ProducingSkillAction;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.object.GameObject;

import java.util.Optional;

public final class RefillableAction extends ProducingSkillAction {
	/**
	 * Represents the refillable
	 */
	private Refillable refillable;

	/**
	 * Represents the game object
	 */
	private GameObject gameObject;

	/**
	 * Represents the refill animation
	 */
	private static final Animation ANIMATION = new Animation(832);

	public static void action() {
		for(Refillable refillable : Refillable.values()) {
			ItemOnObjectAction a = new ItemOnObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, Item item, int container, int slot) {
					RefillableAction refillableAction = new RefillableAction(player, refillable, object);
					refillableAction.start();
					return true;
				}
			};
			a.registerItem(refillable.getNeeded());
		}
	}

	/**
	 * Checks if able to execute action.
	 */
	private boolean check() {
		for(int object : refillable.getObjects()) {
			if(object == gameObject.getId()) {
				if(getPlayer().getInventory().contains(refillable.getNeeded())) {
					return true;
				} else {
					getPlayer().message("You need to use an empty " + new Item(refillable.getNeeded()).getDefinition().getName() + " on a water source.");
					return false;
				}
			}
		}
		return false;
	}

	public RefillableAction(Player player, Refillable refillable, GameObject object) {
		super(player, Optional.of(object.getGlobalPos()));
		this.refillable = refillable;
		this.gameObject = object;
	}

	@Override
	public int delay() {
		return 2;
	}

	@Override
	public boolean instant() {
		return true;
	}

	@Override
	public boolean init() {
		return check();
	}

	@Override
	public boolean canExecute() {
		return check();
	}

	@Override
	public double experience() {
		return 0;
	}

	@Override
	public SkillData skill() {
		return SkillData.HERBLORE;
	}

	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(refillable.getNeeded())});
	}

	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{new Item(refillable.getProduced())});
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			getPlayer().animation(ANIMATION);
		}
	}

}