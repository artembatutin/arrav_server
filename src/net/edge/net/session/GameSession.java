package net.edge.net.session;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscLinkedAtomicQueue;
import net.edge.net.NetworkConstants;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.crypto.IsaacRandom;
import net.edge.net.codec.game.GameDecoder;
import net.edge.net.codec.game.GameEncoder;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.player.Player;

import java.util.Queue;

/**
 * A {@link Session} implementation that handles networking for a {@link Player} during gameplay.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class GameSession extends Session {
	
	/**
	 * The cap limit of outgoing packets per session.
	 */
	public static int outLimit = 200;
	
	/**
	 * The queue of {@link OutgoingPacket}s.
	 */
	private final Queue<OutgoingPacket> outgoing = new MpscLinkedAtomicQueue<>();
	
	/**
	 * The player assigned to this {@code GameSession}.
	 */
	private final Player player;
	
	/**
	 * The message encryptor.
	 */
	private final IsaacRandom encryptor;
	
	/**
	 * The amount of messages received in a tick from this session.
	 */
	private int handledMessages;

	/**
	 * Creates a new {@link GameSession}.
	 * @param channel   The channel for this session.
	 * @param encryptor The message encryptor.
	 * @param decryptor The message decryptor.
	 */
	GameSession(Player player, Channel channel, IsaacRandom encryptor, IsaacRandom decryptor) {
		super(channel);
		this.player = player;
		this.encryptor = encryptor;
		getChannel().pipeline().replace("login-encoder", "game-encoder", new GameEncoder(encryptor, player));
		getChannel().pipeline().replace("login-decoder", "game-decoder", new GameDecoder(decryptor, this));
	}
	
	@Override
	public void handleUpstreamMessage(Object msg) {
		if(handledMessages >= 20) {
			//packet flooding
			return;
		}
		if(msg instanceof IncomingMsg) {
			IncomingMsg packet = (IncomingMsg) msg;
			if(packet.getOpcode() != 0) {
				handledMessages++;
				if(packet.getOpcode() == 41) {
					handle(packet);//item equipping
					return;
				}
				World.get().run(() -> handle(packet));
			}
		}
	}
	
	@Override
	public void terminate() {
		if(!isTerminating()) {
			System.out.println("Game session terminating " + player);
			if(player.getState() == EntityState.ACTIVE) {
				World.get().queueLogout(player);
			}
			outgoing.clear();
			setTerminating(true);
		}
	}
	
	@Override
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Enqueues the given {@link OutgoingPacket} for transport.
	 */
	public void equeue(OutgoingPacket pkt) {
		outgoing.offer(pkt);
	}
	
	/**
	 * Handling an incoming packet/message.
	 * @param packet incoming message.
	 */
	public void handle(IncomingMsg packet) {
		try {
			NetworkConstants.MESSAGES[packet.getOpcode()].handle(player, packet.getOpcode(), packet.getSize(), packet);
		} finally {
			packet.getBuffer().release();
		}
	}
	
	/**
	 * Writes the given {@link OutgoingPacket} to the stream.
	 */
	public void write(OutgoingPacket packet) {
		ByteBuf temp = packet.write(player, new GameBuffer(Unpooled.buffer(256), encryptor));
		getChannel().write(temp);
	}
	
	/**
	 * Writes all enqueued packets to the stream.
	 */
	public void pollOutgoingMessages() {
		int written = 0;
		Channel channel = getChannel();
		if (!outgoing.isEmpty()) {
			if (channel.isActive() && channel.isOpen()) {
				while (channel.isWritable() && written < outLimit) {
					OutgoingPacket packet = outgoing.poll();
					if(packet == null) {
						break;
					}
					getChannel().write(packet, getChannel().voidPromise());
					written++;
				}
			}
		}
	}
	
	/**
	 * Flushes all pending {@link IncomingMsg}s within the channel's queue. Repeated calls to this method are relatively
	 * expensive, which is why messages should be queued up with {@code queue(MessageWriter)} and flushed once at the end of
	 * the cycle.
	 */
	public void flushQueue() {
		if(isActive())
			getChannel().flush();
		handledMessages = 0;
	}
	
	/**
	 * Getting a {@link ByteBufAllocator} to allocate buffers.
	 * @return allocator.
	 */
	public ByteBufAllocator alloc() {
		return getChannel().alloc();
	}

}