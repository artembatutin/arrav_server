package net.edge.world.node.region;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.*;
import net.edge.locale.Position;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

/**
 * Manages all of the cached {@link Region}s and the {@link EntityNode}s contained within them.
 * @author lare96 <http://github.org/lare96>
 * @author Graham
 */
public final class RegionManager {
	
	/**
	 * The map of cached {@link Region}s.
	 */
	private final Int2ObjectOpenHashMap<Region> regions = new Int2ObjectOpenHashMap<>();
	
	/**
	 * Returns a {@link Region} based on the given {@code pos}.
	 * @param pos The position.
	 * @return The region in accordance with this {@code pos}.
	 */
	public Region getRegion(Position pos) {
		return getRegion(pos.getRegion());
	}
	
	/**
	 * Returns a {@link Region} based on the given region id, creates and inserts a new {@code Region} if none present.
	 * @param regionId The region id.
	 * @return The region in accordance with {@code coordinates}.
	 */
	public Region getRegion(int regionId) {
		return regions.computeIfAbsent(regionId, Region::new);
	}
	
	/**
	 * Determines if a {@link Region} exists in accordance with {@code pos}.
	 * @param pos The position.
	 * @return {@code true} if a {@code Region} exists, {@code false} otherwise.
	 */
	public boolean exists(Position pos) {
		return regions.containsKey(pos.getRegion());
	}
	
	/**
	 * Determines if a {@link Region} exists in accordance with the regional boundaries.
	 * @param regionId The region id to check for.
	 * @return {@code true} if a {@code Region} exists, {@code false} otherwise.
	 */
	public boolean exists(int regionId) {
		return RegionDefinition.contains(regionId);
	}
	
	/**
	 * Gets all of the {@link Player}s relevant to {@code player}, prioritized in an order somewhat identical to Runescape.
	 * This is done so that staggered updating does not interfere negatively with gameplay.
	 * @param player The {@link Player}.
	 * @return The local, prioritized, {@code Player}s.
	 */
	public ObjectSet<Player> getPriorityPlayers(Player player) {
		ObjectList<Region> allRegions = getSurroundingRegions(player.getPosition());
		ObjectSet<Player> localPlayers = new ObjectAVLTreeSet<>(new RegionPriorityComparator(player));
		for(Region region : allRegions) {
			localPlayers.addAll(region.getPlayers());
		}
		return localPlayers;
	}
	
	/**
	 * Gets all of the {@link Player}s relevant to a single {@code Position}.
	 * @param pos The {@link Position}.
	 * @return The local, prioritized, {@code Player}s.
	 */
	public ObjectSet<Player> getPlayers(Position pos) {
		ObjectList<Region> allRegions = getAllSurroundingRegions(pos.getRegion());
		ObjectSet<Player> localPlayers = new ObjectOpenHashSet<>();
		for(Region region : allRegions) {
			localPlayers.addAll(region.getPlayers());
		}
		return localPlayers;
	}
	
	/**
	 * Gets all of the {@link Npc}s relevant to {@code player}, prioritized in an order somewhat identical to Runescape. This
	 * is done so that staggered updating does not interfere negatively with gameplay.
	 * @param player The {@link Player}.
	 * @return The local, prioritized, {@code Npc}s.
	 */
	public ObjectSet<Npc> getPriorityNpcs(Player player) {
		ObjectList<Region> allRegions = getSurroundingRegions(player.getPosition());
		ObjectSet<Npc> localNpcs = new ObjectAVLTreeSet<>(new RegionPriorityComparator(player));
		for(Region region : allRegions) {
			localNpcs.addAll(region.getNpcs());
		}
		return localNpcs;
	}
	
	/**
	 * The method that updates all objects in the region for {@code player}.
	 * @param player the player to update objects for.
	 */
	public void updateRegionObjects(Player player) {
		ObjectList<Region> allRegions = getAllSurroundingRegions(player.getPosition().getRegion());
		for(Region region : allRegions) {
			for(ObjectNode obj : region.getRemovedObjects())
				player.getMessages().sendRemoveObject(obj);
			for(ObjectNode o : region.getDynamicObjects()) {
				if(o.getZ() == player.getPosition().getZ() && o.getInstance() == player.getInstance())
					player.getMessages().sendObject(o);
			}
		}
	}
	
	/**
	 * Gets the {@link Region}s surrounding {@code pos}.
	 * @param pos The {@link Position}.
	 * @return The surrounding regions.
	 */
	public ObjectList<Region> getSurroundingRegions(Position pos) {
		int regionId = pos.getRegion();
		
		ObjectList<Region> regions = new ObjectArrayList<>();
		regions.add(getRegion(regionId)); // Initial region.
		
		int x = pos.getX() % 64;
		int y = pos.getY() % 64;
		
		if(x >= 16 && x <= 48 && y >= 16 && y <= 48) {
			//in the middle of the region.
			return regions;
		} else if(y > 48) {
			//top part of region.
			if(exists(regionId + 1))
				regions.add(getRegion(regionId + 1));
			if(x > 48) {
				//top-right of region.
				if(exists(regionId + 256))
					regions.add(getRegion(regionId + 256));
				if(exists(regionId + 257))
					regions.add(getRegion(regionId + 257));
			} else if(x < 16) {
				//top-left of region.
				if(exists(regionId - 256))
					regions.add(getRegion(regionId - 256));
				if(exists(regionId - 255))
					regions.add(getRegion(regionId - 255));
			}
		} else if(y < 16) {
			//bottom part of region.
			if(exists(regionId - 1))
				regions.add(getRegion(regionId - 1));
			if(x > 48) {
				//bottom-right of region.
				if(exists(regionId + 256))
					regions.add(getRegion(regionId + 256));
				if(exists(regionId + 255))
					regions.add(getRegion(regionId + 255));
			} else if(x < 16) {
				//bottom-left of region.
				if(exists(regionId - 256))
					regions.add(getRegion(regionId - 256));
				if(exists(regionId - 257))
					regions.add(getRegion(regionId - 257));
			}
		}
		return regions;
	}
	
	/**
	 * Gets all the {@link Region}s surrounding {@code pos}.
	 * @param region The region id to get surrounding for.
	 * @return The surrounding regions.
	 */
	public ObjectList<Region> getAllSurroundingRegions(int region) {
		ObjectList<Region> regions = new ObjectArrayList<>();
		regions.add(getRegion(region));
		if(exists(region + 256))
			regions.add(getRegion(region + 256));
		if(exists(region - 256))
			regions.add(getRegion(region - 256));
		if(exists(region + 1))
			regions.add(getRegion(region + 1));
		if(exists(region - 1))
			regions.add(getRegion(region - 1));
		if(exists(region + 257))
			regions.add(getRegion(region + 257));
		if(exists(region - 255))
			regions.add(getRegion(region - 255));
		if(exists(region + 255))
			regions.add(getRegion(region + 255));
		if(exists(region - 257))
			regions.add(getRegion(region - 257));
		return regions;
	}
	
	public Int2ObjectOpenHashMap<Region> getRegions() {
		return regions;
	}
	
}