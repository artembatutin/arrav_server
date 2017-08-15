package net.edge.content.newcombat.weapon;

import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.attack.AttackStyle;
import net.edge.net.packet.out.SendConfig;
import net.edge.world.entity.actor.player.Player;

public final class WeaponFactory {

    public static void updateAttackStyle(Player player, WeaponType type) {
        switch (type.getInterfaceId()) {
            case 776: reap(player); break;
            case 1698: battleaxe(player); break;
            case 2276: swordAndDagger(player); break;
            case 4705: longsword(player); break;
            case 2423: twoHandedSword(player); break;
            case 5570: pickaxe(player); break;
            case 3796: mace(player); break;
            case 7762: claws(player); break;
            case 4679: spear(player); break;
            case 6103: staff(player); break;
            case 328: magicStaff(player); break;
            case 8460: halbard(player); break;
            case 425: warhammar(player); break;
            case 5855: unarmed(player); break;
            case 12290: whip(player); break;
            case 1764: case 4446:
            case 24055: range(player); break;
        }

        player.out(new SendConfig(43, getAttackStyleConfig(player)));
    }

    private static WeaponAttackStyle getAttackStyle(WeaponType type) {
        switch (type.getInterfaceId()) {

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
        AttackStance attackType = player.getNewCombat().getAttackStance();

        switch (getAttackStyle(WeaponType.LONGSWORD_OR_SCIMITAR)) {
            case ACCURATE_AGGRESSIVE_DEFENSIVE:
                switch (attackType) {
                    case AGGRESSIVE: return 1;
                    case DEFENSIVE:  return 2;
                    default:         return 0;
                }
            case ACCURATE_CONTROLLED_DEFENSIVE:
                switch (attackType) {
                    case CONTROLLED: return 1;
                    case DEFENSIVE:  return 2;
                    default:         return 0;
                }
            case ACCURATE_AGGRESSIVE_CONTROLLED_DEFENSIVE:
                switch (attackType) {
                    case AGGRESSIVE: return 1;
                    case CONTROLLED: return 2;
                    case DEFENSIVE:  return 3;
                    default:         return 0;
                }
            case ACCURATE_AGGRESSIVE_AGGRESSIVE_DEFENSIVE:
                switch (attackType) {
                    case AGGRESSIVE: return 1;
                    case DEFENSIVE:  return 3;
                    default:         return 0;
                }
            case CONTROLLED_CONTROLLED_CONTROLLED_DEFENSIVE:
                switch (attackType) {
                    case DEFENSIVE:  return 3;
                    case CONTROLLED: return 0;
                    default:         return 0;
                }
            case CONTROLLED_AGGRESSIVE_DEFENSIVE:
                switch (attackType) {
                    case AGGRESSIVE: return 1;
                    case DEFENSIVE:  return 2;
                    default:         return 0;
                }
            case ACCURATE_RAPID_LONGRANGE:
                switch (attackType) {
                    case RAPID:     return 1;
                    case LONGRANGE: return 2;
                    default:        return 0;
                }
        }
        return 0;
    }

    private static void whip(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case AGGRESSIVE:
            case CONTROLLED:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.CONTROLLED);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
        }
    }

    private static void reap(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
        }
    }

    private static void battleaxe(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
        }
    }

    private static void swordAndDagger(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
        }
    }

    private static void longsword(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
        }
    }

    private static void twoHandedSword(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
        }
    }

    private static void pickaxe(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
        }
    }

    private static void mace(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
        }
    }

    private static void claws(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
        }
    }

    private static void staff(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStance(AttackStance.CONTROLLED);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.CONTROLLED);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
        }
    }

    private static void magicStaff(Player player) {
        unarmed(player);
    }

    private static void spear(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStance(AttackStance.CONTROLLED);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.CONTROLLED);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
        }
    }

    private static void halbard(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStance(AttackStance.CONTROLLED);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                break;
        }
    }

    private static void range(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStance(AttackStance.ACCURATE);
                player.getNewCombat().setAttackStyle(AttackStyle.RANGED);
                break;
            case CONTROLLED:
                player.getNewCombat().setAttackStance(AttackStance.LONGRANGE);
                player.getNewCombat().setAttackStyle(AttackStyle.RANGED);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.LONGRANGE);
                player.getNewCombat().setAttackStyle(AttackStyle.RANGED);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.RAPID);
                player.getNewCombat().setAttackStyle(AttackStyle.RANGED);
                break;
        }
    }

    private static void warhammar(Player player) {
        unarmed(player);
    }

    private static void unarmed(Player player) {
        switch (player.getNewCombat().getAttackStance()) {
            case ACCURATE:
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
            case CONTROLLED:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
            case DEFENSIVE:
            case LONGRANGE:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
            case AGGRESSIVE:
            case RAPID:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                break;
        }
    }

    public static boolean clickAttackStyle(Player player, int button) {
        switch (button) {

            case 782:
            case 783:
            case 1704:
            case 1705:
            case 1707:
            case 2284:
            case 2429:
            case 2430:
            case 2432:
            case 4688:
            case 4711:
            case 4712:
            case 4714:
            case 7768:
            case 7769:
            case 7771:
            case 8468:
            case 12296:
            case 12297:
            case 12298:
                player.getNewCombat().setAttackStyle(AttackStyle.SLASH);
                return true;

            case 334:
            case 335:
            case 336:
            case 431:
            case 432:
            case 433:
            case 785:
            case 1706:
            case 3802:
            case 3803:
            case 3805:
            case 4687:
            case 4713:
            case 5578:
            case 5860:
            case 5861:
            case 5862:
            case 6135:
            case 6136:
            case 6137:
                player.getNewCombat().setAttackStyle(AttackStyle.CRUSH);
                return true;

            case 784:
            case 2282:
            case 2283:
            case 2285:
            case 2431:
            case 3804:
            case 4685:
            case 4686:
            case 5576:
            case 5577:
            case 5579:
            case 7770:
            case 8466:
            case 8467:
                player.getNewCombat().setAttackStyle(AttackStyle.STAB);
                return true;

            case 1770:
            case 1771:
            case 1772:
            case 4452:
            case 4453:
            case 4454:
            case 24059:
            case 24060:
            case 24061:
                player.getNewCombat().setAttackStyle(AttackStyle.RANGED);
                return true;

            default:
                return false;
        }
    }

    public static boolean clickAttackStance(Player player, int button) {
        switch (button) {
            case 336:
            case 433:
            case 782:
            case 1772:
            case 1704:
            case 2429:
            case 2282:
            case 3802:
            case 4454:
            case 4711:
            case 5576:
            case 5860:
            case 6137:
            case 7768:
            case 12298:
            case 24059:
                player.getNewCombat().setAttackStance(AttackStance.ACCURATE);
                return true;

            case 335:
            case 432:
            case 784:
            case 785:
            case 1706:
            case 1707:
            case 2232:
            case 2284:
            case 2285:
            case 2432:
            case 3805:
            case 4713:
            case 4714:
            case 5578:
            case 5579:
            case 5862:
            case 6136:
            case 7771:
            case 8468:
                player.getNewCombat().setAttackStance(AttackStance.AGGRESSIVE);
                return true;

            case 334:
            case 431:
            case 783:
            case 1705:
            case 2430:
            case 2283:
            case 3803:
            case 4686:
            case 4712:
            case 5577:
            case 5861:
            case 6135:
            case 7769:
            case 8467:
            case 12296:
                player.getNewCombat().setAttackStance(AttackStance.DEFENSIVE);
                return true;

            case 2431:
            case 3804:
            case 4685:
            case 4687:
            case 4688:
            case 7770:
            case 8466:
            case 12297:
                player.getNewCombat().setAttackStance(AttackStance.CONTROLLED);
                return true;

            case 1771:
            case 4453:
            case 24060:
                player.getNewCombat().setAttackStance(AttackStance.RAPID);
                return true;

            case 1770:
            case 4452:
            case 24061:
                player.getNewCombat().setAttackStance(AttackStance.LONGRANGE);
                return true;

            default:
                return false;
        }
    }
}
