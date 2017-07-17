package net.edge.content.achievements;

import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

/**
 * Handles the achievements.
 *
 * @author Daniel
 */
public class AchievementHandler {

    /**
     * Activates the achievement for the individual player. Increments the
     * completed amount for the player. If the player has completed the
     * achievement, they will receive their reward.
     *
     * @param player      The player activating the achievement.
     * @param achievement The achievement for activation.
     */
    public static void activate(Player player, AchievementKey achievement) {
        activate(player, achievement, 1);
    }

    /**
     * Activates the achievement for the individual player. Increments the
     * completed amount for the player. If the player has completed the
     * achievement, they will receive their reward.
     *
     * @param player      The player activating the achievement.
     * @param achievement The achievement for activation.
     * @param increase The amount to increase the achievement.
     */
    public static void activate(Player player, AchievementKey achievement, int increase) {
        final int current = player.getAchievements().computeIfAbsent(achievement, a -> 0);
        for (AchievementList list : AchievementList.values()) {
            if (list.getKey() == achievement) {
                if (current >= list.getAmount())
                    return;
                player.getAchievements().put(achievement, current + increase);
                if (player.getAchievements().get(achievement) >= list.getAmount()) {
                    player.getBank().deposit(new Item(995, list.getDifficulty().getReward()));
                    //send the cool thingy here. TODO
//                    player.send(new SendBanner("You've completed an achievement", "You've been rewarded with " + Utility.formatDigits(list.getDifficulty().getReward()) + " gp", list.getDifficulty().getColor()));
                    player.message(list.getDifficulty().getReward() + " coin have been added into your bank.");
                }
//                InterfaceWriter.write(new AchievementInterface(player));
            }
        }
    }

    /**
     * Completes all the achievements for player (used for administrative
     * purposes).
     *
     * @param player The player completing all the achievements.
     */
    public static void completeAll(Player player) {
        if (!completedAll(player)) {
            for (AchievementList achievement : AchievementList.values()) {
                if (!player.getAchievements().containsKey(achievement.getKey())) {
                    player.getAchievements().put(achievement.getKey(), achievement.getAmount());
                    continue;
                }
                player.getAchievements().replace(achievement.getKey(), achievement.getAmount());
            }
            player.message("You have successfully mastered all achievements.");
        }
    }

    /**
     * Checks if the reward is completed.
     *
     * @param player      The player checking the achievement.
     * @param achievement The achievement for checking.
     */
    public static boolean completed(Player player, AchievementList achievement) {
        if (!player.getAchievements().containsKey(achievement.getKey()))
            player.getAchievements().put(achievement.getKey(), 0);
        return player.getAchievements().get(achievement.getKey()) >= achievement.getAmount();
    }

    /**
     * Gets the total amount of achievements completed.
     *
     * @param player The player checking how many completed.
     * @return The amount of achievements completed.
     */
    public static int getTotalCompleted(Player player) {
        int count = 0;
        for (AchievementList achievement : AchievementList.values()) {
            if (player.getAchievements().containsKey(achievement.getKey()) && completed(player, achievement)) count++;
        }
        return count;
    }

    /**
     * Handles getting the amount of achivements completed based on it's
     * difficulty.
     *
     * @param player     The player checking the amount of achievements completed.
     * @param difficulty The difficulty of the achievement.
     * @return The amount of achievements completed by player based on it's
     * difficulty.
     */
    public static int getDifficultyCompletion(Player player, AchievementDifficulty difficulty) {
        int count = 0;
        for (AchievementList achievement : AchievementList.values()) {
            if (player.getAchievements().containsKey(achievement.getKey()) && achievement.getDifficulty() == difficulty && completed(player, achievement))
                count++;
        }
        return count;
    }

    /**
     * Handles getting the amount of achievements based on the difficulty.
     *
     * @param difficulty The difficulty of the achievement.
     * @return The amount of achievements based on the difficulty.
     */
    public static int getDifficultyAchievement(AchievementDifficulty difficulty) {
        int count = 0;
        for (AchievementList achievement : AchievementList.values()) {
            if (achievement.getDifficulty() == difficulty) count++;
        }
        return count;
    }

    /**
     * Checks if a player has completed all the available achievements.
     *
     * @param player The player checking if they have completed all the available
     *               achievements.
     * @return If the player has completed all the available acheievements.
     */
    public static boolean completedAll(Player player) {
        return getTotalCompleted(player) == AchievementList.getTotal();
    }
}
