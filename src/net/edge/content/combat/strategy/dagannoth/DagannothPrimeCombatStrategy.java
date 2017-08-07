package net.edge.content.combat.strategy.dagannoth;

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
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public final class DagannothPrimeCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(Actor actor, Actor victim) {
		return actor.isMob() && victim.isPlayer();
	}
	
	@Override
	public void incomingAttack(Actor actor, Actor attacker, CombatHit data) {
		if(data.getType().equals(CombatType.MAGIC) || data.getType().equals(CombatType.MELEE)) {
			attacker.toPlayer().message("Your attacks are completely blocked...");
			Arrays.stream(data.getHits()).filter(Objects::nonNull).forEach(h -> h.setAccurate(false));
			return;
		}
	}

	@Override
	public CombatHit outgoingAttack(Actor actor, Actor victim) {
		actor.setCurrentlyCasting(SPELL);
		SPELL.castAnimation().ifPresent(actor::animation);
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(actor.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || actor.isDead() || victim.isDead()) {
					return;
				}
				SPELL.projectile(actor, victim).ifPresent(p -> p.sendProjectile());
			}
		});
		return new CombatHit(actor, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public int attackDelay(Actor actor) {
		return actor.getAttackDelay();
	}

	@Override
	public int attackDistance(Actor actor) {
		return 8;
	}

	@Override
	public int[] getMobs() {
		return new int[]{2882};
	}
	
	private static final CombatNormalSpell SPELL = new CombatNormalSpell() {

		@Override
		public int spellId() {
			return 0;
		}

		@Override
		public int maximumHit() {
			return 550;
		}

		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(2853));
		}

		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}

		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 500, 44, 4, 60, 43, 0));
		}

		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(502, 50));
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
