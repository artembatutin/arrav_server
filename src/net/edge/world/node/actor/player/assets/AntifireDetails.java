package net.edge.world.node.actor.player.assets;

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
		REGULAR,
		SUPER
	}
}