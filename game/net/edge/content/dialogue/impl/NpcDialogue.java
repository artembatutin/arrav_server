package net.edge.content.dialogue.impl;

import net.edge.content.dialogue.Dialogue;
import net.edge.content.dialogue.DialogueBuilder;
import net.edge.content.dialogue.DialogueType;
import net.edge.content.dialogue.Expression;
import net.edge.world.node.entity.npc.NpcDefinition;

/**
 * The dialogue chain entry that sends the player a dialogue from an NPC.
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDialogue extends Dialogue {
	
	/**
	 * The identifier for the NPC sending this dialogue.
	 */
	private final int npc;
	
	/**
	 * The expression that this NPC will display.
	 */
	private final Expression expression;
	
	/**
	 * Creates a new {@link NpcDialogue}.
	 * @param npc        the identifier for the NPC sending this dialogue.
	 * @param expression the expression that this NPC will display.
	 * @param text       the text that will be displayed on the dialogue.
	 */
	public NpcDialogue(int npc, Expression expression, String... text) {
		super(text);
		this.npc = npc;
		this.expression = expression;
	}
	
	/**
	 * Creates a new {@link NpcDialogue} with the default expression.
	 * @param npc  the identifier for the NPC sending this dialogue.
	 * @param text the text that will be displayed on the dialogue.
	 */
	public NpcDialogue(int npc, String... text) {
		this(npc, Expression.CALM, text);
	}
	
	@Override
	public void accept(DialogueBuilder dialogue) {
		switch(getText().length) {
			case 1:
				dialogue.getPlayer().getMessages().sendInterfaceAnimation(4883, expression.getExpression());
				dialogue.getPlayer().getMessages().sendString(NpcDefinition.DEFINITIONS[npc].getName(), 4884);
				dialogue.getPlayer().getMessages().sendString(getText()[0], 4885);
				dialogue.getPlayer().getMessages().sendNpcModelOnInterface(4883, npc);
				dialogue.getPlayer().getMessages().sendChatInterface(4882);
				break;
			case 2:
				dialogue.getPlayer().getMessages().sendInterfaceAnimation(4888, expression.getExpression());
				dialogue.getPlayer().getMessages().sendString(NpcDefinition.DEFINITIONS[npc].getName(), 4889);
				dialogue.getPlayer().getMessages().sendString(getText()[0], 4890);
				dialogue.getPlayer().getMessages().sendString(getText()[1], 4891);
				dialogue.getPlayer().getMessages().sendNpcModelOnInterface(4888, npc);
				dialogue.getPlayer().getMessages().sendChatInterface(4887);
				break;
			case 3:
				dialogue.getPlayer().getMessages().sendInterfaceAnimation(4894, expression.getExpression());
				dialogue.getPlayer().getMessages().sendString(NpcDefinition.DEFINITIONS[npc].getName(), 4895);
				dialogue.getPlayer().getMessages().sendString(getText()[0], 4896);
				dialogue.getPlayer().getMessages().sendString(getText()[1], 4897);
				dialogue.getPlayer().getMessages().sendString(getText()[2], 4898);
				dialogue.getPlayer().getMessages().sendNpcModelOnInterface(4894, npc);
				dialogue.getPlayer().getMessages().sendChatInterface(4893);
				break;
			case 4:
				dialogue.getPlayer().getMessages().sendInterfaceAnimation(4901, expression.getExpression());
				dialogue.getPlayer().getMessages().sendString(NpcDefinition.DEFINITIONS[npc].getName(), 4902);
				dialogue.getPlayer().getMessages().sendString(getText()[0], 4903);
				dialogue.getPlayer().getMessages().sendString(getText()[1], 4904);
				dialogue.getPlayer().getMessages().sendString(getText()[2], 4905);
				dialogue.getPlayer().getMessages().sendString(getText()[3], 4906);
				dialogue.getPlayer().getMessages().sendNpcModelOnInterface(4901, npc);
				dialogue.getPlayer().getMessages().sendChatInterface(4900);
				break;
			default:
				throw new IllegalArgumentException("Illegal npc dialogue " + "length: " + getText().length);
		}
	}
	
	@Override
	public DialogueType type() {
		return DialogueType.NPC_DIALOGUE;
	}
}
