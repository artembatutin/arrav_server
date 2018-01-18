package net.edge.content.scoreboard;

import com.google.common.collect.ComparisonChain;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.action.impl.MobAction;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.net.packet.out.SendScore;
import net.edge.util.MutableNumber;
import net.edge.util.json.JsonSaver;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Comparator;
import java.util.Map.Entry;

import static net.edge.content.achievements.Achievement.PKER_OF_THE_WEEK;

/**
 * The manager for the scoreboards.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ScoreboardManager {
	
	/**
	 * This world's {@link ScoreboardManager} used to check scores.
	 */
	private static final ScoreboardManager SCOREBOARD_MANAGER = new ScoreboardManager();
	
	/**
	 * The mappings of the player scoreboard.
	 */
	private final Object2ObjectOpenHashMap<String, PlayerScoreboardStatistic> player_scoreboard = new Object2ObjectOpenHashMap<>();
	
	/**
	 * The mappings of the player rewards, where string is the users username and the mutable number
	 * the amount of edge-tokens to be given.
	 */
	private final Object2ObjectArrayMap<String, MutableNumber> player_scoreboard_rewards = new Object2ObjectArrayMap<>();
	
	/**
	 * Determines if the player score board has been reset.
	 */
	private boolean boardResetted = false;
	
	/**
	 * Serializes the individual scoreboard.
	 */
	public void serializeIndividualScoreboard() {
		JsonSaver scoreboard_statistics_saver = new JsonSaver();
		ObjectList<PlayerScoreboardStatistic> statistics = new ObjectArrayList<>(player_scoreboard.values());
		statistics.sort(new PlayerScoreboardComparator());
		if(statistics.size() > 10) {
			statistics = statistics.subList(0, 11);//it's exclusive.
		}
		for(PlayerScoreboardStatistic p : statistics) {
			scoreboard_statistics_saver.current().addProperty("username", p.getUsername());
			scoreboard_statistics_saver.current().addProperty("highest-killstreak", p.getHighestKillstreak().get());
			scoreboard_statistics_saver.current().addProperty("current-killstreak", p.getCurrentKillstreak().get());
			scoreboard_statistics_saver.current().addProperty("kills", p.getKills().get());
			scoreboard_statistics_saver.current().addProperty("deaths", p.getDeaths().get());
			scoreboard_statistics_saver.split();
		}
		scoreboard_statistics_saver.publish("./data/def/score/individual_killstreaks.json");
		
		JsonSaver scoreboard_rewards_saver = new JsonSaver();
		for(Entry<String, MutableNumber> entry : this.player_scoreboard_rewards.entrySet()) {
			String username = entry.getKey();
			int amount = entry.getValue().get();
			scoreboard_rewards_saver.current().addProperty("username", username);
			scoreboard_rewards_saver.current().addProperty("amount", amount);
			scoreboard_rewards_saver.split();
		}
		scoreboard_rewards_saver.publish("./data/def/score/individual_killstreak_rewards.json");
	}
	
	/**
	 * Sends the scoreboard to the player.
	 * @param player the player to send it to.
	 */
	public void sendPlayerScoreboardStatistics(Player player) {
		ObjectList<PlayerScoreboardStatistic> statistics = new ObjectArrayList<>(player_scoreboard.values());
		
		statistics.sort(new PlayerScoreboardComparator());
		if(statistics.size() > 10) {
			statistics = statistics.subList(0, 11);//it's exclusive.
		}
		
		for(int i = 0; i < statistics.size(); i++) {
			PlayerScoreboardStatistic stat = statistics.get(i);
			player.out(new SendScore(i, stat.getUsername(), stat.getKills().get(), stat.getDeaths().get(), stat.getCurrentKillstreak().get()));
		}
		player.widget(-12);
	}
	
	/**
	 * Resets all the statistics on the player scoreboard and rewards the top-3 players.
	 */
	public void resetPlayerScoreboard() {
		this.setBoardResetted(true);
		
		if(player_scoreboard.size() < 10) {
			World.get().message("There weren't enough players participating in the player scoreboard event.", true);
			World.get().message("The event has been extended by another week.", true);
			return;
		}
		
		ObjectList<PlayerScoreboardStatistic> statistics = new ObjectArrayList<>(player_scoreboard.values());
		
		statistics.sort(new PlayerScoreboardComparator());
		statistics.forEach(p -> {
			p.getCurrentKillstreak().set(0);
			p.getHighestKillstreak().set(0);
			p.getKills().set(0);
			p.getDeaths().set(0);
		});
		
		statistics = statistics.subList(0, 3);
		int points = 1200;//we divide by 2, so player 1 gets 600, 2 gets 300, 3 gets 150
		
		for(PlayerScoreboardStatistic p : statistics) {
			MutableNumber amount = player_scoreboard_rewards.putIfAbsent(p.getUsername(), new MutableNumber());
			amount.incrementAndGet(points = points / 2);
		}
		
		player_scoreboard.clear();
	}
	
	public static void action() {
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				DialogueAppender ap = new DialogueAppender(player);
				ap.chain(new NpcDialogue(npc.getId(), "Hello " + player.getFormatUsername() + ", what can I do for you today?"));
				ap.chain(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						ap.getBuilder().advance();
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						ap.getBuilder().go(3);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						ap.getBuilder().go(7);
					}
				}, "Who are you?", "What is the individual scoreboard?", "Claim pending rewards."));
				ap.chain(new PlayerDialogue("Who are you?"));
				ap.chain(new NpcDialogue(npc.getId(), "I am the scoreboard manager, I keep track of the ", "scoreboard of Main. Once a week on every monday, I", "reset the leaderboards and reward certain players on", "the rankings.").attachAfter(() -> ap
						.getBuilder()
						.go(-3)));
				ap.chain(new PlayerDialogue("What is the individual scoreboard?"));
				ap.chain(new NpcDialogue(npc.getId(), "The individual scoreboard, is a scoreboard for individuals.", "It keeps track of certain statistics for players however,", "whenever you die your killstreak does not reset."));
				ap.chain(new NpcDialogue(npc.getId(), "Instead, once every week on monday, all your individual ", "statistics will be reset and I will reward the top-3 players", "on the leaderboards."));
				ap.chain(new PlayerDialogue("Ah, I think I understand now.").attachAfter(() -> ap.getBuilder().go(-7)));
				
				ap.chain(new PlayerDialogue("I would like to claim my rewards."));
				if(ScoreboardManager.get().getPlayerScoreboardRewards().containsKey(player.getFormatUsername())) {
					PKER_OF_THE_WEEK.inc(player);
					player.getInventory().addOrBank(new Item(12852, ScoreboardManager.get().getPlayerScoreboardRewards().get(player.getFormatUsername()).get()));
					ScoreboardManager.get().getPlayerScoreboardRewards().remove(player.getFormatUsername());
					ap.chain(new NpcDialogue(npc.getId(), "Ah yeah, there were some rewards waiting for you however,", "they have been added to your inventory or have been", "banked.").attachAfter(() -> ap.getBuilder().go(-10)));
				} else {
					ap.chain(new NpcDialogue(npc.getId(), "There are no rewards waiting for you.").attachAfter(() -> ap.getBuilder().go(-9)));
					ap.start();
				}
				ap.start();
				return true;
			}
		};
		e.registerFirst(13926);
	}
	
	/**
	 * @return player scoreboard
	 */
	public Object2ObjectOpenHashMap<String, PlayerScoreboardStatistic> getPlayerScoreboard() {
		return player_scoreboard;
	}
	
	/**
	 * @return player scoreboard rewards.
	 */
	public Object2ObjectArrayMap<String, MutableNumber> getPlayerScoreboardRewards() {
		return player_scoreboard_rewards;
	}
	
	/**
	 * @return the resetPlayerScoreboardStatistic
	 */
	public boolean isBoardResetted() {
		return boardResetted;
	}
	
	/**
	 * @param boardResetted the boardResetted to set
	 */
	public void setBoardResetted(boolean boardResetted) {
		this.boardResetted = boardResetted;
	}
	
	/**
	 * The comparator which will compare the values.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private final class PlayerScoreboardComparator implements Comparator<PlayerScoreboardStatistic> {
		
		@Override
		public int compare(PlayerScoreboardStatistic arg0, PlayerScoreboardStatistic arg1) {
			ComparisonChain c = ComparisonChain.start().compare(arg1.getCurrentKillstreak().get(), arg0.getCurrentKillstreak().get()).compare(arg1.getKills().get(), arg0.getKills().get()).compare(arg0.getDeaths(), arg1.getDeaths());
			return c.result();
		}
	}
	
	/**
	 * Returns this world's {@link ScoreboardManager}.
	 */
	public static ScoreboardManager get() {
		return SCOREBOARD_MANAGER;
	}
	
}