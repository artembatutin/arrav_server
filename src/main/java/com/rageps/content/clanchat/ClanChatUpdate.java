package com.rageps.content.clanchat;

/**
 * The enumerated type whose elements represent the update states
 * a clan can be in.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
/*public enum ClanChatUpdate {
	JOINING() {
		@Override
		public void update(ClanMember member) {
			ClanChat clan = member.getClan();
			Player player = member.getPlayer();
			if(member.getRank().getValue() >= clan.getSettings().getBan().getValue())
				player.text(50306, clan.getName());
			player.text(50139, "Talking in: @lye@" + clan.getName());
			player.text(50140, "Owner: @whi@" + TextUtils.capitalize(clan.getOwner()));
			player.text(50135, "Leave Clan");
			if(member.getRank().getValue() >= clan.getSettings().getBan().getValue()) {
				player.text(50136, "Manage");
			}
			player.send(new ClearText(50144, 100));
			for(int pos = 0; pos < clan.getMembers().length; pos++) {
				if(clan.getMembers()[pos] == null) {
					continue;
				}
				ClanMember m = clan.getMembers()[pos];
				String rank = m.isMuted() ? "y" : "n";
				player.text(50144 + pos, rank + m.getRank().toIcon(player, m.getPlayer()) + m.getPlayer().getFormatUsername());
			}
		}
	}, NAME_MODIFICATION() {
		@Override
		public void update(ClanChat clan) {
			for(int pos = 0; pos < clan.getMembers().length; pos++) {
				if(clan.getMembers()[pos] == null)
					continue;
				ClanMember m = clan.getMembers()[pos];
				if(m.getRank().getValue() >= m.getClan().getLowest().getValue())
					m.getPlayer().text(50306, clan.getName());
				m.getPlayer().text(50139, "Talking in: @lye@" + clan.getName());
				m.getPlayer().send(new ClanDetails("The clan name has been changed.", clan.getName(), clan.getName(), Rights.PLAYER));
			}
		}
	}, MEMBER_LIST_MODIFICATION() {
		@Override
		public void update(ClanChat clan, ClanMember member) {
			boolean quit = clan.getMembers()[member.getPos()] == null;
			for(int pos = 0; pos < clan.getMembers().length; pos++) {
				if(clan.getMembers()[pos] == null)
					continue;
				ClanMember m = clan.getMembers()[pos];
				if(quit) {
					m.getPlayer().text(50144 + member.getPos(), "");
				} else {
					String rank = member.isMuted() ? "y" : "n";
					m.getPlayer().text(50144 + member.getPos(), rank + member.getRank().toIcon(m.getPlayer(), member.getPlayer()) + member.getPlayer().getFormatUsername());
				}
			}
		}
	}, BAN_MODIFICATION() {
		@Override
		public void update(ClanChat clan) {
			for(int pos = 0; pos < clan.getMembers().length; pos++) {
				if(clan.getMembers()[pos] == null)
					continue;
				ClanMember m = clan.getMembers()[pos];
				if(m.getRank().getValue() >= clan.getLowest().getValue()) {
					m.getPlayer().send(new ClanBanned(clan.getBanned()));
				}
			}
		}
		
		@Override
		public void update(ClanMember member) {
			member.getPlayer().send(new ClanBanned(member.getClan().getBanned()));
		}
	}, SETTING_MODIFICATION() {
		@Override
		public void update(ClanMember member) {
			ClanChatSettings settings = member.getClan().getSettings();
			member.getPlayer().text(50312, settings.getTalk().toPerm());
			member.getPlayer().text(50315, settings.getMute().toPerm());
			member.getPlayer().text(50318, settings.getBan().toPerm());
		}
	}, LOOT_SHARE_MODIFICATION() {
		@Override
		public void update(ClanMember member) {
			
		}
	}, COIN_SHARE_MODIFICATION() {
		@Override
		public void update(ClanMember member) {
			
		}
	};
	
	public void update(ClanChat clan, ClanMember member) {
	
	}
	
	public void update(ClanMember member) {
	
	}
	
	public void update(ClanChat clan) {
	
	}
}
*/