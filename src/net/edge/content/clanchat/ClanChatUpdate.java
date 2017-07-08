package net.edge.content.clanchat;

import net.edge.net.packet.out.SendClanBanned;
import net.edge.net.packet.out.SendClanMember;
import net.edge.net.packet.out.SendClanMembers;
import net.edge.net.packet.out.SendClanMessage;
import net.edge.util.TextUtils;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

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
				player.text(clan.getName(), 50306);
			player.text("Talking in: @or1@" + clan.getName(), 50139);
			player.text("Owner: " + TextUtils.capitalize(clan.getOwner()), 50140);
			player.text("Leave Clan", 50135);
			player.out(new SendClanMembers(clan.getMembers()));
			if(member.getRank().getValue() >= clan.getSettings().getBan().getValue()) {
				player.text("Manage", 50136);
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
			clan.getMembers().forEach(m -> {
				m.getPlayer().out(new SendClanMembers(clan.getMembers()));
			});
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
	},
	MEMBER_REFRESH() {
		@Override
		public void update(int index, ClanChat clan) {
			clan.getMembers().forEach(m -> m.getPlayer().out(new SendClanMember(index, clan.getMembers().get(index))));
		}
	},;
	
	public void update(int index, ClanChat clan) {
	
	}
	
	public void update(ClanMember member) {
		
	}
	
	public void update(ClanChat clan) {
		
	}
}
