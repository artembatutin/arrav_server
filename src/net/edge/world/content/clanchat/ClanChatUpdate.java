package net.edge.world.content.clanchat;

import net.edge.net.message.OutputMessages;
import net.edge.utils.TextUtils;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;

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
			OutputMessages message = player.getMessages();
			if(member.getRank().getValue() >= clan.getSettings().getBan().getValue())
				message.sendString(clan.getName(), 50306);
			message.sendString("Talking in: @or1@" + clan.getName(), 50139);
			message.sendString("Owner: " + TextUtils.capitalize(clan.getOwner()), 50140);
			message.sendString("Leave Clan", 50135);
			player.getMessages().sendClanMemberList(clan.getMembers());
			if(member.getRank().getValue() >= clan.getSettings().getBan().getValue()) {
				message.sendString("Manage", 50136);
			}
		}
	},
	NAME_MODIFICATION() {
		@Override
		public void update(ClanChat clan) {
			clan.getMembers().forEach(m -> {
				if(m.getRank().getValue() >= m.getClan().getLowest().getValue())
					m.getPlayer().getMessages().sendString(clan.getName(), 50306);
				m.getPlayer().getMessages().sendString("Talking in: @or1@" + clan.getName(), 50139);
				m.getPlayer().getMessages().sendClanMessage("The clan name has been changed to", clan.getName(), clan.getName(), Rights.PLAYER);
			});
		}
	},
	MEMBER_LIST_MODIFICATION() {
		@Override
		public void update(ClanChat clan) {
			clan.sort();
			clan.getMembers().forEach(m -> m.getPlayer().getMessages().sendClanMemberList(clan.getMembers()));
		}
	},
	BAN_MODIFICATION() {
		@Override
		public void update(ClanChat clan) {
			clan.getMembers().forEach(m -> m.getPlayer().getMessages().sendClanBanList(clan.getBanned()));
		}
		
		@Override
		public void update(ClanMember member) {
			member.getPlayer().getMessages().sendClanBanList(member.getClan().getBanned());
		}
	},
	SETTING_MODIFICATION() {
		@Override
		public void update(ClanMember member) {
			ClanChatSettings settings = member.getClan().getSettings();
			member.getPlayer().getMessages().sendString(settings.getTalk().toPerm(), 50312);
			member.getPlayer().getMessages().sendString(settings.getMute().toPerm(), 50315);
			member.getPlayer().getMessages().sendString(settings.getBan().toPerm(), 50318);
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
	
	/**
	 * Updates with a single {@link ClanMember} instance.
	 * @param member the clan member instance
	 */
	public void update(ClanMember member) {
		
	}
	
	/**
	 * Updates with ths {@link ClanChat} instance.
	 * @param clan the clan chat instance
	 */
	public void update(ClanChat clan) {
		
	}
}
