package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.relations.PrivacyChatMode;
import io.netty.buffer.ByteBuf;

public final class SendChatOption implements OutgoingPacket {

	private final PrivacyChatMode publicChat;
	private final PrivacyChatMode privateChat;
	private final PrivacyChatMode clanChat;
	private final PrivacyChatMode tradeChat;

	public SendChatOption(PrivacyChatMode publicChat, PrivacyChatMode privateChat, PrivacyChatMode clanChat,
						  PrivacyChatMode tradeChat) {
		this.publicChat = publicChat;
		this.privateChat = privateChat;
		this.clanChat = clanChat;
		this.tradeChat = tradeChat;
	}

	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(206);
		out.put(publicChat.getCode());
		out.put(privateChat.getCode());
		out.put(clanChat.getCode());
		out.put(tradeChat.getCode());
		return out;
	}

	@Override
	public int size() {
		return 4;
	}
}
