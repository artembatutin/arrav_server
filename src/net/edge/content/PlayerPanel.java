package net.edge.content;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.content.commands.impl.RedeemCommand;
import net.edge.content.market.MarketCounter;
import net.edge.content.wilderness.WildernessActivity;
import net.edge.event.impl.ButtonEvent;
import net.edge.game.GameConstants;
import net.edge.util.TextUtils;
import net.edge.util.Utility;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.content.scoreboard.PlayerScoreboardStatistic;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * The enumerated type whose elements represent functionality for the quest tab.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum PlayerPanel {
	TAB,
	TOOLS,
	COMMUNITY() {
		@Override
		public void onClick(Player player) {
			player.getMessages().sendLink("community/");
		}
	},
	DISCORD() {
		@Override
		public void onClick(Player player) {
			player.getMessages().sendLink("discord");
		}
	},
	VOTE() {
		@Override
		public void onClick(Player player) {
			player.getDialogueBuilder().append(new OptionDialogue(t -> {
				if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
					player.getMessages().sendLink("vote");
					player.getMessages().sendCloseWindows();
				} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
					player.getMessages().sendEnterName("Auth code:", s -> () -> {
						try {
							new RedeemCommand().execute(player, new String[] {"", s}, "");
							player.getMessages().sendCloseWindows();
						} catch(Exception e) {
							e.printStackTrace();
						}
					});
				}
				if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
					MarketCounter.getShops().get(27).openShop(player);
				}
			}, "Vote", "Redeem", "Vote shop"));
			
		}
	},
	STORE() {
		@Override
		public void onClick(Player player) {
			player.getMessages().sendLink("store/");
		}
	},
	NPC_TOOL() {
		@Override
		public void onClick(Player player) {
			player.getMessages().sendNpcInformation(0, null);
		}
	},
	TOOL3,
	
	SERVER_STATISTICS,
	UPTIME,
	PLAYERS_ONLINE() {
		@Override
		public void onClick(Player player) {
			player.message("There is currently " + World.get().getPlayers().size() + " players online.");
		}
	},
	STAFF_ONLINE() {
		@Override
		public void onClick(Player player) {
			List<Player> staff = World.get().getPlayers().findAll(p -> p != null && p.getRights().isStaff());
			if(staff.isEmpty()) {
				player.message("There is currently no staff online to assist you.");
				player.message("You can post a thread on our forums in the support section.");
			} else {
				player.getDialogueBuilder().append(new StatementDialogue("Are you requesting staff assistance?"), new OptionDialogue(t -> {
					if(t == OptionDialogue.OptionType.FIRST_OPTION) {
						staff.forEach(s -> s.message(player.getFormatUsername() + " is requesting assistance. Do ::assist " + player.getUsername() + " to help him."));
						player.message("A staff member should contact you shortly.");
					}
					player.getMessages().sendCloseWindows();
				}, "Yes please!", "No thanks."));
			}
		}
	},
	PLAYERS_IN_WILD,
	
	EMPTY,
	
	PLAYER_STATISTICS,
	USERNAME,
	PASSWORD() {
		@Override
		public void onClick(Player player) {
			player.getDialogueBuilder().append(new StatementDialogue("You sure you want to change your password?"), new OptionDialogue(t -> {
				if(t == OptionDialogue.OptionType.FIRST_OPTION) {
					player.getMessages().sendCloseWindows();
					player.getMessages().sendEnterName("Your new password to set:", s -> () -> {
						player.setPassword(s);
						player.message("You have successfully changed your password. Log out to save it.");
						PlayerPanel.PASSWORD.refresh(player, "@or3@ - Password: " + TextUtils.passwordCheck(s));
					});
				} else if(t == OptionDialogue.OptionType.SECOND_OPTION) {
					player.getMessages().sendCloseWindows();
				}
			}, "Yes please!", "No thanks."));
		}
	},
	RANK,
	IRON() {
		@Override
		public void onClick(Player player) {
			if(player.isIronMan()) {
				player.getDialogueBuilder().append(new StatementDialogue("You want to quit the iron man mode?"), new OptionDialogue(t -> {
					if(t == OptionDialogue.OptionType.FIRST_OPTION) {
						player.setIron(0, true);
						player.teleport(GameConstants.STARTING_POSITION);
					}
					player.getMessages().sendCloseWindows();
				}, "Yes, want to be a regular player.", "No, I want to keep the iron man mode."));
				return;
			}
			player.message("You can only select iron man mode in the beginning.");
		}
	},
	SLAYER_POINTS,
	SLAYER_TASK,
	SLAYER_COUNT,
	PEST_POINTS,
	TOTAL_VOTES() {
		@Override
		public void onClick(Player player) {
			player.message("You have voted " + player.getTotalVotes() + "x for Edgeville.");
		}
	},
	
	EMPTY1,
	
	PVE_HEADER,
	
	HIGHEST_KILLSTREAK,
	CURRENT_KILLSTREAK,
	TOTAL_PLAYER_KILLS,
	TOTAL_PLAYER_DEATHS,
	TOTAL_NPC_KILLS,
	TOTAL_NPC_DEATHS,
	
	EMPTY2,
	
	INDIVIDUAL_SCOREBOARD_STATISTICS,
	
	INDIVIDUAL_HIGHEST_KILLSTREAKS,
	INDIVIDUAL_CURRENT_KILLSTREAKS,
	INDIVIDUAL_KILLS,
	INDIVIDUAL_DEATHS();
	
	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<PlayerPanel> VALUES = Sets.immutableEnumSet(EnumSet.allOf(PlayerPanel.class));
	
	/**
	 * The button identification.
	 */
	private final int buttonId;
	
	/**
	 * Constructs a new {@link PlayerPanel}.
	 */
	PlayerPanel() {
		this.buttonId = 62154 + ordinal();
	}
	
	/**
	 * Gets the button id of the line.
	 * @return the button id.
	 */
	public int getButtonId() {
		return buttonId;
	}
	
	/**
	 * Refreshes every tab for the specified {@code player}.
	 * @param player the player logging in.
	 */
	public static void refreshAll(Player player) {
		if(!player.isHuman())
			return;
		for(int i = 16016; i < 16016 + VALUES.size(); i++) {
			player.getMessages().sendString("", i);
		}
		PlayerPanel.TAB.refresh(player, "@or2@Informative @or1@- @or3@Clickable");
		PlayerPanel.TOOLS.refresh(player, "@or1@Quickies:");
		PlayerPanel.COMMUNITY.refresh(player, "@or3@ - Forums");
		PlayerPanel.DISCORD.refresh(player, "@or3@ - Discord");
		PlayerPanel.VOTE.refresh(player, "@or3@ - Vote points: @yel@" + player.getVotePoints() + " points");
		PlayerPanel.STORE.refresh(player, "@or3@ - Store");
		PlayerPanel.NPC_TOOL.refresh(player, "@or3@ - Monster Database");
		PlayerPanel.SERVER_STATISTICS.refresh(player, "@or1@Server Information:");
		PlayerPanel.UPTIME.refreshAll("@or2@ - Uptime: @yel@" + Utility.timeConvert(World.getRunningTime().elapsedTime(TimeUnit.MINUTES)));
		PlayerPanel.PLAYERS_IN_WILD.refreshAll("@or2@ - Players in wild: @yel@" + WildernessActivity.getPlayers().size());
		PlayerPanel.STAFF_ONLINE.refreshAll("@or3@ - Staff online: @yel@" + World.get().getStaffCount());
		PlayerPanel.PLAYER_STATISTICS.refresh(player, "@or1@Player Information:");
		
		PlayerPanel.EMPTY.refresh(player, "");
		PlayerPanel.USERNAME.refresh(player, "@or2@ - Username: @yel@" + TextUtils.capitalize(player.getUsername()));
		PlayerPanel.PASSWORD.refresh(player, "@or3@ - Password: " + TextUtils.capitalize(TextUtils.passwordCheck(player.getPassword())));
		PlayerPanel.IRON.refresh(player, "@or3@ - Iron man: @yel@" + (player.isIronMan() ? "@gre@yes" : "@red@no"));
		PlayerPanel.RANK.refresh(player, "@or2@ - Rank: @yel@" + TextUtils.capitalize(player.getRights().toString()));
		PlayerPanel.SLAYER_POINTS.refresh(player, "@or2@ - Slayer points: @yel@" + player.getSlayerPoints());
		PlayerPanel.SLAYER_TASK.refresh(player, "@or2@ - Slayer task: @yel@" + (player.getSlayer().isPresent() ? (player.getSlayer().get().toString()) : "none"));
		PlayerPanel.SLAYER_COUNT.refresh(player, "@or2@ - Completed tasks: @yel@" + player.getAttr().get("slayer_tasks").getInt());
		PlayerPanel.PEST_POINTS.refresh(player, "@or2@ - Pest points: @yel@" + player.getPest());
		PlayerPanel.TOTAL_VOTES.refresh(player, "@or2@ - Total votes: @yel@" + player.getTotalVotes());
		
		PlayerPanel.PVE_HEADER.refresh(player, "@or1@PvE Statistics:");
		PlayerPanel.HIGHEST_KILLSTREAK.refresh(player, "@or2@ - Highest Killstreak: @yel@" + player.getHighestKillstreak().get());
		PlayerPanel.CURRENT_KILLSTREAK.refresh(player, "@or2@ - Current Killstreak: @yel@" + player.getCurrentKillstreak().get());
		PlayerPanel.TOTAL_PLAYER_KILLS.refresh(player, "@or2@ - Total Players killed: @yel@" + player.getPlayerKills().get());
		PlayerPanel.TOTAL_PLAYER_DEATHS.refresh(player, "@or2@ - Total Player deaths: @yel@" + player.getDeathsByPlayer().get());
		PlayerPanel.TOTAL_NPC_KILLS.refresh(player, "@or2@ - Total Npcs killed: @yel@" + player.getNpcKills().get());
		PlayerPanel.TOTAL_NPC_DEATHS.refresh(player, "@or2@ - Total Npc deaths: @yel@" + player.getDeathsByNpc().get());
		
		PlayerPanel.EMPTY2.refresh(player, "");
		PlayerPanel.INDIVIDUAL_SCOREBOARD_STATISTICS.refresh(player, "@or1@Indiv. Scoreboard Statistics:");
		PlayerScoreboardStatistic s = World.getScoreboardManager().getPlayerScoreboard().get(player.getFormatUsername());
		PlayerPanel.INDIVIDUAL_HIGHEST_KILLSTREAKS.refresh(player, "@or2@ - Highest Killstreak: @yel@" + (s == null ? 0 : s.getHighestKillstreak()));
		PlayerPanel.INDIVIDUAL_CURRENT_KILLSTREAKS.refresh(player, "@or2@ - Current Killstreak: @yel@" + (s == null ? 0 : s.getCurrentKillstreak()));
		PlayerPanel.INDIVIDUAL_KILLS.refresh(player, "@or2@ - Players killed: @yel@" + (s == null ? 0 : s.getKills()));
		PlayerPanel.INDIVIDUAL_DEATHS.refresh(player, "@or2@ - Player deaths: @yel@" + (s == null ? 0 : s.getDeaths()));
	}
	
	/**
	 * Sets up event clicks.
	 */
	public static void event() {
		for(PlayerPanel p : PlayerPanel.values()) {
			ButtonEvent e = new ButtonEvent() {
				@Override
				public boolean click(Player player, int button) {
					p.onClick(player);
					return true;
				}
			};
			e.register(p.buttonId);
		}
	}
	
	/**
	 * The action to be done on the click.
	 * @param player the player doing the click.
	 */
	public void onClick(Player player) {
		//Empty
	}
	
	/**
	 * Refreshes the specified tab asset for all the players on the world.
	 * @param update the updated string for that tab.
	 */
	public void refreshAll(String update) {
		World.get().getPlayers().forEach(player -> refresh(player, update));
	}
	
	/**
	 * Refreshes the tab asset for a specified player
	 * @param player the player we're refreshing this {@code enumerator} for.
	 * @param text   the new string to set.
	 */
	public void refresh(Player player, String text) {
		player.getMessages().sendString(text, 16026 + ordinal());
	}
}
