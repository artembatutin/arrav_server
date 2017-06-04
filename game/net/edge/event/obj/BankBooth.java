package net.edge.event.obj;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.world.Animation;
import net.edge.world.node.entity.move.ForcedMovement;
import net.edge.world.node.entity.move.ForcedMovementManager;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectDefinition;
import net.edge.world.object.ObjectNode;

public class BankBooth extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent open = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.getBank().open();
				return true;
			}
		};
		//first
		open.registerFirst(3193);
		open.registerFirst(3193);
		open.registerFirst(2213);
		open.registerFirst(34752);
		open.registerFirst(26972);
		open.registerFirst(30928);
		open.registerFirst(19230);
		open.registerFirst(11402);
		open.registerFirst(72931);
		open.registerFirst(35647);
		open.registerFirst(44216);
		//second
		open.registerSecond(3193);
		open.registerSecond(2213);
		open.registerSecond(34752);
		open.registerSecond(26972);
		open.registerSecond(30928);
		open.registerSecond(19230);
		open.registerSecond(11402);
		open.registerSecond(72931);
		open.registerSecond(35647);
		open.registerSecond(44216);
	}
}
