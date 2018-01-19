package net.arrav.content.item.pets;

public final class PetProgress {
	
	private final PetData data;
	
	private double hunger;
	
	private double growth;
	
	public PetProgress(PetData data) {
		this.data = data;
	}
	
	public double getHunger() {
		return hunger;
	}
	
	public void setHunger(double hunger) {
		this.hunger = hunger;
	}
	
	public void updateHunger() {
		this.hunger += this.data.getStage() == 0 ? 2.5 : 4.18;
		if(this.hunger < 0.0) {
			this.hunger = 0.0;
		} else if(growth > 100.0) {
			this.hunger = 100.0;
		}
	}
	
	public double getGrowth() {
		return growth;
	}
	
	public void updateGrowth() {
		this.growth += this.data.getType().getGrowthRate();
		if(this.growth < 0.0) {
			this.growth = 0.0;
		} else if(growth > 100.0) {
			this.growth = 100.0;
		}
	}
	
	public PetData getData() {
		return data;
	}
	
}
