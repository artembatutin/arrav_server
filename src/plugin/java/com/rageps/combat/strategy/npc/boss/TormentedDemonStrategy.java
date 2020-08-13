package com.rageps.combat.strategy.npc.boss;

import com.rageps.combat.strategy.MobCombatStrategyMeta;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.projectile.CombatProjectile;
import com.rageps.combat.strategy.npc.NpcMeleeStrategy;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import com.rageps.task.Task;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.combat.hit.CombatHit;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.combat.strategy.CombatStrategy;
import com.rageps.combat.strategy.npc.MultiStrategy;
import com.rageps.combat.strategy.npc.NpcMagicStrategy;
import com.rageps.combat.strategy.npc.NpcRangedStrategy;
import com.rageps.world.entity.actor.mob.Mob;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Dave/Ophion
 * Date: 05/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
@MobCombatStrategyMeta(ids = {8349, 8350, 8351})
public class TormentedDemonStrategy extends MultiStrategy {
	
	/**
	 * Caches all the damage, Required to calculate the prayer switch.
	 */
	private static final Object2ObjectOpenHashMap<CombatType, Integer> DAMAGE = new Object2ObjectOpenHashMap<>();
	
	/**
	 * The task which is dependant for switching attack styles for this demon.
	 */
	private Optional<Task> switchAttackTask = Optional.empty();
	
	/**
	 * The task which is dependant for restoring the tormented demon's shield.
	 */
	private Optional<Task> shieldRestorationTask = Optional.empty();
	
	/**
	 * Represents the id for the melee demon.
	 */
	public static final int MELEE_DEMON = 8349;
	
	/**
	 * Represents the id for the magic demon.
	 */
	public static final int MAGIC_DEMON = 8350;
	
	/**
	 * Represents the id for the ranged demon.
	 */
	public static final int RANGED_DEMON = 8351;
	
	/**
	 * Represents an array of all the demons.
	 */
	public static final int[] STATES = new int[]{MELEE_DEMON, MAGIC_DEMON, RANGED_DEMON};
	
	private static final Melee MELEE = new Melee();
	private static final Ranged RANGED = new Ranged();
	private static final Magic MAGIC = new Magic();
	
	static final CombatStrategy<Mob>[] FULL_STRATEGIES = createStrategyArray(MELEE, MAGIC, RANGED);
	static final CombatStrategy<Mob>[] NON_MELEE = createStrategyArray(MAGIC, RANGED);
	
	public TormentedDemonStrategy() {
		currentStrategy = randomStrategy(NON_MELEE);
	}
	
	@Override
	public boolean canAttack(Mob attacker, Actor defender) {
		return defender.isPlayer();
	}
	
	@Override
	public void start(Mob attacker, Actor defender, Hit[] hit) {
		if(!switchAttackTask.isPresent()) {
			switchAttackTask = Optional.of(new SwitchAttackTask(this, attacker));
			World.get().submit(switchAttackTask.get());
		}
		currentStrategy.start(attacker, defender, hit);
	}
	
	@Override
	public void block(Actor attacker, Mob defender, Hit hit, CombatType combatType) {
		if(!shieldRestorationTask.isPresent()) {
			defender.graphic(new Graphic(1885));
		}
		
		if(attacker.isPlayer() && !shieldRestorationTask.isPresent()) {
			Arrays.stream(attacker.getStrategy().getHits(attacker, defender)).forEach(h -> {
				h.modifyDamage(damage -> (int) (h.getDamage() * 0.25));// 75% is absorbed
				attacker.toPlayer().message("The demon's shield absorbs the majority of the damage.");
			});
		}
		
		if(attacker.toPlayer().getEquipment().containsAny(2402, 6746) && Arrays.stream(attacker.getStrategy().getHits(attacker, defender)).anyMatch(h -> h.getDamage() > 0) && !isProtectingMelee(defender)) {
			attacker.toPlayer().message("The demon is temporarily weakened by your weapon.");
			shieldRestorationTask = Optional.of(new ShieldRestorationTask(this, defender));
			World.get().submit(shieldRestorationTask.get());
		}
		
		if(!DAMAGE.containsKey(combatType)) {
			DAMAGE.put(combatType, (hit.getDamage() < 20 ? 20 : hit.getDamage()));
		} else {
			DAMAGE.put(combatType, DAMAGE.get(combatType) + (hit.getDamage() < 20 ? 20 : hit.getDamage()));
		}
		
		if(DAMAGE.get(combatType) > 310) {
			DAMAGE.remove(combatType);
			switchState(defender);
		}
		
		if(getProtectedFrom(defender).equals(combatType)) {
			Arrays.stream(attacker.getStrategy().getHits(attacker, defender)).filter(Objects::nonNull).forEach(h -> h.setAccurate(false));
			return;
		}
		
		defender.getCombat().attack(attacker);
		currentStrategy.block(attacker, defender, hit, combatType);
	}
	
	/**
	 * Determines if this demon has the protect melee prayer on.
	 * @param mob the mob to determine this for.
	 * @return true if the mob has the protect melee prayer on, false otherwise.
	 */
	private boolean isProtectingMelee(Mob mob) {
		int id = mob.getTransform().isPresent() ? mob.getTransform().getAsInt() : mob.getId();
		return id == MELEE_DEMON;
	}
	
	/**
	 * Switches the {@code npc}s state to a random id from the {@link #STATES} array.
	 */
	private void switchState(Mob mob) {
		mob.transform(RandomUtils.random(STATES));
	}
	
	/**
	 * Gets the protected prayer currently active for the specified {@code npcId}.
	 * @param mob the mob to return the protected prayer from.
	 * @return the combat type protected from.
	 */
	public static CombatType getProtectedFrom(Mob mob) {
		int id = mob.getTransform().isPresent() ? mob.getTransform().getAsInt() : mob.getId();
		switch(id) {
			case MELEE_DEMON:
				return CombatType.MELEE;
			case RANGED_DEMON:
				return CombatType.RANGED;
			case MAGIC_DEMON:
				return CombatType.MAGIC;
		}
		return CombatType.NONE;
	}
	
	/**
	 * Represents the Melee {@link CombatStrategy} of Tormented demon
	 */
	private static final class Melee extends NpcMeleeStrategy {
		@Override
		public int getAttackDistance(Mob attacker, FightType fightType) {
			return 2;
		}
		
		@Override
		public CombatHit[] getHits(Mob attacker, Actor defender) {
			return new CombatHit[]{nextMeleeHit(attacker, defender)};
		}
	}
	
	/**
	 * Represents the Ranged {@link CombatStrategy} of Tormented Demon
	 */
	private static final class Ranged extends NpcRangedStrategy {
		
		public Ranged() {
			super(CombatProjectile.getDefinition("Tormented_Demon Ranged"));
		}
		
	}
	
	/**
	 * Represents the Magic {@link CombatStrategy} of Tormented Demon
	 */
	private static final class Magic extends NpcMagicStrategy {
		public Magic() {
			super(CombatProjectile.getDefinition("Tormented_Demon Magic"));
		}
	}
	
	/**
	 * The task which is responsible for switching restoring the
	 * tormented demon's shield.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class ShieldRestorationTask extends Task {
		
		/**
		 * The strategy this task is dependant of.
		 */
		private final TormentedDemonStrategy strategy;
		
		private final Mob mob;
		
		/**
		 * Constructs a new {@link SwitchAttackTask}.
		 * @param strategy {@link #strategy}.
		 */
		private ShieldRestorationTask(TormentedDemonStrategy strategy, Mob mob) {
			super(100);
			this.strategy = strategy;
			this.mob = mob;
		}
		
		@Override
		protected void execute() {
			this.cancel();
			if(mob.getState() != EntityState.ACTIVE || mob.isDead() || mob.getCombat().getDefender() == null) {
				return;
			}
			
			Actor entity = mob.getCombat().getDefender();
			
			entity.ifPlayer(pl -> pl.message("The demon restored it's shield."));
		}
		
		@Override
		protected void onCancel() {
			strategy.shieldRestorationTask = Optional.empty();
		}
	}
	
	/**
	 * The task which is responsible for switching attack styles for
	 * the tormented demon.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class SwitchAttackTask extends Task {
		
		/**
		 * The strategy this task is dependant of.
		 */
		private final TormentedDemonStrategy strategy;
		private final Mob mob;
		
		/**
		 * Constructs a new {@link SwitchAttackTask}.
		 * @param strategy {@link #strategy}.
		 */
		private SwitchAttackTask(TormentedDemonStrategy strategy, Mob mob) {
			super(30);
			this.strategy = strategy;
			this.mob = mob;
		}
		
		@Override
		protected void execute() {
			this.cancel();
			if(mob.getState() != EntityState.ACTIVE || mob.isDead() || mob.getCombat().getDefender() == null) {
				return;
			}
			
			mob.getCombat().resetTimers();
			mob.animation(new Animation(10917, Animation.AnimationPriority.HIGH));
			strategy.currentStrategy = randomStrategy(MELEE.withinDistance(mob, mob.getCombat().getDefender()) ? TormentedDemonStrategy.FULL_STRATEGIES : TormentedDemonStrategy.NON_MELEE);
		}
	}
}
