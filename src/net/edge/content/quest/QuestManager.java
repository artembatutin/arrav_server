package net.edge.content.quest;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.edge.content.PlayerPanel;
import net.edge.content.quest.impl.HalloweenQuest;
import net.edge.world.entity.actor.player.Player;

import java.util.Arrays;

/**
 * The manager class which is exclusive to each player.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class QuestManager {

	/**
	 * The player this quest manager is for.
	 */
	private final Player player;

	/**
	 * The list containing all the quests the player has started.
	 */
	private final Object2ObjectOpenHashMap<Quests, Quest> started_quests = new Object2ObjectOpenHashMap<>();

	/**
	 * Constructs a new {@link QuestManager}.
	 *
	 * @param player {@link #player}.
	 */
	public QuestManager(Player player) {
		this.player = player;
	}

	/**
	 * Determines if the specified {@code quests} has been started.
	 *
	 * @param quests the quests to determine this for.
	 * @return {@code true} if the quest has been started, {@code false} otherwise.
	 */
	public boolean started(Quests quests) {
		return started_quests.get(quests) != null && started_quests.get(quests).isStarted();
	}

	/**
	 * Attempts to start the specified {@code quests}.
	 *
	 * @param quests the quests to start.
	 */
	public void start(Quests quests) {
		if(started(quests)) {
			return;
		}

		if(!quests.getQuest().canStart(player)) {
			return;
		}

		quests.getQuest().setStarted(true);

		quests.getQuest().onStart(player);

		quests.getPanel().refresh(player, "@or2@ - " + "@yel@" + Quests.HALLOWEEN.getQuest().getName());

		started_quests.put(quests, quests.getQuest());
	}

	/**
	 * Determines if the player has completed the certain task.
	 *
	 * @param quests the quests to check the task from.
	 * @param name   the name which identifies the task.
	 * @return {@code true} if the player has completed the task, {@code false} otherwise.
	 */
	public boolean hasCompletedCertainTask(Quests quests, String name) {
		return started(quests) && Arrays.stream(started_quests.get(quests).getTasks()).anyMatch(t -> t.getName() == name && t.isCompleted());
	}

	/**
	 * Attempts to complete a certain task.
	 *
	 * @param quests the quests to complete a task from.
	 * @param name   the name of the task to complete.
	 */
	public void completeCertainTask(Quests quests, String name) {
		if(!started(quests)) {
			return;
		}

		for(QuestTask task : started_quests.get(quests).getTasks()) {
			if(task.getName() == name && !task.isCompleted()) {

				if(!task.required().isPresent() || (task.required().isPresent() && Arrays.stream(task.required().get()).allMatch(QuestTask::isCompleted))) {
					task.setCompleted(true);
					break;
				}
			}
		}

		if(Arrays.stream(started_quests.get(quests).getTasks()).allMatch(QuestTask::isCompleted)) {
			complete(quests);
		}
	}

	/**
	 * Attempts to complete the specified {@code quests}.
	 *
	 * @param quests the quests to complete.
	 */
	public void complete(Quests quests) {
		if(!started(quests)) {
			return;
		}

		Quest quest = started_quests.get(quests);

		if(!quest.canFinish(player)) {
			return;
		}

		quest.setCompleted(true);
	}

	/**
	 * Determines if the specified {@code quests} is completed.
	 *
	 * @param quests the quest to determine for completion.
	 * @return {@code true} if the code is completed, {@code false} otherwise.
	 */
	public boolean completed(Quests quests) {
		if(!started(quests)) {
			return false;
		}

		return quests.getQuest().isCompleted();
	}

	/**
	 * @return {@link #player}.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return {@link #started_quests}.
	 */
	public Object2ObjectOpenHashMap<Quests, Quest> getStartedQuests() {
		return started_quests;
	}

	/**
	 * The enumerated type whose elements hold instances of each quest that
	 * is available for a player.
	 *
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum Quests {
		HALLOWEEN(new HalloweenQuest(), null);//TODO

		/**
		 * The instance of the quest.
		 */
		private final Quest quest;

		/**
		 * The panel that displays this quest.
		 */
		private final PlayerPanel panel;

		/**
		 * Constructs a new {@link Quests}.
		 *
		 * @param quest {@link #quest}.
		 * @param panel {@link #panel}.
		 */
		private Quests(Quest quest, PlayerPanel panel) {
			this.quest = quest;
			this.panel = panel;
		}

		/**
		 * Attempts to open the quest interface for this specific {@link Quest}.
		 *
		 * @param player the player to open the quest interface for.
		 */
		public void open(Player player) {
			player.text(8144, quest.getName());

			if(!player.getQuestManager().started(this)) {
				player.text(8145, "@red@" + quest.getGuidance());
				player.widget(8134);
				return;
			}

			Quest quest = player.getQuestManager().getStartedQuests().get(this);

			player.text(8145, "@gre@" + quest.getGuidance());

			for(int i = 0; i < quest.getTasks().length; i++) {
				QuestTask current = quest.getTasks()[i];

				if(!current.isCompleted() && i != 0) {
					break;
				}

				String color = current.isCompleted() ? "@gre@" : "@red@";
				player.text(8146 + i, color + current.description(player));
			}

			player.widget(8134);
		}

		/**
		 * @return {@link #quest}.
		 */
		public Quest getQuest() {
			return quest;
		}

		/**
		 * @return {@link #panel}.
		 */
		public PlayerPanel getPanel() {
			return panel;
		}
	}
}
