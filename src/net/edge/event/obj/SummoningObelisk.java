package net.edge.event.obj;

import net.edge.content.skill.Skills;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

public class SummoningObelisk extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(click == 2) {
					Skills.restore(player, Skills.SUMMONING);
					return true;
				}
				player.getMessages().sendInterface(-8);
				return true;
			}
		};
		l.registerFirst(29947);
		l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				Skills.restore(player, Skills.SUMMONING);
				return true;
			}
		};
		l.registerFirst(29955);
	}
}
