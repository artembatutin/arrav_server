package net.edge.content.combat.weapon;

import net.edge.content.combat.attack.FightStyle;
import net.edge.content.combat.attack.FightType;
import net.edge.net.packet.out.SendConfig;
import net.edge.world.entity.actor.player.Player;

public final class WeaponFactory {

	public static void updateAttackStyle(Player player, WeaponInterface type) {
		switch(type) {
			case SCYTHE:
				reap(player);
				break;
			case BATTLEAXE:
				battleaxe(player);
				break;
			case SWORD:
				sword(player);
				break;
			case DAGGER:
				dagger(player);
				break;
			case SCIMITAR:
				scimitar(player);
				break;
			case LONGSWORD:
				longsword(player);
				break;
			case TWO_HANDED_SWORD:
				twoHandedSword(player);
				break;
			case PICKAXE:
				pickaxe(player);
				break;
			case MACE:
				mace(player);
				break;
			case CLAWS:
				claws(player);
				break;
			case SPEAR:
				spear(player);
				break;
			case STAFF:
				staff(player);
				break;
			case MAGIC_STAFF:
				magicStaff(player);
				break;
			case HALBERD:
				halbard(player);
				break;
			case WARHAMMER:
				warhammar(player);
				break;
			case UNARMED:
				unarmed(player);
				break;
			case WHIP:
				whip(player);
				break;
			case JAVELIN:
				javelin(player);
				break;
			case DART:
				dart(player);
				break;
			case KNIFE:
				knife(player);
				break;
			case CROSSBOW:
				crossbow(player);
				break;
			case SHORTBOW:
				shortbow(player);
				break;
			case COMPOSITE_BOW:
			case LONGBOW:
				longbow(player);
				break;
			case THROWNAXE:
				thrownaxe(player);
				break;
			case CHINCHOMPA:
				chinchompa(player);
				break;
			case SALAMANDER:
				salamander(player);
				break;
		}
		player.out(new SendConfig(43, getAttackStyleConfig(player)));
	}

	private static void chinchompa(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.SHORT_FUSE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.LONG_FUSE);
				break;
			case AGGRESSIVE:
			case CONTROLLED:
				player.getCombat().setFightType(FightType.MEDIUM_FUSE);
				break;
		}
	}

	private static void thrownaxe(Player player) {
	}

	private static void longbow(Player player) {
	}

	private static void shortbow(Player player) {
	}

	private static void crossbow(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.CROSSBOW_ACCURATE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.CROSSBOW_LONGRANGE);
				break;
			case AGGRESSIVE:
			case CONTROLLED:
				player.getCombat().setFightType(FightType.CROSSBOW_RAPID);
				break;
		}
	}

	private static void knife(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.KNIFE_ACCURATE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.KNIFE_LONGRANGE);
				break;
			case AGGRESSIVE:
			case CONTROLLED:
				player.getCombat().setFightType(FightType.KNIFE_RAPID);
				break;
		}
	}

	private static void dart(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.DART_ACCURATE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.DART_LONGRANGE);
				break;
			case AGGRESSIVE:
			case CONTROLLED:
				player.getCombat().setFightType(FightType.DART_RAPID);
				break;
		}
	}

	private static void javelin(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.JAVELIN_ACCURATE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.JAVELIN_LONGRANGE);
				break;
			case AGGRESSIVE:
			case CONTROLLED:
				player.getCombat().setFightType(FightType.JAVELIN_RAPID);
				break;
		}
	}

	private static void salamander(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.FLARE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.BLAZE);
				break;
			case AGGRESSIVE:
			case CONTROLLED:
				player.getCombat().setFightType(FightType.SCORCH);
				break;
		}
	}

	private static WeaponAttackStyle getAttackStyle(WeaponInterface type) {
		switch(type.getId()) {
			case 328:
			case 425:
			case 5855:
			case 6103:
				return WeaponAttackStyle.ACCURATE_AGGRESSIVE_DEFENSIVE;
			case 12290:
				return WeaponAttackStyle.ACCURATE_CONTROLLED_DEFENSIVE;
			case 776:
			case 1698:
			case 2276:
			case 4705:
			case 5570:
				return WeaponAttackStyle.ACCURATE_AGGRESSIVE_AGGRESSIVE_DEFENSIVE;
			case 2423:
			case 3796:
			case 7762:
				return WeaponAttackStyle.ACCURATE_AGGRESSIVE_CONTROLLED_DEFENSIVE;
			case 4679:
				return WeaponAttackStyle.CONTROLLED_CONTROLLED_CONTROLLED_DEFENSIVE;
			case 8460:
				return WeaponAttackStyle.CONTROLLED_AGGRESSIVE_DEFENSIVE;
			case 1764:
			case 4446:
			case 24055:
				return WeaponAttackStyle.ACCURATE_RAPID_LONGRANGE;
		}
		return WeaponAttackStyle.ACCURATE_AGGRESSIVE_DEFENSIVE;
	}

	private static int getAttackStyleConfig(Player player) {
		FightStyle fightStyle = player.getCombat().getFightType().getStyle();
		switch(getAttackStyle(player.getWeapon())) {
			case ACCURATE_AGGRESSIVE_DEFENSIVE:
				switch(fightStyle) {
					case AGGRESSIVE:
						return 1;
					case DEFENSIVE:
						return 2;
					default:
						return 0;
				}
			case ACCURATE_CONTROLLED_DEFENSIVE:
				switch(fightStyle) {
					case CONTROLLED:
						return 1;
					case DEFENSIVE:
						return 2;
					default:
						return 0;
				}
			case ACCURATE_AGGRESSIVE_CONTROLLED_DEFENSIVE:
				switch(fightStyle) {
					case AGGRESSIVE:
						return 1;
					case CONTROLLED:
						return 2;
					case DEFENSIVE:
						return 3;
					default:
						return 0;
				}
			case ACCURATE_AGGRESSIVE_AGGRESSIVE_DEFENSIVE:
				switch(fightStyle) {
					case AGGRESSIVE:
						return 1;
					case DEFENSIVE:
						return 3;
					default:
						return 0;
				}
			case CONTROLLED_CONTROLLED_CONTROLLED_DEFENSIVE:
				switch(fightStyle) {
					case DEFENSIVE:
						return 3;
					case CONTROLLED:
						return 0;
					default:
						return 0;
				}
			case CONTROLLED_AGGRESSIVE_DEFENSIVE:
				switch(fightStyle) {
					case AGGRESSIVE:
						return 1;
					case DEFENSIVE:
						return 2;
					default:
						return 0;
				}
			case ACCURATE_RAPID_LONGRANGE:
				switch(fightStyle) {
					case AGGRESSIVE:
						return 1;
					case DEFENSIVE:
						return 2;
					default:
						return 0;
				}
		}
		return 0;
	}

	private static void whip(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.WHIP_FLICK);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.WHIP_DEFLECT);
				break;
			case AGGRESSIVE:
			case CONTROLLED:
				player.getCombat().setFightType(FightType.WHIP_LASH);
				break;
		}
	}

	private static void reap(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.SCYTHE_REAP);
				break;
			case CONTROLLED:
				player.getCombat().setFightType(FightType.SCYTHE_JAB);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.SCYTHE_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.SCYTHE_CHOP);
				break;
		}
	}

	private static void battleaxe(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.BATTLEAXE_CHOP);
				break;
			case CONTROLLED:
				player.getCombat().setFightType(FightType.BATTLEAXE_HACK);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.BATTLEAXE_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.BATTLEAXE_SMASH);
				break;
		}
	}

	private static void sword(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.SWORD_STAB);
				break;
			case CONTROLLED:
				player.getCombat().setFightType(FightType.SWORD_LUNGE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.SWORD_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.SWORD_SLASH);
				break;
		}
	}

	private static void scimitar(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.SCIMITAR_CHOP);
				break;
			case CONTROLLED:
				player.getCombat().setFightType(FightType.SCIMITAR_LUNGE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.SCIMITAR_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.SCIMITAR_SLASH);
				break;
		}
	}

	private static void dagger(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.DAGGER_STAB);
				break;
			case CONTROLLED:
				player.getCombat().setFightType(FightType.DAGGER_LUNGE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.DAGGER_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.DAGGER_SLASH);
				break;
		}
	}

	private static void longsword(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.LONGSWORD_CHOP);
				break;
			case CONTROLLED:
				player.getCombat().setFightType(FightType.LONGSWORD_LUNGE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.LONGSWORD_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.LONGSWORD_SLASH);
				break;
		}
	}

	private static void twoHandedSword(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.TWOHANDEDSWORD_CHOP);
				break;
			case CONTROLLED:
				player.getCombat().setFightType(FightType.TWOHANDEDSWORD_SMASH);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.TWOHANDEDSWORD_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.TWOHANDEDSWORD_SLASH);
				break;
		}
	}

	private static void pickaxe(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.PICKAXE_SPIKE);
				break;
			case CONTROLLED:
				player.getCombat().setFightType(FightType.PICKAXE_SMASH);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.PICKAXE_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.PICKAXE_IMPALE);
				break;
		}
	}

	private static void mace(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.MACE_POUND);
				break;
			case CONTROLLED:
				player.getCombat().setFightType(FightType.MACE_SPIKE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.MACE_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.MACE_PUMMEL);
				break;
		}
	}

	private static void claws(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.CLAWS_CHOP);
				break;
			case CONTROLLED:
				player.getCombat().setFightType(FightType.CLAWS_LUNGE);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.CLAWS_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.CLAWS_SLASH);
				break;
		}
	}

	private static void staff(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.STAFF_BASH);
				break;
			case CONTROLLED:
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.STAFF_FOCUS);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.STAFF_POUND);
				break;
		}
	}

	private static void magicStaff(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.STAFF_BASH);
				break;
			case CONTROLLED:
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.STAFF_FOCUS);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.STAFF_POUND);
				break;
		}
	}

	private static void spear(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.SPEAR_LUNGE);
				break;
			case CONTROLLED:
				player.getCombat().setFightType(FightType.SPEAR_POUND);
				break;
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.SPEAR_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.SPEAR_SWIPE);
				break;
		}
	}

	private static void halbard(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.HALBERD_SWIPE);
				break;
			case CONTROLLED:
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.HALBERD_FEND);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.HALBERD_SWIPE);
				break;
		}
	}

	private static void warhammar(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.WARHAMMER_POUND);
			case CONTROLLED:
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.WARHAMMER_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.WARHAMMER_PUMMEL);
				break;
		}
	}

	private static void unarmed(Player player) {
		switch(player.getCombat().getFightType().getStyle()) {
			case ACCURATE:
				player.getCombat().setFightType(FightType.UNARMED_PUNCH);
			case CONTROLLED:
			case DEFENSIVE:
				player.getCombat().setFightType(FightType.UNARMED_BLOCK);
				break;
			case AGGRESSIVE:
				player.getCombat().setFightType(FightType.UNARMED_KICK);
				break;
		}
	}

}
