package net.edge.event.npc;

import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.locale.Position;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.player.Player;

public class StrayDog extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				player.animation(new Animation(2110));
				player.forceChat("Thbbbbt!");
				npc.forceChat("Whine!");
				Position pos = World.getTraversalMap().getRandomNearby(npc.getPosition(), player.getPosition(), npc.size());
				if(pos != null)
					npc.getMovementQueue().walk(pos);
				return true;
			}
		};
		e.registerFourth(5917);
	}
}
