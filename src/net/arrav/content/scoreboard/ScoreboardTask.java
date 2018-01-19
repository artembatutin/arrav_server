package net.arrav.content.scoreboard;

import net.arrav.task.Task;
import net.arrav.util.LoggerUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * Handles the timing between each monday for the scoreboard to reset.
 */
public class ScoreboardTask extends Task {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(ScoreboardTask.class);
	private static final int HOURS = 24;
	private static final int MINUTES = 60;
	private static final int SECONDS = 60;
	private static final int MILISECONDS = 1000;
	private static final double CYCLE = 600D;
	private static final int INIT_DELAY = 10;
	
	private final ScoreboardManager score = ScoreboardManager.get();
	private boolean initialized;
	
	public ScoreboardTask() {
		super(INIT_DELAY, false);
	}
	
	@Override
	protected void execute() {
		if(!initialized) {
			setDelay(calculateDelay());
			setRunning(true);
			initialized = true;
			return;
		}
		//resetting board
		score.resetPlayerScoreboard();
	}
	
	private int calculateDelay() {
		//resetting board if monday, else setting flag.
		LocalDateTime date = LocalDateTime.now();
		if(date.getDayOfWeek().equals(DayOfWeek.MONDAY) && !score.isBoardResetted()) {
			score.resetPlayerScoreboard();
		} else if(!date.getDayOfWeek().equals(DayOfWeek.MONDAY) && score.isBoardResetted()) {
			score.setBoardResetted(false);
		}
		//calculating ticks to next monday.
		int days = 7 - date.getDayOfWeek().getValue();
		if(date.getDayOfWeek().getValue() == 1) {
			days += 7;
		}
		
		int hoursToNewDay = HOURS - date.toLocalTime().getHour() - 1;
		int minutesToNewDay = MINUTES - date.toLocalTime().getMinute() - 1;
		int secondsToNewDay = SECONDS - date.toLocalTime().getSecond() - 1;
		int ticksToNewDay = (hoursToNewDay * MINUTES * SECONDS) + (minutesToNewDay * SECONDS) + secondsToNewDay;
		ticksToNewDay = (int) ((ticksToNewDay * MILISECONDS) / CYCLE);
		
		int ticks = (int) ((days * HOURS * MINUTES * SECONDS * MILISECONDS) / CYCLE) + ticksToNewDay;
		LOGGER.info("Scoreboard reset task scheduled in " + ticks + " ticks.");
		return ticks;
	}
}
