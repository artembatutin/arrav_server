package com.rageps.world.entity.region;

import com.rageps.net.packet.out.SendObjectRemoval;
import com.rageps.net.refactor.packet.out.model.RemoveObjectPacket;
import com.rageps.world.locale.Position;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import com.rageps.net.packet.out.SendObject;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;

/**
 * Manages all of the cached {@link Region}s and the {@link Actor}s contained within them.
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
		if(regionId < 0 || regionId >= regions.length || !exists(regionId))
			return null;
		if(regions[regionId] == null)
			regions[regionId] = new Region(regionId, this);
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
		if(allRegions != null) {
			for(Region region : allRegions) {
				localPlayers.addAll(region.getPlayers());
			}
		}
		return localPlayers;
	}
	
	/**
	 * The method that updates all objects in the region for {@code player}.
	 * @param player the player to update objects for.
	 */
	public void updateRegionObjects(Player player) {
		ObjectList<Region> allRegions = getAllSurroundingRegions(player.getPosition().getRegion());
		if(allRegions != null) {
			for(Region region : allRegions) {
				if(!region.getRemovedObjects().isEmpty()) {
					for(GameObject obj : region.getRemovedObjects()) {
						player.send(new RemoveObjectPacket(obj));

					}
				}
				region.dynamicAction(o -> {
					if(o.getZ() == player.getPosition().getZ() && o.getInstance() == player.getInstance()) {
						player.out(new SendObject(o));
					}
				});
			}
		}
	}
	
	/**
	 * Gets all the {@link Region}s surrounding {@code pos}.
	 * @param region The region id to get surrounding for.
	 * @return The surrounding regions.
	 */
	public ObjectList<Region> getAllSurroundingRegions(int region) {
		Region r = getRegion(region);
		if(r != null)
			return r.getSurroundingRegions();
		return null;
	}
	
	public Region[] getRegions() {
		return regions;
	}
	
}