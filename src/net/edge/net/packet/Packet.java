package net.edge.net.packet;

import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.MessageType;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents a single packet used in the in-game protocol.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class Packet {
	
	/**
	 * The opcode of this message.
	 */
	private final int opcode;
	
	/**
	 * The size of this message.
	 */
	private final int size;
	
	/**
	 * The type of this message.
	 */
	private final MessageType type;
	
	/**
	 * The payload of this message.
	 */
	private final IncomingMsg payload;
	
	/**
	 * Creates a new {@link Packet}.
	 * @param opcode  The opcode of this message.
	 * @param type    The type of this message.
	 * @param payload The payload of this message.
	 */
	public Packet(int opcode, MessageType type, IncomingMsg payload) {
		this.opcode = opcode;
		this.type = type;
		this.payload = payload;
		size = payload.getBuffer().readableBytes();
	}
	
	/**
	 * @return The opcode of this message.
	 */
	public int getOpcode() {
		return opcode;
	}
	
	/**
	 * @return The size of this message.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * @return The type of this message.
	 */
	public MessageType getType() {
		return type;
	}
	
	/**
	 * @return The payload of this message.
	 */
	public IncomingMsg getPayload() {
		return payload;
	}
}