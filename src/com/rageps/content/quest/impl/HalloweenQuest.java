package com.rageps.content.quest.impl;

import com.rageps.content.quest.QuestTask;
import com.rageps.content.quest.Quest;
import com.rageps.world.entity.actor.player.Player;

import java.time.LocalDate;

/**
 * The class which is responsible for the tasks to complete for the halloween
 * event.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class HalloweenQuest extends Quest {
	
	/**
	 * Constructs a new {@link HalloweenQuest}.
	 */
	public HalloweenQuest() {
		super("Halloween Fright Nights.", "When black cats prowl and pumpkins gleam, ....", new QuestTask[]{new QuestTask("speak_to_the_reaper") {
			
			@Override
			public String description(Player player) {
				return "Speak to the reaper.";
			}
		}});
	}
	
	/**
	 * Represents the date that halloween takes place.
	 */
	private static final LocalDate HALLOWEEN_DATE = LocalDate.of(LocalDate.now().getYear(), 10, 31);
	
	@Override
	public void onStart(Player player) {
		//player.teleport(new Position());
	}
	
	@Override
	public boolean canStart(Player player) {
		if(this.isCompleted()) {
			player.message("You have already completed this quest.");
			return false;
		}
		LocalDate now = LocalDate.now();
		if(!(now.isAfter(HALLOWEEN_DATE.minusDays(5)) && now.isBefore(HALLOWEEN_DATE.plusDays(5)))) {
			player.message("@red@You can only start this quest 5 days prior and after halloween.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canFinish(Player player) {
		return true;
	}
}
