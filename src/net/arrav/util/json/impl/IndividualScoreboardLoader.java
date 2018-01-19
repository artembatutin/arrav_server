package net.arrav.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.arrav.content.scoreboard.PlayerScoreboardStatistic;
import net.arrav.content.scoreboard.ScoreboardManager;
import net.arrav.util.json.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all individual scoreboard statistics.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class IndividualScoreboardLoader extends JsonLoader {
	
	/**
	 * Constructs a new {@link IndividualScoreboardLoader}.
	 */
	public IndividualScoreboardLoader() {
		super("./data/def/score/individual_killstreaks.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		String username = reader.get("username").getAsString();
		int highestKillstreak = reader.get("highest-killstreak").getAsInt();
		int currentKillstreak = reader.get("current-killstreak").getAsInt();
		int kills = reader.get("kills").getAsInt();
		int deaths = reader.get("deaths").getAsInt();
		ScoreboardManager.get().getPlayerScoreboard().put(username, new PlayerScoreboardStatistic(username, highestKillstreak, currentKillstreak, kills, deaths));
	}
	
}
