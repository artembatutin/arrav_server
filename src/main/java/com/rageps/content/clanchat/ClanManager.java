//package com.rageps.content.clanchat;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
//import it.unimi.dsi.fastutil.objects.ObjectArrayList;
//import it.unimi.dsi.fastutil.objects.ObjectList;
//import com.rageps.net.packet.out.SendClanBanned;
//import com.rageps.net.packet.out.SendClearText;
//import com.rageps.net.packet.out.SendEnterName;
//import com.rageps.util.TextUtils;
//import com.rageps.util.json.JsonSaver;
//import com.rageps.world.entity.actor.player.Player;
//
//import java.util.Optional;
//
///**
// * The class which manages clans on the world.
// * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
// * @author Artem Batutin
// */
//public final class ClanManager {
//
//	/**
//	 * The clan manager for the world.
//	 */
//	private static final ClanManager CLAN_MANAGER = new ClanManager();
//
//	/**
//	 * The collection of clans on this world.
//	 */
//	private static final Object2ObjectArrayMap<String, ClanChat> GLOBAL_CLANS = new Object2ObjectArrayMap<>();
//
//	public void clearOnLogin(Player player) {
//		player.text(50139, "Talking in: ");
//		player.text(50140, "Owner: ");
//		player.text(50135, "Join Clan");
//		player.text(50136, "Clan Setup");
//		player.out(new SendClearText(50144, 100));
//		player.out(new SendClanBanned(new ObjectArrayList<>()));
//	}
//
//	/**
//	 * Attempts to register a clan chat for the specified {@code player}.
//	 * @param player the player attempting to register a clan.
//	 * @param name the name of the clan to be created.
//	 */
//	public void create(Player player, String name) {
//		if(player.getClan().isPresent()) {
//			player.message("You are currently in a clan.");
//			return;
//		}
//		if(GLOBAL_CLANS.containsKey(player.credentials.username)) {
//			player.message("You already own a clan.");
//			return;
//		}
//		ClanChat clan = new ClanChat(TextUtils.capitalize(name), player.credentials.username);
//		clan.add(player, ClanChatRank.OWNER);
//		GLOBAL_CLANS.put(player.credentials.username, clan);
//	}
//
//	/**
//	 * Attempts to join a clan chat for the specified {@code player}.
//	 * @param player the player attempting to join.
//	 * @param name the name of the clan to join.
//	 */
//	public void join(Player player, String name) {
//		if(name.length() == 0) {
//			return;
//		}
//		Optional<ClanChat> clan = getClan(name);
//		if(player.getClan().isPresent()) {
//			player.message("You are currently in a clan chat.");
//			return;
//		}
//		if(!clan.isPresent()) {
//			player.message("This clan doesn't exist.");
//			return;
//		}
//		if(clan.get().getBanned().contains(player.credentials.username)) {
//			player.message("You are banned from this clan.");
//			return;
//		}
//		ClanChat chat = clan.get();
//		if(!chat.add(player, chat.getRank(player.credentials.username))) {
//			player.message("This clan is currently full.");
//		}
//
//	}
//
//	/**
//	 * Attempts to exit from the clan chat for the specified {@code player}.
//	 * @param player the player attempting to exit.
//	 */
//	public void exit(Player player) {
//		if(player.getClan().isPresent()) {
//			ClanChat clan = player.getClan().get().getClan();
//			clan.remove(player, false);
//		} else {
//			clearOnLogin(player);
//		}
//	}
//
//	/**
//	 * Attempts to exit from the clan chat for the specified {@code player}.
//	 * @param player the player attempting to exit.
//	 */
//	public void delete(Player player) {
//		if(player.getClan().isPresent()) {
//			if(player.getClan().get().getRank() != ClanChatRank.OWNER) {
//				player.message("Only the owner is allowed to do that.");
//				return;
//			}
//			ClanChat clan = player.getClan().get().getClan();
//			for(int pos = 0; pos < clan.getMembers().length; pos++) {
//				if(clan.getMembers()[pos] == null)
//					continue;
//				ClanMember m = clan.getMembers()[pos];
//				clan.remove(m.getPlayer(), false);
//			}
//			GLOBAL_CLANS.remove(player.credentials.username);
//		} else {
//			clearOnLogin(player);
//		}
//	}
//
//	/**
//	 * Attempts to rename the clan chat.
//	 * @param player owenr
//	 */
//	public void rename(Player player) {
//		if(player.getClan().isPresent()) {
//			if(player.getClan().get().getRank() != ClanChatRank.OWNER) {
//				player.message("Only the owner is allowed to do that.");
//				return;
//			}
//			player.out(new SendEnterName("The new clan chat name to set:", s -> () -> {
//				String newName = TextUtils.capitalize(s);
//				player.getClan().get().getClan().setName(newName);
//				ClanManager.get().update(ClanChatUpdate.NAME_MODIFICATION, player.getClan().get().getClan());
//			}));
//		}
//	}
//
//	/**
//	 * Attempts to get the clan from the specified {@code name}
//	 * @param name the name to return the clan chat from.
//	 * @return the clan chat wrapped in an optional, {@link Optional#empty()} otherwise.
//	 */
//	public Optional<ClanChat> getClan(String name) {
//		ClanChat clan = GLOBAL_CLANS.get(name);
//		return Optional.ofNullable(clan);
//	}
//
//	/**
//	 * Gets all of the {@link #GLOBAL_CLANS} in the world.
//	 * @return the clan chat wrapped in an optional, {@link Optional#empty()} otherwise.
//	 */
//	public Object2ObjectArrayMap<String, ClanChat> getClans() {
//		return GLOBAL_CLANS;
//	}
//
//	/**
//	 * Updates the clan chat for the specified {@code members}
//	 * @param update the clan update to update the interface for.
//	 * @param members the members to update the interface for.
//	 */
//	public void update(ClanChatUpdate update, ObjectList<ClanMember> members) {
//		for(ClanMember member : members) {
//			if(member == null)
//				continue;
//			if(!member.getPlayer().getClan().isPresent())
//				continue;
//			update(update, member);
//		}
//	}
//
//	public void update(ClanChatUpdate update, ClanChat clan, ClanMember member) {
//		update.update(clan, member);
//	}
//
//	public void update(ClanChatUpdate update, ClanMember member) {
//		update.update(member);
//	}
//
//	public void update(ClanChatUpdate update, ClanChat clan) {
//		update.update(clan);
//	}
//
//	/**
//	 * Saves all the {@link #GLOBAL_CLANS}.
//	 */
//	public void save() {
//		JsonSaver json = new JsonSaver();
//		Gson gson = new Gson();
//		for(String own : GLOBAL_CLANS.keySet()) {
//			json.current().addProperty("owner", own);
//			ClanChat clan = GLOBAL_CLANS.get(own);
//			json.current().addProperty("name", clan.getName());
//			json.current().addProperty("loot", clan.getSettings().isLootShare());
//			json.current().addProperty("ban", clan.getSettings().getBan().toString());
//			json.current().addProperty("mute", clan.getSettings().getMute().toString());
//			json.current().addProperty("talk", clan.getSettings().getTalk().toString());
//			JsonArray mutes = new JsonArray();
//			for(String s : clan.getBanned()) {
//				mutes.add(gson.toJsonTree(s));
//			}
//			json.current().add("mutes", mutes);
//			JsonArray bans = new JsonArray();
//			for(String s : clan.getBanned()) {
//				bans.add(gson.toJsonTree(s));
//			}
//			json.current().add("bans", bans);
//			JsonArray ranks = new JsonArray();
//			for(String s : clan.getRanked().keySet()) {
//				JsonObject ranked = new JsonObject();
//				ranked.addProperty("name", s);
//				ranked.addProperty("rank", clan.getRank(s).toString());
//				ranks.add(ranked);
//			}
//			json.current().add("ranked", ranks);
//			json.split();
//		}
//		json.publish("./data/def/clans.json");
//	}
//
//	/**
//	 * Returns the clan chat manager.
//	 */
//	public static ClanManager get() {
//		return CLAN_MANAGER;
//	}
//
//}
