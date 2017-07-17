package net.edge.world.node.actor.npc.impl.glacor;

import net.edge.locale.Position;
import net.edge.world.node.actor.npc.Npc;

/**
 * Represents a single Glacyte npc.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Glacyte extends Npc {
	
	/**
	 * The glacor that summoned this glacyte.
	 */
	private final Glacor glacor;
	
	/**
	 * The data this glacyte exists from.
	 */
	private final GlacyteData data;
	
	/**
	 * Constructs a new {@link Glacyte}.
	 */
	public Glacyte(GlacyteData data, Position position, Glacor glacor) {
		super(data.getNpcId(), position);
		this.glacor = glacor;
		this.data = data;
	}
	
	@Override
	public Npc create() {
		return new Glacyte(data, getPosition(), glacor);
	}
	
	@Override
	public void appendDeath() {
		super.appendDeath();
		
		glacor.getGlacytes().remove(data);
		
		if(glacor.getGlacytes().isEmpty()) {
			glacor.transform(data);
		}
	}
	
	public Glacor getGlacor() {
		return glacor;
	}
	
	public GlacyteData getGlacyteData() {
		return data;
	}
}