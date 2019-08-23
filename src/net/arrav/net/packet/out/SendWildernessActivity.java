package net.arrav.net.packet.out;


import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.content.wilderness.WildernessActivity;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendWildernessActivity implements OutgoingPacket {
	
	private final ObjectList<Player> pkers;
	
	public SendWildernessActivity(ObjectList<Player> pkers) {
		this.pkers = pkers;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
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
