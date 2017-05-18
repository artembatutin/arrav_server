package net.edge.cache.decoder;

import net.edge.cache.FileSystem;
import net.edge.utils.ByteBufferUtil;
import net.edge.utils.CompressionUtil;
import net.edge.utils.LoggerUtils;
import net.edge.world.World;
import net.edge.world.locale.Position;
import net.edge.world.node.object.ObjectDirection;
import net.edge.world.node.object.ObjectNode;
import net.edge.world.node.object.ObjectType;
import net.edge.world.node.region.RegionDefinition;
import net.edge.world.node.region.RegionTile;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.Logger;

/**
 * A class which parses static object definitions, which include mapviewer tiles and
 * landscapes.
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class RegionDecoder implements Runnable {
	
	/**
	 * The logger that will print important information.
	 */
	private final static Logger LOGGER = LoggerUtils.getLogger(RegionDecoder.class);
	
	/**
	 * The FileSystem.
	 */
	private final FileSystem fs;
	
	/**
	 * Amount of regions correctly decoded.
	 */
	private int decoded;
	
	/**
	 * Amount of regions incorrectly decoded.
	 */
	private int errors;
	
	/**
	 * Creates the {@link ObjectDefinitionDecoder}.
	 *
	 * @param fs The {@link FileSystem}.
	 */
	public RegionDecoder(FileSystem fs) {
		this.fs = fs;
	}
	
	@Override
	public void run() {
		LOGGER.info("Loading regional map data.");
		Map<Integer, RegionDefinition> maps = RegionDefinition.getDefinitions();
		maps.forEach((i, d) -> load(d));
		LOGGER.info("Loaded " + decoded + " regions, skipped " + errors + " maps.");
	}
	
	public void load(RegionDefinition def) {
		final int hash = def.getHash();
		final int x = (hash >> 8 & 0xFF) * 64;
		final int y = (hash & 0xFF) * 64;
		final boolean isNew = def.isNew();
		
		try {
			List<Position> downHeights = new LinkedList<>();
			ByteBuffer terrainData = fs.getFile(FileSystem.MAP_INDEX, def.getTerrainFile());
			ByteBuffer terrainBuffer = ByteBuffer.wrap(CompressionUtil.gunzip(terrainData.array()));
			parseTerrain(terrainBuffer, x, y, downHeights);
			
			ByteBuffer gameObjectData = fs.getFile(FileSystem.MAP_INDEX, def.getObjectFile());
			ByteBuffer gameObjectBuffer = ByteBuffer.wrap(CompressionUtil.gunzip(gameObjectData.array()));
			Set<ObjectNode> objects = parseGameObject(gameObjectBuffer, x, y, downHeights, true);
			for(ObjectNode o : objects) {
				if(isNew) {
					o.setId(o.getId() + 42003);//667 objects.
				} else if(o.getId() >= 42003) {
					o.setId(o.getId() + 42003);//667 offset for 530 objects.
				}
				if(o.getId() == 38453 || o.getId() == 38447)
					continue;
				World.getTraversalMap().markObject(o, true, true);
			}
			decoded++;
		} catch(Exception e) {
			errors++;
		}
	}
	
	/**
	 * Parses a {@link ObjectNode} on the specified coordinates.
	 * @param gameObjectBuffer The uncompressed game object data buffer.
	 * @param x                The x coordinate this object is on.
	 * @param y                The y coordinate this object is on.
	 */
	private Set<ObjectNode> parseGameObject(ByteBuffer gameObjectBuffer, int x, int y, List<Position> downHeights, boolean down) {
		Set<ObjectNode> objs = new HashSet<>();
		for(int deltaId, id = -1; (deltaId = ByteBufferUtil.getSmart(gameObjectBuffer)) != 0; ) {
			id += deltaId;
			for(int deltaPos, hash = 0; (deltaPos = ByteBufferUtil.getSmart(gameObjectBuffer)) != 0; ) {
				hash += deltaPos - 1;
				int localX = hash >> 6 & 0x3F;
				int localY = hash & 0x3F;
				int height = hash >> 12;
				int attributeHashCode = gameObjectBuffer.get() & 0xFF;
				Optional<ObjectType> type = ObjectType.valueOf(attributeHashCode >> 2);
				Optional<ObjectDirection> orientation = ObjectDirection.valueOf(attributeHashCode & 0x3);
				Position position = new Position(x + localX, y + localY, height);
				if(height > 0) {
					if(down && downHeights.contains(new Position(position.getX(), position.getY(), 1))) {
						position = position.move(0, 0, -1);
					}
				}
				if(type.isPresent() && orientation.isPresent()) {
					objs.add(new ObjectNode(id, position, orientation.get(), type.get()));
				}
			}
		}
		return objs;
	}
	
	/**
	 * Loads all of the mapviewer indexes entries and decodes each.
	 * @param mapBuffer The uncompressed mapviewer entry data buffer.
	 * @param x         The x coordinate of this mapviewer entry.
	 * @param y         The y coordinate of this mapviewer entry.
	 */
	private void parseTerrain(ByteBuffer mapBuffer, int x, int y, List<Position> downHeights) {
		for(int height = 0; height < 4; height++) {
			for(int localX = 0; localX < 64; localX++) {
				for(int localY = 0; localY < 64; localY++) {
					Position position = new Position(x + localX, y + localY, height);
					int flags = 0;
					while(true) {
						int attributeId = mapBuffer.get() & 0xFF;
						if(attributeId == 0) {
							terrainDecoded(flags, position, downHeights);
							break;
						}
						if(attributeId == 1) {
							mapBuffer.get();
							terrainDecoded(flags, position, downHeights);
							break;
						}
						if(attributeId <= 49) {
							mapBuffer.get();
						} else if(attributeId <= 81) {
							if(height > 0 && ((attributeId - 49) & RegionTile.FLAG_BRIDGE) == 2) {
								downHeights.add(position);
							}
							flags = attributeId - 49;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Decodes the terrains {@link Position}.
	 * @param flags    The flags for the specified position.
	 * @param position The decoded position.
	 */
	private void terrainDecoded(int flags, Position position, List<Position> downHeights) {
		if(position.getZ() > 0 && downHeights.contains(new Position(position.getX(), position.getY(), 1))) {
			position = position.move(0, 0, -1);
		}
		if((flags & RegionTile.FLAG_BLOCKED) != 0) {
			World.getTraversalMap().mark(position.getZ(), position.getX(), position.getY(), true, false);
		}
		if((flags & RegionTile.FLAG_BRIDGE) != 0) {
			World.getTraversalMap().markBridge(position.getZ(), position.getX(), position.getY() - 1);
		}
	}
	
}