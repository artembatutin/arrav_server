package net.edge.action.npc;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.NpcAction;
import net.edge.world.locale.Position;
import net.edge.world.Animation;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

public class StrayDog extends ActionInitializer {
	@Override
	public void init() {
		NpcAction e = new NpcAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
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
