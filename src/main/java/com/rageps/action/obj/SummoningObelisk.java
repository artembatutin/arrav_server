package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.skill.Skills;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;

public class SummoningObelisk extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(click == 3 && object.getId() == 29947) {
					player.widget(-8);
					return true;
				}
				Skills.restore(player, Skills.SUMMONING);
				return true;
			}
		};
		for(int i = 29938; i < 29960; i++) {
			l.registerFirst(i);
			l.registerFirst(i + 42003);
			l.registerSecond(i);
			l.registerSecond(i + 42003);
			l.registerThird(i);
			l.registerThird(i + 42003);
		}
	}
}
