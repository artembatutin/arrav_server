package net.edge.world.entity.actor.mob;

import net.edge.content.PlayerPanel;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.skill.slayer.Slayer;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.entity.actor.ActorDeath;
import net.edge.world.entity.actor.mob.drop.DropManager;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.edge.content.achievements.Achievement.*;

/**
 * The {@link ActorDeath} implementation that is dedicated to managing the
 * death process for all {@link Mob}s.
 * @author lare96 <http://github.com/lare96>
 */
public final class MobDeath extends ActorDeath<Mob> {
	
	/**
	 * Creates a new {@link MobDeath}.
	 * @param mob the NPC who has died and needs the death process.
	 */
	public MobDeath(Mob mob) {
		super(mob);
	}
	
	/**
	 * An array of npcs which don't drop any items.
	 */
	private static final List<Integer> NON_DROPPABLES = Arrays.asList(2025, 2026, 2027, 2028, 2029, 2030);
	
	@Override
	public void preDeath() {
		if(getActor().getCombat().getDefender() != null) {
			getActor().getCombat().getDefender().getCombat().reset();
		}
		getActor().animation(new Animation(getActor().getDefinition().getDeathAnimation(), Animation.AnimationPriority.HIGH));
	}
	
	@Override
	public void death() {
		Optional<Player> killer = getActor().getCombat().getDamageCache().getPlayerKiller();
		killer.ifPresent(k -> {
			Slayer.decrement(k, getActor());
			MinigameHandler.getMinigame(k).ifPresent(m -> m.onKill(k, getActor()));
			if(NON_DROPPABLES.stream().noneMatch(t -> t == getActor().getId())) {
				DropManager.dropItems(k, getActor());
			}
			if(k.getRights().less(Rights.ADMINISTRATOR)) {
				k.getNpcKills().incrementAndGet();
				PlayerPanel.TOTAL_NPC_KILLS.refresh(k, "@or2@ - Total Mobs killed: @yel@" + k.getNpcKills().get());
			}
			//achievement kills
			int id = getActor().getId();
			if(id > 0 && id < 2)
				KILL_A_MAN.inc(k);
			if(id == 105 || id == 1195)
				GRIZZLY_BEAR.inc(k);
			if(id == 3408)
				NO_GUARD.inc(k);
		});
		World.get().getMobs().remove(getActor());
	}
	
	@Override
	public void postDeath() {
		try {
			if(getActor().isRespawn()) {
				World.get().submit(new Task(getActor().getDefinition().getRespawnTime(), false) {
					@Override
					public void execute() {
						this.cancel();
						Mob mob = Mob.getNpc(getActor().getId(), getActor().getOriginalPosition());
						mob.setOriginalRandomWalk(getActor().isOriginalRandomWalk());
						mob.getMovementCoordinator().setCoordinate(getActor().getMovementCoordinator().isCoordinate());
						mob.getMovementCoordinator().setBoundary(getActor().getMovementCoordinator().getBoundary());
						mob.setRespawn(true);
						World.get().getMobs().add(mob);
					}
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
