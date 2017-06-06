package net.edge.event.npc;

import net.edge.event.EventInitializer;
import net.edge.event.impl.NpcEvent;
import net.edge.locale.Position;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

import java.util.Optional;

public class StrayDog extends EventInitializer {
	@Override
	public void init() {
		NpcEvent e = new NpcEvent() {
			@Override
			public boolean click(Player player, Npc npc, int click) {
				player.animation(new Animation(2110));
				player.forceChat("Thbbbbt!");
				npc.forceChat("Whine!");
				Optional<Position> move = World.getTraversalMap().getRandomTraversableTile(npc.getPosition(), npc.size(), player.getPosition());
				move.ifPresent(position1 -> npc.getMovementQueue().walk(position1));
				return true;
			}
		};
		e.registerFourth(5917);
	}
}
