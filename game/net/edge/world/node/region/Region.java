package net.edge.world.node.region;

import net.edge.util.LoggerUtils;
import net.edge.util.rand.RandomUtils;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.Node;
import net.edge.world.node.NodeState;
import net.edge.world.node.NodeType;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.ItemNode;
import net.edge.world.node.item.ItemState;
import net.edge.world.object.ObjectNode;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

import static net.edge.world.node.NodeState.ACTIVE;
import static net.edge.world.node.NodeState.INACTIVE;

/**
 * A location on the tool.mapviewer that is {@code 64x64} in size. Used primarily for caching various types of {@link Node}s and
 * {@link RegionTile}s. There is a reason that the {@link Node}s are not being cached together.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class Region extends Node {
	
	/**
	 * The {@link Logger} instance to log our global changes.
	 */
	private static final Logger LOGGER = LoggerUtils.getLogger(Region.class);
	
	//Few process constants.
	/**
	 * The size of a region.
	 */
	private static final int REGION_SIZE = 64;
	
	/**
	 * The maximum level a floor can be.
	 */
	private static final int MAXIMUM_HEIGHT_LEVEL = 4;
	
	//The nodes in this region.
	/**
	 * A concurrent {@link Map} of active {@link Player}s in this {@code Region}.
	 */
	private final Map<Integer, Player> players = new HashMap<>();
	
	/**
	 * A concurrent {@link Map} of active {@link Npc}s in this {@code Region}.
	 */
	private final Map<Integer, Npc> npcs = new HashMap<>();
	
	/**
	 * A concurrent {@link Map} of active {@link ObjectNode}s in this {@code Region}.
	 */
	private final Map<Integer, RegionTiledObjects> objects = new HashMap<>();
	
	/**
	 * A concurrent {@link Map} of removed {@link ObjectNode}s in this {@code Region}.
	 */
	private final Map<Integer, Set<ObjectNode>> removeObjects = new HashMap<>();
	
	//The clipping data and few switches.
	/**
	 * The Id of this {@link Region}.
	 */
	private final int regionId;
	
	/**
	 * A simple integer acting as a clean up timer for this region.
	 * We randomize it so all regions clean at their own pace.
	 */
	private int cleanup = RandomUtils.inclusive(0, 400);
	
	/**
	 * The tiles within the region(regional clipping).
	 */
	private RegionTile[][] tiles;
	
	/**
	 * Creates a new {@link Region}.
	 * @param regionId The id of this region.
	 */
	Region(int regionId) {
		super(new Position((regionId >> 8) << 6, (regionId & 0xFF) << 6), NodeType.REGION);
		this.regionId = regionId;
	}
	
	@Override
	public void register() {
		//activating all npcs.
		npcs.forEach((i, n) -> n.setActive(true));
		LOGGER.info("Loaded Region: [" + regionId + "] on the fly.");
	}
	
	@Override
	public void dispose() {
		//deactivating all npcs.
		npcs.forEach((i, n) -> n.setActive(false));
		LOGGER.info("Disposed Region: [" + regionId + "] on the fly.");
	}
	
	/**
	 * Adds an {@link EntityNode} to the backing queue.
	 * @param e The entity to add.
	 * @return {@code true} if it was added successfully, otherwise {@code false}.
	 */
	public <T extends EntityNode> boolean addChar(T e) {
		if(e.isPlayer()) {
			if(players.containsKey(e.getSlot()))
				return false;
			players.put(e.getSlot(), e.toPlayer());
		} else {
			if(npcs.containsKey(e.getSlot()))
				return false;
			npcs.put(e.getSlot(), e.toNpc());
		}
		return true;
	}
	
	/**
	 * Removes an {@link EntityNode} from the backing queue.
	 * @param e The entity to remove.
	 */
	public <T extends EntityNode> void removeChar(T e) {
		if(e.isPlayer()) {
			players.remove(e.getSlot());
		} else {
			npcs.remove(e.getSlot());
		}
	}
	
	/**
	 * Retrieves and returns an {@link Map} of {@link Player}s.
	 * @return all the players inside the region.
	 */
	public Map<Integer, Player> getPlayers() {
		return players;
	}
	
	/**
	 * Retrieves and returns an {@link Map} of {@link Npc}s.
	 * @return all the npcs inside the region.
	 */
	public Map<Integer, Npc> getNpcs() {
		return npcs;
	}
	
	/**
	 * Adds an {@link ObjectNode} to the backing set.
	 * @param o The object to add.
	 */
	public void addObj(ObjectNode o) {
		getObjectsFrompacked(o.getLocalPos()).add(o);
	}
	
	/**
	 * Removes an {@link ObjectNode} from the backing set.
	 * @param o The object to remove.
	 */
	public void removeObj(ObjectNode o) {
		getObjectsFrompacked(o.getLocalPos()).remove(o);
	}
	
	/**
	 * Gets the a set of {@link ObjectNode} on the specified {@link Position}.
	 * @param position The position.
	 * @return A {@link Set} of {@link ObjectNode}s on the specified position.
	 */
	public RegionTiledObjects getObjects(Position position) {
		return objects.computeIfAbsent(position.toLocalPacked(), key -> new RegionTiledObjects());
	}
	
	/**
	 * Gets the a set of {@link ObjectNode} on the packed coordinates.
	 * @param packed The packed position.
	 * @return A {@link Set} of {@link ObjectNode}s on the specified position.
	 */
	public RegionTiledObjects getObjectsFrompacked(int packed) {
		return objects.computeIfAbsent(packed, key -> new RegionTiledObjects());
	}
	
	/**
	 * Gets the an {@link Optional} of {@link ObjectNode}s on the specified {@link Position} with the specified {@code id}.
	 * @param id       The id of the object to seek for.
	 * @param position The position.
	 * @return A {@link Optional} of {@link ObjectNode} on the specified position.
	 */
	public Optional<ObjectNode> getObject(int id, Position position) {
		return getObject(id, position.toLocalPacked());
	}
	
	/**
	 * Gets the an {@link Optional} of {@link ObjectNode}s with the specified {@code id} and {@code packed} coordinates.
	 * @param id       The id of the object to seek for.
	 * @param packed The packed position.
	 * @return A {@link Optional} of {@link ObjectNode}.
	 */
	public Optional<ObjectNode> getObject(int id, int packed) {
		RegionTiledObjects tile = getObjectsFrompacked(packed);
		ObjectNode o = tile.getId(id);
		if(o != null)
			return Optional.of(o);
		return Optional.empty();
	}
	
	/**
	 * Gets the a set of {@link ObjectNode}s with the specified {@code id}.
	 * @param id The id of the object to seek for.
	 * @return A {@link Set} of {@link ObjectNode}s on the specified position.
	 */
	public List<ObjectNode> getInteractiveObjects(int id) {
		List<ObjectNode> filtered = new ArrayList<>();
		List<ObjectNode> active = getInteractiveObjects();
		for(ObjectNode o : active) {
			if(o != null && o.getId() == id)
				filtered.add(o);
		}
		return filtered;
	}
	
	/**
	 * Gets the a set of {@link ObjectNode}s on the specified {@link Position} within a distance.
	 * @param position The position.
	 * @return A {@link Set} of {@link ObjectNode}s on the specified position.
	 */
	public List<ObjectNode> getInteractiveObjects(Position position, int id, int distance) {
		List<ObjectNode> filtered = new ArrayList<>();
		List<ObjectNode> active = getInteractiveObjects();
		for(ObjectNode o : active) {
			if(o != null && o.getId() == id && o.getGlobalPos().withinDistance(position, distance))
				filtered.add(o);
		}
		return filtered;
	}
	
	
	/**
	 * Gets the a List of {@link ObjectNode} from all {@link Position}s of the region where objects are applied.
	 * @return A {@link List}.
	 */
	public List<ObjectNode> getAllObjects() {
		List<ObjectNode> all = new ArrayList<>();
		objects.forEach((l, c) -> all.addAll(c.getAll()));
		return all;
	}
	
	/**
	 * Gets the a List of interactive {@link ObjectNode} from all {@link Position}s of the region where objects are applied.
	 * @return A {@link List}.
	 */
	public List<ObjectNode> getInteractiveObjects() {
		List<ObjectNode> all = new ArrayList<>();
		objects.forEach((l, c) -> all.addAll(c.getInteract()));
		return all;
	}
	
	/**
	 * Gets the a List of static {@link ObjectNode} of interactive from all {@link Position}s of the region where objects are applied.
	 * @return A {@link List}.
	 */
	public List<ObjectNode> getStaticObjects() {
		List<ObjectNode> all = new ArrayList<>();
		objects.forEach((l, c) -> all.addAll(c.getInteract()));
		return all;
	}
	
	/**
	 * Gets the a List of dynamic {@link ObjectNode} of interactive from all {@link Position}s of the region where objects are applied.
	 * @return A {@link List}.
	 */
	public List<ObjectNode> getDynamicObjects() {
		List<ObjectNode> all = new ArrayList<>();
		objects.forEach((l, c) -> all.addAll(c.getDynamic()));
		return all;
	}
	
	/**
	 * Determines if there any player around this region.
	 * @return {@code true} if there is a player around regionally, {@code false} otherwise.
	 */
	private boolean playersAround() {
		for(Region r : World.getRegions().getAllSurroundingRegions(getRegionId())) {
			if(!r.getPlayers().isEmpty())
				return true;
		}
		return false;
	}
	
	/**
	 * Gets a single tile in this region from the specified height, x and y
	 * coordinates.
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @return The tile in this region for the specified attributes.
	 */
	RegionTile getTile(int height, int x, int y) {
		if(tiles == null) {
			tiles = new RegionTile[MAXIMUM_HEIGHT_LEVEL][REGION_SIZE * REGION_SIZE];
		}
		if(tiles[height][x + y * REGION_SIZE] == null) {
			tiles[height][x + y * REGION_SIZE] = new RegionTile();
		}
		return tiles[height][x + y * REGION_SIZE];
	}
	
	/**
	 * Gets the id of this region.
	 * @return region id.
	 */
	public int getRegionId() {
		return regionId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getRegionId());
	}
	
	@Override
	public String toString() {
		return "REGION[id= " + getRegionId() + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Region) {
			Region other = (Region) obj;
			return getRegionId() == other.getRegionId();
		}
		return false;
	}
	
	/**
	 * Gets the a set of removed {@link ObjectNode} on the specified {@link Position}.
	 * @param local The local packed position.
	 * @return A {@link Set} of removed {@link ObjectNode}s on the specified position.
	 */
	public Set<ObjectNode> getRemovedObjects(int local) {
		return removeObjects.computeIfAbsent(local, key -> new HashSet<>());
	}
	
	/**
	 * Gets the a {@link Map} of {@link ObjectNode}s that are removed by the server during game time.
	 * @return A {@link Map} of {@link ObjectNode}s that are removed.
	 */
	public Map<Integer, Set<ObjectNode>> getRemovedObjects() {
		return removeObjects;
	}
	
}