package net.edge.fs.cache.impl;

import net.edge.fs.cache.CacheLoader;

public class CacheConstants {

	/**
	 * Represents the id of the configurations cache.
	 */
	public static final int CONFIG_INDEX = 0;

	/**
	 * Represents the maximum amount of archives within this file system.
	 */
	public static final int MAXIMUM_ARCHIVES = 9;

	/**
	 * Represents the maximum amount of indices within this file system.
	 */
	public static final int MAXIMUM_INDICES = 256;

	/**
	 * Represents the prefix of this {@link CacheLoader}s main cache files.
	 */
	public static final String DATA_PREFIX = "main_file_cache.dat";

	/**
	 * Represents the prefix of this {@link CacheLoader}s index files.
	 */
	public static final String INDEX_PREFIX = "main_file_cache.idx";
		
}
