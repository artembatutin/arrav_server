package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.utils.rand.RandomUtils;
import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.dialogue.impl.OptionDialogue;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.content.skill.summoning.familiar.passive.impl.PeriodicalAbility;
import net.edge.world.content.skill.summoning.specials.SummoningData;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.item.ItemNodeStatic;
import net.edge.world.model.node.region.Region;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the SpiritSpider familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SpiritSpider extends Familiar {
	
	/**
	 * The identification of the red spiders eggs.
	 */
	private static final int RED_SPIDERS_EGGS = 223;
	
	/**
	 * Constructs a new {@link SpiritSpider}.
	 */
	public SpiritSpider() {
		super(SummoningData.SPIRIT_SPIDER);
	}
	
	private final ForagerPassiveAbility ability = new ForagerPassiveAbility(RED_SPIDERS_EGGS);
	
	@Override
	public FamiliarAbility getAbilityType() {
		return ability;
	}
	
	private final PeriodicalAbility passiveAbility = new PeriodicalAbility(350, t -> {
		if(!t.getFamiliar().isPresent()) {
			return;
		}
		if(ThreadLocalRandom.current().nextInt(100) < 40) {
			t.getFamiliar().get().forceChat("Rawrgh, my eggs won't come out!");
			return;
		}
		Region region = t.getFamiliar().get().getRegion();
		if(region != null) {
			ItemNodeStatic eggs = new ItemNodeStatic(new Item(223), t.getFamiliar().get().getPosition());
			region.register(eggs);
		}
	});
	
	@Override
	public Optional<PassiveAbility> getPassiveAbility() {
		return Optional.of(passiveAbility);
	}
	
	@Override
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new OptionDialogue(t -> {
				if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
					player.getDialogueBuilder().advance();
				} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
					Summoning.openBeastOfBurden(player, npc);
				} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
					player.getMessages().sendCloseWindows();
				}
			}, "Interact", "View storage", "Nevermind"), new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		}
	}
	
	@Override
	public boolean isCombatic() {
		return true;
	}
	
	private final String[] RANDOM_DIALOGUE = {"When can I lay my eggs?", "Where are we going?", "Why are you not responding to me?"};
}
