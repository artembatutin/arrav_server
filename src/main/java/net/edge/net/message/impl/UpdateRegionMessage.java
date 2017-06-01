package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.message.InputMessageListener;
import net.edge.world.World;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

/**
 * The message sent from the client when a player sends the load mapviewer region
 * message.
 * @author Artem Batutin <artembatutin@gmail.com></artembatutin@gmail.com>
 * @author lare96 <http://github.com/lare96>
 */
public final class UpdateRegionMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.isUpdateRegion()) {
			World.getRegions().updateRegionObjects(player);
			player.sendInterfaces();
			player.getTolerance().reset();
			player.setUpdateRegion(false);
			World.getRegions().getAllSurroundingRegions(player.getPosition().getRegion()).forEach(r -> {
				if(r.getState() != NodeState.ACTIVE) {
					r.setState(NodeState.ACTIVE);
				}
				r.onEnter(player);
			});
			if(player.getRights().greater(Rights.ADMINISTRATOR))
				player.message("DEBUG[region= " + player.getPosition().getRegion() + "]");
		}
	}
}
