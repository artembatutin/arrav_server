package com.rageps.content.teleport.impl;

import com.rageps.content.teleport.TeleportSpell;
import com.rageps.task.Task;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.World;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.locale.Position;
import com.rageps.world.model.Animation;
import com.rageps.world.model.Graphic;

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
