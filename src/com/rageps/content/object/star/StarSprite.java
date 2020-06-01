package com.rageps.content.object.star;

import com.rageps.content.dialogue.Expression;
import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.impl.PlayerDialogue;
import com.rageps.content.dialogue.test.DialogueAppender;
import com.rageps.world.World;
import com.rageps.action.impl.MobAction;
import com.rageps.content.market.MarketCounter;
import com.rageps.net.packet.out.SendEnterAmount;
import com.rageps.task.Task;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * The class which represents a single star sprite npc.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class StarSprite extends Mob {
	
	/**
	 * The shooting star this star sprite is summoned from.
	 */
	private final ShootingStar star;
	
	/**
	 * Constructs a new {@link StarSprite}.
	 * @param star {@link #star}.
	 */
	public StarSprite(ShootingStar star) {
		super(8091, star.getPosition().copy());
		this.star = star;
	}
	
	/**
	 * The exchange rate in which the star sprite will exchange a single blood coin for.
	 */
	private static final int EXCHANGE_FOR_BLOOD_COINS = 1000;
	
	/**
	 * Spawns this star sprite.
	 */
	public void spawn() {
		star.setDisabled(true);
		star.remove();
		World.get().getMobs().add(star.sprite);
	}
	
	public static void action() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				if(click == 1) {
					DialogueAppender ap = new DialogueAppender(player);
					ap.chain(new NpcDialogue(8091, "Hello " + player.getFormatUsername() + ", I had been trapped inside this rock", "for a few decades, I can now finally return."));
					ap.chain(new PlayerDialogue(Expression.CONFUSED, "Ehh? This is where you say, \"Thank you for saving me", "oh noble warrior, do you want a reward in return?\""));
					
					boolean stardust = player.getInventory().contains(new Item(StarMining.STARDUST.getId(), EXCHANGE_FOR_BLOOD_COINS));
					String[] message = stardust ? new String[]{"Eh, I see you have stardust on you, perhaps you", "would want to trade it for blood coins?"} : player.getInventory().contains(StarMining.STARDUST) ? new String[]{"Only if you had a minimum of " + EXCHANGE_FOR_BLOOD_COINS + " stardust we could", "of had talked...."} : new String[]{"Only if you had some stardust on you we could", "of had talked...."};
					
					ap.chain(new NpcDialogue(8091, message).attachAfter(() -> {
						if(!stardust) {
							player.closeWidget();
						}
					}));
					
					ap.chain(new PlayerDialogue("Mhm, sounds interesting, what's the conversion rate?"));
					ap.chain(new NpcDialogue(8091, "Ehh, I guess " + EXCHANGE_FOR_BLOOD_COINS + " stardust for 1 blood coin would be fair?"));
					ap.chain(new PlayerDialogue("Alright, let's do it."));
					ap.chain(new NpcDialogue(8091, "How much stardust would you like to convert?").attachAfter(() -> {
						sendEnterAmount(player);
					}));
					ap.start();
				} else if(click == 2) {
					sendInstant(player);
				} else if(click == 4) {
					MarketCounter.getShops().get(28).openShop(player);
				}
				return true;
			}
		};
		e.registerFirst(8091);
		e.registerSecond(8091);
		e.registerFourth(8091);
	}
	
	private static void sendInstant(Player player) {
		DialogueAppender ap = new DialogueAppender(player);
		
		boolean stardust = player.getInventory().contains(new Item(StarMining.STARDUST.getId(), EXCHANGE_FOR_BLOOD_COINS));
		String[] message = stardust ? new String[]{"Howmuch stardust would you like to convert to", "blood coins?"} : player.getInventory().contains(StarMining.STARDUST) ? new String[]{"Only if you had a minimum of " + EXCHANGE_FOR_BLOOD_COINS + " stardust we could", "of had talked...."} : new String[]{"Only if you had some stardust on you we could", "of had talked...."};
		
		ap.chain(new NpcDialogue(8091, message).attachAfter(() -> {
			if(!stardust) {
				player.closeWidget();
				return;
			}
			sendEnterAmount(player);
		}));
		ap.start();
	}
	
	private static void sendEnterAmount(Player player) {
		player.out(new SendEnterAmount("How much would you like to convert?", s -> () -> {
			int selectedAmount = Integer.parseInt(s);
			
			if(selectedAmount < EXCHANGE_FOR_BLOOD_COINS) {
				player.getDialogueBuilder().append(new NpcDialogue(8091, "I only accept a minimum of " + EXCHANGE_FOR_BLOOD_COINS + " stardust per blood coin."));
				return;
			}
			if(!player.getInventory().contains(new Item(StarMining.STARDUST.getId(), selectedAmount))) {
				player.getDialogueBuilder().append(new NpcDialogue(8091, "You don't have " + s + " stardust on you.").attachAfter(() -> player.closeWidget()));
				return;
			}
			
			int amountRemovable = ((selectedAmount + 500) / EXCHANGE_FOR_BLOOD_COINS);
			
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
				player.getInventory().remove(new Item(StarMining.STARDUST.getId(), amountRemovable * EXCHANGE_FOR_BLOOD_COINS));
				player.getInventory().addOrBank(new Item(19000, amountRemovable));
			}));
			a.chain(new NpcDialogue(8091, "Very well, your blood coins have been added to your", "inventory or have been banked."));
			a.start();
		}));
	}
	
	@Override
	public void register() {
		World.get().submit(new StarSpriteLifeTask(star.sprite));
	}
	
	@Override
	public Mob create() {
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
			World.get().getMobs().remove(sprite);
			ShootingStarManager.get().stopwatch.reset();
		}
		
	}
	
}