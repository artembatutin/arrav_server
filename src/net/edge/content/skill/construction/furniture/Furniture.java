package net.edge.content.skill.construction.furniture;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.content.skill.construction.Construction;
import net.edge.content.skill.construction.data.Constants;
import net.edge.world.entity.item.Item;

import java.util.EnumSet;

/**
 * Enumeration of all {@link Construction} furniture.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum Furniture {
	EMPTY(-1, 6951, 0, 0, 0, null),
	EXIT_PORTAL(0, 13405, 100, 1, 8168, new Item[]{new Item(Constants.IRON_BAR, 10)}),
	DECORATIVE_ROCK(0, 13406, 100, 5, 8169, new Item[]{new Item(Constants.LIMESTONE_BRICK, 5)}),
	POND(0, 13407, 100, 10, 8170, new Item[]{new Item(Constants.SOFT_CLAY, 10)}),
	IMP_STATUE(0, 13408, 100, 15, 8171, new Item[]{new Item(Constants.LIMESTONE_BRICK, 5), new Item(Constants.SOFT_CLAY, 5)}),
	DUNGEON_ENTRANCE(0, 13409, 500, 70, 8172, new Item[]{new Item(Constants.MARBLE_BLOCK, 1)}),
	DEAD_TREE(1, 13411, 31, 5, 8173, new Item[]{new Item(8417, 1)}),
	NICE_TREE(1, 13412, 44, 10, 8174, new Item[]{new Item(8419, 1)}),
	OAK_TREE(1, 13413, 70, 15, 8175, new Item[]{new Item(8421, 1)}),
	WILLOW_TREE(1, 13414, 100, 30, 8176, new Item[]{new Item(8423, 1)}),
	MARPLE_TREE(1, 13415, 122, 45, 8177, new Item[]{new Item(8425, 1)}),
	YEW_TREE(1, 13416, 141, 60, 8178, new Item[]{new Item(8427, 1)}),
	MAGIC_TREE(1, 13417, 223, 75, 8179, new Item[]{new Item(8429, 1)}),
	PLANT(2, 13431, 31, 1, 8180, new Item[]{new Item(8431, 1)}),
	SMALL_FERN(2, 13432, 70, 6, 8181, new Item[]{new Item(8433, 1)}),
	FERN(2, 13433, 100, 12, 8182, new Item[]{new Item(8435, 1)}),
	DOCK_LEAF(3, 13434, 31, 1, 8183, new Item[]{new Item(8431, 1)}),
	THISTLE(3, 13435, 70, 6, 8184, new Item[]{new Item(8433, 1)}),
	REEDS(3, 13436, 100, 12, 8185, new Item[]{new Item(8435, 1)}),
	BIG_FERN(4, 13425, 31, 1, 8186, new Item[]{new Item(8431, 1)}),
	BUSH(4, 13426, 70, 6, 8187, new Item[]{new Item(8433, 1)}),
	TALL_PLANT(4, 13427, 100, 12, 8188, new Item[]{new Item(8435, 1)}),
	SHORT_PLANT(5, 13428, 31, 1, 8189, new Item[]{new Item(8431, 1)}),
	LARGE_LEAF_BUSH(5, 13429, 70, 6, 8190, new Item[]{new Item(8433, 1)}),
	HUGE_PLANT(5, 13430, 100, 12, 8191, new Item[]{new Item(8435, 1)}),
	/**
	 * Parlour
	 */
	CRUDE_WOODEN_CHAIR(6, 13581, 66, 1, 8309, new Item[]{new Item(Constants.PLANK, 2), new Item(Constants.NAILS, 2)}),
	WOODEN_CHAIR(6, 13582, 96, 8, 8310, new Item[]{new Item(Constants.PLANK, 3), new Item(Constants.NAILS, 3)}),
	ROCKING_CHAIR(6, 13583, 96, 14, 8311, new Item[]{new Item(Constants.PLANK, 3), new Item(Constants.NAILS, 3)}),
	OAK_CHAIR(6, 13584, 120, 19, 8312, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	OAK_ARMCHAIR(6, 13585, 180, 26, 8313, new Item[]{new Item(Constants.OAK_PLANK, 3)}),
	TEAK_ARMCHAIR(6, 13586, 180, 35, 8314, new Item[]{new Item(Constants.TEAK_PLANK, 3)}),
	MAHOGANY_ARMCHAIR(6, 13587, 20, 50, 8315, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3)}),
	BROWN_RUG(7, 13588, 30, 2, 8316, new Item[]{new Item(Constants.CLOTH, 2)}),
	RUG(7, 13591, 60, 13, 8317, new Item[]{new Item(Constants.CLOTH, 4)}),
	OPULENT_RUG(7, 13594, 360, 65, 8318, new Item[]{new Item(Constants.CLOTH, 4), new Item(Constants.GOLD_LEAF, 1)}),
	WOODEN_BOOKCASE(8, 13597, 115, 4, 8319, new Item[]{new Item(Constants.PLANK, 4), new Item(Constants.NAILS, 4)}),
	OAK_BOOKCASE(8, 13598, 180, 29, 8320, new Item[]{new Item(Constants.OAK_PLANK, 3)}),
	MAHOGANY_BOOKCASE(8, 13599, 420, 40, 8321, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3)}),
	CLAY_FIREPLACE(9, 13609, 30, 3, 8325, new Item[]{new Item(Constants.SOFT_CLAY, 3)}),
	STONE_FIREPLACE(9, 13611, 40, 33, 8326, new Item[]{new Item(Constants.LIMESTONE_BRICK, 2)}),
	MARBLE_FIREPLACE(9, 13613, 500, 63, 8327, new Item[]{new Item(Constants.MARBLE_BLOCK, 1)}),
	TORN_CURTAINS(10, 13603, 132, 2, 8322, new Item[]{new Item(Constants.PLANK, 3), new Item(Constants.CLOTH, 3), new Item(Constants.NAILS, 3)}),
	CURTAINS(10, 13604, 225, 18, 8323, new Item[]{new Item(Constants.OAK_PLANK, 3), new Item(Constants.CLOTH, 3)}),
	OPULENT_CURTAINS(10, 13605, 315, 40, 8324, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(Constants.CLOTH, 3)}),
	/**
	 * Kitchen
	 */
	CAT_BLANKET(11, 13574, 15, 5, 8236, new Item[]{new Item(Constants.CLOTH, 1)}),
	CAT_BASKET(11, 13575, 58, 19, 8237, new Item[]{new Item(Constants.PLANK, 2), new Item(Constants.NAILS, 2)}),
	CUSHIONED_CAT_BASKET(11, 13576, 58, 33, 8238, new Item[]{new Item(Constants.PLANK, 2), new Item(Constants.NAILS, 2), new Item(1737, 2)}),
	WOOD_KITCHEN_TABLE(12, 13577, 87, 12, 8246, new Item[]{new Item(Constants.PLANK, 3), new Item(Constants.NAILS, 3)}),
	OAK_KITCHEN_TABLE(12, 13578, 180, 32, 8247, new Item[]{new Item(Constants.OAK_PLANK, 3)}),
	TEAK_KITCHEN_TABLE(12, 13579, 270, 52, 8248, new Item[]{new Item(Constants.TEAK_PLANK, 3)}),
	BEER_BARREL(13, 13568, 87, 7, 8239, new Item[]{new Item(Constants.PLANK, 3), new Item(Constants.NAILS, 3)}),
	CIDER_BARREL(13, 13569, 91, 12, 8240, new Item[]{new Item(Constants.PLANK, 3), new Item(Constants.NAILS, 3), new Item(5763, 8)}, new int[][]{{7, 14}}),
	ASGARNIAN_ALE_BARREL(13, 13570, 184, 14, 8241, new Item[]{new Item(Constants.OAK_PLANK, 3), new Item(1905, 8)}, new int[][]{{7, 24}}),
	GREENMANS_ALE_BARREL(13, 13571, 184, 26, 8242, new Item[]{new Item(Constants.OAK_PLANK, 3), new Item(1909, 8)}, new int[][]{{7, 29}}),
	DRAGON_BITTER_BARREL(13, 13572, 224, 36, 8243, new Item[]{new Item(Constants.OAK_PLANK, 3), new Item(Constants.STEEL_BAR, 2), new Item(1911, 8)}, new int[][]{{7, 39}}),
	CHEFS_DELIGHT_BARREL(13, 13573, 224, 48, 8244, new Item[]{new Item(Constants.OAK_PLANK, 3), new Item(Constants.STEEL_BAR, 2), new Item(5755, 8)}, new int[][]{{7, 54}}),
	FIRE_PIT(14, 13528, 250, 40, 8216, new Item[]{new Item(Constants.SOFT_CLAY, 2), new Item(Constants.STEEL_BAR, 1)}),
	FIRE_PIT_HOOKS(14, 13529, 60, 11, 8217, new Item[]{new Item(Constants.SOFT_CLAY, 2), new Item(Constants.STEEL_BAR, 2)}),
	FIRE_PIT_POT(14, 13531, 80, 17, 8218, new Item[]{new Item(Constants.SOFT_CLAY, 2), new Item(Constants.STEEL_BAR, 3)}),
	SMALL_OVEN(14, 13533, 80, 24, 8219, new Item[]{new Item(Constants.STEEL_BAR, 4)}),
	LARGE_OVEN(14, 13536, 100, 29, 8220, new Item[]{new Item(Constants.STEEL_BAR, 5)}),
	STEEL_RANGE(14, 13539, 120, 34, 8221, new Item[]{new Item(Constants.STEEL_BAR, 6)}),
	FANCY_RANGE(14, 13542, 140, 42, 8222, new Item[]{new Item(Constants.STEEL_BAR, 8)}),
	WOODEN_LARDER(16, 13565, 228, 9, 8233, new Item[]{new Item(Constants.PLANK, 8), new Item(Constants.NAILS, 8)}),
	OAK_LARDER(16, 13566, 480, 33, 8234, new Item[]{new Item(Constants.OAK_PLANK, 8)}),
	TEAK_LARDER(16, 13567, 780, 43, 8235, new Item[]{new Item(Constants.TEAK_PLANK, 8), new Item(Constants.CLOTH, 2)}),
	PUMP_AND_DRAIN(15, 13559, 100, 7, 8230, new Item[]{new Item(Constants.STEEL_BAR, 5)}),
	PUMP_AND_TUB(15, 13561, 200, 27, 8231, new Item[]{new Item(Constants.STEEL_BAR, 10)}),
	SINK(15, 13563, 300, 47, 8232, new Item[]{new Item(Constants.STEEL_BAR, 15)}),
	WOODEN_SHELVES_1(17, 13545, 87, 6, 8223, new Item[]{new Item(Constants.PLANK, 3), new Item(Constants.NAILS, 3)}),
	WOODEN_SHELVES_2(17, 13546, 147, 12, 8224, new Item[]{new Item(Constants.PLANK, 3), new Item(Constants.NAILS, 3), new Item(Constants.SOFT_CLAY, 6)}),
	WOODEN_SHELVES_3(17, 13547, 147, 23, 8225, new Item[]{new Item(Constants.PLANK, 3), new Item(Constants.NAILS, 3), new Item(Constants.SOFT_CLAY, 6)}),
	OAK_SHELVES_1(17, 13548, 240, 34, 8226, new Item[]{new Item(Constants.OAK_PLANK, 3), new Item(Constants.SOFT_CLAY, 6)}),
	OAK_SHELVES_2(17, 13549, 240, 45, 8227, new Item[]{new Item(Constants.OAK_PLANK, 3), new Item(Constants.SOFT_CLAY, 6)}),
	TEAK_SHELVES_1(17, 13550, 330, 56, 8228, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(Constants.SOFT_CLAY, 6)}),
	TEAK_SHELVES_2(17, 13551, 930, 67, 8229, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(Constants.SOFT_CLAY, 6)}),
	/**
	 * bell pull
	 */
	ROPE_BELL_PULL(18, 13307, 64, 26, 8099, new Item[]{new Item(Constants.OAK_PLANK, 1), new Item(Constants.ROPE, 1)}),
	BELL_PULL(18, 13308, 120, 37, 8100, new Item[]{new Item(Constants.TEAK_PLANK, 1), new Item(Constants.CLOTH, 2)}),
	POSH_BELL_PULL(18, 13309, 420, 60, 8101, new Item[]{new Item(Constants.TEAK_PLANK, 1), new Item(Constants.CLOTH, 2), new Item(Constants.GOLD_LEAF, 1)}),
	/**
	 * Wall decoration (dining room)
	 */
	OAK_DECORATION(19, 13798, 120, 16, 8102, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	TEAK_DECORATION(19, 13814, 180, 36, 8103, new Item[]{new Item(Constants.TEAK_PLANK, 2)}),
	GILDED_DECORATION(19, 13782, 1020, 56, 8104, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3), new Item(Constants.GOLD_LEAF, 2)}),
	/**
	 * Dining room seating
	 */
	WOODEN_BENCH(20, 13300, 115, 10, 8108, new Item[]{new Item(Constants.PLANK, 4), new Item(Constants.NAILS, 4)}),
	OAK_BENCH(20, 13301, 240, 22, 8109, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	CARVED_OAK_BENCH(20, 13302, 240, 31, 8110, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	TEAK_DINING_BENCH(20, 13303, 360, 38, 8111, new Item[]{new Item(Constants.TEAK_PLANK, 4)}),
	CARVED_TEAK_DINING_BENCH(20, 13304, 360, 44, 8112, new Item[]{new Item(Constants.TEAK_PLANK, 4)}),
	MAHOGANY_BENCH(20, 13305, 560, 52, 8113, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4)}),
	GILDED_BENCH(20, 13306, 1760, 61, 8114, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.GOLD_LEAF, 4)}),
	/**
	 * Dining room table
	 */
	WOOD_DINING_TABLE(21, 13293, 115, 10, 8115, new Item[]{new Item(Constants.PLANK, 4), new Item(Constants.NAILS, 4)}),
	OAK_TABLE(21, 13294, 240, 22, 8116, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	CARVED_OAK_TABLE(21, 13295, 360, 31, 8117, new Item[]{new Item(Constants.OAK_PLANK, 5)}),
	TEAK_DINING_TABLE(21, 13296, 360, 38, 8118, new Item[]{new Item(Constants.TEAK_PLANK, 4)}),
	CARVED_TEAK_TABLE(21, 13297, 600, 45, 8119, new Item[]{new Item(Constants.TEAK_PLANK, 6), new Item(Constants.CLOTH, 4)}),
	MAHOGANY_TABLE(21, 13298, 840, 52, 8120, new Item[]{new Item(Constants.MAHOGANY_PLANK, 6)}),
	OPULENT_TABLE(21, 13299, 3100, 72, 8121, new Item[]{new Item(Constants.MAHOGANY_PLANK, 6), new Item(Constants.CLOTH, 4), new Item(Constants.GOLD_LEAF, 4), new Item(Constants.MARBLE_BLOCK, 2)}),
	/**
	 * Workshop
	 */
	TOOL_STORE_1(22, 13699, 120, 15, 8384, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	TOOL_STORE_2(22, 13700, 120, 25, 8385, new Item[]{new Item(Constants.OAK_PLANK, 2)}, 13699),
	TOOL_STORE_3(22, 13701, 120, 35, 8386, new Item[]{new Item(Constants.OAK_PLANK, 2)}, 13700),
	TOOL_STORE_4(22, 13702, 120, 44, 8387, new Item[]{new Item(Constants.OAK_PLANK, 2)}, 13701),
	TOOL_STORE_5(22, 13703, 121, 55, 8388, new Item[]{new Item(Constants.OAK_PLANK, 2)}, 13702),
	
	CRAFTING_TABLE_1(23, 13709, 50, 16, 8380, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	CRAFTING_TABLE_2(23, 13710, 100, 25, 8381, new Item[]{new Item(Constants.MOLTEN_GLASS, 1)}, 13709),
	CRAFTING_TABLE_3(23, 13711, 175, 34, 8382, new Item[]{new Item(Constants.MOLTEN_GLASS, 2)}, 13710),
	CRAFTING_TABLE_4(23, 13712, 240, 42, 8383, new Item[]{new Item(Constants.OAK_PLANK, 2)}, 13711),
	
	WOODEN_WORKBENCH(24, 13704, 145, 17, 8375, new Item[]{new Item(Constants.PLANK, 5), new Item(Constants.NAILS, 5)}),
	OAK_WORKBENCH(24, 13705, 300, 32, 8376, new Item[]{new Item(Constants.OAK_PLANK, 5)}),
	STEEL_FRAMED_BENCH(24, 13706, 145, 46, 8377, new Item[]{new Item(Constants.OAK_PLANK, 6), new Item(Constants.STEEL_BAR, 4)}),
	BENCH_WITH_VICE(24, 13707, 750, 62, 8378, new Item[]{new Item(Constants.OAK_PLANK, 2), new Item(Constants.STEEL_BAR, 1)}, 13706),
	BENCH_WITH_A_LATHE(24, 13708, 1000, 77, 8379, new Item[]{new Item(Constants.OAK_PLANK, 2), new Item(Constants.STEEL_BAR, 1)}, 13707),
	
	REAPIR_BENCH(25, 13713, 240, 15, 8389, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	WHETSTONE(25, 13714, 260, 35, 8390, new Item[]{new Item(Constants.OAK_PLANK, 4), new Item(Constants.LIMESTONE_BRICK, 1)}),
	ARMOUR_STAND(25, 13715, 500, 55, 8391, new Item[]{new Item(Constants.OAK_PLANK, 8), new Item(Constants.LIMESTONE_BRICK, 1)}),
	
	PLUMING_STAND(26, 13716, 120, 16, 8392, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	SHIELD_EASEL(26, 13717, 240, 41, 8393, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	BANNER_EASEL(26, 13718, 510, 66, 8394, new Item[]{new Item(Constants.OAK_PLANK, 8), new Item(Constants.CLOTH, 2)}),
	
	SHAVING_STAND(27, 13162, 30, 21, 8045, new Item[]{new Item(Constants.PLANK, 1), new Item(Constants.NAILS, 1), new Item(Constants.MOLTEN_GLASS, 1)}),
	OAK_SHAVING_STAND(27, 13163, 61, 29, 8046, new Item[]{new Item(Constants.OAK_PLANK, 1), new Item(Constants.MOLTEN_GLASS, 1)}),
	OAK_DRESSER(27, 13164, 121, 37, 8047, new Item[]{new Item(Constants.OAK_PLANK, 2), new Item(Constants.MOLTEN_GLASS, 1)}),
	TEAK_DRESSER(27, 13165, 181, 46, 8048, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(Constants.MOLTEN_GLASS, 1)}),
	FANCY_TEAK_DRESSER(27, 13166, 182, 56, 8049, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(Constants.MOLTEN_GLASS, 2)}),
	MAHOGANY_DRESSER(27, 13167, 281, 64, 8050, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(Constants.MOLTEN_GLASS, 1)}),
	GILDED_DRESSER(27, 13168, 582, 74, 8051, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(Constants.MOLTEN_GLASS, 2), new Item(Constants.GOLD_LEAF, 1)}),
	
	WOODEN_BED(28, 13148, 117, 20, 8031, new Item[]{new Item(Constants.PLANK, 3), new Item(Constants.NAILS, 3), new Item(Constants.CLOTH, 2)}),
	OAK_BED(28, 13149, 210, 30, 8032, new Item[]{new Item(Constants.OAK_PLANK, 3), new Item(Constants.CLOTH, 2)}),
	LARGE_OAK_BED(28, 13150, 330, 34, 8033, new Item[]{new Item(Constants.OAK_PLANK, 5), new Item(Constants.CLOTH, 2)}),
	TEAK_BED(28, 13151, 300, 40, 8034, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(Constants.CLOTH, 2)}),
	LARGE_TEAK_BED(28, 13152, 480, 45, 8035, new Item[]{new Item(Constants.TEAK_PLANK, 5), new Item(Constants.CLOTH, 2)}),
	FOUR_POSTER(28, 13153, 450, 53, 8036, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3), new Item(Constants.CLOTH, 2)}),
	GILDED_FOUR_POSTER(28, 13154, 1330, 60, 8037, new Item[]{new Item(Constants.MAHOGANY_PLANK, 5), new Item(Constants.CLOTH, 2), new Item(Constants.GOLD_LEAF, 2)}),
	
	SHOE_BOX(29, 13155, 58, 20, 8038, new Item[]{new Item(Constants.PLANK, 2), new Item(Constants.NAILS, 2)}),
	OAK_DRAWERS(29, 13156, 120, 27, 8039, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	OAK_WARDROBE(29, 13157, 180, 39, 8040, new Item[]{new Item(Constants.OAK_PLANK, 3)}),
	TEAK_DRAWERS(29, 13158, 180, 51, 8041, new Item[]{new Item(Constants.TEAK_PLANK, 2)}),
	TEAK_WARDROBE(29, 13159, 270, 63, 8042, new Item[]{new Item(Constants.TEAK_PLANK, 3)}),
	MAHOGANY_WARDROBE(29, 13160, 420, 75, 8043, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3)}),
	GILDED_WARDROBE(29, 13161, 720, 87, 8044, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3), new Item(Constants.GOLD_LEAF, 1)}),
	
	OAK_CLOCK(30, 13169, 142, 25, 8052, new Item[]{new Item(Constants.OAK_PLANK, 2), new Item(Constants.CLOCKWORK, 1)}),
	TEAK_CLOCK(30, 13170, 142, 55, 8053, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(Constants.CLOCKWORK, 1)}),
	GILDED_CLOCK(30, 13171, 602, 85, 8054, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(Constants.CLOCKWORK, 1), new Item(Constants.GOLD_LEAF, 1)}),
	
	RUNE_CASE_1(31, 13507, 190, 41, 8276, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(Constants.MOLTEN_GLASS, 2)}, new int[][]{{20, 44}}),
	RUNE_CASE_2(31, 13508, 212, 41, 8277, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(Constants.MOLTEN_GLASS, 2)}, new int[][]{{20, 44}}),
	MOUNTED_BASS(32, 13488, 151, 36, 8267, new Item[]{new Item(Constants.OAK_PLANK, 2), new Item(7990, 1)}),
	MOUNTED_SWORDFISH(32, 13489, 230, 56, 8268, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(7992, 1)}),
	MOUNTED_SHARK(32, 13490, 350, 76, 8269, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(7994, 1)}),
	
	CRAWLING_HAND(33, 13481, 211, 38, 8260, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(7982, 1)}),
	COCKTRICE_HEAD(33, 13482, 224, 38, 8261, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(7983, 1)}),
	BASILISK_HEAD(33, 13483, 243, 38, 8262, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(7984, 1)}),
	KURASK_HEAD(33, 13484, 357, 58, 8263, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(7985, 1)}),
	ABYSSAL_DEMON_HEAD(33, 13485, 389, 58, 8264, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(7986, 1)}),
	KING_BLACK_DRAGON_HEAD(33, 13486, 1103, 78, 8265, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(7987, 1), new Item(Constants.GOLD_LEAF, 2)}),
	KALPHITE_QUEEN_HEAD(33, 13487, 1103, 78, 8266, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(7988, 1), new Item(Constants.GOLD_LEAF, 2)}),
	
	MITHRIL_ARMOUR(34, 13491, 135, 28, 8270, new Item[]{new Item(1159, 1), new Item(1121, 1), new Item(1085, 1)}, new int[][]{{13, 68}}),
	ADAMANT_ARMOUR(34, 13492, 150, 28, 8271, new Item[]{new Item(1161, 1), new Item(1123, 1), new Item(1087, 1)}, new int[][]{{13, 88}}),
	RUNE_ARMOUR(34, 13493, 165, 28, 8272, new Item[]{new Item(1163, 1), new Item(1125, 1), new Item(1089, 1)}, new int[][]{{13, 99}}),
	
	BASIC_DECORATIVE_ARMOUR_STAND(35, 13495, 135, 28, 8273, new Item[]{new Item(4071, 1), new Item(4072, 1), new Item(4069, 1)}),
	DETAILED_DECORATIVE_ARMOUR_STAND(35, 13496, 150, 28, 8274, new Item[]{new Item(4506, 1), new Item(4507, 1), new Item(4504, 1)}),
	INTRICATE_DECORATIVE_ARMOUR_STAND(35, 13497, 165, 28, 8275, new Item[]{new Item(4511, 1), new Item(4512, 1), new Item(4509, 1)}),
	PROFOUND_DECORATIVE_ARMOUR_STAND(35, 34281, 180, 28, 18755, new Item[]{new Item(18708, 1), new Item(18709, 1), new Item(18706, 1)}),
	
	OAK_STAIRCASE(36, 13497, 680, 27, 8249, new Item[]{new Item(Constants.OAK_PLANK, 10), new Item(Constants.STEEL_BAR, 4)}),
	TEAK_STAIRCASE(36, 13499, 980, 48, 8252, new Item[]{new Item(Constants.TEAK_PLANK, 10), new Item(Constants.STEEL_BAR, 4)}),
	SPIRAL_STAIRCASE(36, 13503, 1040, 67, 8258, new Item[]{new Item(Constants.TEAK_PLANK, 10), new Item(Constants.LIMESTONE_BRICK, 7)}),
	MARBLE_STAIRCASE(36, 13501, 3200, 82, 8255, new Item[]{new Item(Constants.MAHOGANY_PLANK, 5), new Item(Constants.MARBLE_BLOCK, 5)}),
	MARBLE_SPIRAL(36, 13505, 4400, 97, 8259, new Item[]{new Item(Constants.TEAK_PLANK, 10), new Item(Constants.MARBLE_BLOCK, 10)}),
	
	OAK_STAIRCASE_1(37, 13498, 680, 27, 8249, new Item[]{new Item(Constants.OAK_PLANK, 10), new Item(Constants.STEEL_BAR, 4)}),
	TEAK_STAIRCASE_1(37, 13500, 980, 48, 8252, new Item[]{new Item(Constants.TEAK_PLANK, 10), new Item(Constants.STEEL_BAR, 4)}),
	SPIRAL_STAIRCASE_1(37, 13504, 1040, 67, 8258, new Item[]{new Item(Constants.TEAK_PLANK, 10), new Item(Constants.LIMESTONE_BRICK, 7)}),
	MARBLE_STAIRCASE_1(37, 13502, 3200, 82, 8255, new Item[]{new Item(Constants.MAHOGANY_PLANK, 5), new Item(Constants.MARBLE_BLOCK, 5)}),
	MARBLE_SPIRAL_1(37, 13506, 4400, 97, 8259, new Item[]{new Item(Constants.TEAK_PLANK, 10), new Item(Constants.MARBLE_BLOCK, 10)}),
	/*
	 * get me hotspot ids
	 */
	CLAY_ATTACK_STONE(39, 13392, 100, 39, 8153, new Item[]{new Item(Constants.SOFT_CLAY, 10)}),
	ATTACK_STONE(39, 13393, 200, 59, 8154, new Item[]{new Item(Constants.LIMESTONE_BRICK, 10)}),
	MARBLE_ATTACK_STONE(39, 13394, 2000, 79, 8155, new Item[]{new Item(Constants.MARBLE_BLOCK, 10)}),
	
	ELEMENTAL_BALANCE_1(40, 13395, 179, 37, 8156, new Item[]{new Item(Constants.AIR_RUNE, 500), new Item(Constants.EARTH_RUNE, 500), new Item(Constants.WATER_RUNE, 500), new Item(Constants.FIRE_RUNE, 500)}),
	ELEMENTAL_BALANCE_2(40, 13396, 252, 57, 8157, new Item[]{new Item(Constants.AIR_RUNE, 1000), new Item(Constants.EARTH_RUNE, 1000), new Item(Constants.WATER_RUNE, 1000), new Item(Constants.FIRE_RUNE, 1000)}),
	ELEMENTAL_BALANCE_3(40, 13397, 356, 77, 8158, new Item[]{new Item(Constants.AIR_RUNE, 2000), new Item(Constants.EARTH_RUNE, 2000), new Item(Constants.WATER_RUNE, 2000), new Item(Constants.FIRE_RUNE, 2000)}),
	
	JESTER(42, 13390, 360, 39, 8159, new Item[]{new Item(Constants.TEAK_PLANK, 4)}),
	TREASURE_HUNT(42, 13379, 800, 49, 8160, new Item[]{new Item(Constants.TEAK_PLANK, 8), new Item(Constants.STEEL_BAR, 4)}),
	HANGMAN(42, 13404, 1200, 59, 8161, new Item[]{new Item(Constants.TEAK_PLANK, 12), new Item(Constants.STEEL_BAR, 6)}),
	
	OAK_PRIZED_CHEST(41, 13384, 240, 34, 8165, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	TEAK_PRIZED_CHEST(41, 13386, 660, 34, 8166, new Item[]{new Item(Constants.TEAK_PLANK, 4), new Item(Constants.GOLD_LEAF, 4)}),
	MAHOGANY_PRIZED_CHEST(41, 13388, 860, 54, 8167, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.GOLD_LEAF, 4)}),
	
	HOOP_AND_STICK(38, 13399, 120, 30, 8162, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	DARTBOARD(38, 13400, 290, 54, 8163, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(Constants.STEEL_BAR, 1)}),
	ARCHERY_TARGET(38, 13402, 600, 81, 8164, new Item[]{new Item(Constants.TEAK_PLANK, 6), new Item(Constants.STEEL_BAR, 3)}),
	
	GLOVE_RACK(43, 13381, 120, 34, 8028, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	WEAPONS_RACK(43, 13382, 180, 44, 8029, new Item[]{new Item(Constants.TEAK_PLANK, 2)}),
	EXTRA_WEAPONS_RACK(43, 13383, 440, 54, 8030, new Item[]{new Item(Constants.TEAK_PLANK, 4), new Item(Constants.STEEL_BAR, 4)}),
	
	BOXING_RING(44, 13126, 570, 32, 8023, new Item[]{new Item(Constants.OAK_PLANK, 6), new Item(Constants.CLOTH, 4)}),
	FENCING_RING(44, 13133, 570, 41, 8024, new Item[]{new Item(Constants.OAK_PLANK, 8), new Item(Constants.CLOTH, 6)}),
	COMBAT_RING(44, 13137, 630, 51, 8025, new Item[]{new Item(Constants.TEAK_PLANK, 6), new Item(Constants.CLOTH, 6)}),
	RANGING_PEDESTALS(44, 13145, 620, 71, 8026, new Item[]{new Item(Constants.TEAK_PLANK, 8)}),
	// wall type (0)
	BALANCE_BEAM(44, 13142, 1000, 81, 8027, new Item[]{new Item(Constants.TEAK_PLANK, 10), new Item(Constants.STEEL_BAR, 5)}),
	//ground dec type (22)
	
	SMALL_MAP(45, 13525, 211, 38, 8294, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(8004, 1)}),
	MEDIUM_MAP(45, 13526, 451, 58, 8295, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3), new Item(8005, 1)}),
	LARGE_MAP(45, 13527, 591, 78, 8296, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(8006, 1)}),
	
	SILVERLIGHT(46, 13519, 187, 42, 8279, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(2402, 1)}),
	EXCALIBUR(46, 13520, 194, 42, 8280, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(35, 1)}),
	DARKLIGHT(46, 13521, 202, 42, 8281, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(6746, 1)}),
	
	LUMBRIDGE(47, 13517, 314, 44, 8289, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(8002, 1)}),
	THE_DESERT(47, 13514, 314, 44, 8290, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(8003, 1)}),
	MORYTANIA(47, 13518, 314, 44, 8291, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(8004, 1)}),
	KARAMJA(47, 13516, 464, 65, 8292, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3), new Item(8005, 1)}),
	ISAFDAR(47, 13515, 464, 65, 8293, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3), new Item(8006, 1)}),
	
	KING_ARTHUR(48, 13510, 211, 35, 8285, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(7995, 1)}),
	ELENA(48, 13511, 211, 35, 8286, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(7996, 1)}),
	GIANT_DWARF(48, 13512, 211, 35, 8287, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(7997, 1)}),
	MISCELLANIANS(48, 13513, 311, 55, 8288, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(7998, 1)}),
	
	ANTI_DRAGON_SHIELD(49, 13522, 280, 47, 8282, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(1540, 1)}),
	AMULET_OF_GLORY(49, 13523, 290, 47, 8283, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(1704, 1)}),
	CAPE_OF_LEGENDS(49, 13524, 300, 47, 8284, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(1052, 1)}),
	
	OAK_PET_HOUSE(50, 44828, 240, 37, 15227, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	TEAK_PET_HOUSE(50, 44829, 380, 52, 15228, new Item[]{new Item(Constants.TEAK_PLANK, 4)}),
	MAHOGANY_PET_HOUSE(50, 44830, 580, 67, 15229, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4)}),
	CONSECRATED_PET_HOUSE(50, 44831, 1580, 92, 15230, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.MAGIC_STONE, 1)}),
	DESECRATED_PET_HOUSE(50, 44832, 1580, 92, 15231, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.MAGIC_STONE, 1)}),
	NATURAL_PET_HOUSE(50, 44833, 1580, 92, 15232, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.MAGIC_STONE, 1)}),
	
	OAK_PET_FEEDER(51, 44834, 240, 37, 15233, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	TEAK_PET_FEEDER(51, 44835, 380, 52, 15234, new Item[]{new Item(Constants.TEAK_PLANK, 4)}),
	MAHOGANY_PET_FEEDER(51, 44836, 880, 67, 15235, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.GOLD_LEAF, 1)}),
	
	MINI_OBELISK(52, 44837, 676, 41, 15236, new Item[]{new Item(Constants.MARBLE_BLOCK, 1), new Item(12183, 1000)}),
	
	GARDEN_HABITAT(53, 0, 201, 37, 15222, new Item[]{new Item(8431, 1), new Item(8433, 1), new Item(8435, 1)}),
	JUNGLEN_HABITAT(53, 1, 287, 47, 15223, new Item[]{new Item(1929, 5), new Item(8435, 3), new Item(8423, 1)}),
	DESERT_HABITAT(53, 2, 238, 57, 15224, new Item[]{new Item(1783, 10), new Item(Constants.LIMESTONE_BRICK, 5), new Item(15237, 1)}),
	POLAR_HABITAT(53, 3, 373, 67, 15225, new Item[]{new Item(556, 1000), new Item(555, 1000), new Item(15239, 1)}),
	VOLCANIC_HABITAT(53, 4, 77, 77, 15226, new Item[]{new Item(4699, 1000), new Item(13245, 5), new Item(8417, 1)}),
	
	ALCHEMICAL_CHART(54, 13662, 30, 43, 8354, new Item[]{new Item(Constants.CLOTH, 2)}),
	ASTRONOMICAL_CHART(54, 13663, 30, 63, 8355, new Item[]{new Item(Constants.CLOTH, 3)}),
	INFERNO_CHART(54, 13664, 30, 83, 8356, new Item[]{new Item(Constants.CLOTH, 4)}),
	
	OAK_LECTERN(55, 13642, 60, 40, 8334, new Item[]{new Item(Constants.OAK_PLANK, 1)}),
	EAGLE_LECTERN(55, 13643, 120, 47, 8335, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	DEMON_LECTERN(55, 13644, 120, 47, 8336, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	TEAK_EAGLE_LECTERN(55, 13645, 180, 57, 8337, new Item[]{new Item(Constants.TEAK_PLANK, 2)}),
	TEAK_DEMON_LECTERN(55, 13646, 180, 57, 8338, new Item[]{new Item(Constants.TEAK_PLANK, 2)}),
	MAHOGANY_EAGLE_LECTERN(55, 13647, 570, 67, 8339, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(Constants.GOLD_LEAF, 1)}),
	MAHOGANY_DEMON_LECTERN(55, 13648, 570, 67, 8340, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(Constants.GOLD_LEAF, 1)}),
	
	STATUE(56, 48644, 2, 1, 15521, new Item[]{new Item(15521, 1)}),
	
	CRYSTAL_BALL(57, 13659, 280, 42, 8351, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(567, 1)}),
	ELEMENTAL_SPHERE(57, 13660, 580, 54, 8352, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(567, 1), new Item(Constants.GOLD_LEAF, 1)}),
	CRYSTAL_OF_POWER(57, 13661, 890, 66, 8353, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(567, 1), new Item(Constants.GOLD_LEAF, 1)}),
	
	GLOBE(58, 13649, 180, 41, 8341, new Item[]{new Item(Constants.OAK_PLANK, 3)}),
	ORNAMENTAL_GLOBE(58, 13650, 270, 50, 8342, new Item[]{new Item(Constants.TEAK_PLANK, 3)}),
	LUNAR_GLOBE(58, 13651, 570, 59, 8343, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(Constants.GOLD_LEAF, 1)}),
	CELESTIAL_GLOBE(58, 13652, 570, 68, 8344, new Item[]{new Item(Constants.TEAK_PLANK, 3), new Item(Constants.GOLD_LEAF, 1)}),
	ARMILLARY_GLOBE(58, 13653, 960, 77, 8345, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(Constants.GOLD_LEAF, 2), new Item(Constants.STEEL_BAR, 4)}),
	SMALL_ORRERY(58, 13654, 1320, 86, 8346, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3), new Item(Constants.GOLD_LEAF, 3)}),
	LARGE_ORRERY(58, 13655, 1420, 95, 8347, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3), new Item(Constants.GOLD_LEAF, 5)}),
	
	WOODEN_TELESCOPE(59, 13656, 121, 44, 8348, new Item[]{new Item(Constants.OAK_PLANK, 2), new Item(Constants.MOLTEN_GLASS, 1)}),
	TEAK_TELESCOPE(59, 13657, 181, 64, 8349, new Item[]{new Item(Constants.TEAK_PLANK, 2), new Item(Constants.MOLTEN_GLASS, 1)}),
	MAHOGANY_TELESCOPE(59, 13658, 580, 84, 8350, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(Constants.MOLTEN_GLASS, 1)}),
	
	/*
	 * Costume room
	 */
	OAK_TREASURE_CHEST(60, 18804, 450, 48, 9839, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	TEAK_TREASURE_CHEST(60, 18806, 1300, 66, 9840, new Item[]{new Item(Constants.TEAK_PLANK, 2)}),
	MOHOGANY_TREASURE_CHEST(60, 18808, 2300, 84, 9841, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2)}),
	
	OAK_ARMOR_CASE(63, 18778, 650, 46, 9826, new Item[]{new Item(Constants.OAK_PLANK, 3)}),
	TEAK_ARMOR_CASE(63, 18780, 950, 64, 9827, new Item[]{new Item(Constants.TEAK_PLANK, 3)}),
	MOHOGANY_ARMOR_CASE(63, 18782, 3500, 82, 9828, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3)}),
	
	OAK_MAGIC_WARDROBE(64, 18784, 1350, 42, 9829, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	CARVED_OAK_MAGIC_WARDROBE(64, 18786, 1500, 51, 9830, new Item[]{new Item(Constants.OAK_PLANK, 6)}),
	TEAK_MAGIC_WARDROBE(64, 18788, 1750, 60, 9831, new Item[]{new Item(Constants.TEAK_PLANK, 4)}),
	CARVED_TEAK_MAGIC_WARDROBE(64, 18790, 2150, 69, 9832, new Item[]{new Item(Constants.TEAK_PLANK, 6)}),
	MAHOGANY_MAGIC_WARDROBE(64, 18792, 3100, 78, 9833, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4)}),
	GILDED_MAGIC_WARDROBE(64, 18794, 3500, 87, 9834, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.GOLD_LEAF, 1)}),
	MARBLE_MAGIC_WARDROBE(64, 18796, 4150, 96, 9835, new Item[]{new Item(Constants.MARBLE_BLOCK, 4)}),
	
	OAK_CAPE_RACK(65, 18766, 2600, 54, 9817, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	TEAK_CAPE_RACK(65, 18767, 2850, 63, 9818, new Item[]{new Item(Constants.TEAK_PLANK, 4)}),
	MAHOGANY_CAPE_RACK(65, 18768, 3120, 72, 9819, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4)}),
	GILDED_CAPE_RACK(65, 18769, 3640, 81, 9820, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.GOLD_LEAF, 1)}),
	MARBLE_CAPE_RACK(65, 18770, 3850, 90, 9821, new Item[]{new Item(Constants.MARBLE_BLOCK, 1)}),
	MAGIC_CAPE_RACK(65, 18770, 4250, 99, 9822, new Item[]{new Item(Constants.MAGIC_STONE, 1)}),
	
	OAK_TOY_BOX(62, 18798, 1650, 50, 9836, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	TEAK_TOY_BOX(62, 18800, 1890, 68, 9837, new Item[]{new Item(Constants.TEAK_PLANK, 2)}),
	MAHOGANY_TOY_BOX(62, 18802, 2480, 86, 9838, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2)}),
	
	OAK_FANCY_DRESS_BOX(61, 18772, 850, 44, 9823, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	TEAK_FANCY_DRESS_BOX(61, 18774, 1300, 62, 9824, new Item[]{new Item(Constants.TEAK_PLANK, 2)}),
	MAHOGANY_FANCY_DRESS_BOX(61, 18776, 1890, 80, 9825, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2)}),
	
	SMALL_STATUES(66, 13271, 40, 49, 8082, new Item[]{new Item(Constants.LIMESTONE_BRICK, 2)}),
	MEDIUM_STATUES(66, 13272, 500, 69, 8083, new Item[]{new Item(Constants.MARBLE_BLOCK, 1)}),
	LARGE_STATUES(66, 13273, 1500, 89, 8084, new Item[]{new Item(Constants.MARBLE_BLOCK, 3)}),
	
	STEEL_TORCHES(67, 13200, 80, 45, 8070, new Item[]{new Item(Constants.STEEL_BAR, 2)}),
	WOODEN_TORCHES(67, 13202, 58, 49, 8069, new Item[]{new Item(Constants.PLANK, 2), new Item(Constants.NAILS, 2)}),
	STEEL_CANDLESTICKS(67, 13204, 124, 53, 8071, new Item[]{new Item(Constants.STEEL_BAR, 6), new Item(36, 6)}),
	GOLD_CANDLESTICKS(67, 13206, 46, 57, 8072, new Item[]{new Item(2357, 6), new Item(36, 6)}),
	INCENSE_BURNERS(67, 13208, 280, 61, 8073, new Item[]{new Item(Constants.OAK_PLANK, 4), new Item(Constants.STEEL_BAR, 2)}),
	MAHOGANY_BURNERS(67, 13210, 600, 65, 8074, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.STEEL_BAR, 2)}),
	MARBLE_BURNERS(67, 13212, 1600, 69, 8075, new Item[]{new Item(Constants.MARBLE_BLOCK, 2), new Item(Constants.STEEL_BAR, 2)}),
	
	WINDCHIMES(68, 13214, 323, 49, 8079, new Item[]{new Item(Constants.OAK_PLANK, 4), new Item(Constants.NAILS, 4), new Item(Constants.STEEL_BAR, 4)}),
	BELLS(68, 13215, 480, 58, 8080, new Item[]{new Item(Constants.TEAK_PLANK, 4), new Item(Constants.STEEL_BAR, 6)}),
	ORGAN(68, 13216, 680, 69, 8081, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.STEEL_BAR, 6)}),
	
	OAK_ALTAR(69, 13179, 240, 45, 8062, new Item[]{new Item(Constants.OAK_PLANK, 4)}),
	TEAK_ALTAR(69, 13182, 360, 50, 8063, new Item[]{new Item(Constants.TEAK_PLANK, 4)}),
	CLOTH_ALTAR(69, 13185, 390, 56, 8064, new Item[]{new Item(Constants.TEAK_PLANK, 4), new Item(Constants.CLOTH, 2)}),
	MAHOGANY_ALTAR(69, 13188, 590, 60, 8065, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.CLOTH, 2)}),
	LIMESTONE_ALTAR(69, 13191, 910, 64, 8066, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.CLOTH, 2), new Item(Constants.LIMESTONE_BRICK, 2)}),
	MARBLE_ALTAR(69, 13194, 1030, 70, 8067, new Item[]{new Item(Constants.MARBLE_BLOCK, 2), new Item(Constants.CLOTH, 2)}),
	GILDED_ALTAR(69, 13197, 2230, 75, 8068, new Item[]{new Item(Constants.MARBLE_BLOCK, 2), new Item(Constants.CLOTH, 2), new Item(Constants.GOLD_LEAF, 4)}),
	
	GUTHIX_SYMBOL(70, 13174, 120, 48, 8057, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	SARADOMIN_SYMBOL(70, 13172, 120, 48, 8055, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	ZAMORAK_SYMBOL(70, 13173, 120, 48, 8056, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	GUTHIX_ICON(70, 13177, 960, 59, 8060, new Item[]{new Item(Constants.TEAK_PLANK, 4), new Item(Constants.GOLD_LEAF, 2)}),
	SARADOMIN_ICON(70, 13175, 960, 59, 8058, new Item[]{new Item(Constants.TEAK_PLANK, 4), new Item(Constants.GOLD_LEAF, 2)}),
	ZAMORAK_ICON(70, 13176, 960, 59, 8059, new Item[]{new Item(Constants.TEAK_PLANK, 4), new Item(Constants.GOLD_LEAF, 2)}),
	ICON_OF_BOB(70, 13178, 1160, 71, 8061, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.GOLD_LEAF, 2)}),
	
	SHUTTERED_WINDOW(71, 13217, 228, 49, 8076, new Item[]{new Item(Constants.PLANK, 8), new Item(Constants.NAILS, 8)}),
	DECORATIVE_WINDOW(71, 13220, 400, 69, 8077, new Item[]{new Item(Constants.MOLTEN_GLASS, 8)}),
	STAINED_GLASS(71, 13221, 400, 79, 8078, new Item[]{new Item(Constants.MOLTEN_GLASS, 16)}),
	
	TEAK_PORTAL(72, 13636, 270, 50, 8328, new Item[]{new Item(Constants.TEAK_PLANK, 3)}),
	MAHOGANY_PORTAL(72, 13637, 500, 65, 8329, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3)}),
	MARBLE_PORTAL(72, 13638, 2000, 80, 8330, new Item[]{new Item(Constants.MARBLE_BLOCK, 3)}),
	
	TELEPORT_FOCUS(73, 13640, 40, 50, 8331, new Item[]{new Item(Constants.LIMESTONE_BRICK, 2)}),
	GREATER_FOCUS(73, 13641, 500, 65, 8332, new Item[]{new Item(Constants.MARBLE_BLOCK, 1)}),
	SCYING_POOL(73, 13639, 2000, 80, 8333, new Item[]{new Item(Constants.MARBLE_BLOCK, 4)}),
	
	/*
	 * Formal Garden
	 */
	EXIT_PORTAL_(74, 13405, 100, 1, 8168, new Item[]{new Item(Constants.IRON_BAR, 10)}),
	GAZEBO(74, 13477, 1200, 65, 8192, new Item[]{new Item(Constants.MAHOGANY_PLANK, 4), new Item(Constants.STEEL_BAR, 4)}),
	DUNGEON_ENTERANCE(74, 13409, 500, 70, 8172, new Item[]{new Item(Constants.MARBLE_BLOCK, 1)}),
	SMALL_FOUNTAIN(74, 13478, 500, 71, 8193, new Item[]{new Item(Constants.MARBLE_BLOCK, 1)}),
	LARGE_FOUNTAIN(74, 13479, 1000, 75, 8194, new Item[]{new Item(Constants.MARBLE_BLOCK, 2)}),
	POSH_FOUNTAIN(74, 13480, 1500, 81, 8195, new Item[]{new Item(Constants.MARBLE_BLOCK, 3)}),
	
	SUN_FLOWER(75, 13443, 70, 66, 8213, new Item[]{new Item(Constants.BSF, 1), new Item(Constants.WATER_CAN, 1)}),
	MARIGOLDS(75, 13444, 100, 71, 8214, new Item[]{new Item(Constants.BM, 1), new Item(Constants.WATER_CAN, 1)}),
	ROSES(75, 13448, 100, 76, 8215, new Item[]{new Item(Constants.BR, 1), new Item(Constants.WATER_CAN, 1)}),
	
	ROSEMARY(76, 13437, 70, 66, 8210, new Item[]{new Item(Constants.BRM, 1), new Item(Constants.WATER_CAN, 1)}),
	DAFFODILS(76, 13441, 100, 71, 8211, new Item[]{new Item(Constants.BD, 1), new Item(Constants.WATER_CAN, 1)}),
	BLUEBELLS(76, 13439, 122, 76, 8212, new Item[]{new Item(Constants.BBB, 1), new Item(Constants.WATER_CAN, 1)}),
	
	THORNY_HEDGE(77, 13456, 70, 56, 8203, new Item[]{new Item(Constants.BTH, 1), new Item(Constants.WATER_CAN, 1)}),
	NICE_HEDGE(77, 13459, 100, 60, 8204, new Item[]{new Item(Constants.BNH, 1), new Item(Constants.WATER_CAN, 1)}),
	SMALL_BOX_HEDGE(77, 13462, 122, 64, 8205, new Item[]{new Item(Constants.SBH, 1), new Item(Constants.WATER_CAN, 1)}),
	TOPIARY_HEDGE(77, 13465, 141, 68, 8206, new Item[]{new Item(Constants.TH, 1), new Item(Constants.WATER_CAN, 1)}),
	FANCY_HEDGE(77, 13468, 158, 72, 8207, new Item[]{new Item(Constants.FH, 1), new Item(Constants.WATER_CAN, 1)}),
	TALL_FANCY_HEDGE(77, 13471, 223, 76, 8208, new Item[]{new Item(Constants.TFH, 1), new Item(Constants.WATER_CAN, 1)}),
	TALL_BOX_HEDGE(77, 13474, 316, 80, 8209, new Item[]{new Item(Constants.TFH, 1), new Item(Constants.WATER_CAN, 1)}),
	
	BOUNDARY_STONES(78, 13449, 100, 55, 8196, new Item[]{new Item(Constants.SOFT_CLAY, 10)}),
	WOODEN_FENCE(78, 13450, 280, 59, 8197, new Item[]{new Item(Constants.PLANK, 10)}),
	STONE_WALL(78, 13451, 200, 63, 8198, new Item[]{new Item(Constants.LIMESTONE_BRICK, 10)}),
	IRON_RAILINGS(78, 13452, 220, 67, 8199, new Item[]{new Item(Constants.IRON_BAR, 10), new Item(Constants.LIMESTONE_BRICK, 6)}),
	PICKET_FENCE(78, 13453, 640, 71, 8200, new Item[]{new Item(Constants.OAK_PLANK, 10), new Item(Constants.STEEL_BAR, 2)}),
	GARDEN_FENCE(78, 13454, 940, 75, 8201, new Item[]{new Item(Constants.TEAK_PLANK, 10), new Item(Constants.STEEL_BAR, 2)}),
	MARBLE_WALL(78, 13455, 4000, 79, 8202, new Item[]{new Item(Constants.MARBLE_BLOCK, 8)}),
	
	/*
	 * Throne room
	 */
	OAK_THRONE(79, 13665, 800, 60, 8357, new Item[]{new Item(Constants.OAK_PLANK, 5), new Item(Constants.MARBLE_BLOCK, 2)}),
	TEAK_THRONE(79, 13666, 1450, 67, 8358, new Item[]{new Item(Constants.TEAK_PLANK, 5), new Item(Constants.MARBLE_BLOCK, 2)}),
	MAHOGANY_THRONE(79, 13667, 2200, 74, 8359, new Item[]{new Item(Constants.MAHOGANY_PLANK, 10), new Item(Constants.MARBLE_BLOCK, 3)}),
	GILDED_THRONE(79, 13668, 1700, 81, 8360, new Item[]{new Item(Constants.MAHOGANY_PLANK, 10), new Item(Constants.MARBLE_BLOCK, 3), new Item(Constants.GOLD_LEAF, 3)}),
	SKELETON_THRONE(79, 13669, 7003, 88, 8361, new Item[]{new Item(Constants.OAK_PLANK, 10), new Item(Constants.MARBLE_BLOCK, 2), new Item(Constants.BONES, 5), new Item(Constants.SKULLS, 2)}),
	CRYSTAL_THRONE(79, 13670, 15000, 95, 8362, new Item[]{new Item(Constants.MAGIC_STONE, 15)}),
	DEMONIC_THRONE(79, 13671, 25000, 99, 8363, new Item[]{new Item(Constants.MAGIC_STONE, 25)}),
	
	OAK_LEVER(80, 13672, 300, 68, 8364, new Item[]{new Item(Constants.OAK_PLANK, 5)}),
	TEAK_LEVER(80, 13673, 450, 78, 8365, new Item[]{new Item(Constants.TEAK_PLANK, 5)}),
	MAHOGANY_LEVER(80, 13674, 700, 88, 8366, new Item[]{new Item(Constants.MAHOGANY_PLANK, 5)}),
	
	OAK_TRAPDOOR(81, 13675, 300, 68, 8367, new Item[]{new Item(Constants.OAK_PLANK, 5)}),
	TEAK_TRAPDOOR(81, 13676, 450, 78, 8368, new Item[]{new Item(Constants.TEAK_PLANK, 5)}),
	MAHOGANY_TRAPDOOR(81, 13677, 700, 88, 8369, new Item[]{new Item(Constants.MAHOGANY_PLANK, 5)}),
	
	OAK_DECORATION_(83, 13798, 120, 16, 8102, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	TEAK_DECORATION_(83, 13814, 180, 36, 8103, new Item[]{new Item(Constants.TEAK_PLANK, 2)}),
	GILDED_DECORATION_(83, 13782, 1020, 56, 8104, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3), new Item(Constants.GOLD_LEAF, 2)}),
	ROUND_SHIELD(83, 13734, 120, 66, 8105, new Item[]{new Item(Constants.OAK_PLANK, 2)}),
	SQUARE_SHEILD(83, 13766, 330, 76, 8106, new Item[]{new Item(Constants.TEAK_PLANK, 4)}),
	KITE_DECORATION(83, 13750, 420, 86, 8107, new Item[]{new Item(Constants.MAHOGANY_PLANK, 3)}),
	
	FLOOR_DECORATION(84, 13684, 700, 61, 8370, new Item[]{new Item(Constants.MAHOGANY_PLANK, 5)}),
	STEEL_CAGE(84, 13685, 1100, 68, 68, new Item[]{new Item(Constants.MAHOGANY_PLANK, 5), new Item(Constants.STEEL_BAR, 20)}),
	//13681
	TRAPDOOR(84, 13686, 770, 74, 8372, new Item[]{new Item(Constants.MAHOGANY_PLANK, 5), new Item(Constants.STEEL_BAR, 20)}),
	//13680
	LESSER_MAGIC_CIRCLE(84, 13687, 2700, 82, 8373, new Item[]{new Item(Constants.MAHOGANY_PLANK, 2), new Item(Constants.MAGIC_STONE, 2)}),
	//13682
	GREATER_MAGIC_CIRCLE(84, 13688, 4700, 89, 8374, new Item[]{new Item(Constants.MAHOGANY_PLANK, 5), new Item(Constants.MAGIC_STONE, 4)}),
	//13683
	
	/*
	 * Oubliette room
	 */
	TENTACLE_POOL(85, 13331, 326, 71, 8303, new Item[]{new Item(Constants.BOW, 20), new Item(Constants.COIN, 100000)}),
	SPIKES(85, 13334, 623, 65, 8302, new Item[]{new Item(Constants.STEEL_BAR, 20), new Item(Constants.COIN, 50000)}),
	FLAME_PIT(85, 13337, 357, 77, 8304, new Item[]{new Item(Constants.STEEL_BAR, 20), new Item(Constants.COIN, 50000)}),
	ROCNAR(85, 13373, 387, 83, 8305, new Item[]{new Item(Constants.COIN, 150000)}),
	
	//+2 more each type pls (cages only)
	OAK_CAGE(86, 13313, 640, 65, 8297, new Item[]{new Item(Constants.OAK_PLANK, 10), new Item(Constants.STEEL_BAR, 2)}),
	OAK_AND_STEEL_CAGE(86, 13316, 800, 70, 8298, new Item[]{new Item(Constants.OAK_PLANK, 10), new Item(Constants.STEEL_BAR, 10)}),
	STEEL_CAGE_(86, 13319, 400, 75, 8299, new Item[]{new Item(Constants.STEEL_BAR, 2)}),
	SPIKED_CAGE(86, 13322, 500, 80, 8300, new Item[]{new Item(Constants.STEEL_BAR, 2)}),
	BONE_CAGE(86, 13325, 603, 85, 8301, new Item[]{new Item(Constants.STEEL_BAR, 2)}),
	
	SKELETON_GUARD(87, 13366, 223, 70, 8131, new Item[]{new Item(Constants.COIN, 50000)}),
	GUARD_DOG(87, 13367, 273, 74, 8132, new Item[]{new Item(Constants.COIN, 75000)}),
	HOBGOBLIN(87, 13368, 316, 78, 8133, new Item[]{new Item(Constants.COIN, 100000)}),
	BABY_RED_DRAGON(87, 13372, 387, 82, 8134, new Item[]{new Item(Constants.COIN, 150000)}),
	HUGE_SPIDER(87, 13370, 447, 86, 8135, new Item[]{new Item(Constants.COIN, 200000)}),
	TROLL(87, 13369, 1000, 90, 8136, new Item[]{new Item(Constants.COIN, 1000000)}),
	HELLHOUND(87, 2715, 2236, 94, 8137, new Item[]{new Item(Constants.COIN, 5000000)}),
	
	OAK_LADDER(88, 13328, 300, 68, 8306, new Item[]{new Item(Constants.OAK_PLANK, 5)}),
	TEAK_LADDER(88, 13329, 450, 78, 8307, new Item[]{new Item(Constants.TEAK_PLANK, 5)}),
	MAHOGANY_LADDER(88, 13330, 700, 88, 8308, new Item[]{new Item(Constants.MAHOGANY_PLANK, 5)}),
	
	DECORATIVE_BLOOD(89, 13312, 4, 72, 8125, new Item[]{new Item(Constants.RD, 4)}),
	DECORATIVE_PIPE(89, 13311, 120, 83, 8126, new Item[]{new Item(Constants.STEEL_BAR, 6)}),
	HANGING_SKELETON(89, 13310, 3, 94, 8127, new Item[]{new Item(Constants.SKULLS, 2), new Item(Constants.BONES, 6)}),
	
	CANDLE(90, 13342, 243, 83, 8128, new Item[]{new Item(Constants.OAK_PLANK, 4), new Item(Constants.LIT_CANDLE, 4)}),
	TORCH(90, 13343, 244, 84, 8129, new Item[]{new Item(Constants.OAK_PLANK, 4), new Item(Constants.LIT_CANDLE, 4)}),
	SKULL_TORCH(90, 13343, 244, 84, 8130, new Item[]{new Item(Constants.OAK_PLANK, 4), new Item(Constants.LIT_CANDLE, 4), new Item(Constants.SKULLS, 4)}),
	
	SPIKE_TRAP(91, 13356, 223, 72, 8143, new Item[]{new Item(Constants.COIN, 50000)}),
	MAN_TRAP(91, 13357, 273, 76, 8144, new Item[]{new Item(Constants.COIN, 75000)}),
	TANGLE_TRAP(91, 13358, 323, 80, 8145, new Item[]{new Item(Constants.COIN, 100000)}),
	MARBLE_TRAP(91, 13359, 387, 84, 8146, new Item[]{new Item(Constants.COIN, 150000)}),
	TELEPORT_TRAP(91, 13360, 470, 88, 8147, new Item[]{new Item(Constants.COIN, 200000)}),
	
	OAK_DOOR(92, 13344, 600, 74, 8122, new Item[]{new Item(Constants.OAK_PLANK, 10)}),
	STEEL_PLATED_DOOR(92, 13346, 800, 84, 8123, new Item[]{new Item(Constants.OAK_PLANK, 10), new Item(Constants.STEEL_BAR, 10)}),
	MARBLE_DOOR(92, 13348, 2000, 94, 8124, new Item[]{new Item(Constants.MARBLE_BLOCK, 4)}),
	/**
	 * Pit
	 */
	MINOR_PIT_TRAP(93, 39266, 304, 71, 18797, new Item[]{new Item(Constants.COIN, 45000), new Item(554, 500)}),
	MAJOR_PIT_TRAP(93, 39268, 1000, 83, 18798, new Item[]{new Item(Constants.COIN, 125000), new Item(554, 2500)}),
	SUPERIOR_PIT_TRAP(93, 39270, 1100, 96, 18799, new Item[]{new Item(Constants.COIN, 850000), new Item(554, 4500)}),
	
	PIT_DOG(94, 39260, 200, 70, 18791, new Item[]{new Item(Constants.COIN, 40000)}),
	PIT_OGRE(94, 39261, 234, 73, 18792, new Item[]{new Item(Constants.COIN, 55000)}),
	PIT_PROTECTOR(94, 39262, 300, 79, 18793, new Item[]{new Item(Constants.COIN, 90000)}),
	PIT_SCARABITE(94, 39263, 387, 84, 18794, new Item[]{new Item(Constants.COIN, 150000)}),
	PIT_BLACK_DEMON(94, 39264, 547, 89, 18795, new Item[]{new Item(Constants.COIN, 300000)}),
	PIT_IRON_DRAGON(94, 39265, 2738, 97, 18796, new Item[]{new Item(Constants.COIN, 7500000)}),
	
	/**
	 * Treasure room
	 */
	DEMON(95, 13378, 707, 75, 8138, new Item[]{new Item(Constants.COIN, 500000)}),
	KALPHITE_SOLDIER(95, 13374, 866, 80, 8139, new Item[]{new Item(Constants.COIN, 750000)}),
	TOK_XIL(95, 13377, 2236, 85, 8140, new Item[]{new Item(Constants.COIN, 5000000)}),
	DAGANNOTH(95, 13376, 2738, 90, 8141, new Item[]{new Item(Constants.COIN, 7500000)}),
	STEEL_DRAGON(95, 13375, 3162, 95, 8142, new Item[]{new Item(Constants.COIN, 10000000)}),
	
	WOODEN_CRATE(96, 13283, 143, 75, 8148, new Item[]{new Item(Constants.PLANK, 5), new Item(Constants.NAILS, 5)}),
	OAK_CRATE(96, 13285, 340, 79, 8149, new Item[]{new Item(Constants.OAK_PLANK, 5), new Item(Constants.STEEL_BAR, 2)}),
	TEAK_CRATE(96, 13287, 530, 83, 8150, new Item[]{new Item(Constants.TEAK_PLANK, 5), new Item(Constants.STEEL_BAR, 4)}),
	MAHOGANY_CRATE(96, 13289, 1000, 87, 8151, new Item[]{new Item(Constants.MAHOGANY_PLANK, 5), new Item(Constants.GOLD_LEAF, 1)}),
	MAGIC_CRATE(96, 13291, 1000, 91, 8152, new Item[]{new Item(Constants.MAGIC_STONE, 1)});
	
	public static final ImmutableSet<Furniture> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Furniture.class));
	
	private int hotspotId, furnitureId, xp, level, itemId;
	private int[][] additionalSkillRequirements;
	private Item[] requiredItems;
	private int furnitureRequired;
	
	Furniture(int hotspotId, int furnitureId, int xp, int level, int itemId, Item[] requiredItems) {
		this.hotspotId = hotspotId;
		this.furnitureId = furnitureId;
		this.xp = xp;
		this.level = level;
		this.requiredItems = requiredItems;
		this.itemId = itemId;
		furnitureRequired = -1;
	}
	
	Furniture(int hotspotId, int furnitureId, int xp, int level, int itemId, Item[] requiredItems, int[][] additionalSkillRequirements) {
		this(hotspotId, furnitureId, xp, level, itemId, requiredItems);
		this.setAdditionalSkillRequirements(additionalSkillRequirements);
	}
	
	Furniture(int hotspotId, int furnitureId, int xp, int level, int itemId, Item[] requiredItems, int furnitureRequired) {
		this(hotspotId, furnitureId, xp, level, itemId, requiredItems);
		this.setFurnitureRequired(furnitureRequired);
	}
	
	public static Furniture forFurnitureId(int furnitureId) {
		for(Furniture f : VALUES) {
			if(f.getId() == furnitureId) {
				return f;
			}
		}
		return null;
	}
	
	public static Furniture forItemId(int itemId) {
		for(Furniture f : VALUES) {
			if(f.getItemId() == itemId) {
				return f;
			}
		}
		return null;
	}
	
	public int getItemId() {
		return itemId;
	}
	
	public Item[] getRequiredItems() {
		return requiredItems;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getXP() {
		return xp;
	}
	
	public int getHotSpotId() {
		return hotspotId;
	}
	
	public int getId() {
		return furnitureId;
	}
	
	public int getFurnitureRequired() {
		return furnitureRequired;
	}
	
	public void setFurnitureRequired(int furnitureRequired) {
		this.furnitureRequired = furnitureRequired;
	}
	
	public int[][] getAdditionalSkillRequirements() {
		return additionalSkillRequirements;
	}
	
	public void setAdditionalSkillRequirements(int[][] additionalSkillRequirements) {
		this.additionalSkillRequirements = additionalSkillRequirements;
	}
}