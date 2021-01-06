package com.rageps.content.clanchannel.channel;

import com.rageps.content.TabInterface;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.impl.StatementDialogue;
import com.rageps.net.refactor.packet.out.model.*;
import com.rageps.util.StringUtil;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.content.clanchannel.*;
import com.rageps.world.entity.actor.player.assets.Rights;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.rageps.content.clanchannel.channel.ClanManagement.*;

/**
 * The handler for the clan channel.
 *
 * @author Michael
 * @author Daniel
 */
public class ClanChannelHandler {

	private static final Logger log = LogManager.getLogger();


	/** The clan channel instance. */
	private final ClanChannel channel;

	/** Constructs a new <code>ClanChannelHandler</code>. */
	ClanChannelHandler(ClanChannel channel) {
		this.channel = channel;
	}

	/** Handles logging into the server. */
	public static void onLogin(Player player) {
		clean(player);
		connect(player, player.clanChannel.getOwner(), true);
	}

	/** Handles player joining a clan channel. */
	public static void connect(Player player, String owner, boolean fromLogin) {
		
		player.message("Attempting to connect to clan...");
		
		if (fromLogin)
			player.clanChannel = null;
		
		Optional<ClanChannel> channel = ClanRepository.getChannel(owner);
		
		if (channel.isPresent()) {
			log.warn("player {} tried to join a null clan {}", player.credentials.username, owner);
			boolean loaded = true;
			Path path = Paths.get("./data/content/clan/");
			File[] files = path.toFile().listFiles();

			if (files == null) {
				log.warn("No clan files were found.");
				return;
			}

			for (File file : files) {
				if (file.getName().toLowerCase().contains(player.credentials.username.toLowerCase().trim())
						&& file.length() > 0) {
					loaded = false;
					break;
				}
			}

			if (owner.equalsIgnoreCase(player.credentials.username)) {
				if (!loaded) {
					player.message("Your clan didn't load into the server properly!");
					player.message("If you see this message, your clan was not reset.");
					player.message(
							"Try to join your clan again. If you still see this message, contact a staff member.");
					ClanChannel.load(player.credentials.username.toLowerCase().trim());
					return;
				}
				ClanChannel.create(player);
			} else {
				if (!loaded) {
					player.message("This clan didn't load into the server properly!");
					player.message(
							"Try to join your clan again. If you still see this message, contact a staff member.");
					ClanChannel.load(player.credentials.username.toLowerCase().trim());
					return;
				}
				player.message("Connection was refused: No channel exists!");
			}
			return;
		}
		ClanChannel clanChannel = channel.get();
		if (!clanChannel.getOwner().equals("help") && clanChannel.activeSize() >= 80) {
			player.message("Connection was refused: Channel currently full!");
			return;
		}
		if (clanChannel.isBanned(player.credentials.username)) {
			player.message("Connection was refused: Currently banned from channel!");
			return;
		}
		if (clanChannel.getDetails().type == ClanType.IRON_MAN && !player.getGameMode().isIronman()) {
			player.message("Only Iron-man accounts can join this clan!");
			return;
		}
		clanChannel.connect(player);
	}

	/** Handles player joining a clan channel. */
	public static boolean disconnect(Player player, boolean logout) {
		if (player == null || player.clanChannel == null)
			return false;
		player.clanChannel.disconnect(player.credentials.username, logout);
		return true;
	}

	public static void manage(Player player) {
		ClanChannel channel = player.clanChannel;
		Optional<ClanMember> member = channel.getMember(player.credentials.username);
		member.ifPresent(m -> {
			if (!channel.canManage(m)) {
				player.message("You do not have sufficient privileges to manage this clan!");
				return;
			}
			player.send(new InterfaceStringPacket("" + channel.getName(), 42102));
			player.send(new InterfaceStringPacket("" + channel.getTag(), 42104));
			player.send(new InterfaceStringPacket("" + channel.getSlogan(), 42106));
			player.send(new InterfaceStringPacket("" + channel.getPassword(), 42108));
			player.send(new InterfaceStringPacket(channel.getDetails().type.getIcon() + "" + channel.getDetails().type.getName(),
					42110));
			player.send(new InterfaceStringPacket(
					channel.getManagement().getRank(ENTER_RANK_INDEX) + " " + channel.getManagement().getEnter(),
					42112));
			player.send(new InterfaceStringPacket(
					channel.getManagement().getRank(TALK_RANK_INDEX) + " " + channel.getManagement().getTalk(), 42114));
			player.send(new InterfaceStringPacket(
					channel.getManagement().getRank(MANAGE_RANK_INDEX) + " " + channel.getManagement().getManage(),
					42116));
			player.send(new ConfigPacket(326, channel.getManagement().locked ? 1 : 0));

			player.send(new ItemsOnInterfacePacket(42126, channel.getShowcaseItems()));
			player.getInterfaceManager().setSidebar(TabInterface.CLAN_CHAT, 42000);
		});
	}

	/** Attempts a connection to the clan channel. */
	boolean attemptConnection(Player player, ClanMember member) {
		if (member.rank.equals(ClanRank.MEMBER)) {
			if(player.relations.isFriendWith(channel.getOwner()))
				member.rank = ClanRank.FRIEND;
			if (player.getRights() == Rights.ADMINISTRATOR)
				member.rank = ClanRank.SYSTEM;
		}

		if (!channel.canEnter(member)) {
			player.message("Connection was refused: No required permission!");
			return false;
		}

		if (channel.isLocked() && !channel.canManage(member)) {
			player.message("Connection was refused: Clan is currently locked!");
			return false;
		}

		return true;
	}

	/** Handles entering a clan channel password. */
	boolean testPassword(Player player, ClanMember member) {
		if (!channel.hasPassword() || channel.canManage(member)) {
			return false;
		}
		AtomicBoolean correct = new AtomicBoolean(false);

		player.send(new EnterNamePacket("Enter the password:", input -> () -> {
			if (!channel.isPassword(input)) {
				player.message("You have entered an invalid clan password.");
			} else {
				player.message("Password was accepted!");
				channel.establish(player, member);
				correct.set(true);
			}

		}));
		return correct.get();
	}

	/** Handles connecting to the clan channel. */
	void connected(ClanMember member) {
		updateMemberList(member);
		member.player.ifPresent(player -> {
			player.message("Now talking in clan chat <col=FFFF64><shad=0>"
					+ StringUtil.formatName(channel.getName()) + "</shad></col>.");
			player.message("To talk, start each line of chat with the / symbol.");
		});
	}

	/** The sends the connection warning for hopping clans. */
	void sendConnectionWarning(Player player, ClanMember member) {
		player.getDialogueBuilder().append(new StatementDialogue("-<col=FF0000>WARNING</col>-", "Contributing to this clan or gaining a rank will remove",
				"all of your progress from your current clan <col=FF4444>" + player.lastClan + "</col>.",
				"Are you sure you want to join?"),

				new OptionDialogue(t -> {
						if(t == OptionDialogue.OptionType.FIRST_OPTION)
							channel.add(player, member);
						else
							player.clanChannel = null;
						player.closeWidget();
					},"Yes, I accept", "No, I don't want to risk it."));
	}

	public static void manageMember(Player player, int button) {
		ClanChannel channel = player.clanChannel;
		if (channel == null)
			return;
		channel.getMember(player.credentials.username).ifPresent(member -> {
			if (!channel.canManage(member))
				return;

			List<ClanMember> sorted = new ArrayList<>(channel.getActiveMembers());
			//sorted.sort(player.settings.clanMemberComporator);

			int ordinal = ClanUtility.getRankOrdinal(button);
			if (ordinal >= sorted.size())
				return;

			ClanMember other = sorted.get(ordinal);
			if (other != null && !other.name.equalsIgnoreCase(player.credentials.username)
					&& other.rank.lessThan(ClanRank.LEADER)) {
				//player.attributes.set(PlayerAttributes.CLAN_RANK_MEMBER, other);
				player.send(new InterfaceStringPacket(other.name, 43606));
				player.getInterfaceManager().open(43600);
			}
		});
	}

	/** Handles updating the clan channel member list. */
	public void updateMemberList(ClanMember member) {
		Player player = member.player.orElse(null);
		
		if (player == null) {
		//	System.out.println("player is null..");
			return; 
		}
		
		int size = Math.max(channel.size(), 10);
		player.send(new InterfaceStringPacket("Talking in: <col=F7DC6F>" + StringUtil.formatName(channel.getName()), 33502));
		player.send(new ConfigPacket(393, channel.lootshareEnabled() ? 1 : 0));
		player.send(new ScrollBarPacket(33530, size * 22));

		List<ClanMember> members = new LinkedList<>();
		members.addAll(channel.getMembers());
		//members.sort(player.settings.clanMemberComporator);

		Iterator<ClanMember> iterator = members.iterator();
		boolean tooltip = channel.canManage(member);
		int string = 33532;

		for (int index = 0; index < size * 3; index++) {
			ClanMember nextMember = null;
			if (iterator.hasNext()) {
				nextMember = iterator.next();
			}
			if (nextMember == null || (!nextMember.player.isPresent() && index / 3 >= members.size())) {
				player.send(new InterfaceStringPacket("", string++));
				player.send(new InterfaceStringPacket("", string));
				player.send(new TooltipPacket("", string++));
				player.send(new InterfaceStringPacket("", string++));
				string++;
			} else if (nextMember.player.isPresent()) {
				player.send(new InterfaceStringPacket(nextMember.rank.getString(), string++));
				player.send(new InterfaceStringPacket(nextMember.name, string));
				player.send(new TooltipPacket(
						tooltip ? (nextMember.name.equals(player.credentials.username) ? "" : "Manage " + nextMember.name) : "",
						string++));
				player.send(new InterfaceStringPacket("#" + channel.getDetails().getClanRank(nextMember), string++));
				string++;
			}
		}
	}

	/** Handles cleaning the clan tab itemcontainer. */
	public static void clean(Player player) {
		player.send(new InterfaceStringPacket("Talking in: <col=F7DC6F>None", 33502));
		for (int i = 0; i < 99; i += 4) {
			player.send(new InterfaceStringPacket("", 33531 + (i + 1)));
			player.send(new InterfaceStringPacket("", 33531 + (i + 2)));
			player.send(new InterfaceStringPacket("", 33531 + (i + 3)));
		}
		player.send(new ScrollBarPacket(33530, 11 * 22));
	}

}
