package net.edge.content.combat.strategy.rfd;

import net.edge.content.combat.CombatHit;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.node.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

public final class Karamel implements CombatStrategy {
	
	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return character.isNpc() && victim.isPlayer();
	}

	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		CombatType[] data = character.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.MAGIC} : new CombatType[]{CombatType.MAGIC};
		CombatType type = RandomUtils.random(data);
		return type(character, victim, type);
	}
	
	private CombatHit melee(Actor character, Actor victim) {
		Animation animation = new Animation(422);
		character.animation(animation);
		return new CombatHit(character, victim, 1, CombatType.MELEE, true);
	}
	
	private CombatHit magic(Actor character, Actor victim) {
		character.animation(new Animation(1979));
		//TODO walk away only if the victim is using melee (basically if hes within 1 tile distance)
		character.forceChat("Semolina-Go!");
		CombatNormalSpell spell = SPELL;
		character.setCurrentlyCasting(spell);
		return new CombatHit(character, victim, 1, CombatType.MAGIC, false) {
			@Override
			public CombatHit preAttack() {
				if(this.getType() == CombatType.MAGIC && victim.isPlayer() && this.isAccurate()) {
					Player player = (Player) victim;
					player.freeze(15);
				}
				return this;
			}
		};
	}
	
	private CombatHit type(Actor character, Actor victim, CombatType type) {
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
	public int attackDelay(Actor character) {
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(Actor character) {
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
			return 140;
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
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
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
