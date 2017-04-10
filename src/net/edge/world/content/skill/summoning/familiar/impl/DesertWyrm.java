package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.task.LinkedTaskSequence;
import net.edge.world.World;
import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.model.Graphic;
import net.edge.world.model.node.entity.npc.Npc;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.skill.summoning.familiar.impl.forager.ForagerRightClickAbility;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the Desert wyrm familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DesertWyrm extends Familiar {
	
	/**
	 * The identification of the dread fowl.
	 */
	private static final int DESERT_WYRM_ID = 6831;
	
	/**
	 * The amount of ticks this familiar stays alive for.
	 */
	private static final int LIFE_TICKS = 1900;
	
	/**
	 * Constructs a new {@link DesertWyrm}.
	 */
	public DesertWyrm() {
		super(DESERT_WYRM_ID, LIFE_TICKS);
	}
	
	@Override
	public Item getPouch() {
		return new Item(12049);
	}
	
	@Override
	public int getRequirement() {
		return 18;
	}
	
	@Override
	public int getPoints() {
		return 1;
	}
	
	private final ForagerRightClickAbility ability = new ForagerRightClickAbility(t -> {
		if(!t.getFamiliar().isPresent()) {
			return;
		}
		Familiar familiar = t.getFamiliar().get();
		
		familiar.animation(new Animation(7800));
		familiar.graphic(new Graphic(1412));
		
		Set<ObjectNode> objs = World.getRegions().getRegion(familiar.getPosition()).getObjects(familiar.getPosition(), 7);
		//Rock rock = null; TODO
		for(ObjectNode n : objs) {
			if(n != null) {
				//rock = Rock.getDefinition(n.getId()).orElse(null);
			}
		}
		
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(4, () -> familiar.setVisible(false));
		seq.connect(8, () -> {
			familiar.animation(new Animation(7804));
			familiar.graphic(new Graphic(1412));
			familiar.setVisible(true);
		});
		seq.start();
	}, 1231);
	
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
	public void interact(Player player, Npc npc, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(RANDOM_DIALOGUE[ThreadLocalRandom.current().nextInt(RANDOM_DIALOGUE.length - 1)]);
		} else if(id == 2) {
			this.ability.activate(player);
		}
	}
	
	private final NpcDialogue[] RANDOM_DIALOGUE = new NpcDialogue[]{new NpcDialogue(DESERT_WYRM_ID, "This is so unsafe... I should have a hard hat", "for this work..."), new NpcDialogue(DESERT_WYRM_ID, "You can't touch me, I'm part of the union!"), new NpcDialogue(DESERT_WYRM_ID, "If you have that pick, why make me dig?"),};
}
