//package com.rageps.util.json.impl;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.rageps.net.NetworkConstants;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.util.json.JsonLoader;
//
//import java.util.Arrays;
//import java.util.Objects;
//
///**
// * The {@link JsonLoader} implementation that loads all incoming messages.
// * @author lare96 <http://github.com/lare96>
// */
//public final class PacketOpcodeLoader extends JsonLoader {
//
//	/**
//	 * Creates a new {@link PacketOpcodeLoader}.
//	 */
//	public PacketOpcodeLoader() {
//		super("data/def/packet/packet_opcodes.json");
//	}
//
//	@Override
//	public void load(JsonObject reader, Gson builder) {
//		int[] opcodes = builder.fromJson(reader.get("opcodes").getAsJsonArray(), int[].class);
//		String name = Objects.requireNonNull(reader.get("class").getAsString());
//		boolean invalid = Arrays.stream(opcodes).anyMatch(op -> op < 0 || op > NetworkConstants.MESSAGES.length);
//		if(invalid)
//			throw new IllegalStateException("Invalid message opcode!");
//		execute(opcodes, name);
//	}
//
//	/**
//	 * Executes the loading of the message within {@code name} for
//	 * {@code opcodes}.
//	 * @param opcodes the opcodes of the message.
//	 * @param name the name and path to the class.
//	 * @throws IllegalStateException if the class isn't implementing {@link IncomingPacket}.
//	 */
//	private static void execute(int[] opcodes, String name) {
//		try {
//			Class<?> c = Class.forName(name);
//			if(!(Arrays.stream(c.getInterfaces()).anyMatch($it -> $it == IncomingPacket.class)))
//				throw new IllegalStateException("Class must be implementing IncomingPacket!");
//			IncomingPacket message = (IncomingPacket) c.newInstance();
//			Arrays.stream(opcodes).forEach(op -> NetworkConstants.MESSAGES[op] = message);
//		} catch(Exception e) {
//			System.out.println("Error loading: "+name);
//			e.printStackTrace();
//		}
//	}
//}
