package net.edge.world.content.skill.summoning;

import net.edge.world.node.item.Item;

/*
- SIDE DATA

	SPIRIT_WOLF("Spirit wolf pouch", "Gold Charm", 7, "Wolf bones", "Howl scroll", 1, 5, 6829),
	SPIRIT_DREADFOWL("Dreadfowl pouch", "Gold Charm", 8, "Raw chicken", "Dreadfowl strike scroll", 8, 9, 6825),
	SPIRIT_SPIDER("Spirit spider pouch", "Gold Charm", 8, "Spider carcass", "Egg spawn scroll", 8, 10, 6841),
	THORNY_SNAIL("Thorny Snail pouch", "Gold Charm", 9, "Thin snail","Slime spray scroll", 13, 12, 6806),
	GRANITE_CRAB("Granite Crab pouch", "Gold Charm", 7, "Iron ore", "Stony shell scroll", 16, 22, 6796),
	SPRITI_MOSQUITO("Mosquito pouch", "Gold Charm", 1, "Proboscis", "Pester scroll", 17, 47, 7331),
	DESERT_WYRM("Desert wyrm pouch", "Green Charm", 45, "Bucket of sand", "Electric lash scroll", 18, 31, 6831),
	SPIRIT_SCORPION("Spirit Scorpion pouch", "Crimson Charm", 57, "Bronze claws", "Venom shot scroll", 19, 83, 6837),
	SPIRIT_TZ_KIH("Spirit tz-kih pouch", "Crimson charm", 64, "Obsidian charm", "Fireball assault scroll", 22, 97, 7361),
	ALBINO_RAT("Albino rat pouch", "Blue Charm", 75, "Raw rat meat", "Cheese feast scroll", 23, 202, 6847),
	SPIRIT_KALPHITE("Spirit kalphite pouch", "blue Charm", 51 , "Potato cactus", "Sandstorm scroll", 25, 220, 6994),
	COMPOST_MOUND("Compost mound pouch", "Green charm", 47, "Compost", "Generate compost scroll", 28, 50, 6871),
	GIANT_CHINCHOMPA("Giant chinchompa pouch", "Blue Charm", 84, "Chinchompa", "Explode scroll", 29, 255, 7353),
	VAMPIRE_BAT("Vampire bat pouch", "Crimson Charm", 81, "Vampire dust", "Vampire touch scroll", 31, 136, 6835),
	HONEY_BADGER("Honey badger pouch", "Crimson Charm", 84, "Honeycomb", "Insane ferocity scroll", 32, 141, 6845),
	BEAVER("Beaver pouch", "Green Charm", 72, "Willow logs", "Multichop scroll", 33, 58, 6807),//need npc id
	VOID_RAVAGER("Void ravager pouch", "Green Charm", 74, "Ravager Charm", "Call to arms scroll", 34, 60, 7370),
	VOID_SHIFTER("Void shifter pouch", "Blue charm", 74, "Shifter charm", "Call to arms scroll", 34, 60, 7367),
	VOID_SPINNER("void spinner pouch", "Blue Charm", 74, "Spinner Charm", "Call to arms scroll", 34, 60, 7333),
	VOID_TORCHER("Void Torcher pouch", "Blue Charm", 74, "Torcher Charm", "Call to arms scroll", 34, 60, 7351),
	BRONZE_MINOTAUR("Bronze minotaur pouch", "Blue Charm", 102, "Bronze bar", "Bronze bull rush scroll", 36, 317, 6853),
	BULL_ANT("Bull ant pouch", "Gold Charm", 11, "Marigolds", "Unburden scroll", 40, 53, 6867),
	MACAW("Macaw pouch", "Green Charm", 78, "Clean guam", "Herbcall scroll", 41, 72, 6851),
	EVIL_TURNIP("Evil turnip pouch", "Crimson Charm", 104, "Carved turnip", "Evil flames scroll", 42, 185, 6833),
	IRON_MINOTAUR("Iron minotaur pouch", "Blue Charm", 125, "Iron bar", "Iron bull rush scroll", 46, 405, 6855),
	PYRELORD("Pyrelord pouch", "Crimson Charm", 111, "Tinderbox", "Immense heat scroll", 46, 202, 7377),
	MAGPIE("Magpie pouch", "Green Charm", 88, "Gold ring", "Thieving fingers scroll", 47, 83, 6824),
	BLOATED_LEECH("Bloated leech pouch", "Crimson Charm", 117, "Raw beef", "Blood drain scroll", 49, 215, 6843),
	SPIRIT_TERRORBIRD("Spirit terrorbird pouch", "Gold Charm", 12, "Raw bird meat", "Tireless run scroll", 52, 68, 3596),// need correct npc
	ABYSSAL_PARASITE("Abyssal parasite pouch", "Green Charm", 106, "Abyssal charm", "Abyssal drain scroll", 54, 95, 6818),
	SPIRIT_JELLY("Spirit jelly pouch", "Blue Charm", 151, "Jug of water", "Dissolve scroll", 55, 484, 6922),//need right npc
	STEEL_MINOTAUR("Steel minotaur pouch", "Blue Charm", 141, "Steel bar", "Steel bull rush scroll", 56, 493, 6857),
	IBIS("Ibis pouch", "Green Charm", 109, "Harpoon", "Fish rain scroll", 56, 99, 6951), // need the correct npc id
	SPIRIT_GRAAHK("Spirit Graahk pouch", "Blue Charm", 154, "Graahk fur", "Ambush scroll", 57, 502, 3588),// need correct npc id
	SPIRIT_KYATT("Spirit Kyatt pouch", "Blue Charm", 153, "Kyatt fur", "Rending scroll", 57, 502, 7365),
	SPIRIT_LARUPIA("Spirit larupia pouch", "Blue Charm", 155, "Larupia fur", "Goad scroƒll", 57, 502, 7337),
	KHARAMTHULHU("Karamthulhu overlord pouch", "Blue Charm", 144, "Empty fishbowl", "Doomsphere scroll", 58, 510, 6809),
	SMOKE_DEVIL("Smoke devil pouch", "Crimson Charm", 141, "Goat horn dust", "Dust cloud scroll", 61, 268, 6865),
	ABYSSAL_LURKER("Abyssal lurker", "Green Charm", 119, "Abyssal charm", "Abyssal stealth scroll", 62, 110, 6820),
	SPIRIT_COBRA("Spirit cobra pouch", "Crimson Charm", 116, "Snake hide", "Ophidian incubation scroll", 63, 269, 6802),
	STRANGER_PLANT("Stranger plant pouch", "Crimson Charm", 128, "Bagged plant", "Poisonous blast scroll", 64, 282, 6827),
	MITHRIL_MINOTAUR("Mithril minotaur pouch", "Blue Charm", 152, "Mithril bar", "Mithril bull rush scroll", 66, 581, 6859),
	BARKER_TOAD("Barker toad pouch", "Gold Charm", 11, "Swamp toad", "Toad bark scroll", 66, 87, 6889),// needs id 6889 = rev demon
	WAR_TORTOISE("War tortoise pouch", "Gold Charm", 1, "Tortoise shell", "Testudo scroll", 67, 59, 6815),
	BUNYIP("Bunyip pouch", "Green Charm", 110, "Raw shark", "Swallow whole scroll", 68, 120, 6813),
	FRUIT_BAT("Fruit bat pouch", "Green Charm", 130, "Banana", "Fruitfall scroll", 69, 121, 6817),
	RAVENOUS_LOCUST("Ravenous Locust pouch", "Crimson Charm", 79, "Pot of Flour", "Famine scroll", 70, 132, 7372),
	ARCTIC_BEAR("Arctic bear pouch", "Gold Charm", 14, "Polar kebbit fur", "Arctic blast scroll", 71, 93, 6839),
	PHEONIX("Phoenix pouch", "Crimson Charm", 165, "Phoenix Quill", "Rise from the ashes scroll", 72, 301, 10), //need correct npc id
	OBSIDIAN_GOLEM("Obsidian Golem pouch", "Blue Charm", 195, "Obsidian Charm", "Volcanic strength scroll", 73, 642, 7345),
	GRANITE_LOBSTER("Granite lobster pouch", "Crimson Charm", 166, "Granite (500g)", "Crushing claw scroll", 74, 326, 6849),
	PRAYING_MANTIS("Praying mantis pouch", "Crimson Charm", 168, "Flowers", "Mantis strike scroll", 75, 330,6798),
	ADAMANT_MINOTAUR("Adamant minotaur pouch", "Blue Charm", 144, "Adamant Bar", "Adamant bull rush scroll", 76, 669, 6861),
	FORGE_REGENT("Forge Regent pouch", "Green Charm", 141, "Ruby harvest", "Inferno scroll", 76, 134, 7335),
	TALON_BEAST("Talon Beast pouch", "Crimson Charm", 174, "Talon Beast charm", "Deadly claw scroll", 77, 1015, 7347),
	GIANT_ENT("Giant ent pouch", "Green Charm", 124, "Willow branch", "Acorn missile scroll", 78, 137, 6800),
	FIRE_TITAN("Fire titan pouch", "Blue Charm", 198, "Fire talisman", "Titan's constitution scroll", 79, 695, 7355),
	ICE_TITAN("Ice titan pouch", "Blue Charm", 198, "Water talisman", "Titan's constitution scroll", 79, 695, 7359),
	MOSS_TITAN("Moss titan pouch", "Blue Charm", 202, "Earth talisman", "Titan's constitution scroll", 79, 695, 7357),
	HYDRA("Hydra pouch", "Green Charm", 128, "Water orb", "Regrowth scroll", 80, 141, 6811),
	SPIRIT_DAGGANOTH("Spirit dagannoth", "Crimson Charm", 1, "Dagannoth hide", "Spike shot scroll", 83, 365, 6804),
	LAVA_TITAN("Lava titan pouch", "Blue Charm", 219, "Obsidian Charm", "Ebon thunder scroll", 83, 730, 7341),
	SWAMP_TITAN("Swamp titan pouch", "Crimson Charm", 150, "Swamp lizard", "Swamp plague scroll", 85, 374, 7329),
	RUNE_MINOTAUR("Rune minotaur pouch", "Blue Charm", 1, "Rune bar", "Rune bull rush scroll", 86, 757, 6863),
	UNICORN_STALLION("Unicorn stallion pouch", "Green Charm", 140, "Unicorn Horn", "Healing aura scroll", 88, 154, 3592),//need npc id
	GEYSER_TITAN("Geyser titan pouch", "Blue Charm", 222, "Water talisman", "Boil scroll", 89, 783, 7339),
	WOLFTINGER("Wolpertinger pouch", "Crimson Charm", 203, "Raw rabbit", "Magic focus scroll", 92, 405, 3593),//need npc id
	ABYSSAL_TITAN("Abyssal titan pouch", "Green Charm", 113, "Abyssal charm", "Essence shipment scroll", 93, 163, 7349),
	IRON_TITAN("Iron titan pouch", "Crimson Charm", 198, "Iron platebody", "Iron within scroll", 95, 418, 7375),
	PACK_YAK("Pack yak pouch", "Crimson Charm", 211, "Yak hide", "Winter storage scroll", 96, 422, 6873),
	STEEL_TITAN("Steel titan pouch", "Blue Charm", 178, "Steel platebody", "Steel of legends scroll", 99, 435, 3591)//need npc id

 */

/**
 * An enumeration of possible charm types.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum Charm {
	
	GOLD(new Item(12158)),
	GREEN(new Item(12159)),
	CRIMSON(new Item(12160)),
	BLUE(new Item(12163));
	
	private final Item item;
	
	/**
	 * Creating the charm.
	 * @param item charm item.
	 */
	Charm(Item item) {
		this.item = item;
	}
	
	/**
	 * Getting the charm's item.
	 * @return charm item.
	 */
	public Item getItem() {
		return item;
	}
}
