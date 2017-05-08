package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.Charm;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.skill.summoning.familiar.Familiar;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the Spirit tz-kih familiar. familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class SpiritTzKih extends Familiar {
	
	/**
	 * The identification of the spirit spider.
	 */
	public static final int SPIRIT_TZ_KIH_ID = 7361;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 1800;
	
	/**
	 * Constructs a new {@link SpiritTzKih}.
	 */
	public SpiritTzKih() {
		super(SPIRIT_TZ_KIH_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12808);
	}
	
	@Override
	public Charm getCharm() {
		return Charm.CRIMSON;
	}
	
	@Override
	public int getRequirement() {
		return 22;
	}
	
	@Override
	public int getPoints() {
		return 3;
	}
	
	@Override
	public FamiliarAbility getAbilityType() {
		// TODO Auto-generated method stub
		return null;
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
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(SPIRIT_TZ_KIH_ID, RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]));
		}
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"Hmph, silly JalYt.", "Have you heard of blood bat, JalYt?",};
	
}
