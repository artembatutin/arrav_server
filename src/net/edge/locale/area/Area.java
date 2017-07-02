package net.edge.locale.area;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.locale.loc.Location;

import java.util.List;

/**
 * Represents a single area.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Area {

	/**
	 * The name for this area.
	 */
	private final String name;

	/**
	 * The locations for this area.
	 */
	private final ObjectList<AreaLocation> location;

	/**
	 * Constructs a new {@link Area}.
	 * @param name     {@link #name}.
	 * @param location {@link #location}.
	 */
	public Area(String name, ObjectList<AreaLocation> location) {
		this.name = name;
		this.location = location;
	}

	/**
	 * The class which represents a location inside an area with a set of attributes.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static final class AreaLocation {

		/**
		 * The location for this area.
		 */
		private final Location location;

		/**
		 * Determines if players can teleport in this area.
		 */
		private final boolean teleport;

		/**
		 * Determines if players can summon familiars in this area.
		 */
		private final boolean summon;

		/**
		 * Constructs a new {@link AreaLocation}.
		 * @param location {@link #location}.
		 * @param teleport {@link #teleport}.
		 * @param summon   {@link #summon}.
		 */
		public AreaLocation(Location location, boolean teleport, boolean summon) {
			this.location = location;
			this.teleport = teleport;
			this.summon = summon;
		}

		/**
		 * @return the location
		 */
		public Location getLocation() {
			return location;
		}

		/**
		 * @return the teleport
		 */
		public boolean isTeleport() {
			return teleport;
		}

		/**
		 * @return the summon
		 */
		public boolean isSummon() {
			return summon;
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the location
	 */
	public List<AreaLocation> getAreaLocations() {
		return location;
	}
}
