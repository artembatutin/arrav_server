package net.edge.world.region;

import net.edge.locale.Position;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages all of the cached {@link Region}s and the {@link EntityNode}s contained within them.
 * @author lare96 <http://github.org/lare96>
 * @author Graham
 */
public final class RegionManager {
	
	/**
	 * The tool.mapviewer of cached {@link Region}s.
	 */
	private final Map<Integer, Region> regions = new ConcurrentHashMap<>();
	
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
	public Set<Player> getPriorityPlayers(Player player) {
		List<Region> allRegions = getSurroundingRegions(player.getPosition());
		Set<Player> localPlayers = new TreeSet<>(new RegionPriorityComparator(player));
		
		for(Region region : allRegions) {
			localPlayers.addAll(region.getPlayers().values());
		}
		return localPlayers;
	}
	
	/**
	 * Gets all of the {@link Player}s relevant to a single {@code Position}.
	 * @param pos The {@link Position}.
	 * @return The local, prioritized, {@code Player}s.
	 */
	public Set<Player> getPlayers(Position pos) {
		List<Region> allRegions = getAllSurroundingRegions(pos.getRegion());
		Set<Player> localPlayers = new HashSet<>();
		
		for(Region region : allRegions) {
			localPlayers.addAll(region.getPlayers().values());
		}
		return localPlayers;
	}
	
	/**
	 * Gets all of the {@link Npc}s relevant to {@code player}, prioritized in an order somewhat identical to Runescape. This
	 * is done so that staggered updating does not interfere negatively with gameplay.
	 * @param player The {@link Player}.
	 * @return The local, prioritized, {@code Npc}s.
	 */
	public Set<Npc> getPriorityNpcs(Player player) {
		List<Region> allRegions = getSurroundingRegions(player.getPosition());
		Set<Npc> localNpcs = new TreeSet<>(new RegionPriorityComparator(player));
		
		for(Region region : allRegions) {
			localNpcs.addAll(region.getNpcs().values());
		}
		return localNpcs;
	}
	
	/**
	 * The method that updates all objects in the region for {@code player}.
	 * @param player the player to update objects for.
	 */
	public void updateRegionObjects(Player player) {
		List<Region> allRegions = getAllSurroundingRegions(player.getPosition().getRegion());
		for(Region region : allRegions) {
			region.getRemovedObjects().forEach((p, o) -> {
				for(ObjectNode ob : o)
					player.getMessages().sendRemoveObject(ob);
			});
			Set<ObjectNode> regionObj = region.getDynamicObjects();
			for(ObjectNode o : regionObj) {
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
	public List<Region> getSurroundingRegions(Position pos) {
		int regionId = pos.getRegion();
		
		List<Region> regions = new LinkedList<>();
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
	public List<Region> getAllSurroundingRegions(int region) {
		List<Region> regions = new LinkedList<>();
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
	
	public Map<Integer, Region> getRegions() {
		return regions;
	}
	
}