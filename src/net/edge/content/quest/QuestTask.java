package net.edge.content.quest;

import net.edge.world.node.entity.player.Player;

import java.util.Optional;

/**
 * The interface which represents a contract for a quest task.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class QuestTask {

	/**
	 * The name of this task.
	 */
	private final String name;
	
	/**
	 * Determines if this quest task is completed.
	 */
	private boolean completed;
	
	/**
	 * Constructs a new {@link QuestTask}.
	 * @param name {@link #name}.
	 */
	public QuestTask(String name) {
		this.name = name;
	}
	
	/**
	 * The description that should show on the quest interface.
	 * @param player the player to display this for.
	 * @return the alphabetic value describing the description of this quest task.
	 */
	public abstract String description(Player player);
	
	/**
	 * Sets the underlying {@code completed} field to the specified {@code completed} value given.
	 * @param completed the value to set.
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	/**
	 * @return {@link #completed}.
	 */
	public boolean isCompleted() {
		return completed;
	}
	
	/**
	 * @return {@link #name}.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The optional tasks that have to be completed before this task can be started.
	 * @return the optional tasks that have to be completed.
	 */
	public Optional<QuestTask[]> required() {
		return Optional.empty();
	}

}
