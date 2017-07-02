package net.edge.world.node.entity.npc.strategy.impl;

import net.edge.content.combat.CombatSessionData;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.World;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.impl.KalphiteQueen;
import net.edge.world.node.entity.npc.strategy.DynamicCombatStrategy;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 29-6-2017.
 */
public final class KalphiteQueenCombatStrategy extends DynamicCombatStrategy<KalphiteQueen> {

    /**
     * Constructs a new {@link DynamicCombatStrategy}.
     *
     * @param npc {@link #npc}.
     */
    public KalphiteQueenCombatStrategy(KalphiteQueen npc) {
        super(npc);
    }

    /**
     * Determines if {@link #npc} can attack the specified {@code victim}.
     *
     * @param victim the victim to determine this for.
     * @return {@code true} if the {@link #npc} can, {@code false} otherwise.
     */
    @Override
    public boolean canOutgoingAttack(EntityNode victim) {
        return !npc.isDead() && !victim.isDead();
    }

    @Override
    public boolean canIncomingAttack(EntityNode attacker) {
        return !npc.isDead() && !attacker.isDead();
    }

    /**
     * Executed when {@link #npc} has passed the initial {@code canAttack}
     * check and is about to attack {@code victim}.
     *
     * @param victim the character being attacked.
     * @return a container holding the data for the attacnpk.
     */
    @Override
    public CombatSessionData outgoingAttack(EntityNode victim) {
        CombatType[] data = npc.getPosition().withinDistance(victim.getPosition(), 4) ? new CombatType[]{CombatType.MELEE, CombatType.RANGED, CombatType.MAGIC} : new CombatType[]{CombatType.RANGED, CombatType.MAGIC};
        CombatType c = RandomUtils.random(data);
        return type(victim, c);
    }

    private CombatSessionData type(EntityNode victim, CombatType type) {
        switch(type) {
            case MELEE:
                return melee(victim);
            case RANGED:
                return ranged(victim);
            case MAGIC:
                return magic(victim);
            default:
                return melee(victim);
        }
    }

    private CombatSessionData melee(EntityNode victim) {
        int animationId = npc.getDefinition().getAttackAnimation();
        npc.animation(new Animation(animationId, Animation.AnimationPriority.HIGH));
        return new CombatSessionData(npc, victim, 1, CombatType.MELEE, true);
    }

    private CombatSessionData ranged(EntityNode victim) {
        int animationId = npc.phase.equals(KalphiteQueen.Phase.PHASE_ONE) ? 6240 : 6234;

        npc.animation(new Animation(animationId, Animation.AnimationPriority.HIGH));
        World.get().submit(new Task(1, false) {
            @Override
            public void execute() {
                this.cancel();
                if(npc.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || npc.isDead() || victim.isDead())
                    return;
                new Projectile(npc, victim, 473, 50, 16, 61, 41, 0).sendProjectile();
            }
        });
        return new CombatSessionData(npc, victim, 1, CombatType.RANGED, false);
    }

    private CombatSessionData magic(EntityNode victim) {
        npc.setCurrentlyCasting(KALPHITE_QUEEN_BLAST);
        int animationId = npc.phase.equals(KalphiteQueen.Phase.PHASE_ONE) ? 6240 : 6234;
        int graphicId = npc.phase.equals(KalphiteQueen.Phase.PHASE_ONE) ? 278 : 279;

        npc.animation(new Animation(animationId, Animation.AnimationPriority.HIGH));
        npc.graphic(new Graphic(graphicId));

        World.get().submit(new Task(1, false) {
            @Override
            public void execute() {
                this.cancel();
                if(npc.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || npc.isDead() || victim.isDead())
                    return;
                KALPHITE_QUEEN_BLAST.projectile(npc, victim).get().sendProjectile();
            }
        });
        return new CombatSessionData(npc, victim, 1, CombatType.MAGIC, false);
    }
    /**
     * Executed when the {@link #npc} is hit by the {@code attacker}.
     * @param attacker the attacker whom hit the character.
     * @param data     the combat session data chained to this hit.
     */
    @Override
    public void incomingAttack(EntityNode attacker, CombatSessionData data) {
        KalphiteQueen.Phase phase = npc.phase;
        switch(data.getType()) {
            case MELEE:
                if(phase.equals(KalphiteQueen.Phase.PHASE_TWO)) {
                    Arrays.stream(data.getHits()).forEach(h -> h.setDamage(h.getDamage() / 2));
                }
                break;
            case RANGED:
                if(phase.equals(KalphiteQueen.Phase.PHASE_ONE)) {
                    Arrays.stream(data.getHits()).forEach(h -> h.setDamage(h.getDamage() / 2));
                }
                break;
            case MAGIC:
                if(phase.equals(KalphiteQueen.Phase.PHASE_ONE)) {
                    Arrays.stream(data.getHits()).forEach(h -> h.setDamage(h.getDamage() / 2));
                }
                break;
        }
    }

    /**
     * Determines the delay for when {@link #npc} will attack.
     * @return the value that the attack timer should be reset to.
     */
    @Override
    public int attackDelay() {
        return 6;
    }

    /**
     * Determines how close {@link #npc} must be to attack.
     * @return the radius that the npc must be in to attack.
     */
    @Override
    public int attackDistance() {
        return 8;
    }

    /**
     * The spell casted when a magic attack is executed.
     */
    private final CombatNormalSpell KALPHITE_QUEEN_BLAST = new CombatNormalSpell() {

        @Override
        public int spellId() {
            return -1;
        }

        @Override
        public int maximumHit() {
            return 330;
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
        public Optional<Projectile> projectile(EntityNode cast, EntityNode castOn) {
            return Optional.of(new Projectile(cast, castOn, 280, 50, 16, 61, 41, 0));
        }

        @Override
        public Optional<Graphic> endGraphic() {
            return Optional.empty();
        }

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
            return Optional.empty();
        }

        @Override
        public Optional<Item[]> equipmentRequired(Player player) {
            return Optional.empty();
        }
    };
}
