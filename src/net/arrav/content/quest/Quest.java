package net.arrav.content.quest;

import net.arrav.world.entity.actor.player.Player;

/**
 * The abstract quest class which holds basic support for quests.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class Quest {
	
	/**
	 * The name of this quest.
	 */
	private final String name;
	
	/**
	 * The guidance on how to start this quest.
	 */
	private final String guidance;
	
	/**
	 * The tasks for this quest.
	 */
	private final QuestTask[] tasks;
	
	/**
	 * Determines if the player has started the quest.
	 */
	private boolean started;
	
	/**
	 * Determines if the player has completed the quest.
	 */
	private boolean completed;
	
	/**
	 * Constructs a new {@link Quest}.
	 * @param name {@link #name}.
	 * @param guidance {@link #guidance}.
	 * @param tasks {@link #tasks}.
	 */
	public Quest(String name, String guidance, QuestTask[] tasks) {
		this.name = name;
		this.guidance = guidance;
		this.tasks = tasks;
	}
	
	/**
	 * Determines if this quest can be started.
	 * @param player the player to determine this for.
	 * @return {@code true} if the quest can be started, {@code false} otherwise.
	 */
	public abstract boolean canStart(Player player);
	
	/**
	 * Determines if this quest can be finished.
	 * @param player the player to determine this for.
	 * @return {@code true} if the quest can be finished, {@code false} otherwise.
	 */
	public abstract boolean canFinish(Player player);
	
	/**
	 * The method which can be overriden to add functionality when
	 * the quest is started.
	 * @param player the player this quest is for.
	 */
	public void onStart(Player player) {
	
	}
	
	/**
	 * @return {@link #name}.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return {@link #guidance}.
	 */
	public String getGuidance() {
		return guidance;
	}
	
	/**
	 * @return {@link #tasks}.
	 */
	public final QuestTask[] getTasks() {
		return tasks;
	}
	
	/**
	 * @param started {@link #started}.
	 */
	public final void setStarted(boolean started) {
		this.started = started;
	}
	
	/**
	 * @return {@link #started}.
	 */
	public final boolean isStarted() {
		return started;
	}
	
	/**
	 * @param completed {@link #completed}.
	 */
	public final void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	/**
	 * @return {@link #completed}.
	 */
	public final boolean isCompleted() {
		return completed;
	}
}
