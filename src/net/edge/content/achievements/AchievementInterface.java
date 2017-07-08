//package net.edge.content.achievements;
//
//import java.util.Arrays;
//
///**
// * Handles the achievement interface.
// *
// * @author Daniel
// */
//public class AchievementInterface extends InterfaceWriter {
//
//	private final static int BASE_BUTTON = -30485;
//	private final String[] text;
//
//	public AchievementInterface(Player player) {
//		super(player);
//		int shift = 0;
//		int total = AchievementList.values().length;
//		text = new String[total + AchievementDifficulty.values().length + 3];
//		Arrays.fill(text, "");
//
//		AchievementDifficulty last = null;
//		for (AchievementList achievement : AchievementList.values()) {
//			if (last != achievement.getDifficulty()) {
//				last = achievement.getDifficulty();
//				if (shift != 0) {
//					text[shift++] = "";
//				}
//
//				int completion = AchievementHandler.getDifficultyCompletion(player, achievement.getDifficulty());
//				int progress = (int) (completion * 100 / (double) AchievementHandler.getDifficultyAchievement(achievement.getDifficulty()));
//
//				player.send(new SendFont(startingIndex() + shift, 2));
//				player.send(new SendTooltip("", startingIndex() + shift));
//				text[shift++] = "<col=CF851B>" + Utility.formatEnum(last.name() + ": " + progress + "%");
//
//				int button = (shift - 1) + BASE_BUTTON;
//
//				if (!AchievementButton.getAchievementTitles().containsKey(button)) {
//					AchievementButton.getAchievementTitles().put(button, achievement.getDifficulty());
//				}
//			}
//
//			int completed = player.playerAchievements.computeIfAbsent(achievement.getKey(), a -> 0);
//			if (completed > achievement.getAmount()) {
//				completed = achievement.getAmount();
//			}
//
//			int color = completed == achievement.getAmount() ? 0x00FF00 : completed > 0 ? 0xFFFF00 : 0xFF0000;
//			player.send(new SendColor(startingIndex() + shift, color));
//			player.send(new SendTooltip("View achievement " + achievement.getTask() + "", startingIndex() + shift));
//			text[shift++] = "" + achievement.getTask();
//
//			int button = (shift - 1) + BASE_BUTTON;
//
//			if (!AchievementButton.getAchievementButtons().containsKey(button)) {
//				AchievementButton.getAchievementButtons().put(button, achievement);
//			}
//		}
//
//		int progress = (int) (AchievementHandler.getTotalCompleted(player) * 100 / (double) AchievementList.getTotal());
//
//		player.send(new SendScrollbar(35050, 1500));
//		player.send(new SendString("Completed: " + AchievementHandler.getTotalCompleted(player) + "/" + total + " (" + progress + "%)", 35004));
//	}
//
//	@Override
//	protected int startingIndex() {
//		return 35051;
//	}
//
//	@Override
//	protected String[] text() {
//		return text;
//	}
//
//	@Override
//	protected int[][] color() {
//		return null;
//	}
//
//	@Override
//	protected int[][] font() {
//		return null;
//	}
//
//}
