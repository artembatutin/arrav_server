package com.rageps.content.lottery;


/**
 * Created by Jason M on 2017-04-27 at 6:53 PM todo - replace with interface
 */
public enum LotteryDialogue {
	/*FIRST(new NpcDialogue(8206, Expression.CALM, "Hello adventurer. How can I help you?") {
		@Override
		public Dialogue nextDialogue(Player p) {
			return FIRST_OPTION.getDialogue();
		}
	}),
	FIRST_OPTION(new OptionDialogue("How does the lottery work?", "I'd like to buy tickets for the next draw.",
			"When does the lottery end?", "Who won the last lottery?", "Nevermind") {
		@Override
		public void option(Player player, int option) {
			Lottery lottery = Lottery.getSingleton();

			LotterySession session = lottery.getSession();

			LotterySession lastSession = lottery.getLastSession();

			switch (option) {
				case 1:
					DialogueManager.start(player, HOW_FIRST.getDialogue());
					return;
				case 2:
					DialogueManager.start(player, BUY_FIRST.getDialogue());
					return;
				case 3:
					DialogueManager.start(player, session == null ?
							new NpcDialogue(8206, Expression.CALM, "There is currently no lottery available.", "Come back at a later date.") :
							new NpcDialogue(8206, Expression.CALM, "The lottery ends on "
									+ session.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + "."));
					return;
				case 4:
					DialogueManager.start(player, lastSession == null ?
							new NpcDialogue(8206, Expression.CALM, "There was no lottery winners for the last lottery.") :
							new NpcDialogue(8206, Expression.CALM, "These are the winners from the last lottery:",
									"1st: " + StringUtil.capitalizeFirst(
											lastSession.getWinners().get(LotteryPlacement.FIRST).getName()) + " won "
											+ NumberUtil.format((long) (lastSession.getPotAfterTax() * lastSession.getWinners().get(LotteryPlacement.FIRST).getPlacement().getPercentageOfPot())),
									"2nd: " + StringUtil.capitalizeFirst(
											lastSession.getWinners().get(LotteryPlacement.SECOND).getName()) + " won "
											+ NumberUtil.format((long) (lastSession.getPotAfterTax() * lastSession.getWinners().get(LotteryPlacement.SECOND).getPlacement().getPercentageOfPot())),
									"3rd: " + StringUtil.capitalizeFirst(
											lastSession.getWinners().get(LotteryPlacement.THIRD).getName()) + " won "
											+ NumberUtil.format((long) (lastSession.getPotAfterTax() * lastSession.getWinners().get(LotteryPlacement.THIRD).getPlacement().getPercentageOfPot()))));
					return;
				case 5:
					player.getPacketSender().sendInterfaceRemoval();
					return;
			}
		}
	}),
	HOW_FIRST(new NpcDialogue(8206, Expression.CALM,
			"Great question, i'll explain.",
			"Players such as yourself are able to purchase tickets",
			"in-exchange for a chance at winning the lottery.",
			"The lottery is 60% of all ticket purchases.") {

		@Override
		public Dialogue nextDialogue(Player p) {
			return HOW_SECOND.getDialogue();
		}
	}),
	HOW_SECOND(new PlayerDialogue(Expression.ANGRY, "What about the other 40% of ticket purchases?") {

		@Override
		public Dialogue nextDialogue(Player p) {
			return HOW_THIRD.getDialogue();
		}
	}),

	HOW_THIRD(new NpcDialogue(8206, Expression.CALM,
			"I retain the other 40% for the service of course.",
			"It's not easy standing here all day taking money...") {

		@Override
		public Dialogue nextDialogue(Player p) {
			return HOW_FOURTH.getDialogue();
		}
	}),
	HOW_FOURTH(new NpcDialogue(8206, Expression.CALM,
			"Once a few days have passed since the previous lottery,",
			"I will choose three winners at random.") {

		@Override
		public Dialogue nextDialogue(Player p) {
			return HOW_FIFTH.getDialogue();
		}
	}),
	HOW_FIFTH(new PlayerDialogue(Expression.CONFUSED,
			"How much of the pot do each of the three winners get?") {

		@Override
		public Dialogue nextDialogue(Player p) {
			return HOW_SIXTH.getDialogue();
		}
	}),

	HOW_SIXTH(new NpcDialogue(8206, Expression.CALM,
			"Great question. The first winner I choose will",
			"obtain 60% of the pot. The next person I choose",
			"will obtain 30%, and the last winner I choose will",
			"receive the remaining 10%.") {
		@Override
		public Dialogue nextDialogue(Player p) {
			return HOW_SEVENTH.getDialogue();
		}
	}),

	HOW_SEVENTH(new PlayerDialogue(Expression.CONFUSED,
			"How much does each ticket cost?") {

		@Override
		public Dialogue nextDialogue(Player p) {
			return HOW_EIGHTH.getDialogue();
		}
	}),

	HOW_EIGHTH(new NpcDialogue(8206, Expression.CALM,
			"Another great question. Each ticket costs",
			"5,000,000 coins. Each player can purchase up to 5",
			"tickets per lottery.") {
		@Override
		public Dialogue nextDialogue(Player p) {
			return HOW_NINTH.getDialogue();
		}
	}),
	HOW_NINTH(new PlayerDialogue(Expression.CONFUSED, "When does the lottery end?") {
		@Override
		public Dialogue nextDialogue(Player p) {
			return HOW_TENTH.getDialogue();
		}
	}),
	HOW_TENTH(new NpcDialogue(8206, Expression.CALM,
			"The lottery ends every few days, then a new one starts.",
			"The hour of the day is almost never the same as the last!") {
		@Override
		public Dialogue nextDialogue(Player p) {
			return FIRST_OPTION.getDialogue();
		}
	}),
	BUY_FIRST(new NpcDialogue(8206, Expression.CALM,
			String.format("The cost of a ticket is %s gold coins per ticket.", Lottery.TICKET_COST),
			"Would you like to buy some?") {

		@Override
		public Dialogue nextDialogue(Player p) {
			return BUY_FIRST_OPTION.getDialogue();
		}
	}),
	BUY_FIRST_OPTION(new OptionDialogue("Yes.", "No.") {
		@Override
		public void option(Player player, int option) {
			if (option == 1) {
				player.getPacketSender().sendEnterAmountPrompt("How many tickets would you like to buy? (1 - 5).");
				player.setInputHandling(new Input() {
					@Override
					public void handleAmount(Player player, int amount) {
						Lottery lottery = Lottery.getSingleton();

						if (lottery.getSession() == null || lottery.getPhase() != LotteryPhase.ACTIVE) {
							DialogueManager.start(player, LOTTERY_UNAVAILABLE.getDialogue());
							return;
						}
						if (lottery.queueContains(player.getUsername())) {
							DialogueManager.start(player, RECENTLY_PURCHASED.getDialogue());
							return;
						}
						if (player.getPrivilege().isSpawnRank() || player.isSuperAdmin()) {
							player.getPacketSender().sendInterfaceRemoval();
							player.message("You cannot enter the lottery as you have spawning privileges.");
							return;
						}

						LotterySessionEntry entry = lottery.getSession().getOrNull(player.getUsername());

						if (amount > Lottery.MAXIMUM_ENTRIES_PER_PLAYER) {
							amount = Lottery.MAXIMUM_ENTRIES_PER_PLAYER;
						} else if (amount < 0) {
							amount = 1;
						}
						if (entry != null) {
							if (entry.getTickets() == Lottery.MAXIMUM_ENTRIES_PER_PLAYER) {
								DialogueManager.start(player, MAXIMUM_TICKETS.getDialogue());
								return;
							}
							if (amount + entry.getTickets() > Lottery.MAXIMUM_ENTRIES_PER_PLAYER) {
								amount = Lottery.MAXIMUM_ENTRIES_PER_PLAYER - entry.getTickets();
							}
						}
						if (amount <= 0) {
							player.getPacketSender().sendInterfaceRemoval();
							return;
						}
						int costOfTickets = amount * Lottery.TICKET_COST;

						if (!player.getInventory().hasItem(new Item(995, costOfTickets))) {
						//if (!player.getMoneyPouch().withdrawFromPouchOrInventory(costOfTickets)) {
							DialogueManager.start(player, INSUFFICIENT_FUNDS.getDialogue());
							return;
						}
						lottery.queueEntryUpdate(player.getUsername(), amount);
						DialogueManager.start(player, new NpcDialogue(8206, Expression.CALM,
								String.format("You purchased %s tickets for %s coins.", amount, costOfTickets),
								"I have entered you into the lottery."));
					}
				});
			} else {
				DialogueManager.start(player, FIRST_OPTION.getDialogue());
			}
		}
	}),

	LOTTERY_UNAVAILABLE(new NpcDialogue(8206, Expression.CALM,
			"I am unable to assist you at this time.", "Please come back at a later date.")),
	MAXIMUM_TICKETS(new NpcDialogue(8206, Expression.CALM,
			"You have already bought the maximum tickets.",
			"You cannot buy any more.")),
	INSUFFICIENT_FUNDS(new NpcDialogue(8206, Expression.CALM, "You do not have the coins to cover the cost of", "the tickets.")),
	RECENTLY_PURCHASED(new NpcDialogue(8206, Expression.CALM, "You have recently purchased tickets.", "I need a few minutes to register you.", "Come back shortly."));

	private final Dialogue dialogue;

	LotteryDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
	}

	public Dialogue getDialogue() {
		return dialogue;
	}*/
}
