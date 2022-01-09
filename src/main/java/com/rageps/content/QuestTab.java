package com.rageps.content;

import com.rageps.content.rewardslist.RewardListHandler;
import com.rageps.content.title.TitleManager;
import com.rageps.util.StringUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;

/**
 * An easy to use manager for sending all data, and interactions with the quest tab.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public class QuestTab {


	private String format(boolean parameter) {
		return parameter ? "@gre@Enabled" : "@ged@Disabled";
	}


	public static void updateQuestTab(Player player) {
		InformationTab.update(player);
	}

	private static  final int START_INFORMATION_STRING = 45772;
	public static final int TOOLS_START_BUTTON = -19714;

	public enum InformationTab {

		SERVER_UPTIME() {
			@Override
			public void test(Player player) {
				send(player, this, "Server Uptime: @gre@");
			}
		},
		STAFF_ONLINE() {@Override
        public void test(Player player){send(player, this, "Staff Online: @gre@");}},
		PLAYERS_ONLINE() {@Override
        public void test(Player player){send(player, this, "Players Online: @gre@"+ World.get().getPlayers().size());}},
		WELL() {@Override
        public void test(Player player){send(player, this, "Well: @gre@");}},
		WELL_PERK() {@Override
        public void test(Player player){send(player, this, "Well Perk: ");}},
		SHOOTING_STAR() {@Override
        public void test(Player player){send(player, this, "Shooting Star: ");}},
		EVIL_TREE() {@Override
        public void test(Player player){send(player, this, "Evil Tree: ");}},
		DOUBLE_EXP() {
			@Override
			public void test(Player player) {
			//	boolean active = GameSettings.BONUS_EXP;
			//	send(player, this, "Double Exp: "+ InformationTab.format(active)+(active ? "Active" : "Inactive"));
				}
			},
		DOUBLE_DROPS() {@Override
        public void test(Player player){send(player, this, "Double Drops: @gre@");}},

		USERNAME() {@Override
        public void test(Player player){send(player, this, "Username: @gre@"+player.getFormatUsername());}},
		RANK() {@Override
        public void test(Player player){send(player, this, "Player Rank: @gre@"+StringUtil.formatEnumString(player.getRights().name().toLowerCase()));}},
		MODE() {@Override
        public void test(Player player){send(player, this, "Game Mode: @gre@"+ StringUtil.formatEnumString(player.getGameMode()));}},
		EXP_MODIFIER() {@Override
        public void test(Player player){send(player, this, "Exp Modifier: @gre@");}},
		TOTAL_TIME() {@Override
        public void test(Player player){send(player, this, "Total Play Time: @gre@");}},
		SESSION_TIME() {@Override
        public void test(Player player){send(player, this, "Session Time: @gre@");}},
		CREATED_TIME() {@Override
        public void test(Player player){send(player, this, "Account Created: @gre@");}},
		SLAYER_TASK() {@Override
        public void test(Player player){send(player, this, "Slayer task: @gre@");}},
		SLAYER_POINTS() {@Override
        public void test(Player player){send(player, this, "Slayer Points: ");}},
		TOTAL_VOTES() {@Override
        public void test(Player player){send(player, this, "Total Votes: @gre@");}},
		VOTE_POINTS() {@Override
        public void test(Player player){send(player, this, "Vote Points: @gre@");}},
		DROP_BONUS() {@Override
        public void test(Player player){send(player, this, "Drop Bonus: @gre@"+player.getDropChanceHandler().getDropRate(true));}},
		DOUBLE_DROP_BONUS() {@Override
        public void test(Player player){send(player, this, "Double Drop Bonus: @gre@"+player.getDropChanceHandler().getDoubleDropRate(true));}},
		TRIVIA_POINTS() {@Override
        public void test(Player player){send(player, this, "Trivia Points: ");}},
		GAMBLE_WINS() {@Override
        public void test(Player player){send(player, this, "Gamble Wins: @gre@");}},
		GAMBLE_LOSSES() {@Override
        public void test(Player player){send(player, this, "Gamble Losses: @red@");}},

		;

		InformationTab(){}
		public abstract void test(Player player);

		public static InformationTab[] VALUES = values();


		public static void update(Player player) {
			for(InformationTab tab : VALUES) {
				tab.test(player);
			}
		}

		private static String format(boolean parameter) {
			return parameter ? "@gre@" : "@red@";
		}

		public static void send(Player player, InformationTab tab, String str) {
			int id = START_INFORMATION_STRING + tab.ordinal();

			if(id > 45780)//exmempt the account information string
				id++;

			//player.getPacketSender().sendString(id, str);
		}

	}

	public enum ToolsTab {

		DROPVIEW {
			@Override
			public void test(Player player) {
			//	DropLookup.open(player);
			}},
		DROP_SIMULATOR {
			@Override
			public void test(Player player) {
			//DropSimulator.open(player);
			}},
		TITLE_MANAGER {
			@Override
			public void test(Player player) {
				TitleManager.openInterface(player, 0);
			}},
		REWARDS_LIST {
			@Override
			public void test(Player player) {
				RewardListHandler.open(player);
			}},
		COLOURS_CUSTOMIZER {
			@Override
			public void test(Player player) {
			//player.getPacketSender().sendInterface(56700);
			}},
		DAILY_BOSS{
			@Override
			public void test(Player player) {

			}},
		DAILY_REWARDS {
			@Override
			public void test(Player player) {

			}},
		LOTTERY {
			@Override
			public void test(Player player) {

			}},
		COLLECTION_LOG {
			@Override
			public void test(Player player) {

			}},
		EQUIPMENT_VIEWER {
			@Override
			public void test(Player player) {
				//EquipmentViewHandler.open(player);
			}},




		;

		ToolsTab(){}

		public abstract void test(Player player);

		public static final ToolsTab[] VALUES = values();

		public static ToolsTab forOrdinal(int ordinal) {
			for(ToolsTab tab : VALUES) {
				if(tab.ordinal() == ordinal)
					return tab;
			}
			return DROPVIEW;
		}

	}



}