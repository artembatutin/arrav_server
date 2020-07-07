package com.rageps.net.rest.vote;

import com.rageps.content.achievements.Achievement;
import com.rageps.content.achievements.AchievementHandler;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.test.DialogueAppender;
import com.rageps.net.rest.RestfulNexus;
import com.rageps.world.GameMode;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.rageps.world.entity.actor.player.PlayerAttributes.REDEEMING;

/**
 * Created by Ryley Kimmel on 12/1/2016.
 */
public final class Vote4Rewards {
	private static final ExecutorService service = Executors.newSingleThreadExecutor();

	public static final int VOTE_BOOK = 18768;

	public static final Item[] RARE_REWARDS = {
			new Item(1, 1),
	};

	private static final AtomicInteger voted = new AtomicInteger(0);

	public static boolean disabled = false;

	public static void open(Player player) {
		String[] options;
		if (player.getGameMode() == GameMode.PK_MODE) {
			options = new String[] {
					"100 blood money", "2,000,000 coins", "2 vote points", "Roll rare vote rewards (item not guaranteed)"
			};
		} else {
			options = new String[] {
					"2 Crystal keys", "15 minutes double experience", "2,000,000 coins", "2 vote points", "Roll rare vote rewards (item not guaranteed)"
			};
		}

		DialogueAppender ap = new DialogueAppender(player);

		ap.chain(new OptionDialogue(type ->{
			switch (type) {
				//todo handle reward logic
			}
		}, options));
		ap.start();
	}

	private static void rollRareReward(Player player, boolean secondRoll) {
		/*boolean rare = RandomUtil.random(player.hasVoteRewardIncrease() ? 350 : 500) == 1;

		if (rare) {
			Item reward = RandomUtil.randomElement(RARE_REWARDS);
			player.getInventory().addOrBankOrDrop(reward, GroundItem.getStandard(reward, player.getPosition(), player));

			String msg = "[" + ChatColor.RED + "VOTING</col>] ";
			msg += "%s: received %s from a " + ChatColor.GREEN + "Vote book</col>! Vote to get yours!";

			String prefix = player.getPrivilege().ordinal() > 0 ? "<img=" + (player.getCrownId() - 1) + "> " : "";
			String username = prefix + player.getUsername();

			String item = reward.getDefinition().getName();
			String name = item.endsWith("s") ? item : StringUtil.getIndefiniteArticle(item) + " " + item;
			World.broadcast(String.format(msg, username, name));
		} else {
			if (secondRoll) {
				DialogueManager.start(player, new NPCDialogue(Npcs.PARTY_PETE, DialogueExpression.NORMAL, "Unlucky..."));
			}
		}*/
	}

	public static void claim(Player player) {
		if (disabled) {
			player.message("Voting is currently disabled.");
			return;
		}
		if (!player.getLocation().canReceiveItemRewards()) {
			player.message("You cannot receive voting rewards in this area.");
			return;
		}
		if (player.getAttributeMap().getBoolean(REDEEMING)) {
			player.message("Please wait for your first request to finish!");
			return;
		}

		player.getAttributeMap().set(REDEEMING, true);

		CompletableFuture.runAsync(() -> {
			try {
				int sites = RestfulNexus.NEXUS.unclaimedVotes(player.credentials.username) / 10;
				if (sites > 0) {
					voted.set(voted.get() + 1);
					if (voted.get() >= 10) {
						World.get().message("<img=10><col=f9fe11><shad> Another 10 votes have been claimed, type ::vote to claim yours now!</shad></col>");
						voted.set(0);
					}

					rollRareReward(player, false);
					Achievement.VOTE.inc(player, sites);

					/*Item book = new Item(VOTE_BOOK, sites * (DailyEvents.isActive(DailyEvents.Event.DOUBLE_VOTE_BOOKS) ? 2 : 1));
					int bookCount = book.getAmount();

					String plural = bookCount == 1 ? "book" : "books";
					String singular = bookCount == 1 ? "it has" : "they have";

					if (player.getInventory().hasRoomFor(book)) {
						player.getInventory().add(book);
						player.message(ChatColor.GREEN + "You have received " + bookCount + " vote " + plural + ", " + singular + " been added to your inventory.");
					} else if (player.getBank().hasRoomFor(book)) {
						player.getBank().add(book);
						player.message(ChatColor.GREEN + "You have received " + bookCount + " vote " + plural + ", " + singular + " been added to your bank.");
					} else {
						for (int i = 0; i < bookCount; i++) {
							GroundItemManager.add(new GroundItem(new Item(VOTE_BOOK, 1), player.getPosition(), player.getUsername(), false, 200, false, 200), true);
						}
						player.message(ChatColor.GREEN + "You have received " + bookCount + " vote " + plural + ", " + singular + " dropped below you.");
					}*/
				}
			} catch (Exception cause) {
				cause.printStackTrace();
				player.message("An unexpected error occurred, please try again later.");
			}
		}, service).thenAccept(__ -> player.getAttributeMap().reset(REDEEMING));
	}

}
