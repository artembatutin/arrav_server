package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.content.skill.construction.Palette;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendPaletteMap implements OutgoingPacket {
	
	private final Palette palette;
	
	public SendPaletteMap(Palette palette) {
		this.palette = palette;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(241, GamePacketType.VARIABLE_SHORT);
		buf.putShort(player.getPosition().getRegionX() + 6, ByteTransform.A);
		buf.putShort(player.getPosition().getRegionY() + 6);
		for(int z = 0; z < 4; z++) {
			for(int x = 0; x < 13; x++) {
				for(int y = 0; y < 13; y++) {
					Palette.PaletteTile tile = palette.getTile(x, y, z);
					boolean b = false;
					if(x < 2 || x > 10 || y < 2 || y > 10)
						b = true;
					int toWrite = !b && tile != null ? 5 : 0;
					buf.put(toWrite);
					if(toWrite == 5) {
						int val = tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1;
						buf.putInt(val);
					}
				}
			}
		}
		buf.endVarSize();
		return buf;
	}
}
