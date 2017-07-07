package net.edge.event.impl;

import net.edge.content.PlayerPanel;
import net.edge.event.Event;
import net.edge.event.but.Spellbook;
import net.edge.net.packet.in.ClickButtonPacket;
import net.edge.world.node.entity.player.Player;

/**
 * Event handling button clicks.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class ButtonEvent extends Event {
	
	public abstract boolean click(Player player, int button);
	
	public void register(int button) {
		ClickButtonPacket.BUTTONS.register(button, this);
	}
	
	public static void init() {
		new Spellbook();
		PlayerPanel.event();
	}
	
}
