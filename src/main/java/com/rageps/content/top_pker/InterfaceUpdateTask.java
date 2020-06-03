package com.rageps.content.top_pker;


import com.rageps.task.Task;
import com.rageps.world.entity.actor.player.Player;

/**
 * Created by Jason M on 2017-03-16 at 1:38 AM
 */
class InterfaceUpdateTask extends Task {

	public InterfaceUpdateTask(Player key) {
		super(25, key, true);
	}

	@Override
	protected void execute() {
		Player player = (Player) getAttachment().get();

//		if (player.getInterfaceId() != 50000) {todo - add widget manager
//			cancel();
//			return;
//		}
		TopPkerInterface.getSingleton().updateScoreboard(player);
	}

	@Override
	public void onCancel() {
		super.onCancel();

		((Player) super.getAttachment().get()).getAttributeMap().reset(AttributeKeys.INTERFACE_EVENT);
	}
}
