package com.rageps.content.skill.summoning.familiar.impl;

import com.rageps.content.dialogue.Conversation;
import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.dialogue.impl.PlayerDialogue;
import com.rageps.content.dialogue.test.DialogueAppender;
import com.rageps.content.skill.summoning.familiar.ability.Fighter;
import com.rageps.content.skill.summoning.familiar.passive.PassiveAbility;
import com.rageps.content.skill.summoning.SummoningData;
import com.rageps.content.skill.summoning.familiar.Familiar;
import com.rageps.content.skill.summoning.familiar.FamiliarAbility;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * Represents the Bloated leech familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BloatedLeech extends Familiar {
	
	/**
	 * Constructs a new {@link BloatedLeech}.
	 */
	public BloatedLeech() {
		super(SummoningData.BLOATED_LEECH);
	}
	
	private final Fighter ability = new Fighter();
	
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
		return true;
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
			app.chain(new NpcDialogue(6843, "I'm afraid it's going to have to come off."));
			app.chain(new PlayerDialogue("What is?"));
			app.chain(new NpcDialogue(6843, "Never mind. Trust me, I'm almost a doctor."));
			app.chain(new PlayerDialogue("I think i'll get a second opinion."));
			return app;
		}
	}, new Conversation() {
		@Override
		public void send(Player player, int index) {

		}
		
		@Override
		public DialogueAppender dialogues(Player player) {
			DialogueAppender app = new DialogueAppender(player);
			app.chain(new NpcDialogue(6843, "You're in a critical condition."));
			app.chain(new PlayerDialogue("Is it terminal?"));
			app.chain(new NpcDialogue(6843, "Not yet, Let me get a better look and I'll see what I can do about it."));
			app.chain(new PlayerDialogue("There are two ways to take that...and I think I'll err on the side of caution."));
			return app;
		}
	}, new Conversation() {
		@Override
		public void send(Player player, int index) {

		}
		
		@Override
		public DialogueAppender dialogues(Player player) {
			DialogueAppender app = new DialogueAppender(player);
			app.chain(new NpcDialogue(6843, "Let's get a look at that brain of yours."));
			app.chain(new PlayerDialogue("What? My brains stay inside my head, thanks."));
			app.chain(new NpcDialogue(6843, "That's ok, I can just drill a hole."));
			app.chain(new PlayerDialogue("How about you don't and pretend you did?"));
			return app;
		}
	}, new Conversation() {
		@Override
		public void send(Player player, int index) {

		}
		
		@Override
		public DialogueAppender dialogues(Player player) {
			DialogueAppender app = new DialogueAppender(player);
			app.chain(new NpcDialogue(6824, "I think we're going to need to operate."));
			app.chain(new PlayerDialogue("I think we can skip that for now."));
			app.chain(new NpcDialogue(6843, "Who's the doctor here?"));
			app.chain(new PlayerDialogue("Not you."));
			app.chain(new NpcDialogue(6843, "I may not be a doctor, but I'm keen. Does that not count?"));
			app.chain(new PlayerDialogue("In most other fields, yes; in medicine, no."));
			return app;
		}
	}};
	
}
