package net.edge.world.entity.actor.combat.strategy.player;

import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatImpact;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.CombatUtil;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.magic.MagicRune;
import net.edge.world.entity.actor.combat.magic.CombatSpell;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.basic.MagicStrategy;
import net.edge.world.entity.actor.combat.weapon.WeaponInterface;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PlayerMagicStrategy extends MagicStrategy<Player> {

	/**
	 * The magic spell definition.
	 */
	private final CombatSpell spell;

	/**
	 * The spell splash graphic.
	 */
	private static final Graphic SPLASH = new Graphic(85);

	/**
	 * Constructs a new {@code SpellStrategy} from a {@link CombatSpell}.
	 * @param spell the magic spell spell to be used.
	 */
	public PlayerMagicStrategy(CombatSpell spell) {
		this.spell = spell;
	}

	@Override
	public boolean canAttack(Player attacker, Actor defender) {
		if(attacker.getRights().equals(Rights.ADMINISTRATOR) || spell.canCast(attacker, defender)) {
			return true;
		}
		attacker.message("You need some runes to cast this spell.");
		attacker.getCombat().reset(false);
		return false;
	}

	@Override
	public void start(Player attacker, Actor defender, Hit[] hits) {
		if(attacker.getCombat().getDefender() == defender) {
			Animation animation = spell.getAnimation().orElse(getAttackAnimation(attacker, defender));
			attacker.animation(animation);
			spell.getStart().ifPresent(attacker::graphic);
			spell.sendProjectile(attacker, defender);

			if (spell.getEffect().isPresent()) {
				List<Hit> extra = new LinkedList<>();
				for (Hit hit : hits) {
					Predicate<CombatImpact> filter = effect -> effect.canAffect(attacker, defender, hit);
					Consumer<CombatImpact> execute = effect -> effect.impact(attacker, defender, hit, extra);
					spell.getEffect().filter(filter).ifPresent(execute);
				}
				if (extra.isEmpty()) {
					Collections.addAll(extra, hits);
					addCombatExperience(attacker, spell.getBaseExperience(), extra.toArray(new Hit[0]));
				} else {
					addCombatExperience(attacker, spell.getBaseExperience(), hits);
				}
			} else {
				addCombatExperience(attacker, spell.getBaseExperience(), hits);
			}

			if (attacker.isSingleCast()) {
				attacker.facePosition(defender.getPosition());
				attacker.setSingleCast(null);
				attacker.getCombat().reset(true);
			}
		}
	}

	@Override
	public void attack(Player attacker, Actor defender, Hit hit) {
		if(attacker.getCombat().getDefender() == defender) {
			MagicRune.remove(attacker, spell.getRunes());
		}
	}

	@Override
	public void hit(Player attacker, Actor defender, Hit hit) {
		if(!hit.isAccurate()) {
			defender.graphic(SPLASH);
		} else {
			spell.getEnd().ifPresent(defender::graphic);
		}
	}

	@Override
	public void finishOutgoing(Player attacker, Actor defender) {
		if(!attacker.isAutocast()) {
			attacker.getCombat().reset(false);
		}
	}

	@Override
	public CombatHit[] getHits(Player attacker, Actor defender) {
		int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
		int hitsplatDelay = CombatUtil.getHitsplatDelay(getCombatType());
		return new CombatHit[]{nextMagicHit(attacker, defender, spell.getMaxHit(), spell.getHitDelay().orElse(hitDelay), spell.getHitsplatDelay().orElse(hitsplatDelay))};
	}

	@Override
	public int getAttackDelay(Player attacker, Actor defender, FightType fightType) {
		if(attacker.getPosition().getDistance(defender.getPosition()) > 4) {
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
		if(attacker.getWeaponAnimation() != null && attacker.getWeaponAnimation().getAttacking()[0] != 422) {
			return new Animation(attacker.getWeaponAnimation().getAttacking()[attacker.getCombat().getFightType().getStyle().ordinal()], Animation.AnimationPriority.HIGH);
		}
		return new Animation(attacker.getCombat().getFightType().getAnimation(), Animation.AnimationPriority.HIGH);
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}

	public CombatSpell getSpell() {
		return spell;
	}
}