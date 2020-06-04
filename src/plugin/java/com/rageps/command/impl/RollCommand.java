package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.combat.CombatType;
import com.rageps.combat.strategy.CombatStrategy;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

import static com.rageps.world.entity.actor.combat.formula.FormulaFactory.*;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 9-7-2017.
 */
@CommandSignature(alias = {"getroll", "roll"}, rights = {Rights.ADMINISTRATOR}, syntax = "Displays combat statistics")
public final class RollCommand implements Command {
	
	/**
	 * The functionality to be executed as soon as this command is called.
	 * @param player the player we are executing this command for.
	 * @param cmd the command that we are executing for this player.
	 */
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		CombatStrategy<? super Player> strategy = player.getStrategy();
		
		CombatType type = strategy.getCombatType();
		player.getCombat().addModifier(strategy);

		double attackRoll = rollOffensive(player, player, type.getFormula());
		double defenceRoll = rollDefensive(player, player, type.getFormula());
		double chance = attackRoll / (attackRoll + defenceRoll);
		double accuracy = (int) (chance * 10000) / 100.0;

		int max = getMaxHit(player, player, type);
		max = player.getCombat().modifyDamage(player, max);

		player.message("");
		player.message("You have @red@" + accuracy + "%@bla@ accuracy against " + player.getFormatUsername() + ".");
		player.message("Your max hit against " + player.getFormatUsername() + " is @red@" + max);
		player.message("Attack roll: @red@" + (int) attackRoll + "@bla@  ---  Defence roll: @red@" + (int) defenceRoll);

		String rolls = "";
		rolls += "accuracy: @red@" + player.getCombat().modifyAccuracy(player, 100) + "%@bla@  ---  ";
		rolls += "defence: @red@" + player.getCombat().modifyDefensive(player, 100) + "%@bla@  --- ";
		rolls += "damage: @red@" + player.getCombat().modifyDamage(player, 100) + "%";

		String levels = "";
		levels += "attack: @red@" + player.getCombat().modifyAttackLevel(player, 100) + "%@bla@ -- ";
		levels += "strength: @red@" + player.getCombat().modifyStrengthLevel(player, 100) + "%@bla@ -- ";
		levels += "defence: @red@" + player.getCombat().modifyDefenceLevel(player, 100) + "%@bla@ -- ";
		levels += "ranged: @red@" + player.getCombat().modifyRangedLevel(player, 100) + "%@bla@ -- ";
		levels += "magic: @red@" + player.getCombat().modifyMagicLevel(player, 100) + "%";

		player.getCombat().removeModifier(strategy);
		player.message(rolls);
		player.message(levels);
	}

}
