package net.arrav.content.item.pets;

import net.arrav.content.item.FoodConsumable;

public enum PetType {
	CAT(1.54320987654321, "Miew", PetFoodType.FISH),
	DOG(0.33333333333333, "Wouf", PetFoodType.MEAT),
	BIRD(0.698888, "Piw", PetFoodType.FISH),
	DRAGON(0.52, "Rawr", PetFoodType.FISH, PetFoodType.MEAT),
	
	//collectors
	IMP("Charm catcher at your service!"),
	
	//bosses
	ABYSSAL_ORPHAN("Pss"),
	JAD_JADIKU("Warg"),
	TORAM("Grrr"),
	WYRMY("Whim"),
	KRAA("Wsss"),
	GRARY("Babadum"),
	ZILZY("Leader"),
	;
	
	/**
	 * The growth rate for this pet.
	 */
	private final double growthRate;
	
	/**
	 * The food types for this pet.
	 */
	private final PetFoodType[] foods;
	
	/**
	 * Shouting message.
	 */
	private String shout;
	
	PetType(String shout) {
		this.growthRate = 0;
		this.shout = shout;
		this.foods = new PetFoodType[]{PetFoodType.FISH, PetFoodType.MEAT};
	}
	
	/**
	 * Constructs a new {@link PetType}.
	 * @param growthRate {@link #growthRate}.
	 * @param shout {@link #shout}.
	 * @param foods {@link #foods}.
	 */
	PetType(double growthRate, String shout, PetFoodType... foods) {
		this.foods = foods;
		this.shout = shout;
		this.growthRate = growthRate;
	}
	
	public double getGrowthRate() {
		return growthRate;
	}
	
	public String getShout() {
		return shout;
	}
	
	public PetFoodType[] getFood() {
		return foods;
	}
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
	
	public boolean eat(FoodConsumable eat) {
		for(PetFoodType food : foods) {
			if(food.possible(eat))
				return true;
		}
		return false;
	}
}
