package net.edge.world.content.teleport.impl;

import net.edge.task.Task;
import net.edge.utils.rand.RandomUtils;
import net.edge.world.World;
import net.edge.world.content.teleport.TeleportSpell;
import net.edge.world.model.locale.Position;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.model.Graphic;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

import java.util.Optional;

/**
 * The class which holds support for the aubury teleport.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AuburyTeleport extends TeleportSpell {
	
	/**
	 * The player for this teleport.
	 */
	private final Player player;
	
	/**
	 * The npc teleporting the player.
	 */
	private final Npc aubury;
	
	/**
	 * Constructs a new {@link AuburyTeleport}.
	 * @param player {@link #player}.
	 * @param aubury {@link #aubury}.
	 */
	public AuburyTeleport(Player player, Npc aubury) {
		super(RandomUtils.random(new Position[]{new Position(2901, 4816), new Position(2888, 4845), new Position(2926, 4842), new Position(2921, 4811)}));
		this.player = player;
		this.aubury = aubury;
	}
	
	public static void move(Player player, Npc aubury) {
		player.getMessages().sendCloseWindows();
		World.submit(new TeleportTask(new AuburyTeleport(player, aubury)));
	}
	
	@Override
	public int levelRequired() {
		return 0;
	}
	
	@Override
	public double baseExperience() {
		return 0;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.empty();
	}
	
	@Override
	public Optional<Item[]> equipmentRequired(Player player) {
		return Optional.empty();
	}
	
	@Override
	public void startCast(EntityNode cast, EntityNode castOn) {
		
	}
	
	private static final class TeleportTask extends Task {
		
		private final AuburyTeleport teleport;
		
		public TeleportTask(AuburyTeleport teleport) {
			super(3);
			this.teleport = teleport;
		}
		
		@Override
		protected void onSubmit() {
			teleport.player.getActivityManager().disable();
			teleport.aubury.facePosition(teleport.player.getPosition());
			teleport.aubury.forceChat("Senventior disthine molenko!");
			teleport.aubury.animation(new Animation(1818));
			teleport.aubury.graphic(new Graphic(108));
			teleport.player.graphic(new Graphic(110));
		}
		
		@Override
		protected void execute() {
			this.cancel();
		}
		
		@Override
		protected void onCancel() {
			teleport.player.move(teleport.getDestination());
			teleport.player.getActivityManager().enable();
		}
	}
}
