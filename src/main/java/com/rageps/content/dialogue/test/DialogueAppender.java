package com.rageps.content.dialogue.test;

import com.rageps.content.dialogue.Dialogue;
import com.rageps.content.dialogue.DialogueBuilder;
import com.rageps.world.entity.actor.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.List;

/**
 * Represents the dialogue appender class which is a more functional way of
 * appending dialogues to a player.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DialogueAppender {
	
	/**
	 * The player to append the dialogues to.
	 */
	private final Player player;
	
	/**
	 * The builder for this dialogue appender.
	 */
	private final DialogueBuilder builder;
	
	/**
	 * The list of dialogues to send.
	 */
	private final ObjectList<Dialogue> dialogues = new ObjectArrayList<>();
	
	/**
	 * Constructs a new {@link DialogueAppender}.
	 * @param player {@link #player}.
	 */
	public DialogueAppender(Player player) {
		this.player = player;
		this.builder = player.getDialogueBuilder();
	}
	
	/**
	 * Chains a dialogue to the underlying {@code dialogues} list.
	 * @param dialogue the dialogue to add to the chain.
	 * @return an instance of this class for chaining.
	 */
	public DialogueAppender chain(Dialogue dialogue) {
		this.dialogues.add(dialogue);
		return this;
	}
	
	/**
	 * Starts this dialogue by submitting and sending the necessary packets.
	 */
	public void start() {
		builder.send(this);
	}
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * @return the builder
	 */
	public DialogueBuilder getBuilder() {
		return builder;
	}
	
	/**
	 * @return the dialogue
	 */
	public List<Dialogue> getDialogue() {
		return dialogues;
	}
}
