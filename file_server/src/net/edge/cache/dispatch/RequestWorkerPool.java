package net.edge.cache.dispatch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.edge.cache.fs.IndexedFileSystem;

/**
 * A class which manages the pool of request workers.
 * @author Graham
 */
public final class RequestWorkerPool {
	
	/**
	 * The number of threads per request type.
	 */
	private static final int THREADS_PER_REQUEST_TYPE = Runtime.getRuntime().availableProcessors();
	
	/**
	 * The number of request types.
	 */
	private static final int REQUEST_TYPES = 3;
	
	/**
	 * The executor service.
	 */
	private final ExecutorService service;
	
	/**
	 * A list of request workers.
	 */
	private final List<RequestWorker<?>> workers = new ArrayList<>();
	
	/**
	 * The request worker pool.
	 */
	public RequestWorkerPool() {
		int totalThreads = REQUEST_TYPES * THREADS_PER_REQUEST_TYPE;
		service = Executors.newFixedThreadPool(totalThreads);
	}

	/**
	 * Starts the threads in the pool.
	 * @throws Exception if the file system cannot be created.
	 */
	public void start() throws Exception {
		File base = new File("data/fs/");
		final IndexedFileSystem fs = new IndexedFileSystem(base);
		for(int i = 0; i < THREADS_PER_REQUEST_TYPE; i++) {
			workers.add(new JagGrabRequestWorker(fs));
			workers.add(new OnDemandRequestWorker(fs));
			workers.add(new HttpRequestWorker(fs));
		}
		workers.forEach(service::submit);
	}
	
	/**
	 * Stops the threads in the pool.
	 */
	public void stop() {
		workers.forEach(RequestWorker::stop);
		service.shutdownNow();
	}

}
