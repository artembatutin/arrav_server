package net.arrav.content.item.pets;

import com.jsoniter.annotation.JsonProperty;

public final class PetProgress {
	
	@JsonProperty(value = "data")
	private final PetData data;
	
	@JsonProperty(value = "hunger")
	private double hunger;
	
	@JsonProperty(value = "growth")
	private double growth;
	
	public PetProgress() {
		data = null;
	}
	
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
