package net.edge.world.content.combat;

import net.edge.task.Task;
import net.edge.GameConstants;
import net.edge.world.World;
import net.edge.world.content.combat.effect.CombatEffectType;
import net.edge.world.content.combat.special.CombatSpecial;
import net.edge.world.content.combat.weapon.WeaponInterface;
import net.edge.world.content.minigame.MinigameHandler;
import net.edge.world.locale.Boundary;
import net.edge.world.locale.loc.Location;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.NpcAggression;
import net.edge.world.node.entity.player.Player;

/**
 * The combat session controls the mechanics of when and how the controller of
 * the builder will attack.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatSession extends Task {
	
	/**
	 * The builder assigned to this combat session.
	 */
	private final CombatBuilder builder;
	
	/**
	 * Create a new {@link CombatSession}.
	 * @param builder the builder assigned to this combat session.
	 */
	CombatSession(CombatBuilder builder) {
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
			CombatSessionData data = builder.getStrategy().outgoingAttack(builder.getCharacter(), builder.getVictim());
			
			if(builder.getCharacter().isPlayer() && !data.isIgnored()) {
				Player player = (Player) builder.getCharacter();
				player.getMessages().sendCloseWindows();
				
				if(builder.getVictim().isPlayer() && !MinigameHandler.getMinigame(builder.getVictim().toPlayer()).isPresent()) {
					if(!builder.getCharacter().getCombatBuilder().isBeingAttacked() || builder.getCharacter().getCombatBuilder().isBeingAttacked() && builder.getCharacter().getCombatBuilder().getAggressor() != builder.getVictim() && Location.inMultiCombat(builder.getCharacter())) {
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
				World.submit(new CombatSessionAttack(builder, data));
			}
			builder.resetAttackTimer();
			builder.getCharacter().faceEntity(builder.getVictim());
		}
	}
	
	/**
	 * Determines if the combat session can continue on, this method is invoked
	 * before the attack timers are evaluated.
	 * @return {@code true} if this combat session can continue on,
	 * {@code false} otherwise.
	 */
	private boolean sessionCanAttack() {
		if(builder.getVictim().getState() != NodeState.ACTIVE || builder.getCharacter().getState() != NodeState.ACTIVE || builder.getCharacter().isDead() || builder.getVictim().isDead()) {
			builder.reset();
			return false;
		}
		if(builder.getVictim().isPlayer()) {
			if(builder.getVictim().toPlayer().getTeleportStage() > 0) {
				builder.cooldown(false);
				return false;
			}
		}
		if(!Location.inMultiCombat(builder.getCharacter()) && builder.isBeingAttacked() && !builder.getVictim().equals(builder.getAggressor())) {
			if(builder.getCharacter().isPlayer())
				builder.getCharacter().toPlayer().message("You are already under attack!");
			builder.reset();
			return false;
		}
		if(!Location.inMultiCombat(builder.getCharacter()) && builder.getVictim().getCombatBuilder().isBeingAttacked() && !builder.getVictim().getCombatBuilder().getAggressor().equals(builder.getCharacter())) {
			if(builder.getCharacter().isPlayer())
				builder.getCharacter().toPlayer().message("They are already under attack!");
			builder.reset();
			return false;
		}
		if(builder.getCharacter().isPlayer()) {
			Player player = (Player) builder.getCharacter();
			
			if(Location.inWilderness(builder.getCharacter()) && !Location.inWilderness(builder.getVictim())) {
				player.message("They are not in the wilderness!");
				builder.reset();
				return false;
			}
		}
		if(builder.getCharacter().isNpc()) {
			Npc npc = (Npc) builder.getCharacter();
			boolean retreats = npc.getDefinition().retreats() && npc.getCombatBuilder().inCombat();
			if(builder.getVictim().getCombatBuilder().isCooldown() && !new Boundary(npc.getPosition(), npc.size()).within(npc.getPosition(), npc.size(), GameConstants.TARGET_DISTANCE) && retreats) {
				NpcAggression.retreat(npc);
				return false;
			}
		}
		return true;
	}
}
