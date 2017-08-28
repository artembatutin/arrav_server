package net.edge.world.locale.area;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.world.entity.Entity;
import net.edge.world.locale.Position;
import net.edge.world.locale.area.Area.AreaLocation;
import net.edge.world.locale.loc.Location;

/**
 * The manager class which can be used to check if certain nodes are in an area.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AreaManager {
	
	/**
	 * This world's {@link AreaManager} used to handle to check if certain areas have permissions.
	 */
	private static final AreaManager AREA_MANAGER = new AreaManager();
	
	/**
	 * The mapping of all the areas in the world.
	 */
	private static final Object2ObjectArrayMap<String, Area> AREAS = new Object2ObjectArrayMap<>();
	
	/**
	 * The list of all the multi zones in the world.
	 */
	private static final ObjectList<Location> MULTI_ZONES = new ObjectArrayList<>();
	
	/**
	 * Checks if the specified {@code entity} is in the area specified {@code area}.
	 * @param entity the entity to check for.
	 * @param area   the area to check the entity for.
	 * @return <true> if the entity is, <false> otherwise.
	 */
	public boolean inArea(Entity entity, String area) {
		return inArea(entity.getPosition(), area);
	}
	
	/**
	 * Checks if the specified {@code node} is in the area specified {@code area}.
	 * @param pos  the position to check for.
	 * @param area the area to check the node for.
	 * @return <true> if the node is, <false> otherwise.
	 */
	public boolean inArea(Position pos, String area) {
		if(!AREAS.containsKey(area))
			return false;
		for(AreaLocation loc : AREAS.get(area).getAreaLocations()) {
			if(loc.getLocation().inLocation(pos))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if the specified {@code entity} is in a multi-area.
	 * @param entity the entity to check for.
	 * @return {@code true} if the entity is, {@code false} otherwise.
	 */
	public boolean inMulti(Entity entity) {
		for(Location zone : MULTI_ZONES) {
			if(zone.inLocation(entity.getPosition()))
				return true;
		}
		return false;
		
	}
	
	/**
	 * Gets the mapping which contains all areas loaded on startup.
	 * @return the map which contains all areas.
	 */
	public Object2ObjectArrayMap<String, Area> getAreas() {
		return AREAS;
	}
	
	/**
	 * Gets the list of all multi zones loaded on startup.
	 * @return the list which contains all multi zones.
	 */
	public ObjectList<Location> getMultiZones() {
		return MULTI_ZONES;
	}
	
	/**
	 * Returns this world'{@link AreaManager}.
	 */
	public static AreaManager get() {
		return AREA_MANAGER;
	}
}
