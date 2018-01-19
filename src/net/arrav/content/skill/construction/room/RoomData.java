package net.arrav.content.skill.construction.room;

import net.arrav.content.skill.construction.data.Constants;
import net.arrav.content.skill.construction.furniture.HotSpots;

import static net.arrav.content.skill.construction.furniture.HotSpots.*;

/**
 * Enumeration of all possible rooms.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum RoomData {
	
	EMPTY(1864, 5056, Constants.EMPTY, 1, 0, new boolean[]{true, true, true, true}, new HotSpots[]{}),
	BUILDABLE(1864, 5056, Constants.BUILDABLE, 1, 0, new boolean[]{true, true, true, true}, new HotSpots[]{}),
	GARDEN(1856, 5064, Constants.GARDEN, 1, 1000, new boolean[]{true, true, true, true}, new HotSpots[]{CENTREPIECE, TREE_1, TREE_2, SMALL_PLANT_1, SMALL_PLANT_2, BIG_PLANT_1, BIG_PLANT_2}),
	PARLOUR(1920, 5112, Constants.PARLOUR, 1, 1000, new boolean[]{true, true, true, false}, new HotSpots[]{PARLOUR_CHAIR_1, PARLOUR_CHAIR_2, PARLOUR_CHAIR_3, PARLOUR_RUG_1, PARLOUR_RUG_2, PARLOUR_RUG_3, PARLOUR_BOOKCASE_1, PARLOUR_BOOKCASE_2, PARLOUR_FIREPLACE, PARLOUR_CURTAIN_1, PARLOUR_CURTAIN_2, PARLOUR_CURTAIN_3, PARLOUR_CURTAIN_4, PARLOUR_CURTAIN_5, PARLOUR_CURTAIN_6, PARLOUR_CURTAIN_7, PARLOUR_CURTAIN_8}),
	KITCHEN(1936, 5112, Constants.KITCHEN, 5, 5000, new boolean[]{true, true, false, false}, new HotSpots[]{KITCHEN_CAT_BASKET, KITCHEN_TABLE, KITCHEN_STOVE, KITCHEN_SINK, KITCHEN_LARDER, KITCHEN_SHELF_1, KITCHEN_SHELF_2, KITCHEN_SHELF_3}),
	DINING_ROOM(1952, 5112, Constants.DINING_ROOM, 10, 5000, new boolean[]{true, true, true, false}, new HotSpots[]{DINING_CURTAIN_1, DINING_CURTAIN_2, DINING_CURTAIN_3, DINING_CURTAIN_4, DINING_CURTAIN_5, DINING_CURTAIN_6, DINING_BELL_PULL, DINING_FIREPLACE, DINING_WALL_DEC_1, DINING_WALL_DEC_2, DINING_TABLE, DINING_SEATING_1, DINING_SEATING_2, DINING_SEATING_3, DINING_SEATING_4, DINING_SEATING_5, DINING_SEATING_6, DINING_SEATING_7, DINING_SEATING_8}),
	WORKSHOP(1920, 5096, Constants.WORKSHOP, 15, 10000, new boolean[]{false, true, false, true}, new HotSpots[]{WORKSHOP_TOOL_1, WORKSHOP_TOOL_2, WORKSHOP_TOOL_3, WORKSHOP_TOOL_4, WORKSHOP_TOOL_5, WORKSHOP_CLOCKMAKING, WORKSHOP_HERALDY, WORKSHOP_REPAIR_STANCE, WORKSHOP_WORKBENCH}),
	BEDROOM(1968, 5112, Constants.BEDROOM, 20, 10000, new boolean[]{true, true, false, false}, new HotSpots[]{BEDROOM_RUG_1, BEDROOM_RUG_2, BEDROOM_RUG_3, BEDROOM_CURTAIN_2, BEDROOM_CURTAIN_2, BEDROOM_CURTAIN_3, BEDROOM_CURTAIN_4, BEDROOM_CURTAIN_5, BEDROOM_CURTAIN_6, BEDROOM_CURTAIN_7, BEDROOM_CURTAIN_8, BEDROOM_FIREPLACE, BEDROOM_DRESSER, BEDROOM_BED, BEDROOM_WARDROBE, BEDROOM_CLOCK}),
	SKILL_ROOM(1928, 5104, Constants.SKILL_ROOM, 25, 15000, new boolean[]{true, true, true, true}, new HotSpots[]{SKILL_HALL_RUG_1, SKILL_HALL_RUG_2, SKILL_HALL_RUG_3, SKILL_HALL_RUNE_CASE, SKILL_HALL_FISHING_TOPHY, SKILL_HALL_HEAD_TOPHY, SKILL_HALL_ARMOUR_1, SKILL_HALL_ARMOUR_2, SKILL_HALL_STAIRS, SKILL_HALL_STAIRS_1}),
	QUEST_HALL_DOWN(1976, 5104, Constants.QUEST_HALL_DOWN, 35, 0, new boolean[]{true, true, true, true}, new HotSpots[]{QUEST_HALL_BOOKCASE_DOWN, QUEST_HALL_MAP_DOWN, QUEST_HALL_SWORD_DOWN, QUEST_HALL_LANDSCAPE_DOWN, QUEST_HALL_PORTRAIT_DOWN, QUEST_HALL_GUILD_TROPHY_DOWN, QUEST_HALL_STAIRS_DOWN, QUEST_HALL_RUG_1_DOWN, QUEST_HALL_RUG_2_DOWN, QUEST_HALL_RUG_3_DOWN}),
	SKILL_HALL_DOWN(1944, 5104, Constants.SKILL_HALL_DOWN, 25, 0, new boolean[]{true, true, true, true}, new HotSpots[]{SKILL_HALL_RUG_1_DOWN, SKILL_HALL_RUG_2_DOWN, SKILL_HALL_RUG_3_DOWN, SKILL_HALL_RUNE_CASE_DOWN, SKILL_HALL_FISHING_TOPHY_DOWN, SKILL_HALL_HEAD_TOPHY_DOWN, SKILL_HALL_ARMOUR_1_DOWN, SKILL_HALL_ARMOUR_2_DOWN, SKILL_HALL_STAIRS_DOWN, SKILL_HALL_STAIRS_1_DOWN}),
	GAMES_ROOM(1960, 5088, Constants.GAMES_ROOM, 30, 25000, new boolean[]{true, true, true, false}, new HotSpots[]{RANGING_GAME, STONE_SPACE, ELEMENTAL_BALANCE, PRIZE_CHEST, GAME_SPACE}),
	COMBAT_ROOM(1944, 5088, Constants.COMBAT_ROOM, 32, 25000, new boolean[]{true, true, true, false}, new HotSpots[]{STORAGE_RACK, COMBAT_WALL_DEC_1, COMBAT_WALL_DEC_2, COMBAT_RING_1, COMBAT_RING_3, COMBAT_RING_4, COMBAT_RING_5, COMBAT_RING_6, COMBAT_RING_7, COMBAT_RING_8, COMBAT_RING_9, COMBAT_RING_10, COMBAT_RING_11, COMBAT_RING_12, COMBAT_RING_13, COMBAT_RING_14, COMBAT_RING_15, COMBAT_RING_16}),
	QUEST_ROOM(1960, 5104, Constants.QUEST_ROOM, 35, 25000, new boolean[]{true, true, true, true}, new HotSpots[]{QUEST_HALL_BOOKCASE, QUEST_HALL_MAP, QUEST_HALL_LANDSCAPE, QUEST_HALL_PORTRAIT, QUEST_HALL_GUILD_TROPHY, QUEST_HALL_STAIRS, QUEST_HALL_RUG_1, QUEST_HALL_RUG_2, QUEST_HALL_RUG_3}),
	MENAGERY(1912, 5072, Constants.MENAGERY, 37, 30000, new boolean[]{true, true, true, true}, new HotSpots[]{MENAGERIE_PET_HOUSE, MENAGERIE_PET_FEEDER, MENAGERIE_OBELISK, HABITAT_SPACE}),
	STUDY(1952, 5096, Constants.STUDY, 40, 50000, new boolean[]{true, true, true, false}, new HotSpots[]{STUDY_WALL_CHART_1, STUDY_WALL_CHART_2, STUDY_WALL_CHART_3, STUDY_WALL_CHART_4, STUDY_LECTERN, STUDY_STATUE, STUDY_CRYSTAL_BALL, STUDY_GLOBE, STUDY_BOOKCASE_1, STUDY_BOOKCASE_2, STUDY_TELESCOPE}),
	CUSTOME_ROOM(1968, 5064, Constants.COSTUME_ROOM, 42, 50000, new boolean[]{false, true, false, false}, new HotSpots[]{COSTUME_TREASURE_CHEST, COSTUME_FANCY, TOY_BOX, ARMOUR_CASE, MAGIC_WARDROBE, CAPE_RACK}),
	CHAPEL(1936, 5096, Constants.CHAPEL, 45, 50000, new boolean[]{true, true, false, false}, new HotSpots[]{CHAPEL_STATUE_1, CHAPEL_STATUE_2, CHAPEL_LAMP_1, CHAPEL_LAMP_2, CHAPEL_MUSICAL, CHAPEL_ALTAR, CHAPEL_ICON, CHAPEL_WINDOW_0, CHAPEL_WINDOW_1, CHAPEL_WINDOW_2, CHAPEL_WINDOW_3, CHAPEL_WINDOW_4, CHAPEL_WINDOW_5, CHAPEL_RUG_1, CHAPEL_RUG_2, CHAPEL_RUG_3}),
	PORTAL_ROOM(1928, 5088, Constants.PORTAL_ROOM, 50, 100000, new boolean[]{false, true, false, false}, new HotSpots[]{PORTAL_1, PORTAL_2, PORTAL_3, PORTAL_CENTREPIECE}),
	FORMAL_GARDEN(1872, 5064, Constants.FORMAL_GARDEN, 55, 75000, new boolean[]{true, true, true, true}, new HotSpots[]{FORMAL_CENTREPIECE, FORMAL_SMALL_PLANT_1_0, FORMAL_SMALL_PLANT_1_1, FORMAL_SMALL_PLANT_1_2, FORMAL_SMALL_PLANT_1_3, FORMAL_SMALL_PLANT_2_0, FORMAL_SMALL_PLANT_2_1, FORMAL_SMALL_PLANT_2_2, FORMAL_SMALL_PLANT_2_3, FORMAL_BIG_PLANT_1_0, FORMAL_BIG_PLANT_1_1, FORMAL_BIG_PLANT_2_0, FORMAL_BIG_PLANT_2_1, FORMAL_HEDGE_1, FORMAL_HEDGE_2, FORMAL_HEDGE_3, FORMAL_FENCE}),
	THRONE_ROOM(1968, 5096, Constants.THRONE_ROOM, 60, 150000, new boolean[]{false, true, false, false}, new HotSpots[]{THRONE_ROOM_THRONE_1, THRONE_ROOM_THRONE_2, THRONE_ROOM_LEVER, THRONE_DECORATION_1, THRONE_DECORATION_2, THRONE_TRAP_1, THRONE_TRAP_2, THRONE_TRAP_3, THRONE_TRAP_4, THRONE_BENCH_1, THRONE_BENCH_2, THRONE_BENCH_3, THRONE_BENCH_4, THRONE_BENCH_5, THRONE_BENCH_6, THRONE_BENCH_7, THRONE_BENCH_8, THRONE_BENCH_9, THRONE_BENCH_10, THRONE_BENCH_11, THRONE_BENCH_12}),
	OUBLIETTE(1904, 5080, Constants.OUBLIETTE, 65, 150000, new boolean[]{true, true, true, true}, new HotSpots[]{OUBLIETTE_FLOOR_1, OUBLIETTE_FLOOR_2, OUBLIETTE_FLOOR_3, OUBLIETTE_FLOOR_4, OUBLIETTE_FLOOR_5, OUBLIETTE_CAGE_1, OUBLIETTE_CAGE_2, OUBLIETTE_GUARD, OUBLIETTE_LADDER, OUBLIETTE_DECORATION_1, OUBLIETTE_DECORATION_2, OUBLIETTE_DECORATION_3, OUBLIETTE_DECORATION_4, OUBLIETTE_LIGHT_1, OUBLIETTE_LIGHT_2, OUBLIETTE_LIGHT_3, OUBLIETTE_LIGHT_4}),
	PIT(1896, 5072, Constants.PIT, 70, 10000, new boolean[]{true, true, true, true}, new HotSpots[]{PIT_TRAP_1, PIT_TRAP_2, PIT_TRAP_3, PIT_TRAP_4, PIT_GUARDIAN, PIT_LIGHT_1, PIT_LIGHT_2, PIT_LIGHT_3, PIT_LIGHT_4, PIT_LIGHT_5, PIT_LIGHT_6, PIT_LIGHT_7, PIT_LIGHT_8, PIT_DECORATION_1, PIT_DECORATION_2, PIT_DOOR_1, PIT_DOOR_2, PIT_DOOR_3, PIT_DOOR_4, PIT_DOOR_5, PIT_DOOR_6, PIT_DOOR_7, PIT_DOOR_8}),
	DUNGEON_STAIR_ROOM(1872, 5080, Constants.DUNGEON_STAIR_ROOM, 70, 7500, new boolean[]{true, true, true, true}, new HotSpots[]{DUNG_STAIR_LIGHT_1, DUNG_STAIR_LIGHT_2, DUNG_STAIR_LIGHT_3, DUNG_STAIR_LIGHT_4, DUNG_STAIR_LIGHT_5, DUNG_STAIR_LIGHT_6, DUNG_STAIR_LIGHT_7, DUNG_STAIR_LIGHT_8, DUNG_STAIR_DECORATION_1, DUNG_STAIR_DECORATION_2, DUNG_STAIR_GUARD_1, DUNG_STAIR_GUARD_2, DUNG_STAIRS}),
	TREASURE_ROOM(1912, 5088, Constants.TREASURE_ROOM, 75, 250000, new boolean[]{false, true, false, false}, new HotSpots[]{TREASURE_ROOM_DECORATION_1, TREASURE_ROOM_DECORATION_2, TREASURE_ROOM_LIGHT_1, TREASURE_ROOM_LIGHT_2, TREASURE_MONSTER, TREASURE_CHEST, TREASURE_DECORATION_1, TREASURE_DECORATION_2, TREASURE_ROOM_DOOR_1, TREASURE_ROOM_DOOR_2}),
	CORRIDOR(1888, 5080, Constants.CORRIDOR, 70, 7500, new boolean[]{false, true, false, true}, new HotSpots[]{CORRIDOR_LIGHT_1, CORRIDOR_LIGHT_2, CORRIDOR_LIGHT_3, CORRIDOR_LIGHT_4, CORRIDOR_DECORATION_1, CORRIDOR_DECORATION_2, CORRIDOR_GUARD, CORRIDOR_TRAP_1, CORRIDOR_TRAP_2, CORRIDOR_TRAP_3, CORRIDOR_TRAP_4, CORRIDOR_DOOR_1, CORRIDOR_DOOR_2, CORRIDOR_DOOR_3, CORRIDOR_DOOR_4}),
	JUNCTION(1856, 5080, Constants.JUNCTION, 70, 7500, new boolean[]{true, true, true, true}, new HotSpots[]{JUNCTION_TRAP_1, JUNCTION_TRAP_2, JUNCTION_TRAP_3, JUNCTION_TRAP_4, JUNCTION_LIGHT_1, JUNCTION_LIGHT_2, JUNCTION_LIGHT_3, JUNCTION_LIGHT_4, JUNCTION_DECORATION_1, JUNCTION_DECORATION_2, JUNCTION_GUARD}),
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