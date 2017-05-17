package net.edge.fs.parser;

import net.edge.fs.Archive;
import net.edge.fs.FileSystem;
import net.edge.utils.LoggerUtils;
import net.edge.world.model.node.region.RegionDefinition;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A class which parses {@link RegionDefinition}s
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MapDefinitionParser {
	
	/**
	 * The logger that will print important information.
	 */
	private static Logger logger = LoggerUtils.getLogger(MapDefinitionParser.class);
	
	/**
	 * Parses {@link RegionDefinition}s from the specified {@link FileSystem}.
	 * @param fs The file system.
	 * @return A {@link Map} of parsed map definitions.
	 * @throws IOException If some I/O error occurs.
	 */
	public static Map<Integer, RegionDefinition> parse(FileSystem fs) throws IOException {
		logger.info("Loading map definitions...");
		Archive archive = fs.getArchive(FileSystem.MANIFEST_ARCHIVE);
		ByteBuffer buffer = archive.getData("map_index");
		Map<Integer, RegionDefinition> defs = new HashMap<>();
		int count = buffer.getShort() & 0xFFFF;
		for(int i = 0; i < count; i++) {
			int hash = buffer.getShort() & 0xFFFF;
			int terrainFile = buffer.getShort() & 0xFFFF;
			int objectFile = buffer.getShort() & 0xFFFF;
			boolean isNew = buffer.get() == 1;
			defs.put(hash, new RegionDefinition(hash, terrainFile, objectFile, isNew));
		}
		logger.info("Loaded " + count + " map definitions.");
		return defs;
	}
	
}