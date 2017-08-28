package net.edge.world.entity.actor.move;

import net.edge.task.Task;
import net.edge.task.TaskListener;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;

import java.util.Objects;
import java.util.Optional;

/**
 * The container class that holds the movement queue listener. The listener
 * allows for various actions to be appended to the end of the movement queue,
 * this is useful for things such as "walking to actions".
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class MovementQueueListener {

	/**
	 * The character this listener is dedicated to.
	 */
	private final Actor character;

	/**
	 * The listener being used to execute tasks.
	 */
	private Optional<MovementQueueListenerTask> listener = Optional.empty();

	/**
	 * Creates a new {@link MovementQueueListener}.
	 *
	 * @param character the character this listener is dedicated to.
	 */
	public MovementQueueListener(Actor character) {
		this.character = character;
	}

	/**
	 * Resets this {@link TaskListener} so it may listen for the walking queue
	 * to finish. Once the walking queue is finished the listener will run the
	 * logic within {@code task}.
	 * <p>
	 * <p>
	 * Please note that appended tasks are not guaranteed to be ran! If a new
	 * task is being appended while the listener is already waiting to run
	 * another task, the existing listener is stopped, the old task discarded,
	 * and a new listener is started to run the new task.
	 *
	 * @param task the task that will be ran once the walking queue is finished.
	 */
	public void append(Runnable task) {
		listener.ifPresent(Task::cancel);
		listener = Optional.of(new MovementQueueListenerTask(character, task));
		character.setFollowing(false);
		World.get().submit(listener.get());
	}

	/**
	 * The action listener implementation that allows for a task to be appended
	 * to the end of the movement queue.
	 *
	 * @author lare96 <http://github.com/lare96>
	 */
	private static final class MovementQueueListenerTask extends TaskListener {

		/**
		 * The character that the queued task will be ran for.
		 */
		private final Actor character;

		/**
		 * The queued task that will be executed by this listener.
		 */
		private final Runnable task;

		/**
		 * Creates a new {@link MovementQueueListenerTask}.
		 *
		 * @param character the character that the queued task will be ran for.
		 * @param task      the queued task that will be executed by this listener.
		 */
		public MovementQueueListenerTask(Actor character, Runnable task) {
			this.character = character;
			this.task = Objects.requireNonNull(task);
		}

		@Override
		public boolean canRun() {
			return character.getMovementQueue().isMovementDone();
		}

		@Override
		public void run() {
			if(character.getState() == EntityState.ACTIVE) {
				try {
					task.run();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}