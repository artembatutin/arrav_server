package net.edge.event.obj;

import net.edge.content.skill.Skills;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.world.node.actor.player.Player;
import net.edge.world.object.ObjectNode;

public class SummoningObelisk extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(click == 3 && object.getId() == 29447) {
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
