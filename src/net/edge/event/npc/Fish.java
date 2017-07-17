package net.edge.event.npc;

import net.edge.content.skill.fishing.Fishing;
import net.edge.content.skill.fishing.Tool;
import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

public class Fish extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				Fishing fishing = new Fishing(player, Tool.FISHING_ROD, npc.getPosition());
				fishing.start();
				return true;
			}
		};
		e.registerFirst(233);
		e.registerFirst(234);
		e.registerFirst(235);
		e.registerFirst(236);
		e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				Fishing fly_fishing = new Fishing(player, Tool.FLY_FISHING_ROD, npc.getPosition());
				fly_fishing.start();
				return true;
			}
		};
		e.registerFirst(309);
		e.registerFirst(310);
		e.registerFirst(311);
		e.registerFirst(314);
		e.registerFirst(315);
		e.registerFirst(317);
		e.registerFirst(318);
		e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				Fishing lobster_pot = new Fishing(player, Tool.LOBSTER_POT, npc.getPosition());
				lobster_pot.start();
				return true;
			}
		};
		e.registerFirst(312);
		e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				Fishing big_net = new Fishing(player, Tool.BIG_NET, npc.getPosition());
				big_net.start();
				return true;
			}
		};
		e.registerFirst(313);
		e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				Fishing net = new Fishing(player, Tool.NET, npc.getPosition());
				net.start();
				return true;
			}
		};
		e.registerFirst(316);
		e.registerFirst(319);
		e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				Fishing net_monkfish = new Fishing(player, Tool.NET_MONKFISH, npc.getPosition());
				net_monkfish.start();
				return true;
			}
		};
		e.registerFirst(322);
		
		e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				Fishing lobster_pot = new Fishing(player, Tool.FISHING_ROD, npc.getPosition());
				lobster_pot.start();
				return true;
			}
		};
		e.registerSecond(309);
		e.registerSecond(310);
		e.registerSecond(311);
		e.registerSecond(314);
		e.registerSecond(315);
		e.registerSecond(317);
		e.registerSecond(318);
		e.registerSecond(319);
		e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				Fishing lobster_pot = new Fishing(player, Tool.HARPOON, npc.getPosition());
				lobster_pot.start();
				return true;
			}
		};
		e.registerSecond(312);
		e.registerSecond(322);
		e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				Fishing lobster_pot = new Fishing(player, Tool.SHARK_HARPOON, npc.getPosition());
				lobster_pot.start();
				return true;
			}
		};
		e.registerSecond(313);
	}
}
