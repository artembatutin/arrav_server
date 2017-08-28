package net.edge.world.entity.actor.player.assets;

import net.edge.util.MutableNumber;

public final class AntifireDetails {

	private final MutableNumber antifireDelay = new MutableNumber(600);

	private final AntifireType type;

	public AntifireDetails(AntifireType type) {
		this.type = type;
	}

	public MutableNumber getAntifireDelay() {
		return antifireDelay;
	}

	public AntifireType getType() {
		return type;
	}

	public enum AntifireType {
		REGULAR(450), SUPER(900);

		final int reduction;

		AntifireType(int reduction) {
			this.reduction = reduction;
		}

		public int getReduction() {
			return reduction;
		}
	}
}