package net.edge.content.combat.strategy.armadyl;

import net.edge.content.combat.CombatHit;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the aviansie combat strategy.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AviansieCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return character.isNpc();
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		character.animation(new Animation(character.toMob().getDefinition().getAttackAnimation()));
		CombatType[] data = getCombatType(character.toMob(), victim);
		CombatType c = RandomUtils.random(data);
		return type(character, victim, c);
	}

	private CombatHit type(Actor character, Actor victim, CombatType c) {
		switch(c) {
			case MAGIC:
				return magic(character, victim);
			case MELEE:
				return melee(character, victim);
			case RANGED:
				return ranged(character, victim);
			default:
				return melee(character, victim);
		}
	}

	private CombatType[] getCombatType(Mob mob, Actor victim) {
		switch(mob.getId()) {
			case 6246:
				return mob.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.RANGED, CombatType.MAGIC} : new CombatType[]{CombatType.RANGED, CombatType.MAGIC};
			case 6230:
				return mob.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.RANGED} : new CombatType[]{CombatType.RANGED};
			case 6231:
				return mob.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.MAGIC} : new CombatType[]{CombatType.MAGIC};
			default:
				return new CombatType[]{CombatType.MELEE};
		}
	}

	private CombatHit melee(Actor character, Actor victim) {
		return new CombatHit(character, victim, 1, CombatType.MELEE, true);
	}

	private CombatHit ranged(Actor character, Actor victim) {
		new Projectile(character, victim, 1837, 44, 3, 120, 43, 0).sendProjectile();
		return new CombatHit(character, victim, 1, CombatType.RANGED, true);
	}

	private CombatHit magic(Actor character, Actor victim) {
		character.setCurrentlyCasting(SPELL);
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(character.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || character.isDead() || victim.isDead()) {
					return;
				}
				new Projectile(character, victim, 2729, 44, 3, 120, 43, 0).sendProjectile();
			}
		});
		return new CombatHit(character, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public int attackDelay(Actor character) {
		return character.getAttackDelay();
	}

	@Override
	public int attackDistance(Actor character) {
		return 7;
	}

	@Override
	public int[] getMobs() {
		return new int[]{6246, 6230, 6231};
	}

	private static final CombatNormalSpell SPELL = new CombatNormalSpell() {

		@Override
		public int spellId() {
			return 0;
		}

		@Override
		public int maximumHit() {
			return 130;
		}

		@Override
		public Optional<Animation> castAnimation() {
			return Optional.empty();
		}

		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}

		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
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
