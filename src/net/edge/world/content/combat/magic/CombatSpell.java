package net.edge.world.content.combat.magic;

import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.model.Graphic;
import net.edge.world.node.entity.model.Projectile;
import net.edge.world.node.entity.model.Spell;

import java.util.Optional;

/**
 * The {@link Spell} extension with support for combat related functions such as
 * effects and damage.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatSpell extends Spell {
	
	@Override
	public final void startCast(EntityNode cast, EntityNode castOn) {
		if(!cast.isVisible() || !castOn.isVisible())
			return;
		Optional<Animation> optional = castAnimation();
		if(optional.isPresent()) {
			Animation animation = new Animation(optional.get().getId(), optional.get().getDelay(), Animation.AnimationPriority.NORMAL);
			cast.animation(animation);
		}
		
		startGraphic().ifPresent(cast::graphic);
		
		projectile(cast, castOn).ifPresent(g -> World.submit(new Task(2, false) {
			@Override
			public void execute() {
				g.sendProjectile();
				this.cancel();
			}
		}));
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
	public abstract Optional<Projectile> projectile(EntityNode cast, EntityNode castOn);
	
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
	public abstract void executeOnHit(EntityNode cast, EntityNode castOn, boolean accurate, int damage);
}
