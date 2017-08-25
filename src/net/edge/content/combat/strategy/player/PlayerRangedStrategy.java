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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
    public void start(Player attacker, Actor defender, Hit[] hits) {
        attacker.animation(getAttackAnimation(attacker, defender));
        projectileDefinition.getStart().ifPresent(attacker::graphic);
        projectileDefinition.sendProjectile(attacker, defender, false);

        List<Hit> extra = new LinkedList<>();
        for (Hit hit : hits) {
            Predicate<CombatEffect> filter = effect -> effect.canEffect(attacker, defender, hit);
            Consumer<CombatEffect> execute = effect -> effect.execute(attacker, defender, hit, extra);
            projectileDefinition.getEffect().filter(filter).ifPresent(execute);
        }

        Collections.addAll(extra, hits);
        addCombatExperience(attacker, extra.toArray(new Hit[0]));
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit hit) {
        removeAmmunition(attacker, defender, rangedDefinition.getType());
    }

    @Override
    public void hit(Player attacker, Actor defender, Hit hit) {
        CombatPoisonEffect.getPoisonType(attacker.getEquipment().get(Equipment.WEAPON_SLOT)).ifPresent(p -> {
            if(hit.getDamage() > 0) {
                defender.poison(p);
            }
        });
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        if (attacker.getWeaponAnimation() != null && attacker.getWeaponAnimation().getAttacking()[0] != 422) {
            return new Animation(attacker.getWeaponAnimation().getAttacking()[attacker.getCombat().getFightType().getStyle().ordinal()], Animation.AnimationPriority.HIGH);
        }
        return new Animation(attacker.getCombat().getFightType().getAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
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

        if (rangedDefinition.getType() == RangedWeaponDefinition.AttackType.THROWN && arrows != null) {
            int bonus = arrows.getDefinition().getBonus()[CombatConstants.BONUS_RANGED_STRENGTH];
            attacker.appendBonus(Equipment.ARROWS_SLOT, -bonus);
            CombatHit hit = nextRangedHit(attacker, defender);
            attacker.appendBonus(Equipment.ARROWS_SLOT, arrows.getDefinition().getBonus()[CombatConstants.BONUS_RANGED_STRENGTH]);
            return new CombatHit[] { hit };
        }

        return new CombatHit[] { nextRangedHit(attacker, defender) };
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

        if (next.getAmount() == 0) {
            attacker.out(new SendMessage(getLastFiredMessage(type)));
            attacker.getEquipment().unequip(type.getSlot(), null, true, -1);
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
