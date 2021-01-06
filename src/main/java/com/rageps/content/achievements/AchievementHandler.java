package com.rageps.content.achievements;

import com.rageps.net.packet.out.SendTask;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * Handles the achievements.
 * @author Artem Batutin
 */
public class AchievementHandler {
	
	/**
	 * Activates the achievement for the individual player. Increments the
	 * completed amount for the player. If the player has completed the
	 * achievement, they will receive their reward.
	 * @param player The player activating the achievement.
	 * @param achievement The achievement for activation.
	 * @param increase The amount to increase the achievement.
	 */
	public static void activate(Player player, Achievement achievement, int increase) {
		final int prev = player.achievements.computeIfAbsent(achievement, a -> 0);
		final int curr = prev + increase;
		player.achievements.put(achievement, curr);
		boolean updated = false;
		int dif = 0;
		for(int i : achievement.getAmount()) {
			if(prev >= i) {
				dif++;
				continue;
			}
			if(curr >= i) {
				player.out(new SendTask(String.format(achievement.getTask(), achievement.getAmount()[dif])));
				player.getBank().deposit(new Item(995, AchievementDifficulty.DIF[dif].getReward()));
				player.message(AchievementDifficulty.DIF[dif].getOut() + " coins have been added into your bank.");
				update(player, achievement);
				updated = true;
			}
			dif++;
		}
		if(!updated) {
			int tier = getTier(player, achievement);
			player.interfaceText(26100 + achievement.ordinal(), String.format(achievement.getTask(), achievement.getAmount()[tier]) + " - " + prev + " / " + achievement.getAmount()[tier]);
		}
	}
	
	/**
	 * Updates the text on the panel.
	 * @param player the player updating for.
	 * @param achievement the achievement.
	 */
	public static void update(Player player, Achievement achievement) {
		int tier = getTier(player, achievement);
		final int prev = player.achievements.computeIfAbsent(achievement, a -> 0);
		player.interfaceText(26000 + achievement.ordinal(), tier + (completed(player, achievement) ? "@str@" : "") + achievement.getName());
		player.interfaceText(26100 + achievement.ordinal(), String.format(achievement.getTask(), achievement.getAmount()[tier]) + " - " + prev + " / " + achievement.getAmount()[tier]);
	}
	
	/**
	 * Gets the tier level achieved.
	 * @param player player checking for.
	 * @param achievement the achievement.
	 * @return the tier level achieved.
	 */
	public static int getTier(Player player, Achievement achievement) {
		final int prev = player.achievements.computeIfAbsent(achievement, a -> 0);
		int tier = 0;
		for(int i : achievement.getAmount()) {
			if(prev < i) {
				break;
			}
			tier++;
		}
		if(tier > achievement.getAmount().length - 1) {
			tier = achievement.getAmount().length - 1;
		}
		return tier;
	}
	
	/**
	 * Checks if the reward is completed.
	 * @param player The player checking the achievement.
	 * @param achievement The achievement for checking.
	 */
	public static boolean completed(Player player, Achievement achievement) {
		if(!player.achievements.containsKey(achievement))
			player.achievements.put(achievement, 0);
		return player.achievements.getInt(achievement) >= achievement.getAmount()[achievement.getAmount().length - 1];
	}
	
	/**
	 * Gets the total amount of achievements completed.
	 * @param player The player checking how many completed.
	 * @return The amount of achievements completed.
	 */
	public static int getTotalCompleted(Player player) {
		int count = 0;
		for(Achievement achievement : Achievement.VALUES) {
			if(completed(player, achievement))
				count++;
		}
		return count;
	}
	
	/**
	 * Checks if a player has completed all the available achievements.
	 * @param player The player checking if they have completed all the available
	 * achievements.
	 * @return If the player has completed all the available acheievements.
	 */
	public static boolean completedAll(Player player) {
		return getTotalCompleted(player) == Achievement.getTotal();
	}
}
