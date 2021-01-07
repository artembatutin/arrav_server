package com.rageps.content.top_pker;


import com.rageps.net.sql.DatabaseTransactionWorker;
import com.rageps.net.sql.top_pkers.*;
import com.rageps.util.DateTimeUtil;
import com.rageps.util.Duration;
import com.rageps.util.StringUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.text.ColorConstants;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Queue;
import java.util.*;

/**
 * Created by Jason M on 2017-03-09 at 12:33 PM
 *
 * A single object to represent management for the daily top pker.
 *
 * Every twenty-four hours the daily top player killer is reset to
 */
public class TopPker {

	/**
	 * A single instance of this class, following the singleton pattern.
	 */
	private static final TopPker SINGLETON = new TopPker();

	/**
	 * The number of entry updates that can happen per sequence.
	 */
	private static final int MAXIMUM_POLLS_PER_SEQUENCE = 100;

	/**
	 * The current session, if any.
	 */
	private Optional<TopPkerSession> currentSession = Optional.empty();

	/**
	 * The last session, if any.
	 */
	private Optional<TopPkerSession> lastSession = Optional.empty();

	/**
	 * The queue of updates that should happen.
	 */
	private final Queue<Entry> entryUpdates = new ArrayDeque<>();

	/**
	 * The next announcement that is made globally.
	 */
	private ZonedDateTime nextAnnouncement = ZonedDateTime.now(DateTimeUtil.ZONE).plusMinutes(5);

	/**
	 * The time of the last entry update.
	 */
	private ZonedDateTime nextEntryUpdate = ZonedDateTime.now(DateTimeUtil.ZONE).plusMinutes(1);

	/**
	 * The state of the parent session, by default we're selecting the session.
	 */
	private State state = State.SELECTING_LAST_SESSION;

	/**
	 * The transaction for selecting a session.
	 */
	private Optional<TopPkerSelectSessionTransaction> selectSessionTransaction = Optional.empty();

	/**
	 * The transaction for updating the winner of a session.
	 */
	private Optional<TopPkerUpdateWinnerTransaction> winnerUpdateTransaction = Optional.empty();

	/**
	 * The transaction for creating a new session.
	 */
	private Optional<TopPkerCreateSessionTransaction> createSessionTransaction = Optional.empty();
	/**
	 * The transaction for updating the state of the session.
	 */
	private Optional<TopPkerUpdateStateTransaction> updatingStateTransaction = Optional.empty();

	/**
	 * The transaction for selecting the last session.
	 */
	private Optional<TopPkerSelectLastSessionTransaction> selectLastSessionTransaction = Optional.empty();

	/**
	 * The game task for appending the reward.
	 */
	private Optional<AppendRewardTask> appendRewardTask = Optional.empty();

	/**
	 * The transaction used to generate the rewards for the event.
	 */
	private Optional<TopPkerGenerateRewardsTransaction> generateRewardsTransaction = Optional.empty();

	/**
	 * The rewards for this event from the table.
	 */
	private Set<OnDemandReward> onDemandRewards = new HashSet<>();

	/**
	 * The zoned date time of the next rewards update.
	 */
	private ZonedDateTime nextRewardUpdate = ZonedDateTime.now(DateTimeUtil.ZONE);

	/**
	 * A private constructor to enforce the singleton pattern.
	 */
	private TopPker() {

	}

	/**
	 * Processes each state of the piece of content. Different operations take place during
	 * different states.
	 */
	public void sequence() {
		final ZonedDateTime time = ZonedDateTime.now(DateTimeUtil.ZONE);

		if (currentSession.isPresent()) {
			if (time.isAfter(nextEntryUpdate)) {
				nextEntryUpdate = time.plusMinutes(1);

				pollEntryUpdates();
			}
			if (time.isAfter(nextAnnouncement)) {
				nextAnnouncement = time.plusMinutes(30);

				announce();
			}
		}
		if (state == State.SELECTING_LAST_SESSION) {
			if (!selectLastSessionTransaction.isPresent()) {
				selectLastSessionTransaction = Optional.of(new TopPkerSelectLastSessionTransaction());
				selectLastSessionTransaction.ifPresent(World.get().getDatabaseWorker()::submit);
			} else {
				TopPkerSelectLastSessionTransaction transaction = selectLastSessionTransaction.get();

				if (transaction.isFinished()) {
					lastSession = Optional.ofNullable(transaction.getResult());
					state = State.SELECTING_SESSION;
				}
			}
		} else if (state == State.SELECTING_SESSION) {
			if (!selectSessionTransaction.isPresent()) {
				selectSessionTransaction = Optional.of(new TopPkerSelectSessionTransaction());
				selectSessionTransaction.ifPresent(World.get().getDatabaseWorker()::submit);
			} else {
				selectSessionTransaction.ifPresent(transaction -> {
					if (transaction.isFinished()) {
						TopPkerSession session = transaction.getResult();

						if (session != null) {
							Entry defaultEntry = TopPkerSession.DEFAULT_WINNER;

							queueEntryUpdate(defaultEntry.getUsernameAsLong(), defaultEntry.getKills(), defaultEntry.getDeaths());
							currentSession = Optional.of(new TopPkerSession(session.getId(), session.getEndDate(), session.getEntries()));
							state = State.ACTIVE;
						} else {
							state = State.CREATING_NEW_SESSION;
						}
					}
				});
			}
		} else if (state == State.ACTIVE) {
			if (currentSession.isPresent()) {
				TopPkerSession session = currentSession.get();

				if (ZonedDateTime.now(DateTimeUtil.ZONE).isAfter(session.getEndDate())) {
					pollEntryUpdates();

					Winner winner = session.determineWinner();

					try {
						session.setWinnerIfAbsent(winner);

						appendRewardTask = Optional.of(new AppendRewardTask(session.getId(), winner, session.getEndDate()));
						appendRewardTask.ifPresent(World.get().getTask()::submit);
						state = State.APPENDING_REWARDS;
					} catch (WinnerExistsException wes) {
						state = State.CREATING_NEW_SESSION;
					}
				} else {
					if (time.isAfter(nextRewardUpdate) && !generateRewardsTransaction.isPresent()) {
						generateRewardsTransaction = Optional.of(new TopPkerGenerateRewardsTransaction(session.getId()));
						generateRewardsTransaction.ifPresent(World.get().getDatabaseWorker()::submit);
					} else if (time.isAfter(nextRewardUpdate) && generateRewardsTransaction.isPresent()) {
						TopPkerGenerateRewardsTransaction transaction = generateRewardsTransaction.get();

						if (transaction.isFinished()) {
							onDemandRewards = transaction.getResult();

							nextRewardUpdate = time.plusMinutes(1);
							generateRewardsTransaction = Optional.empty();
						}
					}
				}
			} else {
				// should we do something if there is no current session active and the state is active?
			}
		} else if (state == State.APPENDING_REWARDS) {
			if (appendRewardTask.isPresent() && currentSession.isPresent()) {
				TopPkerSession session = currentSession.get();

				AppendRewardTask task = appendRewardTask.get();

				if (!task.isRunning()) {
					winnerUpdateTransaction = Optional.of(new TopPkerUpdateWinnerTransaction(session.getWinner().orElse(TopPkerSession.DEFAULT_WINNER), session.getId()));
					winnerUpdateTransaction.ifPresent(World.get().getDatabaseWorker()::submit);
					state = State.UPDATING_WINNER;
				}
			}
		} else if (state == State.UPDATING_WINNER) {
			if (winnerUpdateTransaction.isPresent()) {
				TopPkerUpdateWinnerTransaction transactionFuture = winnerUpdateTransaction.get();

				if (transactionFuture.isFinished()) {
					state = State.CREATING_NEW_SESSION;
				}
			}
		} else if (state == State.CREATING_NEW_SESSION) {
			if (createSessionTransaction.isPresent()) {
				TopPkerCreateSessionTransaction transaction = createSessionTransaction.get();

				if (transaction.isFinished()) {
					int sessionId = transaction.getResult();

					if (sessionId == -1) {
						// try for a new result
					} else {
						Entry defaultEntry = TopPkerSession.DEFAULT_WINNER;

						queueEntryUpdate(defaultEntry.getUsernameAsLong(), defaultEntry.getKills(), defaultEntry.getDeaths());
						updatingStateTransaction = Optional.of(new TopPkerUpdateStateTransaction(sessionId, currentSession.map(TopPkerSession::getId).orElse(-1)));
						updatingStateTransaction.ifPresent(World.get().getDatabaseWorker()::submit);
						currentSession.ifPresent(current -> lastSession = Optional.of(current.copy()));
						currentSession = Optional.empty();
						state = State.UPDATING_STATE;
					}
				}
			} else {
				ZonedDateTime nextTime = ZonedDateTime.now(DateTimeUtil.ZONE);

				if (time.getDayOfWeek() == DayOfWeek.THURSDAY) {
					nextTime = time.plusDays(3);
				} else if (time.getDayOfWeek() == DayOfWeek.FRIDAY) {
					nextTime = time.plusDays(2);
				} else {
					if (time.getHour() <= 23) {
						nextTime = time.plusDays(1);
					}
				}
				nextTime = nextTime.withHour(23).withMinute(59).withSecond(0);

				createSessionTransaction = Optional.of(new TopPkerCreateSessionTransaction(nextTime));
				createSessionTransaction.ifPresent(World.get().getDatabaseWorker()::submit);
			}
		} else if (state == State.UPDATING_STATE) {
			if (updatingStateTransaction.isPresent()) {
				TopPkerUpdateStateTransaction transaction = updatingStateTransaction.get();

				if (transaction.isFinished()) {
					state = State.SELECTING_SESSION;
					createSessionTransaction = Optional.empty();
					updatingStateTransaction = Optional.empty();
					winnerUpdateTransaction = Optional.empty();
					selectSessionTransaction = Optional.empty();
					selectLastSessionTransaction = Optional.empty();
					appendRewardTask = Optional.empty();
				}
			}
		} else if (state == State.DISABLED) {
			// check if we should re-enable it.
		}
	}

	/**
	 * Forces all transactions to be submitted to the database worker.
	 */
	public void forceExecuteAllTransactions() {
		DatabaseTransactionWorker worker = World.get().getDatabaseWorker();

		createSessionTransaction.ifPresent(worker::submit);
		updatingStateTransaction.ifPresent(worker::submit);
		winnerUpdateTransaction.ifPresent(worker::submit);
		selectLastSessionTransaction.ifPresent(worker::submit);
		selectSessionTransaction.ifPresent(worker::submit);
		appendRewardTask.ifPresent(World.get().getTask()::submit);
	}

	/**
	 * Queues an update to the entries of the session.
	 *
	 * @param usernameAsLong
	 * 			  the username of the player in the form of a long.
	 * @param killsOffset
	 * 			  the kills to offset by.
	 * @param deathsOffset
	 * 			  the deaths to offset by.
	 */
	public void queueEntryUpdate(long usernameAsLong, int killsOffset, int deathsOffset) {
		TopPkerSession session = currentSession.orElse(null);

		if (session == null) {
			return;
		}
		entryUpdates.add(new Entry(usernameAsLong, killsOffset, deathsOffset));
	}

	/**
	 * Attempts to poll the entry updates for the current session.
	 */
	private void pollEntryUpdates() {
		TopPkerSession session = currentSession.orElse(null);

		if (session == null) {
			return;
		}
		Entry next;

		List<Long> updated = new ArrayList<>();

		int polls = 0;

		while ((next = entryUpdates.poll()) != null && polls++ < MAXIMUM_POLLS_PER_SEQUENCE) {
			if (updated.contains(next.getUsernameAsLong())) {
				continue;
			}
			session.putOrAdd(next.getUsernameAsLong(), next.getKills(), next.getDeaths());
			updated.add(next.getUsernameAsLong());
			World.get().getDatabaseWorker().submit(new TopPkerUpdateEntryTransaction(session.getId(), session.getOrNull(next.getUsernameAsLong())));
		}
	}

	/**
	 * Announces information specific to the current session, if any, to all players over the world.
	 */
	private void announce() {
		TopPkerSession session = currentSession.orElse(null);

		if (session == null) {
			return;
		}
		List<Entry> bestEntries = session.bestEntries(3);

		Duration duration = new Duration(ZonedDateTime.now(DateTimeUtil.ZONE), session.getEndDate());
		for (Player player : World.get().getPlayers()) {
			if (player == null) {
				continue;
			}

			player.message(String.format(ColorConstants.ORANGE + "Daily Top PKer #%s Top 3 Players (Ends in %s days, %s hours, %s minutes)", session.getId(), duration.getDays(), duration.getHours(), duration.getMinutes()));
			bestEntries.forEach(entry -> player.message(String.format(Color.DARK_GRAY + "%s has %s kills and %s deaths.",
			 	StringUtil.prettifyUsername(entry.getUsernameAsLong()), entry.getKills(), entry.getDeaths())));
		}
	}

	/**
	 * Attempts to retrieve the winner of the last session, or null if not possible.
	 *
	 * @return the last session winner, or null.
	 */
	public Winner getLastWinnerOrNull() {
		TopPkerSession lastSession = this.lastSession.orElse(null);

		if (lastSession == null) {
			return null;
		}
		return lastSession.getWinner().orElse(null);
	}

	/**
	 * The set of on-demand rewards received from the event.
	 *
	 * @return the rewards received from the event.
	 */
	public Set<OnDemandReward> getOnDemandRewardsOrEmpty() {
		return onDemandRewards;
	}

	/**
	 * The current session present, or empty.
	 *
	 * @return the session.
	 */
	public final Optional<TopPkerSession> getSession() {
		return currentSession;
	}

	/**
	 * The last session, or an empty optional if none.
	 *
	 * @return the last session.
	 */
	public final Optional<TopPkerSession> getLastSession() {
		return lastSession;
	}


	/**
	 * An exception that will be thrown if we attempt to set the winner of session when one
	 * is already set.
	 */
	public static class WinnerExistsException extends RuntimeException { }


	/**
	 * The single instance of the daily pker object, following the singleton pattern.
	 *
	 * @return the single instance.
	 */
	public static final TopPker getSingleton() {
		return SINGLETON;
	}
}
