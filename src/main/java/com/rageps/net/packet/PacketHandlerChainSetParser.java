package com.rageps.net.packet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.action.ActionInitializer;
import com.rageps.net.refactor.packet.Packet;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.PacketHandlerChainSet;
import com.rageps.util.json.JsonLoader;
import org.xml.sax.SAXException;

/**
 * A class that parses the {@code messages.xml} file to produce {@link PacketHandlerChainSet}s.
 *
 * @author Graham
 */
public final class PacketHandlerChainSetParser {

	/**
	 * Creates the message chain parser.
	 */
	public PacketHandlerChainSetParser() {
	}

	/**
	 * Parses the XML and produces a group of {@link PacketHandlerChainSet}s.
	 *
	 * @return A {@link PacketHandlerChainSet}.
	 * @throws IOException If an I/O error occurs.
	 * @throws SAXException If a SAX error occurs.
	 * @throws ReflectiveOperationException If a reflection error occurs.
	 */
	@SuppressWarnings("unchecked")
	public PacketHandlerChainSet parse() {
		PacketHandlerChainSet chainSet = new PacketHandlerChainSet();

		new JsonLoader("./data/def/packets/packets.json") {
			@Override
			public void load(JsonObject reader, Gson builder) {

				String handlerClassName = reader.get("handler").getAsString();
				String messageClassName = reader.get("type").getAsString();
				try {


					Class<? extends Packet> messageClass = (Class<? extends Packet>) Class.forName(messageClassName);


					Class<? extends PacketHandler<? extends Packet>> handlerClass = (Class<? extends PacketHandler<? extends Packet>>) Class.forName(handlerClassName);
					PacketHandler<? extends Packet> handler = handlerClass.newInstance();
					chainSet.putHandler(messageClass, handler);
				}catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.load();
		return chainSet;
	}

	public static void main(String[] args) {
	}

}