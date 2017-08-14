package net.edge.fs;

import java.nio.charset.Charset;

import net.edge.fs.net.ChannelHandler;

import com.google.common.collect.ImmutableList;

/**
 * Attributes for configuring the FileServer.
 * @author Professor Oak
 */
public class FileServerConstants {

	/**
	 * Represents the file directory path to the cache.
	 */
	public static final String CACHE_BASE_DIR = "./data/cache/";

	/**
	 * The port for the jaggrab service.
	 */
	public static final int JAGGRAB_PORT = 43596;

	/**
	 * The port for the ondemand service.
	 */
	public static final int ONDEMAND_PORT = 43595;

	/**
	 * The list of exceptions that are ignored and discarded by the {@link ChannelHandler}.
	 */
	public static final ImmutableList<String> IGNORED_NETWORK_EXCEPTIONS =
			ImmutableList.of("An existing connection was forcibly closed by the remote host",
					"An established connection was aborted by the software in your host machine",
					"En befintlig anslutning tvingades att st�nga av fj�rrv�rddatorn"); //Swedish <3

	/**
	 * The time required for the channel to time out
	 */
	public static final int SESSION_TIMEOUT = 30;

	/**
	 * The character set used in a JagGrab request.
	 */
	public static final Charset JAGGRAB_CHARSET = Charset.forName("US-ASCII");

	/**
	 * The maximum length of an ondemand file chunk, in bytes.
	 */
	public static final int MAX_ONDEMAND_CHUNK_LENGTH_BYTES = 500;

}
