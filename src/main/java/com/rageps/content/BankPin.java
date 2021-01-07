package com.rageps.content;

import com.rageps.content.dialogue.impl.StatementDialogue;
import com.rageps.net.packet.out.SendCloseInterface;
import com.rageps.net.packet.out.SendMoveComponent;
import com.rageps.net.refactor.packet.out.model.CloseInterfacePacket;
import com.rageps.net.refactor.packet.out.model.MoveComponentPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;

public class BankPin {
	
	private static final int[] actionButtons = {58025, 58026, 58027, 58028, 58029, 58030, 58031, 58032, 58033, 58034};
	private static final int[] sendFrames = {14883, 14884, 14885, 14886, 14887, 14888, 14889, 14890, 14891, 14892};
	private static final int[] starFrames = {14913, 14914, 14915, 14916};
	private static final String[] pinNames = {"First click the FIRST digit.", "Now click the SECOND digit.", "Time for the THIRD digit.", "Finally, the FOURTH digit."};
	
	/**
	 * Sets up the pin interface
	 */
	public static void loadUpPinInterface(Player player) {
		if(player.bankPin.length() < 4) {
			player.bankPin = "";
			player.interfaceText(14923, "Bank of "+World.get().getEnvironment().getName());
			player.interfaceText(14920, "@or1@Set up a bank PIN using the buttons below.");
			player.interfaceText(15313, "First click the FIRST digit.");
			randomizeButtons(player);
			player.getInterfaceManager().open(7424, true);
		} else {
			player.enterPin = "";
			setStars(player);
			randomizeButtons(player);
			player.interfaceText(14923, "Bank of "+ World.get().getEnvironment().getName());
			player.interfaceText(14920, "@or1@Please enter your PIN using the buttons below.");
			player.interfaceText(15313, "First click the FIRST digit.");
			player.getInterfaceManager().open(7424, true);
		}
	}
	
	/**
	 * Handles clicking the button's action.
	 */
	public static void pinButtonAction(Player player, int button) {
		int index = -1;
		
		for(int i = 0; i < 10; i++) {
			if(button == player.pinOrder[i]) {
				index = i;
				break;
			}
		}
		
		if(index == -1) {
			return;
		}
		
		if(player.bankPin.length() < 4) {
			player.bankPin += "" + index;
			if(player.bankPin.length() >= 4) {
				player.message("Pin set successfully.");
				player.getDialogueBuilder().append(new StatementDialogue("Your bank pin has been set to @blu@" + player.bankPin + "@bla@, Please Write it down.").attachAfter(() -> player.getBank().open(false)));
				player.enterPin = player.bankPin;
			} else {
				randomizeButtons(player);
				player.interfaceText(14923, "Bank of "+World.get().getEnvironment().getName());
				player.interfaceText(14920, "@or1@Set up a bank PIN using the buttons below.");
				player.interfaceText(15313, "" + (pinNames[player.bankPin.length()]));
			}
		} else {
			player.enterPin += "" + index;
			setStars(player);
			if(player.enterPin.length() >= 4 && player.enterPin.equals(player.bankPin)) {
				player.message("Pin entered correctly.");
				if(!player.resetingPin) {
					player.resetingPin = false;
					player.getBank().open(false);
				} else
					player.send(new CloseInterfacePacket(player.getDialogueBuilder()));
			} else if(player.enterPin.length() >= 4) {
				player.message("@blu@Pin entered incorrectly, Please try again.");
				player.getBank().open(false);
				player.enterPin = "";
			} else {
				randomizeButtons(player);
				player.interfaceText(14923, "Bank of "+ World.get().getEnvironment().getName());
				player.interfaceText(14920, "@or1@Please enter your PIN using the buttons below.");
				player.interfaceText(15313, "" + (pinNames[player.enterPin.length()]));
			}
		}
	}
	
	private static void setStars(Player player) {
		for(int i = 0; i < 4; i++) {
			if(player.enterPin.length() > i)
				player.interfaceText(starFrames[i], "@or1@*");
			else
				player.interfaceText(starFrames[i], "@or1@?");
		}
	}
	
	public static int random(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}
	
	private static void randomizeButtons(Player player) {
		int pinOrder;
		for(pinOrder = 0; pinOrder < 10; pinOrder++) {
			player.pinOrder[pinOrder] = 0;
		}
		pinOrder = -1;
		while(pinOrder < 9) {
			pinOrder++;
			while(true) {
				int j = random(9);
				if(player.pinOrder[j] == 0) {
					player.pinOrder[j] = actionButtons[pinOrder];
					player.interfaceText(sendFrames[pinOrder], "" + j);
					player.send(new MoveComponentPacket(random(+45), random(-45), sendFrames[pinOrder]));
					break;
				}
			}
		}
	}
	
}
