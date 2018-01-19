package net.arrav.action.impl;

import net.arrav.GameConstants;
import net.arrav.action.Action;
import net.arrav.content.minigame.fightcaves.FightcavesMinigame;
import net.arrav.content.minigame.nexchamber.NexMinigame;
import net.arrav.content.minigame.pestcontrol.PestControlWaitingLobby;
import net.arrav.content.minigame.warriorsguild.WarriorsGuild;
import net.arrav.content.object.WebSlashing;
import net.arrav.content.skill.agility.impl.wild.WildernessAgility;
import net.arrav.content.skill.agility.test.barb.BarbAgility;
import net.arrav.content.skill.agility.test.gnome.GnomeAgility;
import net.arrav.content.skill.agility.test.shortcuts.Shortcuts;
import net.arrav.content.skill.construction.furniture.HotSpots;
import net.arrav.content.skill.crafting.PotClaying;
import net.arrav.content.skill.hunter.Hunter;
import net.arrav.content.skill.hunter.trap.bird.BirdData;
import net.arrav.content.skill.hunter.trap.mammal.MammalData;
import net.arrav.content.skill.mining.Mining;
import net.arrav.content.skill.runecrafting.Runecrafting;
import net.arrav.content.skill.smithing.Smelting;
import net.arrav.content.skill.thieving.impl.Stalls;
import net.arrav.content.skill.woodcutting.Woodcutting;
import net.arrav.content.teleport.TeleportType;
import net.arrav.content.wilderness.Obelisk;
import net.arrav.net.packet.in.ObjectActionPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;
import net.arrav.world.object.GameObject;

/**
 * Action handling object action clicks.
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

			ObjectAction a = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					player.teleport(new Position(3507, 9494), TeleportType.LADDER);
					return true;
				}
			};
			a.registerFirst(65613);
		a = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(new Position(3509, 9496, 2), TeleportType.LADDER);
				return true;
			}
		};
		a.registerFirst(45835);
		a = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.teleport(GameConstants.STARTING_POSITION, TeleportType.LADDER);
				return true;
			}
		};
		a.registerFirst(45832);
	}
	
}
