package net.edge.content.combat.strategy.npc;

import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.world.World;
import net.edge.content.combat.CombatSessionData;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

public final class WildyWyrmCombatStrategy implements CombatStrategy {
	
	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		CombatType[] data = character.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.MAGIC} : new CombatType[]{CombatType.MAGIC};
		CombatType c = RandomUtils.random(data);
		return type(character, victim, c);
	}

	@Override
	public int attackDelay(EntityNode character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(EntityNode character) {
		return 6;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{3334};
	}

	private CombatSessionData melee(EntityNode character, EntityNode victim) {
		Animation animation[] = new Animation[]{new Animation(80), new Animation(91)};
		character.animation(RandomUtils.random(animation));
		return new CombatSessionData(character, victim, 1, CombatType.MELEE, true);
	}

	private CombatSessionData magic(EntityNode character, EntityNode victim) {
		character.animation(new Animation(12794));
		World.submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(character.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || character.isDead() || victim.isDead())
					return;
				SPELL.projectile(character, victim).get().sendProjectile();
			}
		});
		character.setCurrentlyCasting(SPELL);
		return new CombatSessionData(character, victim, 1, CombatType.MAGIC, false);
	}

	private CombatSessionData type(EntityNode character, EntityNode victim, CombatType type) {
		switch(type) {
			case MELEE:
				return melee(character, victim);
			case MAGIC:
				return magic(character, victim);
			default:
				return magic(character, victim);
		}
	}

	private static final CombatNormalSpell SPELL = new CombatNormalSpell() {

		@Override
		public Optional<Animation> castAnimation() {
			return Optional.empty();
		}

		@Override
		public Optional<Projectile> projectile(EntityNode cast, EntityNode castOn) {
			return Optional.of(new Projectile(cast, castOn, 394, 44, 3, 43, 31, 0));
		}

		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2725, 100));
		}

		@Override
		public int maximumHit() {
			return 100;
		}

		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}

		@Override
		public double baseExperience() {
			return -1;
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public int levelRequired() {
			return -1;
		}

		@Override
		public int spellId() {
			return -1;
		}
	};

	private static final CombatNormalSpell FIRE_BLAST = new CombatNormalSpell() {

		@Override
		public Optional<Animation> castAnimation() {
			return Optional.empty();
		}

		@Override
		public Optional<Projectile> projectile(EntityNode cast, EntityNode castOn) {
			return Optional.of(new Projectile(cast, castOn, 393, 44, 3, 43, 31, 0));
		}

		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2739, 100));
		}

		@Override
		public int maximumHit() {
			return 200;
		}

		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}

		@Override
		public double baseExperience() {
			return -1;
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public int levelRequired() {
			return -1;
		}

		@Override
		public int spellId() {
			return -1;
		}
	};
}
