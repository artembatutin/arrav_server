package net.edge.content.skill.summoning.familiar.impl;

import net.edge.content.dialogue.Conversation;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.dialogue.impl.PlayerDialogue;
import net.edge.content.dialogue.test.DialogueAppender;
import net.edge.content.skill.firemaking.Firemaking;
import net.edge.content.skill.summoning.SummoningData;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.content.skill.summoning.familiar.ability.Fighter;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.util.rand.RandomUtils;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 24-7-2017.
 */
public final class Pyrelord extends Familiar {

	private final Fighter ability = new Fighter();

	/**
	 * Constructs a new {@link Familiar}.
	 */
	public Pyrelord() {
		super(SummoningData.PYRELORD);
	}

	/**
	 * The ability type chained to this familiar.
	 *
	 * @return the familiar ability type.
	 */
	@Override
	public FamiliarAbility getAbilityType() {
		return ability;
	}

	/**
	 * The passive ability chained to this familiar.
	 *
	 * @return the passive ability instance wrapped in an
	 * optional if theres a value present, {@link Optional#empty()} otherwise.
	 */
	@Override
	public Optional<PassiveAbility> getPassiveAbility() {
		return Optional.empty();
	}

	/**
	 * Checks if this familiar is combatic or not.
	 *
	 * @return <true> if this familiar is combatic, <false> otherwise.
	 */
	@Override
	public boolean isCombatic() {
		return true;
	}

	/**
	 * Attempts to interact with this familiar
	 *
	 * @param player the player whom is interacting with the familiar.
	 * @param mob    the mob this player is interacting with.
	 * @param id     the action id being interacted with.
	 */
	@Override
	public void interact(Player player, Mob mob, int id) {
		if(id == 1) {
			player.getDialogueBuilder().send(RandomUtils.random(CONVERSATION));
		}
	}

	@Override
	public boolean itemOnNpc(Player player, Mob mob, Item item) {
		player.facePosition(mob.getPosition());
		Firemaking.execute(player, item, item, true);
		return true;
	}

	private static final Conversation[] CONVERSATION = new Conversation[]{
			new Conversation() {
				@Override
				public void send(Player player, int index) {

				}

				@Override
				public DialogueAppender dialogues(Player player) {
					DialogueAppender app = new DialogueAppender(player);
					app.chain(new NpcDialogue(7177, "What are we doing here?"));
					app.chain(new PlayerDialogue("Whatever I feel like doing."));
					app.chain(new NpcDialogue(7177, "I was summoned by a greater demon once you know."));
					app.chain(new NpcDialogue(7177, "He said we'd see the world..."));
					app.chain(new PlayerDialogue("What happened?"));
					app.chain(new NpcDialogue(7177, "He was slain; it was hilarious!"));
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
					app.chain(new NpcDialogue(7177, "I used to be feared across five planes..."));
					app.chain(new PlayerDialogue("Oh dear, now you're going to be sad all day!"));
					app.chain(new NpcDialogue(7177, "At least I won't be the only one."));
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
					app.chain(new NpcDialogue(7177, "I could teach you to smite your enemies with flames."));
					app.chain(new PlayerDialogue("You're not the only one; we have runes to do that."));
					app.chain(new NpcDialogue(7177, "Runes? Oh, that's so cute!"));
					app.chain(new PlayerDialogue("Cute?"));
					app.chain(new NpcDialogue(7177, "Well, not cute so much as tragic. I could teach you to do it without runes."));
					app.chain(new PlayerDialogue("Really?"));
					app.chain(new NpcDialogue(7177, "No."));
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
					app.chain(new NpcDialogue(7177, "Have you never been on fire?"));
					app.chain(new PlayerDialogue("You say that like it's a bad thing."));
					app.chain(new NpcDialogue(7177, "Isn't it? It gives me the heebie-jeebies!"));
					app.chain(new PlayerDialogue("You're afraid of something?"));
					app.chain(new NpcDialogue(7177, "Yes: I'm afraid of being you."));
					app.chain(new PlayerDialogue("I don't think he likes me..."));
					return app;
				}
			}
	};
}
