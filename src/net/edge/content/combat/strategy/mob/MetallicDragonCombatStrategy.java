package net.edge.content.combat.strategy.mob;

import net.edge.content.combat.CombatHit;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.world.*;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.AntifireDetails;
import net.edge.world.entity.actor.player.assets.AntifireDetails.AntifireType;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * Holds functionality for the combat strategy for metallic dragons.
 * @author <a href="http://www.rune-server.org/members/Stand+Up/">Stan</a>
 */
public final class MetallicDragonCombatStrategy implements CombatStrategy {
	
	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		return character.isMob();
	}
	
	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		CombatType[] data = character.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.MAGIC} : new CombatType[]{CombatType.MAGIC};
		CombatType c = RandomUtils.random(data);
		return type(character, victim, c);
	}
	
	private CombatHit melee(Actor character, Actor victim) {
		character.animation(new Animation(character.toMob().getDefinition().getAttackAnimation()));
		return new CombatHit(character, victim, 1, CombatType.MELEE, true);
	}
	
	private CombatHit magic(Actor character, Actor victim) {
		character.animation(new Animation(14246));
		CombatNormalSpell spell = FIRE_BLAST;
		World.get().submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(character.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || character.isDead() || victim.isDead())
					return;
				spell.projectile(character, victim).get().sendProjectile();
			}
		});
		character.setCurrentlyCasting(spell);
		return new CombatHit(character, victim, 1, CombatType.MAGIC, false) {
			@Override
			public CombatHit preAttack() {
				if(this.getType() == CombatType.MAGIC && victim.isPlayer() && this.isAccurate()) {
					Player player = (Player) victim;
					if(!player.getEquipment().containsAny(1540, 11283) && !player.getAntifireDetails().isPresent()) {
						this.getHits()[0] = new Hit(RandomUtils.inclusive(250, 450));
						this.setAccurate(true);
						player.message("You are badly burnt by the dragon's fire breath!");
					} else if(player.getEquipment().containsAny(1540, 11283) && !player.getAntifireDetails().isPresent()) {
						this.getHits()[0] = new Hit(RandomUtils.inclusive(20, 130));
						this.setAccurate(true);
						player.message("Your shield absorbs most of the dragon fire!");
					} else if(!player.getEquipment().containsAny(1540, 11283) && player.getAntifireDetails().isPresent()) {
						AntifireDetails details = player.getAntifireDetails().get();
						
						boolean regular = false;
						
						if(details.getType().equals(AntifireType.REGULAR)) {
							regular = true;
							this.getHits()[0] = new Hit(RandomUtils.inclusive(50, 190));
							this.setAccurate(true);
						} else if(details.getType().equals(AntifireType.SUPER)) {
							if(RandomUtils.nextBoolean()) {
								this.getHits()[0].setAccurate(false);
							} else {
								this.getHits()[0] = new Hit(RandomUtils.inclusive(0, 15));
							}
						}
						
						player.message("Your " + (regular ? "regular" : "super") + " antifire potion protects you from the heat of the dragon's breath!");
					} else {
						if(RandomUtils.nextBoolean()) {
							this.getHits()[0].setAccurate(false);
						} else {
							this.getHits()[0] = new Hit(RandomUtils.inclusive(0, 15));
						}
					}
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
		return character.getAttackDelay();
	}
	
	@Override
	public int attackDistance(Actor character) {
		return 7;
	}
	
	@Override
	public int[] getMobs() {
		return new int[]{1590, 1591, 1592, 5363};
	}
	
	private static final CombatNormalSpell FIRE_BLAST = new CombatNormalSpell() {
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.empty();
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
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
