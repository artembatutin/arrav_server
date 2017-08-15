package net.edge.content.newcombat.strategy.player.magic;

import net.edge.content.newcombat.CombatEffect;
import net.edge.content.newcombat.attack.AttackModifier;
import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.content.MagicRune;
import net.edge.content.newcombat.content.MagicSpell;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.hit.Hit;
import net.edge.content.newcombat.strategy.basic.MagicStrategy;
import net.edge.content.skill.Skills;
import net.edge.net.packet.out.SendMessage;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BookSpellStrategy extends MagicStrategy<Player> {

    /** The magic spell definition. */
    private final MagicSpell spell;

    /** Whether or not this cast is single or auto */
    private final boolean singleCast;

    private static final Graphic SPLASH = new Graphic(85);

    /**
     * Constructs a new {@code SpellStrategy} from a {@link
     * MagicSpell}.
     *
     * @param spell the magic spell spell to be used.
     */
    public BookSpellStrategy(MagicSpell spell, boolean singleCast) {
        this.spell = spell;
        this.singleCast = singleCast;
    }

    @Override
    public boolean canAttack(Player attacker, Actor defender) {
        if (MagicRune.hasRunes(attacker, spell.getRunes())) {
            return true;
        }

        attacker.out(new SendMessage("You need some runes to cast this spell."));
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
        spell.getEffect().filter(filter).ifPresent(execute);
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
    public void finish(Player attacker, Actor defender, Hit[] hits) {
        if (singleCast) {
            attacker.getNewCombat().reset();
            // TODO: set default strategy here
        }
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        return new CombatHit[]{nextMagicHit(attacker, defender, spell.getMaxHit(), spell.getHitDelay(), spell.getHitsplatDelay())};
    }

    @Override
    public int getAttackDelay(AttackStance stance) {
        return 5;
    }

    @Override
    public int getAttackDistance(AttackStance stance) {
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

    public MagicSpell getSpell() {
        return spell;
    }
}