package net.arrav.content.clanchat;

import net.arrav.net.packet.out.SendClanMessage;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a single clan chat member in a {@link ClanChat}.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @author Artem Batutin
 */
public final class ClanMember {
	
	/**
	 * The player which represents the member.
	 */
	private final Player player;
	
	/**
	 * The clanchat this member is in.
	 */
	private final ClanChat clan;
	
	/**
	 * The rank this member has in the clanchat.
	 */
	private ClanChatRank rank;
	
	/**
	 * The condition if the player is muted in the clan chat.
	 */
	private boolean muted;
	
	/**
	 * The cursor position of this member in the list.
	 */
	private int pos;
	
	/**
	 * Constructs a new {@link ClanMember}.
	 * @param player {@link #player}.
	 * @param clan {@link #clan}.
	 * @param rank {@link #rank}.
	 */
	ClanMember(Player player, ClanChat clan, ClanChatRank rank) {
		this.player = player;
		this.clan = clan;
		this.rank = rank;
	}
	
	/**
	 * Tries to mute a clan member.
	 * @param index the clan member index to be muted.
	 */
	public void mute(int index) {
		if(!clan.getSettings().canMute(this)) {
			sendMessage("You don't have the requirement rank to mute in this clan.");
			return;
		}
		ClanMember member = clan.getMembers()[index];
		if(member.getRank().getValue() >= clan.getLowest().getValue()) {
			sendMessage("You cannot mute this player from the clan.");
			return;
		}
		member.setMute(true);
		clan.getMuted().add(member.getPlayer().credentials.username);
		member.sendMessage("You are now muted in the clan chat.");
		for(ClanMember m : clan.getMembers()) {
			if(m == null)
				continue;
			String mute = member.isMuted() ? "y" : "n";
			m.getPlayer().text(50144 + index, mute + member.getRank().toIcon(m.getPlayer(), member.getPlayer()) + member.getPlayer().getFormatUsername());
		}
	}
	
	/**
	 * Tries to mute a clan member.
	 * @param index the clan member index to be muted.
	 */
	public void unmute(int index) {
		if(!clan.getSettings().canMute(this)) {
			sendMessage("You don't have the requirement rank to unmute in this clan.");
			return;
		}
		ClanMember member = clan.getMembers()[index];
		member.setMute(false);
		member.getClan().getMuted().remove(member.getPlayer().credentials.username);
		member.sendMessage("You are now unmuted from the clan chat.");
		for(ClanMember m : clan.getMembers()) {
			if(m == null)
				continue;
			String mute = member.isMuted() ? "y" : "n";
			m.getPlayer().text(50144 + index, mute + member.getRank().toIcon(m.getPlayer(), member.getPlayer()) + member.getPlayer().getFormatUsername());
		}
	}
	
	/**
	 * Tries to ban a player from the clan.
	 * @param index the clan member index to be banned.
	 */
	public void ban(int index) {
		if(!clan.getSettings().canBan(this)) {
			sendMessage("You don't have the requirement rank to ban in this clan.");
			return;
		}
		ClanMember member = clan.getMembers()[index];
		if(member.getRank() == ClanChatRank.OWNER) {
			sendMessage("You cannot ban the owner of this clan.");
			return;
		}
		if(member == this) {
			sendMessage("You cannot ban yourself.");
			return;
		}
		if(member.getRank().getValue() >= clan.getLowest().getValue()) {
			sendMessage("You cannot ban this player from the clan.");
			return;
		}
		clan.getBanned().add(member.getPlayer().credentials.username);
		clan.remove(member.getPlayer(), false);
		ClanManager.get().update(ClanChatUpdate.BAN_MODIFICATION, clan);
		member.sendMessage("You got banned from the clan.");
	}
	
	/**
	 * Tries to unban a player from the clan.
	 * @param index the clan member index to be banned.
	 */
	public void unban(int index) {
		if(!clan.getSettings().canBan(this)) {
			sendMessage("You don't have the requirement rank to unban in this clan.");
			return;
		}
		String member = clan.getBanned().get(index);
		clan.getBanned().remove(member);
		ClanManager.get().update(ClanChatUpdate.BAN_MODIFICATION, clan);
	}
	
	/**
	 * Tries to send a message to the clan chat.
	 * @param message the inputted message to be sent.
	 * @return the saved {@link ClanChatRank}, if none {@link ClanChatRank#MEMBER}.
	 */
	public void message(String message, String author) {
		if(muted) {
			sendMessage("You are currently muted in this clan.");
			return;
		}
		if(!clan.getSettings().canTalk(this)) {
			sendMessage("You don't have the requirement rank to talk in this clan.");
			return;
		}
		Rights rank = player.getRights();
		if(rank == Rights.PLAYER && player.isIronMan())
			rank = Rights.IRON_MAN;
		String clanName = clan.getName();
		for(ClanMember member : clan.getMembers()) {
			if(member == null)
				continue;
			member.getPlayer().out(new SendClanMessage(author, message, clanName, rank));
		}
	}
	
	public void message(String message) {
		message(message, player.getFormatUsername());
	}
	
	/**
	 * Updates the rank of this clan member.
	 * @param interfaceId the interface id to get the member to promote.
	 * @param action the right click action set.
	 */
	public void rank(int interfaceId, int action) {
		if(rank != ClanChatRank.OWNER) {
			sendMessage("Only the owner of the clan can rank members.");
			return;
		}
		ClanMember member = clan.getMembers()[interfaceId];
		if(member == null) {
			return;
		}
		if(Objects.equals(member, this)) {
			sendMessage("You cannot rank yourself.");
			return;
		}
		Optional<ClanChatRank> rank;
		if(action == 0) {
			rank = Optional.of(ClanChatRank.MEMBER);
		} else {
			rank = ClanChatRank.forAction(action, false, true);
		}
		if(rank.isPresent()) {
			if(member.getRank().getValue() < rank.get().getValue()) {
				member.getPlayer().out(new SendClanMessage("", "You were promoted to " + rank.get().toString().toLowerCase() + ".", clan.getName(), Rights.PLAYER));
				member.setRank(rank.get());
				ClanManager.get().update(ClanChatUpdate.MEMBER_LIST_MODIFICATION, clan, member);
			} else if(member.getRank().getValue() > rank.get().getValue()) {
				member.getPlayer().out(new SendClanMessage("", "You were demoted to " + rank.get().toString().toLowerCase() + ".", clan.getName(), Rights.PLAYER));
				member.setRank(rank.get());
				ClanManager.get().update(ClanChatUpdate.MEMBER_LIST_MODIFICATION, clan, member);
			}
		}
	}
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * @return the clan
	 */
	public ClanChat getClan() {
		return clan;
	}
	
	/**
	 * @return the rank
	 */
	public ClanChatRank getRank() {
		return rank;
	}
	
	/**
	 * Sets the new rank of the member.
	 * @param rank the new rank to set.
	 */
	public void setRank(ClanChatRank rank) {
		this.rank = rank;
		if(!clan.getRanked().containsKey(player.credentials.username)) {
			if(rank == ClanChatRank.MEMBER) {
				clan.getRanked().remove(player.credentials.username);
			} else {
				clan.getRanked().replace(player.credentials.username, rank);
			}
			clan.getRanked().put(player.credentials.username, rank);
		} else if(rank != ClanChatRank.MEMBER) {
			clan.getRanked().put(player.credentials.username, rank);
		}
	}
	
	/**
	 * @return the muted
	 */
	public boolean isMuted() {
		return muted;
	}
	
	/**
	 * Sets the new mute flag of the member.
	 * @param muted the mute flag to set.
	 */
	public void setMute(boolean muted) {
		this.muted = muted;
	}
	
	/**
	 * Sends a clan simple message.
	 * @param text the text to send.
	 */
	public void sendMessage(String text) {
		player.out(new SendClanMessage("", text, clan.getName(), Rights.PLAYER));
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
}
