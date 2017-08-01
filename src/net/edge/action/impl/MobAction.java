package net.edge.action.impl;

import net.edge.content.item.Skillcape;
import net.edge.content.scoreboard.ScoreboardManager;
import net.edge.content.shootingstar.StarSprite;
import net.edge.content.skill.hunter.butterfly.ButterflyCatching;
import net.edge.content.skill.slayer.Slayer;
import net.edge.content.skill.thieving.impl.Pickpocketing;
import net.edge.action.Action;
import net.edge.net.packet.in.NpcActionPacket;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * Action handling npc action clicks.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class MobAction extends Action {
	
	public abstract boolean click(Player player, Mob mob, int click);
	
	public void registerFirst(int npc) {
		NpcActionPacket.FIRST.register(npc, this);
	}
	
	public void registerSecond(int npc) {
		NpcActionPacket.SECOND.register(npc, this);
	}
	
	public void registerThird(int npc) {
		NpcActionPacket.THIRD.register(npc, this);
	}
	
	public void registerFourth(int npc) {
		NpcActionPacket.FOURTH.register(npc, this);
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
