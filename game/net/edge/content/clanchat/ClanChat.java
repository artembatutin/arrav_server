package net.edge.content.clanchat;

import net.edge.World;
import net.edge.world.node.entity.player.Player;

import java.util.*;

/**
 * Represents a single clan chat in the world.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class ClanChat {
	
	/**
	 * Comparator to compare members to sort the list.
	 */
	public static Comparator<ClanMember> comparator = (m1, m2) -> m2.getRank().getValue() - m1.getRank().getValue();
	
	/**
	 * The name of the owner of this clan.
	 */
	private final String owner;
	
	/**
	 * The clan chat members in this clan chat.
	 */
	private final List<ClanMember> members = new ArrayList<>(100);
	
	/**
	 * A tool.mapviewer of clan chat ranks.
	 */
	private final Map<String, ClanChatRank> ranks = new HashMap<>();
	
	/**
	 * A list of banned members from this clan chat.
	 */
	private final List<String> banned = new ArrayList<>();
	
	/**
	 * A list of muted members from this clan chat.
	 */
	private final List<String> muted = new ArrayList<>();
	
	/**
	 * The name of the clan chat.
	 */
	private String name;
	
	/**
	 * The settings of this clan chat.
	 */
	private ClanChatSettings settings = new ClanChatSettings();
	
	/**
	 * Constructs a new {@link ClanChat}.
	 */
	public ClanChat(String name, String owner) {
		this.name = name;
		this.owner = owner;
	}
	
	/**
	 * Adds a player to the member's list.
	 * @param player the player to add.
	 */
	public boolean add(Player player, ClanChatRank rank) {
		ClanMember member = new ClanMember(player, this, rank);
		if(members.add(member)) {
			player.setClan(Optional.of(member));
			
			if(muted.contains(player.getUsername())) {
				member.setMute(true);
			}
			if(rank.getValue() >= getLowest().getValue()) {
				World.getClanManager().update(ClanChatUpdate.BAN_MODIFICATION, member);
			}
			if(rank != ClanChatRank.MEMBER) {
				if(!ranks.containsKey(player.getUsername())) {
					ranks.put(player.getUsername(), rank);
				}
			}
			World.getClanManager().update(ClanChatUpdate.JOINING, member);
			World.getClanManager().update(ClanChatUpdate.MEMBER_LIST_MODIFICATION, this);
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a player to the member's list.
	 * @param player the player to add.
	 */
	public void remove(Player player, boolean logout) {
		if(!player.getClan().isPresent()) {
			return;
		}
		members.remove(player.getClan().get());
		
		if(!logout) {
			player.setClan(Optional.empty());
		}
		
		World.getClanManager().update(ClanChatUpdate.MEMBER_LIST_MODIFICATION, this);
		World.getClanManager().clearOnLogin(player);
	}
	
	/**
	 * Gets the rank if any were saved.
	 * @param username the player's username to check
	 * @return the saved {@link ClanChatRank}, if none {@link ClanChatRank#MEMBER}.
	 */
	public ClanChatRank getRank(String username) {
		if(ranks.containsKey(username)) {
			return ranks.get(username);
		}
		return ClanChatRank.MEMBER;
	}
	
	/**
	 * Sorts the array and returns the players
	 */
	public void sort() {
		members.sort(comparator);
	}
	
	/**
	 * @return {@link #owner}.
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * @return {@link #name}.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return {@link #settings}.
	 */
	public ClanChatSettings getSettings() {
		return settings;
	}
	
	/**
	 * @return the members
	 */
	public List<ClanMember> getMembers() {
		return members;
	}
	
	/**
	 * @return the banned
	 */
	public List<String> getBanned() {
		return banned;
	}
	
	/**
	 * @return the banned
	 */
	public List<String> getMuted() {
		return muted;
	}
	
	/**
	 * @return the ranks
	 */
	public Map<String, ClanChatRank> getRanked() {
		return ranks;
	}
	
	/**
	 * Gets the lowest authority rank in the clan.
	 * @return the lowest authority.
	 */
	public ClanChatRank getLowest() {
		return settings.getBan().getValue() >= settings.getMute().getValue() ? settings.getMute() : settings.getBan();
	}
	
}
