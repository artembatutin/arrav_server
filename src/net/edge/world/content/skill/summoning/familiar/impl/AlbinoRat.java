package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.Charm;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.content.skill.summoning.familiar.passive.impl.PeriodicalAbility;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.item.ItemNodeStatic;
import net.edge.world.model.node.region.Region;
import net.edge.world.content.skill.summoning.familiar.Familiar;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the Albino rat familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AlbinoRat extends Familiar {
	
	/**
	 * The identification of the albino rat.
	 */
	private static final int ALBINO_RAT_ID = 6847;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 2200;
	
	/**
	 * Constructs a new {@link AlbinoRat}.
	 */
	public AlbinoRat() {
		super(ALBINO_RAT_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12067);
	}
	
	@Override
	public Charm getCharm() {
		return Charm.BLUE;
	}
	
	@Override
	public int getRequirement() {
		return 23;
	}
	
	@Override
	public int getPoints() {
		return 1;
	}
	
	private final ForagerPassiveAbility ability = new ForagerPassiveAbility(1985);
	
	@Override
	public FamiliarAbility getAbilityType() {
		return ability;
	}
	
	private final PeriodicalAbility passiveAbility = new PeriodicalAbility(350, t -> {
		if(!t.getFamiliar().isPresent()) {
			return;
		}
		if(ThreadLocalRandom.current().nextInt(100) < 40) {
			t.getFamiliar().get().forceChat("Rawrgh, mai cheese!");
			return;
		}
		Region region = t.getRegion();
		if(region != null) {
			ItemNodeStatic eggs = new ItemNodeStatic(new Item(1985), t.getFamiliar().get().getPosition());
			region.register(eggs);
		}
	});
	
	@Override
	public Optional<PassiveAbility> getPassiveAbility() {
		return Optional.of(passiveAbility);
	}
	
	@Override
	public boolean isCombatic() {
		return true;
	}
	
	@Override
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]);
		}
	}
	
	private final NpcDialogue[] RANDOM_DIALOGUE = new NpcDialogue[]{new NpcDialogue(ALBINO_RAT_ID, "Hey boss, we doing to do anything wicked today?"), new NpcDialogue(ALBINO_RAT_ID, "You know, boss, I don't think you're totally into", "this whole 'evil' thing."), new NpcDialogue(ALBINO_RAT_ID, "Hey boss, can we go and loot something now?"),};
}
