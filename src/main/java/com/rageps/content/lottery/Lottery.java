package com.rageps.content.lottery;

import com.rageps.content.top_pker.TopPker;
import com.rageps.net.sql.DatabaseTransactionWorker;
import com.rageps.net.sql.lottery.*;
import com.rageps.task.Task;
import com.rageps.util.DateTimeUtil;
import com.rageps.util.NumberUtil;
import com.rageps.util.StringUtil;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.World;
import com.rageps.world.text.ColorConstants;
import com.rageps.world.text.MessageBuilder;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Jason M on 4/27/2017
 *
 * This lottery class is an adaptation of the {@link TopPker} class. Although similar, some functionality has changed
 * while utilizing a lot of already existing code and contracts.
 *
 * The portion that is most similar is the SQL table layout. The tables are very similar with respect to a few
 * circumstantial changes.
 */
public final class Lottery extends Task {

	/**
	 * The single instance of this class.
	 */
	private static final Lottery SINGLETON = new Lottery();

	/**
	 * The minimum amount of days that can pass before the lottery is drawn again.
	 */
	private static final int MINIMUM_PERIOD_INTERVAL = 3;

	/**
	 * The cost of an individual ticket.
	 */
	static final int TICKET_COST = 5_000_000;

	/**
	 * The rate at which the task's execute function will be called.
	 */
	private static final int SEQUENCE_RATE = 5;

	/**
	 * The amount of tickets an individual player can purchase.
	 */
	static final int MAXIMUM_ENTRIES_PER_PLAYER = 20;

	/**
	 * The maximum amount of changes allowed per sequence update.
	 */
	private static final int MAXIMUM_POLLS_PER_SEQUENCE = 10;

	/**
	 * The tax applied to the pot.
	 */
	static final float TAX = .4f;

	/**
	 * The current lottery session, or null if one doesnt exist at the moment.
	 */
	private LotterySession session;

	/**
	 * The last session, or null if one does not exist.
	 */
	private LotterySession lastSession;

	/**
	 * What phase of the lottery we are currently on.
	 */
	private LotteryPhase phase = LotteryPhase.SELECTING_LAST_LOTTERY;

	/**
	 * A queue containing entries that should be updated periodically.
	 */
	private final Queue<LotterySessionEntry> entryUpdateQueue = new ArrayDeque<>();

	/**
	 * The date and time of the next queue poll.
	 */
	private ZonedDateTime nextQueuePollTime = ZonedDateTime.now(DateTimeUtil.ZONE).plusMinutes(1);

	/**
	 * The date and time of the next chat announcement.
	 */
	private ZonedDateTime nextChatAnnouncement = ZonedDateTime.now(DateTimeUtil.ZONE).plusMinutes(1);

	/**
	 * The date and time of the next announcement made by lottie.
	 */
	private ZonedDateTime nextLottieAnnouncement = ZonedDateTime.now(DateTimeUtil.ZONE).plusMinutes(1);

	/**
	 * A transaction used to select the last session.
	 */
	private LotterySelectLastSessionTransaction selectLastSessionTransaction;

	/**
	 * A transaction used to select the current session.
	 */
	private LotterySelectSessionTransaction selectSessionTransaction;

	/**
	 * A transaction used to update the winner.
	 */
	private LotteryUpdateWinnerTransaction updateWinnerTransaction;

	/**
	 * A transaction used to update the state of the lottery.
	 */
	private LotteryUpdateStateTransaction updateStateTransaction;

	/**
	 * A transaction used to create the session.
	 */
	private LotteryCreateSessionTransaction createSessionTransaction;

	/**
	 * The task used to reward the winners of the session.
	 */
	private LotteryRewardTask rewardTask;

	/**
	 * A private constructor which ensures the task sequences at a specific rate and can
	 * only be constructed from within this class.
	 */
	private Lottery() {
		super(SEQUENCE_RATE);
	}

	/**
	 * The single instance of this lottery class.
	 *
	 * @return the single instance.
	 */
	static Lottery getSingleton() {
		return SINGLETON;
	}

	/**
	 * Sequences through each of the phases and performs actions for each of them.
	 */
	@Override
	public void execute() {
		final ZonedDateTime time = ZonedDateTime.now(DateTimeUtil.ZONE);

		final DatabaseTransactionWorker transactionWorker = World.get().getDatabaseWorker();

		if (session != null) {
			if (time.isAfter(nextQueuePollTime)) {
				nextQueuePollTime = time.plusMinutes(1);
				pollEntryUpdates();
			}
			if (time.isAfter(nextChatAnnouncement)) {
				nextChatAnnouncement = time.plusMinutes(10);
				MessageBuilder mb = new MessageBuilder();
				mb.appendPrefix("Lottery", ColorConstants.PALE_ORANGE, ColorConstants.MAGENTA);
				mb.append("The current lottery pot size is: " + NumberUtil.format(session.getPotAfterTax()) + ".", ColorConstants.BURGUNDY);
				World.get().getWorldUtil().message(mb.toString());
			}
			if (time.isAfter(nextLottieAnnouncement)) {
				nextLottieAnnouncement = time.plusMinutes(5);
				World.get().getMobRepository().stream().filter(Objects::nonNull).forEach(mob ->
						mob.forceChat("Talk to me to win " + NumberUtil.format(session.getPotAfterTax()) + "gp."));
			}
		}
		if (phase == LotteryPhase.SELECTING_LAST_LOTTERY) {
			if (selectLastSessionTransaction == null) {
				transactionWorker.submit(selectLastSessionTransaction = new LotterySelectLastSessionTransaction());
			} else {
				if (selectLastSessionTransaction.isFinished()) {
					lastSession = selectLastSessionTransaction.getResult();
					phase = LotteryPhase.SELECTING_LOTTERY;
				}
			}
		} else if (phase == LotteryPhase.SELECTING_LOTTERY) {
			if (selectSessionTransaction == null) {
				transactionWorker.submit(selectSessionTransaction = new LotterySelectSessionTransaction());
			} else {
				if (selectSessionTransaction.isFinished()) {
					LotterySession result = selectSessionTransaction.getResult();

					if (result == null) {
						phase = LotteryPhase.CREATE_NEW_SESSION;
					} else {
						session = result;
						phase = LotteryPhase.ACTIVE;

						if (session.isEntriesEmpty()) {
							LotterySession.DEFAULT_WINNERS.values().forEach(winner ->  queueEntryUpdate(winner.getName(), winner.getTickets()));
						}
					}
				}
			}
		} else if (phase == LotteryPhase.ACTIVE) {
			if (session != null) {
				if (time.isAfter(session.getEndDate())) {
					pollEntryUpdates();

					session.setWinnersIfAbsent(session.determineWinners());

					World.get().getTask().submit(rewardTask = new LotteryRewardTask(session));

					phase = LotteryPhase.APPENDING_POT;
				}
			}
		} else if (phase == LotteryPhase.APPENDING_POT) {
			if (!rewardTask.isRunning()) {
				transactionWorker.submit(updateWinnerTransaction = new LotteryUpdateWinnerTransaction(session.getWinners(), session.getId()));
				phase = LotteryPhase.UPDATE_WINNER;
			}
		} else if (phase == LotteryPhase.UPDATE_WINNER) {
			if (updateWinnerTransaction.isFinished()) {
				MessageBuilder mb = new MessageBuilder();
				mb.appendPrefix("Lottery", ColorConstants.PALE_ORANGE, ColorConstants.MAGENTA);
				mb.append("The lottery is over, total pot was " + NumberUtil.format(session.getPotAfterTax()) + ".", ColorConstants.BURGUNDY);
				World.get().getWorldUtil().message(mb.toString());
				for (LotterySessionWinner winner : session.getWinners().values().stream().sorted(Comparator.comparing(LotterySessionWinner::getPlacement)).collect(Collectors.toList())) {
					mb = new MessageBuilder();
					mb.appendPrefix("Lottery", ColorConstants.PALE_ORANGE, ColorConstants.MAGENTA);
					mb.append(StringUtil.capitalizeFirst(winner.getPlacement().name().toLowerCase()) + " place: " + winner.getName() + " won "
							+ NumberUtil.format((long) (session.getPotAfterTax() * winner.getPlacement().getPercentageOfPot())) + ".", ColorConstants.BURGUNDY);
				}
				phase = LotteryPhase.CREATE_NEW_SESSION;
			}
		} else if (phase == LotteryPhase.CREATE_NEW_SESSION) {
			if (createSessionTransaction != null) {
				if (createSessionTransaction.isFinished()) {
					int sessionId = createSessionTransaction.getResult();

					if (sessionId == -1) {
						// should we try to create a new session again? Or should we give the fuck up and dig a hole?
					} else {
						transactionWorker.submit(updateStateTransaction = new LotteryUpdateStateTransaction(sessionId, session == null ? -1 : session.getId()));

						if (session != null) {
							lastSession = session.copy();
						}
						session = null;
						phase = LotteryPhase.UPDATE_SAVED_STATE;
					}
				}
			} else {
				transactionWorker.submit(createSessionTransaction = new LotteryCreateSessionTransaction(
				 	TICKET_COST, time.plusDays(MINIMUM_PERIOD_INTERVAL).withHour(RandomUtils.inclusive(0, 23)).withMinute(0).withSecond(0)));
			}
		} else if (phase == LotteryPhase.UPDATE_SAVED_STATE) {
			if (updateStateTransaction != null && updateStateTransaction.isFinished()) {
				phase = LotteryPhase.SELECTING_LOTTERY;
				createSessionTransaction = null;
				selectLastSessionTransaction = null;
				selectSessionTransaction = null;
				updateStateTransaction = null;
				updateWinnerTransaction = null;
				if (rewardTask != null && rewardTask.isRunning()) {
					rewardTask.cancel();
				}
				rewardTask = null;
			}
		}
	}

	boolean queueContains(String username) {
		return entryUpdateQueue.stream().anyMatch(entry -> entry.getName().equals(username));
	}

	void queueEntryUpdate(String username, int offsetTickets) {
		entryUpdateQueue.add(new LotterySessionEntry(username, offsetTickets));
	}

	private void pollEntryUpdates() {
		LotterySessionEntry next;

		List<String> updated = new ArrayList<>();

		int polls = 0;

		while ((next = entryUpdateQueue.poll()) != null && polls++ < MAXIMUM_POLLS_PER_SEQUENCE) {
			if (updated.contains(next.getName())) {
				continue;
			}
			session.putOrAdd(next.getName(), next.getTickets());
			updated.add(next.getName());
			World.get().getDatabaseWorker().submit(new LotteryUpdateEntryTransaction(session.getId(), session.getOrNull(next.getName())));
		}
	}

	public LotterySession getSession() {
		return session;
	}

	LotterySession getLastSession() {
		return lastSession;
	}

	LotteryPhase getPhase() {
		return phase;
	}
}
