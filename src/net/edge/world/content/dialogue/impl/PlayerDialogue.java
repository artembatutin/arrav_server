package net.edge.world.content.dialogue.impl;

import net.edge.world.content.dialogue.Dialogue;
import net.edge.world.content.dialogue.DialogueBuilder;
import net.edge.world.content.dialogue.DialogueType;
import net.edge.world.content.dialogue.Expression;

/**
 * The dialogue chain entry that sends the player a dialogue from a player.
 * @author lare96 <http://github.com/lare96>
 */
public final class PlayerDialogue extends Dialogue {
	
	/**
	 * The expression that this player will display.
	 */
	private final Expression expression;
	
	/**
	 * Creates a new {@link PlayerDialogue}.
	 * @param expression the expression that this player will display.
	 * @param text       the text that will be displayed on the dialogue.
	 */
	public PlayerDialogue(Expression expression, String... text) {
		super(text);
		this.expression = expression;
	}
	
	/**
	 * Creates a new {@link PlayerDialogue} with the default expression.
	 * @param text the text that will be displayed on the dialogue.
	 */
	public PlayerDialogue(String... text) {
		this(Expression.CALM, text);
	}
	
	@Override
	public void accept(DialogueBuilder dialogue) {
		dialogue.getPlayer().getMessages().sendInterfaceAnimation(250, expression.getExpression());
		switch(getText().length) {
			case 1:
				dialogue.getPlayer().getMessages().sendInterfaceAnimation(969, expression.getExpression());
				dialogue.getPlayer().getMessages().sendString(dialogue.getPlayer().getFormatUsername(), 970);
				dialogue.getPlayer().getMessages().sendString(getText()[0], 971);
				dialogue.getPlayer().getMessages().sendPlayerModelOnInterface(969);
				dialogue.getPlayer().getMessages().sendChatInterface(968);
				break;
			case 2:
				dialogue.getPlayer().getMessages().sendInterfaceAnimation(974, expression.getExpression());
				dialogue.getPlayer().getMessages().sendString(dialogue.getPlayer().getFormatUsername(), 975);
				dialogue.getPlayer().getMessages().sendString(getText()[0], 976);
				dialogue.getPlayer().getMessages().sendString(getText()[1], 977);
				dialogue.getPlayer().getMessages().sendPlayerModelOnInterface(974);
				dialogue.getPlayer().getMessages().sendChatInterface(973);
				break;
			case 3:
				dialogue.getPlayer().getMessages().sendInterfaceAnimation(980, expression.getExpression());
				dialogue.getPlayer().getMessages().sendString(dialogue.getPlayer().getFormatUsername(), 981);
				dialogue.getPlayer().getMessages().sendString(getText()[0], 982);
				dialogue.getPlayer().getMessages().sendString(getText()[1], 983);
				dialogue.getPlayer().getMessages().sendString(getText()[2], 984);
				dialogue.getPlayer().getMessages().sendPlayerModelOnInterface(980);
				dialogue.getPlayer().getMessages().sendChatInterface(979);
				break;
			case 4:
				dialogue.getPlayer().getMessages().sendInterfaceAnimation(987, expression.getExpression());
				dialogue.getPlayer().getMessages().sendString(dialogue.getPlayer().getFormatUsername(), 988);
				dialogue.getPlayer().getMessages().sendString(getText()[0], 989);
				dialogue.getPlayer().getMessages().sendString(getText()[1], 990);
				dialogue.getPlayer().getMessages().sendString(getText()[2], 991);
				dialogue.getPlayer().getMessages().sendString(getText()[3], 992);
				dialogue.getPlayer().getMessages().sendPlayerModelOnInterface(987);
				dialogue.getPlayer().getMessages().sendChatInterface(986);
				break;
			default:
				throw new IllegalArgumentException("Illegal player dialogue " + "length: " + getText().length);
		}
	}
	
	@Override
	public DialogueType type() {
		return DialogueType.PLAYER_DIALOGUE;
	}
}
