package net.edge.action.obj;

import net.edge.content.skill.Skills;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.GameObject;

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
