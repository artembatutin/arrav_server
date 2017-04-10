package net.edge.world.content.combat.strategy.rfd;

import net.edge.utils.rand.RandomUtils;
import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.magic.CombatNormalSpell;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.model.Graphic;
import net.edge.world.model.node.entity.model.Projectile;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

import java.util.Optional;

public final class Karamel implements CombatStrategy {
	
	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return character.isNpc() && victim.isPlayer();
	}

	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		CombatType[] data = character.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.MAGIC} : new CombatType[]{CombatType.MAGIC};
		CombatType type = RandomUtils.random(data);
		return type(character, victim, type);
	}
	
	private CombatSessionData melee(EntityNode character, EntityNode victim) {
		Animation animation = new Animation(422);
		character.animation(animation);
		return new CombatSessionData(character, victim, 1, CombatType.MELEE, true);
	}
	
	private CombatSessionData magic(EntityNode character, EntityNode victim) {
		character.animation(new Animation(1979));
		//TODO walk away only if the victim is using melee (basically if hes within 1 tile distance)
		character.forceChat("Semolina-Go!");
		CombatNormalSpell spell = SPELL;
		character.setCurrentlyCasting(spell);
		return new CombatSessionData(character, victim, 1, CombatType.MAGIC, false) {
			@Override
			public CombatSessionData preAttack() {
				if(this.getType() == CombatType.MAGIC && victim.isPlayer() && this.isAccurate()) {
					Player player = (Player) victim;
					player.freeze(15);
				}
				return this;
			}
		};
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
		return new int[]{3495};
	}

	private static final CombatNormalSpell SPELL = new CombatNormalSpell() {

		@Override
		public int spellId() {
			return 0;
		}

		@Override
		public int maximumHit() {
			return 14;
		}

		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1979));
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
			return Optional.of(new Graphic(369));
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
