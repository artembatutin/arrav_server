package net.arrav.content.skill.firemaking;

import net.arrav.task.Task;
import net.arrav.world.entity.item.GroundItemPolicy;
import net.arrav.world.entity.item.GroundItemStatic;
import net.arrav.world.entity.item.Item;
import net.arrav.world.object.DynamicObject;
import net.arrav.world.object.GameObject;
import net.arrav.world.object.ObjectDirection;
import net.arrav.world.object.ObjectType;

/**
 * Represents the task for creating and deregistering fires.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
final class FiremakingTask extends Task {
	
	/**
	 * The skill this task is active for.
	 */
	private final Firemaking firemaking;
	
	/**
	 * The object we're registering and deregistering at a later state.
	 */
	private GameObject object;
	
	/**
	 * Constructs a new {@link FiremakingTask}.
	 * @param firemaking the firemaking skill action we're starting this task for.
	 */
	FiremakingTask(Firemaking firemaking) {
		super(firemaking.getLogType().getTimer(), false);
		this.firemaking = firemaking;
	}
	
	@Override
	public void onSubmit() {
		object = new DynamicObject(firemaking.getFireLighter().getObjectId(), firemaking.getPlayer().getPosition(), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, 0);
		object.publish();
	}
	
	@Override
	public void execute() {
		object.getRegion().ifPresent(r -> r.register(new GroundItemStatic(new Item(592), object.getPosition(), GroundItemPolicy.TIMEOUT)));
		object.remove();
		this.cancel();
	}
	
}
