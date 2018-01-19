package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.object.GameObject;

public class BankBooth extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction open = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.getBank().open();
				return true;
			}
		};
		//first
		open.registerFirst(3193);
		open.registerFirst(2213);
		open.registerFirst(34752);
		open.registerFirst(26972);
		open.registerFirst(30928);
		open.registerFirst(19230);
		open.registerFirst(11402);
		open.registerFirst(72931);
		open.registerFirst(30928);
		open.registerFirst(35647);
		open.registerFirst(44216);
		open.registerFirst(27663);
		open.registerFirst(14367);
		open.registerFirst(99267);
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
		open.registerSecond(14367);
	}
}
