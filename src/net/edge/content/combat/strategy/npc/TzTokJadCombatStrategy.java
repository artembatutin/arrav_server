package net.edge.content.combat.strategy.npc;

import net.edge.content.combat.CombatHit;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.World;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

public final class TzTokJadCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatHit outgoingAttack(EntityNode character, EntityNode victim) {
		CombatType[] data = character.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.RANGED, CombatType.MAGIC} : new CombatType[]{CombatType.RANGED, CombatType.MAGIC};
		CombatType c = RandomUtils.random(data);
		return type(character, victim, c);
	}

	private CombatHit type(EntityNode character, EntityNode victim, CombatType type) {
		switch(type) {
			case MELEE:
				return melee(character, victim);
			case RANGED:
				return ranged(character, victim);
			case MAGIC:
				return magic(character, victim);
			default:
				return magic(character, victim);
		}
	}

	private CombatHit melee(EntityNode character, EntityNode victim) {
		character.animation(new Animation(9277));
		return new CombatHit(character, victim, 1, CombatType.MELEE, true, 2);
	}

	private CombatHit ranged(EntityNode character, EntityNode victim) {
		character.animation(new Animation(9276));
		character.graphic(new Graphic(1625));
		World.get().submit(new Task(2, false) {
			@Override
			protected void execute() {
				this.cancel();
				if(character.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || character.isDead() || victim.isDead())
					return;
				victim.graphic(new Graphic(451));
			}
		});
		return new CombatHit(character, victim, 1, CombatType.RANGED, true, 5);
	}

	private CombatHit magic(EntityNode character, EntityNode victim) {
		character.setCurrentlyCasting(SPELL);
		character.animation(new Animation(9278));
		World.get().submit(new Task(4, false) {
			@Override
			protected void execute() {
				this.cancel();
				if(character.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || character.isDead() || victim.isDead())
					return;
				SPELL.projectile(character, victim).get().sendProjectile();
			}
		});
		return new CombatHit(character, victim, 1, CombatType.MAGIC, true, 6);
	}

	@Override
	public int attackDelay(EntityNode character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(EntityNode character) {
		return 10;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{2745};
	}

	private static final CombatNormalSpell SPELL = new CombatNormalSpell() {

		@Override
		public int spellId() {
			return 0;
		}

		@Override
		public int maximumHit() {
			return 980;
		}

		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(9300));
		}

		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(1626));
		}

		@Override
		public Optional<Projectile> projectile(EntityNode cast, EntityNode castOn) {
			return Optional.of(new Projectile(cast, castOn, 1627, 44, 3, 43, 31, 0));
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
