package net.arrav.world.entity.actor.combat.strategy.player;

import net.arrav.content.item.Requirement;
import net.arrav.net.packet.out.SendMessage;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatImpact;
import net.arrav.world.entity.actor.combat.CombatType;
import net.arrav.world.entity.actor.combat.CombatUtil;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.effect.impl.CombatPoisonEffect;
import net.arrav.world.entity.actor.combat.hit.CombatHit;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.ranged.RangedWeaponType;
import net.arrav.world.entity.actor.combat.strategy.basic.RangedStrategy;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.GroundItem;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.impl.Equipment;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PlayerRangedStrategy extends RangedStrategy<Player> {
    
    private static final PlayerRangedStrategy INSTANCE = new PlayerRangedStrategy();

    protected PlayerRangedStrategy() {
    }

    @Override
    public boolean canAttack(Player attacker, Actor defender) {
        if (!Requirement.canEquip(attacker, attacker.getEquipment().get(Equipment.WEAPON_SLOT))) {
            attacker.getCombat().reset(false, true);
            return false;
        }

        Item ammo = attacker.getEquipment().get(attacker.rangedDefinition.getSlot());
        if (ammo != null) {
            if (attacker.rangedDefinition.isValid(attacker.rangedAmmo)) {
                return true;
            }
            attacker.out(new SendMessage(getInvalidAmmunitionMessage(attacker.rangedDefinition.getType())));
        } else {
            attacker.out(new SendMessage(getNoAmmunitionMessage(attacker.rangedDefinition.getType())));
        }
        attacker.getCombat().reset(false, true);
        return false;
    }

    @Override
    public void start(Player attacker, Actor defender, Hit[] hits) {
        if (attacker.getCombat().getDefender() == defender) {
            if(!disableAnimations()) {
                Animation animation = attacker.rangedAmmo.getAnimation().orElse(getAttackAnimation(attacker, defender));
                attacker.animation(animation);
                attacker.rangedAmmo.getStart().ifPresent(attacker::graphic);
                attacker.rangedAmmo.sendProjectile(attacker, defender);
            }
            if (attacker.rangedAmmo.getEffect().isPresent()) {
                List<Hit> extra = new LinkedList<>();
                for (Hit hit : hits) {
                    Predicate<CombatImpact> filter = effect -> effect.canAffect(attacker, defender, hit);
                    Consumer<CombatImpact> execute = effect -> effect.impact(attacker, defender, hit, extra);
                    attacker.rangedAmmo.getEffect().filter(filter).ifPresent(execute);
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
            if(attacker.isSpecialActivated()) {
                attacker.getCombatSpecial().drain(attacker);
            }
        }
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit hit) {
        removeAmmunition(attacker, defender, attacker.rangedDefinition.getType());
    }

    @Override
    public void hit(Player attacker, Actor defender, Hit hit) {
        attacker.rangedAmmo.getEnd().ifPresent(defender::graphic);
        CombatPoisonEffect.getPoisonType(attacker.getEquipment().get(attacker.rangedDefinition.getSlot())).ifPresent(p -> {
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

        if (attacker.rangedDefinition.getType() == RangedWeaponType.THROWN && arrows != null) {
            CombatHit hit = nextRangedHit(attacker, defender, RangedWeaponType.THROWN);
            return new CombatHit[]{hit};
        }

        return new CombatHit[]{nextRangedHit(attacker, defender, RangedWeaponType.SHOT)};
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.RANGED;
    }

    private void removeAmmunition(Player attacker, Actor defender, RangedWeaponType type) {
        Item next = attacker.getEquipment().get(type.getSlot());

        next.decrementAmount();
        attacker.getEquipment().set(type.getSlot(), next, true);

        if (attacker.rangedAmmo.isDroppable()) {
            GroundItem groundItem = new GroundItem(new Item(next.getId(), attacker.rangedAmmo.getRemoval()), defender.getPosition(), attacker);
            groundItem.getRegion().ifPresent(r -> r.register(groundItem, true));
        }

        if (next.getAmount() == 0) {
            attacker.out(new SendMessage("That was the last of your attacker.ammunition!"));
            attacker.getEquipment().unequip(type.getSlot(), null, true, -1);
        }
    }

    private static String getInvalidAmmunitionMessage(RangedWeaponType type) {
        if (type == RangedWeaponType.SHOT) {
            return "You can't use this attacker.ammunition with this weapon.";
        }
        if (type == RangedWeaponType.THROWN) {
            return "That's weird, there was a ranged weapon error. Contact developers stat!";
        }
        return null;
    }

    private static String getNoAmmunitionMessage(RangedWeaponType type) {
        if (type == RangedWeaponType.SHOT) {
            return "You need some attacker.ammunition to use this weapon!";
        }
        if (type == RangedWeaponType.THROWN) {
            return "That's weird, there was a ranged weapon error. Contact developers stat!";
        }
        return null;
    }

    public static PlayerRangedStrategy get() {
        return INSTANCE;
    }

    public boolean disableAnimations() {
        return false;
    }
}
