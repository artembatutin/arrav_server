package com.rageps.world.entity.region;

import com.rageps.task.Task;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.item.GroundItem;

import java.util.Iterator;
import java.util.Optional;

/**
 * A {@link Region} item sequencing task.
 * @author Artem Batutin
 */
public class RegionItemTask extends Task {
	
	/**
	 * The amount of ticks to execute the sequence listener.
	 */
	private static final int SEQUENCE_TICKS = 100;
	
	/**
	 * The region the task is running for.
	 */
	private final Region region;
	
	/**
	 * Creating a new {@link RegionItemTask}.
	 * @param region region assigned.
	 */
	public RegionItemTask(Region region) {
		super(10, false);
		this.region = region;
	}
	
	@Override
	protected void execute() {
		//sequencing active item nodes.
		if(region.getItems().isEmpty()) {
			//no ground items so no sequencing needed.
			cancel();
		} else {
			Iterator<GroundItem> it = region.getItems().iterator();
			while(it.hasNext()) {
				GroundItem item = it.next();
				if(item.getCounter().incrementAndGet(10) >= SEQUENCE_TICKS) {
					item.onSequence();
					item.getCounter().set(0);
				}
				if(item.getState() != EntityState.ACTIVE) {
					item.dispose();
					it.remove();
				}
			}
		}
	}
	
	@Override
	protected void onCancel() {
		region.setItemTask(Optional.empty());
	}
}
