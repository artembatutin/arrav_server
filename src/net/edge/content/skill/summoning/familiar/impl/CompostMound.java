package net.edge.content.skill.summoning.familiar.impl;

import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.hit.HitIcon;
import net.edge.content.combat.hit.Hitsplat;
import net.edge.util.rand.RandomUtils;
import net.edge.content.dialogue.impl.NpcDialogue;
import net.edge.content.skill.summoning.Summoning;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarAbility;
import net.edge.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import net.edge.content.skill.summoning.familiar.passive.PassiveAbility;
import net.edge.content.skill.summoning.SummoningData;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

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
	public void interact(Player player, Mob mob, int id) {
		if(id == 1) {
			player.getDialogueBuilder().append(new NpcDialogue(getId(), RandomUtils.random(RANDOM_DIALOGUE)));
		} else if(id == 2) {
			Summoning.openBeastOfBurden(player, mob);
		}
	}
	
	@Override
	public boolean itemOnNpc(Player player, Mob mob, Item item) {
		if(item.getId() != 1925) {
			return false;
		}
		mob.damage(new Hit(2, Hitsplat.NORMAL, HitIcon.NONE));
		player.getInventory().remove(new Item(1925));
		player.getInventory().add(new Item(6032));
		return true;
	}
	
	private final String[] RANDOM_DIALOGUE = new String[]{"Oi wud just a-wonderin'...", "*Errr... Are ye gonna eat that?", "Oi've gotta braand new comboine 'aarvester!"};
	
}
