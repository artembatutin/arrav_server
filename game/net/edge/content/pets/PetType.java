package net.edge.content.pets;

public enum PetType {
	CAT(0.0154320987654321, PetFoodType.FISH),
	DOG(0.0033333333333333, PetFoodType.MEAT),
	BIRD(0.00698888, PetFoodType.FISH),
	DRAGON(0.0052, PetFoodType.FISH, PetFoodType.MEAT);
	
	/**
	 * The growth rate for this pet.
	 */
	private final double growthRate;
	
	/**
	 * The food types for this pet.
	 */
	private final PetFoodType[] consumables;
	
	/**
	 * Constructs a new {@link PetType}.
	 * @param growthRate  {@link #growthRate}.
	 * @param consumables {@link #consumables}.
	 */
	PetType(double growthRate, PetFoodType... consumables) {
		this.consumables = consumables;
		this.growthRate = growthRate;
	}
	
	public double getGrowthRate() {
		return growthRate;
	}
	
	public PetFoodType[] getConsumables() {
		return consumables;
	}
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
