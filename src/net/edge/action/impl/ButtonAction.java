package net.edge.action.impl;

import net.edge.content.PlayerPanel;
import net.edge.action.Action;
import net.edge.action.but.Spellbook;
import net.edge.net.packet.in.ClickButtonPacket;
import net.edge.world.entity.actor.player.Player;

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
		new Spellbook();
		PlayerPanel.event();
	}
	
}
