package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.content.scoreboard.ScoreboardManager;
import com.rageps.util.MutableNumber;
import com.rageps.util.json.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all individual scoreboard rewards.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class IndividualScoreboardRewardsLoader extends JsonLoader {
	
	/**
	 * Constructs a new {@link IndividualScoreboardRewardsLoader}.
	 */
	public IndividualScoreboardRewardsLoader() {
		super("./data/def/score/individual_killstreak_rewards.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		String username = reader.get("username").getAsString();
		int amount = reader.get("amount").getAsInt();
		ScoreboardManager.get().getPlayerScoreboardRewards().put(username, new MutableNumber(amount));
	}
	
}
