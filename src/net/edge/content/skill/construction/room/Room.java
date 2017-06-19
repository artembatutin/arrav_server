package net.edge.content.skill.construction.room;

/**
 * Represents a single room.
 */
public class Room {
	
	private final RoomData data;
	private int rotation, theme;
	
	public Room(RoomData data, int rotation, int theme) {
		this.rotation = rotation;
		this.theme = theme;
		this.data = data;
	}
	
	public RoomData data() {
		return data;
	}
	
	public int getX() {
		return data.getX();
	}
	
	public int getY() {
		return data.getY();
	}
	
	public int getZ() {
		return theme;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	public boolean[] getDoors() {
		return data.getRotatedDoors(rotation);
	}
}