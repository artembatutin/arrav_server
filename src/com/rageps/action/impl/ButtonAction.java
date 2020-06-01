package com.rageps.action.impl;

import com.rageps.content.Emote;
import com.rageps.content.PlayerPanel;
import com.rageps.net.packet.in.ClickButtonPacket;
import com.rageps.action.Action;
import com.rageps.action.but.SpellbookButton;
import com.rageps.world.entity.actor.player.Player;

/**
 * Action handling button clicks.
 * @author Artem Batutin
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
