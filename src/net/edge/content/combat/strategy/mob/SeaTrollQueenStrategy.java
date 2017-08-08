package net.edge.content.combat.strategy.mob;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.magic.CombatSpells;
import net.edge.content.combat.strategy.Strategy;
import net.edge.content.skill.Skills;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.Spell;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;
import java.util.Random;

/**
 * Created by Dave/Ophion
 * Date: 08/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class SeaTrollQueenStrategy implements Strategy {


    @Override
    public boolean canOutgoingAttack(Actor actor, Actor victim) {
        return actor.isMob();
    }

    @Override
    public CombatHit outgoingAttack(Actor actor, Actor victim) {
        CombatType[] data = actor.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.MAGIC} : new CombatType[]{CombatType.MAGIC};
        CombatType c = RandomUtils.random(data);
        victim.toPlayer().message("Generating attack.");
        return type(actor, victim, c);
    }

    @Override
    public boolean hitBack() {
        return true;
    }

    @Override
    public int attackDelay(Actor actor) {
        return 5;
    }

    @Override
    public int attackDistance(Actor actor) {
        return 10;
    }

    @Override
    public int[] getMobs() {
        return new int[]{3847};
    }

    /**
     * Represents the melee attack style of this entity
     * @param character
     * @param victim
     * @return
     */
    public CombatHit melee(Actor character, Actor victim) {
        return new CombatHit(character, victim, 1, CombatType.MELEE, true);
    }

    /**
     * Represents the magic attack style of this entity
     * @param character
     * @param victim
     * @return
     */
    public CombatHit magic(Actor character, Actor victim) {
        CombatNormalSpell spell = WATER_WAVE;
        character.animation(new Animation(3992));
        character.setCurrentlyCasting(spell);
        spell.projectile(character, victim).get().sendProjectile();
        return new CombatHit(character, victim, 1, CombatType.MAGIC, false);
    }

     /**
     * Represents the entities drain attack
     * Able to decrease the victims prayer by over 20 points.
     * */
    public CombatHit drainAttack(Actor character, Actor victim) {
        int prayerDrain = RandomUtils.inclusive(5, 25);
        CombatNormalSpell spell = DRAIN_ATTACK;
        character.setCurrentlyCasting(spell);
        character.animation(new Animation(3992));
        spell.projectile(character, victim).get().sendProjectile();
        CombatHit session = new CombatHit(character, victim, 1, CombatType.MAGIC, false) {
            @Override
            public void postAttack(int counter) {
                victim.toPlayer().getSkills()[Skills.PRAYER].decreaseLevel(prayerDrain);
            }
        };
        session.ignore();
        return session;
    }

    private CombatHit type(Actor character, Actor victim, CombatType type) {
        if(RandomUtils.inclusive(100) < 25) {
            return drainAttack(character, victim);
        }
        switch(type) {
            case MELEE:
                return melee(character, victim);
            case MAGIC:
                return magic(character, victim);
            default:
                return magic(character, victim);
        }
    }

    private static final CombatNormalSpell WATER_WAVE = new CombatNormalSpell() {

        @Override
        public int levelRequired() {
            return -1;
        }

        @Override
        public double baseExperience() {
            return -1;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return null;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return null;
        }

        @Override
        public int spellId() {
            return -1;
        }

        @Override
        public int maximumHit() {
            return 370;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return null;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(2702, 0, 20));
        }

        @Override
        public Optional<Projectile> projectile(Actor cast, Actor castOn) {
            return Optional.of(new Projectile(cast, castOn, 2706, 44, 3, 23, 21, 0, CombatType.MAGIC));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(2700, 100));
        }
    };

    private static final CombatNormalSpell DRAIN_ATTACK = new CombatNormalSpell() {

        @Override
        public int levelRequired() {
            return -1;
        }

        @Override
        public double baseExperience() {
            return -1;
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return null;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return null;
        }

        @Override
        public int spellId() {
            return -1;
        }

        @Override
        public int maximumHit() {
            return 0;
        }

        @Override
        public Optional<Animation> castAnimation() {
            return null;
        }

        @Override
        public Optional<Projectile> projectile(Actor cast, Actor castOn) {
            return Optional.of(new Projectile(cast, castOn, 171, 44, 3, 43, 31, 0, CombatType.MAGIC));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(172, 100));
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.of(new Graphic(170, 85, 10));
        }


    };
}
