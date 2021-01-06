package com.rageps.content.dialogue.impl;

import com.rageps.net.packet.out.SendItemModelInterface;
import com.rageps.content.dialogue.Dialogue;
import com.rageps.content.dialogue.DialogueBuilder;
import com.rageps.content.dialogue.DialogueType;
import com.rageps.util.ActionListener;
import com.rageps.world.entity.item.Item;

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
			t.getPlayer().interfaceText(308, getText()[0]);
			t.getPlayer().send(new ItemModelInterface(307, 200, item.getId()));
			t.getPlayer().chatWidget(306);
		} else {
			t.getPlayer().interfaceText(357, "You do not have enough space in your inventory!");
			t.getPlayer().interfaceText(358, "Click here to continue");
			t.getPlayer().chatWidget(356);
		}
	}

	@Override
	public DialogueType type() {
		return DialogueType.GIVE_ITEM_DIALOGUE;
	}
}