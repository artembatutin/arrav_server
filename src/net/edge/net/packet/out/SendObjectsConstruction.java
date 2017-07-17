package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.content.skill.construction.furniture.Furniture;
import net.edge.content.skill.construction.furniture.HotSpots;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;

public final class SendObjectsConstruction implements OutgoingPacket {
	
	private final HotSpots spot;
	
	public SendObjectsConstruction(HotSpots spot) {
		this.spot = spot;
	}
	
	@Override
	public boolean onSent(Player player) {
		Furniture[] panel = spot.getFurnitures();
		if(panel == null || panel.length == 0)
			return false;
		player.getHouse().get().getPlan().setPanel(panel);
		return true;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		Furniture[] panel = spot.getFurnitures();
		msg.message(130, PacketType.VARIABLE_BYTE);
		msg.put(panel.length);
		for(Furniture furniture : panel) {
			msg.putShort(furniture.getItemId());
			msg.put(furniture.getLevel());
			msg.put(furniture.getRequiredItems().length);
			for(Item req : furniture.getRequiredItems()) {
				msg.putShort(req.getId());
				msg.putShort(req.getAmount());
			}
		}
		msg.endVarSize();
		return msg.getBuffer();
	}
}
