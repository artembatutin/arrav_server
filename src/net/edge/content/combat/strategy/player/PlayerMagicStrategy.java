package net.edge.content.combat.strategy.player;

import net.edge.content.combat.CombatEffect;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.content.MagicRune;
import net.edge.content.combat.content.MagicSpells;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.basic.MagicStrategy;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.net.packet.out.SendMessage;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PlayerMagicStrategy extends MagicStrategy<Player> {

    /** The magic spell definition. */
    private final MagicSpells spell;

    /** The spell splash graphic. */
    private static final Graphic SPLASH = new Graphic(85);

    /**
     * Constructs a new {@code SpellStrategy} from a {@link
     * MagicSpells}.
     *
     * @param spell the magic spell spell to be used.
     */
    public PlayerMagicStrategy(MagicSpells spell) {
        this.spell = spell;
    }

    @Override
    public boolean canAttack(Player attacker, Actor defender) {
        if (attacker.getRights() == Rights.ADMINISTRATOR || spell.canCast(attacker, defender)) {
            return true;
        }

        attacker.out(new SendMessage("You need some runes to cast this spell."));
        attacker.getCombat().reset();
        return false;
    }

    @Override
    public void start(Player attacker, Actor defender, Hit[] hits) {
        if (attacker.getCombat().getDefender() == defender) {
            spell.getAnimation().ifPresent(attacker::animation);
            spell.getStart().ifPresent(attacker::graphic);
            spell.sendProjectile(attacker, defender);

            List<Hit> extra = new LinkedList<>();
            for (Hit hit : hits) {
                Predicate<CombatEffect> filter = effect -> effect.canEffect(attacker, defender, hit);
                Consumer<CombatEffect> execute = effect -> effect.execute(attacker, defender, hit, extra);
                spell.getEffect().filter(filter).ifPresent(execute);
            }

            Collections.addAll(extra, hits);
            addCombatExperience(attacker, spell.getBaseExperience(), extra.toArray(new Hit[0]));

            if (!attacker.isAutocast()) {
                WeaponInterface.setStrategy(attacker);
                attacker.getCombat().reset();
            }
        }
    }

    @Override
    public void attack(Player attacker, Actor defender, Hit hit) {
        if (attacker.getCombat().getDefender() == defender) {
            MagicRune.remove(attacker, spell.getRunes());
        }
    }

    @Override
    public void hit(Player attacker, Actor defender, Hit hit) {
        if (!hit.isAccurate()) {
            defender.graphic(SPLASH);
        } else {
            spell.getEnd().ifPresent(defender::graphic);
        }
    }

    @Override
    public CombatHit[] getHits(Player attacker, Actor defender) {
        int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
        int hitsplatDelay = CombatUtil.getHitsplatDelay(getCombatType());
        return new CombatHit[] { nextMagicHit(attacker, defender, spell.getMaxHit() * 10, spell.getHitDelay().orElse(hitDelay), spell.getHitsplatDelay().orElse(hitsplatDelay)) };
    }

    @Override
    public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
        if (attacker.getPosition().getDistance(defender.getPosition()) > 4) {
            return 6;
        }
        return 5;
    }

    @Override
    public int getAttackDistance(Player attacker, FightType fightType) {
        return 10;
    }

    @Override
    public Animation getAttackAnimation(Player attacker, Actor defender) {
        if (attacker.getWeaponAnimation() != null && attacker.getWeaponAnimation().getAttacking()[0] != 422) {
            return new Animation(attacker.getWeaponAnimation().getAttacking()[attacker.getCombat().getFightType().getStyle().ordinal()], Animation.AnimationPriority.HIGH);
        }
        return new Animation(attacker.getCombat().getFightType().getAnimation(), Animation.AnimationPriority.HIGH);
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }

    public MagicSpells getSpell() {
        return spell;
    }
}