package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.wilderness.WildernessActivity;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendWildernessActivity implements OutgoingPacket {
	
	private final ObjectList<Player> pkers;
	
	public SendWildernessActivity(ObjectList<Player> pkers) {
		this.pkers = pkers;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(150, PacketType.VARIABLE_SHORT);
		int fools = WildernessActivity.getFooledCount(player);
		msg.put(pkers.size() + fools);
		for(Player p : pkers) {
			msg.put(WildernessActivity.getX(p.getPosition()));
			msg.put(WildernessActivity.getY(p.getPosition()));
		}
		if(fools > 0) {//fooled map activity.
			while(fools != 0) {
				msg.put(WildernessActivity.fooledX());
				msg.put(WildernessActivity.fooledY());
				fools--;
			}
		}
		msg.endVarSize();
		return msg.getBuffer();
	}
}
