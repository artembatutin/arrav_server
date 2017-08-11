package net.edge.content.combat.strategy.base;

import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatSpell;
import net.edge.content.combat.special.CombatSpecial;
import net.edge.content.combat.strategy.Strategy;
import net.edge.content.minigame.MinigameHandler;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

import java.util.OptionalInt;

public final class MagicStrategy implements Strategy {
	
	@Override
	public boolean canOutgoingAttack(Actor actor, Actor victim) {
		if(actor.isMob()) {
			return true;
		}
		
		Player player = (Player) actor;
		
		if(!MinigameHandler.execute(player, m -> m.canHit(player, victim, CombatType.MAGIC))) {
			return false;
		}
		
		boolean canCast = player.getCombatSpecial() != null && player.getCombatSpecial() == CombatSpecial.KORASI_SWORD || get(player).canCast(player);
		
		if(!canCast) {
			return false;
		}
		
		player.getCombat().setCombatType(CombatType.MAGIC);
		return true;
	}
	
	@Override
	public CombatHit outgoingAttack(Actor actor, Actor victim) {
		int delay = 0;
		if(actor.isPlayer()) {
			Player player = (Player) actor;
			if(player.getAttr().get("lunar_spellbook_swap").getBoolean()) {
				player.getAttr().get("lunar_spellbook_swap").set(false);
			}
			delay = player.prepareSpell(get(player), victim);
		} else if(actor.isMob()) {
			Mob mob = (Mob) actor;
			delay = mob.prepareSpell(CombatUtil.prepareSpellCast(mob).getSpell(), victim);
		}
		
		if(actor.getCurrentlyCasting().maximumHit() == -1) {
			return new CombatHit(actor, victim, 0, CombatType.MAGIC, true, delay == 0 ? OptionalInt.empty() : OptionalInt.of(delay));
		}
		return new CombatHit(actor, victim, 1, CombatType.MAGIC, true, delay + 1);
	}
	
	@Override
	public int attackDelay(Actor actor) {
		return 7;
	}
	
	@Override
	public int attackDistance(Actor actor) {
		return 8;
	}
	
	@Override
	public int[] getMobs() {
		return new int[]{6278, 6257, 6221, 13, 172, 174, 2025,
				3752, 3753, 3754, 3755, 3756, 3757, 3758, 3759, 3760, 3761//pest torchers
		};
	}
	
	private CombatSpell get(Player player) {
		if(player.isAutocast() && player.getCastSpell() != null && player.getAutocastSpell() != null || !player.isAutocast() && player.getAutocastSpell() == null) {
			return player.getCastSpell();
		}
		if(player.getAutocastSpell() != null)
			return player.getAutocastSpell();
		return player.getCastSpell();
	}
}
