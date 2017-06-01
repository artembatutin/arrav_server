package net.edge.content.combat.strategy.armadyl;

import net.edge.task.Task;
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
import net.edge.world.node.entity.npc.impl.gwd.KreeArra;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

public final class WingmanSkreeCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return victim.isPlayer() && KreeArra.CHAMBER.inLocation(victim.getPosition());
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		character.animation(SPELL.castAnimation().get());
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
		return new CombatSessionData(character, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public int attackDelay(EntityNode character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(EntityNode character) {
		return 7;
	}

	@Override
	public int[] getNpcs() {
		return new int[]{6223};
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
			return Optional.of(new Animation(6953));
		}

		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}

		@Override
		public Optional<Projectile> projectile(EntityNode cast, EntityNode castOn) {
			return Optional.of(new Projectile(cast, castOn, 1505, 44, 3, 43, 43, 0));
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
