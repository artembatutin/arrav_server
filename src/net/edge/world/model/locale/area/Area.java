package net.edge.world.model.locale.area;

import net.edge.world.model.locale.Location;

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
	private final List<AreaLocation> location;

	/**
	 * Constructs a new {@link Area}.
	 * @param name     {@link #name}.
	 * @param location {@link #location}.
	 */
	public Area(String name, List<AreaLocation> location) {
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
		 * Determines if this area is multi.
		 */
		private final boolean multi;

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
		 * @param name     {@link #name}.
		 * @param location {@link #location}.
		 * @param multi    {@link #multi}.
		 * @param teleport {@link #teleport}.
		 * @param summon   {@link #summon}.
		 */
		public AreaLocation(Location location, boolean multi, boolean teleport, boolean summon) {
			this.location = location;
			this.multi = multi;
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
		 * @return the multi
		 */
		public boolean isMulti() {
			return multi;
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
