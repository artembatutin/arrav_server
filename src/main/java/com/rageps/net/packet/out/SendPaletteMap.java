package com.rageps.net.packet.out;

import com.rageps.content.skill.construction.Palette;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendPaletteMap implements OutgoingPacket {
	
	private final Palette palette;
	
	public SendPaletteMap(Palette palette) {
		this.palette = palette;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(241, GamePacketType.VARIABLE_SHORT);
		out.putShort(player.getPosition().getRegionX() + 6, ByteTransform.A);
		out.putShort(player.getPosition().getRegionY() + 6);
		for(int z = 0; z < 4; z++) {
			for(int x = 0; x < 13; x++) {
				for(int y = 0; y < 13; y++) {
					Palette.PaletteTile tile = palette.getTile(x, y, z);
					boolean b = false;
					if(x < 2 || x > 10 || y < 2 || y > 10)
						b = true;
					int toWrite = !b && tile != null ? 5 : 0;
					out.put(toWrite);
					if(toWrite == 5) {
						int val = tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1;
						out.putInt(val);
					}
				}
			}
		}
		out.endVarSize();
		return out;
	}
}
