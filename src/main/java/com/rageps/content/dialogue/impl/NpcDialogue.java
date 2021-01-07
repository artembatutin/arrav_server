package com.rageps.content.dialogue.impl;

import com.rageps.content.dialogue.Dialogue;
import com.rageps.content.dialogue.DialogueBuilder;
import com.rageps.content.dialogue.DialogueType;
import com.rageps.content.dialogue.Expression;
import com.rageps.net.refactor.packet.out.model.InterfaceAnimationPacket;
import com.rageps.net.refactor.packet.out.model.InterfaceNpcModelPacket;
import com.rageps.world.entity.actor.mob.MobDefinition;

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
				dialogue.getPlayer().send(new InterfaceAnimationPacket(4883, expression.getExpression()));
				dialogue.getPlayer().interfaceText(4884, MobDefinition.DEFINITIONS[npc].getName());
				dialogue.getPlayer().interfaceText(4885, getText()[0]);
				dialogue.getPlayer().send(new InterfaceNpcModelPacket(4883, npc));
				dialogue.getPlayer().chatWidget(4882);
				break;
			case 2:
				dialogue.getPlayer().send(new InterfaceAnimationPacket(4888, expression.getExpression()));
				dialogue.getPlayer().interfaceText(4889, MobDefinition.DEFINITIONS[npc].getName());
				dialogue.getPlayer().interfaceText(4890, getText()[0]);
				dialogue.getPlayer().interfaceText(4891, getText()[1]);
				dialogue.getPlayer().send(new InterfaceNpcModelPacket(4888, npc));
				dialogue.getPlayer().chatWidget(4887);
				break;
			case 3:
				dialogue.getPlayer().send(new InterfaceAnimationPacket(4894, expression.getExpression()));
				dialogue.getPlayer().interfaceText(4895, MobDefinition.DEFINITIONS[npc].getName());
				dialogue.getPlayer().interfaceText(4896, getText()[0]);
				dialogue.getPlayer().interfaceText(4897, getText()[1]);
				dialogue.getPlayer().interfaceText(4898, getText()[2]);
				dialogue.getPlayer().send(new InterfaceNpcModelPacket(4894, npc));
				dialogue.getPlayer().chatWidget(4893);
				break;
			case 4:
				dialogue.getPlayer().send(new InterfaceAnimationPacket(4901, expression.getExpression()));
				dialogue.getPlayer().interfaceText(4902, MobDefinition.DEFINITIONS[npc].getName());
				dialogue.getPlayer().interfaceText(4903, getText()[0]);
				dialogue.getPlayer().interfaceText(4904, getText()[1]);
				dialogue.getPlayer().interfaceText(4905, getText()[2]);
				dialogue.getPlayer().interfaceText(4906, getText()[3]);
				dialogue.getPlayer().send(new InterfaceNpcModelPacket(4901, npc));
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
