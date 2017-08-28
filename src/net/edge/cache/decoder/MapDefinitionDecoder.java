package net.edge.cache.decoder;

import net.edge.cache.FileSystem;
import net.edge.cache.archive.Archive;
import net.edge.util.LoggerUtils;
import net.edge.world.entity.region.RegionDefinition;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * A class which parses {@link RegionDefinition}s
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MapDefinitionDecoder implements Runnable {
	
	/**
	 * The logger that will print important information.
	 */
	private final static Logger LOGGER = LoggerUtils.getLogger(MapDefinitionDecoder.class);
	
	/**
	 * The IndexedFileSystem.
	 */
	private final FileSystem fs;
	
	/**
	 * Creates the {@link MapDefinitionDecoder}.
	 * @param fs The {@link FileSystem}.
	 */
	public MapDefinitionDecoder(FileSystem fs) {
		this.fs = fs;
	}
	
	@Override
	public void run() {
		LOGGER.info("Loading region definitions.");
		Archive archive = fs.getArchive(FileSystem.MANIFEST_ARCHIVE);
		ByteBuffer buffer = archive.getData("map_index");
		int count = buffer.getShort() & 0xFFFF;
		for(int i = 0; i < count; i++) {
			int hash = buffer.getShort() & 0xFFFF;
			int terrainFile = buffer.getShort() & 0xFFFF;
			int objectFile = buffer.getShort() & 0xFFFF;
			boolean isNew = buffer.get() == 1;
			RegionDefinition.set(new RegionDefinition(hash, terrainFile, objectFile, isNew));
		}
		LOGGER.info("Loaded " + count + " region definitions.");
	}
	
}