package net.edge.content.newcombat.strategy.player.ranged;

import net.edge.content.newcombat.CombatEffect;
import net.edge.content.newcombat.CombatProjectileDefinition;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.hit.Hit;
import net.edge.content.newcombat.strategy.basic.RangedStrategy;
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

public abstract class BowWeapon extends RangedStrategy<Player> {
    private static final Animation DEFAULT = new Animation(426, Animation.AnimationPriority.HIGH);
    private final RangedWeaponDefinition rangedDefinition;
    protected final CombatProjectileDefinition projectileDefinition;

    protected BowWeapon(RangedWeaponDefinition definition, CombatProjectileDefinition projectileDefinition) {
        this.rangedDefinition = definition;
        this.projectileDefinition = projectileDefinition;
    }

    @Override
    public boolean canAttack(Player attacker, Actor defender) {
        if (attacker.getSkillLevel(Skills.RANGED) < rangedDefinition.getLevel()) {
            attacker.out(new SendMessage("You need a Ranged level of " + rangedDefinition.getLevel() + " to use this weapon."));
            return false;
        }

        Item arrow = attacker.getEquipment().get(rangedDefinition.getSlot());

        if (arrow != null) {
            if (rangedDefinition.getAllowed() == null) {
                return false;
            }

            for (RangeData data : rangedDefinition.getAllowed()) {
                for (int id : data.ammo) {
                    if (id == arrow.getId()) {
                        return true;
                    }
                }
            }

            attacker.out(new SendMessage("You can't use these arrows with this bow."));
        } else attacker.out(new SendMessage("You need some arrows to use this bow!"));

        return false;
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        attacker.animation(projectileDefinition.getAnimation().orElse(DEFAULT));
        projectileDefinition.getStart().ifPresent(attacker::graphic);
        addCombatExperience(attacker, hit);
        projectileDefinition.sendProjectile(attacker, defender);

        Item arrow = attacker.getEquipment().get(Equipment.ARROWS_SLOT);
        attacker.getEquipment().remove(new Item(arrow.getId(), 1));
        new GroundItem(new Item(arrow.getId()), defender.getPosition(), attacker).register();

        if (attacker.getEquipment().get(Equipment.ARROWS_SLOT) == null) {
            attacker.out(new SendMessage("You shot your last arrow!"));
        }
    }

    @Override
    public void hit(Player attacker, Actor defender, Hit hit, Hit[] hits) {

        Predicate<CombatEffect> filter = effect -> effect.canEffect(attacker, defender, hit);
        Consumer<CombatEffect> execute = effect -> effect.execute(attacker, defender, hit);
        projectileDefinition.getEffect().filter(filter).ifPresent(execute);
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
    public CombatHit[] getHits(Player attacker, Actor defender) {
        return new CombatHit[] { nextRangedHit(attacker, defender, projectileDefinition.getHitDelay(), projectileDefinition.getHitsplatDelay()) };
    }

}
