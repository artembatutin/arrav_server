package net.edge.content.skill.summoning.familiar.impl;

import net.edge.content.dialogue.Conversation;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.content.skill.summoning.SummoningData;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.util.rand.RandomUtils;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;

/**
 * Represents the Magpie familiar.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Magpie extends Familiar {

	/**
	 * Constructs a new {@link Magpie}.
	 */
	public Magpie() {
		super(SummoningData.MAGPIE);
	}

	private final ForagerPassiveAbility ability = new ForagerPassiveAbility(1625, 1627, 1629, 1623, 1621);

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

	private static final Conversation[] CONVERSATION = new Conversation[]{
			new Conversation() {
				@Override
				public void send(Player player, int index) {

				}

				@Override
				public DialogueAppender dialogues(Player player) {
					DialogueAppender app = new DialogueAppender(player);
					app.chain(new NpcDialogue(6824, "There's nowt gannin on here..."));
					app.chain(new PlayerDialogue("Err...sure? Maybe?"));
					app.chain(new PlayerDialogue("It seems upset, but what is it saying?"));
					return app;
				}
			},
			new Conversation() {
				@Override
				public void send(Player player, int index) {

				}

				@Override
				public DialogueAppender dialogues(Player player) {
					DialogueAppender app = new DialogueAppender(player);
					app.chain(new NpcDialogue(6824, "Howway, let's gaan see what's happenin' in toon."));
					app.chain(new PlayerDialogue("What? I can't understand what you're saying."));
					return app;
				}
			},
			new Conversation() {
				@Override
				public void send(Player player, int index) {

				}

				@Override
				public DialogueAppender dialogues(Player player) {
					DialogueAppender app = new DialogueAppender(player);
					app.chain(new NpcDialogue(6824, "Are we gaan oot soon? I'm up fer a good walk me."));
					app.chain(new PlayerDialogue("That...that was just noise. What does that mean?"));
					return app;
				}
			},
			new Conversation() {
				@Override
				public void send(Player player, int index) {

				}

				@Override
				public DialogueAppender dialogues(Player player) {
					DialogueAppender app = new DialogueAppender(player);
					app.chain(new NpcDialogue(6824, "Ye' been plowdin' i' the claarts aall day."));
					app.chain(new PlayerDialogue("What? That made no sense."));
					return app;
				}
			}
	};
}
