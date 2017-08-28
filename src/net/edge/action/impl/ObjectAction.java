package net.edge.action.impl;

import net.edge.action.Action;
import net.edge.content.minigame.fightcaves.FightcavesMinigame;
import net.edge.content.minigame.nexchamber.NexMinigame;
import net.edge.content.minigame.pestcontrol.PestControlWaitingLobby;
import net.edge.content.minigame.warriorsguild.WarriorsGuild;
import net.edge.content.object.WebSlashing;
import net.edge.content.skill.agility.impl.wild.WildernessAgility;
import net.edge.content.skill.agility.test.barb.BarbAgility;
import net.edge.content.skill.agility.test.gnome.GnomeAgility;
import net.edge.content.skill.agility.test.shortcuts.Shortcuts;
import net.edge.content.skill.construction.furniture.HotSpots;
import net.edge.content.skill.crafting.PotClaying;
import net.edge.content.skill.hunter.Hunter;
import net.edge.content.skill.hunter.trap.bird.BirdData;
import net.edge.content.skill.hunter.trap.mammal.MammalData;
import net.edge.content.skill.mining.Mining;
import net.edge.content.skill.runecrafting.Runecrafting;
import net.edge.content.skill.smithing.Smelting;
import net.edge.content.skill.thieving.impl.Stalls;
import net.edge.content.skill.woodcutting.Woodcutting;
import net.edge.content.wilderness.Obelisk;
import net.edge.net.packet.in.ObjectActionPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.GameObject;

/**
 * Action handling object action clicks.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
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
//		GnomeStrongholdAgility.action();
		NexMinigame.action();
		GnomeAgility.action();
//		BarbarianOutpostAgility.action();
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
	}

}
