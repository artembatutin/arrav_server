package net.arrav.action.impl;

import net.arrav.action.Action;
import net.arrav.action.but.SpellbookButton;
import net.arrav.content.Emote;
import net.arrav.content.PlayerPanel;
import net.arrav.net.packet.in.ClickButtonPacket;
import net.arrav.world.entity.actor.player.Player;

/**
 * Action handling button clicks.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class ButtonAction extends Action {
	
	public abstract boolean click(Player player, int button);
	
	public void register(int button) {
		ClickButtonPacket.BUTTONS.register(button, this);
	}
	
	public static void init() {
		new SpellbookButton();
		PlayerPanel.action();
		Emote.action();
	}
	
}
