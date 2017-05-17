package net.edge.fs.parser;

import net.edge.Server;
import net.edge.fs.FileSystem;
import net.edge.utils.ByteBufferUtil;
import net.edge.utils.CompressionUtil;
import net.edge.world.World;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.object.ObjectDirection;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.world.model.node.object.ObjectType;
import net.edge.world.model.node.region.RegionDefinition;
import net.edge.world.model.node.region.RegionTile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * A class which parses static object definitions, which include map tiles and
 * landscapes.
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author JayArrowz
 */
public final class StaticObjectDefinitionParser {
	
	/**
	 * The map definitions.
	 */
	public static Map<Integer, RegionDefinition> REGION_DEFS = new HashMap<>();
	
	/**
	 * Parses the map definition files from the {@link FileSystem}.
	 * @throws IOException If some I/O exception occurs.
	 */
	public static void parse() throws IOException {
		REGION_DEFS = MapDefinitionParser.parse(Server.getFileSystem());
	}
	
	public static void load(int id) {
		RegionDefinition def = REGION_DEFS.get(id);
		if(def == null || def.loaded())
			return;
		
		final int hash = def.getHash();
		final int x = (hash >> 8 & 0xFF) * 64;
		final int y = (hash & 0xFF) * 64;
		final boolean isNew = def.isNew();
		
		try {
			List<Position> downHeights = new LinkedList<>();
			ByteBuffer terrainData = Server.getFileSystem().getFile(FileSystem.MAP_INDEX, def.getTerrainFile());
			ByteBuffer terrainBuffer = ByteBuffer.wrap(CompressionUtil.gunzip(terrainData.array()));
			parseTerrain(terrainBuffer, x, y, downHeights);
			
			ByteBuffer gameObjectData = Server.getFileSystem().getFile(FileSystem.MAP_INDEX, def.getObjectFile());
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
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		def.setLoaded(true);
	}
	
	/**
	 * Parses a {@link ObjectNode} on the specified coordinates.
	 * @param gameObjectBuffer The uncompressed game object data buffer.
	 * @param x                The x coordinate this object is on.
	 * @param y                The y coordinate this object is on.
	 */
	public static Set<ObjectNode> parseGameObject(ByteBuffer gameObjectBuffer, int x, int y, List<Position> downHeights, boolean down) {
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
	 * Loads all of the map indexes entries and decodes each.
	 * @param mapBuffer The uncompressed map entry data buffer.
	 * @param x         The x coordinate of this map entry.
	 * @param y         The y coordinate of this map entry.
	 */
	private static void parseTerrain(ByteBuffer mapBuffer, int x, int y, List<Position> downHeights) {
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
	private static void terrainDecoded(int flags, Position position, List<Position> downHeights) {
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
	
	public static void setUnloaded(int region) {
		RegionDefinition def = REGION_DEFS.get(region);
		if(def != null) {
			def.setLoaded(false);
		}
	}
	
}