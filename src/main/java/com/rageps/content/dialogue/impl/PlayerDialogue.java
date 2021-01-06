package com.rageps.content.dialogue.impl;

import com.rageps.net.packet.out.SendInterfaceAnimation;
import com.rageps.content.dialogue.Dialogue;
import com.rageps.content.dialogue.DialogueBuilder;
import com.rageps.content.dialogue.DialogueType;
import com.rageps.content.dialogue.Expression;
import com.rageps.net.packet.out.SendInterfacePlayerModel;

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
	 * @param text the text that will be displayed on the dialogue.
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
		dialogue.getPlayer().send(new InterfaceAnimation(250, expression.getExpression()));
		switch(getText().length) {
			case 1:
				dialogue.getPlayer().send(new InterfaceAnimation(969, expression.getExpression()));
				dialogue.getPlayer().interfaceText(970, dialogue.getPlayer().getFormatUsername());
				dialogue.getPlayer().interfaceText(971, getText()[0]);
				dialogue.getPlayer().send(new InterfacePlayerModel(969));
				dialogue.getPlayer().chatWidget(968);
				break;
			case 2:
				dialogue.getPlayer().send(new InterfaceAnimation(974, expression.getExpression()));
				dialogue.getPlayer().interfaceText(975, dialogue.getPlayer().getFormatUsername());
				dialogue.getPlayer().interfaceText(976, getText()[0]);
				dialogue.getPlayer().interfaceText(977, getText()[1]);
				dialogue.getPlayer().send(new InterfacePlayerModel(974));
				dialogue.getPlayer().chatWidget(973);
				break;
			case 3:
				dialogue.getPlayer().send(new InterfaceAnimation(980, expression.getExpression()));
				dialogue.getPlayer().interfaceText(981, dialogue.getPlayer().getFormatUsername());
				dialogue.getPlayer().interfaceText(982, getText()[0]);
				dialogue.getPlayer().interfaceText(983, getText()[1]);
				dialogue.getPlayer().interfaceText(984, getText()[2]);
				dialogue.getPlayer().send(new InterfacePlayerModel(980));
				dialogue.getPlayer().chatWidget(979);
				break;
			case 4:
				dialogue.getPlayer().send(new InterfaceAnimation(987, expression.getExpression()));
				dialogue.getPlayer().interfaceText(988, dialogue.getPlayer().getFormatUsername());
				dialogue.getPlayer().interfaceText(989, getText()[0]);
				dialogue.getPlayer().interfaceText(990, getText()[1]);
				dialogue.getPlayer().interfaceText(991, getText()[2]);
				dialogue.getPlayer().interfaceText(992, getText()[3]);
				dialogue.getPlayer().send(new InterfacePlayerModel(987));
				dialogue.getPlayer().chatWidget(986);
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
