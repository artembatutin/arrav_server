package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.net.NetworkConstants;
import com.rageps.util.json.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all of the sizes of incoming
 * messages.
 * @author lare96 <http://github.com/lare96>
 */
public final class PacketSizeLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link PacketSizeLoader}.
	 */
	public PacketSizeLoader() {
		super("./data/def/packet/packet_sizes.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int opcode = reader.get("opcode").getAsInt();
		int size = reader.get("size").getAsInt();
		NetworkConstants.MESSAGE_SIZES[opcode] = size;
	}
}
