package net.edge.content.clanchat;

import net.edge.world.World;

import java.util.Optional;

/**
 * Represents the settings parameters in a single clan.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class ClanChatSettings {
	
	/**
	 * The minimal clan rank which can talk inside the clan.
	 */
	private ClanChatRank talk = ClanChatRank.MEMBER;
	
	/**
	 * The minimal clan rank which mute others from the clan.
	 */
	private ClanChatRank mute = ClanChatRank.OWNER;
	
	/**
	 * The minimal clan rank which can ban others from the clan.
	 */
	private ClanChatRank ban = ClanChatRank.OWNER;
	
	/**
	 * The boolean which determines if a modification is present.
	 */
	private boolean lootShare;
	
	/**
	 * The actional interface click attempting to change the clan chat settings.
	 * @param clicker     the clan member changing something.
	 * @param interfaceId the interface id clicked.
	 * @param action      the action id changed.
	 */
	public void click(ClanMember clicker, int interfaceId, int action) {
		if(!(interfaceId == 391 || interfaceId == 392 || interfaceId == 393))
			return;
		if(clicker.getRank() != ClanChatRank.OWNER) {
			clicker.getPlayer().message("Only the owner of the clan can do this.");
			return;
		}
		
		switch(interfaceId) {
			case 391:
				Optional<ClanChatRank> talkR = ClanChatRank.forAction(action, false, false);
				if(talkR.isPresent()) {
					talk = talkR.get();
					World.getClanManager().update(ClanChatUpdate.SETTING_MODIFICATION, clicker);
				}
				break;
			case 392:
				Optional<ClanChatRank> muteR = ClanChatRank.forAction(action, true, false);
				if(muteR.isPresent()) {
					mute = muteR.get();
					World.getClanManager().update(ClanChatUpdate.SETTING_MODIFICATION, clicker);
				}
				break;
			case 393:
				Optional<ClanChatRank> banR = ClanChatRank.forAction(action, true, false);
				if(banR.isPresent()) {
					ban = banR.get();
					World.getClanManager().update(ClanChatUpdate.SETTING_MODIFICATION, clicker);
				}
				break;
			
		}
	}
	
	/**
	 * Gets the condition if the {@link ClanMember} can talk in the clan chat.
	 * @param member the clan member attempting to talk.
	 * @return {@code true} if requirement met, {@code false} otherwise.
	 */
	public boolean canTalk(ClanMember member) {
		return member.getRank().getValue() >= talk.getValue();
	}
	
	/**
	 * Sets the new {@link #talk} value.
	 * @param talk the new value to set.
	 */
	public void setTalk(ClanChatRank talk) {
		this.talk = talk;
	}
	
	/**
	 * Gets the condition if the {@link ClanMember} can mute in the clan chat.
	 * @param member the clan member attempting to mute someone.
	 * @return {@code true} if requirement met, {@code false} otherwise.
	 */
	public boolean canMute(ClanMember member) {
		return member.getRank().getValue() >= mute.getValue();
	}
	
	/**
	 * Sets the new {@link #mute} value.
	 * @param mute the new value to set.
	 */
	public void setMute(ClanChatRank mute) {
		this.mute = mute;
	}
	
	/**
	 * Gets the condition if the {@link ClanMember} can ban in the clan chat.
	 * @param member the clan member attempting to ban someone.
	 * @return {@code true} if requirement met, {@code false} otherwise.
	 */
	public boolean canBan(ClanMember member) {
		return member.getRank().getValue() >= ban.getValue();
	}
	
	/**
	 * Sets the new {@link #ban} value.
	 * @param ban the new value to set.
	 */
	public void setBan(ClanChatRank ban) {
		this.ban = ban;
	}
	
	/**
	 * @return the lootShare
	 */
	public boolean isLootShare() {
		return lootShare;
	}
	
	/**
	 * @param lootShare the lootShare to set
	 */
	public void setLootShare(boolean lootShare) {
		this.lootShare = lootShare;
	}
	
	/**
	 * The talking setting rank.
	 * @return talk rank.
	 */
	public ClanChatRank getTalk() {
		return talk;
	}
	
	/**
	 * The kicking setting rank.
	 * @return kick rank.
	 */
	public ClanChatRank getMute() {
		return mute;
	}
	
	/**
	 * The banning setting rank.
	 * @return ban rank.
	 */
	public ClanChatRank getBan() {
		return ban;
	}
	
}
