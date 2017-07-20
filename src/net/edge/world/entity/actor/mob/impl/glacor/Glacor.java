package net.edge.world.entity.actor.mob.impl.glacor;

import net.edge.world.locale.Position;
import net.edge.world.Hit;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.strategy.impl.glacor.GlacorCombatStrategy;
import net.edge.world.entity.actor.mob.strategy.impl.glacor.GlacyteCombatStrategy;
import net.edge.world.entity.actor.player.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The class which represents a single glacor npc.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Glacor extends Mob {
	
	/**
	 * The specialty of this glacor.
	 */
	private Optional<GlacyteData> specialty = Optional.empty();
	
	/**
	 * Constructs a new {@link Glacor}.
	 * @param position the position to spawn this glacor on.
	 */
	public Glacor(Position position) {
		super(14301, position);
		setStrategy(Optional.of(new GlacorCombatStrategy(this)));
	}
	
	/**
	 * Determines if this glacor has summoned his glacytes.
	 */
	private boolean summoned;
	
	/**
	 * The set of glacytes this glacor has summoned.
	 */
	private final Set<GlacyteData> glacytes = new HashSet<>();
	
	/**
	 * Determines if this glacor has summoned his glacytes.
	 * @return {@code true} if the glacor has, {@code false} otherwise.
	 */
	public boolean hasSummoned() {
		return summoned;
	}
	
	/**
	 * Sets the summoned field to true.
	 */
	public void setSummoned() {
		this.summoned = true;
	}
	
	/**
	 * Gets the set of glacytes.
	 * @return the set of glacytes.
	 */
	public Set<GlacyteData> getGlacytes() {
		return glacytes;
	}
	
	/**
	 * Transforms this glacors specialty.
	 * @param data the data to set the specialty from.
	 */
	public void transform(GlacyteData data) {
		this.specialty = Optional.of(data);
	}
	
	@Override
	public Mob create() {
		return new Glacor(getPosition());
	}
	
	@Override
	public void appendDeath() {
		super.appendDeath();
		
		if(specialty.isPresent() && specialty.get().equals(GlacyteData.UNSTABLE)) {
			World.get().submit(new GlacorExplodeTask(null, this, true));
		}
	}
	
	@Override
	public Hit decrementHealth(Hit hit) {
		Hit h = super.decrementHealth(hit);
		
		if((this.getCurrentHealth()) < (this.getMaxHealth() / 2) && !this.isDead() && !this.hasSummoned() && this.getCombatBuilder().getVictim() != null) {
			Player victim = this.getCombatBuilder().getVictim().toPlayer();
			
			if(victim == null) {
				return h;
			}
			
			for(GlacyteData glacyte : GlacyteData.VALUES) {
				Glacyte g = new Glacyte(glacyte, new Position(this.getPosition().getX() + glacyte.ordinal(), this.getPosition().getY()), this);
				g.setStrategy(Optional.of(new GlacyteCombatStrategy(g)));
				g.setRespawn(false);
				this.getGlacytes().add(glacyte);
				World.get().getNpcs().add(g);
				g.getCombatBuilder().attack(victim);
			}
			
			this.setSummoned();
		}
		
		return h;
	}
	
	public Optional<GlacyteData> getSpeciality() {
		return specialty;
	}
}
