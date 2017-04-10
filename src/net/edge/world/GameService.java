package net.edge.world;

import com.google.common.util.concurrent.*;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An {@link AbstractScheduledService} implementation that performs general game logic processing, provides
 * functionality for executing small asynchronous tasks, and allows for tasks from other threads to be executed on
 * the game logic thread.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author lare96 <http://github.org/lare96>
 */
public final class GameService extends AbstractScheduledService {
	
	/**
	 * The asynchronous logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(GameService.class.getName());
	
	/**
	 * A cached thread pool that manages the execution of short, low priority, asynchronous tasks.
	 */
	private final ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("EdgevilleWorkerThread").build()));
	
	/**
	 * A queue of synchronization tasks.
	 */
	private final Queue<Runnable> syncTasks = new ConcurrentLinkedQueue<>();
	
	@Override
	protected String serviceName() {
		return "EdgevilleGameThread";
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>
	 * This method should <b>never</b> be invoked unless by the underlying {@link AbstractScheduledService}. Illegal
	 * invocation of this method will lead to serious gameplay timing issues as well as other unexplainable and
	 * unpredictable issues related to gameplay.
	 */
	@Override
	protected void runOneIteration() throws Exception {
		try {
			for(; ; ) {
				Runnable t = syncTasks.poll();
				if(t == null) {
					break;
				}
				
				try {
					t.run();
				} catch(Exception e) {
					LOGGER.log(Level.WARNING, "Error on runnable service.", e);
				}
			}
			
			World.sequence();
		} catch(Exception e) {
			LOGGER.log(Level.WARNING, "Error on main game tick.", e);
		}
	}
	
	@Override
	protected Scheduler scheduler() {
		return Scheduler.newFixedRateSchedule(600, 600, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Prints a message that this service has been terminated, and attempts to gracefully exit the application
	 * cleaning up resources and ensuring all players are logged out. If an exception is thrown during shutdown, the
	 * shutdown process is aborted completely and the application is exited.
	 */
	@Override
	protected void shutDown() {
		try {
			LOGGER.info("The asynchronous game service has been shutdown, exiting...");
			syncTasks.forEach(Runnable::run);
			syncTasks.clear();
			World.getPlayers().clear();
			executorService.shutdown();
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Couldn't close properly the game service.", e);
		}
		System.exit(0);
	}
	
	/**
	 * Queues {@code t} to be executed on this game service thread.
	 * @param t The task to be queued.
	 */
	public void sync(Runnable t) {
		syncTasks.add(t);
	}
	
	/**
	 * Executes {@code t} using the backing cached thread pool. Tasks submitted this way should generally be short
	 * and low priority.
	 * @param t The task to execute.
	 */
	public void execute(Runnable t) {
		executorService.execute(t);
	}
	
	/**
	 * Executes the result-bearing {@code t} using the backing cached thread pool. Tasks submitted this way should
	 * generally be short and low priority.
	 * @param t The task to execute.
	 * @return The {@link ListenableFuture} to track completion of the task.
	 */
	public <T> ListenableFuture<T> submit(Callable<T> t) {
		return executorService.submit(t);
	}
	
	/**
	 * Executes {@code t} using the backing cached thread pool. Tasks submitted this way should generally be short
	 * and low priority.
	 * @param t The task to execute.
	 * @return The {@link ListenableFuture} to track completion of the task.
	 */
	public ListenableFuture<?> submit(Runnable t) {
		return executorService.submit(t);
	}
	
}