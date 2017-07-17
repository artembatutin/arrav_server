package net.edge.content.combat;

import net.edge.task.Task;
import net.edge.GameConstants;
import net.edge.content.combat.effect.CombatEffectType;
import net.edge.content.combat.special.CombatSpecial;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.content.minigame.MinigameHandler;
import net.edge.world.locale.Boundary;
import net.edge.world.locale.loc.Location;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.MobAggression;
import net.edge.world.entity.actor.player.Player;

/**
 * The combat session controls the mechanics of when and how the controller of
 * the builder will attack.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatTask extends Task {
	
	/**
	 * The builder assigned to this combat session.
	 */
	private final CombatBuilder builder;
	
	/**
	 * Create a new {@link CombatTask}.
	 * @param builder the builder assigned to this combat session.
	 */
	CombatTask(CombatBuilder builder) {
		super(1, false);
		super.attach(builder.getCharacter().isPlayer() ? builder.getCharacter().toPlayer() : builder.getCharacter().toNpc());
		this.builder = builder;
	}
	
	@Override
	public void execute() {
		if(builder.isCooldown()) {
			builder.decrementCooldown();
			builder.decrementAttackTimer();
			
			if(builder.getCooldown() == 0)
				builder.reset();
			return;
		}
		builder.getCharacter().faceEntity(builder.getVictim());
		if(!sessionCanAttack()) {
			return;
		}
		if(builder.getCharacter().isPlayer()) {
			builder.determineStrategy();
			Player player = (Player) builder.getCharacter();
			if(player.isSpecialActivated() && player.getCombatSpecial() == CombatSpecial.GRANITE_MAUL)
				builder.clearAttackTimer();
		}
		builder.decrementAttackTimer();
		if(builder.getAttackTimer() < 1) {
			if(!Combat.checkAttackDistance(builder)) {
				return;
			}
			if(!builder.getStrategy().canIncomingAttack(builder.getCharacter(), builder.getVictim())) {
				return;
			}
			if(!builder.getStrategy().canOutgoingAttack(builder.getCharacter(), builder.getVictim())) {
				return;
			}
			if(!Combat.canBeAttacked(builder.getVictim(), builder.getCharacter())) {
				builder.reset();
				return;
			}
			if(builder.getCharacter().getPosition().getZ() != builder.getVictim().getPosition().getZ()) {
				builder.reset();
				this.cancel();
				return;
			}
			CombatHit data = builder.getStrategy().outgoingAttack(builder.getCharacter(), builder.getVictim());
			if(data == null) {
				return;
			}
			data.experience();
			if(builder.getCharacter().isPlayer() && !data.isIgnored()) {
				Player player = (Player) builder.getCharacter();
				player.closeWidget();
				
				if(builder.getVictim().isPlayer() && !MinigameHandler.getMinigame(builder.getVictim().toPlayer()).isPresent()) {
					if(!builder.getCharacter().getCombatBuilder().isBeingAttacked() || builder.getCharacter().getCombatBuilder().isBeingAttacked() && builder.getCharacter().getCombatBuilder().getAggressor() != builder.getVictim() && builder.getCharacter().inMulti()) {
						Combat.effect(builder.getCharacter(), CombatEffectType.SKULL);
					}
				}
				
				if(player.isSpecialActivated() && player.getCastSpell() == null) {
					data = player.getCombatSpecial().container(player, builder.getVictim());
					CombatSpecial.drain(player, player.getCombatSpecial().getAmount());
				}
			}
			
			if(data.getType() != null && !data.isIgnored()) {
				builder.getVictim().getCombatBuilder().setAggressor(builder.getCharacter());
				builder.getVictim().getLastCombat().reset();
				
				if(data.getType() == CombatType.MAGIC && builder.getCharacter().isPlayer() && !builder.getCharacter().toPlayer().getWeapon().equals(WeaponInterface.SALAMANDER)) {
					Player player = (Player) builder.getCharacter();
					
					if(player.getCastSpell() != null && player.getAutocastSpell() != null || player.getAutocastSpell() == null) {
						if(!player.isSpecialActivated()) {
							player.getCombatBuilder().cooldown(false);
						}
						player.setCastSpell(null);
						player.setFollowing(false);
						builder.determineStrategy();
					}
				}
				World.get().submit(new CombatHitTask(builder, data));
			}
			builder.resetAttackTimer();
		}
	}
	
	/**
	 * Determines if the combat session can continue on, this method is invoked
	 * before the attack timers are evaluated.
	 * @return {@code true} if this combat session can continue on,
	 * {@code false} otherwise.
	 */
	private boolean sessionCanAttack() {
		if(builder == null)
			return false;
		if(builder.getCharacter() == null)
			return false;
		if(builder.getVictim() == null)
			return false;
		if(builder.getVictim().getState() != EntityState.ACTIVE || builder.getCharacter().getState() != EntityState.ACTIVE || builder.getCharacter().isDead() || builder.getVictim().isDead()) {
			builder.reset();
			return false;
		}
		if(builder.getVictim().isPlayer()) {
			if(builder.getVictim().toPlayer().getTeleportStage() > 0) {
				builder.cooldown(false);
				return false;
			}
		}
		if(!builder.getCharacter().inMulti() && builder.isBeingAttacked() && !builder.getVictim().same(builder.getAggressor())) {
			if(builder.getCharacter().isPlayer()) {
				Player player = builder.getCharacter().toPlayer();
				player.message("You are already under attack!");
				player.getMovementQueue().reset();
			}
			builder.reset();
			return false;
		}
		if(!builder.getCharacter().inMulti() && builder.getVictim() != null && builder.getVictim().getCombatBuilder().isBeingAttacked() && builder.getVictim().getCombatBuilder().getAggressor() != null && !builder.getVictim().getCombatBuilder().getAggressor().same(builder.getCharacter())) {
			if(builder.getCharacter().isPlayer()) {
				Player player = builder.getCharacter().toPlayer();
				player.message("You are already under attack!");
				player.getMovementQueue().reset();
			}
			builder.reset();
			return false;
		}
		if(builder.getCharacter().isPlayer()) {
			Player player = (Player) builder.getCharacter();
			if(builder.getCharacter().inWilderness() && !builder.getVictim().inWilderness()) {
				player.message("They are not in the wilderness!");
				player.getMovementQueue().reset();
				builder.reset();
				return false;
			}
		}
		if(builder.getCharacter().isNpc()) {
			Mob mob = (Mob) builder.getCharacter();
			boolean retreats = mob.getDefinition().retreats() && mob.getCombatBuilder().inCombat();
			if(builder.getVictim().getCombatBuilder().isCooldown() && !new Boundary(mob.getPosition(), mob.size()).within(mob
					.getPosition(), mob.size(), GameConstants.TARGET_DISTANCE) && retreats) {
				MobAggression.retreat(mob);
				return false;
			}
		}
		return true;
	}
}
