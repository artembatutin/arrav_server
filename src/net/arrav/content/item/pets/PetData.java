package net.arrav.content.item.pets;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The enumerated type whose elements represent a set of constants used to
 * define all the possible pets
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum PetData {
	BLACK_AND_GREY_OVERGROWN_CAT(2, new PetPolicy(1567, 774, null), PetType.CAT),
	BLACK_AND_GREY_CAT(1, new PetPolicy(1561, 768, BLACK_AND_GREY_OVERGROWN_CAT), PetType.CAT),
	BLACK_AND_GREY_KITTEN(0, new PetPolicy(1555, 761, BLACK_AND_GREY_CAT), PetType.CAT),
	
	//collectors
	IMP(0, new PetPolicy(9952, 1531, null), PetType.IMP),
	
	//bosses
	ABYSSAL_ORPHAN(0, new PetPolicy(3904, 3167, null), PetType.ABYSSAL_ORPHAN),
	JAD_JADIKU(0, new PetPolicy(3906, 3168, null), PetType.JAD_JADIKU),
	TORAM(0, new PetPolicy(3908, 3169, null), PetType.TORAM),
	WYRMY(0, new PetPolicy(3910, 3170, null), PetType.WYRMY),
	KRAA(0, new PetPolicy(3912, 3177, null), PetType.KRAA),
	GRARY(0, new PetPolicy(3914, 3178, null), PetType.GRARY),
	ZILZY(0, new PetPolicy(3918, 3180, null), PetType.ZILZY),;
	
	/**
	 * Caches our enum values.
	 */
	public static final ImmutableSet<PetData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(PetData.class));
	
	/**
	 * The stage of this pet.
	 */
	private final int stage;
	
	/**
	 * The policy for this pet.
	 */
	private final PetPolicy petPolicy;
	
	/**
	 * The type for this pet.
	 */
	private final PetType type;
	
	/**
	 * Constructs a new {@link PetData}.
	 * @param stage {@link #stage}.
	 * @param petPolicy {@link #petPolicy}.
	 * @param type {@link #type}.
	 */
	PetData(int stage, PetPolicy petPolicy, PetType type) {
		this.stage = stage;
		this.petPolicy = petPolicy;
		this.type = type;
	}
	
	public int getStage() {
		return stage;
	}
	
	public PetPolicy getPolicy() {
		return petPolicy;
	}
	
	public PetType getType() {
		return type;
	}
	
	protected static Optional<PetData> getItem(int itemId) {
		return VALUES.stream().filter(t -> t.petPolicy.getItem().getId() == itemId).findAny();
	}
	
	public static Optional<PetData> getNpc(int npcId) {
		return VALUES.stream().filter(t -> t.petPolicy.getNpcId() == npcId).findAny();
	}
}
