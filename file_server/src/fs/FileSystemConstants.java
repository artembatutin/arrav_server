package fs;

/**
 * Holds file system related constants.
 * @author Graham
 */
final class FileSystemConstants {
	
	/**
	 * The size of an index.
	 */
	static final int INDEX_SIZE = 6;
	
	/**
	 * The size of a header.
	 */
	static final int HEADER_SIZE = 8;
	
	/**
	 * The size of a chunk.
	 */
	static final int CHUNK_SIZE = 512;
	
	/**
	 * The size of a block.
	 */
	static final int BLOCK_SIZE = HEADER_SIZE + CHUNK_SIZE;

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private FileSystemConstants() {
		
	}

}
