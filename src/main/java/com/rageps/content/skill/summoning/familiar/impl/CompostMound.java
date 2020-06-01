package com.rageps.content.skill.summoning.familiar.impl;

import com.rageps.content.dialogue.impl.NpcDialogue;
import com.rageps.content.skill.summoning.familiar.impl.forager.ForagerPassiveAbility;
import com.rageps.content.skill.summoning.familiar.passive.PassiveAbility;
import com.rageps.content.skill.summoning.Summoning;
import com.rageps.content.skill.summoning.SummoningData;
import com.rageps.content.skill.summoning.familiar.Familiar;
import com.rageps.content.skill.summoning.familiar.FamiliarAbility;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.HitIcon;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

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
