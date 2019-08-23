package net.arrav.net.packet.out;


import net.arrav.content.skill.construction.furniture.Furniture;
import net.arrav.content.skill.construction.furniture.HotSpots;
import net.arrav.net.codec.game.GamePacket;
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
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
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
