package com.rageps.content.skill.summoning.familiar.impl;

import com.rageps.content.dialogue.Conversation;
import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.dialogue.impl.PlayerDialogue;
import com.rageps.content.dialogue.test.DialogueAppender;
import com.rageps.content.skill.summoning.familiar.ability.BeastOfBurden;
import com.rageps.content.skill.summoning.familiar.passive.PassiveAbility;
import com.rageps.content.skill.summoning.SummoningData;
import com.rageps.content.skill.summoning.familiar.Familiar;
import com.rageps.content.skill.summoning.familiar.FamiliarAbility;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * Represents the Spirit terrorbird familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SpiritTerrorbird extends Familiar {
	
	/**
	 * Constructs a new {@link SpiritTerrorbird}.
	 */
	public SpiritTerrorbird() {
		super(SummoningData.SPIRIT_TERRORBIRD);
	}
	
	private final BeastOfBurden ability = new BeastOfBurden(12);
	
	@Override
	public FamiliarAbility getAbilityType() {
		return ability;
	}
	
	@Override
	public Optional<PassiveAbility> getPassiveAbility() {
		return Optional.empty();
	}
	
	@Override
	public boolean isCombatic() {
		return false;
	}
	
	@Override
	public void interact(Player player, Mob mob, int id) {
		if(id == 1) {
			player.getDialogueBuilder().send(RandomUtils.random(CONVERSATION));
		}
	}
	
	private static final Conversation[] CONVERSATION = new Conversation[]{new Conversation() {
		@Override
		public void send(Player player, int index) {

		}
		
		@Override
		public DialogueAppender dialogues(Player player) {
			DialogueAppender app = new DialogueAppender(player);
			app.chain(new NpcDialogue(6794, "This is a fun little walk."));
			app.chain(new PlayerDialogue("Why do I get the feeling you'll change your tune when I start", "loading you up with items?"));
			return app;
		}
	}, new Conversation() {
		@Override
		public void send(Player player, int index) {

		}
		
		@Override
		public DialogueAppender dialogues(Player player) {
			DialogueAppender app = new DialogueAppender(player);
			app.chain(new NpcDialogue(6794, "I can keep this up for hours."));
			app.chain(new PlayerDialogue("I'm glad, as we still have plenty of time to go."));
			return app;
		}
	}, new Conversation() {
		@Override
		public void send(Player player, int index) {

		}
		
		@Override
		public DialogueAppender dialogues(Player player) {
			DialogueAppender app = new DialogueAppender(player);
			app.chain(new NpcDialogue(6794, "Are we going to visit a bank soon?"));
			app.chain(new PlayerDialogue("I'm not sure, you still have plenty of room for more stuff."));
			app.chain(new NpcDialogue(6794, "Just don't leave it too long, okay?"));
			return app;
		}
	}, new Conversation() {
		@Override
		public void send(Player player, int index) {

		}
		
		@Override
		public DialogueAppender dialogues(Player player) {
			DialogueAppender app = new DialogueAppender(player);
			app.chain(new NpcDialogue(6794, "Can we go to a bank now?"));
			app.chain(new PlayerDialogue("Just give me a little longer, okay?"));
			app.chain(new NpcDialogue(6794, "That's what you said last time!"));
			app.chain(new PlayerDialogue("Did I?"));
			app.chain(new NpcDialogue(6794, "Yes!"));
			app.chain(new PlayerDialogue("Well, I mean it this time, promise."));
			return app;
		}
	}, new Conversation() {
		@Override
		public void send(Player player, int index) {

		}
		
		@Override
		public DialogueAppender dialogues(Player player) {
			DialogueAppender app = new DialogueAppender(player);
			app.chain(new NpcDialogue(6794, "So..heavy..."));
			app.chain(new PlayerDialogue("I knew you'd change your tune once you started carrying things."));
			app.chain(new NpcDialogue(6794, "Can we go bank this stuff now?"));
			app.chain(new PlayerDialogue("Sure. You do look like you're about to collapse."));
			return app;
		}
	}};
	
}
