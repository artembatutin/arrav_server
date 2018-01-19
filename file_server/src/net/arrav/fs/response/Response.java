package net.arrav.fs.response;

import io.netty.buffer.ByteBuf;

/**
 * Represents some type of response which will be sent to the
 * client.
 * @author Professor Oak
 */
public abstract class Response {

	/**
	 * Our buffer.
	 * Containing data which will be sent to the client.
	 */
	private final ByteBuf buffer;
	
	/**
	 * Constructs this response.
	 * @param buffer	This response's buffer.
	 */
	public Response(ByteBuf buffer) {
		this.buffer = buffer;
	}

	/**
	 * Gets this response's buffer.	
	 * @return	The buffer.
	 */
	public ByteBuf getBuffer() {
		return buffer;
	}
}
