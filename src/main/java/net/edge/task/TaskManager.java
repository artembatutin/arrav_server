package net.edge.task;

import java.util.*;

/**
 * Handles the processing and execution of {@link Task}s. Functions contained within this class should only be invoked on the
 * {@link GameService} thread to ensure thread safety.
 * @author lare96 <http://github.org/lare96>
 */
public final class TaskManager {
	
	/**
	 * A {@link List} of tasks that have been submitted and are awaiting execution.
	 */
	private final List<Task> awaitingExecution = new LinkedList<>();
	
	/**
	 * A {@link Queue} of tasks that are ready to be executed.
	 */
	private final Queue<Task> executionQueue = new ArrayDeque<>();
	
	/**
	 * Runs an iteration of the {@link Task} processing logic. All {@link Exception}s thrown by {@code Task}s.
	 */
	public void sequence() {
		Iterator<Task> $it = awaitingExecution.iterator();
		while($it.hasNext()) {
			Task it = $it.next();
			
			if(!it.isRunning()) {
				$it.remove();
				continue;
			}
			it.onSequence();
			if(it.needsExecute() && it.canExecute()) {
				executionQueue.add(it);
			}
		}
		
		for(; ; ) {
			Task it = executionQueue.poll();
			if(it == null) {
				break;
			}
			try {
				it.execute();
			} catch(Exception e) {
				it.onException(e);
			}
		}
	}
	
	/**
	 * Schedules {@code t} to run in the underlying {@code TaskManager}.
	 * @param t The {@link Task} to schedule.
	 */
	public void submit(Task t) {
		if(!t.canExecute()) {
			return;
		}
		t.onSubmit();
		if(t.isInstant()) {
			try {
				t.execute();
			} catch(Exception e) {
				e.printStackTrace();
				t.onException(e);
			}
		}
		awaitingExecution.add(t);
	}
	
	/**
	 * Iterates through all active {@link Task}s and cancels all that have {@code attachment} as their attachment.
	 */
	public void cancel(Object attachment) {
		awaitingExecution.stream().filter(it -> Objects.equals(attachment, it.getAttachment().orElse(null))).forEach(Task::cancel);
	}
}