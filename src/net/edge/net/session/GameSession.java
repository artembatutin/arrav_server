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
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.player.Player;

import java.util.AbstractQueue;

/**
 * A {@link Session} implementation that handles networking for a {@link Player} during gameplay.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class GameSession extends Session {
	/**
	 * The cap limit of outgoing packets per session.
	 */
	public static int UPDATE_LIMIT = 200;
	
	/**
	 * The queue of {@link IncomingMsg}s.
	 */
	private final AbstractQueue<IncomingMsg> incoming = new MpscLinkedAtomicQueue<>();
	
	/**
	 * The queue of {@link OutgoingPacket}s.
	 */
	private final AbstractQueue<OutgoingPacket> outgoing = new MpscLinkedAtomicQueue<>();
	
	/**
	 * The player assigned to this {@code GameSession}.
	 */
	private final Player player;
	
	/**
	 * The mac address the connection was received from.
	 */
	private final String macAddress;
	
	/**
	 * The message encryptor.
	 */
	private final IsaacRandom encryptor;
	
	/**
	 * Creates a new {@link GameSession}.
	 * @param channel   The channel for this session.
	 * @param macAddress The mac address.
	 * @param encryptor The message encryptor.
	 * @param decryptor The message decryptor.
	 */
	GameSession(Player player, Channel channel, String macAddress, IsaacRandom encryptor, IsaacRandom decryptor) {
		super(channel);
		this.player = player;
		this.macAddress = macAddress;
		this.encryptor = encryptor;
		getChannel().pipeline().replace("login-decoder", "game-decoder", new GameDecoder(decryptor, this));
	}
	
	@Override
	public void handleUpstreamMessage(Object msg) {
		if(msg instanceof IncomingMsg) {
			IncomingMsg packet = (IncomingMsg) msg;
			if(packet.getOpcode() != 0) {
				if(packet.getOpcode() == 41) {
					handle(packet);//item equipping
					return;
				}
				incoming.offer(packet);
			}
		}
	}
	
	@Override
	public void terminate() {
		System.out.println("Game session terminating " + player);
		if(player.getState() != EntityState.AWAITING_REMOVAL && player.getState() != EntityState.INACTIVE) {
			World.get().queueLogout(player);
		}
	}
	
	@Override
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Enqueues the given {@link OutgoingPacket} for transport.
	 */
	public void enqueue(OutgoingPacket pkt) {
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
	 * Polling all incoming packets.
	 */
	public void pollIncomingPackets() {
		if(!incoming.isEmpty()) {
			int count = 0;
			while(!incoming.isEmpty() && count < 20) {
				IncomingMsg msg = incoming.poll();
				handle(msg);
				count++;
			}
		}
	}
	
	/**
	 * Polling all outgoing packets.
	 */
	public void pollOutgoingPackets() {
		if(!outgoing.isEmpty()) {
			int count = 0;
			while(!outgoing.isEmpty() && count < UPDATE_LIMIT) {
				OutgoingPacket pkt = outgoing.poll();
				if(getChannel().isWritable()) {
					write(pkt);
				} else {
					outgoing.offer(pkt);
				}
				count++;
			}
		}
	}
	
	/**
	 * Writes the given {@link OutgoingPacket} to the stream.
	 */
	public void write(OutgoingPacket packet) {
		if(getChannel().isActive() && getChannel().isRegistered()) {
			ByteBuf temp = packet.write(player, new GameBuffer(Unpooled.buffer(256), encryptor));
			getChannel().write(temp);
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
	}
	
	/**
	 * Getting a {@link ByteBufAllocator} to allocate buffers.
	 * @return allocator.
	 */
	public ByteBufAllocator alloc() {
		return getChannel().alloc();
	}
	
	/**
	 * Gets the player's mac address.
	 * @return mac address of this session.
	 */
	public String getMacAddress() {
		return macAddress;
	}
	
}