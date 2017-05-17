package net.edge.world.content.dialogue;

import com.google.common.collect.Iterables;
import net.edge.utils.ActionListener;
import net.edge.world.content.dialogue.impl.OptionDialogue;
import net.edge.world.content.dialogue.test.DialogueAppender;
import net.edge.world.model.node.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents the manager class with functional methods for dialogues.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DialogueBuilder {
	
	/**
	 * The player this builder is for.
	 */
	protected final Player player;
	
	/**
	 * The optional value which <b>if</b> provided can be used for dynamic
	 * actions.
	 */
	private Optional<Conversation> conversation = Optional.empty();
	
	/**
	 * The option listener for option dialogues.
	 */
	private Optional<Consumer<OptionDialogue.OptionType>> optionListener = Optional.empty();
	
	/**
	 * The list of all our dialogues for this conversation.
	 */
	private final List<Dialogue> chain = new ArrayList<>();
	
	/**
	 * The position we're at in this chain.
	 * <b></b>
	 * Since we aren't removing / adding values to have support for skipping,
	 * getting the first and last dialogue this will act as our pointer
	 * acting as the index we're at in the list.
	 */
	private int position;
	
	/**
	 * Constructs a new {@link DialogueBuilder}.
	 * @param player the player this builder is for.
	 */
	public DialogueBuilder(Player player) {
		this.player = player;
	}
	
	/**
	 * Advances this <b>chain</b> to the next dialogue.
	 */
	public void advance() {
		if(chain.isEmpty()) {
			player.getMessages().sendCloseWindows();
			return;
		}
		if(position >= chain.size()) {
			Optional<ActionListener> lastAction = chain.get(position - 1).getAfterFunction();
			if(lastAction.isPresent()) {
				lastAction.get().execute();
			} else {
				player.getMessages().sendCloseWindows();
			}
			return;
		}
		
		previousDialogue().ifPresent(d -> d.getAfterFunction().ifPresent(ActionListener::execute));
		
		if(position >= chain.size()) {
			return;
		}
		
		Dialogue current = chain.get(position);
		
		conversation.ifPresent(conv -> conv.send(player, position));
		current.getFunction().ifPresent(ActionListener::execute);
		current.accept(this);
		position += 1;
	}
	
	/**
	 * Sends a conversation to a player.
	 * @param conv the conversation to send.
	 */
	public void send(Conversation conv) {
		send(conv.dialogues(player));
	}
	
	/**
	 * Sends a conversation to a player.
	 * @param conv the conversation to send.
	 */
	public void send(DialogueAppender ap) {
		interrupt();
		chain.addAll(ap.getDialogue());
		position = 1;
		firstDialogue().ifPresent(first -> {
			first.accept(this);
			first.getFunction().ifPresent(ActionListener::execute);
		});
	}
	
	/**
	 * Appends a sequential or an array of dialogues to the backing list.
	 * <p></p>
	 * This can be used in order for sending dialogues in a more fashionable way instead
	 * of creating a whole new class for a simple dialogue.
	 * @param dialogues the dialogues to send.
	 */
	public void append(Dialogue... dialogues) {
		interrupt();
		chain.addAll(Arrays.asList(dialogues));
		position = 1;
		firstDialogue().ifPresent(first -> {
			first.accept(this);
			first.getFunction().ifPresent(ActionListener::execute);
		});
	}
	
	/**
	 * Gets the first dialogue in this list.
	 * @return the first dialogue.
	 */
	public Optional<Dialogue> firstDialogue() {
		Dialogue dialogue = Iterables.getFirst(chain, null);
		return dialogue != null ? Optional.of(dialogue) : Optional.empty();
	}
	
	/**
	 * Goes to the first dialogue in this list.
	 */
	public void first() {
		if(chain.isEmpty()) {
			return;
		}
		position = 1;
		chain.get(position - 1).accept(this);
	}
	
	/**
	 * Skips the next dialogue in the chain.
	 */
	public void skip() {
		if(chain.isEmpty()) {
			return;
		}
		chain.get(position + 1).accept(this);
		position += 2;
	}
	
	/**
	 * Goes to the specified {@code index} in this chain.
	 */
	public void go(int index) {
		if(chain.isEmpty()) {
			return;
		}
		chain.get(position + (index - 1)).accept(this);
		position += index;
	}
	
	/**
	 * Goes to the previous dialogue in the chain, the reason we decrement
	 * by 2 is because we increment again when the dialogue is advanced.
	 */
	public void previous() {
		if(chain.isEmpty()) {
			return;
		}
		position -= 1;
		chain.get(position - 1).accept(this);
	}
	
	public Optional<Dialogue> previousDialogue() {
		if(chain.isEmpty()) {
			return Optional.empty();
		}
		return chain.isEmpty() ? Optional.empty() : Optional.of(chain.get(position - 1));
		
	}
	
	/**
	 * Gets the last dialogue in this list.
	 * @return the last dialogue.
	 */
	public Optional<Dialogue> lastDialogue() {
		Dialogue dialogue = Iterables.getLast(chain, null);
		return dialogue != null ? Optional.of(dialogue) : Optional.empty();
	}
	
	/**
	 * Gets the last dialogue in this list.
	 * @return the last dialogue.
	 */
	public void last() {
		if(chain.isEmpty()) {
			return;
		}
		position = chain.size();
		chain.get(position - 1).accept(this);
	}
	
	/**
	 * Interrupts this conversation by clearing the list of dialogues.
	 */
	public void interrupt() {
		chain.clear();
		conversation = Optional.empty();
		position = 0;
	}
	
	/**
	 * @return the chain
	 */
	public List<Dialogue> getChain() {
		return chain;
	}
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Appends an option listener to the backing {@link #optionListener}.
	 * @param optionListener the option listener to set.
	 */
	public void appendOptionListener(Consumer<OptionDialogue.OptionType> optionListener) {
		this.setOptionListener(Optional.of(optionListener));
	}
	
	/**
	 * @return the optionListener
	 */
	public Optional<Consumer<OptionDialogue.OptionType>> getOptionListener() {
		return optionListener;
	}
	
	/**
	 * @param optionListener the optionListener to set
	 */
	public void setOptionListener(Optional<Consumer<OptionDialogue.OptionType>> optionListener) {
		this.optionListener = optionListener;
	}
}
