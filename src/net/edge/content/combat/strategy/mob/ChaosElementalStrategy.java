package net.edge.content.combat.strategy.mob;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.strategy.Strategy;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.locale.Position;

import java.util.Optional;

/**
 * Created by Dave/Ophion
 * Date: 06/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public final class ChaosElementalStrategy implements Strategy {

    private final static int PRIMARY_PROJECTILE = 558;
    private final static int TELEPORT_PROJECTILE = 555;
    private final static int UNEQUIP_PROJECTILE = 552;

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
    public boolean canOutgoingAttack(Actor actor, Actor victim) {
        return actor.isMob();
    }


    @Override
    public CombatHit outgoingAttack(Actor actor, Actor victim) {
        CombatType[] data = new CombatType[]{CombatType.MELEE, CombatType.MAGIC, CombatType.RANGED};
        CombatType type = RandomUtils.random(data);
        CombatHit session = primaryAttack(actor, victim, type);

        if(victim.isPlayer() && RandomUtils.inclusive(0, 100) > 80) {
            if(specialAttack(actor, victim.toPlayer()))
             session.ignore();
        }

        return session;
    }

    @Override
    public int attackDelay(Actor actor) {
        return 5;
    }

    @Override
    public int attackDistance(Actor actor) {
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
     */
    private CombatHit primaryAttack(Actor character, Actor victim, CombatType type) {
        if(type == CombatType.MAGIC) {
            character.setCurrentlyCasting(SPELL);
        }
        int delay = new Projectile(character, victim, PRIMARY_PROJECTILE, 50, 3, 43, 31, 0).sendProjectile().getDelay();
        return new CombatHit(character, victim, 1, type, true, delay);
    }

    /**
     * Chaos elemental handles two random 'special' attacks -
     * One; moves(teleports) his victim into a near-by randomized area
     * Two; Forces the victim to unequip a certain item.
     */
    private boolean specialAttack(Actor character, Player victim) {
        if(RandomUtils.inclusive(100) > 50 ) {
            new Projectile(character, victim, TELEPORT_PROJECTILE, 50, 3, 43, 31, 0).sendProjectile();
            victim.move(new Position(victim.getPosition().getX() + RandomUtils.inclusive(6), victim.getPosition().getY() + RandomUtils.inclusive(6)));
            return true;
        } else if(!victim.getInventory().isFull()) {
            new Projectile(character, victim, UNEQUIP_PROJECTILE, 50, 3, 43, 31, 0).sendProjectile();
            if(victim.getEquipment().get(3) != null) {
                victim.getEquipment().unequip(3);
            } else {
                for(int i=0; i < 13; i++) {
                    if (victim.getEquipment().get(i) != null) {
                        victim.getEquipment().unequip(i);
                        break;
                    }
                }
            }
            victim.message("The chaos elemental's tentacle removes your equipment.");
            return true;
        }
        return false;
    }

}
