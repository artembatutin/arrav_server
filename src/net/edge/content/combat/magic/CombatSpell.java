package net.edge.content.combat.magic;

import net.edge.content.combat.CombatType;
import net.edge.task.Task;
import net.edge.world.*;
import net.edge.world.entity.actor.Actor;

import java.util.Optional;

import static net.edge.world.Projectile.MAGIC_DELAYS;

/**
 * The {@link Spell} extension with support for combat related functions such as
 * effects and damage.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatSpell extends Spell {
	
	@Override
	public final int startCast(Actor cast, Actor castOn) {
		if(!cast.isVisible() || !castOn.isVisible())
			return 0;
		Optional<Animation> optional = castAnimation();
		if(optional.isPresent()) {
			Animation animation = new Animation(optional.get().getId(), optional.get().getDelay(), Animation.AnimationPriority.NORMAL);
			cast.animation(animation);
		}
		startGraphic().ifPresent(cast::graphic);
		Optional<Projectile> p = projectile(cast, castOn);
		if(p.isPresent()) {
			World.get().submit(new Task(2, false) {
				@Override
				public void execute() {
					p.get().sendProjectile();
					this.cancel();
				}
			});
			return p.get().getTravelTime();
		}
		int delay = p.map(Projectile::getTravelTime).orElse(0);
		if(delay == 0) {
			int distance = (int) cast.getPosition().getDistance(castOn.getPosition());
			delay = MAGIC_DELAYS[distance > 10 ? 10 : distance];
		}
		return delay;
	}
	
	/**
	 * The identification number for this spell.
	 * @return the identification.
	 */
	public abstract int spellId();
	
	/**
	 * The maximum hit that can be dealt with this spell.
	 * @return the maximum hit.
	 */
	public abstract int maximumHit();
	
	/**
	 * The animation played when this spell is cast.
	 * @return the cast animation.
	 */
	public abstract Optional<Animation> castAnimation();
	
	/**
	 * The graphic played when this spell is cast.
	 * @return the cast graphic.
	 */
	public abstract Optional<Graphic> startGraphic();
	
	/**
	 * The projectile played when this spell is cast.
	 * @param cast   the character casting this spell.
	 * @param castOn the character this spell is being cast on.
	 * @return the cast projectile.
	 */
	public abstract Optional<Projectile> projectile(Actor cast, Actor castOn);
	
	/**
	 * The graphic played when this spell hits the victim.
	 * @return the hit graphic.
	 */
	public abstract Optional<Graphic> endGraphic();
	
	/**
	 * Executes when this spell hits {@code castOn}.
	 * @param cast     the character casting this spell.
	 * @param castOn   the character this spell is being cast on.
	 * @param accurate if this spell was accurate.
	 * @param damage   the damage inflicted by this spell.
	 */
	public abstract void executeOnHit(Actor cast, Actor castOn, boolean accurate, int damage);
}
