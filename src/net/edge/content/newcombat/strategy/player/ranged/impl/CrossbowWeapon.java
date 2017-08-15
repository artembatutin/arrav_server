package net.edge.content.newcombat.strategy.player.ranged.impl;

import net.edge.content.newcombat.CombatProjectileDefinition;
import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.hit.Hit;
import net.edge.content.newcombat.strategy.basic.RangedStrategy;
import net.edge.content.newcombat.weapon.RangeData;
import net.edge.content.newcombat.weapon.RangedWeaponDefinition;
import net.edge.net.packet.out.SendMessage;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Equipment;

public class CrossbowWeapon extends RangedStrategy<Player> {
    private static final Animation DEFAULT = new Animation(4230, Animation.AnimationPriority.HIGH);
    private final RangedWeaponDefinition rangedDefinition;
    private CombatProjectileDefinition projectileDefinition;

    public CrossbowWeapon(RangedWeaponDefinition definition, CombatProjectileDefinition projectileDefinition) {
        this.rangedDefinition = definition;
        this.projectileDefinition = projectileDefinition;
    }

    @Override
    public boolean canAttack(Player attacker, Actor defender) {
        Item arrow = attacker.getEquipment().get(Equipment.ARROWS_SLOT);

        if (arrow != null) {
            for (RangeData data : rangedDefinition.getAllowed()) {
                for (int id : data.ammo) {
                    if (id == arrow.getId()) return true;
                }
            }

            attacker.out(new SendMessage("You can't use these bolts with this crossbow."));
        } else attacker.out(new SendMessage("You need some bolts to use this crossbow!"));

        return false;
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        attacker.animation(projectileDefinition.getAnimation().orElse(DEFAULT));
        projectileDefinition.getStart().ifPresent(attacker::graphic);
        addCombatExperience(attacker, hit);
        projectileDefinition.sendProjectile(attacker, defender);
    }

    @Override
    public void hit(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        Item bolt = attacker.getEquipment().get(Equipment.ARROWS_SLOT);
        attacker.getEquipment().remove(new Item(bolt.getId(), 1));
        new GroundItem(new Item(bolt.getId()), defender.getPosition(), attacker).register();

        if (attacker.getEquipment().get(Equipment.ARROWS_SLOT) == null) {
            attacker.out(new SendMessage("You shot your last bolt!"));
        }
    }

    @Override
    public void hitsplat(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        if (hit.getDamage() > 0) {
            String name = projectileDefinition.getName();

            if (isPoisonous(name)) {
                defender.getNewCombat().poison(poisonStrength(name));
            }
        }
    }

    @Override
    public int getAttackDelay(AttackStance stance) {
        return stance == AttackStance.RAPID ? 5 : 6;
    }

    @Override
    public int getAttackDistance(AttackStance stance) {
        return stance == AttackStance.LONGRANGE ? 9 : 7;
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        return new CombatHit[]{nextRangedHit(attacker, defender)};
    }

}
