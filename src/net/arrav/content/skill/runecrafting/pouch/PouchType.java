package net.arrav.content.skill.runecrafting.pouch;

public enum PouchType {
	
	SMALL(3), MEDIUM(5), LARGE(7), GIANT(12);
	
	private int maxAmount;
	
	PouchType(int maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	public int getMaxAmount() {
		return maxAmount;
	}
}
