package net.edge.world.entity.actor.mob.impl.corp;

import net.edge.content.combat.CombatType;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.world.entity.region.Region;
import net.edge.world.locale.Boundary;
import net.edge.world.locale.Position;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.Hit;
import net.edge.world.Hit.HitIcon;
import net.edge.world.Hit.HitType;
import net.edge.world.Projectile;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.area.AreaManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The class which represents a single dark core minion.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DarkEnergyCore extends Mob {
	
	//TODO: Create a player ObjectList and hold players in the corp area?
	
	/**
	 * The corporeal beast this dark core is summoned by.
	 */
	private final CorporealBeast beast;
	
	/**
	 * The move task for this dark energy core.
	 */
	private final DarkEnergyCoreMoveTask moveTask;
	
	/**
	 * The leech task for this dark energy core.
	 */
	private final DarkEnergyCoreLeechTask leechTask;
	
	/**
	 * Constructs a new {@link CorporealBeast}.
	 * @param beast    {@link #beast}.
	 * @param position {@link #getPosition()}.
	 */
	public DarkEnergyCore(CorporealBeast beast, Position position) {
		super(8127, position);
		this.beast = beast;
		this.getMovementQueue().setLockMovement(true);
		this.moveTask = new DarkEnergyCoreMoveTask(this);
		this.leechTask = new DarkEnergyCoreLeechTask(this);
	}
	
	/**
	 * Gets the corporeal beast.
	 * @return the corporeal beast.
	 */
	public CorporealBeast getCorporealBeast() {
		return beast;
	}
	
	@Override
	public void register() {
		this.setRespawn(false);
		World.get().submit(moveTask);
		World.get().submit(leechTask);
	}
	
	@Override
	public void appendDeath() {
		moveTask.cancel();
		leechTask.cancel();
		super.appendDeath();
	}
	
	@Override
	public Mob create() {
		return new DarkEnergyCore(beast, this.getPosition());
	}
	
	/**
	 * The task that is responsible for handling functions for moving the dark
	 * energy core.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private final class DarkEnergyCoreMoveTask extends Task {
		
		/**
		 * The dark energy core this task is responsible for.
		 */
		private final DarkEnergyCore core;
		
		/**
		 * Constructs a new {@link DarkEnergyCoreMoveTask}.
		 * @param core {@link #core}.
		 */
		public DarkEnergyCoreMoveTask(DarkEnergyCore core) {
			super(10);
			this.core = core;
		}
		
		@Override
		protected void execute() {
			Region reg = core.getRegion().orElse(null);
			Player victim = null;
			if(reg != null && !reg.getPlayers().isEmpty()) {
				victim = RandomUtils.random(reg.getPlayers().stream().filter(p -> p.getPosition().withinDistance(core.getPosition(), 5)).collect(Collectors.toList()));
			}
			if(victim == null || core.getState() != EntityState.ACTIVE || victim.getState() != EntityState.ACTIVE || core.isDead() || victim.isDead()) {
				return;
			}
			if(core.isStunned()) {
				return;
			}
			
			core.setVisible(false);
			core.getMovementQueue().setLockMovement(false);
			Position position = victim.getPosition();
			new Projectile(core.getCenterPosition(), position, 0, 1828, 44, 4, 60, 43, 0, core.getInstance(), CombatType.MAGIC).sendProjectile();
			World.get().submit(new Task(2) {
				@Override
				protected void execute() {
					this.cancel();
					core.setVisible(true);
					core.setPosition(position);
					core.getMovementQueue().setLockMovement(true);
					core.leechTask.setDelay(3);
				}
				
			});
		}
		
	}
	
	/**
	 * The task that is responsible for leeching off the players hitpoints.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private final class DarkEnergyCoreLeechTask extends Task {
		
		/**
		 * The dark energy core this task is responsible for.
		 */
		private final DarkEnergyCore core;
		
		/**
		 * Constructs a new {@link DarkEnergyCoreMoveTask}.
		 * @param core {@link #core}.
		 */
		public DarkEnergyCoreLeechTask(DarkEnergyCore core) {
			super(3);
			this.core = core;
		}
		
		@Override
		protected void execute() {
			if(this.getDelay() == 3) {
				this.setDelay(1);//we give the players a small chance to move quickly away before getting hit severely.
			}
			if(core.isStunned()) {
				return;
			}
			if(core.getState() != EntityState.ACTIVE || core.isDead() || !core.isVisible()) {
				return;
			}
			Region reg = core.getRegion().orElse(null);
			if(reg == null)
				return;
			List<Player> possibleVictims = reg.getPlayers().stream().filter(p -> AreaManager.get().inArea(p.getPosition(), "CORPOREAL_BEAST") && new Boundary(p.getPosition(), p.size()).inside(core.getPosition(), 3)).collect(Collectors.toList());
			if(possibleVictims.isEmpty()) {
				return;
			}
			
			Player victim = RandomUtils.random(possibleVictims);
			if(victim.isDead() || victim.getState() != EntityState.ACTIVE) {
				return;
			}
			
			int hit = RandomUtils.inclusive(50, 100);
			victim.damage(new Hit(hit, HitType.NORMAL, HitIcon.NONE, victim.getSlot()));
			
			core.getCorporealBeast().healEntity(hit);
		}
		
	}
}
