package net.edge.world.entity.actor.mob.strategy.impl;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.task.Task;
import net.edge.world.*;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.strategy.DynamicCombatStrategy;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

import static net.edge.util.rand.RandomUtils.*;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 19-6-2017.
 */
public final class WildyWyrmCombatStrategy extends DynamicCombatStrategy<Mob> {

    /**
     * Determines if the wildywyrm is stomping.
     */
    private boolean stomping;

    /**
     * Constructs a new {@link DynamicCombatStrategy}.
     *
     * @param mob {@link #npc}.
     */
    public WildyWyrmCombatStrategy(Mob mob) {
        super(mob);
    }

    /**
     * The combat session data which returns a combat session data dependant on the specified
     * {@code type}.
     * @param victim    the victim being hit.
     * @param type      the type of combat being attacked with.
     * @return the combat session data.
     */
    private CombatHit type(Actor victim, CombatType type) {
        switch(type) {
            case MELEE:
                return melee(victim);
            case MAGIC:
                return magic(victim);
            default:
                return magic(victim);
        }
    }

//    private CombatHit stomp() {FIXME
//        stomping = true;
//
//        Position target = null;
//        npc.animation(new Animation(12796, Animation.AnimationPriority.HIGH));
//
//        for(Player p : World.get().getPlayers()) {
//            if(p.isDead() || !npc.getPosition().withinDistance(p.getPosition(), 12)) {
//                continue;
//            }
//
//            target = p.getPosition();
//        }
//
//        if(target == null) {
//            npc.untransform();
//            return null;
//        }
//
//        final Position finalPosition = target;
//
//        LinkedTaskSequence seq = new LinkedTaskSequence();
//        seq.connect(2, () -> {
//            npc.transform(2417);
//        });
//        seq.connect(1, () -> {
//            ForcedMovement movement = new ForcedMovement(npc);
//
//            movement.setFirst(npc.getPosition());
//            movement.setSecond(finalPosition);
//            movement.setSecondSpeed(40);
//            movement.onDestination(() -> {
//                npc.untransform();
//                npc.animation(new Animation(12795, Animation.AnimationPriority.HIGH));
//            });
//            movement.submit();
//        });
//        seq.start();
//        return null;
//    }

    private CombatHit melee(Actor victim) {
        npc.animation(new Animation(12791, Animation.AnimationPriority.HIGH));
        return new CombatHit(npc, victim, 1, CombatType.MELEE, false);
    }

    private CombatHit magic(Actor victim) {
        npc.animation(new Animation(12794));
        World.get().submit(new Task(1, false) {
            @Override
            public void execute() {
                this.cancel();
                if(npc.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || npc.isDead() || victim.isDead())
                    return;
                SPELL.projectile(npc, victim).get().sendProjectile();
            }
        });
        npc.setCurrentlyCasting(SPELL);
        return new CombatHit(npc, victim, 1, CombatType.MAGIC, false);
    }

    /**
     * Determines if {@link #npc} can attack the specified {@code victim}.
     *
     * @param victim the victim to determine this for.
     * @return {@code true} if the {@link #npc} can, {@code false} otherwise.
     */
    @Override
    public boolean canOutgoingAttack(Actor victim) {
        return !stomping;
    }

    /**
     * Executed when {@link #npc} has passed the initial {@code canAttack}
     * check and is about to attack {@code victim}.
     *
     * @param victim the character being attacked.
     * @return a container holding the data for the attack.
     */
    @Override
    public CombatHit outgoingAttack(Actor victim) {
        CombatType[] types = npc.getPosition().withinDistance(victim.getPosition(), 4) ? new CombatType[]{CombatType.MELEE, CombatType.MAGIC} : new CombatType[]{CombatType.MAGIC};
        return /*RandomUtils.inclusive(100) < 60 ? stomp() : */type(victim, random(types));
    }

    /**
     * Executed when the {@link #npc} is hit by the {@code attacker}.
     *
     * @param attacker the attacker whom hit the character.
     * @param data     the combat session data chained to this hit.
     */
    @Override
    public void incomingAttack(Actor attacker, CombatHit data) {
        if(stomping) {
            data.ignore();
        }
    }

    /**
     * Determines the delay for when {@link #npc} will attack.
     *
     * @return the value that the attack timer should be reset to.
     */
    @Override
    public int attackDelay() {
        return 6;
    }

    /**
     * Determines how close {@link #npc} must be to attack.
     *
     * @return the radius that the npc must be in to attack.
     */
    @Override
    public int attackDistance() {
        return 8;
    }

    private static final CombatNormalSpell SPELL = new CombatNormalSpell() {

        @Override
        public Optional<Animation> castAnimation() {
            return Optional.empty();
        }

        @Override
        public Optional<Projectile> projectile(Actor cast, Actor castOn) {
            return Optional.of(new Projectile(cast, castOn, 1067, 44, 3, 43, 31, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.of(new Graphic(555, 100));
        }

        @Override
        public int maximumHit() {
            return 600;
        }

        @Override
        public Optional<Graphic> startGraphic() {
            return Optional.empty();
        }

        @Override
        public double baseExperience() {
            return -1;
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> itemsRequired(Player player) {
            return Optional.empty();
        }

        @Override
        public int levelRequired() {
            return -1;
        }

        @Override
        public int spellId() {
            return -1;
        }
    };
}
