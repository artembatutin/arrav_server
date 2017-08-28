package net.edge.content.refillable;

public enum Refillable {

	WATER_VIAL(229, 227, new int[]{879, 873, 11661, 28662, 36781}),
	WATER_BUCKET(1925, 1929, new int[]{879, 873, 28662, 36781});

	private int needed;

	private int produced;

	private int[] objects;

	Refillable(int needed, int produced, int[] objects) {
		this.needed = needed;
		this.produced = produced;
		this.objects = objects;
	}

	public int getNeeded() {
		return needed;
	}

	public int getProduced() {
		return produced;
	}

	public int[] getObjects() {
		return objects;
	}

}
