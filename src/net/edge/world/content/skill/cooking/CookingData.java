package net.edge.world.content.skill.cooking;

import com.google.common.collect.ImmutableSet;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemDefinition;

import static net.edge.world.content.skill.cooking.Cooking.BURNT_PIE;

public enum CookingData {
	BREAD(2307, 1, 2309, 12, 2311, 40),
	PITTA_BREAD(1863, 58, 1865, 69, 1867, 40),
	PLAIN_PIZZA(2287, 35, 2289, 68, 2305, 143),
	
	REDBERRY_PIE(2321, 10, 2325, 21, BURNT_PIE.getId(), 78),
	MEAT_PIE(2319, 20, 2327, 31, BURNT_PIE.getId(), 110),
	MUD_PIE(7168, 29, 7170, 40, BURNT_PIE.getId(), 128),
	APPLE_PIE(2317, 30, 2323, 41, BURNT_PIE.getId(), 130),
	GARDEN_PIE(7176, 34, 7178, 45, BURNT_PIE.getId(), 138),
	FISH_PIE(7186, 47, 7188, 58, BURNT_PIE.getId(), 164),
	ADMIRAL_PIE(7196, 70, 7198, 81, BURNT_PIE.getId(), 210),
	WILD_PIE(7206, 85, 7208, 96, BURNT_PIE.getId(), 240),
	SUMMER_PIE(7216, 95, 7218, 99, BURNT_PIE.getId(), 260),
	
	CHICKEN(2138, 1, 2140, 31, 2144, 30),
	SHRIMP(317, 1, 315, 34, 323, 30),
	SARDINE(327, 1, 325, 38, 369, 40),
	ANCHOVIES(321, 1, 319, 34, 323, 30),
	HERRING(345, 5, 347, 37, 357, 50),
	MACKEREL(353, 10, 355, 45, 357, 60),
	TROUT(335, 15, 333, 50, 343, 70),
	COD(341, 18, 339, 39, 343, 75),
	PIKE(349, 20, 351, 52, 343, 80),
	SALMON(331, 25, 329, 58, 343, 90),
	SLIMY_EEL(3379, 28, 3381, 56, 3383, 95),
	TUNA(359, 30, 361, 63, 367, 100),
	CAVE_EEL(5001, 38, 5003, 72, 5002, 115),
	LOBSTER(377, 40, 379, 74, 381, 120),
	BASS(363, 43, 365, 80, 367, 130),
	SWORDFISH(371, 45, 373, 86, 375, 140),
	LAVA_EEL(2148, 53, 2149, 89, 3383, 140),
	MONKFISH(7944, 62, 7946, 92, 7948, 150),
	SHARK(383, 80, 385, 94, 387, 210),
	MANTA_RAY(389, 91, 391, 100, 393, 216.3),
	ROCKTAIL(15270, 93, 15272, 100, 15274, 225);
	
	static final ImmutableSet<CookingData> VALUES = ImmutableSet.copyOf(values());
	private final int rawId;
	private final int level;
	private final int cookedId;
	private final int masterLevel;
	private final int burntId;
	private final double experience;
	
	private CookingData(int rawId, int level, int cookedId, int masterLevel, int burntId, double experience) {
		this.rawId = rawId;
		this.level = level;
		this.cookedId = cookedId;
		this.masterLevel = masterLevel;
		this.burntId = burntId;
		this.experience = experience;
	}
	
	public static ImmutableSet<CookingData> getValues() {
		return VALUES;
	}
	
	public int getRawId() {
		return rawId;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getCookedId() {
		return cookedId;
	}
	
	public int getMasterLevel() {
		return masterLevel;
	}
	
	public int getBurntId() {
		return burntId;
	}
	
	public double getExperience() {
		return experience;
	}
	
	@Override
	public final String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}
	
	public void openInterface(Player player) {
		player.getMessages().sendChatInterface(1743);
		player.getMessages().sendItemModelOnInterface(13716, 190, rawId);
		player.getMessages().sendString("\\n\\n\\n\\n\\n" + ItemDefinition.DEFINITIONS[rawId].getName(), 13717);
	}
	
	public static CookingData forItem(Item item) {
		return VALUES.stream().filter(fish -> fish.rawId == item.getId()).findFirst().orElse(null);
	}
}