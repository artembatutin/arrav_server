package net.edge.content.skill.construction.data;

import net.edge.content.skill.construction.Construction;
import net.edge.content.skill.construction.furniture.HotSpots;
import net.edge.locale.Position;
import net.edge.world.object.ObjectDefinition;

/**
 * Constants declared for {@link Construction}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Constants {
	
	/**
	 * Construction room identifications.
	 */
	public static final int EMPTY = 0, BUILDABLE = 1, GARDEN = 2, PARLOUR = 3, KITCHEN = 4, DINING_ROOM = 5, WORKSHOP = 6, BEDROOM = 7, SKILL_ROOM = 8, QUEST_HALL_DOWN = 9, GAMES_ROOM = 10, COMBAT_ROOM = 11, QUEST_ROOM = 12, MENAGERY = 13, STUDY = 14, COSTUME_ROOM = 15, CHAPEL = 16, PORTAL_ROOM = 17, FORMAL_GARDEN = 18, THRONE_ROOM = 19, OUBLIETTE = 20, PIT = 21, DUNGEON_STAIR_ROOM = 22, TREASURE_ROOM = 23, CORRIDOR = 24, JUNCTION = 25, SKILL_HALL_DOWN = 26, ROOF = 27, DUNGEON_EMPTY = 28;
	
	/**
	 * Construction material parts for construction.
	 */
	public static final int LIT_CANDLE = 34, PLANK = 960, OAK_PLANK = 8778, TEAK_PLANK = 8780, MAHOGANY_PLANK = 8782, GOLD_LEAF = 8784, MARBLE_BLOCK = 8786, MAGIC_STONE = 8788, CLOTH = 8790, CLOCKWORK = 8792, SAW = 8794, NAILS = 1539, MOLTEN_GLASS = 1775, SOFT_CLAY = 1761, LIMESTONE_BRICK = 3420, STEEL_BAR = 2353, IRON_BAR = 2351, ROPE = 954, AIR_RUNE = 556, WATER_RUNE = 555, EARTH_RUNE = 557, FIRE_RUNE = 554, BSF = 8457, WATER_CAN = 5331, BM = 8459, BR = 8461, BRM = 8451, BD = 8453, BBB = 8455, BTH = 8437, BNH = 8439, SBH = 8441, TH = 8443, FH = 8445, TFH = 8447, TBH = 8449, BONES = 526, SKULLS = 964, COIN = 995, BOW = 1929, RD = 1763;
	
	public static final int BASE_X = 1856, BASE_Y = 5056, MIDDLE_X = 1912, MIDDLE_Y = 5112;
	
	public static final int[] DOORSPACEIDS = new int[]{15314, 15313, 15305, 15306, 15317};
	
	public static final String[] HANGMANWORDS = new String[]{"DENNISISNOOB", "ABYSSAL", "ADAMANTITE", "ALKHARID", "ARDOUGNE", "ASGARNIA", "AVANTOE", "BASILISK", "BANSHEE", "BARROWS", "BLOODVELD", "BOBTHECAT", "BRIMHAVEN", "BURTHORPE", "CADANTINE", "CAMELOT", "CANIFIS", "CATHERBY", "CHAOSDRUID", "CHAOSDWARF", "CHOMPYBIRD", "COCKATRICE", "CRANDOR", "CROMADIURE", "DAGANNOTH", "DORGESHUUN", "DRAGON", "DRAYNOR", "DUSTDEVIL", "DWARFWEED", "EDGEVILLE", "ENTRANA", "FALADOR", "FELDIP", "FIREGIANT", "FREMENNIK", "GARGOYLE", "GOBLIN", "GRANDTREE", "GUAMLEAF", "GUTANOTH", "GUTHIX", "HILLGIANT", "HELLHOUND", "HIGHWAYMAN", "HOBGOBLIN", "ICEGIANT", "ICEQUEEN", "ICEWARRIOR", "ICEWOLF", "ICETROLL", "IRITLEAF", "ISAFDAR", "JOGRE", "KALPHITE", "KANDARIN", "KARAMJA", "KELDAGRIM", "KHAZARD", "KWUARM", "LANTADYME", "LLETYA", "LUMBRIDGE", "NECHRYAEL", "MARRENTILL", "MENAPHOS", "MISTHALIN", "MITHRIL", "MOGRE", "MORTTON", "MORYTANIA", "MOSSGIANT", "NIGHTSHADE", "PALADIN", "PHASMATYS", "PORTSARIM", "PRIFDDINAS", "PYREFIEND", "RANARRWEED", "RELLEKKA", "RIMMINGTON", "RUNESCAPE", "RUNITE", "SARADOMIN", "SKELETON", "SNAPDRAGON", "SNAPEGRASS", "SOPHANEM", "SOULLESS", "SPIRITTREE", "TARROMIN", "TAVERLEY", "TERRORBIRD", "TIRANNWN", "TOADFLAX", "TORSTOL", "UGTHANKI", "UNICORN", "VARROCK", "WHIP", "YANILLE", "ZAMORAK"};
	
	public static final Position DRAYNOR = new Position(3092, 3248);
	public static final Position YANILLE = new Position(2606, 3093);
	public static final int VARROCK_X = 3210;
	public static final int VARROCK_Y = 3424;
	public static final String VARROCK = "";
	public static final int LUMBY_X = 3222;
	public static final int LUMBY_Y = 3218;
	public static final String LUMBY = "";
	public static final int FALADOR_X = 2964;
	public static final int FALADOR_Y = 3378;
	public static final String FALADOR = "";
	public static final int CAMELOT_X = 2757;
	public static final int CAMELOT_Y = 3477;
	public static final String CAMELOT = "";
	public static final int ARDOUGNE_X = 2662;
	public static final int ARDOUGNE_Y = 3305;
	public static final String ARDOUGNE = "";
	public static final int KHARYRLL_X = 3492;
	public static final int KHARYRLL_Y = 3471;
	
	public static final int EDGEVILLE_X = 3087;
	public static final int EDGEVILLE_Y = 3500;
	public static final String EDGEVILLE = "";
	public static final int AL_KHARID_X = 2916;
	public static final int AL_KHARID_Y = 3176;
	public static final String AL_KHARID = "";
	public static final int KARAMJA_X = 3293;
	public static final int KARAMJA_Y = 3183;
	public static final String KARAMJA = "";
	public static final int MAGEBANK_X = 3084;
	public static final int MAGEBANK_Y = 3248;
	public static final String MAGEBANK = "";
	
	public static boolean isDungeonRoom(int roomType) {
		return roomType == DUNGEON_STAIR_ROOM || roomType == CORRIDOR || roomType == JUNCTION || roomType == OUBLIETTE || roomType == PIT || roomType == TREASURE_ROOM;
	}
	
	public static boolean isGardenRoom(int roomType) {
		return roomType == GARDEN || roomType == FORMAL_GARDEN;
	}
	
	public static int getXOffsetForObjectId(int objectId, int hsId, int rotation) {
		HotSpots hs = HotSpots.forObjectId(hsId);
		if(hs == null)
			return 0;
		ObjectDefinition objectDef = ObjectDefinition.DEFINITIONS[objectId];
		return getRotatedLandscapeChunkX(rotation, objectDef.getSizeY(), hs.getXOffset(), hs.getYOffset(), objectDef.getSizeX(), hs
				.getRotation(0));
	}
	
	public static int getYOffsetForObjectId(int objectId, int hsId, int rotation) {
		HotSpots hs = HotSpots.forObjectId(hsId);
		if(hs == null)
			return 0;
		ObjectDefinition objectDef = ObjectDefinition.DEFINITIONS[objectId];
		return getRotatedLandscapeChunkY(hs.getYOffset(), objectDef.getSizeY(), rotation, objectDef.getSizeX(), hs.getXOffset(), hs
				.getRotation(0));
	}
	
	public static int getYOffsetForObjectId(int objectId, int offsetX, int offsetY, int rotation, int objectRot) {
		ObjectDefinition objectDef = ObjectDefinition.DEFINITIONS[objectId];
		return getRotatedLandscapeChunkY(offsetY, objectDef.getSizeY(), rotation, objectDef.getSizeX(), offsetX, objectRot);
	}
	
	public static int getXOffsetForObjectId(int objectId, int offsetX, int offsetY, int rotation, int objectRot) {
		ObjectDefinition objectDef = ObjectDefinition.DEFINITIONS[objectId];
		return getRotatedLandscapeChunkX(rotation, objectDef.getSizeY(), offsetX, offsetY, objectDef.getSizeX(), objectRot);
	}
	
	public static int getXOffsetForObjectId(int objectId, HotSpots hs, int rotation) {
		ObjectDefinition objectDef = ObjectDefinition.DEFINITIONS[objectId];
		return getRotatedLandscapeChunkX(rotation, objectDef.getSizeY(), hs.getXOffset(), hs.getYOffset(), objectDef.getSizeX(), hs
				.getRotation(0));
	}
	
	public static int getYOffsetForObjectId(int objectId, HotSpots hs, int rotation) {
		ObjectDefinition objectDef = ObjectDefinition.DEFINITIONS[objectId];
		return getRotatedLandscapeChunkY(hs.getYOffset(), objectDef.getSizeY(), rotation, objectDef.getSizeX(), hs.getXOffset(), hs
				.getRotation(0));
	}
	
	public static int getRotatedLandscapeChunkX(int rotation, int objectSizeY, int x, int y, int objectSizeX, int objectRot) {
		rotation &= 3;
		int tmp1 = objectSizeX;
		int tmp2 = objectSizeY;
		if(!(objectRot == 0 || objectRot == 2)) {
			objectSizeX = tmp2;
			objectSizeY = tmp1;
		}
		if(rotation == 0)
			return x;
		if(rotation == 1)
			return y;
		if(rotation == 2) {
			int ret = 7 - x - (objectSizeX - 1);
			if(ret < 0)
				ret = 0;
			return ret;
		} else {
			int ret = 7 - y - (objectSizeY - 1);
			if(ret < 0)
				ret = 0;
			return ret;
		}
	}
	
	public static int getRotatedLandscapeChunkY(int y, int objectSizeY, int rotation, int objectSizeX, int x, int objectRot) {
		rotation &= 3;
		int tmp1 = objectSizeX;
		int tmp2 = objectSizeY;
		if(!(objectRot == 0 || objectRot == 2)) {
			objectSizeX = tmp2;
			objectSizeY = tmp1;
		}
		if(rotation == 0)
			return y;
		if(rotation == 1) {
			int ret = 7 - x - (objectSizeX - 1);
			if(ret < 0)
				ret = 0;
			return ret;
		}
		if(rotation == 2) {
			int ret = 7 - y - (objectSizeY - 1);
			if(ret < 0)
				ret = 0;
			return ret;
		} else
			return x;
	}
	
	public static int getGuardId(int objectId) {
		switch(objectId) {
			case 13331:
				return 3580;
			case 13373:
				return 3594;
			
			case 13366:
				return 3581;
			case 13367:
				return 3582;
			case 13368:
				return 3583;
			case 13372:
				return 3588;
			case 13370:
				return 3585;
			case 13369:
				return 3584;
			case 2715:
				return 3586;
			
			case 39260:
				return 11562;
			case 39261:
				return 11563;
			case 39262:
				return 11564;
			case 39263:
				return 11565;
			case 39264:
				return 11566;
			case 39265:
				return 11567;
			
			case 13378:
				return 3593;
			case 13374:
				return 3589;
			case 13377:
				return 3592;
			case 13376:
				return 3591;
			case 13375:
				return 3590;
		}
		return -1;
	}
}
