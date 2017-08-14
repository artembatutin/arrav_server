package net.edge.fs.util;

import static com.google.common.io.ByteStreams.toByteArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;

import org.apache.tools.bzip2.CBZip2InputStream;
import net.edge.fs.cache.CacheLoader;

import io.netty.buffer.ByteBuf;

public class Misc {

	/**
	 * Hashes a {@code String} using Jagex's algorithm, this method should be
	 * used to convert actual names to hashed names to lookup files within the
	 * {@link CacheLoader}.
	 * @param string The string to hash.
	 * @return The hashed string.
	 */
	public static int hash(String string) {
		return _hash(string.toUpperCase());
	}
	
	/**
	 * Hashes a {@code String} using Jagex's algorithm, this method should be
	 * used to convert actual names to hashed names to lookup files within the
	 * {@link CacheLoader}.
	 * <p>
	 * <p>
	 * This method should <i>only</i> be used internally, it is marked
	 * deprecated as it does not properly hash the specified {@code String}. The
	 * functionality of this method is used to create a proper {@code String}
	 * {@link #hash(String) <i>hashing method</i>}. The scope of this method has
	 * been marked as {@code private} to prevent confusion.
	 * </p>
	 * @param string The string to hash.
	 * @return The hashed string.
	 * @deprecated This method should only be used internally as it does not
	 * correctly hash the specified {@code String}. See the note
	 * below for more information.
	 */
	@Deprecated
	private static int _hash(String string) {
		return IntStream.range(0, string.length()).reduce(0, (hash, index) -> hash * 61 + string.charAt(index) - 32);
	}
	
	
	/**
	 * Gets a 24-bit medium integer from the specified {@link ByteBuf}, this method does not mark the buffer's
	 * current position.
	 * @param buffer The ByteBuffer to read from.
	 * @return The read 24-bit medium integer.
	 */
	public static int getMedium(ByteBuf buffer) {
		return (buffer.readShort() & 0xFFFF) << 8 | buffer.readByte() & 0xFF;
	}
	
	/**
	 * Gets a 24-bit medium integer from the specified {@link ByteBuffer}, this method does not mark the buffer's
	 * current position.
	 * @param buffer The ByteBuffer to read from.
	 * @return The read 24-bit medium integer.
	 */
	public static int getMedium(ByteBuffer buffer) {
		return (buffer.getShort() & 0xFFFF) << 8 | buffer.get() & 0xFF;
	}
	
	/**
	 * Uncompresses a {@code byte} array of g-zipped data.
	 * @param data The compressed, g-zipped data.
	 * @return The uncompressed data.
	 * @throws IOException If some I/O exception occurs.
	 */
	public static byte[] gunzip(byte[] data) throws IOException {
		return toByteArray(new GZIPInputStream(new ByteArrayInputStream(data)));
	}

	/**
	 * Uncompresses a {@code byte} array of b-zipped data that does not contain
	 * a header.
	 * <p>
	 * <p>
	 * A b-zip header block consists of <tt>2</tt> {@code byte}s, they are
	 * replaced with 'h' and '1' as that is what our {@link CacheLoader file
	 * system} compresses the header as.
	 * </p>
	 * @param data   The compressed, b-zipped data.
	 * @param offset The offset position of the data.
	 * @param length The length of the data.
	 * @return The uncompressed data.
	 * @throws IOException If some I/O exception occurs.
	 */
	public static byte[] unbzip2Headerless(byte[] data, int offset, int length) throws IOException {
	    /* Strip the header from the data. */
		byte[] bzip2 = new byte[length + 2];
		bzip2[0] = 'h';
		bzip2[1] = '1';
		System.arraycopy(data, offset, bzip2, 2, length);

		/* Uncompress the headerless data */
		return unbzip2(bzip2);
	}

	/**
	 * Uncompresses a {@code byte} array of b-zipped data.
	 * @param data The compressed, b-zipped data.
	 * @return The uncompressed data.
	 * @throws IOException If some I/O exception occurs.
	 */
	public static byte[] unbzip2(byte[] data) throws IOException {
		return toByteArray(new CBZip2InputStream(new ByteArrayInputStream(data)));
	}
}
