package net.edge.world.node.actor.npc;

import net.edge.task.Task;
import net.edge.content.PlayerPanel;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.skill.slayer.Slayer;
import net.edge.world.World;
import net.edge.world.node.actor.ActorDeath;
import net.edge.world.Animation;
import net.edge.world.node.actor.npc.drop.NpcDropManager;
import net.edge.world.node.actor.npc.impl.gwd.GodwarsFaction;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The {@link ActorDeath} implementation that is dedicated to managing the
 * death process for all {@link Npc}s.
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDeath extends ActorDeath<Npc> {
	
	/**
	 * Creates a new {@link NpcDeath}.
	 * @param npc the NPC who has died and needs the death process.
	 */
	public NpcDeath(Npc npc) {
		super(npc);
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
				NpcDropManager.dropItems(player, getCharacter());
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
						Npc npc = Npc.getNpc(getCharacter().getId(), getCharacter().getOriginalPosition());
						npc.setOriginalRandomWalk(getCharacter().isOriginalRandomWalk());
						npc.getMovementCoordinator().setCoordinate(getCharacter().getMovementCoordinator().isCoordinate());
						npc.getMovementCoordinator().setBoundary(getCharacter().getMovementCoordinator().getBoundary());
						npc.setRespawn(true);
						World.get().getNpcs().add(npc);
					}
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
