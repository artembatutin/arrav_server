package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.assets.relations.PrivacyChatMode;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ChatOptionPacket extends Packet {

    private final PrivacyChatMode publicChat;
    private final PrivacyChatMode privateChat;
    private final PrivacyChatMode clanChat;
    private final PrivacyChatMode tradeChat;

    public ChatOptionPacket(PrivacyChatMode publicChat, PrivacyChatMode privateChat, PrivacyChatMode clanChat,
                          PrivacyChatMode tradeChat) {
        this.publicChat = publicChat;
        this.privateChat = privateChat;
        this.clanChat = clanChat;
        this.tradeChat = tradeChat;
    }

    public PrivacyChatMode getClanChat() {
        return clanChat;
    }

    public PrivacyChatMode getPrivateChat() {
        return privateChat;
    }

    public PrivacyChatMode getPublicChat() {
        return publicChat;
    }

    public PrivacyChatMode getTradeChat() {
        return tradeChat;
    }
}