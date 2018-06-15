package net.arrav.content.skill.mining;

import net.arrav.content.skill.action.TransformableObject;
import net.arrav.world.entity.item.Item;

import static net.arrav.content.skill.mining.RockType.*;

/**
 * The enumerated type which elements represents the data required for the
 * process of mining rocks.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
enum RockData {
	CLAY(new TransformableObject[]{new TransformableObject(2108, 450), new TransformableObject(2109, 451), big(47769, TYPE1), small(47770, TYPE1), big(44111, TYPE2), crushed(44112, TYPE2), big(53192, TYPE7), small(53193, TYPE7), crushed(53194, TYPE7), big(52580, TYPE8), small(52581, TYPE8), crushed(52582, TYPE8), big(74432, TYPE9), small(74433, TYPE9), crushed(74434, TYPE9), big(57506, TYPE10), small(57507, TYPE10), crushed(57508, TYPE10), big(52951, TYPE11), small(52952, TYPE11), big(56907, TYPE12), small(56908, TYPE12), big(51714, TYPE13), crushed(51716, TYPE13)}, 434, 1, 5.0, 2, 1, 5, 0.3),
	PURE_ESSENCE(new TransformableObject[]{new TransformableObject(44494, 2491)}, 7936, 1, 5.0, 2, 1, 500, 0.4),
	ESSENCE(new TransformableObject[]{new TransformableObject(44494, 2491)}, 1436, 1, 5.0, 2, 1, 500, 0.4),
	TIN(new TransformableObject[]{new TransformableObject(2095, 450), big(47779, TYPE1), small(47780, TYPE1), crushed(47781, TYPE1), big(44097, TYPE2), crushed(44098, TYPE2), big(63296, TYPE2), small(63297, TYPE2), crushed(63298, TYPE2), big(44707, TYPE3), big(60997, TYPE5), small(60998, TYPE5), crushed(60999, TYPE5), big(71230, TYPE6), crushed(71232, TYPE6), big(60955, TYPE7), small(60956, TYPE7), crushed(60957, TYPE7), big(53936, TYPE8), small(53937, TYPE8), crushed(53948, TYPE8), big(45046, TYPE9), big(53960, TYPE10), small(53961, TYPE10), crushed(53962, TYPE10), big(56905, TYPE12), small(56906, TYPE12), big(51717, TYPE13), crushed(51719, TYPE13), big(61027, TYPE15), small(61028, TYPE15), crushed(61029, TYPE15), big(79641, TYPE19), big(79645, TYPE19), small(79646, TYPE19), crushed(79647, TYPE19)}, 438, 1, 17.5, 5, 1, 1, 0.5),
	COPPER(new TransformableObject[]{new TransformableObject(2091, 450), big(47782, TYPE1), small(47783, TYPE1), crushed(47784, TYPE1), big(44093, TYPE2), crushed(44094, TYPE2), big(63287, TYPE2), small(63288, TYPE2), crushed(63289, TYPE2), big(60994, TYPE5), small(60995, TYPE5), crushed(60996, TYPE5), big(71233, TYPE6), small(71234, TYPE6), big(53939, TYPE8), small(53940, TYPE8), crushed(53941, TYPE8), crushed(45045, TYPE9), big(53963, TYPE10), small(53964, TYPE10), crushed(53965, TYPE10), big(56909, TYPE12), small(56910, TYPE12), big(51711, TYPE13), small(51712, TYPE13), crushed(51713, TYPE13), big(79640, TYPE19), big(79648, TYPE19), small(79649, TYPE19), crushed(79650, TYPE19)}, 436, 1, 15.5, 2, 1, 1, 0.5),
	BLURITE(new TransformableObject[]{big(44564, TYPE18), crushed(75224, TYPE18)}, 668, 10, 17.5, 16, 1, 1, 0.3),
	IRON(new TransformableObject[]{new TransformableObject(2092, 450), new TransformableObject(2093, 451), big(47776, TYPE1), small(47777, TYPE1), crushed(47778, TYPE1), big(44095, TYPE2), crushed(44096, TYPE2), big(63284, TYPE2), small(63285, TYPE2), crushed(63286, TYPE2), big(56859, TYPE3), small(56860, TYPE3), crushed(56861, TYPE3), big(61003, TYPE5), small(61004, TYPE5), crushed(61005, TYPE5), big(71224, TYPE6), small(71225, TYPE6), crushed(71226, TYPE6), big(79310, TYPE8), small(79311, TYPE8), crushed(79312, TYPE8), big(54567, TYPE9), small(54568, TYPE9), crushed(54569, TYPE9), big(74444, TYPE9), small(74445, TYPE9), crushed(74446, TYPE9), big(53957, TYPE10), small(53958, TYPE10), crushed(53959, TYPE10), big(56916, TYPE12), small(56917, TYPE12), big(51720, TYPE13), small(51721, TYPE13), crushed(51722, TYPE13), big(74454, TYPE20), small(74455, TYPE20)}, 440, 15, 35.0, 17, 3, 3, 0.2),
	SILVER(new TransformableObject[]{new TransformableObject(2100, 450), new TransformableObject(2101, 451), big(44103, TYPE2), crushed(44104, TYPE2), big(57582, TYPE4), small(57583, TYPE4), crushed(57584, TYPE4), big(71227, TYPE6), small(71228, TYPE6), crushed(71229, TYPE6), big(79307, TYPE8), small(79308, TYPE8), crushed(79309, TYPE8), big(79673, TYPE8), big(74447, TYPE9), small(74448, TYPE9), crushed(74449, TYPE9), big(53951, TYPE10), small(53952, TYPE10), crushed(53953, TYPE10), big(59001, TYPE12), small(59002, TYPE12), crushed(59003, TYPE12)}, 442, 20, 40.0, 160, 3, 3, 0.2),
	COAL(new TransformableObject[]{new TransformableObject(2097, 450), new TransformableObject(2097, 451), big(47773, TYPE1), small(47774, TYPE1), crushed(47775, TYPE1), big(44099, TYPE2), crushed(44100, TYPE2), big(63290, TYPE2), small(63291, TYPE2), crushed(63292, TYPE2), small(56854, TYPE3), big(56853, TYPE3), crushed(56855, TYPE3), big(73170, TYPE4), small(73171, TYPE4), crushed(73172, TYPE4), big(61000, TYPE5), small(61001, TYPE5), crushed(61002, TYPE5), big(71218, TYPE6), small(71219, TYPE6), crushed(71220, TYPE6), big(53189, TYPE7), small(53190, TYPE7), crushed(53191, TYPE7), big(53933, TYPE8), small(53934, TYPE8), crushed(53935, TYPE8), big(74429, TYPE9), small(74430, TYPE9), crushed(74431, TYPE9), big(53966, TYPE10), crushed(53967, TYPE10), big(57249, TYPE14), small(57250, TYPE14), crushed(57251, TYPE14), big(74452, TYPE20), small(74453, TYPE20)}, 453, 30, 50.0, 12, 3, 4, 0.2),
	GOLD(new TransformableObject[]{new TransformableObject(2098, 450), new TransformableObject(2099, 451), big(47771, TYPE1), small(47772, TYPE1), big(44101, TYPE2), crushed(44102, TYPE2), big(44612, TYPE3), small(44613, TYPE3), crushed(44614, TYPE3), big(57579, TYPE4), small(57580, TYPE4), crushed(57581, TYPE4), big(52577, TYPE5), small(52578, TYPE5), crushed(52579, TYPE5), big(87070, TYPE6), small(87071, TYPE6), big(53186, TYPE7), small(53187, TYPE7), crushed(53188, TYPE7), big(79313, TYPE8), crushed(79315, TYPE8), big(74435, TYPE9), small(74436, TYPE9), crushed(74437, TYPE9), big(53954, TYPE10), small(53955, TYPE10), crushed(53956, TYPE10), big(59004, TYPE12), small(59005, TYPE12), crushed(59006, TYPE12), big(51723, TYPE13), crushed(51725, TYPE13)}, 444, 40, 65.0, 180, 3, 3, 0.1),
	MITHRIL(new TransformableObject[]{new TransformableObject(2102, 450), new TransformableObject(2103, 451), big(47787, TYPE1), small(47788, TYPE1), crushed(47789, TYPE1), big(44105, TYPE2), crushed(44106, TYPE2), big(63281, TYPE2), small(63282, TYPE2), crushed(63283, TYPE2), big(56856, TYPE3), small(56857, TYPE3), crushed(56858, TYPE3), big(73173, TYPE4), big(71239, TYPE6), big(53945, TYPE8), small(53946, TYPE8), crushed(535947, TYPE8), big(74441, TYPE9), small(74442, TYPE9), crushed(74443, TYPE9), small(53949, TYPE10), crushed(53950, TYPE10), big(61015, TYPE15), small(61016, TYPE15), crushed(61017, TYPE15), big(67371, TYPE16), small(67372, TYPE16), crushed(67373, TYPE16)}, 447, 55, 80.0, 300, 6, 4, 0.05),
	ADAMANTITE(new TransformableObject[]{new TransformableObject(2105, 11552), new TransformableObject(2104, 450), new TransformableObject(2105, 451), big(47785, TYPE1), crushed(47786, TYPE1), big(44107, TYPE2), crushed(44108, TYPE2), big(63278, TYPE2), small(63279, TYPE2), crushed(63280, TYPE2), big(56865, TYPE3), small(56866, TYPE3), crushed(56867, TYPE3), big(73176, TYPE4), small(73177, TYPE4), big(71236, TYPE6), crushed(71238, TYPE6), big(53942, TYPE8), crushed(53944, TYPE8), big(74438, TYPE9), small(74439, TYPE9), crushed(74440, TYPE9), big(61021, TYPE15), small(61022, TYPE15), crushed(61023, TYPE15)}, 449, 70, 95.0, 600, 8, 8, 0.03),
	RUNITE(new TransformableObject[]{new TransformableObject(2106, 450), new TransformableObject(2107, 451), big(56862, TYPE3), small(56863, TYPE3), big(87072, TYPE6), small(87073, TYPE6), big(79211, TYPE7), big(75081, TYPE9), small(75082, TYPE9)}, 451, 85, 125.0, 900, 12, 16, 0.01);
	
	/**
	 * Creating a new styled big rock depending on it's type.
	 */
	public static TransformableObject big(int rock, RockType type) {
		return new TransformableObject(rock, type.getBig());
	}
	
	/**
	 * Creating a new styled crushed rock depending on it's type.
	 */
	public static TransformableObject crushed(int rock, RockType type) {
		return new TransformableObject(rock, type.getCrushed());
	}
	
	/**
	 * Creating a new styled small rock depending on it's type.
	 */
	public static TransformableObject small(int rock, RockType type) {
		return new TransformableObject(rock, type.getSmall());
	}
	
	/**
	 * The regular and empty rock identification for this rock.
	 */
	private final TransformableObject[] object;
	
	/**
	 * The item this rock produces.
	 */
	private final Item item;
	
	/**
	 * The requirement required to mine this rock.
	 */
	private final int requirement;
	
	/**
	 * The experience gained for mining this rock.
	 */
	private final double experience;
	
	/**
	 * The respawn identification for this rock.
	 */
	private final int respawnTime;
	
	/**
	 * The prospect delay when prospecting for this rock.
	 */
	private final int prospectdelay;
	
	/**
	 * The amount of ores this rock can produce.
	 */
	private final int oreCount;
	
	/**
	 * The success rate for mining this rock.
	 */
	private final double success;
	
	/**
	 * Constructs a new {@link RockData}.
	 * @param object {@link #object}.
	 * @param item {@link #item}.
	 * @param requirement {@link #requirement}.
	 * @param experience {@link #experience}.
	 * @param respawnTime {@link #respawnTime}.
	 * @param prospectdelay {@link #prospectdelay}.
	 * @param oreCount {@link #oreCount}.
	 * @param success {@link #success}.
	 */
	RockData(TransformableObject[] object, int item, int requirement, double experience, int respawnTime, int prospectdelay, int oreCount, double success) {
		this.object = object;
		this.item = new Item(item);
		this.requirement = requirement;
		this.experience = experience * 1.2;//increased a bit.
		this.respawnTime = respawnTime;
		this.prospectdelay = prospectdelay;
		this.oreCount = oreCount;
		this.success = success;
	}
	
	/**
	 * @return {@link #object}.
	 */
	public TransformableObject[] getObject() {
		return object;
	}
	
	/**
	 * @return {@link #item}.
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * @return {@link #requirement}.
	 */
	public int getRequirement() {
		return requirement;
	}
	
	/**
	 * @return {@link #experience}.
	 */
	public double getExperience() {
		return experience;
	}
	
	/**
	 * @return {@link #respawnTime}.
	 */
	public int getRespawnTime() {
		return respawnTime;
	}
	
	/**
	 * @return {@link #prospectdelay}.
	 */
	public int prospectDelay() {
		return prospectdelay;
	}
	
	/**
	 * @return {@link #oreCount}.
	 */
	public int getOreCount() {
		return oreCount;
	}
	
	/**
	 * @return {@link #success}.
	 */
	public double getSuccess() {
		return success;
	}
	
	@Override
	public final String toString() {
		return name().toLowerCase();
	}
	
}
