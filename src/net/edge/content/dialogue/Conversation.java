package net.edge.content.dialogue;

import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.world.node.entity.player.Player;

/**
 * The concrete class which represents a contract each conversation should bind
 * to. This class should hold dynamic support for broad dialogues.
 * <p></p>
 * If you're wanting to register a simple dialogue use {@link DialogueBuilder#append(Dialogue...)}.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public interface Conversation {
	
	/**
	 * The method which holds functionality for the player when the dialogue's pointer
	 * reaches equality with the {@code index}.
	 * @param player the player the extra functionality is for.
	 * @param index  the index of the current dialogue present in this conversation.
	 */
	void send(Player player, int index);
	
	/**
	 * The array which holds all the dialogues specified for this {@link Conversation}.
	 * @param player the player to send these dialogues to.
	 * @return an array of {@link Dialogue}s.
	 */
	DialogueAppender dialogues(Player player);
}
