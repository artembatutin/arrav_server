package com.rageps.content.poll;

/**
 * Created by Jason MacKeigan on 2017-06-05 at 3:09 PM
 *
 * A poll voter is a player that has successfully selected a option on a open
 * poll and proceeded to submit their vote. The state of a vote can never change
 * as to ensure the validity of the vote. It is with this information in mind
 * that we should keep this class stateless. Only accessors are available.
 */
public class PollVote {

	/**
	 * The unique id of the poll.
	 */
	private final int pollId;

	/**
	 * The hash of the poll id and the username, making this vote unique to the poll and player.
	 */
	private final String pollIdUsernameHash;

	/**
	 * The username of the player submitting the vote.
	 */
	private final String username;

	/**
	 * The option selected that is being voted for.
	 */
	private final PollOption option;

	public PollVote(int pollId, String pollIdUsernameHash, String username, PollOption option) {
		this.pollId = pollId;
		this.pollIdUsernameHash = pollIdUsernameHash;
		this.username = username;
		this.option = option;
	}

	public int getPollId() {
		return pollId;
	}

	public String getPollIdUsernameHash() {
		return pollIdUsernameHash;
	}

	public String getUsername() {
		return username;
	}

	public PollOption getOption() {
		return option;
	}
}
