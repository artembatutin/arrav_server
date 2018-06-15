package net.arrav.content;

import net.arrav.content.dialogue.impl.StatementDialogue;
import net.arrav.net.packet.out.SendCloseInterface;
import net.arrav.net.packet.out.SendInterface;
import net.arrav.net.packet.out.SendMoveComponent;
import net.arrav.world.entity.actor.player.Player;

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
			player.text(14923, "Bank of Arrav");
			player.text(14920, "@or1@Set up a bank PIN using the buttons below.");
			player.text(15313, "First click the FIRST digit.");
			randomizeButtons(player);
			player.out(new SendInterface(7424));
		} else {
			player.enterPin = "";
			setStars(player);
			randomizeButtons(player);
			player.text(14923, "Bank of Arrav");
			player.text(14920, "@or1@Please enter your PIN using the buttons below.");
			player.text(15313, "First click the FIRST digit.");
			player.out(new SendInterface(7424));
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
				player.text(14923, "Bank of Arrav");
				player.text(14920, "@or1@Set up a bank PIN using the buttons below.");
				player.text(15313, "" + (pinNames[player.bankPin.length()]));
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
					player.out(new SendCloseInterface());
			} else if(player.enterPin.length() >= 4) {
				player.message("@blu@Pin entered incorrectly, Please try again.");
				player.getBank().open(false);
				player.enterPin = "";
			} else {
				randomizeButtons(player);
				player.text(14923, "Bank of Arrav");
				player.text(14920, "@or1@Please enter your PIN using the buttons below.");
				player.text(15313, "" + (pinNames[player.enterPin.length()]));
			}
		}
	}
	
	private static void setStars(Player player) {
		for(int i = 0; i < 4; i++) {
			if(player.enterPin.length() > i)
				player.text(starFrames[i], "@or1@*");
			else
				player.text(starFrames[i], "@or1@?");
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
					player.text(sendFrames[pinOrder], "" + j);
					player.out(new SendMoveComponent(random(+45), random(-45), sendFrames[pinOrder]));
					break;
				}
			}
		}
	}
	
}
