package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.content.skill.construction.furniture.Furniture;
import net.arrav.content.skill.construction.furniture.HotSpots;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

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
	public ByteBuf write(Player player, ByteBuf buf) {
		Furniture[] panel = spot.getFurnitures();
		buf.message(130, GamePacketType.VARIABLE_BYTE);
		buf.put(panel.length);
		for(Furniture furniture : panel) {
			buf.putShort(furniture.getItemId());
			buf.put(furniture.getLevel());
			buf.put(furniture.getRequiredItems().length);
			for(Item req : furniture.getRequiredItems()) {
				buf.putShort(req.getId());
				buf.putShort(req.getAmount());
			}
		}
		buf.endVarSize();
		return buf;
	}
}
