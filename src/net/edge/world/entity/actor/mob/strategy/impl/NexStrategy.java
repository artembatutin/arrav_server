package net.edge.world.entity.actor.mob.strategy.impl;

import com.google.common.graph.Graph;
import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.magic.CombatSpells;
import net.edge.content.minigame.Minigame;
import net.edge.content.minigame.nexchamber.NexMinigame;
import net.edge.net.packet.out.SendGraphic;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.world.*;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.impl.SkeletalHorror;
import net.edge.world.entity.actor.mob.impl.nex.Nex;
import net.edge.world.entity.actor.mob.strategy.DynamicStrategy;
import net.edge.world.entity.actor.move.ForcedMovement;
import net.edge.world.entity.actor.move.ForcedMovementDirection;
import net.edge.world.entity.actor.move.ForcedMovementManager;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

import java.util.Random;

public final class NexStrategy extends DynamicStrategy<Nex> {
	
	
	public NexStrategy(Nex npc) {
		super(npc);
	}

	@Override
	public boolean canIncomingAttack(Actor attacker) {
		return npc.minionStage > 0 && attacker.toPlayer().getMinigame().isPresent();
	}

	@Override
	public boolean canOutgoingAttack(Actor victim) {
		return npc.minionStage > 0 && victim.toPlayer().getMinigame().isPresent();
	}
	
	@Override
	public CombatHit outgoingAttack(Actor victim) {

		return type(npc, victim, npc.minionStage);
	}


	/**
	 * NO ESCAPE is available during all nex's phases
	 *
	 * @param character
	 * @param victim
	 * @return {@link CombatHit}
	 * @TODO: MORE EFFICIENT WAY  OF HANDLING SPAWN LOC AND CHECK IF IN PATH.
	 * SQUARE LOCATION, X => &&  Y =>
	 *
	 */

	private CombatHit noEscape(Actor character, Actor victim) {
		Position[] startPos = {
				new Position(2914, 5202), new Position(2934, 5202),
				new Position(2924, 5192), new Position(2924, 5222)};
		Position pos = RandomUtils.random(startPos);
		character.getCombat().cooldownTimer(true, 3);
		victim.getCombat().cooldownTimer(true, 3);
		character.animation(new Animation(6321));
		CombatHit session = new CombatHit(character, victim, 1, CombatType.MELEE, false);
		World.get().submit(new Task(3, false) {
			ForcedMovement mov;
			int loops;
			@Override
			protected void execute() {
				switch (loops) {
					case 0:
						character.move(pos);
						character.forceChat("NO ESCAPE!");
						mov = ForcedMovement.create(character, character.getPosition().move(20, 0), new Animation(6321));
						mov.setSecondSpeed(70);
						ForcedMovementManager.submit(character, mov);
						break;
					case 1:
						if (pos.getX() < 2924 || pos.getX() > 2924) {
							Hit hit = CombatUtil.calculateRandomHit(character, victim, CombatType.MELEE, 0, false);
							int damage = (hit.getDamage() + RandomUtils.inclusive(20, 30));
							NexMinigame.getPlayers().forEach(p -> {
								if (p.getPosition().getY() > 5202 || p.getPosition().getY() < 5204) {
									p.getCombat().getDamageCache().add(character, damage);
									p.damage(hit);
								}
							});
						}
						character.move(new Position(2924, 5202));
						character.animation(null);
						this.cancel();
						break;
				}
				loops++;
			}
		});
		session.ignore();
		return session;
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
					character.toMob().getRegion().ifPresent(r -> {
						r.getPlayers().forEach(p -> {
							if (p.getPosition().withinDistance(victim.getPosition(), 3)) {
								p.message(victim.toPlayer().getFormatUsername() +"'s virus infection is spreading and has infected you.");
							}
						});
					});
			}
			character.setCurrentlyCasting(CombatSpells.SMOKE_BARRAGE.getSpell());
			character.animation(new Animation(6987, Animation.AnimationPriority.HIGH));
			CombatUtil.playersWithinDistance(victim, p -> character.getCurrentlyCasting().projectile(character, p), 5);
			return new CombatHit(character, victim, 1, CombatType.MAGIC, false) {
				@Override
				public CombatHit preAttack() {
					CombatUtil.playersWithinDistance(victim, p -> {
								p.getCombat().getDamageCache().add(character, CombatUtil.calculateRandomHit(character, p, CombatType.MAGIC, 0, false).getDamage());
								p.damage(CombatUtil.calculateRandomHit(character, p, CombatType.MAGIC, 0, false));
							}, 5
					);
					return this;
				}
			};
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
		int random = RandomUtils.inclusive(100);
		character.animation(new Animation(6987, Animation.AnimationPriority.HIGH));
		if(random > 50) {
			new Projectile(character, victim, 380, 44, 0, 43, 20, 0, CombatType.RANGED).sendProjectile();
			return new CombatHit(character, victim, 1, CombatType.RANGED, false);
		}
		if(random > 70) {
		    fearShadow(character, victim);
        }
			return new CombatHit(character, victim, 1, CombatType.RANGED, false) {
				@Override
				public CombatHit preAttack() {
				CombatUtil.playersWithinDistance(character, p -> {
					new Projectile(character, p, 380, 44, 0, 43, 20, 0, CombatType.RANGED).sendProjectile();
					p.getCombat().getDamageCache().add(character, CombatUtil.calculateRandomHit(character, p, CombatType.RANGED, 0, false).getDamage());
					p.damage(CombatUtil.calculateRandomHit(character, p, CombatType.RANGED, 0, false));
					}, 10);
				this.ignore();
				return this;
			}
			};
	}

	public void fearShadow(Actor character, Actor victim) {
        for (Player p : NexMinigame.getPlayers()) {
            Position originalVictimPosition = p.getPosition();
            SendGraphic.local(p, 383, originalVictimPosition, 0);
            World.get().submit(new Task(1, false) {
                @Override
                protected void execute() {
                    if (p.getPosition() == originalVictimPosition) {
                        p.getCombat().getDamageCache().add(character, CombatUtil.calculateRandomHit(character, p, CombatType.RANGED, 0, false).getDamage());
                        p.damage(CombatUtil.calculateRandomHit(character, p, CombatType.RANGED, 0, false));
                    }
                }
                //new Projectile(npc.getCenterPosition(), originalVictimPosition, 0, 1824, 44, 3, 60, 36, 0, npc.getInstance(), CombatType.MAGIC).sendProjectile();
            });
        }
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
		int random = RandomUtils.inclusive(100);
		if(random > 50 && character.getPosition().withinDistance(victim.getPosition(), 2)) {
//			phase = 0;
//		} else if (random < 10) {
			return noEscape(character, victim);
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
		return 5;
	}
	
	@Override
	public int attackDistance() {
		return npc.getCombat().getCombatType() == CombatType.MELEE ? 2 : 8;
	}

}
