package net.edge.content.clanchat;

import net.edge.net.packet.out.SendClanBanned;
import net.edge.net.packet.out.SendClanMessage;
import net.edge.util.TextUtils;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

/**
 * The enumerated type whose elements represent the update states
 * a clan can be in.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum ClanChatUpdate {
	JOINING() {
		@Override
		public void update(ClanMember member) {
			ClanChat clan = member.getClan();
			Player player = member.getPlayer();
			if(member.getRank().getValue() >= clan.getSettings().getBan().getValue())
				player.text(50306, clan.getName());
			player.text(50139, "Talking in: @or1@" + clan.getName());
			player.text(50140, "Owner: " + TextUtils.capitalize(clan.getOwner()));
			player.text(50135, "Leave Clan");
			MEMBER_LIST_MODIFICATION.update(member);
			if(member.getRank().getValue() >= clan.getSettings().getBan().getValue()) {
				player.text(50136, "Manage");
			}
		}
	},
	NAME_MODIFICATION() {
		@Override
		public void update(ClanChat clan) {
			clan.getMembers().forEach(m -> {
				if(m.getRank().getValue() >= m.getClan().getLowest().getValue())
					m.getPlayer().text(50306, clan.getName());
				m.getPlayer().text(50139, "Talking in: @or1@" + clan.getName());
				m.getPlayer().out(new SendClanMessage("The clan name has been changed to", clan.getName(), clan.getName(), Rights.PLAYER));
			});
		}
	},
	MEMBER_LIST_MODIFICATION() {
		@Override
		public void update(ClanChat clan) {
			for(ClanMember forM : clan.getMembers()) {
				int i = 0;
				for(ClanMember m : clan.getMembers()) {
					String rank = m.isMuted() ? "y" : "n";
					forM.getPlayer().text(50144 + i, rank + m.getRank().toIcon(forM.getPlayer(), m.getPlayer()) + m.getPlayer().getFormatUsername());
					i++;
				}
			}
		}
	},
	BAN_MODIFICATION() {
		@Override
		public void update(ClanChat clan) {
			clan.getMembers().forEach(m -> m.getPlayer().out(new SendClanBanned(clan.getBanned())));
		}
		
		@Override
		public void update(ClanMember member) {
			member.getPlayer().out(new SendClanBanned(member.getClan().getBanned()));
		}
	},
	SETTING_MODIFICATION() {
		@Override
		public void update(ClanMember member) {
			ClanChatSettings settings = member.getClan().getSettings();
			member.getPlayer().text(50312, settings.getTalk().toPerm());
			member.getPlayer().text(50315, settings.getMute().toPerm());
			member.getPlayer().text(50318, settings.getBan().toPerm());
		}
	},
	LOOT_SHARE_MODIFICATION() {
		@Override
		public void update(ClanMember member) {
			// TODO Auto-generated method stub
			
		}
	},
	COIN_SHARE_MODIFICATION() {
		@Override
		public void update(ClanMember member) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public void update(ClanMember member) {
		
	}
	
	public void update(ClanChat clan) {
		
	}
}
