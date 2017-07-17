package net.edge.net.packet.in;

import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.World;
import net.edge.world.node.EntityState;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;

/**
 * The message sent from the client when a player sends the load tool.mapviewer region
 * message.
 * @author Artem Batutin <artembatutin@gmail.com></artembatutin@gmail.com>
 * @author lare96 <http://github.com/lare96>
 */
public final class UpdateRegionPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.isUpdateRegion()) {
			World.getRegions().updateRegionObjects(player);
			player.sendInterfaces();
			player.getTolerance().reset();
			player.setUpdates(false, false);
			player.setUpdateRegion(false);
			World.getRegions().getAllSurroundingRegions(player.getPosition().getRegion()).forEach(r -> {
				if(r.getState() != EntityState.ACTIVE) {
					r.setState(EntityState.ACTIVE);
				}
				r.onEnter(player);
			});
			if(player.getRights().greater(Rights.ADMINISTRATOR))
				player.message("DEBUG[region= " + player.getPosition().getRegion() + "]");
		}
	}
}
