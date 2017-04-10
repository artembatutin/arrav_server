package net.edge.world.model.node.region;

/**
 * Represents a single regional tile.
 * @author Graham
 * @author Hadyn Richard
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class RegionTile {
	
	/**
	 * The map flag which represents a clipped tile.
	 */
	public static final int FLAG_BLOCKED = 0x1;
	
	/**
	 * The map flag which represents a bridge tile.
	 */
	public static final int FLAG_BRIDGE = 0x2;
	
	/**
	 * The flags for this tile.
	 */
	private int flags;
	
	/**
	 * Constructs a new, normal {@link RegionTile}
	 */
	public RegionTile() {
		this(TraversalConstants.NONE);
	}
	
	/**
	 * Constructs a new {@link RegionTile} with the specified flags.
	 * @param flags The flags this tile contains.
	 */
	public RegionTile(int flags) {
		this.flags = flags;
	}
	
	/**
	 * Sets the specified flag to this tile.
	 * @param flag The flag to set.
	 */
	public void set(int flag) {
		flags |= flag;
	}
	
	/**
	 * Unsets the specified flag from this tile.
	 * @param flag The flag to unset.
	 */
	public void unset(int flag) {
		flags &= ~flag;
	}
	
	/**
	 * Checks whether or not a single flag is active on this tile.
	 * @param flag The flag to check.
	 * @return <code>true</code> if the specified flag is active on this tile
	 * otherwise <code>false</code>.
	 */
	public boolean isActive(int flag) {
		return (flags & flag) != 0;
	}
	
	/**
	 * Checks whether or not a single flag is inactive on this tile.
	 * @param flag The flag to check.
	 * @return <code>true</code> if the specified flag is inactive on this tile
	 * otherwise <code>false</code>.
	 */
	public boolean isInactive(int flag) {
		return (flags & flag) == 0;
	}
	
	/**
	 * Returns the flags for this tile.
	 */
	public int getFlags() {
		return flags;
	}
	
	@Override
	public int hashCode() {
		return flags;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof RegionTile)) {
			return false;
		}
		RegionTile other = (RegionTile) obj;
		return flags == other.getFlags();
	}
	
}