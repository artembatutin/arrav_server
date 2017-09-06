package net.edge.world.entity.actor.combat.magic;

import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.content.skill.slayer.Slayer;
import net.edge.net.packet.out.SendMessage;
import net.edge.util.rand.RandomUtils;
import net.edge.world.PoisonType;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatImpact;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.CombatUtil;
import net.edge.world.entity.actor.combat.formula.FormulaFactory;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

import java.util.List;

public enum MagicImpact {
	
	BIND((attacker, defender, hit, extra) -> freeze(defender, 5)),
	SNARE(((attacker, defender, hit, extra) -> freeze(defender, 10))),
	ENTANGLE(((attacker, defender, hit, extra) -> freeze(defender, 15))),
	
	CONFUSE((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.ATTACK, 5)),
	WEAKEN((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.STRENGTH, 5)),
	CURSE((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.DEFENCE, 5)),
	
	VULNERABILITY((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.DEFENCE, 10)),
	ENFEEBLE((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.STRENGTH, 10)),
	STUN((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.ATTACK, 10)),

	MAGIC_DART((attacker, defender, hit, extra) -> {
		if (attacker.isPlayer() && hit.isAccurate()) {
			Player player = attacker.toPlayer();
			int damage = 10 + player.getSkills()[Skills.MAGIC].getLevel() / 10;

			if (defender.isMob() && player.getSlayer().isPresent()) {
				Mob mob = defender.toMob();
				Slayer slayer = player.getSlayer().get();
				String key = mob.getDefinition().getSlayerKey();

				if (key != null && slayer.getKey().equals(key)) {
					damage = 13 + player.getSkills()[Skills.MAGIC].getLevel() / 6;
				}
			}

			hit.setDamage(RandomUtils.inclusive(damage));
		}
	}),

	SMOKE_RUSH((attacker, defender, hit, extra) -> poison(attacker, defender, hit, PoisonType.DEFAULT_MAGIC)),
	SMOKE_BURST((attacker, defender, hit, extra) -> CombatUtil.areaAction(attacker, defender, a -> smokeBurst(attacker, defender, a, hit, extra))),
	SMOKE_BLITZ((attacker, defender, hit, extra) -> poison(attacker, defender, hit, PoisonType.SUPER_MAGIC)),
	SMOKE_BARRAGE((attacker, defender, hit, extra) -> CombatUtil.areaAction(attacker, defender, a -> smokeBarrage(attacker, defender, a, hit, extra))),
	
	SHADOW_RUSH((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.ATTACK, 10)),
	SHADOW_BURST((attacker, defender, hit, extra) -> CombatUtil.areaAction(attacker, defender, a -> shadowBurst(attacker, defender, a, extra))),
	SHADOW_BLITZ((attacker, defender, hit, extra) -> lowerSkill(defender, Skills.ATTACK, 15)),
	SHADOW_BARRAGE((attacker, defender, hit, extra) -> CombatUtil.areaAction(attacker, defender, a -> shadowBarrage(attacker, defender, a, extra))),
	
	BLOOD_RUSH((attacker, defender, hit, extra) -> heal(attacker, hit)),
	BLOOD_BURST((attacker, defender, hit, extra) -> CombatUtil.areaAction(attacker, defender, a -> bloodBurst(attacker, defender, a, hit, extra))),
	BLOOD_BLITZ((attacker, defender, hit, extra) -> heal(attacker, hit)),
	BLOOD_BARRAGE((attacker, defender, hit, extra) -> CombatUtil.areaAction(attacker, defender, a -> bloodBarrage(attacker, defender, a, hit, extra))),
	
	ICE_RUSH((attacker, defender, hit, extra) -> freeze(defender, 5)),
	ICE_BURST((attacker, defender, hit, extra) -> CombatUtil.areaAction(attacker, defender, a -> iceBurst(attacker, defender, a, extra))),
	ICE_BLITZ((attacker, defender, hit, extra) -> freeze(defender, 15)),
	ICE_BARRAGE((attacker, defender, hit, extra) -> CombatUtil.areaAction(attacker, defender, actor -> iceBarrage(attacker, defender, actor, extra))),
	
	KBD_FREEZE((attacker, defender, hit, extra) -> freeze(defender, 5)),
	KBD_POISON((attacker, defender, hit, extra) -> poison(attacker, defender, hit, PoisonType.DEFAULT_NPC)),
	KBD_SHOCK((attacker, defender, hit, extra) -> kbdShock(defender)),
	
	AHRIM_BLAST((attacker, defender, hit, extra) -> {
//        if (hit.isAccurate() && Math.random() < 0.20) {
//            defender.skills.get(Skills.STRENGTH).removeLevel(5);
//            defender.skills.refresh(Skills.STRENGTH);
//        }
	});
	
	private final CombatImpact effect;
	
	MagicImpact(CombatImpact effect) {
		this.effect = new CombatImpact() {
			@Override
			public boolean canAffect(Actor attacker, Actor defender, Hit hit) {
				return hit.isAccurate() && effect.canAffect(attacker, defender, hit);
			}
			
			@Override
			public void impact(Actor attacker, Actor defender, Hit hit, List<Hit> hits) {
				effect.impact(attacker, defender, hit, hits);
			}
		};
	}
	
	public CombatImpact getEffect() {
		return effect;
	}
	
	private static void kbdShock(Actor defender) {
		if(!defender.isPlayer()) {
			return;
		}
		
		Player player = defender.toPlayer();
		int id = RandomUtils.inclusiveExcludes(Skills.ATTACK, Skills.MAGIC, Skills.HITPOINTS);
		Skill skill = player.getSkills()[id];
		
		if(skill.getLevel() - 1 >= 0) {
			skill.decreaseLevel(1, true);
			Skills.refresh(player, id);
		}
	}
	
	private static void lowerSkill(Actor defender, int id, int percentage) {
		if(!defender.isPlayer()) {
			return;
		}
		
		Player player = defender.toPlayer();
		Skill skill = player.getSkills()[id];
		
		double ratio = percentage / 100.0;
		int limit = (int) (skill.getRealLevel() * (1 - ratio));
		int amount = (int) (skill.getLevel() * ratio);
		
		if(skill.getLevel() - amount < limit) {
			amount = skill.getLevel() - limit;
		}
		
		if(amount > 0) {
			skill.decreaseLevel(amount);
			Skills.refresh(player, id);
		}
	}
	
	private static void poison(Actor attacker, Actor defender, Hit hit, PoisonType type) {
		if(defender.isPoisoned()) {
			return;
		}
		
		if(!attacker.isMob() && (!hit.isAccurate() || hit.getDamage() < 1)) {
			return;
		}
		
		defender.poison(type);
	}
	
	private static void freeze(Actor defender, int timer) {
		if(defender.isFrozen()) {
			return;
		}
		
		if(defender.isPlayer()) {
			defender.toPlayer().out(new SendMessage("You've been frozen!"));
		}
		
		defender.freeze(timer);
	}
	
	private static void heal(Actor attacker, Hit hit) {
		Skill skill = attacker.toPlayer().getSkills()[Skills.HITPOINTS];
		int current = attacker.getCurrentHealth();
		int real = skill.getRealLevel() * 10;
		
		if(current < real) {
			float heal = hit.getDamage() / 40f;
			
			if(heal + current > real) {
				heal = real - current;
			}
			
			attacker.healEntity((int) (heal * 10));
		}
	}
	
	private static void smokeBurst(Actor attacker, Actor defender, Actor actor, Hit hit, List<Hit> extra) {
		poison(attacker, defender, hit, PoisonType.DEFAULT_MAGIC);
		
		CombatHit next = hitEvent(attacker, defender, actor, 18, extra);
		if(next != null && (attacker.isMob() || (next.isAccurate() && next.getDamage() > 0))) {
			poison(attacker, actor, hit, PoisonType.DEFAULT_MAGIC);
		}
	}
	
	private static void smokeBarrage(Actor attacker, Actor defender, Actor actor, Hit hit, List<Hit> extra) {
		poison(attacker, defender, hit, PoisonType.SUPER_MAGIC);
		
		CombatHit next = hitEvent(attacker, defender, actor, 27, extra);
		if(next != null && (attacker.isMob() || (next.isAccurate() && next.getDamage() > 0))) {
			poison(attacker, actor, hit, PoisonType.SUPER_MAGIC);
		}
	}
	
	private static void shadowBurst(Actor attacker, Actor defender, Actor actor, List<Hit> extra) {
		lowerSkill(defender, Skills.ATTACK, 10);
		
		CombatHit next = hitEvent(attacker, defender, actor, 19, extra);
		if(next != null && next.isAccurate()) {
			lowerSkill(actor, Skills.ATTACK, 10);
		}
	}
	
	private static void shadowBarrage(Actor attacker, Actor defender, Actor actor, List<Hit> extra) {
		lowerSkill(defender, Skills.ATTACK, 15);
		
		CombatHit next = hitEvent(attacker, defender, actor, 28, extra);
		if(next != null && next.isAccurate()) {
			lowerSkill(actor, Skills.ATTACK, 15);
		}
	}
	
	private static void bloodBurst(Actor attacker, Actor defender, Actor actor, Hit hit, List<Hit> extra) {
		heal(attacker, hit);
		CombatHit next = hitEvent(attacker, defender, actor, 21, extra);
		if(next != null && next.isAccurate()) {
			heal(attacker, next);
		}
	}
	
	private static void bloodBarrage(Actor attacker, Actor defender, Actor actor, Hit hit, List<Hit> extra) {
		heal(attacker, hit);
		CombatHit next = hitEvent(attacker, defender, actor, 29, extra);
		if(next != null && next.isAccurate()) {
			heal(attacker, next);
		}
	}
	
	private static void iceBurst(Actor attacker, Actor defender, Actor actor, List<Hit> extra) {
		freeze(defender, 10);
		CombatHit next = hitEvent(attacker, defender, actor, 22, extra);
		if(next != null && next.isAccurate()) {
			freeze(actor, 10);
		}
	}
	
	private static void iceBarrage(Actor attacker, Actor defender, Actor actor, List<Hit> extra) {
		freeze(defender, 20);
		
		CombatHit next = hitEvent(attacker, defender, actor, 28, extra);
		if(next != null && next.isAccurate()) {
			freeze(actor, 20);
		}
	}

	private static CombatHit hitEvent(Actor attacker, Actor defender, Actor actor, int max, List<Hit> extra) {
		if (!defender.same(actor)) {
			int hitDelay = CombatUtil.getHitDelay(attacker, defender, CombatType.MAGIC);
			int hitsplatDelay = CombatUtil.getHitsplatDelay(CombatType.MAGIC);
			CombatHit hit = new CombatHit(FormulaFactory.nextMagicHit(attacker, actor, max), hitDelay, hitsplatDelay);
			attacker.getCombat().submitHits(actor, hit);
			if (extra != null) extra.add(hit);
			return hit;
		}
		return null;
	}
	
}
