package com.rageps.content.skill.construction.room;

import com.jsoniter.annotation.JsonProperty;
import com.rageps.content.skill.construction.furniture.Furniture;

/**
 * Represents a single house furniture.
 * @author Artem Batutin
 */
public class RoomFurniture {
	
	@JsonProperty(value = "fur")
	private final Furniture furniture;
	@JsonProperty(value = "xOff")
	private int standardXOff;
	@JsonProperty(value = "yOff")
	private int standardYOff;
	
	public RoomFurniture() {
		furniture = null;
	}
	
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