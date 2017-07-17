package net.edge.world.node.actor.npc.impl;

import net.edge.locale.Position;
import net.edge.task.LinkedTaskSequence;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.actor.Actor;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.npc.strategy.impl.KalphiteQueenCombatStrategy;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 29-6-2017.
 */
public final class KalphiteQueen extends Npc {

    /**
     * The current phase of the kalphite queen.
     */
    public Phase phase;

    /**
     * Creates a new {@link Npc}.
     * @param position the position of this character in the world.
     */
    public KalphiteQueen(Position position) {
        super(Phase.PHASE_ONE.npcId, position);
        this.setStrategy(Optional.of(new KalphiteQueenCombatStrategy(this)));
        this.phase = Phase.PHASE_ONE;
    }

    @Override
    public void appendDeath() {
        switch(phase) {
            case PHASE_ONE:
                setDead(true);
                int deathAnimation = getDefinition().getDeathAnimation();
                this.animation(new Animation(deathAnimation, Animation.AnimationPriority.HIGH));
                LinkedTaskSequence sequence = new LinkedTaskSequence();
                sequence.connect(3, () -> {
                    this.setCurrentHealth(getMaxHealth());
                    this.transform(Phase.PHASE_TWO.npcId);
                    this.phase = Phase.PHASE_TWO;
                    this.graphic(new Graphic(1055));
                    this.animation(new Animation(6270, Animation.AnimationPriority.HIGH));
                });
                sequence.connect(14, () -> {
                    this.setDead(false);
                    Actor victim = this.getCombatBuilder().getVictim();
                    if(victim != null && victim.getPosition().withinDistance(this.getPosition(), 12) && !victim.isDead()) {
                        this.getCombatBuilder().attack(victim);
                    }
                });
                sequence.start();
                break;
            case PHASE_TWO:
                this.setRespawn(true);
                super.appendDeath();
                break;
        }
    }

    /**
     * Creates the particular {@link Npc instance}.
     * @return new {@link Npc} instance.
     */
    @Override
    public Npc create() {
        return new KalphiteQueen(getPosition());
    }

    /**
     * The enumerated type whose elements represent a set of constants used to define
     * information between the phases.
     * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
     * @since 29-6-2017.
     */
    public enum Phase {
        PHASE_ONE(1158),
        PHASE_TWO(1160);

        /**
         * The npc id of this phase.
         */
        public final int npcId;

        /**
         * Constructs a new {@link Phase}.
         * @param npcId {@link #npcId}.
         */
        Phase(int npcId) {
            this.npcId = npcId;
        }
    }
}
