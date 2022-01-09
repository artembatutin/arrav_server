package com.rageps.content.poll;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.rageps.net.sql.DatabaseTransactionWorker;
import com.rageps.net.sql.poll.*;
import com.rageps.task.Task;
import com.rageps.util.DateTimeUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.text.ColorConstants;
import com.rageps.world.text.MessageBuilder;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by Jason MacKeigan on 2017-06-05 at 3:53 PM
 *
 * Manages all of the polls available in the database by initially loading them
 * into memory when the game first initializes. This class also manages queuing
 * polls to be saved to the database.
 */
public class PollManager extends Task {

	/**
	 * The single instance of the manager.
	 */
	private static final PollManager SINGLETON = new PollManager();

	/**
	 * The current phase this poll manager is on.
	 */
	private Phase phase = Phase.SELECTING_POLLS;

	/**
	 * A list of all polls with no specific ordering.
	 */
	private final Map<PollOrder, Poll> polls = new HashMap<>();

	/**
	 * The transaction made to the database that selects all of the polls listed.
	 */
	private SelectPollsTransaction selectPollsTransaction;

	/**
	 * The time of the next queue update.
	 */
	private ZonedDateTime nextQueueUpdate = ZonedDateTime.now();

	/**
	 * The queue of votes to be sent to the database and added to the polls.
	 */
	private Queue<PollVote> voteQueue = new ArrayDeque<>();

	/**
	 * The poll being updated.
	 */
	private Optional<Poll> pollUpdating = Optional.empty();

	/**
	 * The transaction used to update a poll.
	 */
	private UpdatePollTransaction updatePollTransaction;

	/**
	 * The transaction used to create all of the votes.
	 */
	private CreateAllVotesTransaction createAllVotesTransaction;

	/**
	 * The transaction for creating a poll.
	 */
	private CreatePollTransaction createPollTransaction;

	/**
	 * The transaction for updating all poll information.
	 */
	private UpdatePollsTransaction updateAllPollsTransaction;

	/**
	 * Processes the current phase of the manager and does some interactions
	 * with the database depending on the phase.
	 */
	@Override
	public void execute() {
		ZonedDateTime time = ZonedDateTime.now(DateTimeUtil.ZONE);

		DatabaseTransactionWorker database = World.get().getDatabaseWorker();

		if (phase == Phase.SELECTING_POLLS) {
			if (selectPollsTransaction == null) {
				selectPollsTransaction = new SelectPollsTransaction();

				database.submit(selectPollsTransaction);
			} else {
				if (selectPollsTransaction.isFinished()) {
					List<Poll> selected = selectPollsTransaction.getResult();

					for (Poll poll : selected) {
						if (!polls.containsKey(poll.getOrder())) {
							polls.put(poll.getOrder(), poll);
						} else {
							for (PollOrder order : PollOrder.values()) {
								if (!polls.containsKey(order)) {
									poll.setOrder(order);
									polls.put(order, poll);
									break;
								}
							}
						}
					}
					selectPollsTransaction = null;
					phase = Phase.ACTIVE;
				}
			}
		} else if (phase == Phase.ACTIVE) {
			if (time.isAfter(nextQueueUpdate)) {
				nextQueueUpdate = time.plusSeconds(30);

				processQueueIfPossible();
			}
			if (createAllVotesTransaction != null && createAllVotesTransaction.isFinished()) {
				createAllVotesTransaction = null;
				voteQueue.forEach(vote -> {
					Poll poll = polls.values().stream().filter(p -> p.getId() == vote.getPollId()).findAny().orElse(null);

					if (poll != null) {
						poll.addVote(vote);
					}
				});
				voteQueue.clear();
			}
			if (createAllVotesTransaction == null && voteQueue.isEmpty()) {
				for (Poll poll : polls.values()) {
					if (time.isAfter(poll.getEndDate()) && poll.getResult() == PollResult.TO_BE_DETERMINED && poll.getState() == PollState.OPEN) {
						phase = Phase.UPDATING_POLL;
						pollUpdating = Optional.of(poll);
						closeAndAnnounce(poll);
						break;
					}
				}
			}
		} else if (phase == Phase.UPDATING_POLL) {
			if (updatePollTransaction == null && pollUpdating.isPresent()) {
				updatePollTransaction = new UpdatePollTransaction(pollUpdating.get());

				database.submit(updatePollTransaction);
			} else if (updatePollTransaction == null && !pollUpdating.isPresent() || updatePollTransaction != null && updatePollTransaction.isFinished()) {
				phase = Phase.UPDATING_ALL_POLLS;
				updatePollTransaction = null;
				updateAllPollsTransaction = new UpdatePollsTransaction(polls);
				database.submit(updateAllPollsTransaction);
			}
		} else if (phase == Phase.CREATING_POLL) {
			if (updatePollTransaction != null && updatePollTransaction.isFinished() &&
				 	createPollTransaction != null && createPollTransaction.isFinished()) {
				Poll newPoll = createPollTransaction.getResult();

				polls.put(newPoll.getOrder(), newPoll);
				phase = Phase.UPDATING_POLL;
				pollUpdating = Optional.of(newPoll);
				createPollTransaction = null;
				MessageBuilder mb = new MessageBuilder();
				mb.appendPrefix("Poll", ColorConstants.BLUE, ColorConstants.CYAN).append("New poll available; "+newPoll.getQuestion());
				World.get().getWorldUtil().message(mb.toString());
			} else if (createPollTransaction != null) {
				if (createPollTransaction.isFinished()) {
					Poll poll = createPollTransaction.getResult();

					if (poll != null) {
						Poll existing = polls.get(poll.getOrder());

						if (existing != null && existing.getState() == PollState.OPEN) {
							closeAndAnnounce(existing);
							pollUpdating = Optional.of(existing);
							updatePollTransaction = new UpdatePollTransaction(existing);
							database.submit(updatePollTransaction);
						} else {
							polls.put(poll.getOrder(), poll);
							phase = Phase.UPDATING_POLL;
							pollUpdating = Optional.of(poll);
							createPollTransaction = null;
							MessageBuilder mb = new MessageBuilder();
							mb.appendPrefix("Poll", ColorConstants.BLUE, ColorConstants.CYAN).append("New poll available; "+poll.getQuestion());
							World.get().getWorldUtil().message(mb.toString());
						}
					} else {
						phase = Phase.ACTIVE;
					}
				}
			} else {
				phase = Phase.ACTIVE;
			}
		} else if (phase == Phase.UPDATING_ALL_POLLS) {
			if (updateAllPollsTransaction != null && updateAllPollsTransaction.isFinished()) {
				updateAllPollsTransaction = null;
				phase = Phase.ACTIVE;
			} else if (updateAllPollsTransaction == null) {
				phase = Phase.ACTIVE;
			}
		}
	}

	public boolean pollClosingPossible(Player player, PollOrder order) {
		if (phase != Phase.ACTIVE) {
			player.message("The poll manager is not available right now, please try again later.");
			return false;
		}
		Poll poll = polls.get(order);

		if (poll == null) {
			player.message("No poll exists at this order.");
			return false;
		}
		if (poll.getState() == PollState.CLOSED) {
			player.message("The poll is already closed.");
			return false;
		}
		return true;
	}

	public void updateClosedPoll(Poll poll) {
		if (phase != Phase.ACTIVE) {
			throw new IllegalStateException("Phase must be active before we can change to updating.");
		}
		pollUpdating = Optional.of(poll);
		phase = Phase.UPDATING_POLL;
	}


	public void closeAndAnnounce(Poll poll) {
		poll.close();
		MessageBuilder mb = new MessageBuilder();
		mb.appendPrefix("Poll", ColorConstants.BLUE, ColorConstants.CYAN).append("A poll has completed. Please review below for more information.");
		World.get().getWorldUtil().message(mb.toString());
		mb = new MessageBuilder();
		mb.appendPrefix("Poll", ColorConstants.BLUE, ColorConstants.CYAN).append("Question: " + poll.getQuestion());
		mb = new MessageBuilder();
		mb.appendPrefix("Poll", ColorConstants.BLUE, ColorConstants.CYAN);
		if (poll.getResult() == PollResult.SUCCESSFUL && poll.getOptionIfSuccessful().isPresent()) {
			mb.append(poll.votePercentage(poll.getOptionIfSuccessful().get()) + "% of players voted: " + poll.optionFor(poll.getOptionIfSuccessful().get()) + ".");
		} else {
			mb.append("A majority vote could not be made for this poll unfortunately.");
		}
		World.get().getWorldUtil().message(mb.toString());
	}

	public boolean isEligibleToVote(Player player, PollOrder order) {
		Poll poll = polls.get(order);

		if (poll == null) {
			return false;
		}
		if (phase == Phase.CREATING_POLL) {
			player.message("A poll is being created, please wait a few moments.");
			return false;
		}
		if (phase != Phase.ACTIVE) {
			player.message("The voting polls are currently not open!");
			return false;
		}
		if (poll.getState() == PollState.CLOSED) {
			player.message("The poll is closed, you can no longer vote.");
			return false;
		}
		if (createAllVotesTransaction != null) {
			player.message("The voting polls are currently updating, please wait a moment!");
			return false;
		}
		/*if (TimeUnit.MILLISECONDS.toHours(player.getTotalPlayTime()) < 24) {//todo add this
			player.message("You cannot vote unless you have played the game for a total of at least 24 hours.");
			return false;
		}*/
		if (poll.hasVoted(player.credentials.username) || voteQueue.stream().anyMatch(vote -> vote.getPollId() == poll.getId() && vote.getUsername().equals(player.credentials.username))) {
			player.message("You have already voted on this poll.");
			return false;
		}
		return true;
	}

	public boolean pollCreationAvailable(Player player) {
		if (phase == Phase.CREATING_POLL || createPollTransaction != null) {
			player.message("A poll is currently being created, please wait a few moments.");
			return false;
		}
		if (phase != Phase.ACTIVE) {
			player.message("The poll is not active, wait a few moments and try again.");
			return false;
		}
		return true;
	}

	public void create(PollOrder order, String question, String description, String optionOne, String optionTwo, String optionThree, String optionFour, int daysUntilEnd) {
		phase = Phase.CREATING_POLL;
		createPollTransaction = new CreatePollTransaction(order, question, description, optionOne, optionTwo, optionThree, optionFour, daysUntilEnd);
		World.get().getDatabaseWorker().submit(createPollTransaction);
	}

	public void vote(Player player, PollOrder order, PollOption option) {
		Poll poll = polls.get(order);

		if (poll == null) {
			throw new NullPointerException("The poll for the given order is null.");
		}
		voteQueue.add(new PollVote(poll.getId(), Hashing.md5().newHasher().putInt(poll.getId())
												  .putString(player.credentials.username, Charsets.UTF_8).hash().toString(), player.credentials.username, option));
	}

	private void processQueueIfPossible() {
		if (createAllVotesTransaction != null) {
			return;
		}
		if (voteQueue.isEmpty()) {
			return;
		}
		createAllVotesTransaction = new CreateAllVotesTransaction(voteQueue);

		World.get().getDatabaseWorker().submit(createAllVotesTransaction);
	}

	public Map<PollOrder, Poll> getPolls() {
		return polls;
	}

	public static PollManager getSingleton() {
		return SINGLETON;
	}

	public enum Phase {
		SELECTING_POLLS,

		UPDATING_POLL,

		ACTIVE,

		CREATING_POLL,

		UPDATING_ALL_POLLS,
	}
}
