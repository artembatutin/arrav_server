package com.rageps.net.refactor.packet.out.model;


import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.locale.Position;

/**
 * A {@link Packet} sent to the client to remove an object from a tile.
 *
 * @author Major
 */
public final class RemoveObjectPacket extends Packet {

	/**
	 * The orientation of the object.
	 */
	private final int orientation;

	/**
	 * The position of the object.
	 */
	private final Position positionOffset;

	/**
	 * The type of the object.
	 */
	private final int type;

	/**
	 * Creates the RemoveObjectMessage.
	 *
	 * @param object The {@link GameObject} to send.
	 */
	public RemoveObjectPacket(GameObject object) {
		this.positionOffset = object.getPosition();
		type = object.getObjectType().getId();
		orientation = object.getDirection().getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RemoveObjectPacket) {
			RemoveObjectPacket other = (RemoveObjectPacket) obj;
			return type == other.type && orientation == other.orientation && positionOffset.equals(other.positionOffset);
		}

		return false;
	}

	/**
	 * Gets the orientation of the object.
	 *
	 * @return The orientation.
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * Gets the position offset of the object.
	 *
	 * @return The position offset.
	 */
	public Position getPositionOffset() {
		return positionOffset;
	}

	/**
	 * Gets the type of the object.
	 *
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime * positionOffset.getX() + orientation;
		return prime * result + type;
	}
}