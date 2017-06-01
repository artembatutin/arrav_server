package net.edge.world.region;

import net.edge.util.LoggerUtils;
import net.edge.util.rand.RandomUtils;
import net.edge.World;
import net.edge.locale.Position;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
	
	/**
	 * The amount of ticks to execute the sequence listener.
	 */
	private static final int SEQUENCE_TICKS = 100;
	
	//The nodes in this region.
	/**
	 * A concurrent {@link Map} of active {@link Player}s in this {@code Region}.
	 */
	private final Map<Integer, Player> players = new ConcurrentHashMap<>();
	
	/**
	 * A concurrent {@link Map} of active {@link Npc}s in this {@code Region}.
	 */
	private final Map<Integer, Npc> npcs = new ConcurrentHashMap<>();
	
	/**
	 * A concurrent {@link Map} of {@link ItemNode}s in this {@code Region}.
	 */
	private final Map<Position, Set<ItemNode>> items = new ConcurrentHashMap<>();
	
	/**
	 * A concurrent {@link Map} of {@link ItemNode}s in this {@code Region} that is queued to be removed.
	 */
	private final Map<Position, Set<ItemNode>> removedItems = new ConcurrentHashMap<>();
	
	/**
	 * A concurrent {@link Map} of active {@link ObjectNode}s in this {@code Region}.
	 */
	private final Map<Integer, Set<ObjectNode>> objects = new ConcurrentHashMap<>();
	
	/**
	 * A concurrent {@link Map} of removed {@link ObjectNode}s in this {@code Region}.
	 */
	private final Map<Integer, Set<ObjectNode>> removeObjects = new ConcurrentHashMap<>();
	
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
	public void sequence() {
		//cleanup timer increased.
		cleanup++;
		
		//sequencing active item nodes.
		for(Position pos : items.keySet()) {
			for(ItemNode item : items.get(pos)) {
				if(item.getState() == NodeState.INACTIVE) {
					continue;
				}
				if(item.getCounter().incrementAndGet(10) >= SEQUENCE_TICKS) {
					item.onSequence();
					item.getCounter().set(0);
				}
			}
		}
		//removing dead item nodes
		for(Position pos : removedItems.keySet()) {
			getItems(pos).removeAll(removedItems.get(pos));
		}
		removedItems.clear();
		//cleaning only when the cleanup time has come.
		if(cleanup > 500) {
			if(getState() == NodeState.ACTIVE && getPlayers().isEmpty()) {
				boolean playersNearby = playersAround();
				if(!playersNearby) {
					setState(NodeState.INACTIVE);
				}
			}
			cleanup = 0;
		}
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
	 * The method that updates all items in the region for {@code player}.
	 * @param player the player to update items for.
	 */
	public void onEnter(Player player) {
		for(Position pos : items.keySet()) {
			for(ItemNode item : items.get(pos)) {
				if(item.getItemState() == ItemState.HIDDEN || item.getState() != NodeState.ACTIVE)
					continue;
				if(item.getPosition() == null)
					continue;
				if(item.getInstance() != player.getInstance())
					continue;
				player.getMessages().sendRemoveGroundItem(item);
				if(item.getPosition().withinDistance(player.getPosition(), 60)) {
					if(item.getPlayer() == null && item.getItemState() == ItemState.SEEN_BY_EVERYONE) {
						player.getMessages().sendGroundItem(item);
						continue;
					}
					if(item.getPlayer().equals(player) && item.getItemState() == ItemState.SEEN_BY_OWNER) {
						player.getMessages().sendGroundItem(item);
					}
				}
			}
		}
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
	 * The method that attempts to register {@code item} and does not stack by
	 * default.
	 * @param item the item to attempt to register.
	 * @return {@code true} if the item was registered, {@code false} otherwise.
	 */
	public boolean register(ItemNode item) {
		return register(item, false);
	}
	
	/**
	 * The method that attempts to register {@code item}.
	 * @param item  the item to attempt to register.
	 * @param stack if the item should stack upon registration.
	 * @return {@code true} if the item was registered, {@code false} otherwise.
	 */
	public boolean register(ItemNode item, boolean stack) {
		if(item.getState() != NodeState.IDLE)
			return false;
		if(stack) {
			for(ItemNode next : getItems(item.getPosition())) {
				if(next.getPlayer() == null || next.getPosition() == null || next.getItem() == null)
					continue;
				if(next.getItem().getId() == item.getItem().getId() && next.getPlayer().equals(item.getPlayer())) {
					next.getItem().incrementAmountBy(item.getItem().getAmount());
					if(next.getItem().getAmount() <= 5) {
						next.dispose();
						next.register();
					}
					return true;
				}
			}
			item.setState(NodeState.ACTIVE);
			getItems(item.getPosition()).add(item);
			return true;
		}
		if(item.getItem().getDefinition().isStackable()) {
			item.setState(NodeState.ACTIVE);
			getItems(item.getPosition()).add(item);
			return true;
		}
		int amount = item.getItem().getAmount();
		item.getItem().setAmount(1);
		for(int i = 0; i < amount; i++) {
			item.setState(NodeState.ACTIVE);
			getItems(item.getPosition()).add(item);
		}
		return true;
	}
	
	/**
	 * The method that attempts to unregister {@code item}.
	 * @param item the item to attempt to unregister.
	 * @return {@code true} if the item was unregistered, {@code false}
	 * otherwise.
	 */
	public void unregister(ItemNode item) {
		getRemovedItems(item.getPosition()).add(item);
		item.setState(NodeState.INACTIVE);
	}
	
	/**
	 * Gets a {@link Set} of {@link ItemNode}s. If none then creating the set.
	 * @param position the position to grab from.
	 * @return the set of item nodes on the specified position.
	 */
	public Set<ItemNode> getItems(Position position) {
		return items.computeIfAbsent(position, key -> new HashSet<>());
	}
	
	/**
	 * The method that retrieves the item with {@code id} on {@code position}.
	 * @param id       the identifier to retrieve the item with.
	 * @param position the position to retrieve the item on.
	 * @return the item instance wrapped in an optional, or an empty optional if
	 * no item is found.
	 */
	public Optional<ItemNode> getItem(int id, Position position) {
		for(ItemNode i : getItems(position)) {
			if(i.getItemState() != ItemState.HIDDEN && i.getState() == NodeState.ACTIVE && i.getItem().getId() == id) {
				return Optional.of(i);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets a {@link Set} of {@link ItemNode}s to be removed. If none then creating the set.
	 * @param position the position to grab from.
	 * @return the set of item nodes on the specified position.
	 */
	public Set<ItemNode> getRemovedItems(Position position) {
		return removedItems.computeIfAbsent(position, key -> new HashSet<>());
	}
	
	/**
	 * Adds an {@link ObjectNode} to the backing set.
	 * @param o The object to add.
	 * @return {@code true} if it was added successfully, otherwise {@code false}.
	 */
	public boolean addObj(ObjectNode o) {
		return getObjectsFrompacked(o.getLocalPos()).add(o);
	}
	
	/**
	 * Removes an {@link ObjectNode} from the backing set.
	 * @param o The object to remove.
	 * @return {@code true} if it was added successfully, otherwise {@code false}.
	 */
	public boolean removeObj(ObjectNode o) {
		return getObjectsFrompacked(o.getLocalPos()).remove(o);
	}
	
	/**
	 * Unregisters {@link ObjectNode}s located on the specified position from the object backing set.
	 * @param position the position from which we delete our objects.
	 */
	public void unregister(Position position) {
		int local = position.toLocalPacked();
		getDynamicObjects().stream().filter(o2 -> local == o2.getLocalPos()).collect(Collectors.toSet()).stream().filter(this::removeObj).forEach(o -> {
			World.getTraversalMap().markObject(this, o, false, false);
			o.visible(false);
		});
	}
	
	/**
	 * Unregisters all {@link ObjectNode}s from the object backing set.
	 */
	public void unregisterAll() {
		getDynamicObjects().stream().filter(this::removeObj).forEach(o -> {
			World.getTraversalMap().markObject(this, o, false, false);
			o.visible(false);
		});
	}
	
	/**
	 * Gets the a set of {@link ObjectNode} from all {@link Position}s of the region where objects are applied.
	 * @return A {@link Set}.
	 */
	public Set<ObjectNode> getObjects() {
		Set<ObjectNode> all = new HashSet<>();
		for(Integer pos : objects.keySet())
			all.addAll(objects.get(pos));
		return all;
	}
	
	/**
	 * Gets the a set of {@link ObjectNode} on the specified {@link Position}.
	 * @param position The position.
	 * @return A {@link Set} of {@link ObjectNode}s on the specified position.
	 */
	public Set<ObjectNode> getObjects(Position position) {
		return objects.computeIfAbsent(position.toLocalPacked(), key -> new HashSet<>());
	}
	
	/**
	 * Gets the a set of {@link ObjectNode} on the packed coordinates.
	 * @param packed The packed position.
	 * @return A {@link Set} of {@link ObjectNode}s on the specified position.
	 */
	public Set<ObjectNode> getObjectsFrompacked(int packed) {
		return objects.computeIfAbsent(packed, key -> new HashSet<>());
	}
	
	/**
	 * Gets the an {@link Optional} of {@link ObjectNode}s on the specified {@link Position} with the specified {@code id}.
	 * @param id       The id of the object to seek for.
	 * @param position The position.
	 * @return A {@link Optional} of {@link ObjectNode} on the specified position.
	 */
	public Optional<ObjectNode> getObject(int id, Position position) {
		for(ObjectNode o : getObjectsFrompacked(position.toLocalPacked())) {
			if(o.getId() == id) {
				return Optional.of(o);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets the an {@link Optional} of {@link ObjectNode}s with the specified {@code id} and {@code packed} coordinates.
	 * @param id       The id of the object to seek for.
	 * @param packed The packed position.
	 * @return A {@link Optional} of {@link ObjectNode}.
	 */
	public Optional<ObjectNode> getObject(int id, int packed) {
		for(ObjectNode o : getObjectsFrompacked(packed)) {
			if(o.getId() == id) {
				return Optional.of(o);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets the a set of {@link ObjectNode}s with the specified {@code id}.
	 * @param id The id of the object to seek for.
	 * @return A {@link Set} of {@link ObjectNode}s on the specified position.
	 */
	public Set<ObjectNode> getObjects(int id) {
		return getObjects().stream().filter(o -> id == o.getId()).collect(Collectors.toSet());
	}
	
	/**
	 * Gets the a set of {@link ObjectNode}s on the specified {@link Position} within a distance.
	 * @param position The position.
	 * @return A {@link Set} of {@link ObjectNode}s on the specified position.
	 */
	public Set<ObjectNode> getObjects(Position position, int distance) {
		return getObjects().stream().filter(e -> position.withinDistance(e.getGlobalPos(), distance)).collect(Collectors.toSet());
	}
	
	/**
	 * Gets the a set of {@link ObjectNode}s that are registered by the server during game time.
	 * @return A {@link Set} of {@link ObjectNode}s that are registered..
	 */
	public Set<ObjectNode> getDynamicObjects() {
		return getObjects().stream().filter(ObjectNode::isDynamic).collect(Collectors.toSet());
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
	
}