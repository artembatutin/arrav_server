package net.edge.content.skill.prayer.curses;

import net.edge.content.skill.prayer.Prayer;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.listener.SimplifiedListener;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.player.Player;

public final class LeechCurse extends SimplifiedListener<Player> {

	private static final LeechCurse INSTANCE = new LeechCurse();

	private LeechCurse() { }

	private static boolean checkDelay(Player c) {
		if (c.leechDelay.elapsed(2800)) {
			c.leechDelay.reset();
			return true;
		}
		return false;
	}

	@Override
	public void attack(Player attacker, Actor defender, Hit hit) {
		if(defender == null) {
			return;
		}
/*
		if (!LeechCurse.checkDelay(attacker)) {
			return;
	}
*/
//		if(RandomUtils.inclusive(0, 10) != 0) {
//			return;
//		}

		Prayer[] prayers = new Prayer[]{Prayer.LEECH_ATTACK, Prayer.LEECH_DEFENCE, Prayer.LEECH_ENERGY, Prayer.LEECH_MAGIC, Prayer.LEECH_RANGED, Prayer.LEECH_SPECIAL_ATTACK, Prayer.LEECH_STRENGTH};

		Prayer prayer = RandomUtils.random(Prayer.getActivatedPrayers(attacker, prayers));

		switch(prayer) {
			case LEECH_ATTACK:
				attacker.animation(new Animation(12575));
				new AttackCurseEvent(defender, attacker).submit();
				attacker.message("You leech the targets attack.");
				break;
			case LEECH_STRENGTH:
				attacker.animation(12575);
				new StrengthCurseEvent(defender, attacker).submit();
				attacker.message("You leech the targets strength.");
				break;
			case LEECH_DEFENCE:
				attacker.animation(12575);
				new DefenceCurseEvent(defender, attacker).submit();
				attacker.message("You leech the targets defence.");
				break;
			case LEECH_RANGED:
				attacker.animation(new Animation(12575));
				new RangeCurseEvent(defender, attacker).submit();
				attacker.message("You leech the targets range.");
				break;
			case LEECH_MAGIC:
				attacker.animation(12575);
				new MagicCurseEvent(defender, attacker).submit();
				attacker.message("You leech the targets magic.");
				break;
			case LEECH_ENERGY:
				attacker.animation(12575);
				new EnergyCurseEvent(defender, attacker).submit();
				attacker.message("You leech the targets run energy.");
				break;
			case LEECH_SPECIAL_ATTACK:
				attacker.animation(12575);
				if (defender.isPlayer() && attacker.isPlayer()) {
					Player o = (Player) defender;
					if (attacker.getSpecialPercentage().intValue() < 10 && attacker.getSpecialPercentage().intValue() > 0) {
						attacker.getSpecialPercentage().incrementAndGet(10);
						o.getSpecialPercentage().decrementAndGet(10);
					}
				}
				attacker.message("You leech the targets special.");
				new SpecialCurseEvent(defender, attacker).submit();
				break;

		}
	}

	public static LeechCurse get() {
		return INSTANCE;
	}

}