package net.edge.content.combat.strategy.player;

import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.effect.CombatPoisonEffect;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.CombatType;
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
    public void attack(Player attacker, Actor defender, Hit hit) {
        attacker.animation(getAttackAnimation(attacker, defender));
        addCombatExperience(attacker, hit);
    }

    @Override
    public void hit(Player attacker, Actor defender, Hit hit) {
        if (hit.getDamage() > 0) {
            defender.poison(CombatPoisonEffect.getPoisonType(attacker.getEquipment().get(Equipment.WEAPON_SLOT)).orElse(null));
        }
    }

    @Override
    public void block(Actor attacker, Player defender, Hit hit) {
        defender.animation(getBlockAnimation(defender, attacker));
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        return new CombatHit[]{nextMeleeHit(attacker, defender, 1, CombatUtil.getDelay(attacker, defender, getCombatType())) };
    }

    @Override
    public int getAttackDelay(Player attacker, FightType fightType) {
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
        int animation = attacker.getWeaponAnimation().getAttacking()[fightType.getStyle().ordinal()];
        return new Animation(animation, Animation.AnimationPriority.HIGH);
    }

    @Override
    public Animation getBlockAnimation(Player player, Actor defender) {
        int animation = 404;
        if (player.getShieldAnimation() != null) {
            animation = player.getShieldAnimation().getBlock();
        } else if (player.getWeaponAnimation() != null) {
            animation = player.getWeaponAnimation().getBlocking();
        }
        return new Animation(animation, Animation.AnimationPriority.LOW);
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }
}
