package net.edge.world.entity.actor.mob.strategy.impl;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.magic.CombatSpell;
import net.edge.content.combat.magic.CombatSpells;
import net.edge.content.combat.magic.ancients.CombatAncientSpell;
import net.edge.content.minigame.nexchamber.NexMinigame;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.impl.SkeletalHorror;
import net.edge.world.entity.actor.mob.impl.nex.Nex;
import net.edge.world.entity.actor.mob.strategy.DynamicStrategy;
import net.edge.world.entity.actor.player.Player;

import java.util.Random;

public final class NexStrategy extends DynamicStrategy<Nex> {
	
	
	public NexStrategy(Nex npc) {
		super(npc);
	}

	@Override
	public boolean canIncomingAttack(Actor attacker) {
		return npc.minionStage == 1 && attacker.toPlayer().getMinigame().isPresent();
	}

	@Override
	public boolean canOutgoingAttack(Actor victim) {
		return npc.minionStage == 1 && victim.toPlayer().getMinigame().isPresent();
	}
	
	@Override
	public CombatHit outgoingAttack(Actor victim) {
//		if(victim.isPlayer()) {
//			Player player = victim.toPlayer();
//			Skill prayer = player.getSkills()[Skills.PRAYER];
//			prayer.decreaseLevel(1);
//			Skills.refresh(player, Skills.PRAYER);
//		}
		return type(npc, victim, npc.minionStage);
	}

	/**
	 * Represents the nex's 'smoke' phase.
	 * @param character
	 * @param victim
	 * @return
	 */
	public CombatHit smoke(Actor character, Actor victim) {
		if (victim.toPlayer().getMinigame().isPresent()) {
			if (RandomUtils.inclusive(100) < 25) {
				character.toMob().forceChat("Let the virus flow through you!");
					character.toMob().getRegion().getPlayers().forEach(p -> {
						if (p.getPosition().withinDistance(victim.getPosition(), 3)) {
							p.message(victim.toPlayer().getFormatUsername() +"'s virus infection is spreading and has infected you.");
						}
					});
			}
				character.setCurrentlyCasting(CombatSpells.SMOKE_BARRAGE.getSpell());
				character.animation(new Animation(6987, Animation.AnimationPriority.HIGH));
				return new CombatHit(character, victim, 1, CombatType.MAGIC, false);
			}
		return new CombatHit(character, victim, 1, CombatType.MAGIC, false);
	}

	/**
	 * Represents the nex's 'smoke' phase.
	 * @param character
	 * @param victim
	 * @return
	 */
	public CombatHit shadow(Actor character, Actor victim) {
		return new CombatHit(character, victim, 1, CombatType.MAGIC, false);
	}

	/**
	 * Represents the nex's 'smoke' phase.
	 * @param character
	 * @param victim
	 * @return
	 */
	public CombatHit blood(Actor character, Actor victim) {
		return new CombatHit(character, victim, 1, CombatType.MAGIC, false);
	}

	/**
	 * Represents the nex's 'smoke' phase.
	 * @param character
	 * @param victim
	 * @return
	 */
	public CombatHit ice(Actor character, Actor victim) {
		return new CombatHit(character, victim, 1, CombatType.MAGIC, false);
	}

	/**
	 * Represents the nex's 'smoke' phase.
	 * @param character
	 * @param victim
	 * @return
	 */
	public CombatHit finalPhase(Actor character, Actor victim) {
		return new CombatHit(character, victim, 1, CombatType.MAGIC, false);
	}

	/**
	 * Represents the nex's melee attacks.
	 * @param character
	 * @param victim
	 * @return
	 */
	public CombatHit melee(Actor character, Actor victim) {
		character.animation(new Animation(character.toMob().getDefinition().getAttackAnimation()));
		return new CombatHit(character, victim, 1, CombatType.MELEE, false);
	}

	private CombatHit type(Actor character, Actor victim, int phase) {
		if(RandomUtils.inclusive(100) > 50 && character.getPosition().withinDistance(victim.getPosition(), 2)) {
			phase = 0;
		}
		switch(phase) {
			case 1: // smoke
				return smoke(character, victim);
				//return smoke(character, victim);
			case 2: //shadow
				return shadow(character, victim);
			case 3: //blood
				return blood(character, victim);
			case 4:
				return ice(character, victim);
			case 5:
				return finalPhase(character, victim);
			default:
				return melee(character, victim);
		}
	}

	/**
	 * Represents incoming hits of nex.
	 * Nex's incoming hits are capped at 50.
	 * @param character the attacker whom hit the character.
	 * @param data     the combat session data chained to this hit.
	 */
	@Override
	public void incomingAttack(Actor character, CombatHit data) {
		for(int i=0; i < data.getHits().length; i++) {
			if (data.getHits()[i].getDamage() > 50) {
				data.getHits()[i].setDamage(50);
			}
		}
	}
	
	@Override
	public int attackDelay() {
		return 3;
	}
	
	@Override
	public int attackDistance() {
		return npc.getCombat().getCombatType() == CombatType.MELEE ? 2 : 8;
	}

}
