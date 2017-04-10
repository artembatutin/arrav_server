package net.edge.world.content.combat.ranged;

import com.google.common.collect.ImmutableSet;
import net.edge.utils.rand.RandomUtils;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.weapon.FightType;
import net.edge.world.content.skill.Skill;
import net.edge.world.content.skill.Skills;
import net.edge.world.model.node.entity.model.Graphic;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.World;
import net.edge.world.content.combat.Combat;
import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.ranged.CombatRangedDetails.CombatRangedWeapon;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.model.Hit;
import net.edge.world.model.node.entity.model.Hit.HitIcon;
import net.edge.world.model.node.entity.model.Hit.HitType;
import net.edge.world.model.node.entity.model.PoisonType;
import net.edge.world.model.node.item.ItemIdentifiers;

/**
 * Represents a single definition of a ranged weapon which consists of details
 * that each ammunition has.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum CombatRangedAmmoDefinition {
	TRAINING_ARROWS(9706, 1051, 64, 36, 40, 31, 806),
	BRONZE_ARROW(new int[]{882, 883, 5616, 5622}, 10, 64, 36, 40, 31, 19) {
		@Override
		public Graphic getGraphic(Player player) {
			return player.getRangedDetails().getWeapon().isPresent() && player.getRangedDetails().getWeapon().get().getWeapon() == ItemIdentifiers.DARK_BOW ? new Graphic(1104, 100) : new Graphic(19, 100);
		}
	},
	IRON_ARROW(new int[]{884, 885, 5617, 5623}, 9, 64, 36, 40, 31, 18) {
		@Override
		public Graphic getGraphic(Player player) {
			return player.getRangedDetails().getWeapon().isPresent() && player.getRangedDetails().getWeapon().get().getWeapon() == ItemIdentifiers.DARK_BOW ? new Graphic(1105, 100) : new Graphic(18, 100);
		}
	},
	STEEL_ARROW(new int[]{886, 887, 5618, 5624}, 11, 64, 36, 40, 31, 20) {
		@Override
		public Graphic getGraphic(Player player) {
			return player.getRangedDetails().getWeapon().isPresent() && player.getRangedDetails().getWeapon().get().getWeapon() == ItemIdentifiers.DARK_BOW ? new Graphic(1106, 100) : new Graphic(20, 100);
		}
	},
	MITHRIL_ARROW(new int[]{888, 889, 5619, 5625}, 12, 64, 36, 40, 31, 21) {
		@Override
		public Graphic getGraphic(Player player) {
			return player.getRangedDetails().getWeapon().isPresent() && player.getRangedDetails().getWeapon().get().getWeapon() == ItemIdentifiers.DARK_BOW ? new Graphic(1107, 100) : new Graphic(21, 100);
		}
	},
	ADAMANT_ARROW(new int[]{890, 891, 5620, 5626}, 13, 64, 36, 40, 31, 22) {
		@Override
		public Graphic getGraphic(Player player) {
			return player.getRangedDetails().getWeapon().isPresent() && player.getRangedDetails().getWeapon().get().getWeapon() == ItemIdentifiers.DARK_BOW ? new Graphic(1108, 100) : new Graphic(22, 100);
		}
	},
	RUNE_ARROW(new int[]{892, 893, 5621, 5627}, 15, 64, 36, 40, 31, 24) {
		@Override
		public Graphic getGraphic(Player player) {
			return player.getRangedDetails().getWeapon().isPresent() && player.getRangedDetails().getWeapon().get().getWeapon() == ItemIdentifiers.DARK_BOW ? new Graphic(1109, 100) : new Graphic(24, 100);
		}
	},
	
	GUAM_TAR(10142, -1, -1, -1, -1, -1, new Graphic(952, 70)) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			CombatType type = player.getFightType().equals(FightType.FLARE) ? CombatType.RANGED : player.getFightType().equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;
			
			if(type.equals(CombatType.MAGIC)) {
				Skill magic = player.getSkills()[Skills.MAGIC];
				int hit = RandomUtils.inclusive(0, (int) Math.floor(0.5 + magic.getLevel() * 15 / 80) * 10);
				player.setCurrentlyCasting(Combat.emptySpell(hit));
			}
			
			data = new CombatSessionData(player, victim, 1, type, false);
			return data;
		}
	},
	MARRENTIL_TAR(10143, -1, -1, -1, -1, -1, new Graphic(952, 70)) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			CombatType type = player.getFightType().equals(FightType.FLARE) ? CombatType.RANGED : player.getFightType().equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;
			
			if(type.equals(CombatType.MAGIC)) {
				Skill magic = player.getSkills()[Skills.MAGIC];
				int hit = RandomUtils.inclusive(0, (int) Math.floor(0.5 + magic.getLevel() * 123 / 640) * 10);
				player.setCurrentlyCasting(Combat.emptySpell(hit));
			}
			
			data = new CombatSessionData(player, victim, 1, type, false);
			return data;
		}
	},
	TARROMIN_TAR(10144, -1, -1, -1, -1, -1, new Graphic(952, 70)) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			CombatType type = player.getFightType().equals(FightType.FLARE) ? CombatType.RANGED : player.getFightType().equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;
			
			if(type.equals(CombatType.MAGIC)) {
				Skill magic = player.getSkills()[Skills.MAGIC];
				int hit = RandomUtils.inclusive(0, (int) Math.floor(0.5 + magic.getLevel() * 141 / 640) * 10);
				player.setCurrentlyCasting(Combat.emptySpell(hit));
			}
			
			data = new CombatSessionData(player, victim, 1, type, false);
			return data;
		}
	},
	HARRALANDER_TAR(10145, -1, -1, -1, -1, -1, new Graphic(952, 70)) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			CombatType type = player.getFightType().equals(FightType.FLARE) ? CombatType.RANGED : player.getFightType().equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;
			
			if(type.equals(CombatType.MAGIC)) {
				Skill magic = player.getSkills()[Skills.MAGIC];
				int hit = RandomUtils.inclusive(0, (int) Math.floor(0.5 + magic.getLevel() * 156 / 640) * 10);
				player.setCurrentlyCasting(Combat.emptySpell(hit));
			}
			
			data = new CombatSessionData(player, victim, 1, type, false);
			return data;
		}
	},
	
	CRYSTAL_ARROW(-1, 249, 64, 36, 40, 31, 250),
	ZARYTE_ARROW(-1, 249, 64, 36, 40, 31, 250),
	
	DRAGON_ARROW(new int[]{11212, 11227, 11228, 11229}, 1120, 64, 36, 40, 31, 1111),
	
	HAND_CANNON_SHOT(new int[]{15243}, 2143, 8, 3, 43, 31, new Graphic(2141, 13)),
	
	ABYSSALBANE_ARROW(new int[]{21655, 21733, 21734, 21735}, 15, 64, 36, 40, 31, 24) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(!victim.isNpc()) {
				return data;
			}
			
			if(victim.toNpc().getId() != 1615) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			data.getHits()[0].setDamage((int) (baseHit * 1.40));
			return data;
		}
	},
	BASILISKBANE_ARROW(new int[]{21650, 21719, 21720, 21721}, 15, 64, 36, 40, 31, 24) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(!victim.isNpc()) {
				return data;
			}
			
			if(victim.toNpc().getId() != 1616) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			data.getHits()[0].setDamage((int) (baseHit * 1.40));
			return data;
		}
	},
	DRAGONBANE_ARROW(new int[]{21640, 21712, 21713, 21714}, 15, 64, 36, 40, 31, 24) {
		final ImmutableSet<Integer> affectedNpcs = ImmutableSet.of(941, 55, 54, 53, 50, 5362, 1590, 1591, 1592, 5363);
		
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(!victim.isNpc()) {
				return data;
			}
			
			if(!affectedNpcs.contains(victim.toNpc().getId())) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			data.getHits()[0].setDamage((int) (baseHit * 1.40));
			return data;
		}
	},
	WALLASALKIBANE_ARROW(new int[]{21645, 21726, 21727, 21728}, 15, 64, 36, 40, 31, 24) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(!victim.isNpc()) {
				return data;
			}
			
			if(victim.toNpc().getId() != 2457) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			data.getHits()[0].setDamage((int) (baseHit * 1.40));
			return data;
		}
	},
	
	ABYSSALBANE_BOLT(new int[]{21675, 21701, 21702, 21703}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(!victim.isNpc()) {
				return data;
			}
			
			if(victim.toNpc().getId() != 1615) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			data.getHits()[0].setDamage((int) (baseHit * 1.40));
			return data;
		}
	},
	BASILISKBANE_BOLT(new int[]{21670, 21687, 21688, 21689}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(!victim.isNpc()) {
				return data;
			}
			
			if(victim.toNpc().getId() != 1616) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			data.getHits()[0].setDamage((int) (baseHit * 1.40));
			return data;
		}
	},
	DRAGONBANE_BOLT(new int[]{21660, 21680, 21681, 21682}, 27, 86, 42, 43, 31, 0) {
		final ImmutableSet<Integer> affectedNpcs = ImmutableSet.of(941, 55, 54, 53, 50, 5362, 1590, 1591, 1592, 5363);
		
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(!victim.isNpc()) {
				return data;
			}
			
			if(!affectedNpcs.contains(victim.toNpc().getId())) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			data.getHits()[0].setDamage((int) (baseHit * 1.40));
			return data;
		}
	},
	WALLASALKIBANE_BOLT(new int[]{21665, 21694, 21695, 21696}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(!victim.isNpc()) {
				return data;
			}
			
			if(victim.toNpc().getId() != 2457) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			data.getHits()[0].setDamage((int) (baseHit * 1.40));
			return data;
		}
	},
	
	BRUTAL_BRONZE_ARROW(4773, 10, 64, 36, 40, 31, 19),
	BRUTAL_IRON_ARROW(4778, 9, 64, 36, 40, 31, 18),
	BRUTAL_STEEL_ARROW(4783, 11, 64, 36, 40, 31, 20),
	BRUTAL_BLACK_ARROW(4788, 11, 64, 36, 40, 31, 20),
	//TODO
	BRUTAL_MITHRIL_ARROW(4793, 12, 64, 36, 40, 31, 21),
	BRUTAL_ADAMANT_ARROW(4798, 13, 64, 36, 40, 31, 22),
	BRUTAL_RUNE_ARROW(4803, 15, 64, 36, 40, 31, 24),
	
	KEBBIT_BOLT(10158, 27, 86, 42, 43, 31, 0),
	LONG_KEBBIT_BOLT(10159, 27, 86, 42, 43, 31, 0),
	
	BONE_BOLT(8882, 27, 86, 42, 43, 31, 0),
	
	ICE_ARROW(78, 9, 64, 36, 40, 31, 18),
	//TODO
	
	OGRE_ARROW(2866, 9, 64, 36, 40, 31, 18),
	//TODO
	
	BROAD_ARROW(4160, 9, 64, 36, 40, 31, 18),
	//TODO
	
	SARADOMIN_ARROW(19152, 99, 64, 36, 40, 31, 96),
	GUTHIX_ARROW(19157, 98, 64, 36, 40, 31, 95),
	ZAMORAK_ARROW(19162, 100, 64, 36, 40, 31, 97),
	
	//TODO: brutal/ogre arrows.
	
	BRONZE_BOLTS(877, 27, 86, 42, 43, 31, 0),
	IRON_BOLTS(9140, 27, 86, 42, 43, 31, 0),
	OPAL_BOLTS(new int[]{879, 9236}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(weapon.getAmmunition().getItem().getId() != 9236) {
				return data;
			}
			
			if(RandomUtils.inclusive(100) > 10) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			data.getHits()[0].setDamage((int) (baseHit * 1.10));
			victim.graphic(new Graphic(749, 0, 20));
			return data;
		}
	},
	STEEL_BOLTS(9141, 27, 86, 42, 43, 31, 0),
	PEARL_BOLTS(new int[]{880, 9238}, 27, 86, 42, 43, 31, 0) {
		
		final ImmutableSet<Integer> affectedNpcs = ImmutableSet.of(941, 55, 54, 53, 50, 5362, 1590, 1591, 1592, 5363, 110, 1633, 1634, 1635, 1636, 1019, 2591, 2592, 2593, 2594, 2595, 2596, 2597, 2598, 2599, 2600, 2601, 2602, 2603, 2604, 2605, 2606, 2607, 2608, 2609, 2610, 2611, 2612, 2613, 2614, 2615, 2616, 2627, 2628, 2629, 2630, 2631, 2631, 2734, 2735, 2736, 2737, 2738, 2739, 2740, 2741, 2742, 2743, 2744, 2745, 2746);
		
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(weapon.getAmmunition().getItem().getId() != 9238) {
				return data;
			}
			
			if(victim.isPlayer() && victim.toPlayer().getEquipment().containsAny(1383, 1395, 1403)) {
				return data;
			}
			
			if(RandomUtils.inclusive(100) > 10) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			boolean multiply = false;
			
			if(victim.isNpc() && affectedNpcs.contains(victim.toNpc().getId())) {
				multiply = true;
			}
			
			if(victim.isPlayer() && victim.toPlayer().getEquipment().containsAny(1387, 1393, 1401)) {
				multiply = true;
			}
			
			data.getHits()[0].setDamage((int) ((baseHit * 1.075) * (multiply ? 1.20 : 1.0)));
			victim.graphic(new Graphic(750, 0, 20));
			return data;
		}
	},
	BLACK_BOLTS(13083, 27, 86, 42, 43, 31, 0),
	JADE_BOLTS(new int[]{9335, 9237}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(weapon.getAmmunition().getItem().getId() != 9237) {
				return data;
			}
			
			if(RandomUtils.inclusive(100) > 10) {
				return data;
			}
			
			victim.stun(1200);
			victim.graphic(new Graphic(755, 0, 20));
			return data;
		}
	},
	MITHRIL_BOLTS(9142, 27, 86, 42, 43, 31, 0),
	TOPAZ_BOLTS(new int[]{9336, 9239}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(weapon.getAmmunition().getItem().getId() != 9239) {
				return data;
			}
			
			if(!victim.isPlayer()) {
				return data;
			}
			
			if(RandomUtils.inclusive(100) > 10) {
				return data;
			}
			
			victim.toPlayer().getSkills()[Skills.MAGIC].decreaseLevel(1);
			victim.graphic(new Graphic(757, 0, 20));
			return data;
		}
	},
	ADAMANT_BOLTS(9143, 27, 86, 42, 43, 31, 0),
	SAPPHIRE_BOLTS(new int[]{9337, 9240}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(weapon.getAmmunition().getItem().getId() != 9240) {
				return data;
			}
			
			if(!victim.isPlayer()) {
				return data;
			}
			
			if(RandomUtils.inclusive(100) > 10) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			int percentage = (int) (baseHit * 0.05);
			victim.toPlayer().getSkills()[Skills.PRAYER].decreaseLevel(percentage);
			player.getSkills()[Skills.PRAYER].increaseLevel(percentage, player.getSkills()[Skills.PRAYER].getRealLevel());
			victim.graphic(new Graphic(759, 100, 20));
			return data;
		}
	},
	RUNITE_BOLTS(9144, 27, 86, 42, 43, 31, 0),
	EMERALD_BOLTS(new int[]{9338, 9241}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(weapon.getAmmunition().getItem().getId() != 9241) {
				return data;
			}
			
			if(victim.isPoisoned()) {
				return data;
			}
			
			if(RandomUtils.inclusive(100) > 10) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			victim.poison(PoisonType.SUPER_RANGED);
			return data;
		}
	},
	RUBY_BOLTS(new int[]{9339, 9242}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(weapon.getAmmunition().getItem().getId() != 9242) {
				return data;
			}
			
			if(RandomUtils.inclusive(100) > 10) {
				return data;
			}
			
			if(player.getCurrentHealth() == 4) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			int multiplier = (int) (victim.getCurrentHealth() * 1.20);
			
			data.getHits()[0].setDamage(baseHit * multiplier);
			victim.graphic(new Graphic(754, 0, 20));
			return data;
		}
	},
	BROAD_TIPPED_BOLTS(13280, 27, 86, 42, 43, 31, 0),
	DIAMOND_BOLTS(new int[]{9340, 9243}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(weapon.getAmmunition().getItem().getId() != 9243) {
				return data;
			}
			
			if(RandomUtils.inclusive(100) > 10) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			data.getHits()[0].setDamage(baseHit + RandomUtils.inclusive(5, 14));
			victim.graphic(new Graphic(758, 0, 20));
			return data;
		}
	},
	DRAGON_BOLTS(new int[]{9341, 9244}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(weapon.getAmmunition().getItem().getId() != 9244) {
				return data;
			}
			
			if(victim.isPlayer() && victim.toPlayer().getEquipment().containsAny(1540, 11283)) {
				return data;
			}
			
			if(victim.isPlayer() && victim.toPlayer().getAntifireDetails().isPresent()) {
				return data;
			}
			
			if(RandomUtils.inclusive(100) > 10) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			data.getHits()[0].setDamage((int) (baseHit * 1.14));
			victim.graphic(new Graphic(756, 0, 20));
			return data;
		}
	},
	BOLT_RACK(4740, 27, 86, 42, 43, 31, 0),
	ONYX_BOLTS(new int[]{9342, 9245}, 27, 86, 42, 43, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
			if(weapon.getAmmunition().getItem().getId() != 9245) {
				return data;
			}
			
			//TODO spell shouldn't work on undead
			
			if(RandomUtils.inclusive(100) > 10) {
				return data;
			}
			
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			int damage = (int) (baseHit * (RandomUtils.inclusive(20, 25) / 100D));
			
			data.getHits()[0].setDamage(damage);
			player.healEntity(damage / 4);
			victim.graphic(new Graphic(756, 0, 20));
			return data;
		}
	},
	BARBED_BOLTS(881, 27, 86, 42, 43, 31, 0),
	BLURITE_BOLTS(9139, 27, 86, 42, 43, 31, 0),
	SILVER_BOLTS(9145, 27, 86, 42, 43, 31, 0),
	
	CHINCHOMPA(new int[]{10033, 10034}, 908, 64, 36, 40, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode attacker, CombatSessionData data) {
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			Combat.charactersWithinDistance(attacker, World.getLocalPlayers(attacker), 1).forEach(pl -> {
				if(!World.getAreaManager().inMulti(pl)) {
					return;
				}
				pl.graphic(new Graphic(2739, 80, 58));
				pl.damage(new Hit(RandomUtils.inclusive(baseHit - 5, baseHit + 5), HitType.NORMAL, HitIcon.RANGED, attacker.getSlot()));
				pl.getCombatBuilder().getDamageCache().add(player, baseHit);
			});
			
			return data;
		}
	},
	RED_CHINCHOMPA(10034, 909, 64, 36, 40, 31, 0) {
		@Override
		public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode attacker, CombatSessionData data) {
			int baseHit = data.getHits()[0].getDamage();
			
			if(baseHit < 1) {
				return data;
			}
			
			Combat.charactersWithinDistance(attacker, World.getLocalPlayers(attacker), 1).forEach(pl -> {
				if(!World.getAreaManager().inMulti(pl)) {
					return;
				}
				pl.graphic(new Graphic(2739, 80, 58));
				pl.damage(new Hit(baseHit, HitType.NORMAL, HitIcon.RANGED, attacker.getSlot()));
				pl.getCombatBuilder().getDamageCache().add(player, baseHit);
			});
			
			return data;
		}
	},
	
	BRONZE_KNIFE(new int[]{864, 870, 5654, 5661}, 212, 46, 30, 40, 37, 219),
	IRON_KNIFE(new int[]{863, 871, 5655, 5662}, 213, 46, 30, 40, 37, 220),
	STEEL_KNIFE(new int[]{865, 872, 5656, 5663}, 214, 46, 30, 40, 37, 221),
	BLACK_KNIFE(new int[]{869, 874, 5658, 5665}, 215, 46, 30, 40, 37, 222),
	MITHRIL_KNIFE(new int[]{866, 873, 5657, 5664}, 216, 46, 30, 40, 37, 223),
	ADAMANT_KNIFE(new int[]{867, 875, 5659, 5666}, 217, 46, 30, 40, 37, 224),
	RUNE_KNIFE(new int[]{868, 876, 5660, 5667}, 218, 46, 30, 40, 37, 225),
	
	BRONZE_DART(new int[]{806, 812, 5628, 5635}, 226, 40, 16, 45, 37, 232),
	IRON_DART(new int[]{807, 813, 5629, 5636}, 227, 40, 16, 45, 37, 233),
	STEEL_DART(new int[]{808, 814, 5630, 5637}, 228, 40, 16, 45, 37, 234),
	BLACK_DART(new int[]{3093, 3094, 5631, 5638}, 273, 40, 16, 45, 37, 273),
	MITHRIL_DART(new int[]{809, 815, 5632, 5639}, 229, 40, 16, 45, 37, 235),
	ADAMANT_DART(new int[]{810, 816, 5633, 5640}, 230, 40, 16, 45, 37, 236),
	RUNE_DART(new int[]{811, 817, 5634, 5641}, 231, 40, 16, 45, 37, 237),
	
	BRONZE_JAVELIN(new int[]{825, 831, 5642, 5648}, 200, 80, 50, 45, 37, 0),
	IRON_JAVELIN(new int[]{826, 832, 5643, 5649}, 201, 80, 50, 45, 37, 0),
	STEEL_JAVELIN(new int[]{827, 833, 5644, 5650}, 202, 80, 50, 45, 37, 0),
	MITHRIL_JAVELIN(new int[]{828, 834, 5645, 5651}, 203, 80, 50, 45, 37, 0),
	ADAMANT_JAVELIN(new int[]{829, 835, 5646, 5652}, 204, 80, 50, 45, 37, 0),
	RUNE_JAVELIN(new int[]{830, 836, 5647, 5653}, 205, 80, 50, 45, 37, 0),
	MORRIGAN_JAVELIN(new int[]{13879, 13880, 13881, 13882}, 1837, 70, 50, 45, 37, 1836),
	
	BRONZE_THROWNAXE(800, 36, 80, 50, 43, 31, 0),
	IRON_THROWNAXE(801, 35, 80, 50, 43, 31, 0),
	STEEL_THROWNAXE(802, 37, 80, 50, 43, 31, 0),
	MITHRIL_THROWNAXE(803, 38, 80, 50, 43, 31, 0),
	ADAMANT_THROWNAXE(804, 39, 80, 50, 43, 31, 0),
	RUNE_THROWNAXE(805, 41, 80, 50, 43, 31, 0),
	MORRIGAN_THROWNAXE(13883, 1839, 80, 50, 18, 31, 0),
	TOKTZ_XIL_UL(6522, 442, 68, 32, 32, 32, 0);
	
	/**
	 * A set of constants that can never be recovered when fired.
	 */
	public static final ImmutableSet<CombatRangedAmmoDefinition> NON_DROPPABLE = ImmutableSet.of(SARADOMIN_ARROW, ZAMORAK_ARROW, GUTHIX_ARROW, CHINCHOMPA, RED_CHINCHOMPA, HAND_CANNON_SHOT, BOLT_RACK, GUAM_TAR, MARRENTIL_TAR, HARRALANDER_TAR, TARROMIN_TAR);
	
	/**
	 * The item identifications for this ammunition.
	 * <b>This includes poison, enchanted etc. arrows.</b>
	 */
	private final Item[] ids;
	
	/**
	 * The projectile identification for this ranged ammo.
	 */
	private final int projectile;
	
	/**
	 * The delay of the projectile for this ranged ammo.
	 */
	private final int delay;
	
	/**
	 * The speed of the projectile for this ranged ammo.
	 */
	private final int speed;
	
	/**
	 * The starting height of the projectile for this ranged ammo.
	 */
	private final int startHeight;
	
	/**
	 * The ending height of the projectile for this ranged ammo.
	 */
	private final int endHeight;
	
	/**
	 * The graphic played when this ammo is shot.
	 */
	private final Graphic graphic;
	
	/**
	 * Constructs a new {@link CombatRangedAmmoDefinition}.
	 * @param ids         {@link #ids}.
	 * @param projectile  {@link #projectile}.
	 * @param delay       {@link #delay}.
	 * @param speed       {@link #speed}.
	 * @param startHeight {@link #startHeight}.
	 * @param endHeight   {@link #endHeight}.
	 * @param graphic     {@link #graphic}.
	 */
	CombatRangedAmmoDefinition(int[] ids, int projectile, int delay, int speed, int startHeight, int endHeight, int graphic) {
		this.ids = Item.convert(ids);
		this.projectile = projectile;
		this.delay = delay;
		this.speed = speed;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.graphic = new Graphic(graphic, 100);
	}
	
	/**
	 * Constructs a new {@link CombatRangedAmmoDefinition}.
	 * @param ids         {@link #ids}.
	 * @param projectile  {@link #projectile}.
	 * @param delay       {@link #delay}.
	 * @param speed       {@link #speed}.
	 * @param startHeight {@link #startHeight}.
	 * @param endHeight   {@link #endHeight}.
	 * @param graphic     {@link #graphic}.
	 */
	CombatRangedAmmoDefinition(int[] ids, int projectile, int delay, int speed, int startHeight, int endHeight, Graphic graphic) {
		this.ids = Item.convert(ids);
		this.projectile = projectile;
		this.delay = delay;
		this.speed = speed;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.graphic = graphic;
	}
	
	/**
	 * Constructs a new {@link CombatRangedAmmoDefinition}.
	 * @param id          {@link #ids}.
	 * @param projectile  {@link #projectile}.
	 * @param delay       {@link #delay}.
	 * @param speed       {@link #speed}.
	 * @param startHeight {@link #startHeight}.
	 * @param endHeight   {@link #endHeight}.
	 * @param graphic     {@link #graphic}.
	 */
	CombatRangedAmmoDefinition(int id, int projectile, int delay, int speed, int startHeight, int endHeight, int graphic) {
		this.ids = new Item[]{new Item(id)};
		this.projectile = projectile;
		this.delay = delay;
		this.speed = speed;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.graphic = new Graphic(graphic, 100);
	}
	
	/**
	 * Constructs a new {@link CombatRangedAmmoDefinition}.
	 * @param id          {@link #ids}.
	 * @param projectile  {@link #projectile}.
	 * @param delay       {@link #delay}.
	 * @param speed       {@link #speed}.
	 * @param startHeight {@link #startHeight}.
	 * @param endHeight   {@link #endHeight}.
	 * @param graphic     {@link #graphic}.
	 */
	CombatRangedAmmoDefinition(int id, int projectile, int delay, int speed, int startHeight, int endHeight, Graphic graphic) {
		this.ids = new Item[]{new Item(id)};
		this.projectile = projectile;
		this.delay = delay;
		this.speed = speed;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.graphic = graphic;
	}
	
	/**
	 * The method which can be overriden to supply special effects which will be
	 * applied on each <b>successful</b> hit on the specified {@code victim}.
	 * @param player the player who's shooting this ammunition.
	 * @param weapon the weapon the {@code player} used to fire this ammunition from.
	 * @param victim the victim being hit by this ammunition.
	 * @param data   the data of the hit that would of been applied.
	 */
	public CombatSessionData applyEffects(Player player, CombatRangedWeapon weapon, EntityNode victim, CombatSessionData data) {
		return data;
	}
	
	/**
	 * @return the ids
	 */
	public Item[] getAmmus() {
		return ids;
	}
	
	/**
	 * @return the projectile
	 */
	public int getProjectile() {
		return projectile;
	}
	
	/**
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * @return the startHeight
	 */
	public int getStartHeight() {
		return startHeight;
	}
	
	/**
	 * @return the endHeight
	 */
	public int getEndHeight() {
		return endHeight;
	}
	
	/**
	 * @return the graphic
	 */
	public Graphic getGraphic() {
		return graphic;
	}
	
	/**
	 * @return the graphic
	 */
	public Graphic getGraphic(Player player) {
		return graphic;
	}
}