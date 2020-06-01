package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.content.skill.construction.furniture.Furniture;
import com.rageps.content.skill.construction.furniture.HotSpots;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		Furniture[] panel = spot.getFurnitures();
		out.message(130, GamePacketType.VARIABLE_BYTE);
		out.put(panel.length);
		for(Furniture furniture : panel) {
			out.putShort(furniture.getItemId());
			out.put(furniture.getLevel());
			out.put(furniture.getRequiredItems().length);
			for(Item req : furniture.getRequiredItems()) {
				out.putShort(req.getId());
				out.putShort(req.getAmount());
			}
		}
		out.endVarSize();
		return out;
	}
}
