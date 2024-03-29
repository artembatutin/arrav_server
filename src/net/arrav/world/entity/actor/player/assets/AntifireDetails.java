package net.arrav.world.entity.actor.player.assets;

import com.jsoniter.annotation.JsonProperty;
import net.arrav.util.MutableNumber;

public final class AntifireDetails {
	
	@JsonProperty(value = "delay")
	private final MutableNumber antifireDelay = new MutableNumber(600);
	
	@JsonProperty(value = "type")
	private final AntifireType type;
	
	public AntifireDetails() {
		type = AntifireType.REGULAR;
	}
	
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