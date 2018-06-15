package net.arrav.content.skill.farming.attributes;

public enum GrowthState {

	GROWING(0x00), WATERED(0x01), DISEASED(0x02), DEAD(0x03);

	GrowthState(int shift) {
		this.shift = shift;
	}

	private final int shift;

	public int getShift() {
		return shift;
	}
}
