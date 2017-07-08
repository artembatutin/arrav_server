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
				dialogue.getPlayer().text(357, getText()[0]);
				dialogue.getPlayer().chatWidget(356);
				break;
			case 2:
				dialogue.getPlayer().text(360, getText()[0]);
				dialogue.getPlayer().text(361, getText()[1]);
				dialogue.getPlayer().chatWidget(359);
				break;
			case 3:
				dialogue.getPlayer().text(364, getText()[0]);
				dialogue.getPlayer().text(365, getText()[1]);
				dialogue.getPlayer().text(366, getText()[2]);
				dialogue.getPlayer().chatWidget(363);
				break;
			case 4:
				dialogue.getPlayer().text(369, getText()[0]);
				dialogue.getPlayer().text(370, getText()[1]);
				dialogue.getPlayer().text(371, getText()[2]);
				dialogue.getPlayer().text(372, getText()[3]);
				dialogue.getPlayer().chatWidget(368);
				break;
		}
	}
	
	@Override
	public DialogueType type() {
		return DialogueType.STATEMENT_DIALOGUE;
	}
}
