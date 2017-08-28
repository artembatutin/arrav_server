package net.edge.util.log;

import net.edge.world.World;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The class which represents a single log.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class Log {

	/**
	 * Represents the users username.
	 */
	private final String username;

	/**
	 * Represents the date this logger was issued.
	 */
	private final String date;

	/**
	 * The catagory of this log.
	 */
	private final String catagory;

	/**
	 * The information this logger has issued.
	 */
	private final String information;

	/**
	 * Constructs a new {@link Log}.
	 *
	 * @param username    {@link #username}.
	 * @param date        {@link #date}.
	 * @param information {@link #information}.
	 */
	public Log(String username, String date, String catagory, String information) {
		this.username = username;
		this.date = date;
		this.catagory = catagory;
		this.information = information;
	}

	/**
	 * Constructs a new {@link Log}.
	 *
	 * @param username    {@link #username}.
	 * @param catagory    {@link #catagory}.
	 * @param information {@link #information}.
	 */
	public Log(String username, String catagory, String information) {
		this(username, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), catagory, information);
	}

	/**
	 * Creates a new log from an abstract {@link LogDetails} implementation.
	 *
	 * @param details the details to create a new log from.
	 * @return a new {@code Log} build from an abstract {@link LogDetails} implementation.
	 */
	public static Log create(LogDetails details) {
		return new Log(details.getUsername(), details.getCatagory(), details.formatInformation().orElse(details.getInformation()));
	}

	public String getPath() {
		return World.getLoggingManager().parent.getAbsolutePath() + "/" + this.getUsername();
	}

	@Override
	public String toString() {
		return "[Username = " + username + ", date = " + date + ", catagory = " + catagory + ", information = " + information + "]";
	}

	public String getUsername() {
		return username;
	}

	public String getDate() {
		return date;
	}

	public String getCatagory() {
		return catagory;
	}

	public String getInformation() {
		return information;
	}
}
