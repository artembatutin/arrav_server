package com.rageps.content.skill.construction.room;

import com.rageps.content.skill.construction.data.Constants;
import com.rageps.content.skill.construction.furniture.HotSpots;

/**
 * Enumeration of all possible rooms.
 * @author Artem Batutin
 */
public enum RoomData {
	
	EMPTY(1864, 5056, Constants.EMPTY, 1, 0, new boolean[]{true, true, true, true}, new HotSpots[]{}),
	BUILDABLE(1864, 5056, Constants.BUILDABLE, 1, 0, new boolean[]{true, true, true, true}, new HotSpots[]{}),
	GARDEN(1856, 5064, Constants.GARDEN, 1, 1000, new boolean[]{true, true, true, true}, new HotSpots[]{HotSpots.CENTREPIECE, HotSpots.TREE_1, HotSpots.TREE_2, HotSpots.SMALL_PLANT_1, HotSpots.SMALL_PLANT_2, HotSpots.BIG_PLANT_1, HotSpots.BIG_PLANT_2}),
	PARLOUR(1920, 5112, Constants.PARLOUR, 1, 1000, new boolean[]{true, true, true, false}, new HotSpots[]{HotSpots.PARLOUR_CHAIR_1, HotSpots.PARLOUR_CHAIR_2, HotSpots.PARLOUR_CHAIR_3, HotSpots.PARLOUR_RUG_1, HotSpots.PARLOUR_RUG_2, HotSpots.PARLOUR_RUG_3, HotSpots.PARLOUR_BOOKCASE_1, HotSpots.PARLOUR_BOOKCASE_2, HotSpots.PARLOUR_FIREPLACE, HotSpots.PARLOUR_CURTAIN_1, HotSpots.PARLOUR_CURTAIN_2, HotSpots.PARLOUR_CURTAIN_3, HotSpots.PARLOUR_CURTAIN_4, HotSpots.PARLOUR_CURTAIN_5, HotSpots.PARLOUR_CURTAIN_6, HotSpots.PARLOUR_CURTAIN_7, HotSpots.PARLOUR_CURTAIN_8}),
	KITCHEN(1936, 5112, Constants.KITCHEN, 5, 5000, new boolean[]{true, true, false, false}, new HotSpots[]{HotSpots.KITCHEN_CAT_BASKET, HotSpots.KITCHEN_TABLE, HotSpots.KITCHEN_STOVE, HotSpots.KITCHEN_SINK, HotSpots.KITCHEN_LARDER, HotSpots.KITCHEN_SHELF_1, HotSpots.KITCHEN_SHELF_2, HotSpots.KITCHEN_SHELF_3}),
	DINING_ROOM(1952, 5112, Constants.DINING_ROOM, 10, 5000, new boolean[]{true, true, true, false}, new HotSpots[]{HotSpots.DINING_CURTAIN_1, HotSpots.DINING_CURTAIN_2, HotSpots.DINING_CURTAIN_3, HotSpots.DINING_CURTAIN_4, HotSpots.DINING_CURTAIN_5, HotSpots.DINING_CURTAIN_6, HotSpots.DINING_BELL_PULL, HotSpots.DINING_FIREPLACE, HotSpots.DINING_WALL_DEC_1, HotSpots.DINING_WALL_DEC_2, HotSpots.DINING_TABLE, HotSpots.DINING_SEATING_1, HotSpots.DINING_SEATING_2, HotSpots.DINING_SEATING_3, HotSpots.DINING_SEATING_4, HotSpots.DINING_SEATING_5, HotSpots.DINING_SEATING_6, HotSpots.DINING_SEATING_7, HotSpots.DINING_SEATING_8}),
	WORKSHOP(1920, 5096, Constants.WORKSHOP, 15, 10000, new boolean[]{false, true, false, true}, new HotSpots[]{HotSpots.WORKSHOP_TOOL_1, HotSpots.WORKSHOP_TOOL_2, HotSpots.WORKSHOP_TOOL_3, HotSpots.WORKSHOP_TOOL_4, HotSpots.WORKSHOP_TOOL_5, HotSpots.WORKSHOP_CLOCKMAKING, HotSpots.WORKSHOP_HERALDY, HotSpots.WORKSHOP_REPAIR_STANCE, HotSpots.WORKSHOP_WORKBENCH}),
	BEDROOM(1968, 5112, Constants.BEDROOM, 20, 10000, new boolean[]{true, true, false, false}, new HotSpots[]{HotSpots.BEDROOM_RUG_1, HotSpots.BEDROOM_RUG_2, HotSpots.BEDROOM_RUG_3, HotSpots.BEDROOM_CURTAIN_2, HotSpots.BEDROOM_CURTAIN_2, HotSpots.BEDROOM_CURTAIN_3, HotSpots.BEDROOM_CURTAIN_4, HotSpots.BEDROOM_CURTAIN_5, HotSpots.BEDROOM_CURTAIN_6, HotSpots.BEDROOM_CURTAIN_7, HotSpots.BEDROOM_CURTAIN_8, HotSpots.BEDROOM_FIREPLACE, HotSpots.BEDROOM_DRESSER, HotSpots.BEDROOM_BED, HotSpots.BEDROOM_WARDROBE, HotSpots.BEDROOM_CLOCK}),
	SKILL_ROOM(1928, 5104, Constants.SKILL_ROOM, 25, 15000, new boolean[]{true, true, true, true}, new HotSpots[]{HotSpots.SKILL_HALL_RUG_1, HotSpots.SKILL_HALL_RUG_2, HotSpots.SKILL_HALL_RUG_3, HotSpots.SKILL_HALL_RUNE_CASE, HotSpots.SKILL_HALL_FISHING_TOPHY, HotSpots.SKILL_HALL_HEAD_TOPHY, HotSpots.SKILL_HALL_ARMOUR_1, HotSpots.SKILL_HALL_ARMOUR_2, HotSpots.SKILL_HALL_STAIRS, HotSpots.SKILL_HALL_STAIRS_1}),
	QUEST_HALL_DOWN(1976, 5104, Constants.QUEST_HALL_DOWN, 35, 0, new boolean[]{true, true, true, true}, new HotSpots[]{HotSpots.QUEST_HALL_BOOKCASE_DOWN, HotSpots.QUEST_HALL_MAP_DOWN, HotSpots.QUEST_HALL_SWORD_DOWN, HotSpots.QUEST_HALL_LANDSCAPE_DOWN, HotSpots.QUEST_HALL_PORTRAIT_DOWN, HotSpots.QUEST_HALL_GUILD_TROPHY_DOWN, HotSpots.QUEST_HALL_STAIRS_DOWN, HotSpots.QUEST_HALL_RUG_1_DOWN, HotSpots.QUEST_HALL_RUG_2_DOWN, HotSpots.QUEST_HALL_RUG_3_DOWN}),
	SKILL_HALL_DOWN(1944, 5104, Constants.SKILL_HALL_DOWN, 25, 0, new boolean[]{true, true, true, true}, new HotSpots[]{HotSpots.SKILL_HALL_RUG_1_DOWN, HotSpots.SKILL_HALL_RUG_2_DOWN, HotSpots.SKILL_HALL_RUG_3_DOWN, HotSpots.SKILL_HALL_RUNE_CASE_DOWN, HotSpots.SKILL_HALL_FISHING_TOPHY_DOWN, HotSpots.SKILL_HALL_HEAD_TOPHY_DOWN, HotSpots.SKILL_HALL_ARMOUR_1_DOWN, HotSpots.SKILL_HALL_ARMOUR_2_DOWN, HotSpots.SKILL_HALL_STAIRS_DOWN, HotSpots.SKILL_HALL_STAIRS_1_DOWN}),
	GAMES_ROOM(1960, 5088, Constants.GAMES_ROOM, 30, 25000, new boolean[]{true, true, true, false}, new HotSpots[]{HotSpots.RANGING_GAME, HotSpots.STONE_SPACE, HotSpots.ELEMENTAL_BALANCE, HotSpots.PRIZE_CHEST, HotSpots.GAME_SPACE}),
	COMBAT_ROOM(1944, 5088, Constants.COMBAT_ROOM, 32, 25000, new boolean[]{true, true, true, false}, new HotSpots[]{HotSpots.STORAGE_RACK, HotSpots.COMBAT_WALL_DEC_1, HotSpots.COMBAT_WALL_DEC_2, HotSpots.COMBAT_RING_1, HotSpots.COMBAT_RING_3, HotSpots.COMBAT_RING_4, HotSpots.COMBAT_RING_5, HotSpots.COMBAT_RING_6, HotSpots.COMBAT_RING_7, HotSpots.COMBAT_RING_8, HotSpots.COMBAT_RING_9, HotSpots.COMBAT_RING_10, HotSpots.COMBAT_RING_11, HotSpots.COMBAT_RING_12, HotSpots.COMBAT_RING_13, HotSpots.COMBAT_RING_14, HotSpots.COMBAT_RING_15, HotSpots.COMBAT_RING_16}),
	QUEST_ROOM(1960, 5104, Constants.QUEST_ROOM, 35, 25000, new boolean[]{true, true, true, true}, new HotSpots[]{HotSpots.QUEST_HALL_BOOKCASE, HotSpots.QUEST_HALL_MAP, HotSpots.QUEST_HALL_LANDSCAPE, HotSpots.QUEST_HALL_PORTRAIT, HotSpots.QUEST_HALL_GUILD_TROPHY, HotSpots.QUEST_HALL_STAIRS, HotSpots.QUEST_HALL_RUG_1, HotSpots.QUEST_HALL_RUG_2, HotSpots.QUEST_HALL_RUG_3}),
	MENAGERY(1912, 5072, Constants.MENAGERY, 37, 30000, new boolean[]{true, true, true, true}, new HotSpots[]{HotSpots.MENAGERIE_PET_HOUSE, HotSpots.MENAGERIE_PET_FEEDER, HotSpots.MENAGERIE_OBELISK, HotSpots.HABITAT_SPACE}),
	STUDY(1952, 5096, Constants.STUDY, 40, 50000, new boolean[]{true, true, true, false}, new HotSpots[]{HotSpots.STUDY_WALL_CHART_1, HotSpots.STUDY_WALL_CHART_2, HotSpots.STUDY_WALL_CHART_3, HotSpots.STUDY_WALL_CHART_4, HotSpots.STUDY_LECTERN, HotSpots.STUDY_STATUE, HotSpots.STUDY_CRYSTAL_BALL, HotSpots.STUDY_GLOBE, HotSpots.STUDY_BOOKCASE_1, HotSpots.STUDY_BOOKCASE_2, HotSpots.STUDY_TELESCOPE}),
	CUSTOME_ROOM(1968, 5064, Constants.COSTUME_ROOM, 42, 50000, new boolean[]{false, true, false, false}, new HotSpots[]{HotSpots.COSTUME_TREASURE_CHEST, HotSpots.COSTUME_FANCY, HotSpots.TOY_BOX, HotSpots.ARMOUR_CASE, HotSpots.MAGIC_WARDROBE, HotSpots.CAPE_RACK}),
	CHAPEL(1936, 5096, Constants.CHAPEL, 45, 50000, new boolean[]{true, true, false, false}, new HotSpots[]{HotSpots.CHAPEL_STATUE_1, HotSpots.CHAPEL_STATUE_2, HotSpots.CHAPEL_LAMP_1, HotSpots.CHAPEL_LAMP_2, HotSpots.CHAPEL_MUSICAL, HotSpots.CHAPEL_ALTAR, HotSpots.CHAPEL_ICON, HotSpots.CHAPEL_WINDOW_0, HotSpots.CHAPEL_WINDOW_1, HotSpots.CHAPEL_WINDOW_2, HotSpots.CHAPEL_WINDOW_3, HotSpots.CHAPEL_WINDOW_4, HotSpots.CHAPEL_WINDOW_5, HotSpots.CHAPEL_RUG_1, HotSpots.CHAPEL_RUG_2, HotSpots.CHAPEL_RUG_3}),
	PORTAL_ROOM(1928, 5088, Constants.PORTAL_ROOM, 50, 100000, new boolean[]{false, true, false, false}, new HotSpots[]{HotSpots.PORTAL_1, HotSpots.PORTAL_2, HotSpots.PORTAL_3, HotSpots.PORTAL_CENTREPIECE}),
	FORMAL_GARDEN(1872, 5064, Constants.FORMAL_GARDEN, 55, 75000, new boolean[]{true, true, true, true}, new HotSpots[]{HotSpots.FORMAL_CENTREPIECE, HotSpots.FORMAL_SMALL_PLANT_1_0, HotSpots.FORMAL_SMALL_PLANT_1_1, HotSpots.FORMAL_SMALL_PLANT_1_2, HotSpots.FORMAL_SMALL_PLANT_1_3, HotSpots.FORMAL_SMALL_PLANT_2_0, HotSpots.FORMAL_SMALL_PLANT_2_1, HotSpots.FORMAL_SMALL_PLANT_2_2, HotSpots.FORMAL_SMALL_PLANT_2_3, HotSpots.FORMAL_BIG_PLANT_1_0, HotSpots.FORMAL_BIG_PLANT_1_1, HotSpots.FORMAL_BIG_PLANT_2_0, HotSpots.FORMAL_BIG_PLANT_2_1, HotSpots.FORMAL_HEDGE_1, HotSpots.FORMAL_HEDGE_2, HotSpots.FORMAL_HEDGE_3, HotSpots.FORMAL_FENCE}),
	THRONE_ROOM(1968, 5096, Constants.THRONE_ROOM, 60, 150000, new boolean[]{false, true, false, false}, new HotSpots[]{HotSpots.THRONE_ROOM_THRONE_1, HotSpots.THRONE_ROOM_THRONE_2, HotSpots.THRONE_ROOM_LEVER, HotSpots.THRONE_DECORATION_1, HotSpots.THRONE_DECORATION_2, HotSpots.THRONE_TRAP_1, HotSpots.THRONE_TRAP_2, HotSpots.THRONE_TRAP_3, HotSpots.THRONE_TRAP_4, HotSpots.THRONE_BENCH_1, HotSpots.THRONE_BENCH_2, HotSpots.THRONE_BENCH_3, HotSpots.THRONE_BENCH_4, HotSpots.THRONE_BENCH_5, HotSpots.THRONE_BENCH_6, HotSpots.THRONE_BENCH_7, HotSpots.THRONE_BENCH_8, HotSpots.THRONE_BENCH_9, HotSpots.THRONE_BENCH_10, HotSpots.THRONE_BENCH_11, HotSpots.THRONE_BENCH_12}),
	OUBLIETTE(1904, 5080, Constants.OUBLIETTE, 65, 150000, new boolean[]{true, true, true, true}, new HotSpots[]{HotSpots.OUBLIETTE_FLOOR_1, HotSpots.OUBLIETTE_FLOOR_2, HotSpots.OUBLIETTE_FLOOR_3, HotSpots.OUBLIETTE_FLOOR_4, HotSpots.OUBLIETTE_FLOOR_5, HotSpots.OUBLIETTE_CAGE_1, HotSpots.OUBLIETTE_CAGE_2, HotSpots.OUBLIETTE_GUARD, HotSpots.OUBLIETTE_LADDER, HotSpots.OUBLIETTE_DECORATION_1, HotSpots.OUBLIETTE_DECORATION_2, HotSpots.OUBLIETTE_DECORATION_3, HotSpots.OUBLIETTE_DECORATION_4, HotSpots.OUBLIETTE_LIGHT_1, HotSpots.OUBLIETTE_LIGHT_2, HotSpots.OUBLIETTE_LIGHT_3, HotSpots.OUBLIETTE_LIGHT_4}),
	PIT(1896, 5072, Constants.PIT, 70, 10000, new boolean[]{true, true, true, true}, new HotSpots[]{HotSpots.PIT_TRAP_1, HotSpots.PIT_TRAP_2, HotSpots.PIT_TRAP_3, HotSpots.PIT_TRAP_4, HotSpots.PIT_GUARDIAN, HotSpots.PIT_LIGHT_1, HotSpots.PIT_LIGHT_2, HotSpots.PIT_LIGHT_3, HotSpots.PIT_LIGHT_4, HotSpots.PIT_LIGHT_5, HotSpots.PIT_LIGHT_6, HotSpots.PIT_LIGHT_7, HotSpots.PIT_LIGHT_8, HotSpots.PIT_DECORATION_1, HotSpots.PIT_DECORATION_2, HotSpots.PIT_DOOR_1, HotSpots.PIT_DOOR_2, HotSpots.PIT_DOOR_3, HotSpots.PIT_DOOR_4, HotSpots.PIT_DOOR_5, HotSpots.PIT_DOOR_6, HotSpots.PIT_DOOR_7, HotSpots.PIT_DOOR_8}),
	DUNGEON_STAIR_ROOM(1872, 5080, Constants.DUNGEON_STAIR_ROOM, 70, 7500, new boolean[]{true, true, true, true}, new HotSpots[]{HotSpots.DUNG_STAIR_LIGHT_1, HotSpots.DUNG_STAIR_LIGHT_2, HotSpots.DUNG_STAIR_LIGHT_3, HotSpots.DUNG_STAIR_LIGHT_4, HotSpots.DUNG_STAIR_LIGHT_5, HotSpots.DUNG_STAIR_LIGHT_6, HotSpots.DUNG_STAIR_LIGHT_7, HotSpots.DUNG_STAIR_LIGHT_8, HotSpots.DUNG_STAIR_DECORATION_1, HotSpots.DUNG_STAIR_DECORATION_2, HotSpots.DUNG_STAIR_GUARD_1, HotSpots.DUNG_STAIR_GUARD_2, HotSpots.DUNG_STAIRS}),
	TREASURE_ROOM(1912, 5088, Constants.TREASURE_ROOM, 75, 250000, new boolean[]{false, true, false, false}, new HotSpots[]{HotSpots.TREASURE_ROOM_DECORATION_1, HotSpots.TREASURE_ROOM_DECORATION_2, HotSpots.TREASURE_ROOM_LIGHT_1, HotSpots.TREASURE_ROOM_LIGHT_2, HotSpots.TREASURE_MONSTER, HotSpots.TREASURE_CHEST, HotSpots.TREASURE_DECORATION_1, HotSpots.TREASURE_DECORATION_2, HotSpots.TREASURE_ROOM_DOOR_1, HotSpots.TREASURE_ROOM_DOOR_2}),
	CORRIDOR(1888, 5080, Constants.CORRIDOR, 70, 7500, new boolean[]{false, true, false, true}, new HotSpots[]{HotSpots.CORRIDOR_LIGHT_1, HotSpots.CORRIDOR_LIGHT_2, HotSpots.CORRIDOR_LIGHT_3, HotSpots.CORRIDOR_LIGHT_4, HotSpots.CORRIDOR_DECORATION_1, HotSpots.CORRIDOR_DECORATION_2, HotSpots.CORRIDOR_GUARD, HotSpots.CORRIDOR_TRAP_1, HotSpots.CORRIDOR_TRAP_2, HotSpots.CORRIDOR_TRAP_3, HotSpots.CORRIDOR_TRAP_4, HotSpots.CORRIDOR_DOOR_1, HotSpots.CORRIDOR_DOOR_2, HotSpots.CORRIDOR_DOOR_3, HotSpots.CORRIDOR_DOOR_4}),
	JUNCTION(1856, 5080, Constants.JUNCTION, 70, 7500, new boolean[]{true, true, true, true}, new HotSpots[]{HotSpots.JUNCTION_TRAP_1, HotSpots.JUNCTION_TRAP_2, HotSpots.JUNCTION_TRAP_3, HotSpots.JUNCTION_TRAP_4, HotSpots.JUNCTION_LIGHT_1, HotSpots.JUNCTION_LIGHT_2, HotSpots.JUNCTION_LIGHT_3, HotSpots.JUNCTION_LIGHT_4, HotSpots.JUNCTION_DECORATION_1, HotSpots.JUNCTION_DECORATION_2, HotSpots.JUNCTION_GUARD}),
	ROOF(1888, 5064, Constants.ROOF, 0, 0, new boolean[]{true, true, true, true}, new HotSpots[]{}),
	DUNGEON_EMPTY(1880, 5056, Constants.DUNGEON_EMPTY, 0, 0, new boolean[]{true, true, true, true}, new HotSpots[]{});
	
	private HotSpots[] spots;
	private int x, y, cost, levelToBuild, id;
	private boolean[] doors;
	
	RoomData(int x, int y, int id, int levelToBuild, int cost, boolean[] doors, HotSpots[] spots) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.levelToBuild = levelToBuild;
		this.cost = cost;
		this.doors = doors;
		this.spots = spots;
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
	
	public HotSpots[] getSpots() {
		return spots;
	}
	
	public HotSpots getSpot(int object) {
		for(HotSpots hs : spots) {
			if(hs.getObjectId() == object)
				return hs;
		}
		return null;
	}
	
	public HotSpots getSpot(int xOffset, int yOffset) {
		for(HotSpots hs : spots) {
			if(hs.getYOffset() == yOffset && hs.getXOffset() == xOffset && (!hs.isMutiple() || hs.getRoomType() == getId()))
				return hs;
		}
		return null;
	}
}