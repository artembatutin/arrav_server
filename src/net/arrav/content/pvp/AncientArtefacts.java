/**
 * 
 */
package net.arrav.content.pvp;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.arrav.util.rand.Chance;

import java.util.EnumSet;
import java.util.Optional;

/**
 * @author Ophion - Dave
 * https://www.rune-server.ee/members/ophion/
 */
public enum AncientArtefacts {
	HEADDRESS(14892, 5000, Chance.COMMON),
	THIRD_AGE_CARAFE(14891, 10000, Chance.COMMON),
	BRONZED_DRAGON_CLAW(14890, 20000, Chance.COMMON),
	ANCIENT_PSALTERY_BRIDGE(14889, 30000, Chance.COMMON),
	SARADOMIN_AMPHORA(14888, 40000, Chance.UNCOMMON),
	BANDOS_SCRIMSHAW(14887, 50000, Chance.UNCOMMON),
	SARADOMIN_CARVING(14886, 75000, Chance.UNCOMMON),
	ZAMORAK_MEDALLION(14885, 100000, Chance.UNCOMMON),
	ARMADYL_TOTEM(14884, 150000, Chance.UNCOMMON),
	GUTHIXIAN_BRAZIER(14883, 200000, Chance.UNCOMMON),
	RUBY_CHALICE(14882, 250000, Chance.UNCOMMON),
	BANDOS_STATUETTE(14881, 300000, Chance.UNCOMMON),
	SARADOMIN_STATUETTE(14880, 400000, Chance.VERY_UNCOMMON),
	ZAMORAK_STATUETTE(14879, 500000, Chance.VERY_UNCOMMON),
	ARMADYL_STATUETTE(14878, 750000, Chance.VERY_UNCOMMON),
	SEREN_STATUETTE(14877, 10000000, Chance.RARE),
	ANCIENT_STATUETTE(14876, 25000000, Chance.RARE);
	
	private Chance chance;
	private int id;
	private int price;

	private static final ImmutableSet<AncientArtefacts> VALUES = Sets.immutableEnumSet(EnumSet.allOf(AncientArtefacts.class));

	AncientArtefacts(int id, int price, Chance chance) {
		this.setId(id);
		this.setPrice(price);
		this.setChance(chance);
	}

	public Chance getChance() {
		return chance;
	}

	public void setChance(Chance chance) {
		this.chance = chance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public static Optional<AncientArtefacts> getItem(int itemId) {
		return VALUES.stream().filter(t -> t.getId() == itemId).findAny();
	}

}
