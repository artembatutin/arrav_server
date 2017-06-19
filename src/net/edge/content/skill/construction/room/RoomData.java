package net.edge.content.skill.construction.room;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.content.skill.construction.data.Constants;

import java.util.EnumSet;

/**
 * Enumeration of all possible rooms.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum RoomData {
	EMPTY(1864, 5056, Constants.EMPTY, 1, 0, new boolean[]{true, true, true, true}),
	BUILDABLE(1864, 5056, Constants.BUILDABLE, 1, 0, new boolean[]{true, true, true, true}),
	GARDEN(1856, 5064, Constants.GARDEN, 1, 1000, new boolean[]{true, true, true, true}),
	PARLOUR(1920, 5112, Constants.PARLOUR, 1, 1000, new boolean[]{true, true, true, false}),
	KITCHEN(1936, 5112, Constants.KITCHEN, 5, 5000, new boolean[]{true, true, false, false}),
	DINING_ROOM(1952, 5112, Constants.DINING_ROOM, 10, 5000, new boolean[]{true, true, true, false}),
	WORKSHOP(1920, 5096, Constants.WORKSHOP, 15, 10000, new boolean[]{false, true, false, true}),
	BEDROOM(1968, 5112, Constants.BEDROOM, 20, 10000, new boolean[]{true, true, false, false}),
	SKILL_ROOM(1928, 5104, Constants.SKILL_ROOM, 25, 15000, new boolean[]{true, true, true, true}),
	QUEST_HALL_DOWN(1976, 5104, Constants.QUEST_HALL_DOWN, 35, 0, new boolean[]{true, true, true, true}),
	SKILL_HALL_DOWN(1944, 5104, Constants.SKILL_HALL_DOWN, 25, 0, new boolean[]{true, true, true, true}),
	GAMES_ROOM(1960, 5088, Constants.GAMES_ROOM, 30, 25000, new boolean[]{true, true, true, false}),
	COMBAT_ROOM(1944, 5088, Constants.COMBAT_ROOM, 32, 25000, new boolean[]{true, true, true, false}),
	QUEST_ROOM(1960, 5104, Constants.QUEST_ROOM, 35, 25000, new boolean[]{true, true, true, true}),
	MENAGERY(1912, 5072, Constants.MENAGERY, 37, 30000, new boolean[]{true, true, true, true}),
	STUDY(1952, 5096, Constants.STUDY, 40, 50000, new boolean[]{true, true, true, false}),
	CUSTOME_ROOM(1968, 5064, Constants.COSTUME_ROOM, 42, 50000, new boolean[]{false, true, false, false}),
	CHAPEL(1936, 5096, Constants.CHAPEL, 45, 50000, new boolean[]{true, true, false, false}),
	PORTAL_ROOM(1928, 5088, Constants.PORTAL_ROOM, 50, 100000, new boolean[]{false, true, false, false}),
	FORMAL_GARDEN(1872, 5064, Constants.FORMAL_GARDEN, 55, 75000, new boolean[]{true, true, true, true}),
	THRONE_ROOM(1968, 5096, Constants.THRONE_ROOM, 60, 150000, new boolean[]{false, true, false, false}),
	OUBLIETTE(1904, 5080, Constants.OUBLIETTE, 65, 150000, new boolean[]{true, true, true, true}),
	PIT(1896, 5072, Constants.PIT, 70, 10000, new boolean[]{true, true, true, true}),
	DUNGEON_STAIR_ROOM(1872, 5080, Constants.DUNGEON_STAIR_ROOM, 70, 7500, new boolean[]{true, true, true, true}),
	TREASURE_ROOM(1912, 5088, Constants.TREASURE_ROOM, 75, 250000, new boolean[]{false, true, false, false}),
	CORRIDOR(1888, 5080, Constants.CORRIDOR, 70, 7500, new boolean[]{false, true, false, true}),
	JUNCTION(1856, 5080, Constants.JUNCTION, 70, 7500, new boolean[]{true, true, true, true}),
	ROOF(1888, 5064, Constants.ROOF, 0, 0, new boolean[]{true, true, true, true}),
	DUNGEON_EMPTY(1880, 5056, Constants.DUNGEON_EMPTY, 0, 0, new boolean[]{true, true, true, true});
	
	private int x, y, cost, levelToBuild, id;
	private boolean[] doors;
	
	RoomData(int x, int y, int id, int levelToBuild, int cost, boolean[] doors) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.levelToBuild = levelToBuild;
		this.cost = cost;
		this.doors = doors;
	}
	
	public boolean[] getDoors() {
		return doors;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int getLevelToBuild() {
		return levelToBuild;
	}
	
	public int getId() {
		return id;
	}
	
	public static int getFirstElegibleRotation(RoomData rd, int from) {
		for(int rot = 0; rot < 4; rot++) {
			boolean[] door = rd.getRotatedDoors(rot);
			if(from == 0 && door[2])
				return rot;
			if(from == 1 && door[3])
				return rot;
			if(from == 2 && door[0])
				return rot;
			if(from == 3 && door[1])
				return rot;
		}
		return -1;
	}
	
	public static int getNextEligibleRotationClockWise(RoomData rd, int from, int currentRot) {
		for(int rot = currentRot + 1; rot < currentRot + 4; rot++) {
			int rawt = (rot > 3 ? (rot - 4) : rot);
			boolean[] door = rd.getRotatedDoors(rawt);
			if(from == 0 && door[2])
				return rawt;
			if(from == 1 && door[3])
				return rawt;
			if(from == 2 && door[0])
				return rawt;
			if(from == 3 && door[1])
				return rawt;
		}
		return currentRot;
	}
	
	public static int getNextEligibleRotationCounterClockWise(RoomData rd, int from, int currentRot) {
		for(int rot = currentRot - 1; rot > currentRot - 4; rot--) {
			int rawt = (rot < 0 ? (rot + 4) : rot);
			boolean[] door = rd.getRotatedDoors(rawt);
			if(from == 0 && door[2])
				return rawt;
			if(from == 1 && door[3])
				return rawt;
			if(from == 2 && door[0])
				return rawt;
			if(from == 3 && door[1])
				return rawt;
		}
		return -1;
	}
	
	public boolean[] getRotatedDoors(int rotation) {
		if(rotation == 0)
			return doors;
		if(rotation == 1) {
			boolean[] newDoors = new boolean[4];
			if(doors[0])
				newDoors[3] = true;
			if(doors[1])
				newDoors[0] = true;
			if(doors[2])
				newDoors[1] = true;
			if(doors[3])
				newDoors[2] = true;
			return newDoors;
		}
		if(rotation == 2) {
			boolean[] newDoors = new boolean[4];
			if(doors[0])
				newDoors[2] = true;
			if(doors[1])
				newDoors[3] = true;
			if(doors[2])
				newDoors[0] = true;
			if(doors[3])
				newDoors[1] = true;
			return newDoors;
		}
		if(rotation == 3) {
			boolean[] newDoors = new boolean[4];
			if(doors[0])
				newDoors[1] = true;
			if(doors[1])
				newDoors[2] = true;
			if(doors[2])
				newDoors[3] = true;
			if(doors[3])
				newDoors[0] = true;
			return newDoors;
		}
		return null;
	}
}