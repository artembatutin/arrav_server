package com.rageps.content.poll;


import com.rageps.world.entity.actor.player.Player;

/**
 * Created by Jason MacKeigan on 2017-06-05 at 3:15 PM
 */
public final class PollInterface {

	/**
	 * The single instance of this component or interface.
	 */
	private static final PollInterface SINGLETON = new PollInterface();

	/**
	 * An empty, private constructor, following the singleton pattern.
	 */
	private PollInterface() {

	}

	public void open(Player player) {
		/*player.getPacketSender().sendInterface(54000);
		player.getPacketSender().sendInterfaceDisplayState(54300, true);
		player.getPacketSender().sendInterfaceDisplayState(54400, true);
		player.getPacketSender().sendInterfaceDisplayState(54500, true);

		PollManager manager = PollManager.getSingleton();

		Map<PollOrder, Poll> polls = manager.getPolls();

		for (PollOrder order : PollOrder.values()) {
			Poll poll = polls.get(order);

			int childIndex = (order.getIndex() * 16);

			if (poll == null) {
				player.getPacketSender().sendString(54101 + childIndex + 1, "");
				player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 2, true);
				player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 3, true);
				player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 4, true);
				player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 5, true);
				player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 12, true);
				player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 13, true);
				player.getPacketSender().sendString(54101 + childIndex + 6, "");
				player.getPacketSender().sendString(54101 + childIndex + 7, "");
				player.getPacketSender().sendString(54101 + childIndex + 8, "");
				player.getPacketSender().sendString(54101 + childIndex + 9, "");
				player.getPacketSender().sendString(54101 + childIndex + 10, "");
				player.getPacketSender().sendString(54101 + childIndex + 11, "");
				player.getPacketSender().sendString(54101 + childIndex + 12, "");
				player.getPacketSender().sendString(54101 + childIndex + 13, "");
				player.getPacketSender().sendString(54101 + childIndex + 14, "");
			} else {
				try {
					PollVote playerVote = poll.getVote(player);
					player.getPacketSender().sendConfig(2750 + order.getIndex(), playerVote == null ? 0 : 1 + playerVote.getOption().getConfig());
					player.getPacketSender().sendString(54101 + childIndex + 1, String.format("%s%s", poll.getState() == PollState.CLOSED ? "@red@ (Closed) " : "@or2@", poll.getQuestion()));
					player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 2, false);
					player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 3, false);
					player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 4, poll.getOptionThree().equals("null"));
					player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 5, poll.getOptionFour().equals("null"));
					player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 12, poll.getOptionThree().equals("null"));
					player.getPacketSender().sendInterfaceDisplayState(54101 + childIndex + 13, poll.getOptionFour().equals("null"));
					player.getPacketSender().sendString(54101 + childIndex + 6, poll.getOptionOne());
					player.getPacketSender().sendString(54101 + childIndex + 7, poll.getOptionTwo());
					player.getPacketSender().sendString(54101 + childIndex + 8, poll.getOptionThree().equals("null") ? "" : poll.getOptionThree());
					player.getPacketSender().sendString(54101 + childIndex + 9, poll.getOptionFour().equals("null") ? "" : poll.getOptionThree());
					player.getPacketSender().sendString(54101 + childIndex + 10, String.format("%s%%", NumberFormat.getNumberInstance().format((poll.votePercentage(PollOption.FIRST)))));
					player.getPacketSender().sendString(54101 + childIndex + 11, String.format("%s%%", NumberFormat.getNumberInstance().format((poll.votePercentage(PollOption.SECOND)))));
					if (!poll.getOptionThree().equals("null")) {
						player.getPacketSender().sendString(54101 + childIndex + 12, String.format("%s%%", NumberFormat.getNumberInstance().format((poll.votePercentage(PollOption.THIRD)))));
					}
					if (!poll.getOptionFour().equals("null")) {
						player.getPacketSender().sendString(54101 + childIndex + 13, String.format("%s%%", NumberFormat.getNumberInstance().format((poll.votePercentage(PollOption.FOURTH)))));
					}
					player.getPacketSender().sendString(54101 + childIndex + 14, String.format("Closes on %s", poll.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}*/
	}

	public void drawVoteInterface(Player player) {
		/*PollOrder order = player.getAttributeMap().getObject(PollAttributes.SELECTED_POLL);

		PollOption option = player.getAttributeMap().getObject(PollAttributes.SELECTED_OPTION);

		Poll poll = PollManager.getSingleton().getPolls().get(order);

		if (poll == null) {
			return;
		}
		player.getPacketSender().sendInterfaceDisplayState(54400, false);
		player.getPacketSender().sendString(54403, poll.getQuestion());
		player.getPacketSender().sendString(54404, poll.optionFor(option));*/
	}

	public boolean click(Player player, int button) {
		/*PollManager manager = PollManager.getSingleton();

		PollOption currentOption = player.getAttributeMap().getObject(PollAttributes.SELECTED_OPTION);

		PollOrder currentOrder = player.getAttributeMap().getObject(PollAttributes.SELECTED_POLL);

		if (button == -11433 || button == -11417 || button == -11401 || button == -11385 || button == -11369) {
			PollOrder order = button == -11433 ? PollOrder.FIRST : button == -11417 ? PollOrder.SECOND : button == -11401 ? PollOrder.THIRD : button == -11385
																																			   ? PollOrder.FOURTH : PollOrder.FIFTH;
			PollOption option = PollOption.FIRST;

			openVote(player, order, option);
			return true;
		} else if (button == -11432 || button == -11416 || button == -11400 || button == -11384 || button == -11368) {
			PollOrder order = button == -11432 ? PollOrder.FIRST : button == -11416 ? PollOrder.SECOND : button == -11400 ? PollOrder.THIRD : button == -11384
																																			   ? PollOrder.FOURTH : PollOrder.FIFTH;
			PollOption option = PollOption.SECOND;

			openVote(player, order, option);
			return true;
		} else if (button == -11431 || button == -11415 || button == -11399 || button == -11383 || button == -11367) {
			PollOrder order =
			 button == -11431 ? PollOrder.FIRST : button == -11415 ? PollOrder.SECOND : button == -11399 ? PollOrder.THIRD : button == -11383 ? PollOrder.FOURTH : PollOrder.FIFTH;
			PollOption option = PollOption.THIRD;

			openVote(player, order, option);
			return true;
		} else if (button == -11430 || button == -11414 || button == -11398 || button == -11382 || button == -11366) {
			PollOrder order = button == -11430 ? PollOrder.FIRST : button == -11414 ? PollOrder.SECOND : button == -11398 ? PollOrder.THIRD : button == -11382
																																			   ? PollOrder.FOURTH : PollOrder.FIFTH;
			PollOption option = PollOption.FOURTH;

			openVote(player, order, option);
			return true;
		} else if (button == -11131) {
			player.getPacketSender().sendInterfaceDisplayState(54400, true);
			return true;
		} else if (button == -11128) {
			if (currentOption == null || currentOrder == null) {
				return true;
			}
			if (manager.isEligibleToVote(player, currentOrder)) {
				try {
					manager.vote(player, currentOrder, currentOption);
					player.getPacketSender().sendConfig(2750 + currentOrder.getIndex(), 1 + currentOrder.getIndex());
					player.message("You have voted on the poll, thank you for your input.");
				} catch (NullPointerException exception) {
					player.message("There was an issue processing your vote. Please try again soon.");
				}
				player.getPacketSender().sendInterfaceDisplayState(54400, true);
			}
			return true;
		} else if (button == -11420) {
			PollOrder order = PollOrder.FIRST;

			Poll poll = manager.getPolls().get(order);

			if (poll == null) {
				player.message("No poll exists here.");
				return true;
			}
			player.getPacketSender().sendInterfaceDisplayState(54300, false);
			player.getPacketSender().sendString(54303, poll.getQuestion());
			player.getPacketSender().sendString(54304, poll.getDescription());
			return true;
		} else if (button == -11231) {
			player.getPacketSender().sendInterfaceDisplayState(54300, true);
		}*/
		return false;
	}

	private void openVote(Player player, PollOrder order, PollOption option) {
		if (PollManager.getSingleton().isEligibleToVote(player, order)) {
			player.getAttributeMap().set(PollAttributes.SELECTED_OPTION, option);
			player.getAttributeMap().set(PollAttributes.SELECTED_POLL, order);
			drawVoteInterface(player);
		}
	}
	/**
	 * Retrieves the single instance of this class.
	 *
	 * @return the single instance.
	 */
	public static PollInterface getSingleton() {
		return SINGLETON;
	}
}
