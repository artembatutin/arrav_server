package net.edge.world.node.region;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.*;
import net.edge.locale.Position;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import java.util.function.Consumer;

/**
 * Manages all of the cached {@link Region}s and the {@link EntityNode}s contained within them.
 * @author lare96 <http://github.org/lare96>
 * @author Graham
 */
public final class RegionManager {
	
	/**
	 * The map of cached {@link Region}s.
	 */
	private final Region[] regions = new Region[30000];
	
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
		if(regionId < 0 || regionId >= regions.length)
			return null;
		if(regions[regionId] == null)
			regions[regionId] = new Region(regionId);
		return regions[regionId];
	}
	
	/**
	 * Determines if a {@link Region} exists in accordance with {@code pos}.
	 * @param pos The position.
	 * @return {@code true} if a {@code Region} exists, {@code false} otherwise.
	 */
	public boolean exists(Position pos) {
		return exists(pos.getRegion());
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
	
	public Region[] getRegions() {
		return regions;
	}
	
}