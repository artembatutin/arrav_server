package net.edge.content.shootingstar;

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
	
	/**
	 * Attempts to interact with this star sprite.
	 * @param player the player interacting with the star sprite.
	 * @param npcId  the npc id representing the star sprite.
	 * @param option the option id interacted with.
	 * @return {@code true} if there was an interaction, {@code false} otherwise.
	 */
	public static boolean interact(Player player, int npcId, int option) {
		if(npcId != 8091) {
			return false;
		}
		
		switch(option) {
			case 1:
				DialogueAppender ap = new DialogueAppender(player);
				ap.chain(new NpcDialogue(8091, "Hello " + player.getFormatUsername() + ", I had been trapped inside this rock", "for a few decades, I can now finally return."));
				ap.chain(new PlayerDialogue(Expression.CONFUSED, "Ehh? This is where you say, \"Thank you for saving me", "oh noble warrior, do you want a reward in return?\""));
				
				boolean stardust = player.getInventory().contains(new Item(StarMining.STARDUST.getId(), 300));
				String[] message = stardust ? new String[]{"Eh, I see you have stardust on you, perhaps you", "would want to trade it for blood coins?"} : player.getInventory().contains(StarMining.STARDUST) ? new String[]{"Only if you had a minimum of 300 stardust we could", "of had talked...."} : new String[]{"Only if you had some stardust on you we could", "of had talked...."};
				
				ap.chain(new NpcDialogue(8091, message).attachAfter(() -> {
					if(!stardust) {
						player.getMessages().sendCloseWindows();
					}
				}));
				
				ap.chain(new PlayerDialogue("Mhm, sounds interesting, what's the conversion rate?"));
				ap.chain(new NpcDialogue(8091, "Ehh, I guess 300 stardust for 1 blood coin would be fair?"));
				ap.chain(new PlayerDialogue("Alright, let's do it."));
				ap.chain(new NpcDialogue(8091, "How much stardust would you like to convert?").attachAfter(() -> {
					sendEnterAmount(player);
				}));
				
				ap.start();
				break;
			case 2:
				sendInstant(player);
				break;
		}
		return true;
	}
	
	private static void sendInstant(Player player) {
		DialogueAppender ap = new DialogueAppender(player);
		
		boolean stardust = player.getInventory().contains(new Item(StarMining.STARDUST.getId(), 300));
		String[] message = stardust ? new String[]{"Howmuch stardust would you like to convert to", "blood coins?"} : player.getInventory().contains(StarMining.STARDUST) ? new String[]{"Only if you had a minimum of 300 stardust we could", "of had talked...."} : new String[]{"Only if you had some stardust on you we could", "of had talked...."};
		
		ap.chain(new NpcDialogue(8091, message).attachAfter(() -> {
			if(!stardust) {
				player.getMessages().sendCloseWindows();
				return;
			}
			sendEnterAmount(player);
		}));
		ap.start();
	}
	
	private static void sendEnterAmount(Player player) {
		player.getMessages().sendEnterAmount("How much would you like to convert?", s -> () -> {
			int selectedAmount = Integer.parseInt(s);
			
			if(selectedAmount < 300) {
				player.getDialogueBuilder().append(new NpcDialogue(8091, "I only accept a minimum of 300 stardust per blood coin."));
				return;
			}
			if(!player.getInventory().contains(new Item(StarMining.STARDUST.getId(), selectedAmount))) {
				player.getDialogueBuilder().append(new NpcDialogue(8091, "You don't have " + s + " stardust on you.").attachAfter(() -> player.getMessages().sendCloseWindows()));
				return;
			}
			
			int amountRemovable = ((selectedAmount + 50) / 100) * 300;
			
			DialogueAppender a = new DialogueAppender(player);
			
			a.chain(new NpcDialogue(8091, "Alright I can do " + amountRemovable + ", are you sure about it though?"));
			a.chain(new OptionDialogue(t -> {
				if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
					a.getBuilder().skip();
					return;
				}
				
				a.getBuilder().advance();
			}, "Yes", "No"));
			a.chain(new PlayerDialogue("No, nevermind sorry."));
			a.chain(new PlayerDialogue("Yes, let's do it.").attachAfter(() -> {
				player.getInventory().remove(new Item(StarMining.STARDUST.getId(), amountRemovable));
				player.getInventory().addOrBank(new Item(19000, amountRemovable / 10));
			}));
			a.chain(new NpcDialogue(8091, "Very well, your blood coins have been added to your", "inventory or have been banked."));
			a.start();
		});
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
