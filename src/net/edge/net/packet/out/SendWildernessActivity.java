package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.wilderness.WildernessActivity;
import net.edge.net.codec.game.GamePacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendWildernessActivity implements OutgoingPacket {
	
	private final ObjectList<Player> pkers;
	
	public SendWildernessActivity(ObjectList<Player> pkers) {
		this.pkers = pkers;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(150, GamePacketType.VARIABLE_SHORT);
		int fools = WildernessActivity.getFooledCount(player);
		buf.put(pkers.size() + fools);
		for(Player p : pkers) {
			buf.put(WildernessActivity.getX(p.getPosition()));
			buf.put(WildernessActivity.getY(p.getPosition()));
		}
		if(fools > 0) {//fooled map activity.
			while(fools != 0) {
				buf.put(WildernessActivity.fooledX());
				buf.put(WildernessActivity.fooledY());
				fools--;
			}
		}
		buf.endVarSize();
		return buf;
	}
}
