package net.edge.content.skill.action.impl;

import net.edge.content.skill.Skills;
import net.edge.content.skill.action.SkillAction;
import net.edge.task.Task;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.ItemDefinition;
import net.edge.world.locale.Position;

import java.util.Optional;

/**
 * The skill action that represents an action where one item is removed from an
 * inventory and lost forever. This type of skill action is very basic and only
 * requires that a player have the item to destruct in their inventory.
 * <p>
 * <p>
 * The skills that may use this type skill action include, but are not limited
 * to {@code PRAYER}.
 *
 * @author lare96 <http://github.com/lare96>
 * @see SkillAction
 * @see HarvestingSkillAction
 * @see ProducingSkillAction
 */
public abstract class DestructionSkillAction extends SkillAction {

	/**
	 * Creates a new {@link DestructionSkillAction}.
	 *
	 * @param player   the player this skill action is for.
	 * @param position the position the player should face.
	 */
	public DestructionSkillAction(Player player, Optional<Position> position) {
		super(player, position);
	}

	@Override
	public Optional<ActivityManager.ActivityType[]> onDisable() {
		return Optional.of(new ActivityManager.ActivityType[]{ActivityManager.ActivityType.WALKING, ActivityManager.ActivityType.TELEPORT});
	}

	@Override
	public boolean canRun(Task t) {
		String name = ItemDefinition.DEFINITIONS[destructItem().getId()].getName();
		if(!getPlayer().getInventory().contains(destructItem().getId())) {
			getPlayer().message("You do not have any " + name + " in your inventory.");
			return false;
		}
		return true;
	}

	@Override
	public final void execute(Task t) {
		if(getPlayer().getInventory().remove(destructItem()) >= 0) {
			onDestruct(t, true);
			Skills.experience(getPlayer(), experience(), skill().getId());
			return;
		}
		onDestruct(t, false);
		t.cancel();
	}

	/**
	 * The method executed upon destruction of the item.
	 *
	 * @param t       the task executing this method.
	 * @param success determines if the destruction was successful or not.
	 */
	public void onDestruct(Task t, boolean success) {

	}

	/**
	 * The item that will be removed upon destruction.
	 *
	 * @return the item that will be removed.
	 */
	public abstract Item destructItem();

	@Override
	public boolean isPrioritized() {
		return false;
	}
}
