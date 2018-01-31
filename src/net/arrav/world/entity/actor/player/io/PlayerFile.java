package net.arrav.world.entity.actor.player.io;

import net.arrav.net.codec.login.LoginCode;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.io.impl.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class PlayerFile {

	/**
	 * All player IO tokens.
	 */
	private static final PlayerIOToken[] TOKENS = {
			new UsernameToken(),
			new PasswordToken(),
			new BannedToken(),
			new MutedToken(),
			new PositionToken(),
			new RightsToken(),
			new LockedXPToken(),
			new IronmanToken(),
			new DonatedToken(),
			new VotedToken(),
			new VotePointsToken(),
			new AppearanceToken(),
	};

	/**
	 * Path to player saved files.
	 */
	public static final String PATH = "./data/players";

	/**
	 * Length of the file.
	 */
	public static final int LENGTH;

	//Calculating length of tokens.
	static {
		int l = 0;
		for(PlayerIOToken t : TOKENS)
			l += t.offset();
		LENGTH = l;
	}

	/**
	 * The player associated to this file.
	 */
	private final Player player;

	/**
	 * The mapped buffer.
	 */
	private MappedByteBuffer buf;

	public PlayerFile(Player player) {
		this.player = player;
	}

	public void init() throws IOException {
		RandomAccessFile file = new RandomAccessFile(PATH + player.credentials.username + ".save", "rw");
		buf = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0 , 10);
	}

	public LoginCode decode() {
		int pos = 0;
		LoginCode code = LoginCode.NORMAL;
		for(PlayerIOToken t : TOKENS) {
			buf.position(pos);
			code = t.check(player, buf);
			if(code != LoginCode.NORMAL) {
				return code;
			}
			pos += t.offset();
		}
		return code;
	}

	public void encode() {
		int pos = 0;
		for(PlayerIOToken t : TOKENS) {
			buf.position(pos);
			t.encode(player, buf);
			pos += t.offset();
		}
	}

	public boolean isLoaded() {
		return buf.isLoaded();
	}

}
