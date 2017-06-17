package net.edge.content.dialogue.impl;

import net.edge.util.ActionListener;
import net.edge.content.dialogue.Dialogue;
import net.edge.content.dialogue.DialogueBuilder;
import net.edge.content.dialogue.DialogueType;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * The dialogue chain entry that gives the player an item.
 * @author lare96 <http://github.com/lare96>
 */
public final class GiveItemDialogue extends Dialogue {
	
	/**
	 * The item to give to the player during this chain.
	 */
	private final Item item;
	
	/**
	 * The action to execute when the requested item is given.
	 */
	private final Optional<ActionListener> action;
	
	/**
	 * Creates a new {@link GiveItemDialogue}.
	 * @param item the item to give to the player during this chain.
	 * @param text the text to display when the item is given.
	 */
	public GiveItemDialogue(Item item, String text, Optional<ActionListener> action) {
		super(text);
		this.item = item;
		this.action = action;
	}
	
	@Override
	public void accept(DialogueBuilder t) {
		if(t.getPlayer().getInventory().canAdd(item)) {
			t.getPlayer().getInventory().add(item);
			action.ifPresent(ActionListener::execute);
			t.getPlayer().getMessages().sendString(getText()[0], 308);
			t.getPlayer().getMessages().sendItemModelOnInterface(307, 200, item.getId());
			t.getPlayer().getMessages().sendChatInterface(306);
		} else {
			t.getPlayer().getMessages().sendChatboxString("You do not have enough space in your inventory!");
		}
	}
	
	@Override
	public DialogueType type() {
		return DialogueType.GIVE_ITEM_DIALOGUE;
	}
}