package net.arrav.fs.response.impl;

import net.arrav.fs.response.Response;

import io.netty.buffer.ByteBuf;

/**
 * Represents a Jaggrab response.
 * @author Professor Oak
 */
public class JagGrabResponse extends Response {

	/**
	 * Creates the 'JagGrab' response.
	 * @param buffer The file data.
	 */
	public JagGrabResponse(ByteBuf buffer) {
		super(buffer);
	}
}
