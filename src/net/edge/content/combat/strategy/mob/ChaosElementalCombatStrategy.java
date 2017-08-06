package net.edge.content.combat.strategy.mob;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.magic.CombatSpell;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.locale.Position;

import java.util.Optional;
import java.util.Random;

/**
 * Created by Dave/Ophion
 * Date: 06/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public final class ChaosElementalCombatStrategy implements CombatStrategy {

    private final int PRIMARY_PROJECTILE = 558;
    private final int TELEPORT_PROJECTILE = 555;
    private final int UNEQUIP_PROJECTILE = 552;

    private static final CombatNormalSpell SPELL = new CombatNormalSpell() {

        @Override
        public int spellId() {
            return 0;
        }

        @Override
        public int maximumHit() {
            return 28;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public Optional<Projectile> projectile(Actor cast, Actor castOn) {
            return Optional.empty();
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

        @Override
        public int levelRequired() {
            return 0;
        }

        @Override
        public double baseExperience() {
            return 0;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }
    };

    @Override
    public boolean canOutgoingAttack(Actor character, Actor victim) {
        return character.isMob();
    }


    @Override
    public CombatHit outgoingAttack(Actor character, Actor victim) {
        int specialAttack = RandomUtils.inclusive(0, 100);
        if(specialAttack > 80) {
            specialAttack(character, victim);
            return null;
        }
        CombatType[] data = new CombatType[]{CombatType.MELEE, CombatType.MAGIC, CombatType.RANGED};
        CombatType type = RandomUtils.random(data);
        return primaryAttack(character, victim, type);
    }

    @Override
    public int attackDelay(Actor character) {
        return 5;
    }

    @Override
    public int attackDistance(Actor character) {
        return 14;
    }

    @Override
    public int[] getMobs() {
        return new int[]{3200};
    }

    /**
     * Chaos elemental handles a 'primary' attack through out the combat
     * This can deal either magic, ranged or melee damage - it is impossible
     * to distinguish what damage is dealt until the attack is hit.
     * @param character
     * @param victim
     * @param type
     */
    private CombatHit primaryAttack(Actor character, Actor victim, CombatType type) {
        if(type == CombatType.MAGIC) {
            character.setCurrentlyCasting(SPELL);
        }
        new Projectile(character, victim, PRIMARY_PROJECTILE, 50, 3, 43, 31, 0).sendProjectile();
        return new CombatHit(character, victim, 1, type, true, 5);
    }

    /**
     * Chaos elemental handles two random 'special' attacks -
     * One; moves(teleports) his victim into a near-by randomized area
     * Two; Forces the victim to unequip a certain item.
     * @param character
     * @param victim
     */
    private void specialAttack(Actor character, Actor victim) {
        if(RandomUtils.inclusive(100) > 50 ) {
            new Projectile(character, victim, TELEPORT_PROJECTILE, 50, 3, 43, 31, 0).sendProjectile();
            victim.toPlayer().move(new Position(victim.getPosition().getX() + RandomUtils.inclusive(12), victim.getPosition().getY() + RandomUtils.inclusive(12)));
        } else {
            new Projectile(character, victim, UNEQUIP_PROJECTILE, 50, 3, 43, 31, 0).sendProjectile();
            if(victim.toPlayer().getEquipment().get(3) != null) {
                victim.toPlayer().getEquipment().unequip(3);
            } else {
                for(int i=0; i < 13; i++) {
                    if (victim.toPlayer().getEquipment().get(i) != null) {
                        victim.toPlayer().getEquipment().unequip(i);
                        break;
                    }
                }
            }
            victim.toPlayer().message("The chaos elemental's tentacle removes your equipment.");
        }
    }

}
