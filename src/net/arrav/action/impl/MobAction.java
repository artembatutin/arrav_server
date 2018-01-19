package net.arrav.action.impl;

import net.arrav.action.Action;
import net.arrav.content.item.Skillcape;
import net.arrav.content.object.star.StarSprite;
import net.arrav.content.scoreboard.ScoreboardManager;
import net.arrav.content.skill.hunter.butterfly.ButterflyCatching;
import net.arrav.content.skill.slayer.Slayer;
import net.arrav.content.skill.thieving.impl.Pickpocketing;
import net.arrav.net.packet.in.MobActionPacket;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

/**
 * Action handling npc action clicks.
 * @author Artem Batutin <artembatutin@gmail.com>
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
