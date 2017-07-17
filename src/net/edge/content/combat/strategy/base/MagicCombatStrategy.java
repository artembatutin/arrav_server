package net.edge.content.combat.strategy.base;

import net.edge.content.combat.Combat;
import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatSpell;
import net.edge.content.combat.special.CombatSpecial;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.minigame.MinigameHandler;
import net.edge.world.node.actor.Actor;
import net.edge.world.node.actor.mob.Mob;
import net.edge.world.node.actor.player.Player;

import java.util.OptionalInt;

public final class MagicCombatStrategy implements CombatStrategy {
	
	@Override
	public boolean canOutgoingAttack(Actor character, Actor victim) {
		if(character.isNpc()) {
			return true;
		}
		
		Player player = (Player) character;
		
		if(!MinigameHandler.execute(player, m -> m.canHit(player, victim, CombatType.MAGIC))) {
			return false;
		}
		
		boolean canCast = player.getCombatSpecial() != null && player.getCombatSpecial() == CombatSpecial.KORASI_SWORD || get(player).canCast(player);
		
		if(!canCast) {
			return false;
		}
		
		player.getCombatBuilder().setCombatType(CombatType.MAGIC);
		return true;
	}
	
	@Override
	public CombatHit outgoingAttack(Actor character, Actor victim) {
		int delay = 0;
		if(character.isPlayer()) {
			Player player = (Player) character;
			if(player.getAttr().get("lunar_spellbook_swap").getBoolean()) {
				player.getAttr().get("lunar_spellbook_swap").set(false);
			}
			delay = player.prepareSpell(get(player), victim);
		} else if(character.isNpc()) {
			Mob mob = (Mob) character;
			delay = mob.prepareSpell(Combat.prepareSpellCast(mob).getSpell(), victim);
		}
		
		if(character.getCurrentlyCasting().maximumHit() == -1) {
			return new CombatHit(character, victim, 0, CombatType.MAGIC, true, delay == 0 ? OptionalInt.empty() : OptionalInt.of(delay));
		}
		return new CombatHit(character, victim, 1, CombatType.MAGIC, true, delay + 1);
	}
	
	@Override
	public int attackDelay(Actor character) {
		return 7;
	}
	
	@Override
	public int attackDistance(Actor character) {
		return 8;
	}
	
	@Override
	public int[] getNpcs() {
		return new int[]{6278, 6257, 6221, 13, 172, 174, 2025,
				3752, 3753, 3754, 3755, 3756, 3757, 3758, 3759, 3760, 3761//pest torchers
		};
	}
	
	private CombatSpell get(Player player) {
		if(player.isAutocast() && player.getCastSpell() != null && player.getAutocastSpell() != null || !player.isAutocast() && player.getAutocastSpell() == null) {
			return player.getCastSpell();
		}
		return player.getAutocastSpell();
	}
}
