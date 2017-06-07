package net.edge.locale.area;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.edge.locale.Position;
import net.edge.world.node.Node;
import java.util.Optional;

/**
 * The manager class which can be used to check if certain nodes are in an area.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AreaManager {
	
	/**
	 * The mapping of all the areas on the world.
	 */
	private static final Object2ObjectArrayMap<String, Area> AREAS = new Object2ObjectArrayMap<>();
	
	/**
	 * Checks if the specified {@code node} is in the area specified {@code area}.
	 * @param node the node to check for.
	 * @param area the area to check the node for.
	 * @return <true> if the node is, <false> otherwise.
	 */
	public boolean inArea(Node node, String area) {
		return inArea(node.getPosition(), area);
	}
	
	/**
	 * Checks if the specified {@code node} is in the area specified {@code area}.
	 * @param pos  the position to check for.
	 * @param area the area to check the node for.
	 * @return <true> if the node is, <false> otherwise.
	 */
	public boolean inArea(Position pos, String area) {
		return AREAS.containsKey(area) && AREAS.get(area).getAreaLocations().stream().anyMatch(loc -> loc.getLocation().inLocation(pos));
	}
	
	/**
	 * Gets the area the node is in.
	 * @param node the node to get the area for.
	 * @return the area wrapped in an optional, {@link Optional#empty()} otherwise.
	 */
	public Optional<Area> getArea(Node node) {
		Optional<Area> area = Optional.empty();
		for(Area ar : AREAS.values()) {
			for(Area.AreaLocation att : ar.getAreaLocations()) {
				if(att.getLocation().inLocation(node.getPosition())) {
					area = Optional.of(ar);
				}
			}
		}
		return area;
	}
	
	/**
	 * Checks if the specified {@code node} is in a multi-area.
	 * @param node the node to check for.
	 * @return {@code true} if the node is, {@code false} otherwise.
	 */
	public boolean inMulti(Node node) {
		Optional<Area> area = getArea(node);
		return area.isPresent() && area.get().getAreaLocations().stream().anyMatch(loc -> loc.isMulti() && loc.getLocation().inLocation(node.getPosition()));
		
	}
	
	/**
	 * Gets the mapping which contains all areas loaded on startup.
	 * @return the tool.mapviewer which contains all areas.
	 */
	public Object2ObjectArrayMap<String, Area> getAreas() {
		return AREAS;
	}
}
