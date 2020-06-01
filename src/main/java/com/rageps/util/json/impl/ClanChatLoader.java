package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rageps.content.clanchat.ClanChat;
import com.rageps.content.clanchat.ClanChatRank;
import com.rageps.content.clanchat.ClanManager;
import com.rageps.util.json.JsonLoader;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all clan chat instances.
 * @author Artem Batutin<artembatutin@gmail.com>
 */
public final class ClanChatLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link ClanChatLoader}.
	 */
	public ClanChatLoader() {
		super("./data/def/clans.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		String owner = Objects.requireNonNull(reader.get("owner").getAsString());
		String name = Objects.requireNonNull(reader.get("name").getAsString());
		boolean loot = reader.get("loot").getAsBoolean();
		ClanChatRank ban = Objects.requireNonNull(builder.fromJson(reader.get("ban"), ClanChatRank.class));
		ClanChatRank mute = Objects.requireNonNull(builder.fromJson(reader.get("mute"), ClanChatRank.class));
		ClanChatRank talk = Objects.requireNonNull(builder.fromJson(reader.get("talk"), ClanChatRank.class));
		
		ClanChat clan = new ClanChat(name, owner);
		clan.getSettings().setLootShare(loot);
		clan.getSettings().setBan(ban);
		clan.getSettings().setMute(mute);
		clan.getSettings().setTalk(talk);
		
		JsonArray mutes = reader.get("mutes").getAsJsonArray();
		mutes.forEach(b -> clan.getMuted().add(b.getAsString()));
		
		JsonArray bans = reader.get("bans").getAsJsonArray();
		bans.forEach(b -> clan.getBanned().add(b.getAsString()));
		
		JsonArray ranked = reader.get("ranked").getAsJsonArray();
		ranked.forEach(r -> {
			JsonObject o = r.getAsJsonObject();
			clan.getRanked().put(o.get("name").getAsString(), builder.fromJson(o.get("rank"), ClanChatRank.class));
		});
		
		ClanManager.get().getClans().put(owner, clan);
	}
}