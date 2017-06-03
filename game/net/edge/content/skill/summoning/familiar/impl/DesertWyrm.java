package net.edge.content.skill.summoning.familiar.impl;

import net.edge.task.LinkedTaskSequence;
import net.edge.util.rand.RandomUtils;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.content.skill.summoning.familiar.impl.forager.ForagerRightClickAbility;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.content.skill.summoning.specials.SummoningData;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

import java.util.Optional;

/**
 * Represents the Desert wyrm familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DesertWyrm extends Familiar {
	
	/**
	 * Constructs a new {@link DesertWyrm}.
	 */
	public DesertWyrm() {
		super(SummoningData.DESERT_WYRM);
	}
	
	private final ForagerRightClickAbility ability = new ForagerRightClickAbility(t -> {
		if(!t.getFamiliar().isPresent()) {
			return;
		}
		Familiar familiar = t.getFamiliar().get();
		
		familiar.animation(new Animation(7800));
		familiar.graphic(new Graphic(1412));
		
		//List<ObjectNode> objs = World.getRegions().getRegion(familiar.getPosition()).getInteractiveObjects(familiar.getPosition(), 7);
		//Rock rock = null;
		//for(ObjectNode n : objs) {
		//	if(n != null) {
		//		//rock = Rock.getDefinition(n.getId()).orElse(null);
		//		TODO: Stan, look at this.
		//	}
		//}
		
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
			player.getDialogueBuilder().append(RandomUtils.random(RANDOM_DIALOGUE));
		} else if(id == 2) {
			this.ability.activate(player);
		}
	}
	
	private final NpcDialogue[] RANDOM_DIALOGUE = new NpcDialogue[]{new NpcDialogue(getId(), "This is so unsafe... I should have a hard hat", "for this work..."), new NpcDialogue(getId(), "You can't touch me, I'm part of the union!"), new NpcDialogue(getId(), "If you have that pick, why make me dig?")};
}
