package net.edge.content.dialogue.impl;

import net.edge.content.dialogue.Dialogue;
import net.edge.content.dialogue.DialogueBuilder;
import net.edge.content.dialogue.DialogueType;

/**
 * The dialogue chain entry which sends the player a statement dialogue.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class StatementDialogue extends Dialogue {
	
	/**
	 * Constructs a new {@link StatementDialogue}.
	 * @param text the text that will be displayed on the dialogue.
	 */
	public StatementDialogue(String... text) {
		super(text);
	}
	
	@Override
	public void accept(DialogueBuilder dialogue) {
		switch(getText().length) {
			case 1:
				dialogue.getPlayer().getMessages().sendString(getText()[0], 357);
				dialogue.getPlayer().getMessages().sendChatInterface(356);
				break;
			case 2:
				dialogue.getPlayer().getMessages().sendString(getText()[0], 360);
				dialogue.getPlayer().getMessages().sendString(getText()[1], 361);
				dialogue.getPlayer().getMessages().sendChatInterface(359);
				break;
			case 3:
				dialogue.getPlayer().getMessages().sendString(getText()[0], 364);
				dialogue.getPlayer().getMessages().sendString(getText()[1], 365);
				dialogue.getPlayer().getMessages().sendString(getText()[2], 366);
				dialogue.getPlayer().getMessages().sendChatInterface(363);
				break;
			case 4:
				dialogue.getPlayer().getMessages().sendString(getText()[0], 369);
				dialogue.getPlayer().getMessages().sendString(getText()[1], 370);
				dialogue.getPlayer().getMessages().sendString(getText()[2], 371);
				dialogue.getPlayer().getMessages().sendString(getText()[3], 372);
				dialogue.getPlayer().getMessages().sendChatInterface(368);
				break;
		}
	}
	
	@Override
	public DialogueType type() {
		return DialogueType.STATEMENT_DIALOGUE;
	}
}
