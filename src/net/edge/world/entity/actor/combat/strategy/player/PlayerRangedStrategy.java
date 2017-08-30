package net.edge.world.entity.actor.combat.strategy.player;

import net.edge.content.item.Requirement;
import net.edge.net.packet.out.SendMessage;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatConstants;
import net.edge.world.entity.actor.combat.CombatImpact;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.CombatUtil;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.effect.impl.CombatPoisonEffect;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.ranged.RangedAmmunition;
import net.edge.world.entity.actor.combat.ranged.RangedWeaponDefinition;
import net.edge.world.entity.actor.combat.ranged.RangedWeaponType;
import net.edge.world.entity.actor.combat.strategy.basic.RangedStrategy;
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
    private final RangedAmmunition ammunition;

    public PlayerRangedStrategy(RangedAmmunition ammunition, RangedWeaponDefinition definition) {
        this.ammunition = ammunition;
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
            if (rangedDefinition.isValid(ammunition)) {
                return true;
            }
            attacker.out(new SendMessage(getInvalidAmmunitionMessage(rangedDefinition.getType())));
        } else {
            attacker.out(new SendMessage(getNoAmmunitionMessage(rangedDefinition.getType())));
        }
        attacker.getCombat().reset();
        return false;
    }

    @Override
    public void start(Player attacker, Actor defender, Hit[] hits) {
        if (attacker.getCombat().getDefender() == defender) {
            Animation animation = ammunition.getAnimation().orElse(getAttackAnimation(attacker, defender));
            attacker.animation(animation);
            ammunition.getStart().ifPresent(attacker::graphic);
            ammunition.sendProjectile(attacker, defender);

            if (ammunition.getEffect().isPresent()) {
                List<Hit> extra = new LinkedList<>();
                for (Hit hit : hits) {
                    Predicate<CombatImpact> filter = effect -> effect.canAffect(attacker, defender, hit);
                    Consumer<CombatImpact> execute = effect -> effect.impact(attacker, defender, hit, extra);
                    ammunition.getEffect().filter(filter).ifPresent(execute);
                }
                if (extra.isEmpty()) {
                    Collections.addAll(extra, hits);
                    addCombatExperience(attacker, extra.toArray(new Hit[0]));
                } else {
                    addCombatExperience(attacker, hits);
                }
            } else {
                addCombatExperience(attacker, hits);
            }
        }
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit hit) {
        removeAmmunition(attacker, defender, rangedDefinition.getType());
    }

    @Override
    public void hit(Player attacker, Actor defender, Hit hit) {
        ammunition.getEnd().ifPresent(defender::graphic);
        CombatPoisonEffect.getPoisonType(attacker.getEquipment().get(rangedDefinition.getSlot())).ifPresent(p -> {
            if (hit.getDamage() > 0) {
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

        if (rangedDefinition.getType() == RangedWeaponType.THROWN && arrows != null) {
            int bonus = arrows.getDefinition().getBonus()[CombatConstants.BONUS_RANGED_STRENGTH];
            attacker.appendBonus(Equipment.ARROWS_SLOT, -bonus);
            CombatHit hit = nextRangedHit(attacker, defender);
            attacker.appendBonus(Equipment.ARROWS_SLOT, arrows.getDefinition().getBonus()[CombatConstants.BONUS_RANGED_STRENGTH]);
            return new CombatHit[]{hit};
        }

        return new CombatHit[]{nextRangedHit(attacker, defender)};
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.RANGED;
    }

    private void removeAmmunition(Player attacker, Actor defender, RangedWeaponType type) {
        Item next = attacker.getEquipment().get(type.getSlot());

        next.decrementAmount();
        attacker.getEquipment().set(type.getSlot(), next, true);

        if (ammunition.isDroppable()) {
            GroundItem groundItem = new GroundItem(new Item(next.getId(), ammunition.getRemoval()), defender.getPosition(), attacker);
            groundItem.getRegion().ifPresent(r -> r.register(groundItem, true));
        }

        if (next.getAmount() == 0) {
            attacker.out(new SendMessage("That was the last of your ammunition!"));
            attacker.getEquipment().unequip(type.getSlot(), null, true, -1);
        }
    }

    private static String getInvalidAmmunitionMessage(RangedWeaponType type) {
        if (type == RangedWeaponType.SHOT) {
            return "You can't use this ammunition with this weapon.";
        }
        if (type == RangedWeaponType.THROWN) {
            return "That's weird, there was a ranged weapon error. Contact developers stat!";
        }
        return null;
    }

    private static String getNoAmmunitionMessage(RangedWeaponType type) {
        if (type == RangedWeaponType.SHOT) {
            return "You need some ammunition to use this weapon!";
        }
        if (type == RangedWeaponType.THROWN) {
            return "That's weird, there was a ranged weapon error. Contact developers stat!";
        }
        return null;
    }

}
