package com.rageps.action.impl;

import com.rageps.content.minigame.fightcaves.FightcavesMinigame;
import com.rageps.content.minigame.nexchamber.NexMinigame;
import com.rageps.content.minigame.pestcontrol.PestControlWaitingLobby;
import com.rageps.content.minigame.warriorsguild.WarriorsGuild;
import com.rageps.content.object.BarChair;
import com.rageps.content.object.WebSlashing;
import com.rageps.content.skill.agility.impl.wild.WildernessAgility;
import com.rageps.content.skill.agility.test.barb.BarbAgility;
import com.rageps.content.skill.agility.test.gnome.GnomeAgility;
import com.rageps.content.skill.agility.test.shortcuts.Shortcuts;
import com.rageps.content.skill.construction.furniture.HotSpots;
import com.rageps.content.skill.crafting.PotClaying;
import com.rageps.content.skill.hunter.Hunter;
import com.rageps.content.skill.hunter.trap.bird.BirdData;
import com.rageps.content.skill.hunter.trap.mammal.MammalData;
import com.rageps.content.skill.mining.Mining;
import com.rageps.content.skill.runecrafting.Runecrafting;
import com.rageps.content.skill.smithing.Smelting;
import com.rageps.content.skill.thieving.impl.Stalls;
import com.rageps.content.skill.woodcutting.Woodcutting;
import com.rageps.content.wilderness.Obelisk;
import com.rageps.net.packet.in.ObjectActionPacket;
import com.rageps.action.Action;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;

/**
 * Action handling object action clicks.
 * @author Artem Batutin
 */
public abstract class ObjectAction extends Action {
	
	public abstract boolean click(Player player, GameObject object, int click);
	
	public void registerFirst(int object) {
		ObjectActionPacket.FIRST.register(object, this);
	}
	
	public void registerSecond(int object) {
		ObjectActionPacket.SECOND.register(object, this);
	}
	
	public void registerThird(int object) {
		ObjectActionPacket.THIRD.register(object, this);
	}
	
	public void registerFourth(int object) {
		ObjectActionPacket.FOURTH.register(object, this);
	}
	
	public void registerFifth(int object) {
		ObjectActionPacket.FIFTH.register(object, this);
	}
	
	public void registerCons(int object) {
		ObjectActionPacket.CONSTRUCTION.register(object, this);
	}
	
	public static void init() {
		WebSlashing.action();
		WarriorsGuild.action();
		Obelisk.action();
		FightcavesMinigame.action();
		Woodcutting.action();
		Mining.action();
		Runecrafting.action();
		//GnomeStrongholdAgility.action();
		NexMinigame.action();
		GnomeAgility.action();
		//BarbarianOutpostAgility.action();
		BarbAgility.action();
		WildernessAgility.action();
		Shortcuts.action();
		PotClaying.action();
		Stalls.action();
		Smelting.action();
		HotSpots.action();
		PestControlWaitingLobby.event();
		BirdData.action();
		MammalData.action();
		Hunter.action();
		BarChair.action();
	}
	
}
