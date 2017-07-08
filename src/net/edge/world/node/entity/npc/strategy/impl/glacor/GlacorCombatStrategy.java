package net.edge.world.node.entity.npc.strategy.impl.glacor;

import com.google.common.collect.ImmutableSet;
import net.edge.content.combat.CombatHit;
import net.edge.net.packet.out.SendGraphic;
import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.magic.CombatNormalSpell;
import net.edge.content.combat.magic.CombatSpells;
import net.edge.content.skill.Skills;
import net.edge.locale.Position;
import net.edge.world.*;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.impl.glacor.Glacor;
import net.edge.world.node.entity.npc.impl.glacor.GlacorExplodeTask;
import net.edge.world.node.entity.npc.impl.glacor.GlacyteData;
import net.edge.world.node.entity.npc.strategy.DynamicCombatStrategy;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Arrays;
import java.util.Optional;

/**
 * The class which represents the combat strategy for glacors.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class GlacorCombatStrategy extends DynamicCombatStrategy<Glacor> {

	/**
	 * Constructs a new {@link GlacorCombatStrategy}.
	 * @param npc the npc this strategy is for.
	 */
	public GlacorCombatStrategy(Glacor npc) {
		super(npc);
	}

	@Override
	public boolean canOutgoingAttack(EntityNode victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatHit outgoingAttack(EntityNode victim) {
		CombatType[] data = npc.getPosition().withinDistance(victim.getPosition(), 2) ? new CombatType[]{CombatType.MELEE, CombatType.RANGED, CombatType.MAGIC} : new CombatType[]{CombatType.RANGED, CombatType.MAGIC};
		CombatType c = RandomUtils.random(data);

		CombatHit session = type(victim, c);

		if(this.npc.getSpeciality().isPresent()) {
			if(this.npc.getSpeciality().get().equals(GlacyteData.UNSTABLE)) {
				if(!this.npc.getSpecial().isPresent()) {
					this.npc.setSpecial(0);
				}
				
				int special = npc.getSpecial().getAsInt();

				if(special == 100) {
					session.ignore();
					World.get().submit(new GlacorExplodeTask(victim.toPlayer(), this.npc, false));
				}
				
				npc.setSpecial(special + 10);
			} else if(this.npc.getSpeciality().get().equals(GlacyteData.SAPPING)) {
				victim.toPlayer().getSkills()[Skills.PRAYER].decreaseLevel(5, true);
				Skills.refresh(victim.toPlayer(), Skills.PRAYER);
			}
		}

		if(RandomUtils.inclusive(100) < 10 && !session.isIgnored()) {
			return icycleSpecialAttack(victim);
		}

		return session;
	}

	private CombatHit melee(EntityNode victim) {
		npc.animation(new Animation(npc.getDefinition().getAttackAnimation()));
		return new CombatHit(npc, victim, 1, CombatType.MELEE, false, 2);
	}

	private CombatHit ranged(EntityNode victim) {
		npc.animation(new Animation(9968));
		new Projectile(npc, victim, 962, 50, 16, 61, 41, 16).sendProjectile();
		return new CombatHit(npc, victim, 1, CombatType.RANGED, true, 5);
	}

	private CombatHit icycleSpecialAttack(EntityNode victim) {
		npc.animation(new Animation(9955));
		Position tile = victim.getPosition().copy();

		new Projectile(npc.getPosition(), tile, 0, 2314, 50, 16, 61, 41, 16, npc.getInstance(), CombatType.RANGED).sendProjectile();
		return new CombatHit(npc, victim, 1, CombatType.RANGED, false, 12) {
			@Override
			public CombatHit preAttack() {
				CombatHit data = this;

				if(!victim.getPosition().same(tile) || npc.isDead()) {
					data.ignore();
					return data;
				}

				Arrays.fill(data.getHits(), null);
				int damage = victim.getCurrentHealth() / 2 < 200 ? 200 : victim.getCurrentHealth() / 2;
				data.getHits()[0] = new Hit(damage, Hit.HitType.NORMAL, Hit.HitIcon.NONE, victim.getSlot());
				data.getHits()[0].setAccurate(true);
				return data;
			}

			@Override
			public void postAttack(int counter) {
				SendGraphic.local(victim.toPlayer(), 2315, tile, 0);
			}
		};
	}

	private CombatHit magic(EntityNode victim) {
		npc.setCurrentlyCasting(SPELL);
		npc.animation(new Animation(9967));
		new Projectile(npc, victim, 634, 50, 16, 61, 41, 16).sendProjectile();
		return new CombatHit(npc, victim, 1, CombatType.MAGIC, false, 5);
	}

	private CombatHit type(EntityNode victim, CombatType type) {
		switch(type) {
			case MELEE:
				return melee(victim);
			case RANGED:
				return ranged(victim);
			case MAGIC:
				return magic(victim);
			default:
				return magic(victim);
		}
	}

	@Override
	public void incomingAttack(EntityNode attacker, CombatHit data) {
		if(npc.hasSummoned() && npc.getGlacytes().size() != 0) {
			Arrays.stream(data.getHits()).forEach(h -> h.setAccurate(false));
			attacker.toPlayer().message("Your hits have no effect on the glacor.");
			return;
		}
		
		if(npc.getSpeciality().isPresent() && npc.getSpeciality().get().equals(GlacyteData.ENDURING)) {
			Arrays.stream(data.getHits()).forEach(h -> h.setDamage(h.getDamage() / 2));
		}
		
		if(attacker.getCurrentlyCasting() != null && VALUES.stream().anyMatch(s -> s.getSpell().spellId() == attacker.getCurrentlyCasting().spellId())) {
			Arrays.stream(data.getHits()).forEach(h -> h.setDamage((int) (h.getDamage() * 1.2)));
		}
	}

	@Override
	public int attackDelay() {
		return npc.getAttackSpeed();
	}

	@Override
	public int attackDistance() {
		return 7;
	}

	private static final CombatNormalSpell SPELL = new CombatNormalSpell() {

		@Override
		public int spellId() {
			return 0;
		}

		@Override
		public int maximumHit() {
			return 310;
		}

		@Override
		public Optional<Animation> castAnimation() {
			return Optional.empty();
		}

		@Override
		public Optional<Graphic> startGraphic() {
			return Optional.empty();
		}

		@Override
		public Optional<Projectile> projectile(EntityNode cast, EntityNode castOn) {
			return Optional.empty();
		}

		@Override
		public Optional<Graphic> endGraphic() {
			return Optional.empty();
		}

		@Override
		public int levelRequired() {
			return 0;
		}

		@Override
		public double baseExperience() {
			return 0;
		}

		@Override
		public Optional<Item[]> itemsRequired(Player player) {
			return Optional.empty();
		}

		@Override
		public Optional<Item[]> equipmentRequired(Player player) {
			return Optional.empty();
		}

	};
	
	/**
	 * The set of combat spells which boost your hit.
	 */
	private static final ImmutableSet<CombatSpells> VALUES = ImmutableSet.of(CombatSpells.FIRE_BLAST, CombatSpells.FIRE_BOLT, CombatSpells.FIRE_STRIKE, CombatSpells.FIRE_WAVE, CombatSpells.FLAMES_OF_ZAMORAK);

}
