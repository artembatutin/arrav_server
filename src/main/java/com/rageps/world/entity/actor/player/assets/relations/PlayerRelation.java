package com.rageps.world.entity.actor.player.assets.relations;

import com.rageps.net.packet.out.*;
import com.rageps.util.StringUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public final class PlayerRelation {

	private PrivacyChatMode publicChatMode = PrivacyChatMode.ON;
	private PrivacyChatMode privateChatMode = PrivacyChatMode.ON;
	private PrivacyChatMode clanChatMode = PrivacyChatMode.ON;
	private PrivacyChatMode tradeChatMode = PrivacyChatMode.ON;

	private final List<Long> friendList = new ArrayList<>(200);

	private final List<Long> ignoreList = new ArrayList<>(100);

	private int privateMessageId = 1;

	public int getPrivateMessageId() {
		return privateMessageId++;
	}

	private final Player player;

	public PlayerRelation(Player player) {
		this.player = player;
	}

	public PlayerRelation setPrivateMessageId(int privateMessageId) {
		this.privateMessageId = privateMessageId;
		return this;
	}

	public List<Long> getFriendList() {
		return friendList;
	}

	public List<Long> getIgnoreList() {
		return ignoreList;
	}

	public PlayerRelation updateLists(boolean online) {

		if (privateChatMode == PrivacyChatMode.OFF) {
			online = false;
		}

		player.out(new SendPrivateMessageListStatus(PrivateMessageListStatus.LOADED.ordinal()));
		for (Player players : World.get().getPlayers()) {
			if (players == null) {
				continue;
			}
			boolean temporaryOnlineStatus = online;
			if (players.relations.friendList.contains(players.credentials.usernameHash)) {
				if (privateChatMode.equals(PrivacyChatMode.FRIENDS_ONLY) && !friendList.contains(players.credentials.usernameHash)
						|| privateChatMode.equals(PrivacyChatMode.OFF) || ignoreList.contains(players.credentials.usernameHash)) {
					temporaryOnlineStatus = false;
				}
				players.out(new SendAddFriend(player.credentials.usernameHash, temporaryOnlineStatus ? 1 : 0));
			}
			boolean tempOn = true;
			if (player.relations.friendList.contains(players.credentials.usernameHash)) {
				if (players.relations.privateChatMode.equals(PrivacyChatMode.FRIENDS_ONLY)
						&& !players.relations.getFriendList().contains(players.credentials.usernameHash)
						|| players.relations.privateChatMode.equals(PrivacyChatMode.OFF)
						|| players.relations.getIgnoreList().contains(players.credentials.usernameHash)) {
					tempOn = false;
				}
				player.out(new SendAddFriend(players.credentials.usernameHash, tempOn ? 1 : 0));
			}
		}
		return this;
	}

	private void updatePrivacyChatOptions() {
		player.out(new SendChatOption(publicChatMode, privateChatMode, clanChatMode, tradeChatMode));
	}

	private void sendFriends() {
		for (long l : friendList) {
			player.out(new SendAddFriend(l, 0));
		}
	}

	private void sendIgnores() {
		for (long l : ignoreList) {
			player.out(new SendAddIgnore(l));
		}
	}

	private void sendAddFriend(long name) {
		player.out(new SendAddFriend(name, 0));
	}

	public PlayerRelation onLogin() {
		sendFriends();
		sendIgnores();
		updatePrivacyChatOptions();
		updateLists(true);
		return this;
	}

	public void addFriend(Long username) {
		String name = StringUtil.formatName(StringUtil.longToString(username));

		if (player.credentials.usernameHash == username) {
			return;
		}

		if (friendList.size() >= 200) {
			player.message("Your friend list is full!");
			return;
		}
		if (ignoreList.contains(username)) {
			player.message("Please remove " + name + " from your ignore list first.");
			return;
		}
		if (friendList.contains(username)) {
			player.message(name + " is already on your friends list!");
		} else {
			friendList.add(username);
			sendAddFriend(username);
			updateLists(true);
			Optional<Player> result = World.get().search(name);

			result.ifPresent(it -> it.relations.updateLists(true));
		}
	}

	public boolean isFriendWith(String player) {
		return friendList.contains(StringUtil.stringToLong(player));
	}

	public void deleteFriend(Long username) {
		final String name = StringUtil.formatName(StringUtil.longToString(username));
		if (name.equals(player.credentials.username)) {
			return;
		}
		if (friendList.contains(username)) {
			friendList.remove(username);

			if (privateChatMode != PrivacyChatMode.ON) {
				updateLists(false);
			}
		} else {
			player.message("This player is not on your friends list!");
		}
	}

	public void addIgnore(Long username) {
		String name = StringUtil.formatName(StringUtil.longToString(username));

		if (player.credentials.usernameHash == username) {
			return;
		}

		if (ignoreList.size() >= 100) {
			player.message("Your ignore list is full!");
			return;
		}
		if (friendList.contains(username)) {
			player.message("Please remove " + name + " from your friend list first.");
			return;
		}
		if (ignoreList.contains(username)) {
			player.message(name + " is already on your ignore list!");
		} else {
			ignoreList.add(username);
			updateLists(true);

			Optional<Player> result = World.get().search(name);
			result.ifPresent(it -> it.relations.updateLists(false));
		}
	}

	public void deleteIgnore(Long username) {
		String name = StringUtil.formatName(StringUtil.longToString(username));
		if (name.equals(player.credentials.username)) {
			return;
		}
		if (ignoreList.contains(username)) {
			ignoreList.remove(username);
			updateLists(true);
			if (privateChatMode.equals(PrivacyChatMode.ON)) {
				Optional<Player> result = World.get().search(name);
				result.ifPresent(it -> it.relations.updateLists(true));
			}
		} else {
			player.message("This player is not on your ignore list!");
		}
	}

	public void message(Player friend, PrivateChatMessage message) {
		if (friend == null) {
			player.message("This player is currently offline.");
			return;
		}

		if (friend.relations.privateChatMode.equals(PrivacyChatMode.FRIENDS_ONLY)
				&& !friend.relations.friendList.contains(player.credentials.usernameHash)
				|| friend.relations.privateChatMode.equals(PrivacyChatMode.OFF)) {
			player.message("This player is currently offline.");
			return;
		}

		if(player.muted) {
			player.message("You are muted and cannot chat.");
			return;
		}
		if (privateChatMode == PrivacyChatMode.OFF) {
			setPrivateChatMode(PrivacyChatMode.FRIENDS_ONLY, true);
		}
		int rights = player.getRights() == Rights.PLAYER && player.isIronMan() ? Rights.IRON_MAN.getProtocolValue() : player.getRights().getProtocolValue();
		friend.out(new SendPrivateMessage(player.credentials.usernameHash, rights, message.getCompressed(), message.getCompressed().length));
		//World.getDataBus().publish(new PrivateMessageChatLogEvent(player, friend, message.getDecompressed()));
	}

	public PlayerRelation setPrivateChatMode(PrivacyChatMode privateChatMode, boolean update) {
		if (this.privateChatMode == privateChatMode) {
			return this;
		}

		this.privateChatMode = privateChatMode;

		if (update) {
			updateLists(true);
		}

		return this;
	}

	public void setPrivacyChatModes(int publicChat, int privateChat, int clanChat, int tradeChat) {
		if (publicChat >= 0 && publicChat <= PrivacyChatMode.HIDE.getCode()) {
			PrivacyChatMode.get(publicChat).ifPresent(it -> this.publicChatMode = it);
		}

		if (privateChat >= 0 && privateChat <= PrivacyChatMode.OFF.getCode()) {
			PrivacyChatMode.get(privateChat).ifPresent(it -> setPrivateChatMode(it, true));
		}

		if (clanChat >= 0 && clanChat <= PrivacyChatMode.OFF.getCode()) {
			PrivacyChatMode.get(clanChat).ifPresent(it -> this.clanChatMode = it);
		}

		if (tradeChat >= 0 && tradeChat <= PrivacyChatMode.OFF.getCode()) {
			PrivacyChatMode.get(tradeChat).ifPresent(it -> this.tradeChatMode = it);
		}
	}

	public PrivacyChatMode getPublicChatMode() {
		return publicChatMode;
	}

	public void setPublicChatMode(PrivacyChatMode publicChatMode, boolean update) {
		this.publicChatMode = publicChatMode;
		if (update) {
			updatePrivacyChatOptions();
		}
	}

	public PrivacyChatMode getPrivateChatMode() {
		return privateChatMode;
	}

	public void setClanChatMode(PrivacyChatMode clanChatMode, boolean update) {
		this.clanChatMode = clanChatMode;
		if (update) {
			updatePrivacyChatOptions();
		}
	}

	public PrivacyChatMode getClanChatMode() {
		return clanChatMode;
	}

	public PrivacyChatMode getTradeChatMode() {
		return tradeChatMode;
	}

	public void setTradeChatMode(PrivacyChatMode tradeChatMode, boolean update) {
		this.tradeChatMode = tradeChatMode;
		if (update) {
			updatePrivacyChatOptions();
		}
	}

}
