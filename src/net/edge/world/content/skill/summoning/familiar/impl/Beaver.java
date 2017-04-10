package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.fletching.BowCarving;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.model.node.entity.npc.Npc;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the beaver familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Beaver extends Familiar {

	/**
	 * The identification of the beaver.
	 */
	private static final int BEAVER_ID = 6808;

	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 2700;

	/**
	 * Constructs a new {@link Beaver}.
	 */
	public Beaver() {
		super(BEAVER_ID, LIFE_TICKS);
	}

	@Override
	public Item getPouch() {
		return new Item(12021);
	}

	@Override
	public int getRequirement() {
		return 33;
	}

	@Override
	public int getPoints() {
		return 4;
	}

	private final ForagerPassiveAbility ability = new ForagerPassiveAbility(1511, 1515, 1517, 1519, 1521, 960, 8778);

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
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(BEAVER_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, npc);
		}
	}

	@Override
	public boolean itemOnNpc(Player player, Npc npc, Item item) {
		BowCarving.openInterface(player, item, item, true);
		return true;
	}

	private final String[][] RANDOM_DIALOGUE = new String[][]{{"Vot are we doing 'ere when we could be logging", "and building mighty dams, alors?"}, {"Pardonnez-moi - you call yourself a lumberjack?",},};

}
