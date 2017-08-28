package net.edge.content.item.pets;

import net.edge.content.item.FoodConsumable;

public enum PetFoodType {
	MEAT(FoodConsumable.COOKED_CHICKEN, FoodConsumable.COOKED_MEAT, FoodConsumable.COOKED_RABBIT, FoodConsumable.MINCED_MEAT, FoodConsumable.ROAST_BIRD_MEAT, FoodConsumable.UGTHANKI_MEAT),
	FISH(FoodConsumable.ANCHOVIES, FoodConsumable.COD, FoodConsumable.CRAYFISH, FoodConsumable.EEL_SUSHI, FoodConsumable.HERRING, FoodConsumable.LOBSTER, FoodConsumable.MACKREL, FoodConsumable.MANTA_RAY, FoodConsumable.MONKFISH, FoodConsumable.PIKE, FoodConsumable.ROCKTAIL, FoodConsumable.SALMON, FoodConsumable.SARDINE, FoodConsumable.SEA_TURTLE, FoodConsumable.SHARK, FoodConsumable.SHRIMP, FoodConsumable.SWORDFISH, FoodConsumable.TROUT, FoodConsumable.TUNA);

	private final FoodConsumable[] consumables;

	PetFoodType(FoodConsumable... consumables) {
		this.consumables = consumables;
	}

	public boolean possible(FoodConsumable consumable) {
		for(FoodConsumable possible : consumables) {
			if(consumable.equals(possible))
				return true;
		}
		return false;
	}

}
