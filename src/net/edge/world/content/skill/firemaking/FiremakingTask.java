package net.edge.world.content.skill.firemaking;

import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNodeStatic;
import net.edge.world.node.item.ItemPolicy;
import net.edge.world.node.object.ObjectDirection;
import net.edge.world.node.object.ObjectNode;
import net.edge.world.node.region.Region;

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
		object = new ObjectNode(firemaking.getFireLighter().getObjectId(), firemaking.getPlayer().getPosition(), ObjectDirection.SOUTH);
		World.getRegions().getRegion(object.getPosition()).register(object);
	}
	
	@Override
	public void execute() {
		Region region = object.getRegion();
		if(region.unregister(object)) {
			region.register(new ItemNodeStatic(new Item(592), object.getPosition(), ItemPolicy.TIMEOUT));
		}
		cancel();
	}
	
}
