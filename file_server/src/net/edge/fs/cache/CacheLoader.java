package net.edge.fs.cache;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.CRC32;

import net.edge.fs.FileServerConstants;
import net.edge.fs.cache.impl.Cache;
import net.edge.fs.cache.impl.CacheArchive;
import net.edge.fs.cache.impl.CacheConstants;
import net.edge.fs.util.Misc;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Represents a file system of {@link Cache}s and {@link CacheArchive}s.
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 * @editor Professor Oak
 */
public class CacheLoader {

	/**
	 * All of the {@link Cache}s within this {@link CacheLoader}.
	 */
	public Cache[] caches;

	/**
	 * All of the {@link CacheArchive}s within this {@link CacheLoader}.
	 */
	public CacheArchive[] archives;

	/**
	 * The cached archive hashes.
	 */
	public ByteBuf crcTable;

	/**
	 * Initializes the {@link CacheLoader} and loads our cache.
	 * @throws Exception 		If an error occured whilst initializing.
	 */
	public void init() throws Exception {
		Path root = Paths.get(FileServerConstants.CACHE_BASE_DIR);
		Preconditions.checkArgument(Files.isDirectory(root), "Supplied path must be a directory!");

		Path data = root.resolve(CacheConstants.DATA_PREFIX);
		Preconditions.checkArgument(Files.exists(data), "No main cache data file found in the specified path!");

		//Load cache files...
		SeekableByteChannel dataChannel = Files.newByteChannel(data, READ, WRITE);
		caches = new Cache[CacheConstants.MAXIMUM_INDICES];
		archives = new CacheArchive[CacheConstants.MAXIMUM_ARCHIVES];
		for(int index = 0; index < caches.length; index++) {
			Path path = root.resolve(CacheConstants.INDEX_PREFIX + index);
			if(Files.exists(path)) {
				SeekableByteChannel indexChannel = Files.newByteChannel(path, READ, WRITE);
				caches[index] = new Cache(dataChannel, indexChannel, index);
			}
		}

		//Load the archives from the cache files...
		// We don't use index 0
		for(int id = 1; id < archives.length; id++) {
			Cache cache = Objects.requireNonNull(caches[CacheConstants.CONFIG_INDEX], "Configuration cache is null - unable to decode archives");
			archives[id] = CacheArchive.decode(cache.get(id));
		}
	}

	/**
	 * Attempts to return the file with the {@code path} requested.
	 * @param path		The path of the file requested.
	 * @return			The file requested.
	 */
	public ByteBuf request(String path) {
		try {
			if (path.startsWith("crc")) {
				return getCrcTable();
			} else if (path.startsWith("title")) {
				return getFile(0, 1);
			} else if (path.startsWith("config")) {
				return getFile(0, 2);
			} else if (path.startsWith("interface")) {
				return getFile(0, 3);
			} else if (path.startsWith("media")) {
				return getFile(0, 4);
			} else if (path.startsWith("versionlist")) {
				return getFile(0, 5);
			} else if (path.startsWith("textures")) {
				return getFile(0, 6);
			} else if (path.startsWith("wordenc")) {
				return getFile(0, 7);
			} else if (path.startsWith("sounds")) {
				return getFile(0, 8);
			}

		} catch(IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Gets an {@link CacheArchive} for the specified {@code id}, this method
	 * fails-fast if no archive can be found.
	 * @param id The id of the {@link CacheArchive} to fetch.
	 * @return The {@link CacheArchive} for the specified {@code id}.
	 * @throws NullPointerException If the archive cannot be found.
	 */
	public CacheArchive getArchive(int id) {
		Preconditions.checkElementIndex(id, archives.length);
		return Objects.requireNonNull(archives[id]);
	}

	/**
	 * Gets a {@link Cache} for the specified {@code id}, this method fails-fast
	 * if no cache can be found.
	 * @param id The id of the {@link Cache} to fetch.
	 * @return The {@link Cache} for the specified {@code id}.
	 * @throws NullPointerException If the cache cannot be found.
	 */
	public Cache getCache(int id) {
		Preconditions.checkElementIndex(id, caches.length);
		return Objects.requireNonNull(caches[id]);
	}

	/**
	 * Returns a {@link ByteBuffer} of file data for the specified index within
	 * the specified {@link Cache}.
	 * @param cacheId The id of the cache.
	 * @param indexId The id of the index within the cache.
	 * @return A {@link ByteBuffer} of file data for the specified index.
	 * @throws IOException If some I/O exception occurs.
	 */
	public ByteBuf getFile(int cacheId, int indexId) throws IOException {
		Cache cache = getCache(cacheId);
		return cache.get(indexId);
	}

	public byte[] getDecompressedFile(int cacheId, int indexId) throws IOException {
		ByteBuf compressed = getFile(cacheId, indexId);
		ByteBuf decompressed = Unpooled.wrappedBuffer(Misc.gunzip(compressed.array()));
		return decompressed.array();
	}

	/**
	 * Gets a byte array containing a file's data.
	 * @param file 		The file to get.
	 * @return 			A byte array which contains the contents of the file.
	 */
	public byte[] getFile(File file) {
		try  {
			int i = (int)file.length();
			byte data[] = new byte[i];
			DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			datainputstream.readFully(data, 0, i);
			datainputstream.close();
			return data;			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the cached {@link #crcTable} if they exist, otherwise they
	 * are calculated and cached for future use.
	 * @return The hashes of each {@link CacheArchive}.
	 * @throws IOException If some I/O exception occurs.
	 */
	public ByteBuf getCrcTable() throws IOException {
		if(crcTable != null) {
			return crcTable.slice();
		}

		int[] crcs = new int[CacheConstants.MAXIMUM_ARCHIVES];
		CRC32 crc32 = new CRC32();
		for(int file = 1; file < crcs.length; file++) {
			ByteBuf buffer = getFile(CacheConstants.CONFIG_INDEX, file);
			crc32.reset();
			byte[] bytes = new byte[buffer.readableBytes()];
			buffer.readBytes(bytes, 0, bytes.length);
			crc32.update(bytes, 0, bytes.length);
			crcs[file] = (int) crc32.getValue();
		}

		ByteBuf buffer = Unpooled.buffer(crcs.length * Integer.BYTES + 4);
		int hash = 1234;
		for(int crc : crcs) {
			hash = (hash << 1) + crc;
			buffer.writeInt(crc);
		}

		buffer.writeInt(hash);
		crcTable = buffer.asReadOnly();
		return crcTable.slice();
	}
}