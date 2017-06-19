package net.edge.content.skill.construction.furniture;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.Dice;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.skill.construction.data.Constants;
import net.edge.content.skill.construction.room.Room;
import net.edge.content.skill.construction.room.RoomManipulation;
import net.edge.event.impl.ObjectEvent;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import java.awt.*;
import java.util.EnumSet;

import static net.edge.content.dialogue.impl.OptionDialogue.OptionType.FIRST_OPTION;
import static net.edge.content.dialogue.impl.OptionDialogue.OptionType.SECOND_OPTION;
import static net.edge.content.dialogue.impl.OptionDialogue.OptionType.THIRD_OPTION;

/**
 * Enumeration of all of the {@link Construction} hotspots.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum HotSpots {
	/**
	 * Garden
	 */
	CENTREPIECE(0, 15361, 3, 3, 0, Constants.GARDEN),
	TREE_1(1, 15362, 1, 5, 0, Constants.GARDEN),
	TREE_2(1, 15363, 6, 6, 0, Constants.GARDEN),
	SMALL_PLANT_1(2, 15366, 3, 1, 0, Constants.GARDEN),
	SMALL_PLANT_2(3, 15367, 4, 5, 0, Constants.GARDEN),
	BIG_PLANT_1(4, 15364, 6, 0, 0, Constants.GARDEN),
	BIG_PLANT_2(5, 15365, 0, 0, 0, Constants.GARDEN),
	/**
	 * Parlour
	 */
	PARLOUR_CHAIR_1(6, 15410, 2, 4, 2, Constants.PARLOUR, 11),
	PARLOUR_CHAIR_2(6, 15412, 4, 3, 2, Constants.PARLOUR),
	PARLOUR_CHAIR_3(6, 15411, 5, 4, 1, Constants.PARLOUR, 11),
	PARLOUR_RUG_1(7, 15414, 2, 2, 0, Constants.PARLOUR, 22, new Dimension(5, 5)),
	PARLOUR_RUG_2(7, 15415, 2, 2, 0, Constants.PARLOUR, 22, new Dimension(5, 5)),
	PARLOUR_RUG_3(7, 15413, 2, 2, 0, Constants.PARLOUR, 22, new Dimension(5, 5)),
	PARLOUR_BOOKCASE_1(8, 15416, 0, 1, 0, Constants.PARLOUR),
	PARLOUR_BOOKCASE_2(8, 15416, 7, 1, 2, Constants.PARLOUR),
	PARLOUR_FIREPLACE(9, 15418, 3, 7, 1, Constants.PARLOUR),
	PARLOUR_CURTAIN_1(10, 15419, 0, 2, 0, Constants.PARLOUR, 5, true),
	PARLOUR_CURTAIN_2(10, 15419, 0, 5, 0, Constants.PARLOUR, 5, true),
	PARLOUR_CURTAIN_3(10, 15419, 2, 7, 1, Constants.PARLOUR, 5, true),
	PARLOUR_CURTAIN_4(10, 15419, 5, 7, 1, Constants.PARLOUR, 5, true),
	PARLOUR_CURTAIN_5(10, 15419, 7, 5, 2, Constants.PARLOUR, 5, true),
	PARLOUR_CURTAIN_6(10, 15419, 7, 2, 2, Constants.PARLOUR, 5, true),
	PARLOUR_CURTAIN_7(10, 15419, 5, 0, 3, Constants.PARLOUR, 5, true),
	PARLOUR_CURTAIN_8(10, 15419, 2, 0, 3, Constants.PARLOUR, 5, true),
	
	/**
	 * Kitchen
	 */
	KITCHEN_CAT_BASKET(11, 15402, 0, 0, 0, Constants.KITCHEN, 22),
	KITCHEN_TABLE(12, 15405, 3, 3, 0, Constants.KITCHEN),
	KITCHEN_BARREL(13, 15401, 0, 6, 3, Constants.KITCHEN),
	KITCHEN_STOVE(14, 15398, 3, 7, 1, Constants.KITCHEN),
	KITCHEN_SINK(15, 15404, 7, 3, 0, Constants.KITCHEN),
	KITCHEN_LARDER(16, 15403, 6, 0, 3, Constants.KITCHEN),
	KITCHEN_SHELF_1(17, 15400, 1, 7, 1, Constants.KITCHEN, 5),
	KITCHEN_SHELF_2(17, 15400, 6, 7, 1, Constants.KITCHEN, 5),
	KITCHEN_SHELF_3(17, 15399, 7, 6, 2, Constants.KITCHEN, 5),
	
	/**
	 * Dining room
	 */
	DINING_CURTAIN_1(10, 15302, 0, 2, 0, Constants.DINING_ROOM, 5, true),
	DINING_CURTAIN_2(10, 15302, 0, 5, 0, Constants.DINING_ROOM, 5, true),
	DINING_CURTAIN_3(10, 15302, 7, 5, 2, Constants.DINING_ROOM, 5, true),
	DINING_CURTAIN_4(10, 15302, 7, 2, 2, Constants.DINING_ROOM, 5, true),
	DINING_CURTAIN_5(10, 15302, 5, 0, 3, Constants.DINING_ROOM, 5, true),
	DINING_CURTAIN_6(10, 15302, 2, 0, 3, Constants.DINING_ROOM, 5, true),
	DINING_BELL_PULL(18, 15304, 0, 0, 2, Constants.DINING_ROOM),
	DINING_WALL_DEC_1(19, 15303, 2, 7, 1, Constants.DINING_ROOM, 5),
	DINING_WALL_DEC_2(19, 15303, 5, 7, 1, Constants.DINING_ROOM, 5),
	DINING_FIREPLACE(9, 15301, 3, 7, 1, Constants.DINING_ROOM),
	DINING_SEATING_1(20, 15300, 2, 2, 2, Constants.DINING_ROOM, true),
	DINING_SEATING_2(20, 15300, 3, 2, 2, Constants.DINING_ROOM, true),
	DINING_SEATING_3(20, 15300, 4, 2, 2, Constants.DINING_ROOM, true),
	DINING_SEATING_4(20, 15300, 5, 2, 2, Constants.DINING_ROOM, true),
	DINING_SEATING_5(20, 15299, 2, 5, 0, Constants.DINING_ROOM, true),
	DINING_SEATING_6(20, 15299, 3, 5, 0, Constants.DINING_ROOM, true),
	DINING_SEATING_7(20, 15299, 4, 5, 0, Constants.DINING_ROOM, true),
	DINING_SEATING_8(20, 15299, 5, 5, 0, Constants.DINING_ROOM, true),
	DINING_TABLE(21, 15298, 2, 3, 0, Constants.DINING_ROOM),
	
	/**
	 * Workshop
	 */
	WORKSHOP_TOOL_1(22, 15443, 1, 0, 3, Constants.WORKSHOP, 5),
	WORKSHOP_TOOL_2(22, 15445, 0, 1, 0, Constants.WORKSHOP, 5),
	WORKSHOP_TOOL_3(22, 15447, 0, 6, 0, Constants.WORKSHOP, 5),
	WORKSHOP_TOOL_4(22, 15446, 7, 1, 2, Constants.WORKSHOP, 5),
	WORKSHOP_TOOL_5(22, 15444, 6, 0, 3, Constants.WORKSHOP, 5),
	WORKSHOP_CLOCKMAKING(23, 15441, 0, 3, 3, Constants.WORKSHOP),
	WORKSHOP_WORKBENCH(24, 15439, 3, 4, 0, Constants.WORKSHOP),
	WORKSHOP_REPAIR_STANCE(25, 15448, 7, 3, 1, Constants.WORKSHOP),
	WORKSHOP_HERALDY(26, 15450, 7, 6, 1, Constants.WORKSHOP),
	
	/**
	 * Bedroom
	 */
	BEDROOM_RUG_1(7, 15266, 2, 2, 0, Constants.BEDROOM, 22, new Dimension(5, 3)),
	BEDROOM_RUG_2(7, 15265, 2, 2, 0, Constants.BEDROOM, 22, new Dimension(5, 3)),
	BEDROOM_RUG_3(7, 15264, 2, 2, 0, Constants.BEDROOM, 22, new Dimension(5, 3)),
	
	BEDROOM_CURTAIN_1(10, 15263, 0, 2, 0, Constants.BEDROOM, 5, true),
	BEDROOM_CURTAIN_2(10, 15263, 0, 5, 0, Constants.BEDROOM, 5, true),
	BEDROOM_CURTAIN_3(10, 15263, 2, 7, 1, Constants.BEDROOM, 5, true),
	BEDROOM_CURTAIN_4(10, 15263, 5, 7, 1, Constants.BEDROOM, 5, true),
	BEDROOM_CURTAIN_5(10, 15263, 7, 5, 2, Constants.BEDROOM, 5, true),
	BEDROOM_CURTAIN_6(10, 15263, 7, 2, 2, Constants.BEDROOM, 5, true),
	BEDROOM_CURTAIN_7(10, 15263, 5, 0, 3, Constants.BEDROOM, 5, true),
	BEDROOM_CURTAIN_8(10, 15263, 2, 0, 3, Constants.BEDROOM, 5, true),
	BEDROOM_FIREPLACE(9, 15267, 7, 3, 2, Constants.BEDROOM),
	BEDROOM_DRESSER(27, 15262, 0, 7, 0, Constants.BEDROOM),
	BEDROOM_BED(28, 15260, 3, 6, 0, Constants.BEDROOM),
	BEDROOM_WARDROBE(29, 15261, 6, 7, 0, Constants.BEDROOM),
	BEDROOM_CLOCK(30, 15268, 7, 0, 1, Constants.BEDROOM, 11),
	
	/**
	 * Skill hall
	 */
	SKILL_HALL_RUG_1(7, 15379, 2, 2, 0, Constants.SKILL_ROOM, 22, new Dimension(5, 5)),
	SKILL_HALL_RUG_2(7, 15378, 2, 2, 0, Constants.SKILL_ROOM, 22, new Dimension(5, 5)),
	SKILL_HALL_RUG_3(7, 15377, 2, 2, 0, Constants.SKILL_ROOM, 22, new Dimension(5, 5)),
	SKILL_HALL_RUNE_CASE(31, 15386, 0, 6, 1, Constants.SKILL_ROOM),
	SKILL_HALL_FISHING_TOPHY(32, 15383, 1, 7, 0, Constants.SKILL_ROOM),
	SKILL_HALL_HEAD_TOPHY(33, 15382, 6, 7, 0, Constants.SKILL_ROOM),
	SKILL_HALL_ARMOUR_1(34, 15384, 5, 3, 0, Constants.SKILL_ROOM),
	SKILL_HALL_ARMOUR_2(35, 34255, 2, 3, 0, Constants.SKILL_ROOM),
	SKILL_HALL_STAIRS(36, 15380, 3, 3, 0, Constants.SKILL_ROOM),
	SKILL_HALL_STAIRS_1(37, 15381, 3, 3, 0, Constants.SKILL_ROOM),
	
	SKILL_HALL_RUG_1_DOWN(7, 15379, 2, 2, 0, Constants.SKILL_HALL_DOWN, 22, new Dimension(5, 5)),
	SKILL_HALL_RUG_2_DOWN(7, 15378, 2, 2, 0, Constants.SKILL_HALL_DOWN, 22, new Dimension(5, 5)),
	SKILL_HALL_RUG_3_DOWN(7, 15377, 2, 2, 0, Constants.SKILL_HALL_DOWN, 22, new Dimension(5, 5)),
	SKILL_HALL_RUNE_CASE_DOWN(31, 15386, 0, 6, 1, Constants.SKILL_HALL_DOWN),
	SKILL_HALL_FISHING_TOPHY_DOWN(32, 15383, 1, 7, 0, Constants.SKILL_HALL_DOWN),
	SKILL_HALL_HEAD_TOPHY_DOWN(33, 15382, 6, 7, 0, Constants.SKILL_HALL_DOWN),
	SKILL_HALL_ARMOUR_1_DOWN(34, 15384, 5, 3, 0, Constants.SKILL_HALL_DOWN),
	SKILL_HALL_ARMOUR_2_DOWN(35, 34255, 2, 3, 0, Constants.SKILL_HALL_DOWN),
	SKILL_HALL_STAIRS_DOWN(36, 15380, 3, 3, 0, Constants.SKILL_HALL_DOWN),
	SKILL_HALL_STAIRS_1_DOWN(37, 15381, 3, 3, 0, Constants.SKILL_HALL_DOWN),
	
	/**
	 * Games room
	 */
	RANGING_GAME(38, 15346, 1, 0, 2, Constants.GAMES_ROOM),
	STONE_SPACE(39, 15344, 2, 4, 0, Constants.GAMES_ROOM),
	ELEMENTAL_BALANCE(40, 15345, 5, 4, 0, Constants.GAMES_ROOM),
	PRIZE_CHEST(41, 15343, 3, 7, 0, Constants.GAMES_ROOM),
	GAME_SPACE(42, 15342, 6, 0, 1, Constants.GAMES_ROOM),
	
	/**
	 * Combat room
	 */
	STORAGE_RACK(43, 15296, 3, 7, 0, Constants.COMBAT_ROOM),
	COMBAT_WALL_DEC_1(19, 15297, 1, 7, 1, Constants.COMBAT_ROOM, 5),
	COMBAT_WALL_DEC_2(19, 15297, 6, 7, 1, Constants.COMBAT_ROOM, 5),
	
	COMBAT_RING_1(44, 15294, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_3(44, 15293, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_4(44, 15292, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_5(44, 15291, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_6(44, 15290, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_7(44, 15289, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_8(44, 15288, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_9(44, 15287, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_10(44, 15286, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_11(44, 15282, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_12(44, 15281, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_13(44, 15280, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_14(44, 15279, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_15(44, 15278, 1, 1, 0, Constants.COMBAT_ROOM),
	COMBAT_RING_16(44, 15277, 1, 1, 0, Constants.COMBAT_ROOM),
	/**
	 * Quest hall
	 */
	QUEST_HALL_BOOKCASE(8, 15397, 0, 1, 0, Constants.QUEST_ROOM),
	QUEST_HALL_MAP(45, 15396, 7, 1, 2, Constants.QUEST_ROOM, 5),
	QUEST_HALL_SWORD(46, 15395, 7, 6, 2, Constants.QUEST_ROOM, 5),
	QUEST_HALL_LANDSCAPE(47, 15393, 6, 7, 1, Constants.QUEST_ROOM, 5),
	QUEST_HALL_PORTRAIT(48, 15392, 1, 7, 1, Constants.QUEST_ROOM, 5),
	QUEST_HALL_GUILD_TROPHY(49, 15394, 0, 6, 0, Constants.QUEST_ROOM, 5),
	QUEST_HALL_STAIRS(36, 15390, 3, 3, 0, Constants.QUEST_ROOM),
	QUEST_HALL_RUG_1(7, 15389, 2, 2, 0, Constants.QUEST_ROOM, 22, new Dimension(5, 5)),
	QUEST_HALL_RUG_2(7, 15388, 2, 2, 0, Constants.QUEST_ROOM, 22, new Dimension(5, 5)),
	QUEST_HALL_RUG_3(7, 15387, 2, 2, 0, Constants.QUEST_ROOM, 22, new Dimension(5, 5)),
	
	QUEST_HALL_BOOKCASE_DOWN(8, 15397, 0, 1, 0, Constants.QUEST_HALL_DOWN),
	QUEST_HALL_MAP_DOWN(45, 15396, 7, 1, 2, Constants.QUEST_HALL_DOWN, 5),
	QUEST_HALL_SWORD_DOWN(46, 15395, 7, 6, 2, Constants.QUEST_HALL_DOWN, 5),
	QUEST_HALL_LANDSCAPE_DOWN(47, 15393, 6, 7, 1, Constants.QUEST_HALL_DOWN, 5),
	QUEST_HALL_PORTRAIT_DOWN(48, 15392, 1, 7, 1, Constants.QUEST_HALL_DOWN, 5),
	QUEST_HALL_GUILD_TROPHY_DOWN(49, 15394, 0, 6, 0, Constants.QUEST_HALL_DOWN, 5),
	QUEST_HALL_STAIRS_DOWN(36, 15390, 3, 3, 0, Constants.QUEST_HALL_DOWN),
	QUEST_HALL_RUG_1_DOWN(7, 15389, 2, 2, 0, Constants.QUEST_HALL_DOWN, 22, new Dimension(5, 5)),
	QUEST_HALL_RUG_2_DOWN(7, 15388, 2, 2, 0, Constants.QUEST_HALL_DOWN, 22, new Dimension(5, 5)),
	QUEST_HALL_RUG_3_DOWN(7, 15387, 2, 2, 0, Constants.QUEST_HALL_DOWN, 22, new Dimension(5, 5)),
	
	/**
	 * Menagerie
	 */
	MENAGERIE_PET_HOUSE(50, 44909, 1, 1, 2, Constants.MENAGERY),
	MENAGERIE_PET_FEEDER(51, 44910, 5, 1, 3, Constants.MENAGERY),
	MENAGERIE_OBELISK(52, 44911, 5, 5, 3, Constants.MENAGERY),
	
	HABITAT_SPACE(53, 44908, 0, 0, 0, Constants.MENAGERY),
	
	/**
	 * Study
	 */
	STUDY_WALL_CHART_1(54, 15423, 0, 1, 0, Constants.STUDY, 5, true),
	STUDY_WALL_CHART_2(54, 15423, 1, 7, 1, Constants.STUDY, 5, true),
	STUDY_WALL_CHART_3(54, 15423, 6, 7, 1, Constants.STUDY, 5, true),
	STUDY_WALL_CHART_4(54, 15423, 7, 1, 2, Constants.STUDY, 5, true),
	STUDY_LECTERN(55, 15420, 2, 2, 2, Constants.STUDY),
	STUDY_STATUE(56, 48662, 6, 1, 2, Constants.STUDY),
	STUDY_CRYSTAL_BALL(57, 15422, 5, 4, 2, Constants.STUDY),
	STUDY_GLOBE(58, 15421, 1, 4, 2, Constants.STUDY),
	STUDY_BOOKCASE_1(8, 15425, 3, 7, 1, Constants.STUDY),
	STUDY_BOOKCASE_2(8, 15425, 4, 7, 1, Constants.STUDY),
	STUDY_TELESCOPE(59, 15424, 5, 7, 2, Constants.STUDY),
	
	/**
	 * Costume room
	 */
	COSTUME_TREASURE_CHEST(60, 18813, 0, 3, 3, Constants.COSTUME_ROOM),
	COSTUME_FANCY(61, 18814, 3, 3, 0, Constants.COSTUME_ROOM),
	TOY_BOX(62, 18812, 7, 3, 1, Constants.COSTUME_ROOM),
	ARMOUR_CASE(63, 18815, 2, 7, 1, Constants.COSTUME_ROOM),
	MAGIC_WARDROBE(64, 18811, 3, 7, 1, Constants.COSTUME_ROOM),
	CAPE_RACK(65, 18810, 6, 6, 1, Constants.COSTUME_ROOM),
	/**
	 * Chapel
	 */
	CHAPEL_STATUE_1(66, 15275, 0, 0, 2, Constants.CHAPEL, 11, true),
	CHAPEL_STATUE_2(66, 15275, 7, 0, 1, Constants.CHAPEL, 11, true),
	CHAPEL_LAMP_1(67, 15271, 1, 5, 2, Constants.CHAPEL, true),
	CHAPEL_LAMP_2(67, 15271, 6, 5, 2, Constants.CHAPEL, true),
	CHAPEL_MUSICAL(68, 15276, 7, 3, 1, Constants.CHAPEL),
	CHAPEL_ALTAR(69, 15270, 3, 5, 0, Constants.CHAPEL),
	CHAPEL_ICON(70, 15269, 3, 7, 0, Constants.CHAPEL),
	CHAPEL_WINDOW_0(71, 13733, 0, 2, 0, Constants.CHAPEL, 0, true),
	CHAPEL_WINDOW_1(71, 13733, 0, 5, 0, Constants.CHAPEL, 0, true),
	CHAPEL_WINDOW_2(71, 13733, 2, 7, 1, Constants.CHAPEL, 0, true),
	CHAPEL_WINDOW_3(71, 13733, 5, 7, 1, Constants.CHAPEL, 0, true),
	CHAPEL_WINDOW_4(71, 13733, 7, 5, 2, Constants.CHAPEL, 0, true),
	CHAPEL_WINDOW_5(71, 13733, 7, 2, 2, Constants.CHAPEL, 0, true),
	
	CHAPEL_RUG_1(7, 15270, 4, 1, 0, Constants.CHAPEL, 22, new Dimension(1, 4)),
	CHAPEL_RUG_2(7, 15274, 4, 1, 0, Constants.CHAPEL, 22, new Dimension(1, 4)),
	CHAPEL_RUG_3(7, 15273, 4, 1, 0, Constants.CHAPEL, 22, new Dimension(1, 4)),
	
	/**
	 * Portal room
	 */
	
	PORTAL_1(72, 15406, 0, 3, 1, Constants.PORTAL_ROOM),
	PORTAL_2(72, 15407, 3, 7, 2, Constants.PORTAL_ROOM),
	PORTAL_3(72, 15408, 7, 3, 3, Constants.PORTAL_ROOM),
	PORTAL_CENTREPIECE(73, 15409, 3, 3, 0, Constants.PORTAL_ROOM),
	/**
	 * Formal garden
	 */
	FORMAL_CENTREPIECE(74, 15368, 3, 3, 0, Constants.FORMAL_GARDEN),
	FORMAL_SMALL_PLANT_1_0(75, 15375, 5, 1, 2, Constants.FORMAL_GARDEN, true),
	FORMAL_SMALL_PLANT_1_1(75, 15375, 6, 2, 2, Constants.FORMAL_GARDEN, true),
	FORMAL_SMALL_PLANT_1_2(75, 15375, 1, 5, 0, Constants.FORMAL_GARDEN, true),
	FORMAL_SMALL_PLANT_1_3(75, 15375, 2, 6, 0, Constants.FORMAL_GARDEN, true),
	FORMAL_SMALL_PLANT_2_0(76, 15376, 1, 2, 2, Constants.FORMAL_GARDEN, true),
	FORMAL_SMALL_PLANT_2_1(76, 15376, 2, 1, 2, Constants.FORMAL_GARDEN, true),
	FORMAL_SMALL_PLANT_2_2(76, 15376, 5, 6, 0, Constants.FORMAL_GARDEN, true),
	FORMAL_SMALL_PLANT_2_3(76, 15376, 6, 5, 0, Constants.FORMAL_GARDEN, true),
	FORMAL_BIG_PLANT_1_0(75, 15373, 6, 1, 2, Constants.FORMAL_GARDEN, true),
	FORMAL_BIG_PLANT_1_1(75, 15373, 1, 6, 0, Constants.FORMAL_GARDEN, true),
	FORMAL_BIG_PLANT_2_0(76, 15374, 1, 1, 2, Constants.FORMAL_GARDEN, true),
	FORMAL_BIG_PLANT_2_1(76, 15374, 6, 6, 0, Constants.FORMAL_GARDEN, true),
	FORMAL_HEDGE_1(77, 15370, 0, 0, 0, Constants.FORMAL_GARDEN),
	FORMAL_HEDGE_2(77, 15371, 0, 0, 0, Constants.FORMAL_GARDEN),
	FORMAL_HEDGE_3(77, 15372, 0, 0, 0, Constants.FORMAL_GARDEN),
	FORMAL_FENCE(78, 15369, 0, 0, 0, Constants.FORMAL_GARDEN),
	/**
	 * Throne room
	 */
	THRONE_ROOM_THRONE_1(79, 15426, 3, 6, 0, Constants.THRONE_ROOM, true),
	THRONE_ROOM_THRONE_2(79, 15426, 4, 6, 0, Constants.THRONE_ROOM, true),
	THRONE_ROOM_LEVER(80, 15435, 6, 6, 0, Constants.THRONE_ROOM),
	THRONE_ROOM_TRAPDOOR(81, 15438, 1, 6, 0, Constants.THRONE_ROOM, 22),
	THRONE_DECORATION_1(83, 15434, 3, 7, 1, Constants.THRONE_ROOM, 4, true),
	THRONE_DECORATION_2(83, 15434, 4, 7, 1, Constants.THRONE_ROOM, 4, true),
	THRONE_TRAP_1(84, 15431, 3, 3, 0, Constants.THRONE_ROOM, 22, true),
	THRONE_TRAP_2(84, 15431, 4, 3, 0, Constants.THRONE_ROOM, 22, true),
	THRONE_TRAP_3(84, 15431, 3, 4, 0, Constants.THRONE_ROOM, 22, true),
	THRONE_TRAP_4(84, 15431, 4, 4, 0, Constants.THRONE_ROOM, 22, true),
	THRONE_BENCH_1(20, 15436, 0, 0, 3, Constants.THRONE_ROOM, true),
	THRONE_BENCH_2(20, 15436, 0, 1, 3, Constants.THRONE_ROOM, true),
	THRONE_BENCH_3(20, 15436, 0, 2, 3, Constants.THRONE_ROOM, true),
	THRONE_BENCH_4(20, 15436, 0, 3, 3, Constants.THRONE_ROOM, true),
	THRONE_BENCH_5(20, 15436, 0, 4, 3, Constants.THRONE_ROOM, true),
	THRONE_BENCH_6(20, 15436, 0, 5, 3, Constants.THRONE_ROOM, true),
	THRONE_BENCH_7(20, 15437, 7, 0, 1, Constants.THRONE_ROOM, true),
	THRONE_BENCH_8(20, 15437, 7, 1, 1, Constants.THRONE_ROOM, true),
	THRONE_BENCH_9(20, 15437, 7, 2, 1, Constants.THRONE_ROOM, true),
	THRONE_BENCH_10(20, 15437, 7, 3, 1, Constants.THRONE_ROOM, true),
	THRONE_BENCH_11(20, 15437, 7, 4, 1, Constants.THRONE_ROOM, true),
	THRONE_BENCH_12(20, 15437, 7, 5, 1, Constants.THRONE_ROOM, true),
	/**
	 * Oubliette
	 */
	OUBLIETTE_FLOOR_1(85, 15350, 2, 2, 22, Constants.OUBLIETTE),
	OUBLIETTE_FLOOR_2(85, 15348, 2, 2, 22, Constants.OUBLIETTE),
	OUBLIETTE_FLOOR_3(85, 15347, 2, 2, 22, Constants.OUBLIETTE),
	OUBLIETTE_FLOOR_4(85, 15351, 2, 2, 22, Constants.OUBLIETTE),
	OUBLIETTE_FLOOR_5(85, 15349, 2, 2, 22, Constants.OUBLIETTE),
	OUBLIETTE_CAGE_1(86, 15353, 2, 2, 0, Constants.OUBLIETTE, 0),
	OUBLIETTE_CAGE_2(86, 15352, 2, 2, 0, Constants.OUBLIETTE, 0),
	OUBLIETTE_GUARD(87, 15354, 0, 0, 3, Constants.OUBLIETTE),
	OUBLIETTE_LADDER(88, 15356, 1, 6, 0, Constants.OUBLIETTE),
	OUBLIETTE_DECORATION_1(89, 15331, 0, 2, 0, Constants.OUBLIETTE, 4, true),
	OUBLIETTE_DECORATION_2(89, 15331, 2, 7, 1, Constants.OUBLIETTE, 4, true),
	OUBLIETTE_DECORATION_3(89, 15331, 7, 5, 2, Constants.OUBLIETTE, 4, true),
	OUBLIETTE_DECORATION_4(89, 15331, 5, 0, 3, Constants.OUBLIETTE, 4, true),
	OUBLIETTE_LIGHT_1(90, 15355, 2, 0, 3, Constants.OUBLIETTE, 4, true),
	OUBLIETTE_LIGHT_2(90, 15355, 0, 5, 0, Constants.OUBLIETTE, 4, true),
	OUBLIETTE_LIGHT_3(90, 15355, 5, 7, 1, Constants.OUBLIETTE, 4, true),
	OUBLIETTE_LIGHT_4(90, 15355, 7, 2, 2, Constants.OUBLIETTE, 4, true),
	/**
	 * Corridor
	 */
	CORRIDOR_LIGHT_1(90, 15330, 3, 1, 0, Constants.CORRIDOR, 4, true),
	CORRIDOR_LIGHT_2(90, 15330, 3, 6, 0, Constants.CORRIDOR, 4, true),
	CORRIDOR_LIGHT_3(90, 15330, 4, 1, 2, Constants.CORRIDOR, 4, true),
	CORRIDOR_LIGHT_4(90, 15330, 4, 6, 2, Constants.CORRIDOR, 4, true),
	CORRIDOR_DECORATION_1(89, 15331, 3, 4, 0, Constants.CORRIDOR, 4, true),
	CORRIDOR_DECORATION_2(89, 15331, 4, 3, 2, Constants.CORRIDOR, 4, true),
	CORRIDOR_GUARD(87, 15323, 3, 3, 2, Constants.CORRIDOR),
	CORRIDOR_TRAP_1(91, 15325, 3, 2, 0, Constants.CORRIDOR, 22, true),
	CORRIDOR_TRAP_2(91, 15325, 4, 2, 0, Constants.CORRIDOR, 22, true),
	CORRIDOR_TRAP_3(91, 15324, 3, 5, 0, Constants.CORRIDOR, 22, true),
	CORRIDOR_TRAP_4(91, 15324, 4, 5, 0, Constants.CORRIDOR, 22, true),
	CORRIDOR_DOOR_1(92, 15329, 3, 1, 3, Constants.CORRIDOR, 0),
	CORRIDOR_DOOR_2(92, 15328, 4, 1, 3, Constants.CORRIDOR, 0),
	CORRIDOR_DOOR_3(92, 15326, 3, 6, 1, Constants.CORRIDOR, 0),
	CORRIDOR_DOOR_4(92, 15327, 4, 6, 1, Constants.CORRIDOR, 0),
	
	/**
	 * junction
	 */
	JUNCTION_TRAP_1(91, 15325, 3, 2, 0, Constants.JUNCTION, 22, true),
	JUNCTION_TRAP_2(91, 15325, 4, 2, 0, Constants.JUNCTION, 22, true),
	JUNCTION_TRAP_3(91, 15324, 3, 5, 0, Constants.JUNCTION, 22, true),
	JUNCTION_TRAP_4(91, 15324, 4, 5, 0, Constants.JUNCTION, 22, true),
	JUNCTION_LIGHT_1(90, 15330, 3, 1, 0, Constants.JUNCTION, 4, true),
	JUNCTION_LIGHT_2(90, 15330, 1, 4, 1, Constants.JUNCTION, 4, true),
	JUNCTION_LIGHT_3(90, 15330, 4, 6, 2, Constants.JUNCTION, 4, true),
	JUNCTION_LIGHT_4(90, 15330, 6, 3, 3, Constants.JUNCTION, 4, true),
	JUNCTION_DECORATION_1(89, 15331, 1, 3, 3, Constants.JUNCTION, 4, true),
	JUNCTION_DECORATION_2(89, 15331, 6, 4, 1, Constants.JUNCTION, 4, true),
	JUNCTION_GUARD(87, 15323, 3, 3, 2, Constants.JUNCTION),
	/**
	 * Dungeon stair room
	 */
	DUNG_STAIR_LIGHT_1(90, 34138, 2, 1, 3, Constants.DUNGEON_STAIR_ROOM, 4, true),
	DUNG_STAIR_LIGHT_2(90, 15330, 1, 2, 0, Constants.DUNGEON_STAIR_ROOM, 4, true),
	DUNG_STAIR_LIGHT_3(90, 15330, 2, 6, 1, Constants.DUNGEON_STAIR_ROOM, 4, true),
	DUNG_STAIR_LIGHT_4(90, 34138, 1, 5, 0, Constants.DUNGEON_STAIR_ROOM, 4, true),
	DUNG_STAIR_LIGHT_5(90, 34138, 5, 6, 1, Constants.DUNGEON_STAIR_ROOM, 4, true),
	DUNG_STAIR_LIGHT_6(90, 15330, 6, 5, 2, Constants.DUNGEON_STAIR_ROOM, 4, true),
	DUNG_STAIR_LIGHT_7(90, 34138, 6, 2, 2, Constants.DUNGEON_STAIR_ROOM, 4, true),
	DUNG_STAIR_LIGHT_8(90, 15330, 5, 1, 3, Constants.DUNGEON_STAIR_ROOM, 4, true),
	DUNG_STAIR_DECORATION_1(89, 15331, 1, 6, 1, Constants.DUNGEON_STAIR_ROOM, 4, true),
	DUNG_STAIR_DECORATION_2(89, 15331, 6, 1, 3, Constants.DUNGEON_STAIR_ROOM, 4, true),
	DUNG_STAIR_GUARD_1(87, 15337, 1, 1, 2, Constants.DUNGEON_STAIR_ROOM),
	DUNG_STAIR_GUARD_2(87, 15336, 5, 5, 0, Constants.DUNGEON_STAIR_ROOM),
	DUNG_STAIRS(36, 15380, 3, 3, 0, Constants.DUNGEON_STAIR_ROOM),
	/**
	 * Pit
	 */
	PIT_TRAP_1(93, 39230, 3, 1, 0, Constants.PIT),
	PIT_TRAP_2(93, 39231, 5, 3, 3, Constants.PIT),
	PIT_TRAP_3(93, 36692, 3, 5, 2, Constants.PIT),
	PIT_TRAP_4(93, 39229, 1, 3, 1, Constants.PIT),
	PIT_GUARDIAN(94, 36676, 3, 3, 0, Constants.PIT),
	PIT_LIGHT_1(90, 34138, 2, 1, 3, Constants.PIT, 4, true),
	PIT_LIGHT_2(90, 15330, 1, 2, 0, Constants.PIT, 4, true),
	PIT_LIGHT_3(90, 15330, 2, 6, 1, Constants.PIT, 4, true),
	PIT_LIGHT_4(90, 34138, 1, 5, 0, Constants.PIT, 4, true),
	PIT_LIGHT_5(90, 34138, 5, 6, 1, Constants.PIT, 4, true),
	PIT_LIGHT_6(90, 15330, 6, 5, 2, Constants.PIT, 4, true),
	PIT_LIGHT_7(90, 34138, 6, 2, 2, Constants.PIT, 4, true),
	PIT_LIGHT_8(90, 15330, 5, 1, 3, Constants.PIT, 4, true),
	PIT_DECORATION_1(89, 15331, 1, 6, 1, Constants.PIT, 4, true),
	PIT_DECORATION_2(89, 15331, 6, 1, 3, Constants.PIT, 4, true),
	
	PIT_DOOR_1(92, 36675, 3, 1, 3, Constants.PIT, 0),
	PIT_DOOR_2(92, 36672, 4, 1, 3, Constants.PIT, 0),
	PIT_DOOR_3(92, 36672, 3, 6, 1, Constants.PIT, 0),
	PIT_DOOR_4(92, 36675, 4, 6, 1, Constants.PIT, 0),
	PIT_DOOR_5(92, 36672, 1, 3, 0, Constants.PIT, 0),
	PIT_DOOR_6(92, 36675, 1, 4, 0, Constants.PIT, 0),
	PIT_DOOR_7(92, 36675, 6, 3, 0, Constants.PIT, 2),
	PIT_DOOR_8(92, 36672, 6, 4, 0, Constants.PIT, 2),
	/**
	 * Treasure room
	 */
	TREASURE_ROOM_DECORATION_1(89, 15331, 1, 2, 0, Constants.TREASURE_ROOM, 4, true),
	TREASURE_ROOM_DECORATION_2(89, 15331, 6, 2, 2, Constants.TREASURE_ROOM, 4, true),
	TREASURE_ROOM_LIGHT_1(90, 15330, 1, 5, 0, Constants.TREASURE_ROOM, 4, true),
	TREASURE_ROOM_LIGHT_2(90, 15330, 6, 5, 2, Constants.TREASURE_ROOM, 4, true),
	TREASURE_MONSTER(95, 15257, 3, 3, 0, Constants.TREASURE_ROOM),
	TREASURE_CHEST(96, 15256, 2, 6, 0, Constants.TREASURE_ROOM),
	TREASURE_DECORATION_1(83, 15259, 3, 6, 1, Constants.TREASURE_ROOM, 4, true),
	TREASURE_DECORATION_2(83, 15259, 4, 6, 1, Constants.TREASURE_ROOM, 4, true),
	TREASURE_ROOM_DOOR_1(92, 15327, 3, 1, 3, Constants.TREASURE_ROOM, 0),
	TREASURE_ROOM_DOOR_2(92, 15326, 4, 1, 3, Constants.TREASURE_ROOM, 0);
	
	public static final ImmutableSet<HotSpots> VALUES = Sets.immutableEnumSet(EnumSet.allOf(HotSpots.class));
	
	private int hotSpotId, objectId, xOffset, yOffset, standardRotation, objectType, roomType;
	private boolean mutiple;
	private Dimension carpetDim;
	
	HotSpots(int hotSpotId, int objectId, int xOffset, int yOffset, int standardRotation, int roomType) {
		this.hotSpotId = hotSpotId;
		this.objectId = objectId;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.standardRotation = standardRotation;
		setCarpetDim(null);
		setObjectType(10);
		setMutiple(false);
		setRoomType(roomType);
		
	}
	
	HotSpots(int hotSpotId, int objectId, int xOffset, int yOffset, int standardRotation, int roomType, int objectType) {
		this(hotSpotId, objectId, xOffset, yOffset, standardRotation, roomType);
		this.setObjectType(objectType);
	}
	
	HotSpots(int hotSpotId, int objectId, int xOffset, int yOffset, int standardRotation, int roomType, int objectType, boolean mutiple) {
		this(hotSpotId, objectId, xOffset, yOffset, standardRotation, roomType, objectType);
		this.setMutiple(mutiple);
	}
	
	HotSpots(int hotSpotId, int objectId, int xOffset, int yOffset, int standardRotation, int roomType, boolean mutiple) {
		this(hotSpotId, objectId, xOffset, yOffset, standardRotation, roomType);
		this.setMutiple(mutiple);
	}
	
	HotSpots(int hotSpotId, int objectId, int xOffset, int yOffset, int standardRotation, int roomType, int objectType, Dimension carpetDim) {
		this(hotSpotId, objectId, xOffset, yOffset, standardRotation, roomType, objectType);
		this.setCarpetDim(carpetDim);
	}
	
	public static HotSpots forHotSpotId(int hotSpotId) {
		for(HotSpots hs : VALUES) {
			if(hs.hotSpotId == hotSpotId)
				return hs;
		}
		return null;
	}
	
	public static HotSpots forHotSpotIdAndCoords(int hotSpotId, int xOffset, int yOffset, Room room) {
		for(HotSpots hs : VALUES) {
			if(hs.hotSpotId == hotSpotId && hs.xOffset == xOffset && hs.yOffset == yOffset && (hs.isMutiple() ? hs.getRoomType() == room.data().getId() : xOffset == xOffset))
				return hs;
		}
		return null;
	}
	
	public static HotSpots forObjectId(int objectId) {
		for(HotSpots hs : VALUES) {
			if(hs.objectId == objectId)
				return hs;
		}
		return null;
	}
	
	public static ObjectArrayList<HotSpots> forObjectId_2(int objectId) {
		ObjectArrayList<HotSpots> hs_1 = new ObjectArrayList<>();
		for(HotSpots hs : VALUES) {
			if(hs.objectId == objectId)
				hs_1.add(hs);
		}
		return hs_1;
	}
	
	public static ObjectArrayList<HotSpots> forObjectId_3(int hotSpotId) {
		ObjectArrayList<HotSpots> hs_1 = new ObjectArrayList<>();
		for(HotSpots hs : VALUES) {
			if(hs.hotSpotId == hotSpotId)
				hs_1.add(hs);
		}
		return hs_1;
	}
	
	public static HotSpots forObjectIdAndCoords(int objectId, int x, int y) {
		for(HotSpots hs : VALUES) {
			if(hs.objectId == objectId && hs.xOffset == x && hs.yOffset == y) {
				return hs;
			}
		}
		return null;
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
	
	public static void event() {
		for(HotSpots spot : VALUES) {
			ObjectEvent e = new ObjectEvent() {
				@Override
				public boolean click(Player player, ObjectNode object, int click) {
					player.getMessages().sendConstruction(spot);
					return true;
				}
			};
			e.registerCons(spot.getObjectId());
		}
		
		/*
		 * Creating rooms.
		 */
		ObjectEvent roomCreation = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(RoomManipulation.roomExists(player)) {
					player.getDialogueBuilder().append(
							new OptionDialogue(t -> {
								if(t.equals(FIRST_OPTION)) {
									RoomManipulation.rotateRoom(0, player);
								} else if(t.equals(SECOND_OPTION)) {
									RoomManipulation.rotateRoom(1, player);
								} else if(t.equals(THIRD_OPTION)) {
									RoomManipulation.deleteRoom(player, player.getPosition().getZ());
								}
								player.getMessages().sendCloseWindows();
							}, "Rotate clockwise", "Rotate counter-clockwise", "Remove")
					);
				} else
					player.getMessages().sendInterface(-14);
				return true;
			}
		};
		roomCreation.registerCons(15314);
		roomCreation.registerCons(15313);
		roomCreation.registerCons(15306);
		roomCreation.registerCons(15305);
	}
}