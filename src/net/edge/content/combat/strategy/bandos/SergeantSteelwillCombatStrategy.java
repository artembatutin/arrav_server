package net.edge.content.combat.strategy.bandos;

import net.edge.content.combat.CombatHit;
import net.edge.task.Task;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.mob.impl.gwd.GeneralGraardor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Represents the combat strategy for the steelwill minion.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SergeantSteelwillCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return victim.isPlayer() && GeneralGraardor.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		character.animation(SPELL.castAnimation().get());
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(character.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || character.isDead() || victim.isDead())
					return;
				SPELL.projectile(character, victim).get().sendProjectile();
			}
		});
		character.setCurrentlyCasting(SPELL);
		return new CombatHit(character, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public int attackDelay(Actor character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(Actor character) {
		return 7;
	}

	@Override
	public int[] getMobs() {
		return new int[]{6263};
	}

	private static final CombatNormalSpell SPELL = new CombatNormalSpell() {

		@Override
		public int spellId() {
			return 0;
		}

		@Override
		public int maximumHit() {
			return 160;
		}

		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(6154));
		}

		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(1202));
		}

		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 1203, 44, 3, 43, 43, 0));
		}

		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.empty();
		}

		@Override
		public int levelRequired() {
			return 0;
		}

		@Override
		public double baseExperience() {
			return 0;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

	};

}
