package net.edge.world.entity.actor.combat.ranged;

import com.google.common.collect.ImmutableSet;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Graphic;
import net.edge.world.PoisonType;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatImpact;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.attack.FormulaFactory;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.hit.HitIcon;
import net.edge.world.entity.actor.player.Player;

import java.util.List;

import static net.edge.world.entity.actor.combat.CombatUtil.*;

public enum RangedEffects {
    OPAL_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.inclusive(100) <= 10;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            int baseHit = hit.getDamage();
            hit.setDamage((int) (baseHit * 1.10));
            defender.graphic(new Graphic(749, 0, 20));
        }
    }),
    PEARL_BOLTS(new CombatImpact() {
        final ImmutableSet<Integer> affectedMobs = ImmutableSet.of(941, 55, 54, 53, 50, 5362, 1590, 1591, 1592, 5363, 110, 1633, 1634, 1635, 1636, 1019, 2591, 2592, 2593, 2594, 2595, 2596, 2597, 2598, 2599, 2600, 2601, 2602, 2603, 2604, 2605, 2606, 2607, 2608, 2609, 2610, 2611, 2612, 2613, 2614, 2615, 2616, 2627, 2628, 2629, 2630, 2631, 2631, 2734, 2735, 2736, 2737, 2738, 2739, 2740, 2741, 2742, 2743, 2744, 2745, 2746);

        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            if (defender.isPlayer() && defender.toPlayer().getEquipment().containsAny(1383, 1395, 1403)) {
                return false;
            }
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.inclusive(100) <= 10;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            boolean multiply = false;
            int baseHit = hit.getDamage();

            if (defender.isMob() && affectedMobs.contains(defender.toMob().getId())) {
                multiply = true;
            }

            if (defender.isPlayer() && defender.toPlayer().getEquipment().containsAny(1387, 1393, 1401)) {
                multiply = true;
            }

            hit.setDamage((int) ((baseHit * 1.075) * (multiply ? 1.20 : 1.0)));
            defender.graphic(new Graphic(750, 0, 20));
        }
    }),
    JADE_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.inclusive(100) <= 10;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            defender.stun(1_200);
            defender.graphic(new Graphic(755, 0, 20));
        }
    }),
    TOPAZ_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            if (!defender.isPlayer())
                return false;
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.inclusive(100) <= 10;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            defender.toPlayer().getSkills()[Skills.MAGIC].decreaseLevel(1);
            defender.graphic(new Graphic(757, 0, 20));
        }
    }),
    SAPPHIRE_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            if (!defender.isPlayer())
                return false;
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.inclusive(100) <= 10;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            int baseHit = hit.getDamage();
            int percentage = (int) (baseHit * 0.05);
            defender.toPlayer().getSkills()[Skills.PRAYER].decreaseLevel(percentage);
            attacker.toPlayer().getSkills()[Skills.PRAYER].increaseLevel(percentage, attacker.toPlayer().getSkills()[Skills.PRAYER].getRealLevel());
            defender.graphic(new Graphic(759, 100, 20));
        }
    }),
    EMERALD_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            if (defender.isPoisoned())
                return false;
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.inclusive(100) <= 10;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            defender.poison(PoisonType.SUPER_RANGED);
        }
    }),
    RUBY_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            if ((int) (attacker.getCurrentHealth() * 0.20) <= 0)
                return false;
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.inclusive(100) <= 10;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            int damage = (int) (defender.getCurrentHealth() * 0.20);
            int inflict = (int) (defender.getCurrentHealth() * 0.10);
            hit.setDamage(hit.getDamage() + damage);
            attacker.damage(new Hit(inflict, HitIcon.DEFLECT));

            defender.graphic(new Graphic(754, 0, 20));
        }
    }),
    DIAMOND_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            if ((int) (attacker.getCurrentHealth() * 0.20) <= 0)
                return false;
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.inclusive(100) <= 10;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            hit.setDamage(hit.getDamage() + RandomUtils.inclusive(5, 14));
            defender.graphic(new Graphic(758, 0, 20));
        }
    }),
    DRAGON_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            if (defender.isPlayer() && defender.toPlayer().getEquipment().containsAny(1540, 11283)) {
                return false;
            }

            if (defender.isPlayer() && defender.toPlayer().getAntifireDetails().isPresent()) {
                return false;
            }

            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.inclusive(100) <= 10;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            int baseHit = hit.getDamage();
            hit.setDamage((int) (baseHit * 1.14));
            defender.graphic(new Graphic(756, 0, 20));
        }
    }),
    ONYX_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.inclusive(100) <= 10;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            //TODO spell shouldn't work on undead
            int damage = (int) (hit.getDamage() * (RandomUtils.inclusive(20, 25) / 100D));
            hit.setDamage(damage);
            attacker.healEntity(damage / 4);
            defender.graphic(new Graphic(756, 0, 20));
        }
    }),

    GUAM_TAR(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            return attacker.isPlayer();
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            Player player = attacker.toPlayer();
            FightType fightType = player.getCombat().getFightType();
            CombatType type = fightType.equals(FightType.FLARE) ? CombatType.RANGED : fightType.equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;

            if (type.equals(CombatType.MAGIC)) {
                Skill magic = player.getSkills()[Skills.MAGIC];
                int max = (int) (Math.floor(0.5 + magic.getLevel() * 15 / 80) * 10);
                Hit magicHit = FormulaFactory.nextMagicHit(attacker, defender, max);
                hit.setAs(magicHit);
            } else if (type.equals(CombatType.MELEE)) {
                Hit magicHit = FormulaFactory.nextMeleeHit(attacker, defender);
                hit.setAs(magicHit);
            }
        }
    }),
    MARRENTIL_TAR(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            return attacker.isPlayer();
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            Player player = attacker.toPlayer();
            FightType fightType = player.getCombat().getFightType();
            CombatType type = fightType.equals(FightType.FLARE) ? CombatType.RANGED : fightType.equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;

            if (type.equals(CombatType.MAGIC)) {
                Skill magic = player.getSkills()[Skills.MAGIC];
                int max = (int) (Math.floor(0.5 + magic.getLevel() * 123 / 640) * 10);
                Hit magicHit = FormulaFactory.nextMagicHit(attacker, defender, max);
                hit.setAs(magicHit);
            } else if (type.equals(CombatType.MELEE)) {
                Hit magicHit = FormulaFactory.nextMeleeHit(attacker, defender);
                hit.setAs(magicHit);
            }
        }
    }),
    TARROMIN_TAR(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            return attacker.isPlayer();
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            Player player = attacker.toPlayer();
            FightType fightType = player.getCombat().getFightType();
            CombatType type = fightType.equals(FightType.FLARE) ? CombatType.RANGED : fightType.equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;

            if (type.equals(CombatType.MAGIC)) {
                Skill magic = player.getSkills()[Skills.MAGIC];
                int max = (int) (Math.floor(0.5 + magic.getLevel() * 141 / 640) * 10);
                Hit magicHit = FormulaFactory.nextMagicHit(attacker, defender, max);
                hit.setAs(magicHit);
            } else if (type.equals(CombatType.MELEE)) {
                Hit magicHit = FormulaFactory.nextMeleeHit(attacker, defender);
                hit.setAs(magicHit);
            }
        }
    }),
    HARRALANDER_TAR(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            return attacker.isPlayer();
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            Player player = attacker.toPlayer();
            FightType fightType = player.getCombat().getFightType();
            CombatType type = fightType.equals(FightType.FLARE) ? CombatType.RANGED : fightType.equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;

            if (type.equals(CombatType.MAGIC)) {
                Skill magic = player.getSkills()[Skills.MAGIC];
                int max = (int) (Math.floor(0.5 + magic.getLevel() * 156 / 640) * 10);
                Hit magicHit = FormulaFactory.nextMagicHit(attacker, defender, max);
                hit.setAs(magicHit);
            } else if (type.equals(CombatType.MELEE)) {
                Hit magicHit = FormulaFactory.nextMeleeHit(attacker, defender);
                hit.setAs(magicHit);
            }
        }
    }),

    ABYSSALBANE(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            return defender.isMob() && defender.toMob().getId() == 1615 && hit.getDamage() > 0;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            int baseHit = hit.getDamage();
            hit.setDamage((int) (baseHit * 1.40));
        }
    }),
    BASILISKBANE(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            return defender.isMob() && defender.toMob().getId() == 1616 && hit.getDamage() > 0;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            int baseHit = hit.getDamage();
            hit.setDamage((int) (baseHit * 1.40));
        }
    }),
    DRAGONBANE(new CombatImpact() {
        final ImmutableSet<Integer> affectedMobs = ImmutableSet.of(941, 55, 54, 53, 50, 5362, 1590, 1591, 1592, 5363);

        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            return defender.isMob() && affectedMobs.contains(defender.toMob().getId()) && hit.getDamage() > 0;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            int baseHit = hit.getDamage();
            hit.setDamage((int) (baseHit * 1.40));
        }
    }),
    WALLASALKIBANE(new CombatImpact() {
        @Override
        public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
            return defender.isMob() && defender.toMob().getId() == 2457 && hit.getDamage() > 0;
        }

        @Override
        public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
            int baseHit = hit.getDamage();
            hit.setDamage((int) (baseHit * 1.40));
        }
    }),

    CHINCHOMPA((attacker, defender, hit, hits) -> {
        int baseHit = hit.getDamage();
        areaAction(attacker, defender,
            actor -> {
                int min = baseHit - 50;
                if (min < 0) min = 0;
                hitEvent(attacker, defender, actor, RandomUtils.inclusive(min, baseHit + 50), hits);
            });
    }),
    RED_CHINCHOMPA((attacker, defender, hit, hits) -> {
        areaAction(attacker, defender,
                actor -> hitEvent(attacker, defender, actor, hit.getDamage(), hits));
    });

    private final CombatImpact effect;

    RangedEffects(CombatImpact effect) {
        this.effect = effect;
    }

    public CombatImpact getEffect() {
        return effect;
    }

    private static void hitEvent(Actor attacker, Actor defender, Actor actor, int damage, List<Hit> extra) {
        if (!defender.same(actor)) {
            int hitDelay = getHitDelay(attacker, defender, CombatType.RANGED);
            int hitsplatDelay = getHitsplatDelay(CombatType.RANGED);
            CombatHit hit = new CombatHit(new Hit(damage, HitIcon.RANGED), hitDelay, hitsplatDelay);
            attacker.getCombat().submitHits(actor, hit);
            if (extra != null) extra.add(hit);
        }
    }

}
