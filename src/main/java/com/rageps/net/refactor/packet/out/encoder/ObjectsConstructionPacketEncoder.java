package com.rageps.net.refactor.packet.out.encoder;

import com.rageps.content.skill.construction.furniture.Furniture;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.refactor.codec.game.GamePacket;
import com.rageps.net.refactor.codec.game.GamePacketBuilder;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.net.refactor.packet.out.PacketEncoder;
import com.rageps.net.refactor.packet.out.model.ObjectsConstructionPacket;
import com.rageps.world.entity.item.Item;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ObjectsConstructionPacketEncoder implements PacketEncoder<ObjectsConstructionPacket> {

    @Override
    public GamePacket encode(ObjectsConstructionPacket message) {
        GamePacketBuilder builder = new GamePacketBuilder(130, PacketType.VARIABLE_BYTE);
        Furniture[] panel = message.getSpot().getFurnitures();
        builder.put(panel.length);
        for(Furniture furniture : panel) {
            builder.putShort(furniture.getItemId());
            builder.put(furniture.getLevel());
            builder.put(furniture.getRequiredItems().length);
            for(Item req : furniture.getRequiredItems()) {
                builder.putShort(req.getId());
                builder.putShort(req.getAmount());
            }
        }
        return builder.toGamePacket();
    }

    @Override
    public boolean onSent(ObjectsConstructionPacket message) {
        Furniture[] panel = message.getSpot().getFurnitures();
        if(panel == null || panel.length == 0)
            return false;
        message.getPlayer().getHouse().get().getPlan().setPanel(panel);
        return true;
    }
}
