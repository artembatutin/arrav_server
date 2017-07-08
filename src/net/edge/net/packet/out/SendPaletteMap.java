package net.edge.net.packet.out;

import net.edge.content.skill.construction.Palette;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendPaletteMap implements OutgoingPacket {
	
	private final Palette palette;
	
	public SendPaletteMap(Palette palette) {
		this.palette = palette;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(241, MessageType.VARIABLE_SHORT);
		msg.putShort(player.getPosition().getRegionX() + 6, ByteTransform.A);
		msg.putShort(player.getPosition().getRegionY() + 6);
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 13; x++) {
				for (int y = 0; y < 13; y++) {
					Palette.PaletteTile tile = palette.getTile(x, y, z);
					boolean b = false;
					if (x < 2 || x > 10 || y < 2 || y > 10)
						b = true;
					int toWrite = !b && tile != null ? 5 : 0;
					msg.put(toWrite);
					if(toWrite == 5) {
						int val = tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1;
						msg.putInt(val);
					}
				}
			}
		}
		msg.endVarSize();
	}
}
