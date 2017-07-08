package net.edge.content.achievements;

import net.edge.util.TextUtils;
import net.edge.world.node.entity.player.Player;

import java.util.HashMap;

/**
 * Handles the clicking on the achievement tab interface.
 * @author Daniel
 */
public class AchievementButton {

    /**
     * Holds all the achievement buttons.
     */
    private static final HashMap<Integer, AchievementList> ACHIEVEMENT_BUTTONS = new HashMap<Integer, AchievementList>();

    /**
     * Holds all the achievement titles.
     */
    private static final HashMap<Integer, AchievementDifficulty> ACHIEVEMENT_TITLES = new HashMap<Integer, AchievementDifficulty>();

    /**
     * Handles the clicking method.
     *
     * @param player The player instance.
     * @param button The button identification.
     * @return If an achievement button was clicked.
     */
    public static boolean click(Player player, int button) {
        if (ACHIEVEMENT_BUTTONS.containsKey(button)) {
            AchievementList achievement = ACHIEVEMENT_BUTTONS.get(button);
            boolean completed = AchievementHandler.completed(player, achievement);
            int completion = player.getAchievements().computeIfAbsent(achievement.getKey(), a -> 0);
            int progress = (int) (completion * 100 / (double) achievement.getAmount());
            String remaining = " (" + TextUtils.formatPrice((achievement.getAmount() - completion)) + " remaining).";
            player.message("You have completed " + progress + "% of this achievement" + (completed ? "." : remaining));
            return true;
        }

        if (ACHIEVEMENT_TITLES.containsKey(button)) {
            AchievementDifficulty difficulty = ACHIEVEMENT_TITLES.get(button);
            int completed = AchievementHandler.getDifficultyCompletion(player, difficulty);
            int total = AchievementHandler.getDifficultyAchievement(difficulty);
            player.message("You have completed a total of " + completed + "/" + total + " " + difficulty.name().toLowerCase() + " achievements.");
            return true;
        }
        return false;
    }

    public static HashMap<Integer, AchievementList> getAchievementButtons() {
        return ACHIEVEMENT_BUTTONS;
    }

    public static HashMap<Integer, AchievementDifficulty> getAchievementTitles() {
        return ACHIEVEMENT_TITLES;
    }
}
