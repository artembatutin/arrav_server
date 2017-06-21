package net.edge.world.node.entity.npc.strategy.impl;

import net.edge.content.combat.CombatSessionData;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.task.Task;
import net.edge.world.*;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.strategy.DynamicCombatStrategy;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

import static net.edge.util.rand.RandomUtils.*;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 19-6-2017.
 */
public final class WildyWyrmCombatStrategy extends DynamicCombatStrategy<Npc> {

    /**
     * Constructs a new {@link DynamicCombatStrategy}.
     *
     * @param npc {@link #npc}.
     */
    public WildyWyrmCombatStrategy(Npc npc) {
        super(npc);
    }

    /**
     * The combat session data which returns a combat session data dependant on the specified
     * {@code type}.
     * @param victim    the victim being hit.
     * @param type      the type of combat being attacked with.
     * @return the combat session data.
     */
    private CombatSessionData type(EntityNode victim, CombatType type) {
        switch(type) {
            case MELEE:
                return melee(victim);
            case MAGIC:
                return magic(victim);
            default:
                return magic(victim);
        }
    }

    private CombatSessionData melee(EntityNode victim) {
        npc.animation(new Animation(12791, Animation.AnimationPriority.HIGH));
        return new CombatSessionData(npc, victim, 1, CombatType.MELEE, false);
    }

    private CombatSessionData magic(EntityNode victim) {
        npc.animation(new Animation(12794));
        World.get().submit(new Task(1, false) {
            @Override
            public void execute() {
                this.cancel();
                if(npc.getState() != NodeState.ACTIVE || victim.getState() != NodeState.ACTIVE || npc.isDead() || victim.isDead())
                    return;
                SPELL.projectile(npc, victim).get().sendProjectile();
            }
        });
        npc.setCurrentlyCasting(SPELL);
        return new CombatSessionData(npc, victim, 1, CombatType.MAGIC, false);
    }

    /**
     * Determines if {@link #npc} can attack the specified {@code victim}.
     *
     * @param victim the victim to determine this for.
     * @return {@code true} if the {@link #npc} can, {@code false} otherwise.
     */
    @Override
    public boolean canOutgoingAttack(EntityNode victim) {
        return true;
    }

    /**
     * Executed when {@link #npc} has passed the initial {@code canAttack}
     * check and is about to attack {@code victim}.
     *
     * @param victim the character being attacked.
     * @return a container holding the data for the attack.
     */
    @Override
    public CombatSessionData outgoingAttack(EntityNode victim) {
        CombatType[] types = npc.getPosition().withinDistance(victim.getPosition(), 4) ? new CombatType[]{CombatType.MELEE, CombatType.MAGIC} : new CombatType[]{CombatType.MAGIC};
        return type(victim, random(types));
    }

    /**
     * Executed when the {@link #npc} is hit by the {@code attacker}.
     *
     * @param attacker the attacker whom hit the character.
     * @param data     the combat session data chained to this hit.
     */
    @Override
    public void incomingAttack(EntityNode attacker, CombatSessionData data) {

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
        public Optional<Projectile> projectile(EntityNode cast, EntityNode castOn) {
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
