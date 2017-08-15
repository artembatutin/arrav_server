package net.edge.content.newcombat.strategy.player.ranged;

import net.edge.content.newcombat.CombatConstants;
import net.edge.content.newcombat.CombatEffect;
import net.edge.content.newcombat.CombatProjectileDefinition;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.hit.Hit;
import net.edge.content.newcombat.strategy.basic.RangedStrategy;
import net.edge.content.newcombat.strategy.player.melee.FistStrategy;
import net.edge.content.newcombat.weapon.RangeData;
import net.edge.content.newcombat.weapon.RangedWeaponDefinition;
import net.edge.content.skill.Skills;
import net.edge.net.packet.out.SendMessage;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Equipment;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ThrownWeapon extends RangedStrategy<Player> {
    private static final Animation DEFAULT = new Animation(806, Animation.AnimationPriority.HIGH);
    private final RangedWeaponDefinition rangeDefinition;
    private CombatProjectileDefinition projectileDefinition;
    private Item ammo;

    protected ThrownWeapon(RangedWeaponDefinition definition, CombatProjectileDefinition projectileDefinition) {
        this.rangeDefinition = definition;
        this.projectileDefinition = projectileDefinition;
    }

    @Override
    public boolean canAttack(Player attacker, Actor defender) {
        if (attacker.getSkillLevel(Skills.RANGED) < rangeDefinition.getLevel()) {
            attacker.out(new SendMessage("You need a Ranged level of " + rangeDefinition.getLevel() + " to use this weapon."));
            return false;
        }

        Item inSlot = attacker.getEquipment().get(Equipment.WEAPON_SLOT);

        if (inSlot == null) {
            return false;
        }

        for (RangeData data : rangeDefinition.getAllowed()) {
            for (int id : data.ammo) {
                if (id == inSlot.getId()) {
                    ammo = new Item(id);
                    return true;
                }
            }
        }

        attacker.out(new SendMessage("That's weird, you broke my fucking code dick"));
        return false;
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        Item inSlot = attacker.getEquipment().get(Equipment.WEAPON_SLOT);

        attacker.animation(projectileDefinition.getAnimation().orElse(DEFAULT));
        projectileDefinition.getStart().ifPresent(attacker::graphic);
        addCombatExperience(attacker, hit);
        projectileDefinition.sendProjectile(attacker, defender);

        attacker.getEquipment().remove(ammo);

        if (inSlot == null) {
            attacker.out(new SendMessage(getLastThrownMessage()));
            attacker.getNewCombat().setStrategy(new FistStrategy());
        }
    }

    @Override
    public void hit(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        new GroundItem(ammo, defender.getPosition().copy(), attacker).register();
        Predicate<CombatEffect> filter = effect -> effect.canEffect(attacker, defender, hit);
        Consumer<CombatEffect> execute = effect -> effect.execute(attacker, defender, hit);
        projectileDefinition.getEffect().filter(filter).ifPresent(execute);
    }

    @Override
    public void hitsplat(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        projectileDefinition.getEnd().ifPresent(attacker::graphic);

        if (hit.getDamage() > 0) {
            String name = projectileDefinition.getName();

            if (isPoisonous(name)) {
                defender.getNewCombat().poison(poisonStrength(name));
            }
        }
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        Item item = attacker.getEquipment().get(Equipment.ARROWS_SLOT);
        int bonus = 0;
        if (item != null) {
            bonus = item.getDefinition().getBonus()[CombatConstants.RANGED_STRENGTH];
        }

        attacker.appendBonus(CombatConstants.RANGED_STRENGTH, -bonus);
        CombatHit hit = nextRangedHit(attacker, defender);
        attacker.appendBonus(CombatConstants.RANGED_STRENGTH, bonus);
        return new CombatHit[] { hit };
    }

    protected abstract String getLastThrownMessage();

}
