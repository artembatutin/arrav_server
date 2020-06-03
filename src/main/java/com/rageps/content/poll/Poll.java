package com.rageps.content.poll;


import com.rageps.world.entity.actor.player.Player;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Jason MacKeigan on 2017-06-05 at 2:58 PM
 *
 * <p>A poll contains a specific question that requires feedback from players to be solved.
 * Players give this feedback by selecting a {@link PollOption}. After the option has been
 * selected, an interface is drawn which will give the player the option to vote.</p>
 *
 * <p>A poll will end once the end date has been reached. Upon doing so, we will determine
 * if there is a majority vote for one of the options. If that requirement is reached,
 * the poll is deemed {@link PollResult#SUCCESSFUL}, otherwise {@link PollResult#UNSUCCESSFUL}.
 * </p>
 */
public class Poll {

	/**
	 * The percentage required for a certain poll option to be successful.
	 */
	public static final double MAJORITY_PERCENTAGE_REQUIRED = 70.0;

	/**
	 * The numerical unique identification value of the poll.
	 */
	private final int id;

	/**
	 * The date and time that the poll ends.
	 */
	private final ZonedDateTime endDate;

	/**
	 * The question that is being asked in this poll.
	 */
	private final String question;

	/**
	 * The description of the poll.
	 */
	private final String description;

	/**
	 * The first option, or option one.
	 */
	private final String optionOne;

	/**
	 * The second option, or option two.
	 */
	private final String optionTwo;

	/**
	 * The third option, or option three.
	 */
	private final String optionThree;

	/**
	 * The fourth option, or option four.
	 */
	private final String optionFour;

	/**
	 * A list of voters in this poll.
	 */
	private final List<PollVote> votes = new ArrayList<>();

	/**
	 * The state of the poll, which by default is open.
	 */
	private PollState state = PollState.OPEN;

	/**
	 * The result of the poll after all options have been considered, this by default is
	 * {@link PollResult#TO_BE_DETERMINED}.
	 */
	private PollResult result = PollResult.TO_BE_DETERMINED;

	/**
	 * The order of the poll, or first by default.
	 */
	private PollOrder order = PollOrder.FIFTH;

	/**
	 * The option, if any, that was successful in the poll.
	 */
	private Optional<PollOption> optionIfSuccessful = Optional.empty();

	public Poll(int id, ZonedDateTime endDate, String question, String description, String optionOne, String optionTwo, String optionThree, String optionFour, PollState state,
                PollResult result, PollOption optionIfSuccessful, PollOrder order) {
		this.id = id;
		this.endDate = endDate;
		this.question = question;
		this.description = description;
		this.optionOne = optionOne;
		this.optionTwo = optionTwo;
		this.optionThree = optionThree;
		this.optionFour = optionFour;
		this.state = state;
		this.result = result;
		this.optionIfSuccessful = Optional.ofNullable(optionIfSuccessful);
		this.order = order;
	}

	public void close() {
		if (result != PollResult.TO_BE_DETERMINED) {
			throw new IllegalStateException("Poll result has already been determined.");
		}
		if (state != PollState.OPEN) {
			throw new IllegalStateException("Poll must be open to be closed.");
		}
		PollOption successfulOption = Stream.of(PollOption.values()).filter(option -> votePercentage(option) >= MAJORITY_PERCENTAGE_REQUIRED).findAny().orElse(null);

		optionIfSuccessful = Optional.ofNullable(successfulOption);
		state = PollState.CLOSED;
		result = optionIfSuccessful.isPresent() ? PollResult.SUCCESSFUL : PollResult.UNSUCCESSFUL;
	}

	public double votePercentage(PollOption option) {
		int totalVotes = votes.size();

		if (totalVotes == 0) {
			return 0;
		}
		int optionVotes = (int) votes.stream().filter(vote -> vote.getOption() == option).count();

		return ((double) optionVotes / (double) totalVotes) * 100D;
	}

	public String optionFor(PollOption option) {
		return option == PollOption.FIRST ? optionOne : option == PollOption.SECOND ? optionTwo : option == PollOption.THIRD ? optionThree : optionFour;
	}

	public void addVote(PollVote vote) {
		votes.add(vote);
	}

	public boolean hasVoted(String username) {
		return votes.stream().anyMatch(vote -> vote.getUsername().equals(username));
	}

	public PollVote getVote(Player player) {
		return votes.stream().filter(vote -> vote.getUsername().equals(player.credentials.username)).findAny().orElse(null);
	}
	public int getId() {
		return id;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public String getQuestion() {
		return question;
	}

	public String getDescription() {
		return description;
	}

	public String getOptionOne() {
		return optionOne;
	}

	public String getOptionTwo() {
		return optionTwo;
	}

	public String getOptionThree() {
		return optionThree;
	}

	public String getOptionFour() {
		return optionFour;
	}

	public PollState getState() {
		return state;
	}

	public PollResult getResult() {
		return result;
	}

	public Optional<PollOption> getOptionIfSuccessful() {
		return optionIfSuccessful;
	}

	public void setState(PollState state) {
		this.state = state;
	}

	public void setResult(PollResult result) {
		this.result = result;
	}

	public void setOptionIfSuccessful(Optional<PollOption> optionIfSuccessful) {
		this.optionIfSuccessful = optionIfSuccessful;
	}

	public PollOrder getOrder() {
		return order;
	}

	public void setOrder(PollOrder order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "Poll{" + "id=" + id + ", endDate=" + endDate + ", question='" + question + '\'' + ", description='" + description + '\'' + ", optionOne='" + optionOne + '\''
				+ ", optionTwo='" + optionTwo + '\'' + ", optionThree='" + optionThree + '\'' + ", optionFour='" + optionFour + '\'' + ", votes=" + votes + ", state=" + state
				+ ", result=" + result + ", order=" + order + ", optionIfSuccessful=" + optionIfSuccessful + '}';
	}
}
