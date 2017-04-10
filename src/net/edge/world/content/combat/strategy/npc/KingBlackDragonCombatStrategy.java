package net.edge.world.content.combat.strategy.npc;

import com.google.common.collect.ImmutableList;
import net.edge.utils.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.model.node.NodeState;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.model.*;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.combat.magic.CombatNormalSpell;
import net.edge.world.model.node.entity.player.assets.AntifireDetails;
import net.edge.world.model.node.entity.player.assets.AntifireDetails.AntifireType;
import net.edge.task.Task;

import java.util.Optional;

public final class KingBlackDragonCombatStrategy implements CombatStrategy {
	
	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		return character.isNpc();
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
		return 7;
	}
	
	@Override
	public int[] getNpcs() {
		return new int[]{50};
	}
	
	private CombatSessionData melee(EntityNode character, EntityNode victim) {
		Animation animation[] = new Animation[]{new Animation(80), new Animation(91)};
		character.animation(RandomUtils.random(animation));
		return new CombatSessionData(character, victim, 1, CombatType.MELEE, true);
	}
	
	private CombatNormalSpell getSpell() {
		ImmutableList<CombatNormalSpell> spells = ImmutableList.of(FIRE_BLAST, POISON_BLAST);
		return RandomUtils.random(spells);
	}
	
	private CombatSessionData magic(EntityNode character, EntityNode victim) {
		character.animation(new Animation(81));
		CombatNormalSpell spell = getSpell();
		World.submit(new Task(1, false) {
			@Override
			public void execute() {
				this.cancel();
				if(character.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || character.isDead() || victim.isDead())
					return;
				spell.projectile(character, victim).get().sendProjectile();
			}
		});
		character.setCurrentlyCasting(spell);
		return new CombatSessionData(character, victim, 1, CombatType.MAGIC, false) {
			@Override
			public CombatSessionData preAttack() {
				if(this.getType() == CombatType.MAGIC && victim.isPlayer() && this.isAccurate()) {
					Player player = (Player) victim;
					if(spell.equals(POISON_BLAST) && !player.isPoisoned() && RandomUtils.inclusive(1000) < 100) {
						player.poison(PoisonType.STRONG_NPC);
					}
					if(!player.getEquipment().containsAny(1540, 11283) && !player.getAntifireDetails().isPresent()) {
						this.getHits()[0] = new Hit(RandomUtils.inclusive(350, 650));
						this.setAccurate(true);
						player.message("You are badly burnt by the dragon's fire breath!");
					} else if(player.getEquipment().containsAny(1540, 11283) && !player.getAntifireDetails().isPresent()) {
						this.getHits()[0] = new Hit(RandomUtils.inclusive(100, 200));
						this.setAccurate(true);
						player.message("Your shield absorbs most of the dragon fire!");
					} else if(!player.getEquipment().containsAny(1540, 11283) && player.getAntifireDetails().isPresent()) {
						AntifireDetails details = player.getAntifireDetails().get();
						
						boolean regular = false;
						
						if(details.getType().equals(AntifireType.REGULAR)) {
							regular = true;
							this.getHits()[0] = new Hit(RandomUtils.inclusive(100, 200));
							this.setAccurate(true);
						} else if(details.getType().equals(AntifireType.SUPER)) {
							if(RandomUtils.nextBoolean()) {
								this.getHits()[0].setAccurate(false);
							} else {
								this.getHits()[0] = new Hit(RandomUtils.inclusive(1, 75));
							}
						}
						
						player.message("Your " + (regular ? "regular" : "super") + " antifire potion protects you from the heat of the dragon's breath!");
					} else {
						if(RandomUtils.nextBoolean()) {
							this.getHits()[0].setAccurate(false);
						} else {
							this.getHits()[0] = new Hit(RandomUtils.inclusive(1, 50));
						}
					}
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
	
	private static final CombatNormalSpell POISON_BLAST = new CombatNormalSpell() {
		
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