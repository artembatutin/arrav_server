package net.edge.cache;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

/**
 * Represents a sector and index cache.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class Cache {

	/**
	 * Represents the size of a index file.
	 * Calculating the total size of a index file. the total size may be that of a {@code long}.
	 */
	public static final int INDEX_SIZE = 6;

	/**
	 * Represents the size of a sector header.
	 * Calculating the total size of the sector header. the total size may be that of a {@code long}.
	 */
	public static final int SECTOR_HEADER_SIZE = 8;

	/**
	 * Represents the size of a sector header.
	 * Calculating the total size of the sector header. the total size may be that of a {@code long}
	 */
	private static final int SECTOR_SIZE = 520;

	/**
	 * Byte buffer array.
	 * <p>
	 * This byte buffer is used to read index and sector data from their
	 * respective byte channels.
	 * </p>
	 */
	private static final byte[] buffer = new byte[SECTOR_SIZE];

	/**
	 * A byte channel that contains a series of variable-length bytes which
	 * represent a sector.
	 */
	private final MappedByteBuffer dataChannel;

	/**
	 * A byte channel that contains a series of variable-length bytes which
	 * represent a index.
	 */
	private final MappedByteBuffer indexChannel;

	/**
	 * Represents the id of this {@link Cache}.
	 */
	private final int index;

	/**
	 * Constructs a new {@link Cache} with the specified sector and index
	 * channels and id.
	 * @param dataChannel The cache sectors byte channel.
	 * @param indexChannel  The cache sectors index channel.
	 * @param index            This caches index.
	 */
	Cache(MappedByteBuffer dataChannel, MappedByteBuffer indexChannel, int index) {
		this.dataChannel = dataChannel;
		this.indexChannel = indexChannel;
		this.index = ++index;
	}
	
	/**
	 * Gives out a byte array of the requested file.
	 * @param indexId index id to grab.
	 * @return buffer byte array.
	 */
	public ByteBuffer get(int indexId) {
		indexChannel.position(indexId * INDEX_SIZE);
		int n;
		for(int i = 0; i < INDEX_SIZE; i += n) {
			try {
				indexChannel.get(buffer, i, INDEX_SIZE - i);
				n = INDEX_SIZE - i;
			} catch(Exception e) {
				return null;
			}
		}
		int fileSize = ((buffer[0] & 0xff) << 16) + ((buffer[1] & 0xff) << 8) + (buffer[2] & 0xff);
		int fileBlock = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
		
		byte[] fileBuffer = new byte[fileSize];
		int read = 0;
		for(int i = 0; read < fileSize; i++) {
			if(fileBlock == 0) {
				return null;
			}
			dataChannel.position(fileBlock * SECTOR_SIZE);
			int size = 0;
			int remaining = fileSize - read;
			if(remaining > 512) {
				remaining = 512;
			}
			int nbytes;
			for(; size < remaining + SECTOR_HEADER_SIZE; size += nbytes) {
				try {
					dataChannel.get(buffer, size, remaining + SECTOR_HEADER_SIZE - size);
					nbytes = remaining + SECTOR_HEADER_SIZE - size;
				} catch(Exception e) {
					return null;
				}
			}
			int nextFileId = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
			int currentPartId = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
			int nextBlockId = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
			int nextStoreId = buffer[7] & 0xff;
			if(nextFileId != indexId || currentPartId != i || nextStoreId != index) {
				return null;
			}
			if(nextBlockId < 0 || nextBlockId > dataChannel.capacity() / 520L) {
				return null;
			}
			for(int k3 = 0; k3 < remaining; k3++) {
				fileBuffer[read++] = buffer[k3 + 8];
			}
			fileBlock = nextBlockId;
		}
		return ByteBuffer.wrap(fileBuffer);
	}

}