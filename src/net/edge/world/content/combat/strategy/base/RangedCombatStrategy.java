package net.edge.world.content.combat.strategy.base;

import net.edge.world.content.combat.Combat;
import net.edge.world.content.combat.CombatSessionData;
import net.edge.world.content.combat.CombatType;
import net.edge.world.content.combat.ranged.CombatRangedAmmoDefinition;
import net.edge.world.content.combat.ranged.CombatRangedDetails.CombatRangedAmmo;
import net.edge.world.content.combat.ranged.CombatRangedDetails.CombatRangedWeapon;
import net.edge.world.content.combat.strategy.CombatStrategy;
import net.edge.world.content.combat.weapon.FightStyle;
import net.edge.world.content.combat.weapon.WeaponAnimation;
import net.edge.world.content.combat.weapon.WeaponInterface;
import net.edge.world.content.container.impl.Equipment;
import net.edge.world.content.minigame.MinigameHandler;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.model.Animation;
import net.edge.world.node.entity.model.Animation.AnimationPriority;
import net.edge.world.node.entity.model.Projectile;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.update.UpdateFlag;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemIdentifiers;

/**
 * The strategy class which holds support for ranged combat.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class RangedCombatStrategy implements CombatStrategy {
	
	@Override
	public boolean canOutgoingAttack(EntityNode character, EntityNode victim) {
		if(character.isNpc()) {
			return true;
		}
		
		Player player = character.toPlayer();
		
		if(!MinigameHandler.execute(player, m -> m.canHit(player, victim, CombatType.RANGED))) {
			return false;
		}
		
		if(!prerequisites(player)) {
			return false;
		}
		
		player.getCombatBuilder().setCombatType(CombatType.RANGED);
		return true;
	}
	
	@Override
	public CombatSessionData outgoingAttack(EntityNode character, EntityNode victim) {
		if(character.isNpc()) {
			Npc npc = character.toNpc();
			character.animation(new Animation(npc.getDefinition().getAttackAnimation()));
			
			CombatRangedAmmoDefinition ammo = prepareAmmo(npc.getId());
			
			if(ammo.getGraphic().getId() != 0)
				character.graphic(ammo.getGraphic());
			
			new Projectile(character, victim, ammo.getProjectile(), ammo.getDelay(), ammo.getSpeed(), ammo.getStartHeight(), ammo.getEndHeight(), 0).sendProjectile();
			return new CombatSessionData(character, victim, 1, CombatType.RANGED, true);
		}
		
		Player player = character.toPlayer();
		
		CombatRangedWeapon weapon = player.getRangedDetails().getWeapon().get();
		
		CombatRangedAmmo ammo = weapon.getAmmunition();
		
		if(!player.isSpecialActivated()) {
			if(!player.isVisible()) {
				return new CombatSessionData(character, victim, 1, CombatType.RANGED, true);
			}
			
			if(weapon.getWeapon() == ItemIdentifiers.DARK_BOW) {
				new Projectile(character, victim, ammo.getDefinition().getProjectile(), 64, 36, 40, 31, 0).sendProjectile();
			} else {
				if(ammo.getDefinition().getProjectile() != -1) {
					new Projectile(character, victim, ammo.getDefinition().getProjectile(), ammo.getDefinition().getDelay(), ammo.getDefinition().getSpeed(), ammo.getDefinition().getStartHeight(), ammo.getDefinition().getEndHeight(), 0).sendProjectile();
				}
			}
			
		}
		startAnimation(player);
		if(ammo.getDefinition().getGraphic(player).getId() != 0)
			player.graphic(ammo.getDefinition().getGraphic(player));
		
		CombatSessionData data = ammo.getDefinition().applyEffects(player, weapon, victim, new CombatSessionData(character, victim, 1, CombatType.RANGED, true));
		
		decrementAmmo(player, weapon, ammo);
		
		return data;
	}
	
	@Override
	public int attackDelay(EntityNode character) {
		return character.isPlayer() ? character.toPlayer().getRangedDetails().delay() : character.getAttackSpeed();
	}
	
	@Override
	public int attackDistance(EntityNode character) {
		if(character.isNpc())
			return 6;
		Player player = (Player) character;
		return Combat.getRangedDistance(player.getWeapon()) + (player.getFightType().getStyle() == FightStyle.DEFENSIVE ? 2 : 0);
	}
	
	@Override
	public int[] getNpcs() {
		return new int[]{6276, 6256, 6220, 688, 1183, 8781, 8776};
	}
	
	private void startAnimation(Player player) {
		if(player.getWeaponAnimation() != null) {
			player.animation(new Animation(player.getWeaponAnimation().getAttacking()[player.getFightType().getStyle().ordinal()], AnimationPriority.HIGH));
		} else {
			player.animation(new Animation(player.getFightType().getAnimation(), Animation.AnimationPriority.HIGH));
		}
	}
	
	private CombatRangedAmmoDefinition prepareAmmo(int id) {
		switch(id) {
			case 8776:
				return CombatRangedAmmoDefinition.HAND_CANNON_SHOT;
			case 1183:
				return CombatRangedAmmoDefinition.CRYSTAL_ARROW;
			case 8781:
				return CombatRangedAmmoDefinition.BLACK_BOLTS;
			default:
				return CombatRangedAmmoDefinition.BRONZE_ARROW;
		}
	}
	
	private boolean prerequisites(Player player) {
		return player.getRangedDetails().determine();
	}
	
	private void decrementAmmo(Player player, CombatRangedWeapon weapon, CombatRangedAmmo ammo) {
		if(weapon.getType().isSpecialBow()) {
			return;
		}
		
		Item item = ammo.getItem();
		
		if(item == null) {
			throw new IllegalStateException("Player doesn't have ammunition at this stage which is not permissible.");
		}
		
		item.decrementAmount();
		
		//Item cape = player.getEquipment().get(Equipment.CAPE_SLOT);
		//if(cape != null && cape.getId() == ItemIdentifiers.AVAS_ACCUMULATOR && gen.inclusive(4) == 1) {
		//	item.incrementAmount();
		//}
		
		int slot = weapon.getType().checkAmmunition() ? Equipment.ARROWS_SLOT : Equipment.WEAPON_SLOT;
		
		if(item.getAmount() == 0) {
			player.message("That was your last piece of ammunition!");
			
			player.getEquipment().set(slot, null, true);
			
			if(slot == Equipment.WEAPON_SLOT) {
				WeaponInterface.execute(player, null);
				WeaponAnimation.execute(player, new Item(0));
			}
		}
		
		if(slot == Equipment.WEAPON_SLOT) {
			player.getFlags().flag(UpdateFlag.APPEARANCE);
		}
		
		player.getEquipment().refresh(player, Equipment.EQUIPMENT_DISPLAY_ID);
	}
}
