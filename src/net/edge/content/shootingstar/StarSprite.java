package net.edge.content.shootingstar;

import net.edge.content.market.MarketCounter;
import net.edge.event.impl.NpcEvent;
import net.edge.task.Task;
import net.edge.content.dialogue.Expression;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.world.World;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

/**
 * The class which represents a single star sprite npc.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class StarSprite extends Npc {
	
	/**
	 * The shooting star this star sprite is summoned from.
	 */
	private final ShootingStar star;
	
	/**
	 * Constructs a new {@link StarSprite}.
	 * @param star {@link #star}.
	 */
	public StarSprite(ShootingStar star) {
		super(8091, star.getGlobalPos().copy());
		this.star = star;
	}

	/**
	 * Spawns this star sprite.
	 * @param player the last player to get stardust from the rock.
	 */
	public void spawn(Player player) {
		star.setDisabled(true);
		star.remove();
		World.get().getNpcs().add(star.sprite);
	}
	
	public static void event() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				if(click == 1) {
					DialogueAppender ap = new DialogueAppender(player);
					ap.chain(new NpcDialogue(8091, "Hello " + player.getFormatUsername() + ", I had been trapped inside this rock", "for a few decades, I can now finally return."));
					ap.chain(new PlayerDialogue(Expression.CONFUSED, "Ehh? This is where you say, \"Thank you for saving me", "oh noble warrior, do you want a reward in return?\""));
					ap.chain(new NpcDialogue(8091, "Eh, I see you have stardust on you, perhaps you", "would want to see my store?").attachAfter(() -> {
						player.getMessages().sendCloseWindows();
						MarketCounter.getShops().get(28).openShop(player);
					}));
					ap.start();
				} else if(click == 2) {
					player.getMessages().sendCloseWindows();
					MarketCounter.getShops().get(28).openShop(player);
				}
				return true;
			}
		};
		e.registerFirst(8091);
		e.registerSecond(8091);
	}
	
	@Override
	public void register() {
		World.get().submit(new StarSpriteLifeTask(star.sprite));
	}
	
	@Override
	public Npc create() {
		return new StarSprite(star);
	}
	
	/**
	 * Represents the task that is ran when the star sprite is summoned.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class StarSpriteLifeTask extends Task {
		
		/**
		 * The sprite this task is dependent of.
		 */
		private final StarSprite sprite;
		
		/**
		 * Constructs a new {@link StarSprite}.
		 * @param sprite {@link #sprite}.
		 */
		public StarSpriteLifeTask(StarSprite sprite) {
			super(1000);
			this.sprite = sprite;
		}
		
		@Override
		protected void execute() {
			this.cancel();
			World.get().getNpcs().remove(sprite);
			World.getShootingStarEvent().stopwatch.reset();
		}
		
	}
	
}
