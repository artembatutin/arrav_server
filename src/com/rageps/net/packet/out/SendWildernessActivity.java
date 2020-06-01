package com.rageps.net.packet.out;

import com.rageps.content.wilderness.WildernessActivity;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectList;

public final class SendWildernessActivity implements OutgoingPacket {
	
	private final ObjectList<Player> pkers;
	
	public SendWildernessActivity(ObjectList<Player> pkers) {
		this.pkers = pkers;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(150, GamePacketType.VARIABLE_SHORT);
		int fools = WildernessActivity.getFooledCount(player);
		out.put(pkers.size() + fools);
		for(Player p : pkers) {
			out.put(WildernessActivity.getX(p.getPosition()));
			out.put(WildernessActivity.getY(p.getPosition()));
		}
		if(fools > 0) {//fooled map activity.
			while(fools != 0) {
				out.put(WildernessActivity.fooledX());
				out.put(WildernessActivity.fooledY());
				fools--;
			}
		}
		out.endVarSize();
		return out;
	}
}
