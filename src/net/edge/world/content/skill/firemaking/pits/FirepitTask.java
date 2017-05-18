package net.edge.world.content.skill.firemaking.pits;

import net.edge.task.Task;
import net.edge.world.GameConstants;
import net.edge.world.World;
import net.edge.world.node.object.ObjectNode;

import java.util.Optional;

/**
 * Represents the task ran when a fire pit is active.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class FirepitTask extends Task {
	
	/**
	 * The fire pit object this task is dependent of.
	 */
	private final FirepitObject object;
	
	/**
	 * Constructs a new {@link FirepitTask}.
	 * @param object {@link #object}.
	 */
	public FirepitTask(FirepitObject object) {
		super(FirepitManager.EVENT_TIME_IN_TICKS);
		this.object = object;
	}
	
	@Override
	protected void onSubmit() {
		object.setActive(Optional.of(this));
	}
	
	@Override
	protected void execute() {
		this.cancel();
		object.setActive(Optional.empty());
		World.getRegions().getRegion(object.getPosition()).unregister(object);
		World.getRegions().getRegion(object.getPosition()).register(new ObjectNode(FirepitData.PHASE_ONE.objectId, object.getPosition(), object.getDirection()));
	}
	
	@Override
	protected void onCancel() {
		World.message("@red@The double blood money event has ended due to the fire pit being distinguised!", true);
		GameConstants.DOUBLE_BLOOD_MONEY_EVENT = false;
	}
}
