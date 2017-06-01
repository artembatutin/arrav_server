package net.edge.world.content.pets;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.world.content.item.FoodConsumable;

import java.util.EnumSet;
import java.util.Optional;

public enum PetFoodType {
	MEAT(FoodConsumable.COOKED_CHICKEN, FoodConsumable.COOKED_MEAT, FoodConsumable.COOKED_RABBIT, FoodConsumable.MINCED_MEAT, FoodConsumable.ROAST_BIRD_MEAT, FoodConsumable.UGTHANKI_MEAT),
	FISH(FoodConsumable.ANCHOVIES, FoodConsumable.COD, FoodConsumable.CRAYFISH, FoodConsumable.EEL_SUSHI, FoodConsumable.HERRING, FoodConsumable.LOBSTER, FoodConsumable.MACKREL, FoodConsumable.MANTA_RAY, FoodConsumable.MONKFISH, FoodConsumable.PIKE, FoodConsumable.ROCKTAIL, FoodConsumable.SALMON, FoodConsumable.SARDINE, FoodConsumable.SEA_TURTLE, FoodConsumable.SHARK, FoodConsumable.SHRIMP, FoodConsumable.SWORDFISH, FoodConsumable.TROUT, FoodConsumable.TUNA);
	
	private static final ImmutableSet<PetFoodType> VALUES = Sets.immutableEnumSet(EnumSet.allOf(PetFoodType.class));
	
	private final FoodConsumable[] consumables;
	
	PetFoodType(FoodConsumable... consumables) {
		this.consumables = consumables;
	}
	
	static Optional<PetFoodType> getType(FoodConsumable food) {
		return VALUES.stream().filter(t -> {
			for(FoodConsumable consumable : t.consumables) {
				if(consumable == food) {
					return true;
				}
			}
			return false;
		}).findAny();
	}
}
