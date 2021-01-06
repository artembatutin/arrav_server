package com.rageps.content;

import com.rageps.GameConstants;
import com.rageps.content.achievements.Achievement;
import com.rageps.content.achievements.AchievementHandler;
import com.rageps.command.impl.RedeemCommand;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.impl.StatementDialogue;
import com.rageps.content.market.MarketCounter;
import com.rageps.content.scoreboard.PlayerScoreboardStatistic;
import com.rageps.content.scoreboard.ScoreboardManager;
import com.rageps.content.wilderness.WildernessActivity;
import com.rageps.util.Utility;
import com.rageps.world.World;
import com.rageps.action.impl.ButtonAction;
import com.rageps.net.packet.out.SendClearText;
import com.rageps.net.packet.out.SendEnterName;
import com.rageps.net.packet.out.SendLink;
import com.rageps.net.packet.out.SendMobDrop;
import com.rageps.util.TextUtils;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * The enumerated type whose elements represent functionality for the quest tab.
 * @author Artem Batutin
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum PlayerPanel {
	QUICKIES,
	COMMUNITY() {
		@Override
		public void onClick(Player player) {
			player.out(new SendLink("community/"));
		}
	},
	DISCORD() {
		@Override
		public void onClick(Player player) {
			player.out(new SendLink("discord"));
		}
	},
	VOTE() {
		@Override
		public void onClick(Player player) {
			player.getDialogueBuilder().append(new OptionDialogue(t -> {
				if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
					player.out(new SendLink("vote"));
					player.closeWidget();
				} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
					player.out(new SendEnterName("Auth code:", s -> () -> {
						try {
							new RedeemCommand().execute(player, new String[]{"", s}, "");
							player.closeWidget();
						} catch(Exception e) {
							e.printStackTrace();
						}
					}));
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
			player.out(new SendLink("store/"));
		}
	},
	EXP_LOCK() {
		@Override
		public void onClick(Player player) {
			player.playerData.lockedXP = !player.playerData.lockedXP;
			player.message("Your experience is now : " + (player.playerData.lockedXP ? "Locked" : "Unlocked"));
			this.refresh(player, "@or3@ - Experience Lock: @yel@" + (player.playerData.lockedXP ? "@gre@yes" : "@red@no"));
		}
	},
	NPC_TOOL() {
		@Override
		public void onClick(Player player) {
			player.out(new SendMobDrop(0, null));
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
			if(World.get().getStaffCount() == 0) {
				player.message("There is currently no staff online to assist you.");
				player.message("You can post a thread on our forums in the support section.");
			} else {
				player.getDialogueBuilder().append(new StatementDialogue("Are you requesting staff assistance?"), new OptionDialogue(t -> {
					if(t == OptionDialogue.OptionType.FIRST_OPTION) {
						if(!World.get().getPlayers().isEmpty()) {
							Player p;
							Iterator<Player> it = World.get().getPlayers().iterator();
							while((p = it.next()) != null) {
								if(p.getRights().isStaff())
									p.message(player.getFormatUsername() + " is requesting assistance. Do ::assist " + player.credentials.username + " to help him.");
							}
						}
						player.message("A staff member should contact you shortly.");
					}
					player.closeWidget();
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
					player.closeWidget();
					player.out(new SendEnterName("Your new password to set:", s -> () -> {
						player.credentials.password = s;
						player.message("You have successfully changed your password. Log out to save it.");
						PlayerPanel.PASSWORD.refresh(player, "@or3@ - Password: " + TextUtils.passwordCheck(s));
					}));
				} else if(t == OptionDialogue.OptionType.SECOND_OPTION) {
					player.closeWidget();
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
					player.closeWidget();
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
			player.message("You have voted " + player.totalVotes + "x for "+World.get().getEnvironment().getName()+".");
		}
	},
	
	EMPTY1,
	MONSTER_HEADER,
	HIGHEST_KILLSTREAK,
	CURRENT_KILLSTREAK,
	TOTAL_PLAYER_KILLS,
	TOTAL_PLAYER_DEATHS,
	TOTAL_NPC_KILLS,
	TOTAL_NPC_DEATHS,
	
	EMPTY2,
	PVP_SCOREBOARD_STATISTICS,
	PVP_HIGHEST_KILLSTREAKS,
	PVP_CURRENT_KILLSTREAKS,
	PVP_KILLS,
	PVP_DEATHS();
	
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
		player.out(new SendClearText(16026, 100));
		PlayerPanel.QUICKIES.refresh(player, "@or1@Quickies @or3@[clickable]@or1@:");
		PlayerPanel.COMMUNITY.refresh(player, "@or3@ - Forums");
		PlayerPanel.DISCORD.refresh(player, "@or3@ - Discord");
		PlayerPanel.VOTE.refresh(player, "@or3@ - Vote points: @yel@" + player.votePoints + " points", true);
		PlayerPanel.STORE.refresh(player, "@or3@ - Store");
		PlayerPanel.EXP_LOCK.refresh(player, "@or3@ - Experience Lock: @yel@" + (player.playerData.lockedXP ? "@gre@yes" : "@red@no"));
		PlayerPanel.NPC_TOOL.refresh(player, "@or3@ - Monster Database");
		
		PlayerPanel.SERVER_STATISTICS.refresh(player, "@or1@Server Information:");
		PlayerPanel.UPTIME.refresh(player, "@or2@ - Uptime: @yel@" + Utility.timeConvert(World.getRunningTime().elapsedTime(TimeUnit.MINUTES)));
		PlayerPanel.PLAYERS_IN_WILD.refresh(player, "@or2@ - Players in wild: @yel@" + WildernessActivity.getPlayers().size());
		PlayerPanel.STAFF_ONLINE.refresh(player, "@or3@ - Staff online: @yel@" + World.get().getStaffCount(), true);
		PlayerPanel.PLAYER_STATISTICS.refresh(player, "@or1@Player Information:");
		
		PlayerPanel.EMPTY.refresh(player, "");
		PlayerPanel.USERNAME.refresh(player, "@or2@ - Username: @yel@" + player.credentials.formattedUsername);
		PlayerPanel.PASSWORD.refresh(player, "@or3@ - Password: " + TextUtils.capitalize(TextUtils.passwordCheck(player.credentials.password)));
		PlayerPanel.RANK.refresh(player, "@or2@ - Rank: @yel@" + TextUtils.capitalize(player.getRights().toString()));
		PlayerPanel.IRON.refresh(player, "@or3@ - Iron man: @yel@" + (player.isIronMan() ? "@gre@yes" : "@red@no"), true);
		PlayerPanel.SLAYER_POINTS.refresh(player, "@or2@ - Slayer points: @yel@" + player.getSlayerPoints(), true);
		PlayerPanel.SLAYER_TASK.refresh(player, "@or2@ - Slayer task: @yel@" + (player.getSlayer().isPresent() ? (player.getSlayer().get().toString()) : "none"));
		PlayerPanel.SLAYER_COUNT.refresh(player, "@or2@ - Completed tasks: @yel@" + player.getAttributeMap().getInt(PlayerAttributes.SLAYER_TASKS));
		PlayerPanel.PEST_POINTS.refresh(player, "@or2@ - Pest points: @yel@" + player.getPest());
		PlayerPanel.TOTAL_VOTES.refresh(player, "@or2@ - Total votes: @yel@" + player.totalVotes);
		
		PlayerPanel.MONSTER_HEADER.refresh(player, "@or1@Killing Statistics:");
		//PlayerPanel.HIGHEST_KILLSTREAK.refresh(player, "@or2@ - Highest killstreak: @yel@" + player.getHighestKillstreak().get());
		//PlayerPanel.CURRENT_KILLSTREAK.refresh(player, "@or2@ - Current killstreak: @yel@" + player.getCurrentKillstreak().get());
		//PlayerPanel.TOTAL_PLAYER_KILLS.refresh(player, "@or2@ - Killed players: @yel@" + player.getPlayerKills().get());
		//PlayerPanel.TOTAL_PLAYER_DEATHS.refresh(player, "@or2@ - Killed by players: @yel@" + player.getDeathsByPlayer().get());
		PlayerPanel.TOTAL_NPC_KILLS.refresh(player, "@or2@ - Mobs killed: @yel@" + player.getNpcKills().get());
		PlayerPanel.TOTAL_NPC_DEATHS.refresh(player, "@or2@ - Killed by mobs: @yel@" + player.getDeathsByNpc().get());
		
		PlayerPanel.EMPTY2.refresh(player, "");
		PlayerScoreboardStatistic s = ScoreboardManager.get().getPlayerScoreboard().get(player.getFormatUsername());
		PlayerPanel.PVP_SCOREBOARD_STATISTICS.refresh(player, "@or1@Scoreboard Statistics:");
		PlayerPanel.PVP_HIGHEST_KILLSTREAKS.refresh(player, "@or2@ - Highest killstreak: @yel@" + (s == null ? 0 : s.getHighestKillstreak()));
		PlayerPanel.PVP_CURRENT_KILLSTREAKS.refresh(player, "@or2@ - Current killstreak: @yel@" + (s == null ? 0 : s.getCurrentKillstreak()));
		PlayerPanel.PVP_KILLS.refresh(player, "@or2@ - Kills: @yel@" + (s == null ? 0 : s.getKills()));
		PlayerPanel.PVP_DEATHS.refresh(player, "@or2@ - Death: @yel@" + (s == null ? 0 : s.getDeaths()));
		
		//achievements
		for(Achievement a : Achievement.VALUES) {
			AchievementHandler.update(player, a);
		}
	}
	
	/**
	 * Sets up event clicks.
	 */
	public static void action() {
		for(PlayerPanel p : PlayerPanel.values()) {
			ButtonAction e = new ButtonAction() {
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
		if(!World.get().getPlayers().isEmpty()) {
			Player player;
			Iterator<Player> it = World.get().getPlayers().iterator();
			while((player = it.next()) != null) {
				refresh(player, update);
			}
		}
	}
	
	/**
	 * Refreshes the tab asset for a specified player
	 * @param player the player we're refreshing this {@code enumerator} for.
	 * @param text the new string to set.
	 * @param skipCheck The condition if we should skip the check.
	 */
	public void refresh(Player player, String text, boolean skipCheck) {
		player.interfaceText(16026 + ordinal(), text, skipCheck);
	}
	
	/**
	 * Refreshes the tab asset for a specified player
	 * @param player the player we're refreshing this {@code enumerator} for.
	 * @param text the new string to set.
	 */
	public void refresh(Player player, String text) {
		player.interfaceText(16026 + ordinal(), text, false);
	}
}
