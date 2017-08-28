package net.edge.content.skill.action.impl;

import com.google.common.base.Preconditions;
import net.edge.content.skill.Skills;
import net.edge.content.skill.action.SkillAction;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.Item;
import net.edge.world.locale.Position;

import java.util.Optional;

/**
 * The skill action that represents an action where items are periodically added
 * to and removed from an inventory based on a success factor. This type of
 * skill action is more complicated and requires that a player have the items to
 * be removed and the space for the items to harvest.
 * <p>
 * <p>
 * The skills that may use this type skill action include, but are not limited
 * to {@code FISHING} and {@code WOODCUTTING}.
 *
 * @author lare96 <http://github.com/lare96>
 * @see SkillAction
 * @see DestructionSkillAction
 * @see ProducingSkillAction
 */
public abstract class HarvestingSkillAction extends SkillAction {

	/**
	 * The factor boost that determines the success rate for harvesting based on
	 * skill level. The higher the number the less frequently harvest will be
	 * obtained. A value higher than {@code 99} or lower than {@code 0} will
	 * throw an {@link IllegalStateException}.
	 */
	private static final int SUCCESS_FACTOR = 10;

	/**
	 * Creates a new {@link HarvestingSkillAction}.
	 *
	 * @param player   the player this skill action is for.
	 * @param position the position the player should face.
	 */
	public HarvestingSkillAction(Player player, Optional<Position> position) {
		super(player, position);
	}

	@Override
	public final boolean canRun(Task t) {
		Optional<Item[]> removeItems = removeItems();
		Item[] harvestItems = harvestItems();
		if(removeItems.isPresent() && !getPlayer().getInventory().containsAll(removeItems.get())) {
			getPlayer().message("You do not have the required items to perform this!");
			return false;
		}
		if(!getPlayer().getInventory().hasCapacityFor(harvestItems)) {
			onHarvest(t, harvestItems, false);
			getPlayer().message("You do not have any space left in your inventory!");
			return false;
		}
		return true;
	}

	@Override
	public final void execute(Task t) {
		Preconditions.checkState(SUCCESS_FACTOR >= 0 && SUCCESS_FACTOR <= 99, "Invalid success factor for harvesting!");
		int factor = (getPlayer().getSkills()[skill().getId()].getLevel() / SUCCESS_FACTOR);
		double boost = (factor * 0.01);
		if(RandomUtils.success((successFactor() + boost))) {
			Optional<Item[]> removeItems = removeItems();
			Item[] harvestItems = harvestItems();

			for(Item item : harvestItems) {
				if(item == null)
					continue;
				if(item.getDefinition() == null)
					continue;
				getPlayer().getInventory().add(item);
				if(harvestMessage() && item.getDefinition() != null && item.getDefinition().getName() != null) {
					getPlayer().message("You get some " + item.getDefinition().getName() + ".");
				}
			}
			Skills.experience(getPlayer(), experience(), skill().getId());
			removeItems.ifPresent(getPlayer().getInventory()::removeAll);
			onHarvest(t, harvestItems, true);
		}
	}

	@Override
	public int delay() {
		return 1;
	}

	@Override
	public Optional<ActivityManager.ActivityType[]> onDisable() {
		return Optional.of(new ActivityManager.ActivityType[]{ActivityManager.ActivityType.WALKING, ActivityManager.ActivityType.TELEPORT});
	}

	/**
	 * The method executed upon harvest of the items.
	 *
	 * @param t       the task executing this method.
	 * @param items   the items being harvested.
	 * @param success determines if the harvest was successful or not.
	 */
	public void onHarvest(Task t, Item[] items, boolean success) {

	}

	/**
	 * The success factor for the harvest. The higher the number means the more
	 * frequently harvest will be obtained.
	 *
	 * @return the success factor.
	 */
	public abstract double successFactor();

	/**
	 * The items to be removed upon a successful harvest.
	 *
	 * @return the items to be removed.
	 */
	public abstract Optional<Item[]> removeItems();

	/**
	 * The items to be harvested upon a successful harvest.
	 *
	 * @return the items to be harvested.
	 */
	public abstract Item[] harvestItems();

	/**
	 * Determines if a message should be sent upon successfully harvesting.
	 *
	 * @return {@code true} if a message should be sent, {@code false} otherwise.
	 */
	public boolean harvestMessage() {
		return true;
	}

	@Override
	public boolean isPrioritized() {
		return false;
	}
}
