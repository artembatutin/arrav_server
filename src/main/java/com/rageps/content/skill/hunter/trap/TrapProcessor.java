package com.rageps.content.skill.hunter.trap;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.task.Task;

import java.util.Optional;

/**
 * Represents a processor which manages a list of traps.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class TrapProcessor {
	
	/**
	 * The list of traps to manage.
	 */
	private final ObjectList<Trap> traps = new ObjectArrayList<>();
	
	/**
	 * The task running for the list of traps.
	 */
	private Optional<Task> task = Optional.empty();
	
	/**
	 * Constructs a new _empty_ (non-set default initialization) {@link TrapProcessor}.
	 */
	public TrapProcessor() {
	}
	
	/**
	 * @return the list of traps.
	 */
	public ObjectList<Trap> getTraps() {
		return traps;
	}
	
	/**
	 * @return the task running.
	 */
	public Optional<Task> getTask() {
		return task;
	}
	
	/**
	 * @param task the task to set.
	 */
	public void setTask(Optional<Task> task) {
		this.task = task;
	}
	
	/**
	 * @param task the task to set.
	 */
	public void setTask(Task task) {
		setTask(Optional.of(task));
	}
}
