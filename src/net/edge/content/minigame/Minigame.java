package net.edge.content.minigame;

import net.edge.util.rand.RandomUtils;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.special.CombatSpecial;
import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.content.container.impl.Equipment;
import net.edge.content.container.impl.EquipmentType;
import net.edge.content.item.FoodConsumable;
import net.edge.content.item.PotionConsumable;
import net.edge.content.skill.Skills;
import net.edge.content.skill.prayer.Prayer;
import net.edge.locale.Position;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.update.UpdateFlag;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNode;
import net.edge.world.object.ObjectNode;

import java.util.Optional;

/**
 * The class that provides all of the functionality needed for very generic
 * minigames. These types of minigames can usually be ran on their own meaning
 * they aren't dependent on some sort of sequencer or task.
 * @author lare96 <http://github.com/lare96>
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class Minigame {
	
	/**
	 * The string which defines the current minigame.
	 */
	private final String minigame;
	
	/**
	 * The current name of this minigame.
	 */
	private final MinigameSafety safety;
	
	/**
	 * The current type of this minigame.
	 */
	private final MinigameType type;
	
	/**
	 * Creates a new {@link Minigame}.
	 * @param minigame the enumerated type which defines the current minigame.
	 * @param safety   the current name of this minigame.
	 * @param type     the current type of this minigame;
	 */
	public Minigame(String minigame, MinigameSafety safety, MinigameType type) {
		this.minigame = minigame;
		this.safety = safety;
		this.type = type;
	}
	
	public Optional<Minigame> copy() {
		return Optional.empty();
	}
	
	/**
	 * Determines if the {@code player} can walk.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean canWalk(Player player) {
		return true;
	}
	
	/**
	 * Determines if this player can click the npc.
	 * @param player the player the message was sent for.
	 * @param npc    the npc that was clicked by the player.
	 * @return <true> if the player can click the npc, <false> otherwise.
	 */
	public boolean onFirstClickNpc(Player player, Npc npc) {
		player.message("You cannot interact with this npc in here!");
		return false;
	}
	
	/**
	 * Determines if this player can click the npc.
	 * @param player the player the message was sent for.
	 * @param npc    the npc that was clicked by the player.
	 * @return <true> if the player can click the npc, <false> otherwise.
	 */
	public boolean onSecondClickNpc(Player player, Npc npc) {
		player.message("You cannot interact with this npc in here!");
		return false;
	}
	
	/**
	 * The flag which determines functionality for right click menus.
	 * @return <true> if this minigame has one, <false> otherwise.
	 */
	public boolean canClickMenu(Player player, Player other) {
		return true;
	}
	
	/**
	 * The method executed when the first click object message is sent.
	 * @param player the player the message was sent for.
	 * @param object the object interacted with.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean onFirstClickObject(Player player, ObjectNode object) {
		player.message("You cannot interact with this object in here!");
		return false;
	}
	
	/**
	 * The method executed when the second click object message is sent.
	 * @param player the player the message was sent for.
	 * @param object the object interacted with.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean onSecondClickObject(Player player, ObjectNode object) {
		player.message("You cannot interact with this object in here!");
		return false;
	}
	
	/**
	 * The method executed when the third click object message is sent.
	 * @param player the player the message was sent for.
	 * @param object the object interacted with.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean onThirdClickObject(Player player, ObjectNode object) {
		player.message("You cannot interact with this object in here!");
		return false;
	}
	
	/**
	 * The method executed when the fourth click object message is sent.
	 * @param player the player the message was sent for.
	 * @param object the object interacted with.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean onFourthClickObject(Player player, ObjectNode object) {
		player.message("You cannot interact with this object in here!");
		return false;
	}
	
	/**
	 * The method executed when the fifth click object message is sent.
	 * @param player the player the message was sent for.
	 * @param object the object interacted with.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean onFifthClickObject(Player player, ObjectNode object) {
		player.message("You cannot interact with this object in here!");
		return false;
	}
	
	/**
	 * Determines if a player can use an item on an object.
	 * @param player the player to determine this for.
	 * @param object the object the item is being used on.
	 * @param item   the item being used on the object.
	 * @return {@code true} if the player can, {@code false} otherwise.
	 */
	public boolean onItemOnObject(Player player, ObjectNode object, Item item) {
		player.message("You cannot do this while in here!");
		return true;
	}
	
	/**
	 * The method executed when {@code player} utilises the {@link InterfaceClickMessage}.
	 * @param player the player clicking the interface.
	 */
	public void onInterfaceClick(Player player) {
		
	}
	
	/**
	 * The method executed when {@code player} kills {@code other}.
	 * @param player the player that killed another character.
	 * @param other  the character that was killed by the player.
	 */
	public void onKill(Player player, EntityNode other) {
		
	}
	
	/**
	 * The method executed when {@code player} dies.
	 * @param player the player that died within the minigame.
	 */
	public void onDeath(Player player) {
		
	}
	
	/**
	 * Determines if the player can drop the item.
	 * @param player the player to check for.
	 * @param item   the item being dropped.
	 * @param slot   the slot being dropped from.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean canDrop(Player player, Item item, int slot) {
		player.message("You cannot drop items in here!");
		return false;
	}
	
	/**
	 * Determines if the player can pick up an item from the floor.
	 * @param player the player attempting to pick up an item from the floor.
	 * @param node   the item node that is being picked up.
	 * @return {@code true} if the player can, {@code false} otherwise.
	 */
	public boolean canPickup(Player player, ItemNode node) {
		player.message("You cannot pick up items from the floor in here!");
		return false;
	}
	
	/**
	 * The method which handles functionality when a player picks up an item from the ground,
	 * this method differs with {@link #canPickup(Player, ItemNode)} as this method is executed
	 * after the item from the ground has been added to the inventory.
	 * @param player the player attempting to pick up an item from the floor.
	 * @param item   the item that was picked up.
	 */
	public void onPickup(Player player, Item item) {
		
	}
	
	/**
	 * Determines if this player can use prayers.
	 * @param player the player to check for.
	 * @param prayer the prayer activated.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean canPray(Player player, Prayer prayer) {
		return true;
	}
	
	/**
	 * Determines if this player can consume potions.
	 * @param player the player to check for.
	 * @param potion the food that the player attempted to consume.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean canPot(Player player, PotionConsumable potion) {
		player.message("You cannot drink potions in here!");
		return false;
	}
	
	/**
	 * Determines if this player can consume food.
	 * @param player the player to check for.
	 * @param food   the food that the player attempted to consume.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean canEat(Player player, FoodConsumable food) {
		player.message("You cannot eat food in here!");
		return false;
	}
	
	/**
	 * Determines if the player can bank.
	 * @param player the player to check for.
	 * @return <true> if the player can, <false>
	 */
	public boolean canBank(Player player) {
		player.message("You cannot bank in here!");
		return false;
	}
	
	/**
	 * Determines if {@code player} can equip {@code item} to
	 * {@code equipmentSlot}.
	 * @param playe the player attempting to equip the item.
	 * @param item  the item that is being equipped.
	 * @param type  the type of equipment being equipped.
	 * @return <true> if the player can equip the item, <false> otherwise.
	 */
	public boolean canEquip(Player player, Item item, EquipmentType type) {
		return true;
	}
	
	/**
	 * Determines if {@code player} can unequip {@code item} from
	 * {@code equipmentSlot}.
	 * @param player the player attempting to unequip the item.
	 * @param item   the item that is being unequipped.
	 * @param type   the type of equipment being unequipped.
	 * @return <true> if the player can equip the item, <false> otherwise.
	 */
	public boolean canUnequip(Player player, Item item, EquipmentType type) {
		return true;
	}
	
	/**
	 * Determines if {@code player} can trade {@code other}.
	 * @param player the player attempting to trade the other player.
	 * @param other  the player that is being traded with.
	 * @return <true> if the players can trade, <false> otherwise.
	 */
	public boolean canTrade(Player player, Player other) {
		player.message("You cannot trade in here!");
		return false;
	}
	
	/**
	 * Determines if {@code player} can attack {@code other}.
	 * @param player the player attempting to attack the character.
	 * @param other  the character that is being targeted.
	 * @param type   the combat type the other player is being hit with.
	 * @return <true> if the player can attack, <false> otherwise.
	 */
	public boolean canHit(Player player, EntityNode other, CombatType type) {
		return other.isNpc();
	}
	
	/**
	 * Determines if {@code player} can log out using the logout button.
	 * @param player the player attempting to logout.
	 * @return <true> if the player can logout, <false> otherwise.
	 */
	public boolean canLogout(Player player) {
		player.message("You cannot logout in here!");
		return false;
	}
	
	/**
	 * Determines if {@code player} can teleport somewhere.
	 * @param player   the player attempting to teleport.
	 * @param position the destination the player is teleporting to.
	 * @return <true> if the player can teleport, <false> otherwise.
	 */
	public boolean canTeleport(Player player, Position position) {
		player.message("You cannot teleport in here!");
		return false;
	}
	
	/**
	 * Determines if {@code player} can keep their items on death.
	 * @param player the player attempting to keep their items.
	 * @return <true> if the player can keep their items, <false> otherwise.
	 */
	public boolean canKeepItems(Player player) {
		return true;
	}
	
	/**
	 * Determines if {@code player} can use their special attacks.
	 * @param player the player attempting to use the special attack.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean canUseSpecialAttacks(Player player, CombatSpecial special) {
		return true;
	}
	
	/**
	 * Retrieves the position {@code player} will be moved to when they respawn.
	 * @param player the player who is being respawned.
	 * @return the position the player will be moved to on death.
	 */
	public Position deathPosition(Player player) {
		return new Position(3217 + RandomUtils.inclusive(4), 3430 + RandomUtils.inclusive(4));
	}
	
	/**
	 * The method which can be overriden to supply functionality whe the minigame
	 * doesn't {@link #contains(Player)} the player anymore
	 * @param player the player the functionality should be passed for.
	 */
	public void onDestruct(Player player) {
		
	}
	
	/**
	 * The method executed when {@code player} has logged in while in the
	 * minigame.
	 * @param player the player that has logged in.
	 */
	public abstract void onLogin(Player player);
	
	/**
	 * The method executed when {@code player} has disconnected while in the
	 * minigame.
	 * @param player the player that has logged out.
	 */
	public abstract void onLogout(Player player);
	
	/**
	 * Functionality that should be handled as soon as the player enters the
	 * minigame.
	 * @param player the player entering the minigame.
	 */
	public abstract void onEnter(Player player);
	
	/**
	 * Determines if {@code player} is in this minigame.
	 * @param player the player to determine this for.
	 * @return <true> if this minigame contains the player, <false> otherwise.
	 */
	public abstract boolean contains(Player player);
	
	/**
	 * The method which clears the players fields for restoring their skill levels,
	 * hitpoints, special attack amount and more.
	 */
	public final void restore(Player player) {
		player.getMessages().sendCloseWindows();
		player.getCombatBuilder().reset();
		player.getCombatBuilder().getDamageCache().clear();
		player.getTolerance().reset();
		player.getSpecialPercentage().set(100);
		player.getPoisonDamage().set(0);
		player.setRunEnergy(100D);
		player.getMessages().sendConfig(301, 0);
		player.setSpecialActivated(false);
		player.getSkullTimer().set(0);
		player.getTeleblockTimer().set(0);
		player.animation(new Animation(65535));
		player.getMessages().sendWalkable(-1);
		Prayer.deactivateAll(player);
		Skills.restoreAll(player);
		WeaponInterface.execute(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
		player.getFlags().flag(UpdateFlag.APPEARANCE);
	}
	
	/**
	 * @return the minigame
	 */
	public String getMinigame() {
		return minigame;
	}
	
	/**
	 * The enumerated type whose elements represent the minigame types.
	 * @author lare96 <http://github.com/lare96>
	 */
	public enum MinigameType {
		NORMAL,
		SEQUENCED
	}
	
	/**
	 * The enumerated type whose elements represent the item safety of a player
	 * who is playing the minigame.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum MinigameSafety {
		/**
		 * This safety is similar to when a player dies while he is skulled.
		 */
		DANGEROUS,
		/**
		 * Indicates the default safety is applied
		 */
		DEFAULT,
		/**
		 * Indicates the minigame is fully safe and no items will be lost on death
		 */
		SAFE;
	}
	
	/**
	 * Gets the current safety of this minigame.
	 * @return the safety of this minigame.
	 */
	public final MinigameSafety getSafety() {
		return safety;
	}
	
	/**
	 * Gets the current type of this minigame.
	 * @return the type of this minigame.
	 */
	public final MinigameType getType() {
		return type;
	}
}
