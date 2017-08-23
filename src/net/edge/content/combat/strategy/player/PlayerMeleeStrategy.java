package net.edge.content.combat.strategy.player;

import net.edge.content.combat.CombatType;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.effect.CombatPoisonEffect;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.basic.MeleeStrategy;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.container.impl.Equipment;

public class PlayerMeleeStrategy extends MeleeStrategy<Player> {

    public static final PlayerMeleeStrategy INSTANCE = new PlayerMeleeStrategy();

    protected PlayerMeleeStrategy() {
    }

    @Override
    public void start(Player attacker, Actor defender) {
        if (attacker.isSpecialActivated()) {
            attacker.getCombatSpecial().drain(attacker);
        }

        attacker.animation(getAttackAnimation(attacker, defender));
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit hit) {
        addCombatExperience(attacker, hit);
    }

    @Override
    public void hit(Player attacker, Actor defender, Hit hit) {
        CombatPoisonEffect.getPoisonType(attacker.getEquipment().get(Equipment.WEAPON_SLOT)).ifPresent(p -> {
            if(hit.isAccurate()) {
                defender.poison(p);
            }
        });
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        return new CombatHit[] { nextMeleeHit(attacker, defender) };
    }

    @Override
    public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
        return attacker.getAttackDelay();
    }

    @Override
    public int getAttackDistance(Player attacker, FightType fightType) {
        if (attacker.getWeapon() == WeaponInterface.HALBERD) return 2;
        return 1;
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        FightType fightType = attacker.getCombat().getFightType();
        int animation;

        if(attacker.getWeaponAnimation() != null && !attacker.getCombat().getFightType().isAnimationPrioritized()) {
            animation = attacker.getWeaponAnimation().getAttacking()[fightType.getStyle().ordinal()];
        } else {
            animation = fightType.getAnimation();
        }
        return new Animation(animation, Animation.AnimationPriority.HIGH);
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }
}
