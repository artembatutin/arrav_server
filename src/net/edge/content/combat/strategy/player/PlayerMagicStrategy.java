package net.edge.content.combat.strategy.player;

import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.content.combat.CombatEffect;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.attack.AttackModifier;
import net.edge.content.combat.content.MagicRune;
import net.edge.content.combat.content.MagicSpell;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.basic.MagicStrategy;
import net.edge.content.skill.Skills;
import net.edge.net.packet.out.SendMessage;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PlayerMagicStrategy extends MagicStrategy<Player> {

    /** The magic spell definition. */
    private final MagicSpell spell;

    /** Whether or not this cast is single or auto */
    private final boolean singleCast;

    /** The spell splash graphic. */
    private static final Graphic SPLASH = new Graphic(85);

    /**
     * Constructs a new {@code SpellStrategy} from a {@link
     * MagicSpell}.
     *
     * @param spell the magic spell spell to be used.
     */
    public PlayerMagicStrategy(MagicSpell spell, boolean singleCast) {
        this.spell = spell;
        this.singleCast = singleCast;
    }

    @Override
    public boolean canAttack(Player attacker, Actor defender) {
        if (attacker.getRights() == Rights.ADMINISTRATOR || MagicRune.hasRunes(attacker, spell.getRunes())) {
            return true;
        }

        attacker.out(new SendMessage("You need some runes to cast this spell."));
        attacker.getNewCombat().reset();
        return false;
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        MagicRune.remove(attacker, spell.getRunes());
        addCombatExperience(attacker, spell.getBaseExperience(), hit);

        spell.getAnimation().ifPresent(attacker::animation);
        spell.getStart().ifPresent(attacker::graphic);
        spell.sendProjectile(attacker, defender);

        Predicate<CombatEffect> filter = effect -> effect.canEffect(attacker, defender, hit);
        Consumer<CombatEffect> execute = effect -> effect.execute(attacker, defender, hit);
        spell.getEffect().filter(Objects::nonNull).filter(filter).ifPresent(execute);
    }

    @Override
    public void hit(Player attacker, Actor defender, Hit hit, Hit[] hits) {
        if (!hit.isAccurate()) {
            defender.graphic(SPLASH);
        } else {
            spell.getEnd().ifPresent(defender::graphic);
        }
    }

    @Override
    public void block(Actor attacker, Player defender, Hit hit, Hit[] hits) {
        defender.animation(getBlockAnimation(defender, attacker));
    }

    @Override
    public void finish(Player attacker, Actor defender, Hit[] hits) {
        if (attacker.getNewCombat().getStrategy() == this) {
            if (singleCast) {
                attacker.getNewCombat().reset();
                WeaponInterface.setStrategy(attacker);
            }
        }
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        return new CombatHit[] { nextMagicHit(attacker, defender, spell.getMaxHit(), spell.getHitDelay(attacker, defender), spell.getHitsplatDelay()) };
    }

    @Override
    public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
        return 4;
    }

    @Override
    public int getAttackDistance(Player attacker, FightType fightType) {
        return 10;
    }

    @Override
    public Optional<AttackModifier> getModifier(Player attacker, Actor defender) {
        if (attacker.getSkills()[Skills.MAGIC].getRealLevel() > spell.getLevel()) {
            double modifier = (attacker.getSkills()[Skills.MAGIC].getRealLevel() - spell.getLevel()) * 0.003;
            return Optional.of(new AttackModifier().accuracy(modifier));
        }
        return Optional.empty();
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        if (attacker.getWeaponAnimation() != null && attacker.getWeaponAnimation().getAttacking()[0] != 422) {
            return new Animation(attacker.getWeaponAnimation().getAttacking()[attacker.getFightType().getStyle().ordinal()], Animation.AnimationPriority.HIGH);
        }
        return new Animation(attacker.getFightType().getAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    public Animation getBlockAnimation(Player attacker, Actor defender) {
        int animation = 404;
        if (attacker.getShieldAnimation() != null) {
            animation = attacker.getShieldAnimation().getBlock();
        } else if (attacker.getWeaponAnimation() != null) {
            animation = attacker.getWeaponAnimation().getBlocking();
        }
        return new Animation(animation, Animation.AnimationPriority.LOW);
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }

    public MagicSpell getSpell() {
        return spell;
    }
}