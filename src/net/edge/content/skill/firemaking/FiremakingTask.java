package net.edge.content.skill.firemaking;

import net.edge.task.Task;
import net.edge.world.entity.item.GroundItemStatic;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.GroundItemPolicy;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectNode;
import net.edge.world.object.ObjectType;

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
	private ObjectNode object;
	
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
		object.getRegion().register(new GroundItemStatic(new Item(592), object.getGlobalPos(), GroundItemPolicy.TIMEOUT));
		object.remove();
		this.cancel();
	}
	
}
