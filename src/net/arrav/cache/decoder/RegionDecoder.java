package net.arrav.cache.decoder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.cache.FileSystem;
import net.arrav.content.object.BarChair;
import net.arrav.content.object.door.DoorHandler;
import net.arrav.util.ByteBufferUtil;
import net.arrav.util.CompressionUtil;
import net.arrav.util.LoggerUtils;
import net.arrav.world.World;
import net.arrav.world.entity.region.*;
import net.arrav.world.locale.Position;
import net.arrav.world.object.GameObject;
import net.arrav.world.object.ObjectDirection;
import net.arrav.world.object.ObjectType;
import net.arrav.world.object.StaticObject;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.Logger;

/**
 * A class which parses static object definitions, which include tool.mapviewer tiles and
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
		World.getRegions().getRegion(((x >> 6) << 8) + (y >> 6)).ifPresent(r -> {
			try {
				ObjectList<Position> downHeights = new ObjectArrayList<>();
				ByteBuffer terrainData = fs.getFile(FileSystem.MAP_INDEX, def.getTerrainFile());
				ByteBuffer terrainBuffer = ByteBuffer.wrap(CompressionUtil.gunzip(terrainData.array()));
				parseTerrain(r, terrainBuffer, x, y, downHeights);
				
				ByteBuffer gameObjectData = fs.getFile(FileSystem.MAP_INDEX, def.getObjectFile());
				ByteBuffer gameObjectBuffer = ByteBuffer.wrap(CompressionUtil.gunzip(gameObjectData.array()));
				ObjectList<StaticObject> objects = parseGameObject(def.getObjectFile(), r, gameObjectBuffer, x, y, downHeights, isNew);
				for(StaticObject o : objects) {
					TraversalMap.markObject(r, o, true, true);
					if(o.getDefinition() != null && DoorHandler.isDoor(o.getDefinition()))
						DoorHandler.APPENDER.registerFirst(o.getId());
					if(o.getId() == BarChair.CHAIR_ID)
						BarChair.register(o);
				}
				downHeights.clear();
				objects.clear();
				decoded++;
			} catch(Exception e) {
				errors++;
			}
		});
	}
	
	/**
	 * Parses a {@link GameObject} on the specified coordinates.
	 */
	private ObjectList<StaticObject> parseGameObject(int file, Region region, ByteBuffer gameObjectBuffer, int x, int y, ObjectList<Position> downHeights, boolean isNew) {
		ObjectList<StaticObject> objs = new ObjectArrayList<>();
		int offset = 0;
		if(file == 625) {
			offset = 5;
		}
		for(int deltaId, id = -1; (deltaId = ByteBufferUtil.getSmart(gameObjectBuffer)) != 0; ) {
			id += deltaId;
			for(int deltaPos, hash = 0; (deltaPos = ByteBufferUtil.getSmart(gameObjectBuffer)) != 0; ) {
				hash += deltaPos - 1;
				int localX = hash >> 6 & 0x3F;
				int localY = hash & 0x3F;
				int height = hash >> 12;
				int attributeHashCode = gameObjectBuffer.get() & 0xFF;
				Optional<ObjectType> type = ObjectType.valueOf((attributeHashCode >> 2) - offset);
				Optional<ObjectDirection> orientation = ObjectDirection.valueOf(attributeHashCode & 0x3);
				Position pos = new Position(x + localX, y + localY, height);
				if(height > 0) {
					if(downHeights.contains(new Position(pos.getX(), pos.getY(), 1))) {
						pos = pos.move(0, 0, -1);
					}
				}
				int objectId = id;
				if(isNew || objectId == 2563) {
					objectId = objectId + 42003;//667 objects.
				} else if(objectId >= 42003) {
					objectId = objectId + 42003;//667 offset for 530 objects.
				}
				if(objectId == 38453 || objectId == 38447)
					continue;
				if(type.isPresent() && orientation.isPresent()) {
					objs.add(new StaticObject(region, objectId, localX, localY, pos.getZ(), orientation.get(), type.get()));
				}
			}
		}
		return objs;
	}
	
	/**
	 * Loads all of the tool.mapviewer indexes entries and decodes each.
	 */
	private void parseTerrain(Region region, ByteBuffer mapBuffer, int x, int y, ObjectList<Position> downHeights) {
		for(int height = 0; height < 4; height++) {
			for(int localX = 0; localX < 64; localX++) {
				for(int localY = 0; localY < 64; localY++) {
					Position position = new Position(x + localX, y + localY, height);
					int flags = 0;
					while(true) {
						int attributeId = mapBuffer.get() & 0xFF;
						if(attributeId == 0) {
							terrainDecoded(region, flags, position, downHeights);
							break;
						}
						if(attributeId == 1) {
							mapBuffer.get();
							terrainDecoded(region, flags, position, downHeights);
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
	 */
	private void terrainDecoded(Region region, int flags, Position position, ObjectList<Position> downHeights) {
		if(position.getZ() > 0 && downHeights.contains(new Position(position.getX(), position.getY(), 1))) {
			position = position.move(0, 0, -1);
		}
		if((flags & RegionTile.FLAG_BLOCKED) != 0) {
			TraversalMap.mark(region, position.getZ(), position.getX(), position.getY(), true, false);
		}
		if((flags & RegionTile.FLAG_BRIDGE) != 0) {
			TraversalMap.markBridge(region, position.getZ(), position.getX(), position.getY() - 1);
		}
	}
	
}