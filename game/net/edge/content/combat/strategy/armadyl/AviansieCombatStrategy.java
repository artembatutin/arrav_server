package net.edge.content.combat.strategy.armadyl;

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
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the aviansie combat strategy.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AviansieCombatStrategy implements CombatStrategy {

	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return character.isNpc();
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		character.animation(new Animation(character.toNpc().getDefinition().getAttackAnimation()));
		CombatType[] data = getCombatType(character.toNpc(), victim);
		CombatType c = RandomUtils.random(data);
		return type(character, victim, c);
	}

	private CombatSessionData type(EntityNode character, EntityNode victim, CombatType c) {
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

	private CombatType[] getCombatType(Npc npc, EntityNode victim) {
		switch(npc.getId()) {
			case 6246:
				return npc.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.RANGED, CombatType.MAGIC} : new CombatType[]{CombatType.RANGED, CombatType.MAGIC};
			case 6230:
				return npc.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.RANGED} : new CombatType[]{CombatType.RANGED};
			case 6231:
				return npc.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.MAGIC} : new CombatType[]{CombatType.MAGIC};
			default:
				return new CombatType[]{CombatType.MELEE};
		}
	}

	private CombatSessionData melee(EntityNode character, EntityNode victim) {
		return new CombatSessionData(character, victim, 1, CombatType.MELEE, true);
	}

	private CombatSessionData ranged(EntityNode character, EntityNode victim) {
		new Projectile(character, victim, 1837, 44, 3, 120, 43, 0).sendProjectile();
		return new CombatSessionData(character, victim, 1, CombatType.RANGED, true);
	}

	private CombatSessionData magic(EntityNode character, EntityNode victim) {
		character.setCurrentlyCasting(SPELL);
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(character.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || character.isDead() || victim.isDead()) {
					return;
				}
				new Projectile(character, victim, 2729, 44, 3, 120, 43, 0).sendProjectile();
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
		return 7;
	}

	@Override
	public int[] getNpcs() {
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
		public Optional<Projectile> projectile(EntityNode cast, EntityNode castOn) {
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
