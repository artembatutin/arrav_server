package net.edge.content.dialogue.impl;

import net.edge.content.dialogue.Dialogue;
import net.edge.content.dialogue.DialogueBuilder;
import net.edge.content.dialogue.DialogueType;
import net.edge.content.dialogue.Expression;
import net.edge.net.packet.out.SendInterfaceAnimation;
import net.edge.net.packet.out.SendInterfacePlayerModel;

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
		dialogue.getPlayer().out(new SendInterfaceAnimation(250, expression.getExpression()));
		switch(getText().length) {
			case 1:
				dialogue.getPlayer().out(new SendInterfaceAnimation(969, expression.getExpression()));
				dialogue.getPlayer().text(970, dialogue.getPlayer().getFormatUsername());
				dialogue.getPlayer().text(971, getText()[0]);
				dialogue.getPlayer().out(new SendInterfacePlayerModel(969));
				dialogue.getPlayer().chatWidget(968);
				break;
			case 2:
				dialogue.getPlayer().out(new SendInterfaceAnimation(974, expression.getExpression()));
				dialogue.getPlayer().text(975, dialogue.getPlayer().getFormatUsername());
				dialogue.getPlayer().text(976, getText()[0]);
				dialogue.getPlayer().text(977, getText()[1]);
				dialogue.getPlayer().out(new SendInterfacePlayerModel(974));
				dialogue.getPlayer().chatWidget(973);
				break;
			case 3:
				dialogue.getPlayer().out(new SendInterfaceAnimation(980, expression.getExpression()));
				dialogue.getPlayer().text(981, dialogue.getPlayer().getFormatUsername());
				dialogue.getPlayer().text(982, getText()[0]);
				dialogue.getPlayer().text(983, getText()[1]);
				dialogue.getPlayer().text(984, getText()[2]);
				dialogue.getPlayer().out(new SendInterfacePlayerModel(980));
				dialogue.getPlayer().chatWidget(979);
				break;
			case 4:
				dialogue.getPlayer().out(new SendInterfaceAnimation(987, expression.getExpression()));
				dialogue.getPlayer().text(988, dialogue.getPlayer().getFormatUsername());
				dialogue.getPlayer().text(989, getText()[0]);
				dialogue.getPlayer().text(990, getText()[1]);
				dialogue.getPlayer().text(991, getText()[2]);
				dialogue.getPlayer().text(992, getText()[3]);
				dialogue.getPlayer().out(new SendInterfacePlayerModel(987));
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
