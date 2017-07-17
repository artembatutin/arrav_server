package net.edge.content.combat.strategy.npc;

import com.google.common.collect.ImmutableSet;
import net.edge.content.combat.Combat;
import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.task.LinkedTaskSequence;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Hit;
import net.edge.world.Projectile;
import net.edge.world.node.actor.Actor;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 8-7-2017.
 */
public final class NomadCombatStrategy implements CombatStrategy {

    private static final Animation anim2 = new Animation(12696, Animation.AnimationPriority.HIGH);
    private static final Animation CHARGE_ANIMATION = new Animation(12698, Animation.AnimationPriority.HIGH);
    private static final Graphic CHARGE_GRAPHIC = new Graphic(2281);
    private static final Graphic MAGIC_GRAPHIC = new Graphic(369);

    private Charge charge;

    private int totalDamage;

    private final List<Actor> victims = new ArrayList<>();

    /**
     * Executed when the {@code character} is hit by the {@code attacker}.
     * @param character the character being hit.
     * @param attacker  the attacker whom hit the character.
     * @param data      the combat session data chained to this hit.
     */
    @Override
    public void incomingAttack(Actor character, Actor attacker, CombatHit data) {
        if(charge != null) {
            switch(charge) {
                case SARADOMIN:
                    for(Hit h : data.getHits()) {
                        h.setDamage(h.getDamage() * 2);
                    }
                    break;
                case ZAMORAK:
                    for(Hit h : data.getHits()) {
                        totalDamage += h.getDamage();
                        h.setAccurate(false);
                    }
                    break;
                case REBOUND:
                    for(Hit h : data.getHits()) {
                        totalDamage += h.getDamage();
                        victims.add(attacker);
                    }
                    break;
            }
            return;
        }
        if(RandomUtils.inclusive(100) < 10) {
            for(Hit h : data.getHits()) {
                h.setAccurate(false);
            }

            attacker.ifPlayer(p -> p.message("The nomad completely blocks your attack."));
        }
    }

    /**
     * Executed when the {@code character} is hit by the {@code attacker}.
     * @param character the character being hit.
     * @param attacker  the attacker whom hit the character.
     */
    @Override
    public boolean canIncomingAttack(Actor character, Actor attacker) {
        return true;
    }

    /**
     * Determines if {@code character} is able to make an attack on
     * {@code victim}.
     *
     * @param character the character to has if able.
     * @param victim    the character being attacked.
     * @return {@code true} if an attack can be made, {@code false} otherwise.
     */
    @Override
    public boolean canOutgoingAttack(Actor character, Actor victim) {
        return charge == null;
    }

    /**
     * Executed when {@code character} has passed the initial {@code canAttack}
     * check and is about to attack {@code victim}.
     *
     * @param character the character that is attacking.
     * @param victim    the character being attacked.
     * @return a container holding the data for the attack.
     */
    @Override
    public CombatHit outgoingAttack(Actor character, Actor victim) {
        if(RandomUtils.inclusive(100) < 5) {
            return charge(character);
        } else if(RandomUtils.inclusive(100) < 5) {
            return superCharge(character);
        }
        CombatType[] data = character.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE} : new CombatType[]{CombatType.MAGIC};
        CombatType type = RandomUtils.random(data);
        return type(character, victim, type);
    }

    private CombatHit type(Actor character, Actor victim, CombatType type) {
        switch(type) {
            case MELEE:
                return melee(character, victim);
            case MAGIC:
                return magic(character, victim);
            default:
                return magic(character, victim);
        }
    }

    private CombatHit melee(Actor character, Actor victim) {
        int animationId = character.toNpc().getDefinition().getAttackAnimation();
        character.animation(new Animation(animationId, Animation.AnimationPriority.HIGH));
        return new CombatHit(character, victim, 1, CombatType.MELEE, true);
    }

    private CombatHit charge(Actor character) {
        charge = Charge.random();
        character.getMovementQueue().setLockMovement(true);
        character.graphic(CHARGE_GRAPHIC);
        character.animation(CHARGE_ANIMATION);
        LinkedTaskSequence seq = new LinkedTaskSequence();
        seq.connect(1, () -> character.forceChat(charge.message));
        seq.connect(4, () -> {
            switch(charge) {
                case SARADOMIN:
                    break;
                case ZAMORAK:
                    if(!character.isDead()) {
                        character.healEntity(totalDamage);
                    }
                    break;
                case REBOUND:
                    for(Actor victim : victims) {
                        if(!character.isDead() && !victim.isDead() && victim.getPosition().withinDistance(character.getPosition(), 15)) {
                            victim.damage(new Hit(totalDamage * 2));
                        }
                    }
                    break;
            }
            charge = null;
            totalDamage = 0;
            victims.clear();
            character.getMovementQueue().setLockMovement(false);
        });
        seq.start();
        return null;
    }

    private CombatHit superCharge(Actor character) {
        charge = Charge.SUPER;
        character.getMovementQueue().setLockMovement(true);
        character.graphic(CHARGE_GRAPHIC);
        character.animation(CHARGE_ANIMATION);
        LinkedTaskSequence seq = new LinkedTaskSequence();
        seq.connect(1, () -> {
            character.graphic(CHARGE_GRAPHIC);
            character.animation(CHARGE_ANIMATION);
        });
        seq.connect(4, () -> {
            character.graphic(CHARGE_GRAPHIC);
            character.animation(CHARGE_ANIMATION);
        });
        seq.connect(3, () -> character.forceChat(charge.message));
        seq.connect(2, () -> {
            character.forceChat("Die!");
            character.animation(new Animation(12697, Animation.AnimationPriority.HIGH));
            character.graphic(new Graphic(65565));
        });
        seq.connect(1, () -> {
            Combat.damagePlayersWithin(character, character.getPosition(), 5, 1, CombatType.NONE, false/*, p -> new Projectile(character, p, 2283, 44, 3, 43, 31, 0).sendProjectile()*/);
            charge = null;
            character.getMovementQueue().setLockMovement(false);
        });
        seq.start();
        return null;
    }

    private CombatHit magic(Actor character, Actor victim) {
        character.animation(new Animation(12697, Animation.AnimationPriority.HIGH));
        character.forceChat("Freeze!");
        character.setCurrentlyCasting(SPELL);
        return new CombatHit(character, victim, 1, CombatType.MAGIC, true) {
            @Override
            public CombatHit preAttack() {
                if(this.getType() == CombatType.MAGIC && victim.isPlayer() && this.isAccurate()) {
                    Player player = (Player) victim;
                    player.graphic(MAGIC_GRAPHIC);
                    player.freeze(15);
                }
                return this;
            }
        };
    }

    /**
     * Determines the delay for when {@code character} will attack.
     *
     * @param character the character waiting to attack.
     * @return the value that the attack timer should be reset to.
     */
    @Override
    public int attackDelay(Actor character) {
        return character.getAttackSpeed();
    }

    /**
     * Determines how close {@code character} must be to attack.
     *
     * @param character the character that is attacking.
     * @return the radius that the character must be in to attack.
     */
    @Override
    public int attackDistance(Actor character) {
        return 6;
    }

    /**
     * The NPCs that will be assigned this combat strategy.
     * @return the array of assigned NPCs.
     */
    @Override
    public int[] getNpcs() {
        return new int[]{8528};
    }

    private enum Charge {
        SARADOMIN("Saradomin... aid me!"),
        ZAMORAK("Zamorak.. aid me!"),
        REBOUND("Zaros.. revenge me!"),
        SUPER("Almost... almost there!");

        private final String message;

        private static final ImmutableSet<Charge> REGULAR_CHARGE = ImmutableSet.copyOf(new Charge[]{SARADOMIN, ZAMORAK, REBOUND});

        Charge(String message) {
            this.message = message;
        }

        static Charge random() {
            return RandomUtils.random(REGULAR_CHARGE.asList());
        }
    }

    private static final CombatNormalSpell SPELL = new CombatNormalSpell() {

        @Override
        public int spellId() {
            return 0;
        }

        @Override
        public int maximumHit() {
            return 450;
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
}
