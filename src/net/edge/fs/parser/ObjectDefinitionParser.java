package net.edge.fs.parser;

import net.edge.fs.Archive;
import net.edge.fs.FileSystem;
import net.edge.utils.ByteBufferUtil;
import net.edge.utils.LoggerUtils;
import net.edge.world.model.node.object.ObjectDefinition;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * A class which parses object definitions.
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Steve, initial contribution
 */
public final class ObjectDefinitionParser {
	
	/**
	 * The logger that will print important information.
	 */
	private static Logger logger = LoggerUtils.getLogger(ObjectDefinitionParser.class);
	
	/**
	 * Reads object information from the file system and parses all of the
	 * definitions.
	 * @param fileSystem The file system.
	 * @throws IOException If some I/O exception occurs.
	 */
	public static void parse(FileSystem fileSystem) throws IOException {
		logger.info("Loading object definitions.");
		
		Archive archive = fileSystem.getArchive(FileSystem.CONFIG_ARCHIVE);
		ByteBuffer buf = archive.getData("loc.dat");
		ByteBuffer idx = archive.getData("loc.idx");
		
		int count = idx.getInt();
		
		int pos = 4;
		int loaded = 0;
		for(int i = 0; i < count; i++) {
			buf.position(pos);
			parseDefinition(i, buf);
			pos += idx.getShort() & 0xFFFF;
			loaded += 1;
		}
		
		logger.info("Loaded " + loaded + " object definitions.");
	}
	
	/**
	 * Parses a single game object definition by reading object info from a
	 * buffer.
	 * @param id     The id of the object.
	 * @param buffer The buffer.
	 */
	private static void parseDefinition(int id, ByteBuffer buffer) {
		String name = "";
		String description = "";
		int i = -1;
		int sizeX = 1;
		int sizeY = 1;
		String[] actions = new String[10];
		int[] modelIds = null;
		int[] modelTypes = null;
		boolean hasActions = false;
		boolean solid = true;
		boolean walkable = true;
		boolean wall = false;
		boolean decoration = false;
		boolean unwalkableSolid = false;
		boolean pass = true;
		while(true) {
			if(buffer.position() >= buffer.array().length) {
				break;
			}
			int code = buffer.get() & 0xFF;
			
			if(code == 0) {
				pass = false;
				break;
			} else if(code == 1) {
				final int amount = buffer.get() & 0xFF;
				if(amount > 0 && modelIds == null) {
					modelIds = new int[amount];
					modelTypes = new int[amount];
					for(int l = 0; l < amount; l++) {
						modelIds[l] = buffer.getShort() & 0xFFFF;
						modelTypes[l] = buffer.get() & 0xFF;
					}
				}
			} else if(code == 2) {
				name = ByteBufferUtil.getJString(buffer);
			} else if(code == 3) {
				description = ByteBufferUtil.getJString(buffer);
			} else if(code == 4) {
				final int amount = buffer.get() & 0xFF;
				if(amount > 0 && modelIds == null) {
					modelIds = new int[amount];
					for(int l = 0; l < amount; l++) {
						modelIds[l] = (buffer.getShort() & 0xFFFF);
					}
				}
			} else if(code == 5) {
				sizeX = buffer.get() & 0xFF;
			} else if(code == 6) {
				sizeY = buffer.get() & 0xFF;
			} else if(code == 7) {
				solid = false;
			} else if(code == 8) {
				walkable = false;
			} else if(code == 9) {
				i = buffer.get() & 0xFF;
				if(i == 1)
					hasActions = true;
			} else if(code == 12) {
				wall = true;
			} else if(code == 13) {
				buffer.getShort();
			} else if(code == 14) {
				buffer.get();
			} else if(code == 15) {
				buffer.get();
			} else if(code == 16) {
				buffer.get();
			} else if(code >= 40 && code < 50) {
				final String action = ByteBufferUtil.getJString(buffer);
				if(!action.equalsIgnoreCase("hidden")) {
					actions[code - 40] = action;
				}
			} else if(code == 17) {
				final int amount = buffer.get() & 0xFF;
				for(int l = 0; l < amount; l++) {
					buffer.getShort();
					buffer.getShort();
				}
			} else if(code == 18) {
				final int amount = buffer.get() & 0xFF;
				for(int l = 0; l < amount; l++) {
					buffer.getShort();
					buffer.getShort();
				}
			} else if(code == 19) {
				buffer.getShort();
			} else if(code == 22) {
				buffer.getShort();
			} else if(code == 23) {
				buffer.getShort();
			} else if(code == 24) {
				buffer.getShort();
			} else if(code == 25) {
				buffer.getShort();
			} else if(code == 26) {
				buffer.get();
			} else if(code == 27) {
				buffer.getShort();
			} else if(code == 28) {
				buffer.getShort();
			} else if(code == 29) {
				buffer.getShort();
			} else if(code == 30) {
				decoration = true;
			} else if(code == 31) {
				unwalkableSolid = true;
			} else if(code == 32) {
				buffer.get();
			} else if(code == 33) {
				buffer.getShort();
				buffer.getShort();
				int j = buffer.get() & 0xFF;
				for(int l = 0; l < j; l++) {
					buffer.getInt();
				}
			} else if(code != 10 && code != 11 && code != 20 && code != 21 && code != 30 && code != 31) {
				System.out.println("[ObjectDef " + id + "] Unknown opcode: " + code);
				break;
			}
		}
		if(pass) {
			if(i == -1) {
				hasActions = modelIds != null && (modelTypes == null || modelTypes[0] == 10);
				if(actions != null)
					hasActions = true;
			}
			
			if(unwalkableSolid) {
				solid = false;
				walkable = false;
			}
		}
		
		if(id == 85532)//swing pole
			sizeX = 6;
		if(id == 85584)//gnome billboard
			sizeY = 5;
		
		ObjectDefinition.DEFINITIONS[id] = new ObjectDefinition(id, name, description, sizeX, sizeY, actions, hasActions, solid, walkable, wall, decoration);
		
	}
	
}
