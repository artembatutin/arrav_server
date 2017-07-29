package net.edge.content.combat.magic;

import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.effect.CombatEffectType;
import net.edge.content.combat.magic.ancients.CombatAncientSpell;
import net.edge.world.entity.actor.Actor;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.PoisonType;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Arrays;
import java.util.Optional;

/**
 * The enumerated type whose elements represent the combat spells that can be
 * cast.
 * @author lare96 <http://github.com/lare96>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum CombatSpells {
	WIND_STRIKE(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14221, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2699, 54, 3, 13, 11, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2700, 50));
		}
		
		@Override
		public int maximumHit() {
			return 20;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 5.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556), new Item(558)});
		}
		
		@Override
		public int levelRequired() {
			return 1;
		}
		
		@Override
		public int spellId() {
			return 1152;
		}
	}),
	CONFUSE(new CombatEffectSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(716));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 103, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public void effect(Actor cast, Actor castOn) {
			if(!castOn.weaken(CombatWeaken.ATTACK_LOW) && cast.isPlayer()) {
				Player player = (Player) cast;
				String s = castOn.getType().name().toLowerCase();
				player.message("The spell has no effect " + "because the " + s + " has already been weakened.");
			}
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(104, 100));
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(102, 85));
		}
		
		@Override
		public double baseExperience() {
			return 13;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555, 3), new Item(557, 2), new Item(559)});
		}
		
		@Override
		public int levelRequired() {
			return 3;
		}
		
		@Override
		public int spellId() {
			return 1153;
		}
	}),
	WATER_STRIKE(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14221, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2703, 54, 30, 27, 21, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2708, 100));
		}
		
		@Override
		public int maximumHit() {
			return 40;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2701, 0, 20));
		}
		
		@Override
		public double baseExperience() {
			return 7.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555), new Item(556), new Item(558)});
		}
		
		@Override
		public int levelRequired() {
			return 5;
		}
		
		@Override
		public int spellId() {
			return 1154;
		}
	}),
	EARTH_STRIKE(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14221, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2718, 48, 5, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2723, 100));
		}
		
		@Override
		public int maximumHit() {
			return 60;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2713, 0, 20));
		}
		
		@Override
		public double baseExperience() {
			return 9.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 1), new Item(558, 1), new Item(557, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 9;
		}
		
		@Override
		public int spellId() {
			return 1156;
		}
	}),
	WEAKEN(new CombatEffectSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(717, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 106, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public void effect(Actor cast, Actor castOn) {
			if(!castOn.weaken(CombatWeaken.STRENGTH_LOW) && cast.isPlayer()) {
				Player player = (Player) cast;
				String s = castOn.getType().name().toLowerCase();
				player.message("The spell has no effect " + "because the " + s + " has already been weakened.");
			}
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(107, 150));
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(105, 85, 20));
		}
		
		@Override
		public double baseExperience() {
			return 20.5;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555, 3), new Item(557, 2), new Item(559, 1)});
		}
		
		@Override
		public int levelRequired() {
			return 11;
		}
		
		@Override
		public int spellId() {
			return 1157;
		}
	}),
	FIRE_STRIKE(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14221, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2729, 44, 3, 23, 21, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2737, 100));
		}
		
		@Override
		public int maximumHit() {
			return 80;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2728, 0, 20));
		}
		
		@Override
		public double baseExperience() {
			return 11.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 1), new Item(558, 1), new Item(554, 3)});
		}
		
		@Override
		public int levelRequired() {
			return 13;
		}
		
		@Override
		public int spellId() {
			return 1158;
		}
	}),
	FIRE_STRIKE_TORCHER(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(3882, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2729, 44, 3, 144, 71, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2737, 100));
		}
		
		@Override
		public int maximumHit() {
			return 80;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 11.5;
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
			return 13;
		}
		
		@Override
		public int spellId() {
			return 1158;
		}
	}),
	WIND_BOLT(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14220));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2699, 44, 3, 23, 18, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(119, 100));
		}
		
		@Override
		public int maximumHit() {
			return 90;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 13.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 2), new Item(562, 1)});
		}
		
		@Override
		public int levelRequired() {
			return 17;
		}
		
		@Override
		public int spellId() {
			return 1160;
		}
	}),
	CURSE(new CombatEffectSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(718, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 109, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public void effect(Actor cast, Actor castOn) {
			if(!castOn.weaken(CombatWeaken.DEFENCE_LOW) && cast.isPlayer()) {
				Player player = (Player) cast;
				String s = castOn.getType().name().toLowerCase();
				player.message("The spell has no effect " + "because the " + s + " has already been weakened.");
			}
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(110, 100));
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(108, 85, 20));
		}
		
		@Override
		public double baseExperience() {
			return 29;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555, 2), new Item(557, 3), new Item(559, 1)});
		}
		
		@Override
		public int levelRequired() {
			return 19;
		}
		
		@Override
		public int spellId() {
			return 1161;
		}
	}),
	BIND(new CombatEffectSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(710));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 178, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public void effect(Actor cast, Actor castOn) {
			if(castOn.getMovementQueue().isLockMovement()) {
				if(cast.isPlayer())
					cast.toPlayer().message("The spell has " + "no effect because they are already frozen.");
				return;
			}
			if(castOn.isPlayer()) {
				Player player = (Player) castOn;
				player.message("You have been frozen by " + "magic!");
			}
			castOn.freeze(5);
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(181, 100));
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(177, 85));
		}
		
		@Override
		public double baseExperience() {
			return 30;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555, 3), new Item(557, 3), new Item(561, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 20;
		}
		
		@Override
		public int spellId() {
			return 1572;
		}
	}),
	WATER_BOLT(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14220, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2704, 44, 3, 23, 21, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2709, 100));
		}
		
		@Override
		public int maximumHit() {
			return 100;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2701, 0, 20));
		}
		
		@Override
		public double baseExperience() {
			return 16.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 2), new Item(562, 1), new Item(555, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 23;
		}
		
		@Override
		public int spellId() {
			return 1163;
		}
	}),
	EARTH_BOLT(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14222, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2719, 49, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2724, 100));
		}
		
		@Override
		public int maximumHit() {
			return 110;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2714, 0, 20));
		}
		
		@Override
		public double baseExperience() {
			return 19.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 2), new Item(562, 1), new Item(557, 3)});
		}
		
		@Override
		public int levelRequired() {
			return 29;
		}
		
		@Override
		public int spellId() {
			return 1166;
		}
	}),
	FIRE_BOLT(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14223, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2731, 44, 3, 18, 18, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2738, 100));
		}
		
		@Override
		public int maximumHit() {
			return 120;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2728, 0, 20));
		}
		
		@Override
		public double baseExperience() {
			return 22.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 3), new Item(562, 1), new Item(554, 4)});
		}
		
		@Override
		public int levelRequired() {
			return 35;
		}
		
		@Override
		public int spellId() {
			return 1169;
		}
	}),
	CRUMBLE_UNDEAD(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(724, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 146, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(147, 100));
		}
		
		@Override
		public int maximumHit() {
			return 150;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(145, 85, 20));
		}
		
		@Override
		public double baseExperience() {
			return 24.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 2), new Item(562, 1), new Item(557, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 39;
		}
		
		@Override
		public int spellId() {
			return 1171;
		}
		
	}),
	WIND_BLAST(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14221, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2699, 44, 3, 23, 21, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2700, 100));
		}
		
		@Override
		public int maximumHit() {
			return 130;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 25.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 3), new Item(560, 1)});
		}
		
		@Override
		public int levelRequired() {
			return 41;
		}
		
		@Override
		public int spellId() {
			return 1172;
		}
	}),
	WATER_BLAST(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14220, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2706, 44, 3, 22, 19, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2710, 100));
		}
		
		@Override
		public int maximumHit() {
			return 140;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2702, 0, 20));
		}
		
		@Override
		public double baseExperience() {
			return 28.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555, 3), new Item(556, 3), new Item(560, 1)});
		}
		
		@Override
		public int levelRequired() {
			return 47;
		}
		
		@Override
		public int spellId() {
			return 1175;
		}
	}),
	IBAN_BLAST(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(708));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 88, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(89, 100));
		}
		
		@Override
		public int maximumHit() {
			return 250;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(87, 85, 20));
		}
		
		@Override
		public double baseExperience() {
			return 30;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.of(new Item[]{new Item(1409)});
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(560, 1), new Item(554, 5)});
		}
		
		@Override
		public int levelRequired() {
			return 50;
		}
		
		@Override
		public int spellId() {
			return 1539;
		}
	}),
	SNARE(new CombatEffectSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(710));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 178, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public void effect(Actor cast, Actor castOn) {
			if(castOn.getMovementQueue().isLockMovement()) {
				if(cast.isPlayer())
					cast.toPlayer().message("The spell has no effect because they are already frozen.");
				return;
			}
			if(castOn.isPlayer()) {
				castOn.toPlayer().message("You have been frozen by magic!");
			}
			castOn.freeze(10);
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(180, 100));
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(177, 85, 20));
		}
		
		@Override
		public double baseExperience() {
			return 60.5;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555, 3), new Item(557, 4), new Item(561, 3)});
		}
		
		@Override
		public int levelRequired() {
			return 50;
		}
		
		@Override
		public int spellId() {
			return 1582;
		}
	}),
	MAGIC_DART(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(711));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 328, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(329));
		}
		
		@Override
		public int maximumHit() {
			return 190;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(327, 85, 20));
		}
		
		@Override
		public double baseExperience() {
			return 30;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.of(new Item[]{new Item(4170)});
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(558, 4), new Item(560, 1)});
		}
		
		@Override
		public int levelRequired() {
			return 50;
		}
		
		@Override
		public int spellId() {
			return 12037;
		}
	}),
	EARTH_BLAST(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14222, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2720, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2725, 100));
		}
		
		@Override
		public int maximumHit() {
			return 150;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2715, 85, 20));
		}
		
		@Override
		public double baseExperience() {
			return 31.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 3), new Item(560, 1), new Item(557, 4)});
		}
		
		@Override
		public int levelRequired() {
			return 53;
		}
		
		@Override
		public int spellId() {
			return 1177;
		}
	}),
	FIRE_BLAST(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14223, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2733, 44, 3, 18, 17, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2739, 100));
		}
		
		@Override
		public int maximumHit() {
			return 160;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2728, 0, 20));
		}
		
		@Override
		public double baseExperience() {
			return 34.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 4), new Item(560, 1), new Item(554, 5)});
		}
		
		@Override
		public int levelRequired() {
			return 59;
		}
		
		@Override
		public int spellId() {
			return 1181;
		}
	}),
	SARADOMIN_STRIKE(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(811));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(76, 100));
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
			return 61;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.of(new Item[]{new Item(2415)});
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 4), new Item(565, 2), new Item(554, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 60;
		}
		
		@Override
		public int spellId() {
			return 1190;
		}
	}),
	CLAWS_OF_GUTHIX(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(811));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(77, 100));
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
			return 61;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.of(new Item[]{new Item(2416)});
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 4), new Item(565, 2), new Item(554, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 60;
		}
		
		@Override
		public int spellId() {
			return 1191;
		}
	}),
	FLAMES_OF_ZAMORAK(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(811));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(78));
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
			return 61;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.of(new Item[]{new Item(2417)});
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 4), new Item(565, 2), new Item(554, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 60;
		}
		
		@Override
		public int spellId() {
			return 1192;
		}
	}),
	WIND_WAVE(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14221, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2699, 44, 3, 23, 21, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2700, 100));
		}
		
		@Override
		public int maximumHit() {
			return 170;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 36;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 5), new Item(565, 1)});
		}
		
		@Override
		public int levelRequired() {
			return 62;
		}
		
		@Override
		public int spellId() {
			return 1183;
		}
	}),
	WATER_WAVE(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14220, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2706, 44, 3, 23, 21, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2700, 100));
		}
		
		@Override
		public int maximumHit() {
			return 180;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2702, 0, 20));
		}
		
		@Override
		public double baseExperience() {
			return 37.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 5), new Item(565, 1), new Item(555, 7)});
		}
		
		@Override
		public int levelRequired() {
			return 65;
		}
		
		@Override
		public int spellId() {
			return 1185;
		}
	}),
	VULNERABILITY(new CombatEffectSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(718, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 168, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public void effect(Actor cast, Actor castOn) {
			if(!castOn.weaken(CombatWeaken.DEFENCE_HIGH) && cast.isPlayer()) {
				Player player = (Player) cast;
				String s = castOn.getType().name().toLowerCase();
				player.message("The spell has no effect " + "because the " + s + " has already been weakened.");
			}
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(169, 100));
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(167, 85, 20));
		}
		
		@Override
		public double baseExperience() {
			return 76;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(557, 5), new Item(555, 5), new Item(566, 1)});
		}
		
		@Override
		public int levelRequired() {
			return 66;
		}
		
		@Override
		public int spellId() {
			return 1542;
		}
	}),
	EARTH_WAVE(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14222, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2721, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2726, 100));
		}
		
		@Override
		public int maximumHit() {
			return 190;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2716, 0, 20));
		}
		
		@Override
		public double baseExperience() {
			return 40;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 5), new Item(565, 1), new Item(557, 7)});
		}
		
		@Override
		public int levelRequired() {
			return 70;
		}
		
		@Override
		public int spellId() {
			return 1188;
		}
	}),
	ENFEEBLE(new CombatEffectSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(728, 10));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 171, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public void effect(Actor cast, Actor castOn) {
			if(!castOn.weaken(CombatWeaken.STRENGTH_HIGH) && cast.isPlayer()) {
				Player player = (Player) cast;
				String s = castOn.getType().name().toLowerCase();
				player.message("The spell has no effect " + "because the " + s + " has already been weakened.");
			}
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(172, 100));
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(170, 85, 10));
		}
		
		@Override
		public double baseExperience() {
			return 83;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(557, 8), new Item(555, 8), new Item(566, 1)});
		}
		
		@Override
		public int levelRequired() {
			return 73;
		}
		
		@Override
		public int spellId() {
			return 1543;
		}
	}),
	FIRE_WAVE(new CombatNormalSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(14223, 20));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 2735, 44, 3, 23, 21, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(2740, 100));
		}
		
		@Override
		public int maximumHit() {
			return 200;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(2728, 0, 20));
		}
		
		@Override
		public double baseExperience() {
			return 42.5;
		}
		
		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 5), new Item(565, 1), new Item(554, 7)});
		}
		
		@Override
		public int levelRequired() {
			return 75;
		}
		
		@Override
		public int spellId() {
			return 1189;
		}
	}),
	ENTANGLE(new CombatEffectSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(710));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 178, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public void effect(Actor cast, Actor castOn) {
			if(castOn.getMovementQueue().isLockMovement()) {
				if(cast.isPlayer())
					cast.toPlayer().message("The spell has " + "no effect because they are already frozen.");
				return;
			}
			if(castOn.isPlayer()) {
				castOn.toPlayer().message("You have been frozen by " + "magic!");
			}
			castOn.freeze(15);
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(179, 100));
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(177, 85, 20));
		}
		
		@Override
		public double baseExperience() {
			return 91;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555, 5), new Item(557, 5), new Item(561, 4)});
		}
		
		@Override
		public int levelRequired() {
			return 79;
		}
		
		@Override
		public int spellId() {
			return 1592;
		}
	}),
	STUN(new CombatEffectSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(729, 15));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 174, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public void effect(Actor cast, Actor castOn) {
			if(!castOn.weaken(CombatWeaken.ATTACK_HIGH) && cast.isPlayer()) {
				Player player = (Player) cast;
				String s = castOn.getType().name().toLowerCase();
				player.message("The spell has no effect " + "because the " + s + " has already been weakened.");
			}
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(107, 100));
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(173, 85, 15));
		}
		
		@Override
		public double baseExperience() {
			return 90;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(557, 12), new Item(555, 12), new Item(566, 1)});
		}
		
		@Override
		public int levelRequired() {
			return 80;
		}
		
		@Override
		public int spellId() {
			return 1562;
		}
	}),
	TELEBLOCK(new CombatEffectSpell() {
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1819));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 344, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public void effect(Actor cast, Actor castOn) {
			if(castOn.isPlayer())
				CombatUtil.effect(castOn, CombatEffectType.TELEBLOCK);
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(345));
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 65;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(563, 1), new Item(562, 1), new Item(560, 1)});
		}
		
		@Override
		public int levelRequired() {
			return 85;
		}
		
		@Override
		public int spellId() {
			return 12445;
		}
	}),
	SMOKE_RUSH(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.poison(PoisonType.DEFAULT_RANGED);
		}
		
		@Override
		public int radius() {
			return 0;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1978));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 384, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(385, 100));
		}
		
		@Override
		public int maximumHit() {
			return 130;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 30;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 1), new Item(554, 1), new Item(562, 2), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 50;
		}
		
		@Override
		public int spellId() {
			return 12939;
		}
	}),
	SHADOW_RUSH(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.weaken(CombatWeaken.ATTACK_LOW);
		}
		
		@Override
		public int radius() {
			return 0;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1978));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 378, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(379));
		}
		
		@Override
		public int maximumHit() {
			return 140;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 31;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 1), new Item(566, 1), new Item(562, 2), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 52;
		}
		
		@Override
		public int spellId() {
			return 12987;
		}
	}),
	BLOOD_RUSH(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			cast.healEntity((int) (damage * 0.25));
		}
		
		@Override
		public int radius() {
			return 0;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1978));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 372, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(373));
		}
		
		@Override
		public int maximumHit() {
			return 150;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 33;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(565, 1), new Item(562, 2), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 56;
		}
		
		@Override
		public int spellId() {
			return 12901;
		}
	}),
	ICE_RUSH(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.freeze(7);
		}
		
		@Override
		public int radius() {
			return 0;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1978));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 360, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(361));
		}
		
		@Override
		public int maximumHit() {
			return 160;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 36;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555, 2), new Item(562, 2), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 58;
		}
		
		@Override
		public int spellId() {
			return 12861;
		}
	}),
	SMOKE_BURST(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.poison(PoisonType.DEFAULT_RANGED);
		}
		
		@Override
		public int radius() {
			return 1;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1979));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(389));
		}
		
		@Override
		public int maximumHit() {
			return 170;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 36;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 2), new Item(554, 2), new Item(562, 4), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 62;
		}
		
		@Override
		public int spellId() {
			return 12963;
		}
	}),
	SHADOW_BURST(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.weaken(CombatWeaken.ATTACK_LOW);
		}
		
		@Override
		public int radius() {
			return 1;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1979));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(382));
		}
		
		@Override
		public int maximumHit() {
			return 18;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 37;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 1), new Item(566, 2), new Item(562, 4), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 64;
		}
		
		@Override
		public int spellId() {
			return 13011;
		}
	}),
	BLOOD_BURST(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			cast.healEntity((int) (damage * 0.25));
		}
		
		@Override
		public int radius() {
			return 1;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1979));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(376));
		}
		
		@Override
		public int maximumHit() {
			return 210;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 39;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(565, 2), new Item(562, 4), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 68;
		}
		
		@Override
		public int spellId() {
			return 12919;
		}
	}),
	ICE_BURST(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.freeze(10);
		}
		
		@Override
		public int radius() {
			return 1;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1979));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(363));
		}
		
		@Override
		public int maximumHit() {
			return 220;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 40;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555, 4), new Item(562, 4), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 70;
		}
		
		@Override
		public int spellId() {
			return 12881;
		}
	}),
	SMOKE_BLITZ(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.poison(PoisonType.DEFAULT_RANGED);
		}
		
		@Override
		public int radius() {
			return 0;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1978));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 386, 44, 3, 43, 31, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(387, 100));
		}
		
		@Override
		public int maximumHit() {
			return 230;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 42;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 2), new Item(554, 2), new Item(565, 2), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 74;
		}
		
		@Override
		public int spellId() {
			return 12951;
		}
	}),
	SHADOW_BLITZ(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.weaken(CombatWeaken.ATTACK_HIGH);
		}
		
		@Override
		public int radius() {
			return 0;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1978));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 380, 44, 3, 43, 20, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(381));
		}
		
		@Override
		public int maximumHit() {
			return 240;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 43;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 2), new Item(566, 2), new Item(565, 2), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 76;
		}
		
		@Override
		public int spellId() {
			return 12999;
		}
	}),
	BLOOD_BLITZ(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			cast.healEntity((int) (damage * 0.25));
		}
		
		@Override
		public int radius() {
			return 0;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1978));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.of(new Projectile(cast, castOn, 374, 44, 3, 43, 21, 0, CombatType.MAGIC));
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(375));
		}
		
		@Override
		public int maximumHit() {
			return 250;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 45;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(565, 4), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 80;
		}
		
		@Override
		public int spellId() {
			return 12911;
		}
	}),
	ICE_BLITZ(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.freeze(10);
		}
		
		@Override
		public int radius() {
			return 0;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1978));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(367));
		}
		
		@Override
		public int maximumHit() {
			return 260;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.of(new Graphic(366, 100));
		}
		
		@Override
		public double baseExperience() {
			return 46;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555, 3), new Item(565, 2), new Item(560, 2)});
		}
		
		@Override
		public int levelRequired() {
			return 82;
		}
		
		@Override
		public int spellId() {
			return 12871;
		}
	}),
	SMOKE_BARRAGE(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.poison(PoisonType.SUPER_RANGED);
		}
		
		@Override
		public int radius() {
			return 1;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1979));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(391, 100));
		}
		
		@Override
		public int maximumHit() {
			return 270;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 48;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 4), new Item(554, 4), new Item(565, 2), new Item(560, 4)});
		}
		
		@Override
		public int levelRequired() {
			return 86;
		}
		
		@Override
		public int spellId() {
			return 12975;
		}
	}),
	SHADOW_BARRAGE(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.weaken(CombatWeaken.ATTACK_HIGH);
		}
		
		@Override
		public int radius() {
			return 1;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1979));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(383));
		}
		
		@Override
		public int maximumHit() {
			return 280;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 48;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(556, 4), new Item(566, 3), new Item(565, 2), new Item(560, 4)});
		}
		
		@Override
		public int levelRequired() {
			return 88;
		}
		
		@Override
		public int spellId() {
			return 13023;
		}
	}),
	BLOOD_BARRAGE(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			cast.healEntity((int) (damage * 0.25));
		}
		
		@Override
		public int radius() {
			return 1;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1979));
		}
		
		@Override
		public Optional<Projectile> projectile(Actor cast, Actor castOn) {
			return Optional.empty();
		}
		
		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.of(new Graphic(377));
		}
		
		@Override
		public int maximumHit() {
			return 290;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 51;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(560, 4), new Item(566, 1), new Item(565, 4)});
		}
		
		@Override
		public int levelRequired() {
			return 92;
		}
		
		@Override
		public int spellId() {
			return 12929;
		}
	}),
	ICE_BARRAGE(new CombatAncientSpell() {
		@Override
		public void effect(Actor cast, Actor castOn, int damage) {
			if(damage < 1)
				return;
			castOn.freeze(15);
		}
		
		@Override
		public int radius() {
			return 1;
		}
		
		@Override
		public Optional<Animation> castAnimation() {
			return Optional.of(new Animation(1979));
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
		public int maximumHit() {
			return 300;
		}
		
		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}
		
		@Override
		public double baseExperience() {
			return 52;
		}
		
		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.of(new Item[]{new Item(555, 6), new Item(565, 2), new Item(560, 4)});
		}
		
		@Override
		public int levelRequired() {
			return 94;
		}
		
		@Override
		public int spellId() {
			return 12891;
		}
	});
	
	/**
	 * The spell attached to this element.
	 */
	private final CombatSpell spell;
	
	/**
	 * Creates a new {@link CombatSpells}.
	 * @param spell the spell attached to this element.
	 */
	CombatSpells(CombatSpell spell) {
		this.spell = spell;
	}
	
	/**
	 * Gets the spell attached to this element.
	 * @return the spell.
	 */
	public final CombatSpell getSpell() {
		return spell;
	}
	
	/**
	 * Gets the spell with a {@link CombatSpell#spellId()} of {@code id}.
	 * @param id the identification of the combat spell.
	 * @return the combat spell with that identification.
	 */
	public static Optional<CombatSpells> getSpell(int id) {
		return Arrays.stream(CombatSpells.values()).filter(s -> s != null && s.getSpell().spellId() == id).findFirst();
	}
}
