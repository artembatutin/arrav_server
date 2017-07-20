package net.edge.content.dialogue;

import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * The static utility class that contains functions for sending dialogues.
 * @author lare96 <http://github.com/lare96>
 */
public final class Dialogues {
	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private Dialogues() {
		throw new UnsupportedOperationException("This class cannot be instantiated!");
	}
	
	/**
	 * Executes the option listeners for the backing option dialogue.
	 * @param player the player this listener is for.
	 * @param button the button clicked.
	 */
	public static boolean executeOptionListeners(Player player, int button) {
		Optional<OptionDialogue.OptionType> option = OptionDialogue.OptionType.getOptions(button);
		
		if(!option.isPresent()) {
			return false;
		}
		
		Optional<Consumer<OptionDialogue.OptionType>> value = player.getDialogueBuilder().getOptionListener();
		if(!value.isPresent() || player.getDialogueBuilder().getChain().isEmpty()) {
			return false;
		}
		
		value.get().accept(option.get());
		return true;
	}
}
