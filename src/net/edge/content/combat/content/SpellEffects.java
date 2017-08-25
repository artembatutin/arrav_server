package net.edge.content.combat.content;

import net.edge.content.combat.CombatEffect;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.FormulaFactory;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.net.packet.out.SendMessage;
import net.edge.world.PoisonType;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

import java.util.LinkedList;
import java.util.List;

public enum SpellEffects {

    BIND((attacker, defender, hit, extra) -> freeze(defender, 5)),
    SNARE(((attacker, defender, hit, extra) -> freeze(defender, 10))),
    ENTANGLE(((attacker, defender, hit, extra) -> freeze(defender, 15))),

    CONFUSE((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.ATTACK, 5)),
    WEAKEN((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.STRENGTH, 5)),
    CURSE((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.DEFENCE, 5)),

    VULNERABILITY((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.DEFENCE, 10)),
    ENFEEBLE((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.STRENGTH, 10)),
    STUN((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.ATTACK, 10)),

    SMOKE_RUSH((attacker, defender, hit, extra) -> poison(attacker, defender, hit, PoisonType.DEFAULT_MAGIC)),
    SMOKE_BURST((attacker, defender, hit, extra) -> getSurrounding(attacker, defender).forEach(actor -> smokeBurst(attacker, defender, actor, hit, extra))),
    SMOKE_BLITZ((attacker, defender, hit, extra) -> poison(attacker, defender, hit, PoisonType.SUPER_MAGIC)),
    SMOKE_BARRAGE((attacker, defender, hit, extra) -> getSurrounding(attacker, defender).forEach(actor -> smokeBarrage(attacker, defender, actor, hit, extra))),

    SHADOW_RUSH((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.ATTACK, 10)),
    SHADOW_BURST((attacker, defender, hit, extra) -> getSurrounding(attacker, defender).forEach(actor -> shadowBurst(attacker, defender, actor, extra))),
    SHADOW_BLITZ((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.ATTACK, 15)),
    SHADOW_BARRAGE((attacker, defender, hit, extra) -> getSurrounding(attacker, defender).forEach(actor -> shadowBarrage(attacker, defender, actor, extra))),

    BLOOD_RUSH((attacker, defender, hit, extra) -> heal(attacker, hit)),
    BLOOD_BURST((attacker, defender, hit, extra) -> getSurrounding(attacker, defender).forEach(actor -> bloodBurst(attacker, defender, actor, hit, extra))),
    BLOOD_BLITZ((attacker, defender, hit, extra) -> heal(attacker, hit)),
    BLOOD_BARRAGE((attacker, defender, hit, extra) -> getSurrounding(attacker, defender).forEach(actor -> bloodBarrage(attacker, defender, actor, hit, extra))),

    ICE_RUSH((attacker, defender, hit, extra) -> freeze(defender, 5)),
    ICE_BURST((attacker, defender, hit, extra) -> getSurrounding(attacker, defender).forEach(actor -> iceBurst(attacker, defender, actor, extra))),
    ICE_BLITZ((attacker, defender, hit, extra) -> freeze(defender, 15)),
    ICE_BARRAGE((attacker, defender, hit, extra) -> getSurrounding(attacker, defender).forEach(actor -> iceBarrage(attacker, defender, actor, extra))),

    AHRIM_BLAST((attacker, defender, hit, extra) -> {
//        if (hit.isAccurate() && Math.random() < 0.20) {
//            defender.skills.get(Skills.STRENGTH).removeLevel(5);
//            defender.skills.refresh(Skills.STRENGTH);
//        }
    });

    private final CombatEffect effect;

    SpellEffects(CombatEffect effect) {
        this.effect = effect;
    }

    public CombatEffect getEffect() {
        return effect;
    }

    private static List<Actor> getSurrounding(Actor attacker, Actor defender) {
        List<Actor> actors = new LinkedList<>();
        actors.add(defender);

//        if (!attacker.inMulti() || !defender.inMulti())
//            return actors;

        for (Mob other : defender.getLocalMobs()) {
            if (other == null) continue;
            if (!other.getPosition().withinDistance(defender.getPosition(), 1)) continue;
            if (other.same(attacker) || other.same(defender)) continue;
            if (other.getCurrentHealth() <= 0 || other.isDead()) continue;
            if (!other.getDefinition().isAttackable()) continue;
//            if (!other.inMulti()) continue;
            actors.add(other);
        }

        for (Player other : defender.getLocalPlayers()) {
            if (other == null) continue;
            if (!other.getPosition().withinDistance(defender.getPosition(), 1)) continue;
            if (other.same(attacker) || other.same(defender)) continue;
            if (other.getCurrentHealth() <= 0 || other.isDead()) continue;
            if (!other.inMulti() || (attacker.isPlayer() && !other.inWilderness())) continue;
            actors.add(other);
        }

        return actors;
    }

    private static void lowerSkill(Actor defender, int id, int percentage) {
        if (!defender.isPlayer()) {
            return;
        }

        Player player = defender.toPlayer();
        Skill skill = player.getSkills()[id];

        double ratio = percentage / 100.0;
        int limit = (int) (skill.getRealLevel() * (1 - ratio));
        int amount = (int) (skill.getLevel() * ratio);

        if (skill.getLevel() - amount < limit) {
            amount = skill.getLevel() - limit;
        }

        if (amount > 0) {
            skill.decreaseLevel(amount);
        }
    }

    private static void poison(Actor attacker, Actor defender, Hit hit, PoisonType type) {
        if (defender.isPoisoned()) {
            return;
        }

        if (!attacker.isMob() && (!hit.isAccurate() || hit.getDamage() < 1)) {
            return;
        }

        defender.poison(type);
    }

    private static void freeze(Actor defender, int timer) {
        if (defender.isFrozen()) {
            return;
        }

        if (defender.isPlayer()) {
            defender.toPlayer().out(new SendMessage("You have been frozen!"));
        }

        defender.freeze(timer);
    }

    private static void heal(Actor attacker, Hit hit) {
        Skill skill = attacker.toPlayer().getSkills()[Skills.HITPOINTS];
        int current = attacker.getCurrentHealth();
        int real = skill.getRealLevel() * 10;

        if (current < real) {
            float heal = hit.getDamage() / 40f;

            if (heal + current > real) {
                heal = real - current;
            }

            attacker.healEntity((int) (heal * 10));
            System.out.println((int) (heal * 10));
        }
    }

    private static void smokeBurst(Actor attacker, Actor defender, Actor actor, Hit hit, List<Hit> extra) {
        poison(attacker, defender, hit, PoisonType.DEFAULT_MAGIC);

        CombatHit next = hitEvent(attacker, defender, actor, 18, extra);
        if (next != null && (attacker.isMob() || (next.isAccurate() && next.getDamage() > 0))) {
            poison(attacker, actor, hit, PoisonType.DEFAULT_MAGIC);
        }
    }

    private static void smokeBarrage(Actor attacker, Actor defender, Actor actor, Hit hit, List<Hit> extra) {
        poison(attacker, defender, hit, PoisonType.SUPER_MAGIC);

        CombatHit next = hitEvent(attacker, defender, actor, 27, extra);
        if (next != null && (attacker.isMob() || (next.isAccurate() && next.getDamage() > 0))) {
            poison(attacker, actor, hit, PoisonType.SUPER_MAGIC);
        }
    }

    private static void shadowBurst(Actor attacker, Actor defender, Actor actor, List<Hit> extra) {
        lowerSkill(defender, Skills.ATTACK, 10);

        CombatHit next = hitEvent(attacker, defender, actor, 19, extra);
        if (next != null && next.isAccurate()) {
            lowerSkill(actor, Skills.ATTACK, 10);
        }
    }

    private static void shadowBarrage(Actor attacker, Actor defender, Actor actor, List<Hit> extra) {
        lowerSkill(defender, Skills.ATTACK, 15);

        CombatHit next = hitEvent(attacker, defender, actor, 28, extra);
        if (next != null && next.isAccurate()) {
            lowerSkill(actor, Skills.ATTACK, 15);
        }
    }

    private static void bloodBurst(Actor attacker, Actor defender, Actor actor, Hit hit, List<Hit> extra) {
        heal(attacker, hit);
        CombatHit next = hitEvent(attacker, defender, actor, 21, extra);
        if (next != null && next.isAccurate()) {
            heal(attacker, next);
        }
    }

    private static void bloodBarrage(Actor attacker, Actor defender, Actor actor, Hit hit, List<Hit> extra) {
        heal(attacker, hit);
        CombatHit next = hitEvent(attacker, defender, actor, 29, extra);
        if (next != null && next.isAccurate()) {
            heal(attacker, next);
        }
    }

    private static void iceBurst(Actor attacker, Actor defender, Actor actor, List<Hit> extra) {
        freeze(defender, 10);
        CombatHit next = hitEvent(attacker, defender, actor, 22, extra);
        if (next != null && next.isAccurate()) {
            freeze(actor, 10);
        }
    }

    private static void iceBarrage(Actor attacker, Actor defender, Actor actor, List<Hit> extra) {
        freeze(defender, 20);

        CombatHit next = hitEvent(attacker, defender, actor, 28, extra);
        if (next != null && next.isAccurate()) {
            freeze(actor, 20);
        }
    }

    private static CombatHit hitEvent(Actor attacker, Actor defender, Actor actor, int max, List<Hit> extra) {
        if (!defender.same(actor)) {
            int hitDelay = CombatUtil.getHitDelay(attacker, actor, CombatType.MAGIC);
            int hitsplatDelay = CombatUtil.getHitsplatDelay(CombatType.MAGIC);
            CombatHit hit = new CombatHit(FormulaFactory.nextMagicHit(attacker, actor, max), hitDelay, hitsplatDelay);
            attacker.getCombat().submitHits(actor, hit);
            if (extra != null) extra.add(hit);
            return hit;
        }
        return null;
    }

}
