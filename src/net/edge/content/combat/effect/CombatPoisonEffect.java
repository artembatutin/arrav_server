package net.edge.content.combat.effect;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.edge.net.packet.out.SendConfig;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Hit;
import net.edge.world.PoisonType;
import net.edge.world.node.entity.npc.NpcDefinition;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

/**
 * The combat effect applied when a character needs to be poisoned.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatPoisonEffect extends CombatEffect {
	
	/**
	 * The collection of weapons mapped to their respective poison types.
	 */
	public static final Int2ObjectArrayMap<PoisonType> TYPES = new Int2ObjectArrayMap<>();
	
	/**
	 * The amount of times this player has been hit.
	 */
	private int amount;
	
	/**
	 * Creates a new {@link CombatPoisonEffect}.
	 */
	CombatPoisonEffect() {
		super(30);
	}
	
	@Override
	public boolean apply(EntityNode t) {
		if(t.isPoisoned() || t.getPoisonType() == null)
			return false;
		if(t.isPlayer()) {
			Player player = (Player) t;
			if(player.getPoisonImmunity().get() > 0 || t.isDead())
				return false;
			player.out(new SendConfig(174, 1));
			player.message("You have been poisoned!");
		}
		t.getPoisonDamage().set(t.getPoisonType().getDamage());
		return true;
	}
	
	@Override
	public boolean removeOn(EntityNode t) {
		return !t.isPoisoned() || t.isDead();
	}
	
	@Override
	public void process(EntityNode t) {
		amount--;
		t.damage(new Hit(t.getPoisonDamage().get(), Hit.HitType.POISON, Hit.HitIcon.NONE));
		if(amount == 0) {
			amount = 4;
			t.getPoisonDamage().decrementAndGet();
		}
	}
	
	@Override
	public boolean onLogin(EntityNode t) {
		return t.isPoisoned();
	}
	
	/**
	 * Gets the {@link PoisonType} for {@code item} wrapped in an optional. If a
	 * poison type doesn't exist for the item then an empty optional is
	 * returned.
	 * @param item the item to get the poison type for.
	 * @return the poison type for this item wrapped in an optional, or an empty
	 * optional if no poison type exists.
	 */
	public static Optional<PoisonType> getPoisonType(Item item) {
		if(item == null || item.getId() < 1 || item.getAmount() < 1)
			return Optional.empty();
		return Optional.ofNullable(TYPES.get(item.getId()));
	}
	
	/**
	 * Gets the {@link PoisonType} for {@code npc} wrapped in an optional. If a
	 * poison type doesn't exist for the NPC then an empty optional is returned.
	 * @param npc the NPC to get the poison type for.
	 * @return the poison type for this NPC wrapped in an optional, or an empty
	 * optional if no poison type exists.
	 */
	public static Optional<PoisonType> getPoisonType(int npc) {
		NpcDefinition def = NpcDefinition.DEFINITIONS[npc];
		if(def == null || !def.isAttackable() || !def.isPoisonous())
			return Optional.empty();
		if(def.getCombatLevel() < 75)
			return Optional.of(PoisonType.DEFAULT_NPC);
		if(def.getCombatLevel() < 200)
			return Optional.of(PoisonType.STRONG_NPC);
		return Optional.of(PoisonType.SUPER_NPC);
	}
}
