package net.edge.world.node.region;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
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
import net.edge.world.object.ObjectType;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

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
	 * A {@link ObjectList} of {@link ItemNode}s in this {@code Region}.
	 */
	private final ObjectList<ItemNode> items = new ObjectArrayList<>();
	
	/**
	 * A {@link Int2ObjectOpenHashMap} of active {@link Player}s in this {@code Region}.
	 */
	private final ObjectArrayList<Player> players = new ObjectArrayList<>();
	
	/**
	 * A {@link Int2ObjectOpenHashMap} of active {@link Npc}s in this {@code Region}.
	 */
	private final ObjectArrayList<Npc> npcs = new ObjectArrayList<>();
	
	/**
	 * A {@link Int2ObjectOpenHashMap} of active {@link ObjectNode}s in this {@code Region}.
	 */
	private final Int2ObjectOpenHashMap<RegionTiledObjects> objects = new Int2ObjectOpenHashMap<>();
	
	/**
	 * A {@link ObjectList} of removed {@link ObjectNode}s in this {@code Region}.
	 */
	private final ObjectList<ObjectNode> removeObjects = new ObjectArrayList<>();
	
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
		Iterator<ItemNode> it = items.iterator();
		while (it.hasNext()) {
			ItemNode item = it.next();
			if (item.getCounter().incrementAndGet(10) >= SEQUENCE_TICKS) {
				item.onSequence();
				item.getCounter().set(0);
			}
			if (item.getState() != NodeState.ACTIVE) {
				item.dispose();
				it.remove();
			}
		}
		
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
		for(Npc n : npcs) {
			n.setActive(true);
		}
		LOGGER.info("Loaded Region: [" + regionId + "] on the fly.");
	}
	
	@Override
	public void dispose() {
		//deactivating all npcs.
		for(Npc n : npcs) {
			n.setActive(false);
		}
		LOGGER.info("Disposed Region: [" + regionId + "] on the fly.");
	}
	
	/**
	 * The method that updates all items in the region for {@code player}.
	 * @param player the player to update items for.
	 */
	public void onEnter(Player player) {
		for(ItemNode item : items) {
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
				if(item.getPlayer().same(player) && item.getItemState() == ItemState.SEEN_BY_OWNER) {
					player.getMessages().sendGroundItem(item);
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
		if(e.getState() == NodeState.INACTIVE)
			return false;
		if(e.isPlayer()) {
			return players.add(e.toPlayer());
		} else {
			return npcs.add(e.toNpc());
		}
	}
	
	/**
	 * Removes an {@link EntityNode} from the backing queue.
	 * @param e The entity to remove.
	 */
	public <T extends EntityNode> boolean removeChar(T e) {
		if(e.isPlayer()) {
			return players.remove(e.toPlayer());
		} else {
			return npcs.remove(e.toNpc());
		}
	}
	
	/**
	 * Retrieves and returns an {@link Int2ObjectOpenHashMap} of {@link Player}s.
	 * @return all the players inside the region.
	 */
	public ObjectArrayList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Retrieves and returns an {@link Int2ObjectOpenHashMap} of {@link Npc}s.
	 * @return all the npcs inside the region.
	 */
	public ObjectArrayList<Npc> getNpcs() {
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
			for(ItemNode next : items) {
				if(next.getPlayer() == null || next.getPosition() == null || next.getItem() == null)
					continue;
				if(!next.getPosition().same(item.getPosition()))
					continue;
				if(next.getItem().getId() == item.getItem().getId() && next.getPlayer().same(item.getPlayer())) {
					next.getItem().incrementAmountBy(item.getItem().getAmount());
					if(next.getItem().getAmount() <= 5) {
						next.dispose();
						next.register();
					}
					return true;
				}
			}
			item.setState(NodeState.ACTIVE);
			items.add(item);
			return true;
		}
		if(item.getItem().getDefinition().isStackable()) {
			item.setState(NodeState.ACTIVE);
			items.add(item);
			return true;
		}
		int amount = item.getItem().getAmount();
		item.getItem().setAmount(1);
		for(int i = 0; i < amount; i++) {
			item.setState(NodeState.ACTIVE);
			items.add(item);
		}
		return true;
	}
	
	/**
	 * The method that attempts to unregister {@code item}.
	 * @param item the item to attempt to unregister.
	 * @return {@code true} if the item was unregistered, {@code false}
	 * otherwise.
	 */
	public boolean unregister(ItemNode item) {
		if (item.getState() != NodeState.ACTIVE)
			return false;
		if (items.remove(item)) {
			item.setState(NodeState.INACTIVE);
			return true;
		}
		return false;
	}
	
	/**
	 * The method that retrieves the item with {@code id} on {@code position}.
	 * @param id       the identifier to retrieve the item with.
	 * @param position the position to retrieve the item on.
	 * @return the item instance wrapped in an optional, or an empty optional if
	 * no item is found.
	 */
	public Optional<ItemNode> getItem(int id, Position position) {
		for(ItemNode i : items) {
			if(i.getItem().getId() == id && i.getItemState() != ItemState.HIDDEN && i.getState() == NodeState.ACTIVE && i.getPosition().same(position)) {
				return Optional.of(i);
			}
		}
		return Optional.empty();
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
	 * @return A {@link RegionTiledObjects} of {@link ObjectNode}s on the specified position.
	 */
	public RegionTiledObjects getObjects(Position position) {
		return objects.computeIfAbsent(position.toLocalPacked(), key -> new RegionTiledObjects());
	}
	
	/**
	 * Gets the a set of {@link ObjectNode} on the packed coordinates.
	 * @param packed The packed position.
	 * @return A {@link RegionTiledObjects} of {@link ObjectNode}s on the specified position.
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
	 * Gets the an {@link Optional} of {@link ObjectNode}s on the specified {@link Position} with the specified {@code type}.
	 * @param type       The type of the object to seek for.
	 * @param position The position.
	 * @return A {@link Optional} of {@link ObjectNode} on the specified position.
	 */
	public Optional<ObjectNode> getObject(ObjectType type, Position position) {
		RegionTiledObjects tile = getObjectsFrompacked(position.toLocalPacked());
		ObjectNode o = tile.getType(type);
		if(o != null)
			return Optional.of(o);
		return Optional.empty();
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
	 * @return A {@link ObjectList} of {@link ObjectNode}s on the specified position.
	 */
	public ObjectList<ObjectNode> getInteractiveObjects(int id) {
		ObjectList<ObjectNode> filtered = new ObjectArrayList<>();
		ObjectList<ObjectNode> active = getInteractiveObjects();
		for(ObjectNode o : active) {
			if(o != null && o.getId() == id)
				filtered.add(o);
		}
		return filtered;
	}
	
	/**
	 * Gets the a set of {@link ObjectNode}s on the specified {@link Position} within a distance.
	 * @param position The position.
	 * @return A {@link ObjectList} of {@link ObjectNode}s on the specified position.
	 */
	public ObjectList<ObjectNode> getInteractiveObjects(Position position, int id, int distance) {
		ObjectList<ObjectNode> filtered = new ObjectArrayList<>();
		ObjectList<ObjectNode> active = getInteractiveObjects();
		for(ObjectNode o : active) {
			if(o != null && o.getId() == id && o.getGlobalPos().withinDistance(position, distance))
				filtered.add(o);
		}
		return filtered;
	}
	
	
	/**
	 * Gets the a List of {@link ObjectNode} from all {@link Position}s of the region where objects are applied.
	 * @return A {@link ObjectList}.
	 */
	public ObjectList<ObjectNode> getAllObjects() {
		ObjectList<ObjectNode> all = new ObjectArrayList<>();
		objects.forEach((l, c) -> all.addAll(c.getAll()));
		return all;
	}
	
	/**
	 * Gets the a List of interactive {@link ObjectNode} from all {@link Position}s of the region where objects are applied.
	 * @return A {@link ObjectList}.
	 */
	public ObjectList<ObjectNode> getInteractiveObjects() {
		ObjectList<ObjectNode> all = new ObjectArrayList<>();
		objects.forEach((l, c) -> all.addAll(c.getInteract()));
		return all;
	}
	
	/**
	 * Gets the a List of static {@link ObjectNode} of interactive from all {@link Position}s of the region where objects are applied.
	 * @return A {@link ObjectList}.
	 */
	public ObjectList<ObjectNode> getStaticObjects() {
		ObjectList<ObjectNode> all = new ObjectArrayList<>();
		objects.forEach((l, c) -> all.addAll(c.getInteract()));
		return all;
	}
	
	/**
	 * Gets the a List of dynamic {@link ObjectNode} of interactive from all {@link Position}s of the region where objects are applied.
	 * @return A {@link ObjectList}.
	 */
	public ObjectList<ObjectNode> getDynamicObjects() {
		ObjectList<ObjectNode> all = new ObjectArrayList<>();
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
	 * Gets the a {@link ObjectList} of {@link ObjectNode}s that are removed by the server during game time.
	 * @return A {@link ObjectList of {@link ObjectNode}s that are removed.
	 */
	public ObjectList<ObjectNode> getRemovedObjects() {
		return removeObjects;
	}
	
}