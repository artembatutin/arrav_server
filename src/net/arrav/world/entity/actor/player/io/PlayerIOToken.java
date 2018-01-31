package net.arrav.world.entity.actor.player.io;

import net.arrav.net.codec.login.LoginCode;
import net.arrav.util.ByteBufferUtil;
import net.arrav.world.entity.actor.player.Player;

import java.io.ByteArrayOutputStream;
import java.nio.MappedByteBuffer;

public abstract class PlayerIOToken {

	public abstract void encode(Player p, MappedByteBuffer buf);

	public abstract void decode(Player p, MappedByteBuffer buf);

	public abstract int offset();

	public LoginCode check(Player p, MappedByteBuffer buf) {
		decode(p, buf);
		return LoginCode.NORMAL;
	}

	protected String getString(MappedByteBuffer buf) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		for(; ; ) {
			int read = buf.get();
			if(read == ByteBufferUtil.J_STRING_TERMINATOR) {
				break;
			}
			os.write(read);
		}
		return new String(os.toByteArray());
	}

	protected void putString(MappedByteBuffer buf, String text, int length) {
		int count = 0;
		for(char c : text.toCharArray()) {
			if(count == length)
				throw new IndexOutOfBoundsException();
			buf.put((byte) c);
			count++;
		}
		buf.put((byte) ByteBufferUtil.J_STRING_TERMINATOR);
		//for(; count < length; count++) {
		//	buf.put((byte) ByteBufferUtil.J_STRING_TERMINATOR);
		//}
	}

	protected boolean getBoolean(MappedByteBuffer buf) {
		return buf.get() == 1;
	}

	protected void putBoolean(MappedByteBuffer buf, boolean flag) {
		buf.put((byte) (flag ? 1 : 0));
	}

}
