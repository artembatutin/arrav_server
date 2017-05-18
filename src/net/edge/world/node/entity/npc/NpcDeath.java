package net.edge.world.node.entity.npc;

import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.content.PlayerPanel;
import net.edge.world.content.minigame.MinigameHandler;
import net.edge.world.content.skill.slayer.Slayer;
import net.edge.world.node.entity.EntityDeath;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.npc.drop.NpcDropManager;
import net.edge.world.node.entity.npc.impl.gwd.GodwarsFaction;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

import java.util.Optional;

/**
 * The {@link EntityDeath} implementation that is dedicated to managing the
 * death process for all {@link Npc}s.
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDeath extends EntityDeath<Npc> {
	
	/**
	 * Creates a new {@link NpcDeath}.
	 * @param npc the NPC who has died and needs the death process.
	 */
	public NpcDeath(Npc npc) {
		super(npc);
	}
	
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
			NpcDropManager.dropItems(player, getCharacter());
			
			if(player.getRights().less(Rights.ADMINISTRATOR)) {
				player.getNpcKills().incrementAndGet();
				PlayerPanel.TOTAL_NPC_KILLS.refresh(player, "@or2@ - Total Npcs killed: @yel@" + player.getNpcKills().get());
			}
		}
		World.getNpcs().remove(getCharacter());
	}
	
	@Override
	public void postDeath() {
		try {
			if(getCharacter().isRespawn()) {
				World.submit(new Task(getCharacter().getDefinition().getRespawnTime(), false) {
					@Override
					public void execute() {
						this.cancel();
						Npc npc = Npc.getNpc(getCharacter().getId(), getCharacter().getOriginalPosition());
						npc.setOriginalRandomWalk(getCharacter().isOriginalRandomWalk());
						npc.getMovementCoordinator().setCoordinate(getCharacter().getMovementCoordinator().isCoordinate());
						npc.getMovementCoordinator().setBoundary(getCharacter().getMovementCoordinator().getBoundary());
						npc.setRespawn(true);
						World.getNpcs().add(npc);
					}
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
