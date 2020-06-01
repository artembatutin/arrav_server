package com.rageps.util.log;

import java.util.Optional;

/**
 * The class which represents details for a single log.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class LogDetails {
	
	/**
	 * The username this details of this log is for.
	 */
	private final String username;
	
	/**
	 * The catagory of this log.
	 */
	private final String catagory;
	
	/**
	 * The information of this detail.
	 */
	private String information;
	
	/**
	 * Constructs a new {@link LogDetails}.
	 * @param username {@link #username}.
	 * @param catagory {@link #catagory}.
	 * @param information {@link #information}.
	 */
	public LogDetails(String username, String catagory, String information) {
		this.username = username;
		this.catagory = catagory;
		this.information = information;
	}
	
	/**
	 * Constructs a new {@link LogDetails}.
	 * <p>Only use this constructor if you're going to supply the catagory information
	 * by using LogDetails#formatInformation</p>
	 * @param catagory {@link #catagory}.
	 */
	public LogDetails(String username, String catagory) {
		this(username, catagory, null);
	}
	
	/**
	 * The method which can be overriden to format more detailed information about
	 * the log.
	 * @return an optional empty on default.
	 */
	public Optional<String> formatInformation() {
		return Optional.empty();
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getCatagory() {
		return catagory;
	}
	
	public String getInformation() {
		return information;
	}
}
