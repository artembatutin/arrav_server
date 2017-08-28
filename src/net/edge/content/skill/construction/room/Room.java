package net.edge.content.skill.construction.room;

/**
 * Represents a single room.
 */
public class Room {

	private final RoomData data;
	private final RoomFurniture[] furniture;
	private int rotation, theme;

	public Room(RoomData data, int rotation, int theme) {
		this.rotation = rotation;
		this.theme = theme;
		this.data = data;
		this.furniture = new RoomFurniture[data.getSpots().length];
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

	public RoomFurniture[] getFurniture() {
		return furniture;
	}

	public void addFurniture(RoomFurniture furniture) {
		for(int i = 0; i < data.getSpots().length; i++) {
			if(data.getSpots()[i].getHotSpotId() == furniture.getFurniture().getHotSpotId()) {
				this.furniture[i] = furniture;
			}
		}
	}
}