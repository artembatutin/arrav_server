package net.arrav.content.dialogue.impl;

import net.arrav.content.dialogue.Dialogue;
import net.arrav.content.dialogue.DialogueBuilder;
import net.arrav.content.dialogue.DialogueType;
import net.arrav.content.dialogue.Expression;
import net.arrav.net.packet.out.SendInterfaceAnimation;
import net.arrav.net.packet.out.SendInterfaceNpcModel;
import net.arrav.world.entity.actor.mob.MobDefinition;

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
	 * @param npc the identifier for the NPC sending this dialogue.
	 * @param expression the expression that this NPC will display.
	 * @param text the text that will be displayed on the dialogue.
	 */
	public NpcDialogue(int npc, Expression expression, String... text) {
		super(text);
		this.npc = npc;
		this.expression = expression;
	}
	
	/**
	 * Creates a new {@link NpcDialogue} with the default expression.
	 * @param npc the identifier for the NPC sending this dialogue.
	 * @param text the text that will be displayed on the dialogue.
	 */
	public NpcDialogue(int npc, String... text) {
		this(npc, Expression.CALM, text);
	}
	
	@Override
	public void accept(DialogueBuilder dialogue) {
		switch(getText().length) {
			case 1:
				dialogue.getPlayer().out(new SendInterfaceAnimation(4883, expression.getExpression()));
				dialogue.getPlayer().text(4884, MobDefinition.DEFINITIONS[npc].getName());
				dialogue.getPlayer().text(4885, getText()[0]);
				dialogue.getPlayer().out(new SendInterfaceNpcModel(4883, npc));
				dialogue.getPlayer().chatWidget(4882);
				break;
			case 2:
				dialogue.getPlayer().out(new SendInterfaceAnimation(4888, expression.getExpression()));
				dialogue.getPlayer().text(4889, MobDefinition.DEFINITIONS[npc].getName());
				dialogue.getPlayer().text(4890, getText()[0]);
				dialogue.getPlayer().text(4891, getText()[1]);
				dialogue.getPlayer().out(new SendInterfaceNpcModel(4888, npc));
				dialogue.getPlayer().chatWidget(4887);
				break;
			case 3:
				dialogue.getPlayer().out(new SendInterfaceAnimation(4894, expression.getExpression()));
				dialogue.getPlayer().text(4895, MobDefinition.DEFINITIONS[npc].getName());
				dialogue.getPlayer().text(4896, getText()[0]);
				dialogue.getPlayer().text(4897, getText()[1]);
				dialogue.getPlayer().text(4898, getText()[2]);
				dialogue.getPlayer().out(new SendInterfaceNpcModel(4894, npc));
				dialogue.getPlayer().chatWidget(4893);
				break;
			case 4:
				dialogue.getPlayer().out(new SendInterfaceAnimation(4901, expression.getExpression()));
				dialogue.getPlayer().text(4902, MobDefinition.DEFINITIONS[npc].getName());
				dialogue.getPlayer().text(4903, getText()[0]);
				dialogue.getPlayer().text(4904, getText()[1]);
				dialogue.getPlayer().text(4905, getText()[2]);
				dialogue.getPlayer().text(4906, getText()[3]);
				dialogue.getPlayer().out(new SendInterfaceNpcModel(4901, npc));
				dialogue.getPlayer().chatWidget(4900);
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
