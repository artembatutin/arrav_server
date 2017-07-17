package net.edge.world.node.actor.mob;

import net.edge.task.Task;
import net.edge.content.PlayerPanel;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.skill.slayer.Slayer;
import net.edge.world.World;
import net.edge.world.node.actor.ActorDeath;
import net.edge.world.Animation;
import net.edge.world.node.actor.mob.drop.DropManager;
import net.edge.world.node.actor.mob.impl.gwd.GodwarsFaction;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
		if(getCharacter().getCombatBuilder().getVictim() != null) {
			getCharacter().getCombatBuilder().getVictim().getCombatBuilder().reset();
		}
		getCharacter().animation(new Animation(getCharacter().getDefinition().getDeathAnimation(), Animation.AnimationPriority.HIGH));
	}
	
	@Override
	public void death() {
		Optional<Player> killer = getCharacter().getCombatBuilder().getDamageCache().getPlayerKiller();
		if(killer.isPresent()) {
			Player player = killer.get();
			GodwarsFaction.increment(player, getCharacter());
			Slayer.decrement(player, getCharacter());
			MinigameHandler.getMinigame(player).ifPresent(m -> m.onKill(player, getCharacter()));
			if(NON_DROPPABLES.stream().noneMatch(t -> t == getCharacter().getId())) {
				DropManager.dropItems(player, getCharacter());
			}
			if(player.getRights().less(Rights.ADMINISTRATOR)) {
				player.getNpcKills().incrementAndGet();
				PlayerPanel.TOTAL_NPC_KILLS.refresh(player, "@or2@ - Total Npcs killed: @yel@" + player.getNpcKills().get());
			}
		}
		World.get().getNpcs().remove(getCharacter());
	}
	
	@Override
	public void postDeath() {
		try {
			if(getCharacter().isRespawn()) {
				World.get().submit(new Task(getCharacter().getDefinition().getRespawnTime(), false) {
					@Override
					public void execute() {
						this.cancel();
						Mob mob = Mob.getNpc(getCharacter().getId(), getCharacter().getOriginalPosition());
						mob.setOriginalRandomWalk(getCharacter().isOriginalRandomWalk());
						mob.getMovementCoordinator().setCoordinate(getCharacter().getMovementCoordinator().isCoordinate());
						mob.getMovementCoordinator().setBoundary(getCharacter().getMovementCoordinator().getBoundary());
						mob.setRespawn(true);
						World.get().getNpcs().add(mob);
					}
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
