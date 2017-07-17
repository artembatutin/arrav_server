package net.edge.action.npc;

import net.edge.content.skill.fishing.Fishing;
import net.edge.content.skill.fishing.Tool;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.NpcAction;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

public class Fish extends ActionInitializer {
	@Override
	public void init() {
		NpcAction e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				Fishing fishing = new Fishing(player, Tool.FISHING_ROD, npc.getPosition());
				fishing.start();
				return true;
			}
		};
		e.registerFirst(233);
		e.registerFirst(234);
		e.registerFirst(235);
		e.registerFirst(236);
		e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob mob, int click) {
				Fishing fly_fishing = new Fishing(player, Tool.FLY_FISHING_ROD, mob.getPosition());
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
		e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob mob, int click) {
				Fishing lobster_pot = new Fishing(player, Tool.LOBSTER_POT, mob.getPosition());
				lobster_pot.start();
				return true;
			}
		};
		e.registerFirst(312);
		e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob mob, int click) {
				Fishing big_net = new Fishing(player, Tool.BIG_NET, mob.getPosition());
				big_net.start();
				return true;
			}
		};
		e.registerFirst(313);
		e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob mob, int click) {
				Fishing net = new Fishing(player, Tool.NET, mob.getPosition());
				net.start();
				return true;
			}
		};
		e.registerFirst(316);
		e.registerFirst(319);
		e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob mob, int click) {
				Fishing net_monkfish = new Fishing(player, Tool.NET_MONKFISH, mob.getPosition());
				net_monkfish.start();
				return true;
			}
		};
		e.registerFirst(322);
		
		e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob mob, int click) {
				Fishing lobster_pot = new Fishing(player, Tool.FISHING_ROD, mob.getPosition());
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
		e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob mob, int click) {
				Fishing lobster_pot = new Fishing(player, Tool.HARPOON, mob.getPosition());
				lobster_pot.start();
				return true;
			}
		};
		e.registerSecond(312);
		e.registerSecond(322);
		e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob mob, int click) {
				Fishing lobster_pot = new Fishing(player, Tool.SHARK_HARPOON, mob.getPosition());
				lobster_pot.start();
				return true;
			}
		};
		e.registerSecond(313);
	}
}
