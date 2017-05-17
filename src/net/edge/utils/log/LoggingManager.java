package net.edge.utils.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class which manages the i/o of all the logs.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class LoggingManager {
	
	/**
	 * A new thread pool to dispatch threads for writing asynchronously to the txt file.
	 */
	private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	/**
	 * The parent folder containing all the logs.
	 */
	private final File parent = new File("./data/logs/");
	
	/**
	 * The logger which will print out important information.
	 */
	private final Logger logger = Logger.getLogger(LoggingManager.class.getName());
	
	/**
	 * Writes the log information to the {@code LOG_DATA} map and to the logs.txt file.
	 * @param log the log to submit.
	 */
	public void write(Log log) {
		pool.execute(() -> {
			File file = new File(parent.getAbsolutePath() + "/" + log.getUsername());
			if(!file.exists()) {
				file.mkdirs();
			}
			
			try(FileWriter writer = new FileWriter(file.getAbsolutePath() + "/" + log.getCatagory() + ".txt", true)) {
				writer.write("[START]\n");
				writer.write("[Date: " + log.getDate() + "]\n");
				writer.write("[Information: " + log.getInformation() + "]\n");
				writer.write("[END]\n\n");
			} catch(IOException e) {
				logger.log(Level.WARNING, "Error writing a log for " + log.getUsername() + ".", e);
			}
		});
	}
}
