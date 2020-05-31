package net.arrav.world.entity.region;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.net.packet.out.SendItemNode;
import net.arrav.net.packet.out.SendItemNodeRemoval;
import net.arrav.task.Task;
import net.arrav.util.LoggerUtils;
import net.arrav.util.Utility;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.entity.Entity;
import net.arrav.world.entity.EntityState;
import net.arrav.world.entity.EntityType;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.GroundItem;
import net.arrav.world.entity.item.GroundItemState;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.entity.object.ObjectType;
import net.arrav.world.locale.Position;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static net.arrav.world.entity.EntityState.ACTIVE;
import static net.arrav.world.entity.EntityState.INACTIVE;

/**
 * A location on the tool.mapviewer that is {@code 64x64} in size. Used primarily for caching various types of {@link Entity}s and
 * {@link RegionTile}s. There is a reason that the {@link Entity}s are not being cached together.
 * @author Artem Batutin
 */
public final class Region extends Entity {
	
	/**
	 * The {@link Logger} instance to log our global changes.
	 */
	private static final Logger LOGGER = LoggerUtils.getLogger(Region.class);
	
	/**
	 * A {@link ObjectList} of all active {@link Region}s in the world.
	 */
	private static final ObjectList<Region> ACTIVE_REGIONS = new ObjectArrayList<>();
	
	/**
	 * The size of a region.
	 */
	private static final int REGION_SIZE = 64;
	
	/**
	 * The maximum level a floor can be.
	 */
	private static final int MAXIMUM_HEIGHT_LEVEL = 4;
	
	/**
	 * A {@link ObjectList} of {@link GroundItem}s in this {@code Region}.
	 */
	private final ObjectArrayList<GroundItem> items = new ObjectArrayList<>();
	
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
	
	/**
	 * A list of all surrounding regions.
	 */
	private ObjectList<Region> surroundingRegions = null;
	
	/**
	 * The Id of this {@link Region}.
	 */
	private final int regionId;
	
	/**
	 * An task for regional item sequencing.
	 */
	private Optional<Task> itemTask = Optional.empty();
	
	/**
	 * The tiles within the region(regional clipping).
	 */
	private RegionTile[][] tiles;
	
	/**
	 * A saved region manager instance.
	 */
	private RegionManager manager;
	
	/**
	 * A simple integer acting as a clean up timer for this region.
	 * We randomize it so all regions clean at their own pace.
	 */
	private int cleanup = RandomUtils.inclusive(0, 400);
	
	/**
	 * Creates a new {@link Region}.
	 */
	public Region(int regionId) {
		super(new Position((regionId >> 8) << 6, (regionId & 0xFF) << 6), EntityType.REGION);
		this.regionId = regionId;
	}
	
	/**
	 * Creates a new {@link Region}.
	 */
	public Region(int regionId, RegionManager manager) {
		super(new Position((regionId >> 8) << 6, (regionId & 0xFF) << 6), EntityType.REGION);
		this.regionId = regionId;
		this.manager = manager;
	}
	
	/**
	 * Cleaning up all empty-from-players regions.
	 */
	public static void cleanup() {
		Iterator<Region> it = ACTIVE_REGIONS.iterator();
		while(it.hasNext()) {
			Region r = it.next();
			r.cleanup++;
			//cleaning only when the cleanup time has come.
			if(r.cleanup > 500) {
				if(r.getState() == EntityState.ACTIVE && r.getPlayers().isEmpty()) {
					boolean playersNearby = r.playersAround();
					if(!playersNearby) {
						r.setState(EntityState.INACTIVE);
						it.remove();
					}
				}
				r.cleanup = 0;
			}
		}
	}
	
	@Override
	public void register() {
		ACTIVE_REGIONS.add(this);
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
			if(item.getItemState() == GroundItemState.HIDDEN || item.getState() != ACTIVE)
				continue;
			if(item.getPosition() == null)
				continue;
			if(item.getInstance() != player.getInstance())
				continue;
			player.out(new SendItemNodeRemoval(item));
			if(item.getPosition().withinDistance(player.getPosition(), 60)) {
				if(item.getItemState() == GroundItemState.SEEN_BY_EVERYONE) {
					player.out(new SendItemNode(item));
				} else if(item.getPlayer().same(player) && item.getItemState() == GroundItemState.SEEN_BY_OWNER) {
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
	public <T extends Actor> boolean add(T e) {
		if(e.getState() == INACTIVE)
			return false;
		if(e.isPlayer()) {
			return players.add(e.toPlayer());
		} else {
			return mobs.add(e.toMob());
		}
	}
	
	/**
	 * Removes an {@link Actor} from the backing queue.
	 * @param e The entity to remove.
	 */
	public <T extends Actor> boolean remove(T e) {
		if(e.isPlayer()) {
			return players.remove(e.toPlayer());
		} else {
			return mobs.remove(e.toMob());
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
	 * @param item the item to attempt to register.
	 * @param stack if the item should stack upon registration.
	 * @return {@code true} if the item was registered, {@code false} otherwise.
	 */
	public synchronized boolean register(GroundItem item, boolean stack) {
		if(item.getState() != EntityState.IDLE)
			return false;
		//sending the task sequencing if none.
		if(!itemTask.isPresent()) {
			itemTask = Optional.of(new RegionItemTask(this));
			itemTask.get().submit();
		}
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
			item.setState(ACTIVE);
			items.add(item);
			return true;
		}
		if(item.getItem().getDefinition().isStackable()) {
			item.setState(ACTIVE);
			items.add(item);
			return true;
		}
		int amount = item.getItem().getAmount();
		item.getItem().setAmount(1);
		for(int i = 0; i < amount; i++) {
			item.setState(ACTIVE);
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
	public synchronized boolean unregister(GroundItem item) {
		if(item.getState() != ACTIVE)
			return false;
		if(items.remove(item)) {
			item.setState(INACTIVE);
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the item node list.
	 * @return item nodes.
	 */
	public ObjectArrayList<GroundItem> getItems() {
		return items;
		/*ObjectArrayList<GroundItem> instancedItems = new ObjectArrayList<>();
		for(GroundItem item : items) {
			if(item.getInstance() == Instance.NORMAL)
				instancedItems.add(item);
		}
		return instancedItems;*/
	}

	/**
	 * Gets item nodes with a specific {@link Instance} property.
	 * @param instance the instance the ground items are on.
	 * @return item nodes.
	 */
	/*public ObjectArrayList<GroundItem> getItemsInstance(Instance instance) {
		ObjectArrayList<GroundItem> instancedItems = new ObjectArrayList<>();
		for(GroundItem item : items) {
			if(item.getInstance() == instance)
				instancedItems.add(item);
		}
		return instancedItems;
	}*/


	
	/**
	 * The method that retrieves the item with {@code id} on {@code position}.
	 * @param id the identifier to retrieve the item with.
	 * @param position the position to retrieve the item on.
	 * @return the item instance wrapped in an optional, or an empty optional if
	 * no item is found.
	 */
	public Optional<GroundItem> getItem(int id, Position position) {
		if(items.isEmpty())
			return Optional.empty();
		for(GroundItem i : items) {
			if(i.getItem().getId() == id && i.getItemState() != GroundItemState.HIDDEN && i.getState() == ACTIVE && i.getPosition().same(position)) {
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
	 * @param id The id of the object to seek for.
	 * @param position The position.
	 * @return A {@link Optional} of {@link GameObject} on the specified position.
	 */
	public Optional<GameObject> getObject(int id, Position position) {
		return getObject(id, position.toLocalPacked());
	}
	
	/**
	 * Gets the an {@link Optional} of {@link GameObject}s on the specified {@link Position} with the specified {@code type}.
	 * @param type The type of the object to seek for.
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
	 * @param id The id of the object to seek for.
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
	 * Setting a new {@link #itemTask}.
	 * @param itemTask item task to set.
	 */
	public void setItemTask(Optional<Task> itemTask) {
		this.itemTask = itemTask;
	}
	
	/**
	 * Finds if the two position with sizes are reachable.
	 * @param source source pos.
	 * @param sourceWidth source width.
	 * @param sourceLength source length.
	 * @param target target pos.
	 * @param targetWidth target width.
	 * @param targetLength target length.
	 * @return true if reachable.
	 */
	public static boolean reachable(Position source, int sourceWidth, int sourceLength, Position target, int targetWidth, int targetLength) {
		if(Utility.inside(source, sourceWidth, sourceLength, target, targetWidth, targetLength)) {
			return true;//targetWidth == 0 || targetLength == 0;
		}
		
		int x, y;
		int sourceTopX = source.getX() + sourceWidth - 1;
		int sourceTopY = source.getY() + sourceLength - 1;
		int targetTopX = target.getX() + targetWidth - 1;
		int targetTopY = target.getY() + targetLength - 1;
		
		if(sourceTopY == target.getY() - 1 && source.getX() >= target.getX() && source.getX() <= targetTopX) {
			for(x = 0, y = sourceLength - 1; x < sourceWidth; x++)
				if(TraversalMap.blockedNorth(source.move(x, y)))
					return false;
			return true;
		}
		
		if(sourceTopX == target.getX() - 1 && source.getY() >= target.getY() && source.getY() <= targetTopY) {
			for(x = 0, y = 0; y < sourceLength; y++)
				if(TraversalMap.blockedEast(source.move(x, y)))
					return false;
			return true;
		}
		
		if(source.getY() == targetTopY + 1 && source.getX() >= target.getX() && source.getX() <= targetTopX) {
			for(x = 0, y = 0; x < sourceWidth; x++)
				if(TraversalMap.blockedSouth(source.move(x, y)))
					return false;
			return true;
		}
		
		if(source.getX() == targetTopX + 1 && source.getY() >= target.getY() && source.getY() <= targetTopY) {
			for(x = sourceWidth - 1, y = 0; y < sourceLength; y++)
				if(TraversalMap.blockedWest(source.move(x, y)))
					return false;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determines if there any player around this region.
	 * @return {@code true} if there is a player around regionally, {@code false} otherwise.
	 */
	boolean playersAround() {
		ObjectList<Region> surrounding = getSurroundingRegions();
		if(surrounding != null) {
			for(Region r : surrounding) {
				if(r == null)
					continue;
				if(!r.getPlayers().isEmpty())
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets a single tile in this region from the specified height, x and y
	 * coordinates.
	 * @param height The height.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The tile in this region for the specified attributes.
	 */
	public RegionTile getTile(int height, int x, int y) {
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
	
	/**
	 * Gets the a {@link ObjectList} of {@link GameObject}s that are removed by the server during game time.
	 * @return A {@link ObjectList of {@link GameObject }s that are removed.
	 */
	public ObjectList<GameObject> getRemovedObjects() {
		return removeObjects;
	}
	
	/**
	 * Gets the surrounding regions.
	 * @return surrounding regions.
	 */
	public ObjectList<Region> getSurroundingRegions() {
		if(manager == null)
			return null;
		if(surroundingRegions == null) {
			ObjectList<Region> regions = new ObjectArrayList<>();
			Region reg = manager.getRegion(regionId);
			if(reg != null)
				regions.add(reg);
			reg = manager.getRegion(regionId + 256);
			if(reg != null)
				regions.add(reg);
			reg = manager.getRegion(regionId - 256);
			if(reg != null)
				regions.add(reg);
			reg = manager.getRegion(regionId + 1);
			if(reg != null)
				regions.add(reg);
			reg = manager.getRegion(regionId - 1);
			if(reg != null)
				regions.add(reg);
			reg = manager.getRegion(regionId + 257);
			if(reg != null)
				regions.add(reg);
			reg = manager.getRegion(regionId - 255);
			if(reg != null)
				regions.add(reg);
			reg = manager.getRegion(regionId + 255);
			if(reg != null)
				regions.add(reg);
			reg = manager.getRegion(regionId - 257);
			if(reg != null)
				regions.add(reg);
			surroundingRegions = regions;
		}
		return surroundingRegions;
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