package net.edge.cache.archive;

import com.google.common.base.Preconditions;
import net.edge.cache.Cache;
import net.edge.util.ByteBufferUtil;
import net.edge.util.CompressionUtil;
import net.edge.util.TextUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static net.edge.cache.Cache.INDEX_SIZE;

/**
 * Represents an archive within the {@link Cache}.
 * <p>
 * An archive contains varied amounts of {@link ArchiveSector}s which contain
 * compressed file system data.
 * </p>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class Archive {
	
	/**
	 * A {@link Map} of {@link ArchiveSector} hashes to {@link ArchiveSector}s.
	 */
	private final Map<Integer, ArchiveSector> sectors;
	
	/**
	 * Constructs a new {@link Archive} with the specified {@link Map} of
	 * {@link ArchiveSector}s.
	 * @param sectors The {@link Map} of sectors within this archive.
	 */
	private Archive(Map<Integer, ArchiveSector> sectors) {
		this.sectors = sectors;
	}
	
	/**
	 * Decodes the data within this {@link Archive}.
	 * @param data The encoded data within this archive.
	 * @return Returns an {@link Archive} with the decoded data, never
	 * {@code null}.
	 * @throws IOException If some I/O exception occurs.
	 */
	public static Archive decode(ByteBuffer data) throws IOException {
		int length = ByteBufferUtil.getMedium(data);
		int compressedLength = ByteBufferUtil.getMedium(data);
		
		byte[] uncompressedData = data.array();
		
		if(compressedLength != length) {
			uncompressedData = CompressionUtil.unbzip2Headerless(data.array(), INDEX_SIZE, compressedLength);
			data = ByteBuffer.wrap(uncompressedData);
		}
		
		int total = data.getShort() & 0xFF;
		int offset = data.position() + total * 10;
		
		Map<Integer, ArchiveSector> sectors = new HashMap<>(total);
		for(int i = 0; i < total; i++) {
			int hash = data.getInt();
			length = ByteBufferUtil.getMedium(data);
			compressedLength = ByteBufferUtil.getMedium(data);
			
			byte[] sectorData = new byte[length];
			
			if(length != compressedLength) {
				sectorData = CompressionUtil.unbzip2Headerless(uncompressedData, offset, compressedLength);
			} else {
				System.arraycopy(uncompressedData, offset, sectorData, 0, length);
			}
			
			sectors.put(hash, new ArchiveSector(ByteBuffer.wrap(sectorData), hash));
			offset += compressedLength;
		}
		
		return new Archive(sectors);
	}
	
	/**
	 * Retrieves an {@link Optional<ArchiveSector>} for the specified hash.
	 * @param hash The archive sectors hash.
	 * @return The optional container.
	 */
	private Optional<ArchiveSector> getSector(int hash) {
		return Optional.ofNullable(sectors.get(hash));
	}
	
	/**
	 * Retrieves an {@link Optional<ArchiveSector>} for the specified name.
	 * @param name The archive sectors name.
	 * @return The optional container.
	 */
	private Optional<ArchiveSector> getSector(String name) {
		int hash = TextUtils.hash(name);
		return getSector(hash);
	}
	
	/**
	 * Returns the data within the {@link ArchiveSector} for the specified
	 * {@code String} name.
	 * @param name The name of the {@link ArchiveSector}.
	 * @return The data within the {@link ArchiveSector} or nothing, this method
	 * fails-fast if no {@link ArchiveSector} exists for the specified
	 * {@code name}.
	 */
	public ByteBuffer getData(String name) {
		Optional<ArchiveSector> optionalData = getSector(name);
		Preconditions.checkArgument(optionalData.isPresent());
		ArchiveSector dataSector = optionalData.get();
		return dataSector.getData();
	}
	
}