package net.edge.content.clanchat;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.util.TextUtils;
import net.edge.world.node.entity.player.Player;

import java.util.List;
import java.util.Optional;

/**
 * The enumerated type whose elements represents all the ranks a member can have
 * in a clan chat.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum ClanChatRank {
	MEMBER(0),
	FRIEND(1),
	RECRUIT(2),
	CORPORAL(3),
	SERGEANT(4),
	LIEUTENANT(5),
	CAPTAIN(6),
	GENERAL(7),
	OWNER(8);
	
	private static final ObjectList<ClanChatRank> RANKS = new ObjectArrayList<>(ClanChatRank.values());
	
	/**
	 * The value that matches this difficulty level.
	 */
	private final int value;
	
	/**
	 * Constructs a new {@link ClanChatRank}.
	 * @param value {@link #value}.
	 */
	ClanChatRank(int value) {
		this.value = value;
	}
	
	/**
	 * Retrieves the clan chat rank element for {@code action}.
	 * @param action the action click from the interface.
	 * @return the clam chat rank wrapped in an optional, or an empty optional
	 * if no rank was found.
	 */
	public static Optional<ClanChatRank> forAction(int action, boolean ignoreMembers, boolean ignoreFriends) {
		if(ignoreMembers)
			action += 1;
		if(ignoreFriends)
			action += 1;
		final int seek = action;
		for(ClanChatRank r : RANKS) {
			if(r.getValue() == seek) {
				return Optional.of(r);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Formats the rank to represent the rank icon for the client.
	 * @param parent the player for who we will send the icon.
	 * @param player the player to be checked for friendship..
	 * @return the formatted icon format
	 */
	public int toIcon(Player parent, Player player) {
		if(player.getClan().get().getRank() == MEMBER) {
			if(parent.getFriends().contains(player.getCredentials().getUsernameHash())) {
				return 1;
			}
		}
		return this.ordinal();
	}
	
	/**
	 * Formats the rank to represent the permission state for the client.
	 * @return the formatted permission format
	 */
	public String toPerm() {
		if(this == MEMBER) {
			return "Anyone";
		} else if(this == OWNER) {
			return "Only me";
		} else {
			return TextUtils.capitalize(this.toString().toLowerCase());
		}
	}
	
	/**
	 * Determines if this rank is greater than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 * @param other the argued right to compare.
	 * @return {@code true} if this right is greater, {@code false} otherwise.
	 */
	public final boolean greater(ClanChatRank other) {
		return value > other.value;
	}
	
	/**
	 * Determines if this right is greater or equal than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 * @param other the argued right to compare.
	 * @return {@code true} if this right is lesser, {@code false} otherwise.
	 */
	public final boolean greaterOrEqual(ClanChatRank other) {
		return value >= other.value;
	}
	
	/**
	 * Determines if this rank is lesser than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 * @param other the argued right to compare.
	 * @return {@code true} if this right is lesser, {@code false} otherwise.
	 */
	public final boolean less(ClanChatRank other) {
		return value < other.value;
	}
	
	/**
	 * Determines if this rank is lesser or equal than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 * @param other the argued right to compare.
	 * @return {@code true} if this right is lesser, {@code false} otherwise.
	 */
	public final boolean lessOrEqual(ClanChatRank other) {
		return value <= other.value;
	}
	
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
}
