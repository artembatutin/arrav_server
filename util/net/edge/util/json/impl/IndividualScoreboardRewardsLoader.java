package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.MutableNumber;
import net.edge.util.json.JsonLoader;
import net.edge.world.World;

/**
 * The {@link JsonLoader} implementation that loads all individual scoreboard rewards.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class IndividualScoreboardRewardsLoader extends JsonLoader {

	/**
	 * Constructs a new {@link IndividualScoreboardRewardsLoader}.
	 */
	public IndividualScoreboardRewardsLoader() {
		super("./data/json/scoreboard/individual_killstreak_rewards.json");
	}

	@Override
	public void load(JsonObject reader, Gson builder) {
		String username = reader.get("username").getAsString();
		int amount = reader.get("amount").getAsInt();
		
		World.getScoreboardManager().getPlayerScoreboardRewards().put(username, new MutableNumber(amount));
		
	}

}
