package net.edge.world.entity.region;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.net.packet.out.SendItemNode;
import net.edge.net.packet.out.SendItemNodeRemoval;
import net.edge.util.LoggerUtils;
import net.edge.util.rand.RandomUtils;
import net.edge.world.locale.Position;
import net.edge.world.World;
import net.edge.world.entity.Entity;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.EntityType;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.GroundItemState;
import net.edge.world.object.GameObject;
import net.edge.world.object.ObjectType;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * A location on the tool.mapviewer that is {@code 64x64} in size. Used primarily for caching various types of {@link Entity}s and
 * {@link RegionTile}s. There is a reason that the {@link Entity}s are not being cached together.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class Region extends Entity {
	
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
	 * A {@link ObjectList} of {@link GroundItem}s in this {@code Region}.
	 */
	private final ObjectList<GroundItem> items = new ObjectArrayList<>();
	
	/**
	 * A {@link Set} of active {@link Player}s in this {@code Region}.
	 */
	private final Set<Player> players = Sets.newConcurrentHashSet();
	
	/**
	 * A {@link Set} of active {@link Mob}s in this {@code Region}.
	 */
	private final Set<Mob> mobs = Sets.newConcurrentHashSet();
	
	/**
	 * A {@link Int2ObjectOpenHashMap} of active {@link GameObject}s in this {@code Region}.
	 */
	private final Int2ObjectOpenHashMap<RegionTiledObjects> staticObjects = new Int2ObjectOpenHashMap<>();
	
	/**
	 * A {@link ObjectList} of removed {@link GameObject}s in this {@code Region}.
	 */
	private final ObjectList<GameObject> removeObjects = new ObjectArrayList<>();

	private ObjectList<Region> surroundingRegions = null;
	
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

	private RegionManager manager;
	
	/**
	 * Creates a new {@link Region}.
	 * @param regionId The id of this region.
	 */
	Region(int regionId, RegionManager manager) {
		super(new Position((regionId >> 8) << 6, (regionId & 0xFF) << 6), EntityType.REGION);
		this.regionId = regionId;
		this.manager = manager;
	}

	public ObjectList<Region> getSurroundingRegions() {
		if (surroundingRegions == null) {
			ObjectList<Region> regions = new ObjectArrayList<>();
			regions.add(manager.getRegion(regionId));
			if (manager.exists(regionId + 256))
				regions.add(manager.getRegion(regionId + 256));
			if (manager.exists(regionId - 256))
				regions.add(manager.getRegion(regionId - 256));
			if (manager.exists(regionId + 1))
				regions.add(manager.getRegion(regionId + 1));
			if (manager.exists(regionId - 1))
				regions.add(manager.getRegion(regionId - 1));
			if (manager.exists(regionId + 257))
				regions.add(manager.getRegion(regionId + 257));
			if (manager.exists(regionId - 255))
				regions.add(manager.getRegion(regionId - 255));
			if (manager.exists(regionId + 255))
				regions.add(manager.getRegion(regionId + 255));
			if (manager.exists(regionId - 257))
				regions.add(manager.getRegion(regionId - 257));
			surroundingRegions = regions;
		}
		return surroundingRegions;
	}
	
	@Override
	public void update() {
		//cleanup timer increased.
		cleanup++;
		
		//sequencing active item nodes.
		if(!items.isEmpty()) {
			Iterator<GroundItem> it = items.iterator();
			while(it.hasNext()) {
				GroundItem item = it.next();
				if(item.getCounter().incrementAndGet(10) >= SEQUENCE_TICKS) {
					item.onSequence();
					item.getCounter().set(0);
				}
				if(item.getState() != EntityState.ACTIVE) {
					item.dispose();
					it.remove();
				}
			}
		}
		
		//cleaning only when the cleanup time has come.
		if(cleanup > 500) {
			if(getState() == EntityState.ACTIVE && getPlayers().isEmpty()) {
				boolean playersNearby = playersAround();
				if(!playersNearby) {
					setState(EntityState.INACTIVE);
				}
			}
			cleanup = 0;
		}
	}
	
	@Override
	public void register() {
		//activating all mobs.
		for(Mob n : mobs) {
			n.setActive(true);
		}
		LOGGER.info("Loaded Region: [" + regionId + "] on the fly.");
	}
	
	@Override
	public void dispose() {
		//deactivating all mobs.
		for(Mob n : mobs) {
			n.setActive(false);
		}
		LOGGER.info("Disposed Region: [" + regionId + "] on the fly.");
	}
	
	/**
	 * The method that updates all items in the region for {@code player}.
	 * @param player the player to update items for.
	 */
	public void onEnter(Player player) {
		for(GroundItem item : items) {
			if(item.getItemState() == GroundItemState.HIDDEN || item.getState() != EntityState.ACTIVE)
				continue;
			if(item.getPosition() == null)
				continue;
			if(item.getInstance() != player.getInstance())
				continue;
			player.out(new SendItemNodeRemoval(item));
			if(item.getPosition().withinDistance(player.getPosition(), 60)) {
				if(item.getPlayer() == null && item.getItemState() == GroundItemState.SEEN_BY_EVERYONE) {
					player.out(new SendItemNode(item));
					continue;
				}
				if(item.getPlayer().same(player) && item.getItemState() == GroundItemState.SEEN_BY_OWNER) {
					player.out(new SendItemNode(item));
				}
			}
		}
	}
	
	/**
	 * Adds an {@link Actor} to the backing queue.
	 * @param e The entity to add.
	 * @return {@code true} if it was added successfully, otherwise {@code false}.
	 */
	public <T extends Actor> boolean addChar(T e) {
		if(e.getState() == EntityState.INACTIVE)
			return false;
		if(e.isPlayer()) {
			return players.add(e.toPlayer());
		} else {
			return mobs.add(e.toNpc());
		}
	}
	
	/**
	 * Removes an {@link Actor} from the backing queue.
	 * @param e The entity to remove.
	 */
	public <T extends Actor> boolean removeChar(T e) {
		if(e.isPlayer()) {
			return players.remove(e.toPlayer());
		} else {
			return mobs.remove(e.toNpc());
		}
	}
	
	/**
	 * Retrieves and returns an {@link Set} of {@link Player}s.
	 * @return all the players inside the region.
	 */
	public Set<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Retrieves and returns an {@link Set} of {@link Mob}s.
	 * @return all the mobs inside the region.
	 */
	public Set<Mob> getMobs() {
		return mobs;
	}
	
	/**
	 * The method that attempts to register {@code item} and does not stack by
	 * default.
	 * @param item the item to attempt to register.
	 * @return {@code true} if the item was registered, {@code false} otherwise.
	 */
	public boolean register(GroundItem item) {
		return register(item, false);
	}
	
	/**
	 * The method that attempts to register {@code item}.
	 * @param item  the item to attempt to register.
	 * @param stack if the item should stack upon registration.
	 * @return {@code true} if the item was registered, {@code false} otherwise.
	 */
	public boolean register(GroundItem item, boolean stack) {
		if(item.getState() != EntityState.IDLE)
			return false;
		if(stack) {
			for(GroundItem next : items) {
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
			item.setState(EntityState.ACTIVE);
			items.add(item);
			return true;
		}
		if(item.getItem().getDefinition().isStackable()) {
			item.setState(EntityState.ACTIVE);
			items.add(item);
			return true;
		}
		int amount = item.getItem().getAmount();
		item.getItem().setAmount(1);
		for(int i = 0; i < amount; i++) {
			item.setState(EntityState.ACTIVE);
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
	public boolean unregister(GroundItem item) {
		if (item.getState() != EntityState.ACTIVE)
			return false;
		if (items.remove(item)) {
			item.setState(EntityState.INACTIVE);
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
	public Optional<GroundItem> getItem(int id, Position position) {
		for(GroundItem i : items) {
			if(i.getItem().getId() == id && i.getItemState() != GroundItemState.HIDDEN && i.getState() == EntityState.ACTIVE && i.getPosition().same(position)) {
				return Optional.of(i);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Adds an {@link GameObject} to the backing set.
	 * @param o The object to add.
	 */
	public void addObj(GameObject o) {
		getObjectsFrompacked(o.getLocalPos()).add(o);
	}
	
	/**
	 * Removes an {@link GameObject} from the backing set.
	 * @param o The object to remove.
	 */
	public void removeObj(GameObject o) {
		getObjectsFrompacked(o.getLocalPos()).remove(o);
	}
	
	/**
	 * Gets the a set of {@link GameObject} on the specified {@link Position}.
	 * @param position The position.
	 * @return A {@link RegionTiledObjects} of {@link GameObject}s on the specified position.
	 */
	public RegionTiledObjects getObjects(Position position) {
		return staticObjects.computeIfAbsent(position.toLocalPacked(), key -> new RegionTiledObjects());
	}
	
	/**
	 * Gets the a set of {@link GameObject} on the packed coordinates.
	 * @param packed The packed position.
	 * @return A {@link RegionTiledObjects} of {@link GameObject}s on the specified position.
	 */
	public RegionTiledObjects getObjectsFrompacked(int packed) {
		return staticObjects.computeIfAbsent(packed, key -> new RegionTiledObjects());
	}
	
	/**
	 * Gets the an {@link Optional} of {@link GameObject}s on the specified {@link Position} with the specified {@code id}.
	 * @param id       The id of the object to seek for.
	 * @param position The position.
	 * @return A {@link Optional} of {@link GameObject} on the specified position.
	 */
	public Optional<GameObject> getObject(int id, Position position) {
		return getObject(id, position.toLocalPacked());
	}
	
	/**
	 * Gets the an {@link Optional} of {@link GameObject}s on the specified {@link Position} with the specified {@code type}.
	 * @param type       The type of the object to seek for.
	 * @param position The position.
	 * @return A {@link Optional} of {@link GameObject} on the specified position.
	 */
	public Optional<GameObject> getObject(ObjectType type, Position position) {
		RegionTiledObjects tile = getObjectsFrompacked(position.toLocalPacked());
		GameObject o = tile.getType(type);
		if(o != null)
			return Optional.of(o);
		return Optional.empty();
	}
	
	/**
	 * Gets the an {@link Optional} of {@link GameObject}s with the specified {@code id} and {@code packed} coordinates.
	 * @param id       The id of the object to seek for.
	 * @param packed The packed position.
	 * @return A {@link Optional} of {@link GameObject}.
	 */
	public Optional<GameObject> getObject(int id, int packed) {
		RegionTiledObjects tile = getObjectsFrompacked(packed);
		GameObject o = tile.getId(id);
		if(o != null)
			return Optional.of(o);
		return Optional.empty();
	}
	
	/**
	 * Sends an action to interactive objects with the specified {@code id}.
	 * @param id The id of the object to seek for.
	 */
	public void interactAction(int id, Consumer<GameObject> action) {
		staticObjects.forEach((l, c) -> c.interactiveAction(id, action));
	}
	
	/**
	 * Gets the a List of dynamic {@link GameObject} of interactive from all {@link Position}s of the region where staticObjects are applied.
	 * @return A {@link ObjectList}.
	 */
	public void dynamicAction(Consumer<GameObject> action) {
		staticObjects.forEach((l, c) -> c.dynamicAction(action));
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
	 * Gets the a {@link ObjectList} of {@link GameObject}s that are removed by the server during game time.
	 * @return A {@link ObjectList of {@link GameObject }s that are removed.
	 */
	public ObjectList<GameObject> getRemovedObjects() {
		return removeObjects;
	}
	
}