package net.edge.world.content.skill.summoning.familiar.impl;

import net.edge.utils.rand.RandomUtils;
import net.edge.world.content.dialogue.impl.NpcDialogue;
import net.edge.world.content.skill.summoning.Summoning;
import net.edge.world.content.skill.summoning.familiar.Familiar;
import net.edge.world.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.world.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.edge.world.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.world.content.skill.summoning.specials.SummoningData;
import net.edge.world.Hit;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * Represents the Compost mound familiar.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CompostMound extends Familiar {
	
	/**
	 * Constructs a new {@link CompostMound}.
	 */
	public CompostMound() {
		super(SummoningData.COMPOST_MOUND);
	}
	
	private final ForagerPassiveAbility ability = new ForagerPassiveAbility(6034, 6032, 5291, 5320, 5101);
	
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
			player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, npc);
		}
	}
	
	@Override
	public boolean itemOnNpc(Player player, Npc npc, Item item) {
		if(item.getId() != 1925) {
			return false;
		}
		npc.damage(new Hit(2));
		player.getInventory().remove(new Item(1925));
		player.getInventory().add(new Item(6032));
		return true;
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"Oi wud just a-wonderin'...", "*Errr... Are ye gonna eat that?", "Oi've gotta braand new comboine 'aarvester!"};
	
}
