package net.edge.content.skill.action.impl;

import net.edge.task.Task;
import net.edge.util.TextUtils;
import net.edge.content.container.ItemContainer;
import net.edge.content.skill.Skills;
import net.edge.content.skill.action.SkillAction;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * The skill action that represents an action where one item in an inventory is
 * replaced with a new one. This type of skill action is somewhat basic and
 * requires that a player have the item to be removed.
 * <p>
 * <p>
 * The skills that may use this type skill action include, but are not limited
 * to {@code COOKING}.
 * @author lare96 <http://github.com/lare96>
 * @see SkillAction
 * @see DestructionSkillAction
 * @see HarvestingSkillAction
 */
public abstract class ProducingSkillAction extends SkillAction {
	
	/**
	 * Creates a new {@link ProducingSkillAction}.
	 * @param player   the player this skill action is for.
	 * @param position the position the player should face.
	 */
	public ProducingSkillAction(Player player, Optional<Position> position) {
		super(player, position);
	}
	
	@Override
	public final boolean canRun(Task t) {
		Optional<Item[]> removeItem = removeItem();
		getPlayer().getInventory().test();
		//removing items from the test container.
		if(removeItem.isPresent()) {
			//if player missing any items check.
			if(!getPlayer().getInventory().containsAll(removeItem.get())) {
				//loop checking specifics if message not present.
				if(!message().isPresent()) {
					for(Item item : removeItem.get()) {
						if(!getPlayer().getInventory().contains(item)) {
							String anyOrEnough = item.getAmount() == 1 ? "any" : "enough";
							getPlayer().message("You don't have " + anyOrEnough + " " + TextUtils.appendPluralCheck(item.getDefinition().getName()) + ".");
							return false;
						}
					}
				} else {
					player.message(message().get());
				}
				return false;
			}
			//removing items from the test container.
			getPlayer().getInventory().removeAll(removeItem.get());
		}
		
		//Looking if player has empty space for produce items.
		if(produceItem().isPresent() && !getPlayer().getInventory().hasCapacityFor(produceItem().get())) {
			getPlayer().getInventory().fireCapacityExceededEvent();
			return false;
		}
		getPlayer().getInventory().untest();
		//producing action
		onProduce(t, false);
		return true;
	}
	
	@Override
	public final void execute(Task t) {
		Skills.experience(getPlayer(), experience(), skill().getId());
		removeItem().ifPresent(getPlayer().getInventory()::removeAll);
		produceItem().ifPresent(getPlayer().getInventory()::addAll);
		onProduce(t, true);
	}
	
	@Override
	public Optional<ActivityManager.ActivityType[]> onDisable() {
		return Optional.of(new ActivityManager.ActivityType[]{ActivityManager.ActivityType.WALKING, ActivityManager.ActivityType.TELEPORT});
	}
	
	/**
	 * The method executed upon production of an item.
	 * @param t       the task executing this method.
	 * @param success determines if the production was successful or not.
	 */
	public void onProduce(Task t, boolean success) {
		
	}
	
	/**
	 * The item that will be removed upon production.
	 * @return the item that will be removed.
	 */
	public abstract Optional<Item[]> removeItem();
	
	/**
	 * The item that will be added upon production.
	 * @return the item that will be added.
	 */
	public abstract Optional<Item[]> produceItem();
	
	/**
	 * The message that will be sent when the player doesn't
	 * have the items required.
	 * @return the alphabetic value which represents the message.
	 */
	public Optional<String> message() {
		return Optional.empty();
	}
	
	@Override
	public boolean isPrioritized() {
		return false;
	}
	
}
