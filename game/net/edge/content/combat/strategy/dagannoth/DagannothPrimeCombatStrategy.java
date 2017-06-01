package net.edge.content.combat.strategy.dagannoth;

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
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public final class DagannothPrimeCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return character.isNpc() && victim.isPlayer();
	}
	
	@Override
	public void incomingAttack(EntityNode character, EntityNode attacker, CombatSessionData data) {
		if(data.getType().equals(CombatType.MAGIC) || data.getType().equals(CombatType.MELEE)) {
			attacker.toPlayer().message("Your attacks are completely blocked...");
			Arrays.stream(data.getHits()).filter(Objects::nonNull).forEach(h -> h.setAccurate(false));
			return;
		}
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		character.setCurrentlyCasting(SPELL);
		SPELL.castAnimation().ifPresent(character::animation);
		World.submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(character.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || character.isDead() || victim.isDead()) {
					return;
				}
				SPELL.projectile(character, victim).ifPresent(p -> p.sendProjectile());
			}
		});
		return new CombatSessionData(character, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public int attackDelay(EntityNode character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(EntityNode character) {
		return 8;
	}

	@Override
	public int[] getNpcs() {
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
		public Optional<Projectile> projectile(EntityNode cast, EntityNode castOn) {
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
