package net.edge.content.skill.construction.room;

import net.edge.content.skill.construction.furniture.Furniture;

/**
 * Represents a single house furniture.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class RoomFurniture {
	
	private final Furniture furniture;
	private int standardXOff, standardYOff;
	
	public RoomFurniture(Furniture furniture, int standardXOff, int standardYOff) {
		this.furniture = furniture;
		this.standardXOff = standardXOff;
		this.standardYOff = standardYOff;
	}
	
	public RoomFurniture(int furniture, int standardXOff, int standardYOff) {
		this.furniture = Furniture.forFurnitureId(furniture);
		this.standardXOff = standardXOff;
		this.standardYOff = standardYOff;
	}
	
	public int getStandardXOff() {
		return standardXOff;
	}
	
	public void setStandardXOff(int standardXOff) {
		this.standardXOff = standardXOff;
	}
	
	public int getStandardYOff() {
		return standardYOff;
	}
	
	public void setStandardYOff(int standardYOff) {
		this.standardYOff = standardYOff;
	}
	
	public Furniture getFurniture() {
		return furniture;
	}
}