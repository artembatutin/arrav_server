package com.rageps.action.impl;

import com.rageps.content.item.Skillcape;
import com.rageps.content.object.star.StarSprite;
import com.rageps.content.scoreboard.ScoreboardManager;
import com.rageps.content.skill.hunter.butterfly.ButterflyCatching;
import com.rageps.content.skill.slayer.Slayer;
import com.rageps.content.skill.thieving.impl.Pickpocketing;
import com.rageps.action.Action;
import com.rageps.net.packet.in.MobActionPacket;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

/**
 * Action handling npc action clicks.
 * @author Artem Batutin
 */
public abstract class MobAction extends Action {
	
	public abstract boolean click(Player player, Mob mob, int click);
	
	public void registerFirst(int npc) {
		MobActionPacket.FIRST.register(npc, this);
	}
	
	public void registerSecond(int npc) {
		MobActionPacket.SECOND.register(npc, this);
	}
	
	public void registerThird(int npc) {
		MobActionPacket.THIRD.register(npc, this);
	}
	
	public void registerFourth(int npc) {
		MobActionPacket.FOURTH.register(npc, this);
	}
	
	public static void init() {
		ScoreboardManager.action();
		ButterflyCatching.action();
		Slayer.actionMob();
		StarSprite.action();
		Pickpocketing.action();
		Skillcape.action();
	}
	
}
