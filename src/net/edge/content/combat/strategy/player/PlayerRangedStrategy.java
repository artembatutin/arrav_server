package net.edge.content.combat.strategy.player;

import net.edge.content.combat.*;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.effect.CombatPoisonEffect;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.basic.RangedStrategy;
import net.edge.content.combat.weapon.RangedAmmunition;
import net.edge.content.combat.weapon.RangedWeaponDefinition;
import net.edge.content.item.Requirement;
import net.edge.net.packet.out.SendMessage;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Equipment;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PlayerRangedStrategy extends RangedStrategy<Player> {
    private final RangedWeaponDefinition rangedDefinition;
    private CombatProjectileDefinition projectileDefinition;
    private Item ammunition;

    public PlayerRangedStrategy(RangedWeaponDefinition definition) {
        this.rangedDefinition = Objects.requireNonNull(definition);
    }

    @Override
    public boolean canAttack(Player attacker, Actor defender) {
        if (!Requirement.canEquip(attacker, attacker.getEquipment().get(Equipment.WEAPON_SLOT))) {
            attacker.getCombat().reset();
            return false;
        }

        Item ammo = attacker.getEquipment().get(rangedDefinition.getSlot());

        if (ammo != null) {
            if (rangedDefinition.getAllowed() == null) {
                attacker.getCombat().reset();
                return false;
            }

            for (RangedAmmunition data : rangedDefinition.getAllowed()) {
                for (int id : data.ammo) {
                    if (id == ammo.getId()) {
                        ammunition = ammo;
                        projectileDefinition = CombatProjectileDefinition.getDefinition(ammo.getName());
                        return true;
                    }
                }
            }

            attacker.out(new SendMessage(getInvalidAmmunitionMessage(rangedDefinition.getType())));
        } else attacker.out(new SendMessage(getNoAmmunitionMessage(rangedDefinition.getType())));
        attacker.getCombat().reset();
        return false;
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit hit) {
        attacker.animation(getAttackAnimation(attacker, defender));
        projectileDefinition.getStart().ifPresent(attacker::graphic);
        projectileDefinition.sendProjectile(attacker, defender, false);
        removeAmmunition(attacker, defender, rangedDefinition.getType());
        addCombatExperience(attacker, hit);
    }

    @Override
    public void hit(Player attacker, Actor defender, Hit hit) {
        Predicate<CombatEffect> filter = effect -> effect.canEffect(attacker, defender, hit);
        Consumer<CombatEffect> execute = effect -> effect.execute(attacker, defender, hit);
        projectileDefinition.getEffect().filter(Objects::nonNull).filter(filter).ifPresent(execute);

        if (hit.getDamage() > 0) {
            defender.poison(CombatPoisonEffect.getPoisonType(attacker.getEquipment().get(rangedDefinition.getSlot())).orElse(null));
        }
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        if (attacker.getWeaponAnimation() != null && attacker.getWeaponAnimation().getAttacking()[0] != 422) {
            return new Animation(attacker.getWeaponAnimation().getAttacking()[attacker.getCombat().getFightType().getStyle().ordinal()], Animation.AnimationPriority.HIGH);
        }
        return new Animation(attacker.getCombat().getFightType().getAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    public int getAttackDelay(Player attacker, FightType fightType) {
        return attacker.getAttackDelay();
    }

    @Override
    public int getAttackDistance(Player attacker, FightType fightType) {
        int distance = CombatUtil.getRangedDistance(attacker.getWeapon());
        switch (fightType) {
            case CROSSBOW_LONGRANGE:
            case DART_LONGRANGE:
            case JAVELIN_LONGRANGE:
            case KNIFE_LONGRANGE:
            case LONGBOW_LONGRANGE:
            case SHORTBOW_LONGRANGE:
            case THROWNAXE_LONGRANGE:
                return (distance += 2) > 10 ? 10 : distance;
        }
        return distance;
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        Item arrows = attacker.getEquipment().get(Equipment.ARROWS_SLOT);
        int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
        int hitsplatDelay = CombatUtil.getHitsplatDelay(attacker, defender);

        if (rangedDefinition.getType() == RangedWeaponDefinition.AttackType.THROWN && arrows != null) {
            int bonus = arrows.getDefinition().getBonus()[CombatConstants.BONUS_RANGED_STRENGTH];
            attacker.appendBonus(Equipment.ARROWS_SLOT, -bonus);
            CombatHit hit = nextRangedHit(attacker, defender, hitDelay, hitsplatDelay);
            attacker.appendBonus(Equipment.ARROWS_SLOT, arrows.getDefinition().getBonus()[CombatConstants.BONUS_RANGED_STRENGTH]);
            return new CombatHit[] { hit };
        }

        return new CombatHit[] { nextRangedHit(attacker, defender, hitDelay, hitsplatDelay) };
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.RANGED;
    }

    private void removeAmmunition(Player attacker, Actor defender, RangedWeaponDefinition.AttackType type) {
        Item next = attacker.getEquipment().get(type.getSlot());
        next.decrementAmount();
        attacker.getEquipment().set(type.getSlot(), next, true);

        GroundItem groundItem = new GroundItem(new Item(ammunition.getId(), 1), defender.getPosition(), attacker);
        groundItem.getRegion().ifPresent(r -> r.register(groundItem, true));

        if (!attacker.getEquipment().contains(ammunition.getId())) {
            attacker.out(new SendMessage(getLastFiredMessage(type)));
        }
    }

    private static String getInvalidAmmunitionMessage(RangedWeaponDefinition.AttackType type) {
        if (type == RangedWeaponDefinition.AttackType.SHOT) {
            return "You can't use these arrows with this bow.";
        }
        if (type == RangedWeaponDefinition.AttackType.THROWN) {
            return "That's weird, you broke my fucking code dick";
        }
        return null;
    }

    private static String getNoAmmunitionMessage(RangedWeaponDefinition.AttackType type) {
        if (type == RangedWeaponDefinition.AttackType.SHOT) {
            return "You need some arrows to use this bow!";
        }
        if (type == RangedWeaponDefinition.AttackType.THROWN) {
            return "That's weird, you broke my fucking code dick";
        }
        return null;
    }

    private static String getLastFiredMessage(RangedWeaponDefinition.AttackType type) {
        if (type == RangedWeaponDefinition.AttackType.SHOT) {
            return "You shot your last arrow!";
        }
        if (type == RangedWeaponDefinition.AttackType.THROWN) {
            return "You threw your last shot!";
        }
        return null;
    }

}
