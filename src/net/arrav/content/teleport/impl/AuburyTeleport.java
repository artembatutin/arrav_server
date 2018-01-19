package net.arrav.content.teleport.impl;

import net.arrav.content.teleport.TeleportSpell;
import net.arrav.task.Task;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.Animation;
import net.arrav.world.Graphic;
import net.arrav.world.World;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

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
	private final Mob aubury;
	
	/**
	 * Constructs a new {@link AuburyTeleport}.
	 * @param player {@link #player}.
	 * @param aubury {@link #aubury}.
	 */
	public AuburyTeleport(Player player, Mob aubury) {
		super(RandomUtils.random(new Position[]{new Position(2901, 4816), new Position(2888, 4845), new Position(2926, 4842), new Position(2921, 4811)}));
		this.player = player;
		this.aubury = aubury;
	}
	
	public static void move(Player player, Mob aubury) {
		player.closeWidget();
		World.get().submit(new AuburyTask(new AuburyTeleport(player, aubury)));
	}
	
	private static final class AuburyTask extends Task {
		
		private final AuburyTeleport teleport;
		
		AuburyTask(AuburyTeleport teleport) {
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
