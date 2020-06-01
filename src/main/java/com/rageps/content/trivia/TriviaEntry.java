package com.rageps.content.trivia;

import com.rageps.content.achievements.Achievement;
import com.rageps.world.World;
import com.rageps.world.text.ColorConstants;
import com.rageps.world.text.MessageBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.rageps.GameConstants;
import com.rageps.util.TextUtils;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 3-6-2017.
 */
public final class TriviaEntry {
	
	/**
	 * The current trivia question being asked.
	 */
	public TriviaData current = TriviaData.random();
	
	/**
	 * The list of attempted answers.
	 */
	public final ObjectList<String> attempted_answers = new ObjectArrayList<>();
	
	/**
	 * Sends a message to the entire world asking the trivia question.
	 */
	public void ask() {
		MessageBuilder mb = new MessageBuilder();
		mb.appendPrefix("Trivia Bot", ColorConstants.BLACK, ColorConstants.CYAN).append(": ").
				append(current.question, ColorConstants.BLUE);
		World.get().message(mb.toString());
	}
	
	/**
	 * Sends a reminder to the world.
	 */
	public void reminder() {
		MessageBuilder mb = new MessageBuilder();
		mb.appendPrefix("Trivia Bot", ColorConstants.BLACK, ColorConstants.CYAN).append(": ").
				append("The last question hasn't been answered yet!", ColorConstants.BLUE);
		World.get().message(mb.toString());
		ask();
	}
	
	/**
	 * The trivia question that should be send to the player on login.
	 * @param player the player to send the question to.
	 */
	public void onLogin(Player player) {
		if(current != null) {
			MessageBuilder mb = new MessageBuilder();
			mb.appendPrefix("Trivia Bot", ColorConstants.BLACK, ColorConstants.CYAN).append(": ").
					append(current.question, ColorConstants.BLUE);
			player.message(mb.toString());
		}
	}
	
	/**
	 * Attempts to answer the current trivia question.
	 * @param player the player attempting to answer.
	 * @param answer the answer that the player has submit.
	 */
	public void answer(Player player, String answer) {
		if(current == null) {
			player.message("@red@[Trivia Bot]: @blu@The last question has been answered or has expired already.");
			return;
		}
		if(Arrays.stream(GameConstants.BAD_STRINGS).anyMatch(answer::contains)) {
			player.message("@red@[Trivia Bot]: @blu@You think you're funny, don't you? Guess what? You ain't.");
			return;
		}
		
		answer = answer.toLowerCase();
		for(String a : current.answers) {
			if(answer.equals(a)) {
				answered(player, answer);
				return;
			}
		}
		
		if(!attempted_answers.contains(answer)) {
			attempted_answers.add(answer);
		}
		
		if(RandomUtils.inclusive(5) == 0) {
			player.forceChat("Whoops! I just entered a wrong trivia answer!");
		}
		
		player.message("@red@[Trivia Bot]: @blu@Sorry, the answer you have entered is incorrect! Try again!");
	}
	
	/**
	 * The functionality that occurs when a player successfully answers a trivia question.
	 * @param player the player whom answered the trivia question.
	 * @param answer the answer that was correct.
	 */
	private void answered(Player player, String answer) {
		World.get().message("@red@[Trivia Bot]: @bla@" + player.getFormatUsername() + "@blu@ has answered the question correctly! @red@Answer: " + answer + "@blu@.");
		
		if(!attempted_answers.isEmpty()) {
			List<String> attemptedAnswers = attempted_answers.stream().filter(a -> Arrays.stream(GameConstants.BAD_STRINGS).noneMatch(a::contains)).collect(Collectors.toList());
			World.get().message("@red@[Trivia Bot]: @blu@Attempted answers: @red@" + attemptedAnswers.toString() + "@blu@!");
		}
		Achievement.TRIVIABOT.inc(player);
		int amount = RandomUtils.inclusive(100_000, 500_000);
		player.getBank().add(0, new Item(995, amount));
		player.message("@red@[Trivia Bot]: @blu@" + TextUtils.formatPrice(amount) + " coins were added into your bank.");
		reset();
	}
	
	/**
	 * Resets the current question and it's attempted answers.
	 */
	public void reset() {
		current = null;
		attempted_answers.clear();
	}
}

