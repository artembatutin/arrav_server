package net.arrav.action.mob;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.MobAction;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.region.TraversalMap;
import net.arrav.world.locale.Position;

public class StrayDog extends ActionInitializer {
	@Override
	public void init() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				player.animation(new Animation(2110));
				player.forceChat("Thbbbbt!");
				npc.forceChat("Whine!");
				Position pos = TraversalMap.getRandomNearby(npc.getPosition(), player.getPosition(), npc.size());
				if(pos != null)
					npc.getMovementQueue().walk(pos);
				return true;
			}
		};
		e.registerFourth(5917);
	}
}
