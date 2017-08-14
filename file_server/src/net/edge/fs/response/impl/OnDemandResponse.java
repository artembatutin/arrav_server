package net.edge.fs.response.impl;

import net.edge.fs.response.Response;

import io.netty.buffer.ByteBuf;

/**
 * Represents an ondemand response.
 * @author Professor Oak
 */
public class OnDemandResponse extends Response {

	/**
	 * The file type requested.
	 */
	private final int fileType;
	
	/**
	 * The id of the file requested.
	 */
	private final int fileId;
	
	/**
	 * The file size.
	 */
	private final int fileSize;
	
	/**
	 * The chunk id.
	 */
	private final int chunkId;
	
	/**
	 * Creates the 'on-demand' response.
	 * @param buffer The chunk data.
	 * @param fileDescriptor The file descriptor.
	 * @param fileSize The file size.
	 * @param chunkId The chunk id.
	 */
	public OnDemandResponse(ByteBuf buffer, int fileType, int fileId, int fileSize, int chunkId) {
		super(buffer);
		this.fileType = fileType;
		this.fileId = fileId;
		this.fileSize = fileSize;
		this.chunkId = chunkId;
	}
	
	/**
	 * Gets the file type.
	 * @return	The file type.
	 */
	public int getFileType() {
		return fileType;
	}

	/**
	 * Gets the file id.
	 * @return	The file id.
	 */
	public int getFileId() {
		return fileId;
	}
	
	/**
	 * Gets the file size.
	 * @return The file size.
	 */
	public int getFileSize() {
		return fileSize;
	}
	
	/**
	 * Gets the chunk id.
	 * @return The chunk id.
	 */
	public int getChunkId() {
		return chunkId;
	}	
}
