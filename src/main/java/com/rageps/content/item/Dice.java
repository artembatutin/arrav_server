package com.rageps.content.item;

import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.action.impl.ItemAction;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.Animation;
import com.rageps.world.Graphic;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Inventory;

import java.util.concurrent.TimeUnit;

/**
 * The class which is responsible for dicing actions.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Dice {
	
	/**
	 * Attempts to roll the dice.
	 * @param player the player rolling the dice.
	 * @param data the dice data.
	 * @param clanchat determines if this roll is in the clan chat.
	 * @return {@code true} if the dice was rolled, {@code false} otherwise.
	 */
	public static boolean roll(Player player, DiceData data, boolean clanchat) {
		if(player.getCombat().inCombat()) {
			return true;
		}
		if(player.getRights().less(Rights.DONATOR)) {
			player.message("You have to be a donator to be able to gamble");
			return true;
		}
		if(!player.getDiceTimer().elapsed(3, TimeUnit.SECONDS)) {
			return true;
		}
		player.getDiceTimer().reset();
		if(clanchat) {
			if(player.clan != null) {
				player.message("You have to be in a clan chat to do this.");
				return true;
			}

				if (player.clanChannel != null) {
					player.animation(new Animation(11900, Animation.AnimationPriority.HIGH));
					player.graphic(data.graphic);

					LinkedTaskSequence seq = new LinkedTaskSequence();
					//player.clanChannel.message(player.getName() + " has rolled <col=ff0000>" + random + "</col> on the percentile dice!");
					seq.connect(1, () -> player.clanChannel.message(data.clanChatformat(), "[Dice: " + player.getFormatUsername() + "]"));
					seq.start();
					return true;
				}
				player.message("@blu@You need to be in a Clan Chat channel in order to roll the percentile dice!");
				return true;
		}
		
		player.animation(new Animation(11900, Animation.AnimationPriority.HIGH));
		player.graphic(data.graphic);
		
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(1, () -> player.message(data.privateChatFormat(player.getFormatUsername())));
		seq.start();
		return true;
	}
	
	public static void action() {
		for(DiceData data : DiceData.values()) {
			if(data.equals(DiceData.DICE_BAG)) {
				continue;
			}
			ItemAction e = new ItemAction() {
				@Override
				public boolean click(Player player, Item item, int container, int slot, int click) {
					if(container != Inventory.INVENTORY_DISPLAY_ID)
						return true;
					return roll(player, data, false);
				}
			};
			e.register(data.item.getId());
			e = new ItemAction() {
				@Override
				public boolean click(Player player, Item item, int container, int slot, int click) {
					return roll(player, data, true);
				}
			};
			e.registerEquip(data.item.getId());
		}
		ItemAction bag = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				player.getDialogueBuilder().append(new OptionDialogue(t -> {
							if(!t.equals(OptionDialogue.OptionType.FIFTH_OPTION)) {
								player.getInventory().remove(DiceData.DICE_BAG.item);
								player.closeWidget();
							}
							switch(t) {
								case FIRST_OPTION:
									player.getInventory().add(DiceData.DIE_4_SIDED.item);
									break;
								case SECOND_OPTION:
									player.getInventory().add(DiceData.DIE_6_SIDED.item);
									break;
								case THIRD_OPTION:
									player.getInventory().add(DiceData.DIE_8_SIDED.item);
									break;
								case FOURTH_OPTION:
									player.getInventory().add(DiceData.DIE_10_SIDED.item);
									break;
								case FIFTH_OPTION:
									player.getDialogueBuilder().advance();
									break;
							}
						}, "Die (4 sides)", "Die (6 sides)", "Die (8 sides)", "Die (10 sides)", "@red@Next page"),
						
						new OptionDialogue(t -> {
							if(!t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
								player.getInventory().remove(DiceData.DICE_BAG.item);
								player.closeWidget();
							}
							switch(t) {
								case FIRST_OPTION:
									player.getDialogueBuilder().previous();
									break;
								case SECOND_OPTION:
									player.getInventory().add(DiceData.DIE_12_SIDED.item);
									break;
								case THIRD_OPTION:
									player.getInventory().add(DiceData.DIE_20_SIDED.item);
									break;
								case FOURTH_OPTION:
									player.getInventory().add(DiceData.DICE_2_6_SIDED.item);
									break;
								case FIFTH_OPTION:
									player.getInventory().add(DiceData.DICE_100.item);
									break;
							}
						}, "@red@Previous page", "Die (12 sides)", "Die (20 sides)", "Dice (2, 6)", "Dice (up to 100)")
				
				);
				return true;
			}
		};
		bag.register(DiceData.DICE_BAG.item.getId());
	}
	
	/**
	 * The enumerated type whose elements represent a set of constants used to define
	 * the data of dices with.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum DiceData {
		DICE_BAG(15084, -1, -1, "Dice bag"),
		DIE_4_SIDED(15100, 2069, 4, "4-sided die"),
		DIE_6_SIDED(15086, 2072, 6, "6-sided die"),
		DIE_8_SIDED(15090, 2071, 8, "8-sided die"),
		DIE_10_SIDED(15092, 2070, 10, "10-sided die"),
		DIE_12_SIDED(15094, 2073, 12, "12-sided die"),
		DIE_20_SIDED(15096, 2068, 20, "20-sided die"),
		DICE_2_6_SIDED(15088, 2074, 26, "2-, 6-sided dice"),
		DICE_100(15098, 2075, 100, "percentile dice");
		
		/**
		 * The item this dice represents.
		 */
		private final Item item;
		
		/**
		 * The graphic played when this dice is rolled.
		 */
		private final Graphic graphic;
		
		/**
		 * The amount to roll.
		 */
		private final int amount;
		
		/**
		 * The format name of this dice.
		 */
		private final String format;
		
		/**
		 * Constructs a new {@link DiceData}.
		 * @param itemId {@link #item}.
		 * @param graphic {@link #graphic}.
		 * @param amount {@link #amount}.
		 * @param format {@link #format}.
		 */
		DiceData(int itemId, int graphic, int amount, String format) {
			this.item = new Item(itemId);
			this.graphic = new Graphic(graphic);
			this.amount = amount;
			this.format = format;
		}
		
		public final String clanChatformat() {
			return this.amount == 26 ? "just rolled a " + RandomUtils.inclusive(1, 6) + " and " + RandomUtils.inclusive(1, 6) + " on the " + this.format + "." : "just rolled a " + RandomUtils.inclusive(1, this.amount) + " on the " + this.format + ".";
		}
		
		public final String privateChatFormat(String username) {
			return this.amount == 26 ? "You rolled a @red@" + RandomUtils.inclusive(1, 6) + "@bla@ and @red@" + RandomUtils.inclusive(1, 6) + "@bla@ on the " + this.format + "." : "You rolled a @red@" + RandomUtils.inclusive(1, this.amount) + "@bla@ on the " + this.format + ".";
		}
	}
}
