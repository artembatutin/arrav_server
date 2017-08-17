package net.edge.content.combat.content;

import net.edge.content.combat.CombatEffect;
import net.edge.content.combat.attack.FormulaFactory;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.skill.Skills;
import net.edge.net.packet.out.SendMessage;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

import java.util.LinkedList;
import java.util.List;

public enum ProjectileEffect {

    BIND(((attacker, defender, hit) -> freeze(defender, 5))),
    SNARE(((attacker, defender, hit) -> freeze(defender, 10))),
    ENTANGLE(((attacker, defender, hit) -> freeze(defender, 15))),

    CONFUSE(((attacker, defender, hit) -> lowerSkill(defender, Skills.ATTACK, 5))),
    WEAKEN(((attacker, defender, hit) -> lowerSkill(defender, Skills.STRENGTH, 5))),
    CURSE(((attacker, defender, hit) -> lowerSkill(defender, Skills.DEFENCE, 5))),

    VULNERABILITY(((attacker, defender, hit) -> lowerSkill(defender, Skills.DEFENCE, 10))),
    ENFEEBLE(((attacker, defender, hit) -> lowerSkill(defender, Skills.STRENGTH, 10))),
    STUN(((attacker, defender, hit) -> lowerSkill(defender, Skills.ATTACK, 10))),

    SMOKE_RUSH((attacker, defender, hit) -> poison(attacker, defender, hit, 2)),
    SMOKE_BURST((attacker, defender, hit) -> getSurrounding(attacker, defender).forEach(actor -> smokeBurst(attacker, actor))),
    SMOKE_BLITZ((attacker, defender, hit) -> poison(attacker, defender, hit, 4)),
    SMOKE_BARRAGE((attacker, defender, hit) -> getSurrounding(attacker, defender).forEach(actor -> smokeBarrage(attacker, actor))),

    SHADOW_RUSH((attacker, defender, hit) -> lowerSkill(defender, Skills.ATTACK, 10)),
    SHADOW_BURST((attacker, defender, hit) -> getSurrounding(attacker, defender).forEach(actor -> shadowBurst(attacker, actor))),
    SHADOW_BLITZ((attacker, defender, hit) -> lowerSkill(defender, Skills.ATTACK, 15)),
    SHADOW_BARRAGE((attacker, defender, hit) -> getSurrounding(attacker, defender).forEach(actor -> shadowBarrage(attacker, actor))),

    BLOOD_RUSH((attacker, defender, hit) -> heal(attacker, hit)),
    BLOOD_BURST((attacker, defender, hit) -> getSurrounding(attacker, defender).forEach(actor -> bloodBurst(attacker, actor))),
    BLOOD_BLITZ((attacker, defender, hit) -> heal(attacker, hit)),
    BLOOD_BARRAGE((attacker, defender, hit) -> getSurrounding(attacker, defender).forEach(actor -> bloodBarrage(attacker, actor))),

    ICE_RUSH((attacker, defender, hit) -> freeze(defender, 5)),
    ICE_BURST((attacker, defender, hit) -> getSurrounding(attacker, defender).forEach(actor -> iceBurst(attacker, actor))),
    ICE_BLITZ((attacker, defender, hit) -> freeze(defender, 15)),
    ICE_BARRAGE((attacker, defender, hit) -> getSurrounding(attacker, defender).forEach(actor -> iceBarrage(attacker, actor))),

    AHRIM_BLAST(((attacker, defender, hit) -> {
//        if (hit.isAccurate() && Math.random() < 0.20) {
//            defender.skills.get(Skills.STRENGTH).removeLevel(5);
//            defender.skills.refresh(Skills.STRENGTH);
//        }
    }))

    ;

    private final CombatEffect effect;

    ProjectileEffect(CombatEffect effect) {
        this.effect = effect;
    }

    public CombatEffect getEffect() {
        return effect;
    }

    private static List<Actor> getSurrounding(Actor attacker, Actor defender) {
//        TODO: Fix surrounding entities #ProjectileEffects
        List<Actor> actors;

//        if (!Area.inMultiCombatZone(attacker) || !Area.inMultiCombatZone(defender)) {
            actors = new LinkedList<>();
            actors.add(defender);
//        } else actors = Utility.getSurroundingactors(attacker, defender, 3);
//        actors.removeIf(Actor -> !Area.inMultiCombatZone(actor));
        return actors;
    }

    private static void lowerSkill(Actor actor, int id, int percentage) {
//        TODO: Fix loweing skills #ProjectileEffects
//        Skill skill = actor.skills.get(id);
//
//        double ratio = percentage / 100.0;
//        int limit = (int) (skill.getMaxLevel() * (1 - ratio));
//        int amount = (int) (skill.getLevel() * ratio);
//
//        if (skill.getLevel() - amount < limit) {
//            amount = skill.getLevel() - limit;
//        }
//
//        if (amount > 0) {
//            skill.removeLevel(amount);
//        }
    }

    private static void poison(Actor attacker, Actor actor, Hit hit, int strength) {
        if (attacker.isMob() || hit.getDamage() > 0) {
            actor.getNewCombat().poison(strength);
        }
    }

    private static void freeze(Actor actor, int timer) {
        if (!actor.getMovementQueue().isLockMovement()) {
            if (actor.isPlayer()) {
                actor.toPlayer().out(new SendMessage("You have been frozen!"));
            }

            actor.getMovementQueue().reset();
        }
    }

    private static void heal(Actor actor, Hit hit) {
        if (actor.getCurrentHealth() < actor.getSkillLevel(Skills.HITPOINTS)) {
            int heal = hit.getDamage() / 4;

            if (heal + actor.getCurrentHealth() > actor.getSkillLevel(Skills.HITPOINTS)) {
                heal = actor.getSkillLevel(Skills.HITPOINTS) - actor.getCurrentHealth();
            }

            actor.healEntity(heal);
        }
    }

    private static void smokeBurst(Actor attacker, Actor actor) {
        CombatHit hit = new CombatHit(FormulaFactory.nextMagicHit(attacker, actor, 18), 2, 1);
        hitEvent(attacker, actor, hit);
        poison(attacker, actor, hit, 2);
    }

    private static void smokeBarrage(Actor attacker, Actor actor) {
        CombatHit hit = new CombatHit(FormulaFactory.nextMagicHit(attacker, actor, 27), 2, 0);
        hitEvent(attacker, actor, hit);
        poison(attacker, actor, hit, 4);
    }

    private static void shadowBurst(Actor attacker, Actor actor) {
        CombatHit hit = new CombatHit(FormulaFactory.nextMagicHit(attacker, actor, 19), 2, 1);
        hitEvent(attacker, actor, hit);
        lowerSkill(actor, Skills.ATTACK, 10);
    }

    private static void shadowBarrage(Actor attacker, Actor actor) {
        CombatHit hit = new CombatHit(FormulaFactory.nextMagicHit(attacker, actor, 28), 2, 0);
        hitEvent(attacker, actor, hit);
        lowerSkill(actor, Skills.ATTACK, 15);
    }

    private static void bloodBurst(Actor attacker, Actor actor) {
        CombatHit hit = new CombatHit(FormulaFactory.nextMagicHit(attacker, actor, 21), 2, 1);
        hitEvent(attacker, actor, hit);
        heal(attacker, hit);
    }

    private static void bloodBarrage(Actor attacker, Actor actor) {
        CombatHit hit = new CombatHit(FormulaFactory.nextMagicHit(attacker, actor, 29), 2, 0);
        hitEvent(attacker, actor, hit);
        heal(attacker, hit);
    }

    private static void iceBurst(Actor attacker, Actor actor) {
        CombatHit hit = new CombatHit(FormulaFactory.nextMagicHit(attacker, actor, 22), 2, 1);
        hitEvent(attacker, actor, hit);
        freeze(actor, 10);
    }

    private static void iceBarrage(Actor attacker, Actor actor) {
        CombatHit hit = new CombatHit(FormulaFactory.nextMagicHit(attacker, actor, 30), 2, 0);
        hitEvent(attacker, actor, hit);
        freeze(actor, 20);
    }

    private static void hitEvent(Actor attacker, Actor actor, CombatHit hit) {
        if (attacker.isPlayer()) {
            Player player = (Player) attacker;
            Actor defender = player.getNewCombat().getDefender();

            if (!defender.equals(actor)) {
                player.getNewCombat().submitHits(actor, hit);
            }
        }
    }

}
