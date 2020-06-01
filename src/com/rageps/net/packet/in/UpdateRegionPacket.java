package com.rageps.net.packet.in;

import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.World;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.region.Region;

/**
 * The message sent from the client when a player sends the load tool.mapviewer region
 * message.
 * @author Artem Batutin</artembatutin@gmail.com>
 * @author lare96 <http://github.com/lare96>
 */
public final class UpdateRegionPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.isUpdateRegion()) {
			World.getRegions().updateRegionObjects(player);
			player.sendInterfaces();
			player.getTolerance().reset();
			player.setUpdates(false, false);
			player.setUpdateRegion(false);
			ObjectList<Region> surrounding = World.getRegions().getAllSurroundingRegions(player.getPosition().getRegion());
			if(surrounding != null) {
				for(Region r : surrounding) {
					if(r.getState() != EntityState.ACTIVE) {
						r.setState(EntityState.ACTIVE);
					}
					r.onEnter(player);
				}
			}
			if(player.getRights().greater(Rights.ADMINISTRATOR))
				player.message("DEBUG[region= " + player.getPosition().getRegion() + "]");
		}
	}
}
