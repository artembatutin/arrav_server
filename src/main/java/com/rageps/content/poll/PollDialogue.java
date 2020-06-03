package com.rageps.content.poll;


import com.rageps.content.dialogue.Dialogue;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.impl.StatementDialogue;
import com.rageps.world.entity.actor.player.Player;

/**
 * Created by Jason MacKeigan on 2017-06-06 at 7:45 PM
 */
public enum PollDialogue {//todo replace with interface
	/*FIRST(new OptionDialogue("Create new poll (closes and overwrites)", "Close existing poll", "Nevermind") {

		@Override
		public void option(Player player, int option) {
			if (option == 1) {
				if (!PollManager.getSingleton().pollCreationAvailable(player)) {
					DialogueManager.start(player, new StatementDialogue("You cannot create a poll right now."));
					return;
				}
				DialogueManager.start(player, CREATE_CONFIRM.getDialogue());
			} else if (option == 2) {
				player.getPacketSender().sendInterfaceRemoval();
				DialogueManager.start(player, CLOSE.getDialogue());
			} else {
				player.getPacketSender().sendInterfaceRemoval();
			}
		}
	}),
	CREATE_CONFIRM(new StatementDialogue("Creating a poll will close and overwrite", "an existing poll if one exists in the",
								 "order you have selected.", "Are you sure you want to do this?") {

		@Override
		public Dialogue nextDialogue(Player p) {
			return new OptionDialogue("Yes, create new poll.", "No, nevermind.") {
				@Override
				public void option(Player player, int option) {
					if (option == 1) {
						player.getPacketSender().sendInterfaceRemoval();
						player.getPacketSender().sendEnterInputPrompt("Enter the order or position will the poll exist at");
						player.setInputHandling(new Input() {
							@Override
							public void handleText(Player player, String text) {
								try {
									PollOrder order = PollOrder.valueOf(text.toUpperCase());

									player.getAttributeMap().set(PollAttributes.CREATE_POLL_ORDER, order);
									player.getPacketSender().sendEnterInputPrompt("Enter the question to the poll");
									player.setInputHandling(new Input() {
										@Override
										public void handleText(Player player, String text) {
											if (text.isEmpty() || text.length() >= 80) {
												DialogueManager.start(player, INCORRECT_QUESTION.getDialogue());
												return;
											}
											player.getAttributeMap().set(PollAttributes.CREATE_POLL_QUESTION, text);

											player.getPacketSender().sendEnterInputPrompt("Enter the poll description.");
											player.setInputHandling(new Input() {
												@Override
												public void handleText(Player player, String text) {
													if (text.isEmpty() || text.length() >= 80) {
														DialogueManager.start(player, INCORRECT_DESCRIPTION.getDialogue());
														return;
													}
													player.getAttributeMap().set(PollAttributes.CREATE_POLL_DESCRIPTION, text);
													player.getPacketSender().sendEnterInputPrompt("Enter the first option");
													player.setInputHandling(new Input() {
														@Override
														public void handleText(Player player, String text) {
															if (text.isEmpty() || text.length() >= 50) {
																DialogueManager.start(player, INCORRECT_OPTION.getDialogue());
																return;
															}
															player.getAttributeMap().set(PollAttributes.CREATE_POLL_OPTION_ONE, text);
															player.getPacketSender().sendEnterInputPrompt("Enter the second option");
															player.setInputHandling(new Input() {
																@Override
																public void handleText(Player player, String text) {
																	if (text.isEmpty() || text.length() >= 80) {
																		DialogueManager.start(player, INCORRECT_OPTION.getDialogue());
																		return;
																	}
																	player.getAttributeMap().set(PollAttributes.CREATE_POLL_OPTION_TWO, text);
																	player.getPacketSender().sendEnterInputPrompt("Enter the third option (type 'null' to leave empty)");
																	player.setInputHandling(new Input() {
																		@Override
																		public void handleText(Player player, String text) {
																			if (text.length() >= 80) {
																				DialogueManager.start(player, INCORRECT_OPTION.getDialogue());
																				return;
																			}
																			player.getAttributeMap().set(PollAttributes.CREATE_POLL_OPTION_THREE, text);
																			player.getPacketSender().sendEnterInputPrompt("Enter the fourth option (type 'null' to leave empty)");
																			player.setInputHandling(new Input() {
																				@Override
																				public void handleText(Player player, String text) {
																					if (text.isEmpty() || text.length() >= 80) {
																						DialogueManager.start(player, INCORRECT_OPTION.getDialogue());
																						return;
																					}
																					player.getAttributeMap().set(PollAttributes.CREATE_POLL_OPTION_FOUR, text);
																					player.getPacketSender().sendEnterAmountPrompt("Enter the amount of days until the poll closes.");
																					player.setInputHandling(new Input() {

																						@Override
																						public void handleAmount(Player player, int amount) {
																							if (amount < 1 || amount > 14) {
																								DialogueManager.start(player, INCORRECT_DATE.getDialogue());
																								return;
																							}
																							player.getAttributeMap().set(PollAttributes.CREATE_POLL_END_DATE, amount);

																							PollManager manager = PollManager.getSingleton();

																							if (manager.pollCreationAvailable(player)) {
																								manager.create(player.getAttributeMap().getObject(PollAttributes.CREATE_POLL_ORDER),
																								 player.getAttributeMap().getString(PollAttributes.CREATE_POLL_QUESTION),
																								 player.getAttributeMap().getString(PollAttributes.CREATE_POLL_DESCRIPTION),
																								 player.getAttributeMap().getString(PollAttributes.CREATE_POLL_OPTION_ONE),
																								 player.getAttributeMap().getString(PollAttributes.CREATE_POLL_OPTION_TWO),
																								 player.getAttributeMap().getString(PollAttributes.CREATE_POLL_OPTION_THREE),
																								 player.getAttributeMap().getString(PollAttributes.CREATE_POLL_OPTION_FOUR),
																								 player.getAttributeMap().getInt(PollAttributes.CREATE_POLL_END_DATE));
																							}
																						}
																					});
																				}
																			});
																		}
																	});
																}
															});
														}
													});
												}
											});
										}
									});
								} catch (IllegalArgumentException e) {
									DialogueManager.start(player, INCORRECT_ORDER_SYNTAX.getDialogue());
								}
							}
						});
					} else {
						player.getPacketSender().sendInterfaceRemoval();
					}
				}
			};
		}
	}),
	CLOSE(new StatementDialogue("If you do this, it cannot be undone.", "Are you sure you want to do this?") {

		@Override
		public Dialogue nextDialogue(Player p) {
			return new OptionDialogue("Yes", "No") {
				@Override
				public void option(Player player, int option) {
					player.getPacketSender().sendInterfaceRemoval();
					if (option == 1) {
						player.getPacketSender().sendEnterInputPrompt("Enter the order of the poll you want to close");
						player.setInputHandling(new Input() {
							@Override
							public void handleText(Player player, String text) {
								try {
									PollManager manager = PollManager.getSingleton();

									PollOrder order = PollOrder.valueOf(text.toUpperCase());

									Poll poll = manager.getPolls().get(order);

									if (poll != null) {
										if (manager.pollClosingPossible(player, order)) {
											manager.closeAndAnnounce(poll);
											manager.updateClosedPoll(poll);
										}
									}
								} catch (IllegalArgumentException exception) {
									DialogueManager.start(player, INCORRECT_ORDER_SYNTAX.getDialogue());
								}
							}
						});
					} else {
						player.getPacketSender().sendInterfaceRemoval();
					}
				}
			};
		}
	}),
	INCORRECT_ORDER_SYNTAX(new StatementDialogue("Incorrect syntax for order. The order", "must be first, second, third",
												 "fourth, or fifth.") {
		@Override
		public Dialogue nextDialogue(Player p) {
			return FIRST.getDialogue();
		}
	}),
	INCORRECT_QUESTION(new StatementDialogue("Poll question cannot be empty, or greater than 50 characters.") {
		@Override
		public Dialogue nextDialogue(Player p) {
			return CREATE_CONFIRM.getDialogue();
		}
	}),
	INCORRECT_OPTION(new StatementDialogue("Poll option cannot be empty unless the option", "is the third or fourth. All options must", "have no more than 50 characters.") {
		@Override
		public Dialogue nextDialogue(Player p) {
			return CREATE_CONFIRM.getDialogue();
		}
	}),
	INCORRECT_DATE(new StatementDialogue("Poll end date must be between 1 and 14 days.") {
		@Override
		public Dialogue nextDialogue(Player p) {
			return CREATE_CONFIRM.getDialogue();
		}
	}),
	INCORRECT_DESCRIPTION(new StatementDialogue("The description must be between 1 and 50 characters.") {

		@Override
		public Dialogue nextDialogue(Player p) {
			return CREATE_CONFIRM.getDialogue();
		}
	})*/
	;

	private final Dialogue dialogue;

	PollDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
	}

	public Dialogue getDialogue() {
		return dialogue;
	}
}
