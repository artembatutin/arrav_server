package com.rageps.world.entity.actor.combat.ranged;

import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import com.rageps.world.model.Projectile;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatImpact;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.item.Item;

import java.util.Optional;

public enum RangedAmmunition {
	BRONZE_JAVELIN(CombatProjectile.getDefinition("Bronze javelin"), true, 825, 831, 5642, 5648),
	IRON_JAVELIN(CombatProjectile.getDefinition("Iron javelin"), true, 826, 832, 5643, 5649),
	STEEL_JAVELIN(CombatProjectile.getDefinition("Steel javelin"), true, 827, 833, 5644, 5650),
	MITHRIL_JAVELIN(CombatProjectile.getDefinition("Mithril javelin"), true, 828, 834, 5645, 5651),
	ADAMANT_JAVELIN(CombatProjectile.getDefinition("Adamant javelin"), true, 829, 835, 5646, 5652),
	RUNE_JAVELIN(CombatProjectile.getDefinition("Rune javelin"), true, 830, 836, 5647, 5653),
	
	BRONZE_THROWNAXE(CombatProjectile.getDefinition("Bronze thrownaxe"), true, 800),
	IRON_THROWNAXE(CombatProjectile.getDefinition("Iron thrownaxe"), true, 801),
	STEEL_THROWNAXE(CombatProjectile.getDefinition("Steel thrownaxe"), true, 802),
	MITHRIL_THROWNAXE(CombatProjectile.getDefinition("Mithril thrownaxe"), true, 803),
	ADAMANT_THROWNAXE(CombatProjectile.getDefinition("Adamant thrownaxe"), true, 804),
	RUNE_THROWNAXE(CombatProjectile.getDefinition("Rune thrownaxe"), true, 805),
	
	BRONZE_DART(CombatProjectile.getDefinition("Bronze dart"), true, 806, 812, 5628, 5635),
	IRON_DART(CombatProjectile.getDefinition("Iron dart"), true, 807, 813, 5629, 5636),
	BLACK_DART(CombatProjectile.getDefinition("Black dart"), true, 3093, 3094, 5631, 5638),
	STEEL_DART(CombatProjectile.getDefinition("Steel dart"), true, 808, 814, 5630, 5637),
	MITHRIL_DART(CombatProjectile.getDefinition("Mithril dart"), true, 809, 815, 5632, 5639),
	ADAMANT_DART(CombatProjectile.getDefinition("Adamant dart"), true, 810, 816, 5633, 5640),
	RUNE_DART(CombatProjectile.getDefinition("Rune dart"), true, 811, 817, 5634, 5641),
	DRAGON_DART(CombatProjectile.getDefinition("Dragon dart"), true, 11230, 11231),
	
	BRONZE_KNIFE(CombatProjectile.getDefinition("Bronze knife"), true, 864, 870, 5654, 5651),
	IRON_KNIFE(CombatProjectile.getDefinition("Iron knife"), true, 863, 871, 5655, 5662),
	BLACK_KNIFE(CombatProjectile.getDefinition("Black knife"), true, 869, 874, 5658, 5665),
	STEEL_KNIFE(CombatProjectile.getDefinition("Steel knife"), true, 865, 872, 5656, 5663),
	MITHRIL_KNIFE(CombatProjectile.getDefinition("Mithril knife"), true, 866, 873, 5657, 5664),
	ADAMANT_KNIFE(CombatProjectile.getDefinition("Adamant knife"), true, 867, 875, 5659, 5666),
	RUNE_KNIFE(CombatProjectile.getDefinition("Rune knife"), true, 868, 876, 5660, 5667),
	
	BRONZE_ARROW(CombatProjectile.getDefinition("Bronze arrow"), true, 882, 883, 5616, 5622),
	IRON_ARROW(CombatProjectile.getDefinition("Iron arrow"), true, 884, 885, 5617, 5623),
	STEEL_ARROW(CombatProjectile.getDefinition("Steel arrow"), true, 886, 887, 5618, 5624),
	MITHRIL_ARROW(CombatProjectile.getDefinition("Mithril arrow"), true, 888, 889, 5619, 5625),
	ADAMANT_ARROW(CombatProjectile.getDefinition("Adamant arrow"), true, 890, 891, 5620, 5626),
	RUNE_ARROW(CombatProjectile.getDefinition("Rune arrow"), true, 892, 893, 5621, 5627),
	DRAGON_ARROW(CombatProjectile.getDefinition("Dragon arrow"), true, 11212, 11227, 0, 11228),
	
	DOUBLE_BRONZE_ARROW(CombatProjectile.getDefinition("Bronze arrow"), true, 882, 883, 5616, 5622),
	DOUBLE_IRON_ARROW(CombatProjectile.getDefinition("Iron arrow"), true, 884, 885, 5617, 5623),
	DOUBLE_STEEL_ARROW(CombatProjectile.getDefinition("Steel arrow"), true, 886, 887, 5618, 5624),
	DOUBLE_MITHRIL_ARROW(CombatProjectile.getDefinition("Mithril arrow"), true, 888, 889, 5619, 5625),
	DOUBLE_ADAMANT_ARROW(CombatProjectile.getDefinition("Adamant arrow"), true, 890, 891, 5620, 5626),
	DOUBLE_RUNE_ARROW(CombatProjectile.getDefinition("Rune arrow"), true, 892, 893, 5621, 5627),
	DOUBLE_DRAGON_ARROW(CombatProjectile.getDefinition("Dragon arrow"), true, 11212, 11227, 0, 11228),
	
	BRONZE_BOLTS(CombatProjectile.getDefinition("Bronze bolts"), true, 877, 878, 6061, 6062, 879, 9236),
	BLURITE_BOLTS(CombatProjectile.getDefinition("Blurite bolts"), true, 9139, 9286, 9293, 9300, 9335, 9237),
	IRON_BOLTS(CombatProjectile.getDefinition("Iron bolts"), true, 9140, 9287, 9294, 9301, 880, 9238),
	SILVER_BOLTS(CombatProjectile.getDefinition("Silver bolts"), true, 9145, 9292, 9299, 9306),
	STEEL_BOLTS(CombatProjectile.getDefinition("Steel bolts"), true, 9141, 9288, 9295, 9302, 9336, 9239),
	MITHRIL_BOLTS(CombatProjectile.getDefinition("Mithril bolts"), true, 9142, 9289, 9296, 9303, 9337, 9240, 9338, 9241),
	ADAMANT_BOLTS(CombatProjectile.getDefinition("Adamant bolts"), true, 9143, 9290, 9297, 9304, 9339, 9242, 9340, 9243),
	RUNITE_BOLTS(CombatProjectile.getDefinition("Runite bolts"), true, 9144, 9291, 9298, 9305, 9341, 9244, 9342, 9245),
	
	BRUTAL_BRONZE_ARROW(CombatProjectile.getDefinition("Bronze brutal"), true, 4773),
	BRUTAL_IRON_ARROW(CombatProjectile.getDefinition("Iron brutal"), true, 4778),
	BRUTAL_BLACK_ARROW(CombatProjectile.getDefinition("Steel brutal"), true, 4783),
	BRUTAL_STEEL_ARROW(CombatProjectile.getDefinition("Black brutal"), true, 4788),
	BRUTAL_MITHRIL_ARROW(CombatProjectile.getDefinition("Mithril brutal"), true, 4793),
	BRUTAL_ADAMANT_ARROW(CombatProjectile.getDefinition("Adamant brutal"), true, 4798),
	BRUTAL_RUNE_ARROW(CombatProjectile.getDefinition("Rune brutal"), true, 4803),
	
	TOKTZ_XIL_UL(CombatProjectile.getDefinition("Toktz-xil-ul"), true, 6522),
	BONE_BOLT(CombatProjectile.getDefinition("Bone bolts"), true, 8882),
	OGRE_ARROW(CombatProjectile.getDefinition("Ogre arrow"), true, 2866),
	ICE_ARROW(CombatProjectile.getDefinition("Ice arrows"), true, 78),
	TRAINING_ARROWS(CombatProjectile.getDefinition("Training arrows"), true, 9706),
	BOLT_RACK(CombatProjectile.getDefinition("Bolt rack"), false, 4740),
	HAND_CANNON_SHOT(CombatProjectile.getDefinition("Hand cannon shot"), false, 15243),
	
	SARADOMIN_ARROW(CombatProjectile.getDefinition("Saradomin arrows"), false, 19152),
	GUTHIX_ARROW(CombatProjectile.getDefinition("Guthix arrows"), false, 19157),
	ZAMORAK_ARROW(CombatProjectile.getDefinition("Zamorak arrows"), false, 19162),
	
	BROAD_TIPPED_BOLTS(CombatProjectile.getDefinition("Broad-tipped bolts"), true, 13280),
	BROAD_ARROW(CombatProjectile.getDefinition("Broad arrow"), true, 4160),
	
	CHINCHOMPA(CombatProjectile.getDefinition("Chinchompa"), false, 10033),
	RED_CHINCHOMPA(CombatProjectile.getDefinition("Red chinchompa"), false, 10034),
	
	KEBBIT_BOLT(CombatProjectile.getDefinition("Kebbit bolts"), true, 10158),
	LONG_KEBBIT_BOLT(CombatProjectile.getDefinition("Long kebbit bolts"), true, 10159),
	
	GUAM_TAR(CombatProjectile.getDefinition("Guam tar"), false, 10142),
	MARRENTIL_TAR(CombatProjectile.getDefinition("Marrentill tar"), false, 10143),
	TARROMIN_TAR(CombatProjectile.getDefinition("Tarromin tar"), false, 10144),
	HARRALANDER_TAR(CombatProjectile.getDefinition("Harralander tar"), false, 10145),
	
	ABYSSALBANE_BOLT(CombatProjectile.getDefinition("Abyssalbane bolt"), true, 21675, 21701, 21702, 21703),
	ABYSSALBANE_ARROW(CombatProjectile.getDefinition("Abyssalbane arrow"), true, 21655, 21733, 21734, 21735),
	
	DRAGONBANE_BOLT(CombatProjectile.getDefinition("Dragonbane bolt"), true, 21660, 21680, 21681, 21682),
	DRAGONBANE_ARROW(CombatProjectile.getDefinition("Dragonbane arrow"), true, 21640, 21712, 21713, 21714),
	
	WALLASALKIBANE_BOLT(CombatProjectile.getDefinition("Wallasalkibane bolt"), true, 21665, 21694, 21695, 21696),
	WALLASALKIBANE_ARROW(CombatProjectile.getDefinition("Wallasalkibane arrow"), true, 21645, 21726, 21727, 21728),
	
	BASILISKBANE_BOLT(CombatProjectile.getDefinition("Basiliskbane bolt"), true, 21670, 21687, 21688, 21689),
	BASILISKBANE_ARROW(CombatProjectile.getDefinition("Basiliskbane arrow"), true, 21650, 21719, 21720, 21721),
	
	MORRIGAN_THROWNAXE(CombatProjectile.getDefinition("Morrigan's throwing axe"), true, 13883, 13957),
	MORRIGAN_JAVELIN(CombatProjectile.getDefinition("Morrigan's javelin"), true, 13879, 13880, 13881, 13882, 13953, 13954, 13955, 13956);
	
	private final CombatProjectile projectile;
	private final boolean droppable;
	private final int[] ids;
	
	RangedAmmunition(CombatProjectile projectile, boolean droppable, int... ids) {
		this.projectile = projectile;
		this.droppable = droppable;
		this.ids = ids;
	}
	
	public void sendProjectile(Actor attacker, Actor defender) {
		projectile.sendProjectile(attacker, defender, false);
		
		int id = 0;
		switch(this) {
			case DOUBLE_BRONZE_ARROW:
				id = 1104;
				break;
			case DOUBLE_IRON_ARROW:
				id = 1105;
				break;
			case DOUBLE_STEEL_ARROW:
				id = 1106;
				break;
			case DOUBLE_MITHRIL_ARROW:
				id = 1107;
				break;
			case DOUBLE_ADAMANT_ARROW:
				id = 1108;
				break;
			case DOUBLE_RUNE_ARROW:
				id = 1109;
				break;
			case DOUBLE_DRAGON_ARROW:
				id = 1110;
				break;
		}
		
		if(id > 0) {
			int speed = 59;
			Projectile projectile = new Projectile(attacker, defender, id, speed, 35, 43, 40, CombatType.RANGED);
			projectile.sendProjectile();
		}
	}
	
	public static RangedAmmunition find(Item item) {
		if(item == null)
			return null;
		for(RangedAmmunition ammo : values()) {
			for(int id : ammo.ids) {
				if(id == item.getId()) {
					return ammo;
				}
			}
		}
		return null;
	}
	
	public Optional<Animation> getAnimation() {
		return projectile.getAnimation();
	}
	
	public Optional<Graphic> getStart() {
		return projectile.getStart();
	}
	
	public Optional<Graphic> getEnd() {
		return projectile.getEnd();
	}
	
	public Optional<CombatImpact> getEffect() {
		return projectile.getEffect();
	}
	
	public boolean isDroppable() {
		return droppable;
	}
	
	public int getRemoval() {
		switch(this) {
			case DOUBLE_BRONZE_ARROW:
			case DOUBLE_IRON_ARROW:
			case DOUBLE_STEEL_ARROW:
			case DOUBLE_MITHRIL_ARROW:
			case DOUBLE_ADAMANT_ARROW:
			case DOUBLE_RUNE_ARROW:
			case DOUBLE_DRAGON_ARROW:
				return 2;
			default:
				return 1;
		}
	}
	
}
