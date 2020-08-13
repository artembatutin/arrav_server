package com.rageps.action.mob;

import com.rageps.action.impl.MobAction;
import com.rageps.action.ActionInitializer;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.region.TraversalMap;
import com.rageps.world.locale.Position;

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
