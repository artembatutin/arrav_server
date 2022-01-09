package com.rageps.world.entity.actor.combat.magic.lunars.spell.impl;

import com.rageps.content.skill.cooking.Cooking;
import com.rageps.content.skill.cooking.CookingData;
import com.rageps.world.entity.actor.combat.magic.MagicRune;
import com.rageps.world.entity.actor.combat.magic.RequiredRune;
import com.rageps.world.entity.actor.combat.magic.lunars.spell.LunarItemSpell;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * Holds functionality for the bake pie spell.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class BakePie extends LunarItemSpell {
	
	private CookingData data;
	
	/**
	 * Constructs a new {@link BakePie}.
	 */
	public BakePie() {
		super("Bake Pie", 30017, 3186, 63, 60, new RequiredRune(MagicRune.ASTRAL_RUNE, 1), new RequiredRune(MagicRune.FIRE_RUNE, 5), new RequiredRune(MagicRune.WATER_RUNE, 4));
	}
	
	@Override
	public void effect(Player caster, Item item) {
		Cooking cooking = new Cooking(caster, null, data, false, 1, true);
		cooking.start();
	}
	
	@Override
	public boolean canCast(Player player, Item item) {
		CookingData data = CookingData.forItem(item);
		
		if(data == null) {
			player.message("You can only cast this spell on pies.");
			return false;
		}
		
		if(!data.toString().contains("pie")) {
			player.message("You can only bake pies with this spell.");
			return false;
		}
		this.data = data;
		return true;
	}
}
