package com.rageps.content.skill.construction.furniture;

import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.net.packet.out.SendObjectsConstruction;
import com.rageps.action.impl.ObjectAction;
import com.rageps.content.skill.construction.Construction;
import com.rageps.content.skill.construction.data.Constants;
import com.rageps.content.skill.construction.room.RoomManipulation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;

import java.awt.*;

/**
 * Enumeration of all of the {@link Construction} hotspots.
 * @author Artem Batutin
 */
public enum HotSpots {
	/**
	 * Garden
	 */
	CENTREPIECE(0, 15361, 3, 3, 0, Constants.GARDEN, new Furniture[]{Furniture.EXIT_PORTAL, Furniture.DECORATIVE_ROCK, Furniture.POND, Furniture.IMP_STATUE, Furniture.DUNGEON_ENTRANCE}),
	TREE_1(1, 15362, 1, 5, 0, Constants.GARDEN, new Furniture[]{Furniture.DEAD_TREE, Furniture.NICE_TREE, Furniture.OAK_TREE, Furniture.WILLOW_TREE, Furniture.MARPLE_TREE, Furniture.YEW_TREE, Furniture.MAGIC_TREE}),
	TREE_2(1, 15363, 6, 6, 0, Constants.GARDEN, new Furniture[]{Furniture.DEAD_TREE, Furniture.NICE_TREE, Furniture.OAK_TREE, Furniture.WILLOW_TREE, Furniture.MARPLE_TREE, Furniture.YEW_TREE, Furniture.MAGIC_TREE}),
	SMALL_PLANT_1(2, 15366, 3, 1, 0, Constants.GARDEN, new Furniture[]{Furniture.PLANT, Furniture.SMALL_FERN, Furniture.FERN}),
	SMALL_PLANT_2(3, 15367, 4, 5, 0, Constants.GARDEN, new Furniture[]{Furniture.DOCK_LEAF, Furniture.THISTLE, Furniture.REEDS}),
	BIG_PLANT_1(4, 15364, 6, 0, 0, Constants.GARDEN, new Furniture[]{Furniture.BIG_FERN, Furniture.BUSH, Furniture.TALL_PLANT}),
	BIG_PLANT_2(5, 15365, 0, 0, 0, Constants.GARDEN, new Furniture[]{Furniture.SHORT_PLANT, Furniture.LARGE_LEAF_BUSH, Furniture.HUGE_PLANT}),
	/**
	 * Parlour
	 */
	PARLOUR_CHAIR_1(6, 15410, 2, 4, 2, Constants.PARLOUR, 11, new Furniture[]{Furniture.CRUDE_WOODEN_CHAIR, Furniture.WOODEN_CHAIR, Furniture.ROCKING_CHAIR, Furniture.OAK_CHAIR, Furniture.OAK_ARMCHAIR, Furniture.TEAK_ARMCHAIR, Furniture.MAHOGANY_ARMCHAIR}),
	PARLOUR_CHAIR_2(6, 15412, 4, 3, 2, Constants.PARLOUR, new Furniture[]{Furniture.CRUDE_WOODEN_CHAIR, Furniture.WOODEN_CHAIR, Furniture.ROCKING_CHAIR, Furniture.OAK_CHAIR, Furniture.OAK_ARMCHAIR, Furniture.TEAK_ARMCHAIR, Furniture.MAHOGANY_ARMCHAIR}),
	PARLOUR_CHAIR_3(6, 15411, 5, 4, 1, Constants.PARLOUR, 11, new Furniture[]{Furniture.CRUDE_WOODEN_CHAIR, Furniture.WOODEN_CHAIR, Furniture.ROCKING_CHAIR, Furniture.OAK_CHAIR, Furniture.OAK_ARMCHAIR, Furniture.TEAK_ARMCHAIR, Furniture.MAHOGANY_ARMCHAIR}),
	PARLOUR_RUG_1(7, 15414, 2, 2, 0, Constants.PARLOUR, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	PARLOUR_RUG_2(7, 15415, 2, 2, 0, Constants.PARLOUR, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	PARLOUR_RUG_3(7, 15413, 2, 2, 0, Constants.PARLOUR, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	PARLOUR_BOOKCASE_1(8, 15416, 0, 1, 0, Constants.PARLOUR, new Furniture[]{Furniture.WOODEN_BOOKCASE, Furniture.OAK_BOOKCASE, Furniture.MAHOGANY_BOOKCASE}),
	PARLOUR_BOOKCASE_2(8, 15416, 7, 1, 2, Constants.PARLOUR, new Furniture[]{Furniture.WOODEN_BOOKCASE, Furniture.OAK_BOOKCASE, Furniture.MAHOGANY_BOOKCASE}),
	PARLOUR_FIREPLACE(9, 15418, 3, 7, 1, Constants.PARLOUR, new Furniture[]{Furniture.CLAY_FIREPLACE, Furniture.STONE_FIREPLACE, Furniture.MARBLE_FIREPLACE}),
	PARLOUR_CURTAIN_1(10, 15419, 0, 2, 0, Constants.PARLOUR, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	PARLOUR_CURTAIN_2(10, 15419, 0, 5, 0, Constants.PARLOUR, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	PARLOUR_CURTAIN_3(10, 15419, 2, 7, 1, Constants.PARLOUR, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	PARLOUR_CURTAIN_4(10, 15419, 5, 7, 1, Constants.PARLOUR, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	PARLOUR_CURTAIN_5(10, 15419, 7, 5, 2, Constants.PARLOUR, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	PARLOUR_CURTAIN_6(10, 15419, 7, 2, 2, Constants.PARLOUR, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	PARLOUR_CURTAIN_7(10, 15419, 5, 0, 3, Constants.PARLOUR, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	PARLOUR_CURTAIN_8(10, 15419, 2, 0, 3, Constants.PARLOUR, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	
	/**
	 * Kitchen
	 */
	KITCHEN_CAT_BASKET(11, 15402, 0, 0, 0, Constants.KITCHEN, 22, new Furniture[]{Furniture.CAT_BLANKET, Furniture.CAT_BASKET, Furniture.CUSHIONED_CAT_BASKET}),
	KITCHEN_TABLE(12, 15405, 3, 3, 0, Constants.KITCHEN, new Furniture[]{Furniture.WOOD_KITCHEN_TABLE, Furniture.OAK_KITCHEN_TABLE, Furniture.TEAK_KITCHEN_TABLE}),
	KITCHEN_BARREL(13, 15401, 0, 6, 3, Constants.KITCHEN, new Furniture[]{Furniture.BEER_BARREL, Furniture.CIDER_BARREL, Furniture.ASGARNIAN_ALE_BARREL, Furniture.GREENMANS_ALE_BARREL, Furniture.DRAGON_BITTER_BARREL, Furniture.CHEFS_DELIGHT_BARREL}),
	KITCHEN_STOVE(14, 15398, 3, 7, 1, Constants.KITCHEN, new Furniture[]{Furniture.FIRE_PIT, Furniture.FIRE_PIT_HOOKS, Furniture.FIRE_PIT_POT, Furniture.SMALL_OVEN, Furniture.LARGE_OVEN, Furniture.STEEL_RANGE, Furniture.FANCY_RANGE}),
	KITCHEN_SINK(15, 15404, 7, 3, 0, Constants.KITCHEN, new Furniture[]{Furniture.PUMP_AND_DRAIN, Furniture.PUMP_AND_TUB, Furniture.SINK}),
	KITCHEN_LARDER(16, 15403, 6, 0, 3, Constants.KITCHEN, new Furniture[]{Furniture.WOODEN_LARDER, Furniture.OAK_LARDER, Furniture.TEAK_LARDER}),
	KITCHEN_SHELF_1(17, 15400, 1, 7, 1, Constants.KITCHEN, 5, new Furniture[]{Furniture.WOODEN_SHELVES_1, Furniture.WOODEN_SHELVES_2, Furniture.WOODEN_SHELVES_3, Furniture.OAK_SHELVES_1, Furniture.OAK_SHELVES_2, Furniture.TEAK_SHELVES_1, Furniture.TEAK_SHELVES_2}),
	KITCHEN_SHELF_2(17, 15400, 6, 7, 1, Constants.KITCHEN, 5, new Furniture[]{Furniture.WOODEN_SHELVES_1, Furniture.WOODEN_SHELVES_2, Furniture.WOODEN_SHELVES_3, Furniture.OAK_SHELVES_1, Furniture.OAK_SHELVES_2, Furniture.TEAK_SHELVES_1, Furniture.TEAK_SHELVES_2}),
	KITCHEN_SHELF_3(17, 15399, 7, 6, 2, Constants.KITCHEN, 5, new Furniture[]{Furniture.WOODEN_SHELVES_1, Furniture.WOODEN_SHELVES_2, Furniture.WOODEN_SHELVES_3, Furniture.OAK_SHELVES_1, Furniture.OAK_SHELVES_2, Furniture.TEAK_SHELVES_1, Furniture.TEAK_SHELVES_2}),
	
	/**
	 * Dining room
	 */
	DINING_CURTAIN_1(10, 15302, 0, 2, 0, Constants.DINING_ROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	DINING_CURTAIN_2(10, 15302, 0, 5, 0, Constants.DINING_ROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	DINING_CURTAIN_3(10, 15302, 7, 5, 2, Constants.DINING_ROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	DINING_CURTAIN_4(10, 15302, 7, 2, 2, Constants.DINING_ROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	DINING_CURTAIN_5(10, 15302, 5, 0, 3, Constants.DINING_ROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	DINING_CURTAIN_6(10, 15302, 2, 0, 3, Constants.DINING_ROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	DINING_BELL_PULL(18, 15304, 0, 0, 2, Constants.DINING_ROOM, new Furniture[]{Furniture.ROPE_BELL_PULL, Furniture.BELL_PULL, Furniture.POSH_BELL_PULL}),
	DINING_WALL_DEC_1(19, 15303, 2, 7, 1, Constants.DINING_ROOM, 5, new Furniture[]{Furniture.OAK_DECORATION, Furniture.TEAK_DECORATION, Furniture.GILDED_DECORATION}),
	DINING_WALL_DEC_2(19, 15303, 5, 7, 1, Constants.DINING_ROOM, 5, new Furniture[]{Furniture.OAK_DECORATION, Furniture.TEAK_DECORATION, Furniture.GILDED_DECORATION}),
	DINING_FIREPLACE(9, 15301, 3, 7, 1, Constants.DINING_ROOM, new Furniture[]{Furniture.CLAY_FIREPLACE, Furniture.STONE_FIREPLACE, Furniture.MARBLE_FIREPLACE}),
	DINING_SEATING_1(20, 15300, 2, 2, 2, Constants.DINING_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	DINING_SEATING_2(20, 15300, 3, 2, 2, Constants.DINING_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	DINING_SEATING_3(20, 15300, 4, 2, 2, Constants.DINING_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	DINING_SEATING_4(20, 15300, 5, 2, 2, Constants.DINING_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	DINING_SEATING_5(20, 15299, 2, 5, 0, Constants.DINING_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	DINING_SEATING_6(20, 15299, 3, 5, 0, Constants.DINING_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	DINING_SEATING_7(20, 15299, 4, 5, 0, Constants.DINING_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	DINING_SEATING_8(20, 15299, 5, 5, 0, Constants.DINING_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	DINING_TABLE(21, 15298, 2, 3, 0, Constants.DINING_ROOM, new Furniture[]{Furniture.WOOD_DINING_TABLE, Furniture.OAK_TABLE, Furniture.CARVED_OAK_TABLE, Furniture.TEAK_DINING_TABLE, Furniture.CARVED_TEAK_TABLE, Furniture.MAHOGANY_TABLE, Furniture.OPULENT_TABLE}),
	
	/**
	 * Workshop
	 */
	WORKSHOP_TOOL_1(22, 15443, 1, 0, 3, Constants.WORKSHOP, 5, new Furniture[]{Furniture.TOOL_STORE_1, Furniture.TOOL_STORE_2, Furniture.TOOL_STORE_3, Furniture.TOOL_STORE_4, Furniture.TOOL_STORE_5}),
	WORKSHOP_TOOL_2(22, 15445, 0, 1, 0, Constants.WORKSHOP, 5, new Furniture[]{Furniture.TOOL_STORE_1, Furniture.TOOL_STORE_2, Furniture.TOOL_STORE_3, Furniture.TOOL_STORE_4, Furniture.TOOL_STORE_5}),
	WORKSHOP_TOOL_3(22, 15447, 0, 6, 0, Constants.WORKSHOP, 5, new Furniture[]{Furniture.TOOL_STORE_1, Furniture.TOOL_STORE_2, Furniture.TOOL_STORE_3, Furniture.TOOL_STORE_4, Furniture.TOOL_STORE_5}),
	WORKSHOP_TOOL_4(22, 15446, 7, 1, 2, Constants.WORKSHOP, 5, new Furniture[]{Furniture.TOOL_STORE_1, Furniture.TOOL_STORE_2, Furniture.TOOL_STORE_3, Furniture.TOOL_STORE_4, Furniture.TOOL_STORE_5}),
	WORKSHOP_TOOL_5(22, 15444, 6, 0, 3, Constants.WORKSHOP, 5, new Furniture[]{Furniture.TOOL_STORE_1, Furniture.TOOL_STORE_2, Furniture.TOOL_STORE_3, Furniture.TOOL_STORE_4, Furniture.TOOL_STORE_5}),
	WORKSHOP_CLOCKMAKING(23, 15441, 0, 3, 3, Constants.WORKSHOP, new Furniture[]{Furniture.CRAFTING_TABLE_1, Furniture.CRAFTING_TABLE_2, Furniture.CRAFTING_TABLE_3, Furniture.CRAFTING_TABLE_4}),
	WORKSHOP_WORKBENCH(24, 15439, 3, 4, 0, Constants.WORKSHOP, new Furniture[]{Furniture.WOODEN_WORKBENCH, Furniture.OAK_WORKBENCH, Furniture.STEEL_FRAMED_BENCH, Furniture.BENCH_WITH_VICE, Furniture.BENCH_WITH_A_LATHE}),
	WORKSHOP_REPAIR_STANCE(25, 15448, 7, 3, 1, Constants.WORKSHOP, new Furniture[]{Furniture.REAPIR_BENCH, Furniture.WHETSTONE, Furniture.ARMOUR_STAND}),
	WORKSHOP_HERALDY(26, 15450, 7, 6, 1, Constants.WORKSHOP, new Furniture[]{Furniture.PLUMING_STAND, Furniture.SHIELD_EASEL, Furniture.BANNER_EASEL}),
	
	/**
	 * Bedroom
	 */
	BEDROOM_RUG_1(7, 15266, 2, 2, 0, Constants.BEDROOM, 22, new Dimension(5, 3), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	BEDROOM_RUG_2(7, 15265, 2, 2, 0, Constants.BEDROOM, 22, new Dimension(5, 3), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	BEDROOM_RUG_3(7, 15264, 2, 2, 0, Constants.BEDROOM, 22, new Dimension(5, 3), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	
	BEDROOM_CURTAIN_1(10, 15263, 0, 2, 0, Constants.BEDROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	BEDROOM_CURTAIN_2(10, 15263, 0, 5, 0, Constants.BEDROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	BEDROOM_CURTAIN_3(10, 15263, 2, 7, 1, Constants.BEDROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	BEDROOM_CURTAIN_4(10, 15263, 5, 7, 1, Constants.BEDROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	BEDROOM_CURTAIN_5(10, 15263, 7, 5, 2, Constants.BEDROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	BEDROOM_CURTAIN_6(10, 15263, 7, 2, 2, Constants.BEDROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	BEDROOM_CURTAIN_7(10, 15263, 5, 0, 3, Constants.BEDROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	BEDROOM_CURTAIN_8(10, 15263, 2, 0, 3, Constants.BEDROOM, 5, true, new Furniture[]{Furniture.TORN_CURTAINS, Furniture.CURTAINS, Furniture.OPULENT_CURTAINS}),
	BEDROOM_FIREPLACE(9, 15267, 7, 3, 2, Constants.BEDROOM, new Furniture[]{Furniture.CLAY_FIREPLACE, Furniture.STONE_FIREPLACE, Furniture.MARBLE_FIREPLACE}),
	BEDROOM_DRESSER(27, 15262, 0, 7, 0, Constants.BEDROOM, new Furniture[]{Furniture.SHAVING_STAND, Furniture.OAK_SHAVING_STAND, Furniture.OAK_DRESSER, Furniture.TEAK_DRESSER, Furniture.FANCY_TEAK_DRESSER, Furniture.MAHOGANY_DRESSER, Furniture.GILDED_DRESSER}),
	BEDROOM_BED(28, 15260, 3, 6, 0, Constants.BEDROOM, new Furniture[]{Furniture.WOODEN_BED, Furniture.OAK_BED, Furniture.LARGE_OAK_BED, Furniture.TEAK_BED, Furniture.LARGE_TEAK_BED, Furniture.FOUR_POSTER, Furniture.GILDED_FOUR_POSTER}),
	BEDROOM_WARDROBE(29, 15261, 6, 7, 0, Constants.BEDROOM, new Furniture[]{Furniture.SHOE_BOX, Furniture.OAK_DRAWERS, Furniture.OAK_WARDROBE, Furniture.TEAK_DRAWERS, Furniture.TEAK_WARDROBE, Furniture.MAHOGANY_WARDROBE, Furniture.GILDED_WARDROBE}),
	BEDROOM_CLOCK(30, 15268, 7, 0, 1, Constants.BEDROOM, 11, new Furniture[]{Furniture.OAK_CLOCK, Furniture.TEAK_CLOCK, Furniture.GILDED_CLOCK}),
	
	/**
	 * Skill hall
	 */
	SKILL_HALL_RUG_1(7, 15379, 2, 2, 0, Constants.SKILL_ROOM, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	SKILL_HALL_RUG_2(7, 15378, 2, 2, 0, Constants.SKILL_ROOM, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	SKILL_HALL_RUG_3(7, 15377, 2, 2, 0, Constants.SKILL_ROOM, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	SKILL_HALL_RUNE_CASE(31, 15386, 0, 6, 1, Constants.SKILL_ROOM, new Furniture[]{Furniture.RUNE_CASE_1, Furniture.RUNE_CASE_2}),
	SKILL_HALL_FISHING_TOPHY(32, 15383, 1, 7, 0, Constants.SKILL_ROOM, new Furniture[]{Furniture.MOUNTED_BASS, Furniture.MOUNTED_SWORDFISH, Furniture.MOUNTED_SHARK}),
	SKILL_HALL_HEAD_TOPHY(33, 15382, 6, 7, 0, Constants.SKILL_ROOM, new Furniture[]{Furniture.CRAWLING_HAND, Furniture.COCKTRICE_HEAD, Furniture.BASILISK_HEAD, Furniture.KURASK_HEAD, Furniture.ABYSSAL_DEMON_HEAD, Furniture.KING_BLACK_DRAGON_HEAD, Furniture.KALPHITE_QUEEN_HEAD}),
	SKILL_HALL_ARMOUR_1(34, 15384, 5, 3, 0, Constants.SKILL_ROOM, new Furniture[]{Furniture.MITHRIL_ARMOUR, Furniture.ADAMANT_ARMOUR, Furniture.RUNE_ARMOUR}),
	SKILL_HALL_ARMOUR_2(35, 34255, 2, 3, 0, Constants.SKILL_ROOM, new Furniture[]{Furniture.BASIC_DECORATIVE_ARMOUR_STAND, Furniture.DETAILED_DECORATIVE_ARMOUR_STAND, Furniture.INTRICATE_DECORATIVE_ARMOUR_STAND, Furniture.PROFOUND_DECORATIVE_ARMOUR_STAND}),
	SKILL_HALL_STAIRS(36, 15380, 3, 3, 0, Constants.SKILL_ROOM, new Furniture[]{Furniture.OAK_STAIRCASE, Furniture.TEAK_STAIRCASE, Furniture.SPIRAL_STAIRCASE, Furniture.MARBLE_STAIRCASE, Furniture.MARBLE_SPIRAL}),
	SKILL_HALL_STAIRS_1(37, 15381, 3, 3, 0, Constants.SKILL_ROOM, new Furniture[]{Furniture.OAK_STAIRCASE_1, Furniture.TEAK_STAIRCASE_1, Furniture.SPIRAL_STAIRCASE_1, Furniture.MARBLE_STAIRCASE_1, Furniture.MARBLE_SPIRAL_1}),
	
	SKILL_HALL_RUG_1_DOWN(7, 15379, 2, 2, 0, Constants.SKILL_HALL_DOWN, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	SKILL_HALL_RUG_2_DOWN(7, 15378, 2, 2, 0, Constants.SKILL_HALL_DOWN, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	SKILL_HALL_RUG_3_DOWN(7, 15377, 2, 2, 0, Constants.SKILL_HALL_DOWN, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	SKILL_HALL_RUNE_CASE_DOWN(31, 15386, 0, 6, 1, Constants.SKILL_HALL_DOWN, new Furniture[]{Furniture.RUNE_CASE_1, Furniture.RUNE_CASE_2}),
	SKILL_HALL_FISHING_TOPHY_DOWN(32, 15383, 1, 7, 0, Constants.SKILL_HALL_DOWN, new Furniture[]{Furniture.MOUNTED_BASS, Furniture.MOUNTED_SWORDFISH, Furniture.MOUNTED_SHARK}),
	SKILL_HALL_HEAD_TOPHY_DOWN(33, 15382, 6, 7, 0, Constants.SKILL_HALL_DOWN, new Furniture[]{Furniture.CRAWLING_HAND, Furniture.COCKTRICE_HEAD, Furniture.BASILISK_HEAD, Furniture.KURASK_HEAD, Furniture.ABYSSAL_DEMON_HEAD, Furniture.KING_BLACK_DRAGON_HEAD, Furniture.KALPHITE_QUEEN_HEAD}),
	SKILL_HALL_ARMOUR_1_DOWN(34, 15384, 5, 3, 0, Constants.SKILL_HALL_DOWN, new Furniture[]{Furniture.MITHRIL_ARMOUR, Furniture.ADAMANT_ARMOUR, Furniture.RUNE_ARMOUR}),
	SKILL_HALL_ARMOUR_2_DOWN(35, 34255, 2, 3, 0, Constants.SKILL_HALL_DOWN, new Furniture[]{Furniture.BASIC_DECORATIVE_ARMOUR_STAND, Furniture.DETAILED_DECORATIVE_ARMOUR_STAND, Furniture.INTRICATE_DECORATIVE_ARMOUR_STAND, Furniture.PROFOUND_DECORATIVE_ARMOUR_STAND}),
	SKILL_HALL_STAIRS_DOWN(36, 15380, 3, 3, 0, Constants.SKILL_HALL_DOWN, new Furniture[]{Furniture.OAK_STAIRCASE, Furniture.TEAK_STAIRCASE, Furniture.SPIRAL_STAIRCASE, Furniture.MARBLE_STAIRCASE, Furniture.MARBLE_SPIRAL}),
	SKILL_HALL_STAIRS_1_DOWN(37, 15381, 3, 3, 0, Constants.SKILL_HALL_DOWN, new Furniture[]{Furniture.OAK_STAIRCASE_1, Furniture.TEAK_STAIRCASE_1, Furniture.SPIRAL_STAIRCASE_1, Furniture.MARBLE_STAIRCASE_1, Furniture.MARBLE_SPIRAL_1}),
	
	/**
	 * Games room
	 */
	RANGING_GAME(38, 15346, 1, 0, 2, Constants.GAMES_ROOM, new Furniture[]{Furniture.HOOP_AND_STICK, Furniture.DARTBOARD, Furniture.ARCHERY_TARGET}),
	STONE_SPACE(39, 15344, 2, 4, 0, Constants.GAMES_ROOM, new Furniture[]{Furniture.CLAY_ATTACK_STONE, Furniture.ATTACK_STONE, Furniture.MARBLE_ATTACK_STONE}),
	ELEMENTAL_BALANCE(40, 15345, 5, 4, 0, Constants.GAMES_ROOM, new Furniture[]{Furniture.ELEMENTAL_BALANCE_1, Furniture.ELEMENTAL_BALANCE_2, Furniture.ELEMENTAL_BALANCE_3}),
	PRIZE_CHEST(41, 15343, 3, 7, 0, Constants.GAMES_ROOM, new Furniture[]{Furniture.OAK_PRIZED_CHEST, Furniture.TEAK_PRIZED_CHEST, Furniture.MAHOGANY_PRIZED_CHEST}),
	GAME_SPACE(42, 15342, 6, 0, 1, Constants.GAMES_ROOM, new Furniture[]{Furniture.JESTER, Furniture.TREASURE_HUNT, Furniture.HANGMAN}),
	
	/**
	 * Combat room
	 */
	STORAGE_RACK(43, 15296, 3, 7, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.GLOVE_RACK, Furniture.WEAPONS_RACK, Furniture.EXTRA_WEAPONS_RACK}),
	COMBAT_WALL_DEC_1(19, 15297, 1, 7, 1, Constants.COMBAT_ROOM, 5, new Furniture[]{Furniture.OAK_DECORATION, Furniture.TEAK_DECORATION, Furniture.GILDED_DECORATION}),
	COMBAT_WALL_DEC_2(19, 15297, 6, 7, 1, Constants.COMBAT_ROOM, 5, new Furniture[]{Furniture.OAK_DECORATION, Furniture.TEAK_DECORATION, Furniture.GILDED_DECORATION}),
	
	COMBAT_RING_1(44, 15294, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_3(44, 15293, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_4(44, 15292, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_5(44, 15291, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_6(44, 15290, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_7(44, 15289, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_8(44, 15288, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_9(44, 15287, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_10(44, 15286, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_11(44, 15282, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_12(44, 15281, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_13(44, 15280, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_14(44, 15279, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_15(44, 15278, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	COMBAT_RING_16(44, 15277, 1, 1, 0, Constants.COMBAT_ROOM, new Furniture[]{Furniture.BOXING_RING, Furniture.FENCING_RING, Furniture.COMBAT_RING, Furniture.RANGING_PEDESTALS, Furniture.BALANCE_BEAM}),
	/**
	 * Quest hall
	 */
	QUEST_HALL_BOOKCASE(8, 15397, 0, 1, 0, Constants.QUEST_ROOM, new Furniture[]{Furniture.WOODEN_BOOKCASE, Furniture.OAK_BOOKCASE, Furniture.MAHOGANY_BOOKCASE}),
	QUEST_HALL_MAP(45, 15396, 7, 1, 2, Constants.QUEST_ROOM, 5, new Furniture[]{Furniture.SMALL_MAP, Furniture.MEDIUM_MAP, Furniture.LARGE_MAP}),
	QUEST_HALL_SWORD(46, 15395, 7, 6, 2, Constants.QUEST_ROOM, 5, new Furniture[]{Furniture.SILVERLIGHT, Furniture.EXCALIBUR, Furniture.DARKLIGHT}),
	QUEST_HALL_LANDSCAPE(47, 15393, 6, 7, 1, Constants.QUEST_ROOM, 5, new Furniture[]{Furniture.LUMBRIDGE, Furniture.THE_DESERT, Furniture.MORYTANIA, Furniture.KARAMJA, Furniture.ISAFDAR}),
	QUEST_HALL_PORTRAIT(48, 15392, 1, 7, 1, Constants.QUEST_ROOM, 5, new Furniture[]{Furniture.KING_ARTHUR, Furniture.ELENA, Furniture.GIANT_DWARF, Furniture.MISCELLANIANS}),
	QUEST_HALL_GUILD_TROPHY(49, 15394, 0, 6, 0, Constants.QUEST_ROOM, 5, new Furniture[]{Furniture.ANTI_DRAGON_SHIELD, Furniture.AMULET_OF_GLORY, Furniture.CAPE_OF_LEGENDS}),
	QUEST_HALL_STAIRS(36, 15390, 3, 3, 0, Constants.QUEST_ROOM, new Furniture[]{Furniture.OAK_STAIRCASE, Furniture.TEAK_STAIRCASE, Furniture.SPIRAL_STAIRCASE, Furniture.MARBLE_STAIRCASE, Furniture.MARBLE_SPIRAL}),
	QUEST_HALL_RUG_1(7, 15389, 2, 2, 0, Constants.QUEST_ROOM, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	QUEST_HALL_RUG_2(7, 15388, 2, 2, 0, Constants.QUEST_ROOM, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	QUEST_HALL_RUG_3(7, 15387, 2, 2, 0, Constants.QUEST_ROOM, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	
	QUEST_HALL_BOOKCASE_DOWN(8, 15397, 0, 1, 0, Constants.QUEST_HALL_DOWN, new Furniture[]{Furniture.WOODEN_BOOKCASE, Furniture.OAK_BOOKCASE, Furniture.MAHOGANY_BOOKCASE}),
	QUEST_HALL_MAP_DOWN(45, 15396, 7, 1, 2, Constants.QUEST_HALL_DOWN, 5, new Furniture[]{Furniture.SMALL_MAP, Furniture.MEDIUM_MAP, Furniture.LARGE_MAP}),
	QUEST_HALL_SWORD_DOWN(46, 15395, 7, 6, 2, Constants.QUEST_HALL_DOWN, 5, new Furniture[]{Furniture.SILVERLIGHT, Furniture.EXCALIBUR, Furniture.DARKLIGHT}),
	QUEST_HALL_LANDSCAPE_DOWN(47, 15393, 6, 7, 1, Constants.QUEST_HALL_DOWN, 5, new Furniture[]{Furniture.LUMBRIDGE, Furniture.THE_DESERT, Furniture.MORYTANIA, Furniture.KARAMJA, Furniture.ISAFDAR}),
	QUEST_HALL_PORTRAIT_DOWN(48, 15392, 1, 7, 1, Constants.QUEST_HALL_DOWN, 5, new Furniture[]{Furniture.KING_ARTHUR, Furniture.ELENA, Furniture.GIANT_DWARF, Furniture.MISCELLANIANS}),
	QUEST_HALL_GUILD_TROPHY_DOWN(49, 15394, 0, 6, 0, Constants.QUEST_HALL_DOWN, 5, new Furniture[]{Furniture.ANTI_DRAGON_SHIELD, Furniture.AMULET_OF_GLORY, Furniture.CAPE_OF_LEGENDS}),
	QUEST_HALL_STAIRS_DOWN(36, 15390, 3, 3, 0, Constants.QUEST_HALL_DOWN, new Furniture[]{Furniture.OAK_STAIRCASE, Furniture.TEAK_STAIRCASE, Furniture.SPIRAL_STAIRCASE, Furniture.MARBLE_STAIRCASE, Furniture.MARBLE_SPIRAL}),
	QUEST_HALL_RUG_1_DOWN(7, 15389, 2, 2, 0, Constants.QUEST_HALL_DOWN, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	QUEST_HALL_RUG_2_DOWN(7, 15388, 2, 2, 0, Constants.QUEST_HALL_DOWN, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	QUEST_HALL_RUG_3_DOWN(7, 15387, 2, 2, 0, Constants.QUEST_HALL_DOWN, 22, new Dimension(5, 5), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	
	/**
	 * Menagerie
	 */
	MENAGERIE_PET_HOUSE(50, 44909, 1, 1, 2, Constants.MENAGERY, new Furniture[]{Furniture.OAK_PET_HOUSE, Furniture.TEAK_PET_HOUSE, Furniture.MAHOGANY_PET_HOUSE, Furniture.CONSECRATED_PET_HOUSE, Furniture.DESECRATED_PET_HOUSE, Furniture.NATURAL_PET_HOUSE}),
	MENAGERIE_PET_FEEDER(51, 44910, 5, 1, 3, Constants.MENAGERY, new Furniture[]{Furniture.OAK_PET_FEEDER, Furniture.TEAK_PET_FEEDER, Furniture.MAHOGANY_PET_FEEDER}),
	MENAGERIE_OBELISK(52, 44911, 5, 5, 3, Constants.MENAGERY, new Furniture[]{Furniture.MINI_OBELISK}),
	HABITAT_SPACE(53, 44908, 0, 0, 0, Constants.MENAGERY, new Furniture[]{Furniture.GARDEN_HABITAT, Furniture.JUNGLEN_HABITAT, Furniture.DESERT_HABITAT, Furniture.POLAR_HABITAT, Furniture.VOLCANIC_HABITAT}),
	
	/**
	 * Study
	 */
	STUDY_WALL_CHART_1(54, 15423, 0, 1, 0, Constants.STUDY, 5, true, new Furniture[]{Furniture.ALCHEMICAL_CHART, Furniture.ASTRONOMICAL_CHART, Furniture.INFERNO_CHART}),
	STUDY_WALL_CHART_2(54, 15423, 1, 7, 1, Constants.STUDY, 5, true, new Furniture[]{Furniture.ALCHEMICAL_CHART, Furniture.ASTRONOMICAL_CHART, Furniture.INFERNO_CHART}),
	STUDY_WALL_CHART_3(54, 15423, 6, 7, 1, Constants.STUDY, 5, true, new Furniture[]{Furniture.ALCHEMICAL_CHART, Furniture.ASTRONOMICAL_CHART, Furniture.INFERNO_CHART}),
	STUDY_WALL_CHART_4(54, 15423, 7, 1, 2, Constants.STUDY, 5, true, new Furniture[]{Furniture.ALCHEMICAL_CHART, Furniture.ASTRONOMICAL_CHART, Furniture.INFERNO_CHART}),
	STUDY_LECTERN(55, 15420, 2, 2, 2, Constants.STUDY, new Furniture[]{Furniture.OAK_LECTERN, Furniture.EAGLE_LECTERN, Furniture.DEMON_LECTERN, Furniture.TEAK_EAGLE_LECTERN, Furniture.TEAK_DEMON_LECTERN, Furniture.MAHOGANY_EAGLE_LECTERN, Furniture.MAHOGANY_DEMON_LECTERN}),
	STUDY_STATUE(56, 48662, 6, 1, 2, Constants.STUDY, new Furniture[]{Furniture.STATUE}),
	STUDY_CRYSTAL_BALL(57, 15422, 5, 4, 2, Constants.STUDY, new Furniture[]{Furniture.CRYSTAL_BALL, Furniture.ELEMENTAL_SPHERE, Furniture.CRYSTAL_OF_POWER}),
	STUDY_GLOBE(58, 15421, 1, 4, 2, Constants.STUDY, new Furniture[]{Furniture.GLOBE, Furniture.ORNAMENTAL_GLOBE, Furniture.LUNAR_GLOBE, Furniture.CELESTIAL_GLOBE, Furniture.ARMILLARY_GLOBE, Furniture.SMALL_ORRERY, Furniture.LARGE_ORRERY}),
	STUDY_BOOKCASE_1(8, 15425, 3, 7, 1, Constants.STUDY, new Furniture[]{Furniture.WOODEN_BOOKCASE, Furniture.OAK_BOOKCASE, Furniture.MAHOGANY_BOOKCASE}),
	STUDY_BOOKCASE_2(8, 15425, 4, 7, 1, Constants.STUDY, new Furniture[]{Furniture.WOODEN_BOOKCASE, Furniture.OAK_BOOKCASE, Furniture.MAHOGANY_BOOKCASE}),
	STUDY_TELESCOPE(59, 15424, 5, 7, 2, Constants.STUDY, new Furniture[]{Furniture.WOODEN_TELESCOPE, Furniture.TEAK_TELESCOPE, Furniture.MAHOGANY_TELESCOPE}),
	
	/**
	 * Costume room
	 */
	COSTUME_TREASURE_CHEST(60, 18813, 0, 3, 3, Constants.COSTUME_ROOM, new Furniture[]{Furniture.OAK_TREASURE_CHEST, Furniture.TEAK_TREASURE_CHEST, Furniture.MOHOGANY_TREASURE_CHEST}),
	COSTUME_FANCY(61, 18814, 3, 3, 0, Constants.COSTUME_ROOM, new Furniture[]{Furniture.OAK_FANCY_DRESS_BOX, Furniture.TEAK_FANCY_DRESS_BOX, Furniture.MAHOGANY_FANCY_DRESS_BOX}),
	TOY_BOX(62, 18812, 7, 3, 1, Constants.COSTUME_ROOM, new Furniture[]{Furniture.OAK_TOY_BOX, Furniture.TEAK_TOY_BOX, Furniture.MAHOGANY_TOY_BOX}),
	ARMOUR_CASE(63, 18815, 2, 7, 1, Constants.COSTUME_ROOM, new Furniture[]{Furniture.OAK_ARMOR_CASE, Furniture.TEAK_ARMOR_CASE, Furniture.MOHOGANY_ARMOR_CASE}),
	MAGIC_WARDROBE(64, 18811, 3, 7, 1, Constants.COSTUME_ROOM, new Furniture[]{Furniture.OAK_MAGIC_WARDROBE, Furniture.CARVED_OAK_MAGIC_WARDROBE, Furniture.TEAK_MAGIC_WARDROBE, Furniture.CARVED_TEAK_MAGIC_WARDROBE, Furniture.MAHOGANY_MAGIC_WARDROBE, Furniture.GILDED_MAGIC_WARDROBE, Furniture.MARBLE_MAGIC_WARDROBE}),
	CAPE_RACK(65, 18810, 6, 6, 1, Constants.COSTUME_ROOM, new Furniture[]{Furniture.OAK_CAPE_RACK, Furniture.TEAK_CAPE_RACK, Furniture.MAHOGANY_CAPE_RACK, Furniture.GILDED_CAPE_RACK, Furniture.MARBLE_CAPE_RACK, Furniture.MAGIC_CAPE_RACK}),
	
	/**
	 * Chapel
	 */
	CHAPEL_STATUE_1(66, 15275, 0, 0, 2, Constants.CHAPEL, 11, true, new Furniture[]{Furniture.SMALL_STATUES, Furniture.MEDIUM_STATUES, Furniture.LARGE_STATUES}),
	CHAPEL_STATUE_2(66, 15275, 7, 0, 1, Constants.CHAPEL, 11, true, new Furniture[]{Furniture.SMALL_STATUES, Furniture.MEDIUM_STATUES, Furniture.LARGE_STATUES}),
	CHAPEL_LAMP_1(67, 15271, 1, 5, 2, Constants.CHAPEL, true, new Furniture[]{Furniture.STEEL_TORCHES, Furniture.WOODEN_TORCHES, Furniture.STEEL_CANDLESTICKS, Furniture.GOLD_CANDLESTICKS, Furniture.INCENSE_BURNERS, Furniture.MAHOGANY_BURNERS, Furniture.MARBLE_BURNERS}),
	CHAPEL_LAMP_2(67, 15271, 6, 5, 2, Constants.CHAPEL, true, new Furniture[]{Furniture.STEEL_TORCHES, Furniture.WOODEN_TORCHES, Furniture.STEEL_CANDLESTICKS, Furniture.GOLD_CANDLESTICKS, Furniture.INCENSE_BURNERS, Furniture.MAHOGANY_BURNERS, Furniture.MARBLE_BURNERS}),
	CHAPEL_MUSICAL(68, 15276, 7, 3, 1, Constants.CHAPEL, new Furniture[]{Furniture.WINDCHIMES, Furniture.BELLS, Furniture.ORGAN}),
	CHAPEL_ALTAR(69, 15270, 3, 5, 0, Constants.CHAPEL, new Furniture[]{Furniture.OAK_ALTAR, Furniture.TEAK_ALTAR, Furniture.CLOTH_ALTAR, Furniture.MAHOGANY_ALTAR, Furniture.LIMESTONE_ALTAR, Furniture.MARBLE_ALTAR, Furniture.GILDED_ALTAR}),
	CHAPEL_ICON(70, 15269, 3, 7, 0, Constants.CHAPEL, new Furniture[]{Furniture.GUTHIX_SYMBOL, Furniture.SARADOMIN_SYMBOL, Furniture.ZAMORAK_SYMBOL, Furniture.GUTHIX_ICON, Furniture.SARADOMIN_ICON, Furniture.ZAMORAK_ICON, Furniture.ICON_OF_BOB}),
	CHAPEL_WINDOW_0(71, 13733, 0, 2, 0, Constants.CHAPEL, 0, true, new Furniture[]{Furniture.SHUTTERED_WINDOW, Furniture.DECORATIVE_WINDOW, Furniture.STAINED_GLASS}),
	CHAPEL_WINDOW_1(71, 13733, 0, 5, 0, Constants.CHAPEL, 0, true, new Furniture[]{Furniture.SHUTTERED_WINDOW, Furniture.DECORATIVE_WINDOW, Furniture.STAINED_GLASS}),
	CHAPEL_WINDOW_2(71, 13733, 2, 7, 1, Constants.CHAPEL, 0, true, new Furniture[]{Furniture.SHUTTERED_WINDOW, Furniture.DECORATIVE_WINDOW, Furniture.STAINED_GLASS}),
	CHAPEL_WINDOW_3(71, 13733, 5, 7, 1, Constants.CHAPEL, 0, true, new Furniture[]{Furniture.SHUTTERED_WINDOW, Furniture.DECORATIVE_WINDOW, Furniture.STAINED_GLASS}),
	CHAPEL_WINDOW_4(71, 13733, 7, 5, 2, Constants.CHAPEL, 0, true, new Furniture[]{Furniture.SHUTTERED_WINDOW, Furniture.DECORATIVE_WINDOW, Furniture.STAINED_GLASS}),
	CHAPEL_WINDOW_5(71, 13733, 7, 2, 2, Constants.CHAPEL, 0, true, new Furniture[]{Furniture.SHUTTERED_WINDOW, Furniture.DECORATIVE_WINDOW, Furniture.STAINED_GLASS}),
	
	CHAPEL_RUG_1(7, 15270, 4, 1, 0, Constants.CHAPEL, 22, new Dimension(1, 4), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	CHAPEL_RUG_2(7, 15274, 4, 1, 0, Constants.CHAPEL, 22, new Dimension(1, 4), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	CHAPEL_RUG_3(7, 15273, 4, 1, 0, Constants.CHAPEL, 22, new Dimension(1, 4), new Furniture[]{Furniture.BROWN_RUG, Furniture.RUG, Furniture.OPULENT_RUG}),
	
	/**
	 * Portal room
	 */
	PORTAL_1(72, 15406, 0, 3, 1, Constants.PORTAL_ROOM, new Furniture[]{Furniture.TEAK_PORTAL, Furniture.MAHOGANY_PORTAL, Furniture.MARBLE_PORTAL}),
	PORTAL_2(72, 15407, 3, 7, 2, Constants.PORTAL_ROOM, new Furniture[]{Furniture.TEAK_PORTAL, Furniture.MAHOGANY_PORTAL, Furniture.MARBLE_PORTAL}),
	PORTAL_3(72, 15408, 7, 3, 3, Constants.PORTAL_ROOM, new Furniture[]{Furniture.TEAK_PORTAL, Furniture.MAHOGANY_PORTAL, Furniture.MARBLE_PORTAL}),
	PORTAL_CENTREPIECE(73, 15409, 3, 3, 0, Constants.PORTAL_ROOM, new Furniture[]{Furniture.TELEPORT_FOCUS, Furniture.GREATER_FOCUS, Furniture.SCYING_POOL}),
	
	/**
	 * Formal garden
	 */
	FORMAL_CENTREPIECE(74, 15368, 3, 3, 0, Constants.FORMAL_GARDEN, new Furniture[]{Furniture.EXIT_PORTAL_, Furniture.GAZEBO, Furniture.DUNGEON_ENTERANCE, Furniture.SMALL_FOUNTAIN, Furniture.LARGE_FOUNTAIN, Furniture.POSH_FOUNTAIN}),
	FORMAL_SMALL_PLANT_1_0(75, 15375, 5, 1, 2, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.SUN_FLOWER, Furniture.MARIGOLDS, Furniture.ROSES}),
	FORMAL_SMALL_PLANT_1_1(75, 15375, 6, 2, 2, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.SUN_FLOWER, Furniture.MARIGOLDS, Furniture.ROSES}),
	FORMAL_SMALL_PLANT_1_2(75, 15375, 1, 5, 0, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.SUN_FLOWER, Furniture.MARIGOLDS, Furniture.ROSES}),
	FORMAL_SMALL_PLANT_1_3(75, 15375, 2, 6, 0, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.SUN_FLOWER, Furniture.MARIGOLDS, Furniture.ROSES}),
	FORMAL_SMALL_PLANT_2_0(76, 15376, 1, 2, 2, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.ROSEMARY, Furniture.DAFFODILS, Furniture.BLUEBELLS}),
	FORMAL_SMALL_PLANT_2_1(76, 15376, 2, 1, 2, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.ROSEMARY, Furniture.DAFFODILS, Furniture.BLUEBELLS}),
	FORMAL_SMALL_PLANT_2_2(76, 15376, 5, 6, 0, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.ROSEMARY, Furniture.DAFFODILS, Furniture.BLUEBELLS}),
	FORMAL_SMALL_PLANT_2_3(76, 15376, 6, 5, 0, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.ROSEMARY, Furniture.DAFFODILS, Furniture.BLUEBELLS}),
	FORMAL_BIG_PLANT_1_0(75, 15373, 6, 1, 2, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.SUN_FLOWER, Furniture.MARIGOLDS, Furniture.ROSES}),
	FORMAL_BIG_PLANT_1_1(75, 15373, 1, 6, 0, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.SUN_FLOWER, Furniture.MARIGOLDS, Furniture.ROSES}),
	FORMAL_BIG_PLANT_2_0(76, 15374, 1, 1, 2, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.ROSEMARY, Furniture.DAFFODILS, Furniture.BLUEBELLS}),
	FORMAL_BIG_PLANT_2_1(76, 15374, 6, 6, 0, Constants.FORMAL_GARDEN, true, new Furniture[]{Furniture.ROSEMARY, Furniture.DAFFODILS, Furniture.BLUEBELLS}),
	FORMAL_HEDGE_1(77, 15370, 0, 0, 0, Constants.FORMAL_GARDEN, new Furniture[]{Furniture.THORNY_HEDGE, Furniture.NICE_HEDGE, Furniture.SMALL_BOX_HEDGE, Furniture.TOPIARY_HEDGE, Furniture.FANCY_HEDGE, Furniture.TALL_FANCY_HEDGE, Furniture.TALL_BOX_HEDGE}),
	FORMAL_HEDGE_2(77, 15371, 0, 0, 0, Constants.FORMAL_GARDEN, new Furniture[]{Furniture.THORNY_HEDGE, Furniture.NICE_HEDGE, Furniture.SMALL_BOX_HEDGE, Furniture.TOPIARY_HEDGE, Furniture.FANCY_HEDGE, Furniture.TALL_FANCY_HEDGE, Furniture.TALL_BOX_HEDGE}),
	FORMAL_HEDGE_3(77, 15372, 0, 0, 0, Constants.FORMAL_GARDEN, new Furniture[]{Furniture.THORNY_HEDGE, Furniture.NICE_HEDGE, Furniture.SMALL_BOX_HEDGE, Furniture.TOPIARY_HEDGE, Furniture.FANCY_HEDGE, Furniture.TALL_FANCY_HEDGE, Furniture.TALL_BOX_HEDGE}),
	FORMAL_FENCE(78, 15369, 0, 0, 0, Constants.FORMAL_GARDEN, new Furniture[]{Furniture.BOUNDARY_STONES, Furniture.WOODEN_FENCE, Furniture.STONE_WALL, Furniture.IRON_RAILINGS, Furniture.PICKET_FENCE, Furniture.GARDEN_FENCE, Furniture.MARBLE_WALL}),
	/**
	 * Throne room
	 */
	THRONE_ROOM_THRONE_1(79, 15426, 3, 6, 0, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.OAK_THRONE, Furniture.TEAK_THRONE, Furniture.MAHOGANY_THRONE, Furniture.GILDED_THRONE, Furniture.SKELETON_THRONE, Furniture.CRYSTAL_THRONE, Furniture.DEMONIC_THRONE}),
	THRONE_ROOM_THRONE_2(79, 15426, 4, 6, 0, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.OAK_THRONE, Furniture.TEAK_THRONE, Furniture.MAHOGANY_THRONE, Furniture.GILDED_THRONE, Furniture.SKELETON_THRONE, Furniture.CRYSTAL_THRONE, Furniture.DEMONIC_THRONE}),
	THRONE_ROOM_LEVER(80, 15435, 6, 6, 0, Constants.THRONE_ROOM, new Furniture[]{Furniture.OAK_LEVER, Furniture.TEAK_LEVER, Furniture.MAHOGANY_LEVER}),
	THRONE_ROOM_TRAPDOOR(81, 15438, 1, 6, 0, Constants.THRONE_ROOM, 22, new Furniture[]{Furniture.OAK_TRAPDOOR, Furniture.TEAK_TRAPDOOR, Furniture.MAHOGANY_TRAPDOOR}),
	THRONE_DECORATION_1(83, 15434, 3, 7, 1, Constants.THRONE_ROOM, 4, true, new Furniture[]{Furniture.OAK_DECORATION_, Furniture.TEAK_DECORATION_, Furniture.GILDED_DECORATION_, Furniture.ROUND_SHIELD, Furniture.SQUARE_SHEILD, Furniture.KITE_DECORATION}),
	THRONE_DECORATION_2(83, 15434, 4, 7, 1, Constants.THRONE_ROOM, 4, true, new Furniture[]{Furniture.OAK_DECORATION_, Furniture.TEAK_DECORATION_, Furniture.GILDED_DECORATION_, Furniture.ROUND_SHIELD, Furniture.SQUARE_SHEILD, Furniture.KITE_DECORATION}),
	THRONE_TRAP_1(84, 15431, 3, 3, 0, Constants.THRONE_ROOM, 22, true, new Furniture[]{Furniture.FLOOR_DECORATION, Furniture.STEEL_CAGE, Furniture.TRAPDOOR, Furniture.LESSER_MAGIC_CIRCLE, Furniture.GREATER_MAGIC_CIRCLE}),
	THRONE_TRAP_2(84, 15431, 4, 3, 0, Constants.THRONE_ROOM, 22, true, new Furniture[]{Furniture.FLOOR_DECORATION, Furniture.STEEL_CAGE, Furniture.TRAPDOOR, Furniture.LESSER_MAGIC_CIRCLE, Furniture.GREATER_MAGIC_CIRCLE}),
	THRONE_TRAP_3(84, 15431, 3, 4, 0, Constants.THRONE_ROOM, 22, true, new Furniture[]{Furniture.FLOOR_DECORATION, Furniture.STEEL_CAGE, Furniture.TRAPDOOR, Furniture.LESSER_MAGIC_CIRCLE, Furniture.GREATER_MAGIC_CIRCLE}),
	THRONE_TRAP_4(84, 15431, 4, 4, 0, Constants.THRONE_ROOM, 22, true, new Furniture[]{Furniture.FLOOR_DECORATION, Furniture.STEEL_CAGE, Furniture.TRAPDOOR, Furniture.LESSER_MAGIC_CIRCLE, Furniture.GREATER_MAGIC_CIRCLE}),
	THRONE_BENCH_1(20, 15436, 0, 0, 3, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	THRONE_BENCH_2(20, 15436, 0, 1, 3, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	THRONE_BENCH_3(20, 15436, 0, 2, 3, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	THRONE_BENCH_4(20, 15436, 0, 3, 3, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	THRONE_BENCH_5(20, 15436, 0, 4, 3, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	THRONE_BENCH_6(20, 15436, 0, 5, 3, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	THRONE_BENCH_7(20, 15437, 7, 0, 1, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	THRONE_BENCH_8(20, 15437, 7, 1, 1, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	THRONE_BENCH_9(20, 15437, 7, 2, 1, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	THRONE_BENCH_10(20, 15437, 7, 3, 1, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	THRONE_BENCH_11(20, 15437, 7, 4, 1, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	THRONE_BENCH_12(20, 15437, 7, 5, 1, Constants.THRONE_ROOM, true, new Furniture[]{Furniture.WOODEN_BENCH, Furniture.OAK_BENCH, Furniture.CARVED_OAK_BENCH, Furniture.TEAK_DINING_BENCH, Furniture.CARVED_TEAK_DINING_BENCH, Furniture.MAHOGANY_BENCH, Furniture.GILDED_BENCH}),
	/**
	 * Oubliette
	 */
	OUBLIETTE_FLOOR_1(85, 15350, 2, 2, 22, Constants.OUBLIETTE, new Furniture[]{Furniture.TENTACLE_POOL, Furniture.SPIKES, Furniture.FLAME_PIT, Furniture.ROCNAR}),
	OUBLIETTE_FLOOR_2(85, 15348, 2, 2, 22, Constants.OUBLIETTE, new Furniture[]{Furniture.TENTACLE_POOL, Furniture.SPIKES, Furniture.FLAME_PIT, Furniture.ROCNAR}),
	OUBLIETTE_FLOOR_3(85, 15347, 2, 2, 22, Constants.OUBLIETTE, new Furniture[]{Furniture.TENTACLE_POOL, Furniture.SPIKES, Furniture.FLAME_PIT, Furniture.ROCNAR}),
	OUBLIETTE_FLOOR_4(85, 15351, 2, 2, 22, Constants.OUBLIETTE, new Furniture[]{Furniture.TENTACLE_POOL, Furniture.SPIKES, Furniture.FLAME_PIT, Furniture.ROCNAR}),
	OUBLIETTE_FLOOR_5(85, 15349, 2, 2, 22, Constants.OUBLIETTE, new Furniture[]{Furniture.TENTACLE_POOL, Furniture.SPIKES, Furniture.FLAME_PIT, Furniture.ROCNAR}),
	OUBLIETTE_CAGE_1(86, 15353, 2, 2, 0, Constants.OUBLIETTE, 0, new Furniture[]{Furniture.OAK_CAGE, Furniture.OAK_AND_STEEL_CAGE, Furniture.STEEL_CAGE_, Furniture.SPIKED_CAGE, Furniture.BONE_CAGE}),
	OUBLIETTE_CAGE_2(86, 15352, 2, 2, 0, Constants.OUBLIETTE, 0, new Furniture[]{Furniture.OAK_CAGE, Furniture.OAK_AND_STEEL_CAGE, Furniture.STEEL_CAGE_, Furniture.SPIKED_CAGE, Furniture.BONE_CAGE}),
	OUBLIETTE_GUARD(87, 15354, 0, 0, 3, Constants.OUBLIETTE, new Furniture[]{Furniture.SKELETON_GUARD, Furniture.GUARD_DOG, Furniture.HOBGOBLIN, Furniture.BABY_RED_DRAGON, Furniture.HUGE_SPIDER, Furniture.TROLL, Furniture.HELLHOUND}),
	OUBLIETTE_LADDER(88, 15356, 1, 6, 0, Constants.OUBLIETTE, new Furniture[]{Furniture.OAK_LADDER, Furniture.TEAK_LADDER, Furniture.MAHOGANY_LADDER}),
	OUBLIETTE_DECORATION_1(89, 15331, 0, 2, 0, Constants.OUBLIETTE, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	OUBLIETTE_DECORATION_2(89, 15331, 2, 7, 1, Constants.OUBLIETTE, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	OUBLIETTE_DECORATION_3(89, 15331, 7, 5, 2, Constants.OUBLIETTE, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	OUBLIETTE_DECORATION_4(89, 15331, 5, 0, 3, Constants.OUBLIETTE, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	OUBLIETTE_LIGHT_1(90, 15355, 2, 0, 3, Constants.OUBLIETTE, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	OUBLIETTE_LIGHT_2(90, 15355, 0, 5, 0, Constants.OUBLIETTE, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	OUBLIETTE_LIGHT_3(90, 15355, 5, 7, 1, Constants.OUBLIETTE, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	OUBLIETTE_LIGHT_4(90, 15355, 7, 2, 2, Constants.OUBLIETTE, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	/**
	 * Corridor
	 */
	CORRIDOR_LIGHT_1(90, 15330, 3, 1, 0, Constants.CORRIDOR, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	CORRIDOR_LIGHT_2(90, 15330, 3, 6, 0, Constants.CORRIDOR, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	CORRIDOR_LIGHT_3(90, 15330, 4, 1, 2, Constants.CORRIDOR, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	CORRIDOR_LIGHT_4(90, 15330, 4, 6, 2, Constants.CORRIDOR, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	CORRIDOR_DECORATION_1(89, 15331, 3, 4, 0, Constants.CORRIDOR, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	CORRIDOR_DECORATION_2(89, 15331, 4, 3, 2, Constants.CORRIDOR, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	CORRIDOR_GUARD(87, 15323, 3, 3, 2, Constants.CORRIDOR, new Furniture[]{Furniture.SKELETON_GUARD, Furniture.GUARD_DOG, Furniture.HOBGOBLIN, Furniture.BABY_RED_DRAGON, Furniture.HUGE_SPIDER, Furniture.TROLL, Furniture.HELLHOUND}),
	CORRIDOR_TRAP_1(91, 15325, 3, 2, 0, Constants.CORRIDOR, 22, true, new Furniture[]{Furniture.SPIKE_TRAP, Furniture.MAN_TRAP, Furniture.TANGLE_TRAP, Furniture.MARBLE_TRAP, Furniture.TELEPORT_TRAP}),
	CORRIDOR_TRAP_2(91, 15325, 4, 2, 0, Constants.CORRIDOR, 22, true, new Furniture[]{Furniture.SPIKE_TRAP, Furniture.MAN_TRAP, Furniture.TANGLE_TRAP, Furniture.MARBLE_TRAP, Furniture.TELEPORT_TRAP}),
	CORRIDOR_TRAP_3(91, 15324, 3, 5, 0, Constants.CORRIDOR, 22, true, new Furniture[]{Furniture.SPIKE_TRAP, Furniture.MAN_TRAP, Furniture.TANGLE_TRAP, Furniture.MARBLE_TRAP, Furniture.TELEPORT_TRAP}),
	CORRIDOR_TRAP_4(91, 15324, 4, 5, 0, Constants.CORRIDOR, 22, true, new Furniture[]{Furniture.SPIKE_TRAP, Furniture.MAN_TRAP, Furniture.TANGLE_TRAP, Furniture.MARBLE_TRAP, Furniture.TELEPORT_TRAP}),
	CORRIDOR_DOOR_1(92, 15329, 3, 1, 3, Constants.CORRIDOR, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	CORRIDOR_DOOR_2(92, 15328, 4, 1, 3, Constants.CORRIDOR, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	CORRIDOR_DOOR_3(92, 15326, 3, 6, 1, Constants.CORRIDOR, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	CORRIDOR_DOOR_4(92, 15327, 4, 6, 1, Constants.CORRIDOR, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	
	/**
	 * junction
	 */
	JUNCTION_TRAP_1(91, 15325, 3, 2, 0, Constants.JUNCTION, 22, true, new Furniture[]{Furniture.SPIKE_TRAP, Furniture.MAN_TRAP, Furniture.TANGLE_TRAP, Furniture.MARBLE_TRAP, Furniture.TELEPORT_TRAP}),
	JUNCTION_TRAP_2(91, 15325, 4, 2, 0, Constants.JUNCTION, 22, true, new Furniture[]{Furniture.SPIKE_TRAP, Furniture.MAN_TRAP, Furniture.TANGLE_TRAP, Furniture.MARBLE_TRAP, Furniture.TELEPORT_TRAP}),
	JUNCTION_TRAP_3(91, 15324, 3, 5, 0, Constants.JUNCTION, 22, true, new Furniture[]{Furniture.SPIKE_TRAP, Furniture.MAN_TRAP, Furniture.TANGLE_TRAP, Furniture.MARBLE_TRAP, Furniture.TELEPORT_TRAP}),
	JUNCTION_TRAP_4(91, 15324, 4, 5, 0, Constants.JUNCTION, 22, true, new Furniture[]{Furniture.SPIKE_TRAP, Furniture.MAN_TRAP, Furniture.TANGLE_TRAP, Furniture.MARBLE_TRAP, Furniture.TELEPORT_TRAP}),
	JUNCTION_LIGHT_1(90, 15330, 3, 1, 0, Constants.JUNCTION, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	JUNCTION_LIGHT_2(90, 15330, 1, 4, 1, Constants.JUNCTION, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	JUNCTION_LIGHT_3(90, 15330, 4, 6, 2, Constants.JUNCTION, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	JUNCTION_LIGHT_4(90, 15330, 6, 3, 3, Constants.JUNCTION, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	JUNCTION_DECORATION_1(89, 15331, 1, 3, 3, Constants.JUNCTION, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	JUNCTION_DECORATION_2(89, 15331, 6, 4, 1, Constants.JUNCTION, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	JUNCTION_GUARD(87, 15323, 3, 3, 2, Constants.JUNCTION, new Furniture[]{Furniture.SKELETON_GUARD, Furniture.GUARD_DOG, Furniture.HOBGOBLIN, Furniture.BABY_RED_DRAGON, Furniture.HUGE_SPIDER, Furniture.TROLL, Furniture.HELLHOUND}),
	/**
	 * Dungeon stair room
	 */
	DUNG_STAIR_LIGHT_1(90, 34138, 2, 1, 3, Constants.DUNGEON_STAIR_ROOM, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	DUNG_STAIR_LIGHT_2(90, 15330, 1, 2, 0, Constants.DUNGEON_STAIR_ROOM, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	DUNG_STAIR_LIGHT_3(90, 15330, 2, 6, 1, Constants.DUNGEON_STAIR_ROOM, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	DUNG_STAIR_LIGHT_4(90, 34138, 1, 5, 0, Constants.DUNGEON_STAIR_ROOM, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	DUNG_STAIR_LIGHT_5(90, 34138, 5, 6, 1, Constants.DUNGEON_STAIR_ROOM, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	DUNG_STAIR_LIGHT_6(90, 15330, 6, 5, 2, Constants.DUNGEON_STAIR_ROOM, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	DUNG_STAIR_LIGHT_7(90, 34138, 6, 2, 2, Constants.DUNGEON_STAIR_ROOM, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	DUNG_STAIR_LIGHT_8(90, 15330, 5, 1, 3, Constants.DUNGEON_STAIR_ROOM, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	DUNG_STAIR_DECORATION_1(89, 15331, 1, 6, 1, Constants.DUNGEON_STAIR_ROOM, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	DUNG_STAIR_DECORATION_2(89, 15331, 6, 1, 3, Constants.DUNGEON_STAIR_ROOM, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	DUNG_STAIR_GUARD_1(87, 15337, 1, 1, 2, Constants.DUNGEON_STAIR_ROOM, new Furniture[]{Furniture.SKELETON_GUARD, Furniture.GUARD_DOG, Furniture.HOBGOBLIN, Furniture.BABY_RED_DRAGON, Furniture.HUGE_SPIDER, Furniture.TROLL, Furniture.HELLHOUND}),
	DUNG_STAIR_GUARD_2(87, 15336, 5, 5, 0, Constants.DUNGEON_STAIR_ROOM, new Furniture[]{Furniture.SKELETON_GUARD, Furniture.GUARD_DOG, Furniture.HOBGOBLIN, Furniture.BABY_RED_DRAGON, Furniture.HUGE_SPIDER, Furniture.TROLL, Furniture.HELLHOUND}),
	DUNG_STAIRS(36, 15380, 3, 3, 0, Constants.DUNGEON_STAIR_ROOM, new Furniture[]{Furniture.OAK_STAIRCASE, Furniture.TEAK_STAIRCASE, Furniture.SPIRAL_STAIRCASE, Furniture.MARBLE_STAIRCASE, Furniture.MARBLE_SPIRAL}),
	/**
	 * Pit
	 */
	PIT_TRAP_1(93, 39230, 3, 1, 0, Constants.PIT, new Furniture[]{Furniture.MINOR_PIT_TRAP, Furniture.MAJOR_PIT_TRAP, Furniture.SUPERIOR_PIT_TRAP}),
	PIT_TRAP_2(93, 39231, 5, 3, 3, Constants.PIT, new Furniture[]{Furniture.MINOR_PIT_TRAP, Furniture.MAJOR_PIT_TRAP, Furniture.SUPERIOR_PIT_TRAP}),
	PIT_TRAP_3(93, 36692, 3, 5, 2, Constants.PIT, new Furniture[]{Furniture.MINOR_PIT_TRAP, Furniture.MAJOR_PIT_TRAP, Furniture.SUPERIOR_PIT_TRAP}),
	PIT_TRAP_4(93, 39229, 1, 3, 1, Constants.PIT, new Furniture[]{Furniture.MINOR_PIT_TRAP, Furniture.MAJOR_PIT_TRAP, Furniture.SUPERIOR_PIT_TRAP}),
	PIT_GUARDIAN(94, 36676, 3, 3, 0, Constants.PIT, new Furniture[]{Furniture.PIT_DOG, Furniture.PIT_OGRE, Furniture.PIT_PROTECTOR, Furniture.PIT_SCARABITE, Furniture.PIT_BLACK_DEMON, Furniture.PIT_IRON_DRAGON}),
	PIT_LIGHT_1(90, 34138, 2, 1, 3, Constants.PIT, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	PIT_LIGHT_2(90, 15330, 1, 2, 0, Constants.PIT, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	PIT_LIGHT_3(90, 15330, 2, 6, 1, Constants.PIT, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	PIT_LIGHT_4(90, 34138, 1, 5, 0, Constants.PIT, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	PIT_LIGHT_5(90, 34138, 5, 6, 1, Constants.PIT, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	PIT_LIGHT_6(90, 15330, 6, 5, 2, Constants.PIT, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	PIT_LIGHT_7(90, 34138, 6, 2, 2, Constants.PIT, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	PIT_LIGHT_8(90, 15330, 5, 1, 3, Constants.PIT, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	PIT_DECORATION_1(89, 15331, 1, 6, 1, Constants.PIT, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	PIT_DECORATION_2(89, 15331, 6, 1, 3, Constants.PIT, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	
	PIT_DOOR_1(92, 36675, 3, 1, 3, Constants.PIT, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	PIT_DOOR_2(92, 36672, 4, 1, 3, Constants.PIT, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	PIT_DOOR_3(92, 36672, 3, 6, 1, Constants.PIT, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	PIT_DOOR_4(92, 36675, 4, 6, 1, Constants.PIT, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	PIT_DOOR_5(92, 36672, 1, 3, 0, Constants.PIT, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	PIT_DOOR_6(92, 36675, 1, 4, 0, Constants.PIT, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	PIT_DOOR_7(92, 36675, 6, 3, 0, Constants.PIT, 2, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	PIT_DOOR_8(92, 36672, 6, 4, 0, Constants.PIT, 2, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	/**
	 * Treasure room
	 */
	TREASURE_ROOM_DECORATION_1(89, 15331, 1, 2, 0, Constants.TREASURE_ROOM, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	TREASURE_ROOM_DECORATION_2(89, 15331, 6, 2, 2, Constants.TREASURE_ROOM, 4, true, new Furniture[]{Furniture.DECORATIVE_BLOOD, Furniture.DECORATIVE_PIPE, Furniture.HANGING_SKELETON}),
	TREASURE_ROOM_LIGHT_1(90, 15330, 1, 5, 0, Constants.TREASURE_ROOM, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	TREASURE_ROOM_LIGHT_2(90, 15330, 6, 5, 2, Constants.TREASURE_ROOM, 4, true, new Furniture[]{Furniture.CANDLE, Furniture.TORCH, Furniture.SKULL_TORCH}),
	TREASURE_MONSTER(95, 15257, 3, 3, 0, Constants.TREASURE_ROOM, new Furniture[]{Furniture.DEMON, Furniture.KALPHITE_SOLDIER, Furniture.TOK_XIL, Furniture.DAGANNOTH, Furniture.STEEL_DRAGON}),
	TREASURE_CHEST(96, 15256, 2, 6, 0, Constants.TREASURE_ROOM, new Furniture[]{Furniture.WOODEN_CRATE, Furniture.OAK_CRATE, Furniture.TEAK_CRATE, Furniture.MAHOGANY_CRATE, Furniture.MAGIC_CRATE}),
	TREASURE_DECORATION_1(83, 15259, 3, 6, 1, Constants.TREASURE_ROOM, 4, true, new Furniture[]{Furniture.OAK_DECORATION_, Furniture.TEAK_DECORATION_, Furniture.GILDED_DECORATION_, Furniture.ROUND_SHIELD, Furniture.SQUARE_SHEILD, Furniture.KITE_DECORATION}),
	TREASURE_DECORATION_2(83, 15259, 4, 6, 1, Constants.TREASURE_ROOM, 4, true, new Furniture[]{Furniture.OAK_DECORATION_, Furniture.TEAK_DECORATION_, Furniture.GILDED_DECORATION_, Furniture.ROUND_SHIELD, Furniture.SQUARE_SHEILD, Furniture.KITE_DECORATION}),
	TREASURE_ROOM_DOOR_1(92, 15327, 3, 1, 3, Constants.TREASURE_ROOM, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR}),
	TREASURE_ROOM_DOOR_2(92, 15326, 4, 1, 3, Constants.TREASURE_ROOM, 0, new Furniture[]{Furniture.OAK_DOOR, Furniture.STEEL_PLATED_DOOR, Furniture.MARBLE_DOOR});
	
	private final Furniture[] furnitures;
	private int hotSpotId, objectId, xOffset, yOffset, standardRotation, objectType, roomType;
	private boolean mutiple;
	private Dimension carpetDim;
	
	HotSpots(int hotSpotId, int objectId, int xOffset, int yOffset, int standardRotation, int roomType, Furniture[] furnitures) {
		this.hotSpotId = hotSpotId;
		this.objectId = objectId;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.standardRotation = standardRotation;
		this.furnitures = furnitures;
		carpetDim = null;
		setObjectType(10);
		setMutiple(false);
		setRoomType(roomType);
		
	}
	
	HotSpots(int hotSpotId, int objectId, int xOffset, int yOffset, int standardRotation, int roomType, int objectType, Furniture[] furnitures) {
		this(hotSpotId, objectId, xOffset, yOffset, standardRotation, roomType, furnitures);
		this.setObjectType(objectType);
	}
	
	HotSpots(int hotSpotId, int objectId, int xOffset, int yOffset, int standardRotation, int roomType, int objectType, boolean mutiple, Furniture[] furnitures) {
		this(hotSpotId, objectId, xOffset, yOffset, standardRotation, roomType, objectType, furnitures);
		this.setMutiple(mutiple);
	}
	
	HotSpots(int hotSpotId, int objectId, int xOffset, int yOffset, int standardRotation, int roomType, boolean mutiple, Furniture[] furnitures) {
		this(hotSpotId, objectId, xOffset, yOffset, standardRotation, roomType, furnitures);
		this.setMutiple(mutiple);
	}
	
	HotSpots(int hotSpotId, int objectId, int xOffset, int yOffset, int standardRotation, int roomType, int objectType, Dimension carpetDim, Furniture[] furnitures) {
		this(hotSpotId, objectId, xOffset, yOffset, standardRotation, roomType, objectType, furnitures);
		this.setCarpetDim(carpetDim);
	}
	
	public int getRotation(int roomRot) {
		if(roomRot == 0)
			return standardRotation;
		else if(roomRot == 1)
			return (standardRotation == 3 ? 0 : standardRotation + 1);
		else if(roomRot == 2)
			return (standardRotation == 2 ? 0 : standardRotation + 2);
		else if(roomRot == 3)
			return (standardRotation == 1 ? 0 : standardRotation + 3);
		return standardRotation;
	}
	
	public static int getRotation_2(int rot, int roomRot) {
		if(roomRot == 0)
			return rot;
		else if(roomRot == 1)
			return (rot == 3 ? 0 : rot + 1);
		else if(roomRot == 2)
			return (rot == 2 ? 0 : rot + 2);
		else if(roomRot == 3)
			return (rot == 1 ? 0 : rot + 3);
		return rot;
	}
	
	public int getHotSpotId() {
		return hotSpotId;
	}
	
	public int getObjectId() {
		return objectId;
	}
	
	public int getXOffset() {
		return xOffset;
	}
	
	public int getYOffset() {
		return yOffset;
	}
	
	public int getObjectType() {
		return objectType;
	}
	
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	
	public Dimension getCarpetDim() {
		return carpetDim;
	}
	
	public void setCarpetDim(Dimension carpetDim) {
		this.carpetDim = carpetDim;
	}
	
	public boolean isMutiple() {
		return mutiple;
	}
	
	public void setMutiple(boolean mutiple) {
		this.mutiple = mutiple;
	}
	
	public int getRoomType() {
		return roomType;
	}
	
	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}
	
	public static void action() {
		for(HotSpots spot : values()) {
			ObjectAction e = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					if(!player.getHouse().get().isBuilding())
						return true;
					player.out(new SendObjectsConstruction(spot));
					return true;
				}
			};
			e.registerCons(spot.getObjectId());
		}
		
		/*
		 * Creating rooms.
		 */
		ObjectAction roomCreation = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(!player.getHouse().get().isBuilding())
					return true;
				if(RoomManipulation.roomExists(player)) {
					player.getDialogueBuilder().append(new OptionDialogue(t -> {
						if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
							RoomManipulation.rotateRoom(0, player);
						} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
							RoomManipulation.rotateRoom(1, player);
						} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
							RoomManipulation.deleteRoom(player, player.getPosition().getZ());
						}
						player.closeWidget();
					}, "Rotate clockwise", "Rotate counter-clockwise", "Remove"));
				} else
					player.widget(-14);
				return true;
			}
		};
		roomCreation.registerCons(15314);
		roomCreation.registerCons(15313);
		roomCreation.registerCons(15306);
		roomCreation.registerCons(15305);
		roomCreation.registerCons(15308);
		roomCreation.registerCons(15307);
	}
	
	public Furniture[] getFurnitures() {
		return furnitures;
	}
}