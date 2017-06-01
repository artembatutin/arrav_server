package net.edge.world.node.entity.npc.impl.gwd;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.util.rand.RandomUtils;
import net.edge.World;
import net.edge.content.combat.CombatType;
import net.edge.locale.Position;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.player.Player;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * The enumerated type whose elements represent a set of constants used to differ
 * between minions of each godwars faction.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum GodwarsFaction {
	ARMADYL(0, 68429, new Position(2839, 5295, 2), new Position(2839, 5296, 2), 6222, 6246, 87, 11694, 11718, 11720, 11722, 12670, 12671),
	BANDOS(1, 26425, new Position(2863, 5354, 2), new Position(2864, 5354, 2), 6260, 6283, 11061, 11696, 11724, 11726, 11728),
	SARADOMIN(2, 68430, new Position(2908, 5265, 0), new Position(2907, 5265, 0), 6247, 6259, 1718, 2412, 2415, 2661, 2663, 2665, 2667, 3479, 3675, 3489, 3840, 4682, 6762, 8055, 10384, 10386, 10388, 10390, 10440, 10446, 10452, 10458, 10464, 10470, 11181, 11698, 11730),
	ZAMORAK(3, 26428, new Position(2925, 5332, 2), new Position(2925, 5331, 2), 6203, 6221, 11716, 11700, 1724, 2414, 2417, 2653, 2655, 2657, 2659, 3478, 3674, 3841, 3842, 3852, 4683, 6764, 8056, 10368, 10370, 10372, 10374, 10444, 10450, 10456, 10460, 10468, 10474, 10776, 10786, 10790);

	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<GodwarsFaction> VALUES = Sets.immutableEnumSet(EnumSet.allOf(GodwarsFaction.class));

	/**
	 * The id of this faction.
	 */
	private final int id;

	/**
	 * The door object id of this faction.
	 */
	private final int objectId;

	/**
	 * The position this player will stand on before entering the room.
	 */
	private final Position startPosition;

	/**
	 * The position this player will move to once he enters the room.
	 */
	private final Position chamberPosition;

	/**
	 * The start index of the npc array.
	 */
	private final int startId;

	/**
	 * The end index of the npc array.
	 */
	private final int endId;

	/**
	 * The items which will protect you from being attacked.
	 */
	private final int[] protectionItems;

	/**
	 * Constructs a new {@code GodwarsFaction}.
	 * @param id              {@link #id}.
	 * @param objectId        {@link #objectId}.
	 * @param startPosition   {@link #startPosition}.
	 * @param chamberPosition {@link #chamberPosition}.
	 * @param startId         {@link #startId}
	 * @param endId           {@link #endId}
	 * @param protectionItems {@link #protectionItems}
	 */
	GodwarsFaction(int id, int objectId, Position startPosition, Position chamberPosition, int startId, int endId, int... protectionItems) {
		this.id = id;
		this.objectId = objectId;
		this.startPosition = startPosition;
		this.chamberPosition = chamberPosition;
		this.startId = startId;
		this.endId = endId;
		this.protectionItems = protectionItems;
	}

	/**
	 * Attempts to increment the killcount for the specified {@code player}.
	 * @param player the player to increment the killcount for.
	 * @return {@code true} if the killcount was incremented, {@code false} otherwise.
	 */
	public static boolean increment(Player player, Npc npc) {
		GodwarsFaction faction = GodwarsFaction.getFaction(npc.getId()).orElse(null);

		if(faction == null) {
			return false;
		}

		if(!World.getAreaManager().inArea(npc, "GODWARS") || !World.getAreaManager().inArea(player, "GODWARS")) {
			return false;
		}

		if(player.getGodwarsKillcount()[faction.id] == 100) {
			player.getMessages().sendString("Max.", 16216 + faction.id);
			return true;
		}

		player.getGodwarsKillcount()[faction.id]++;
		player.getMessages().sendString(Integer.toString(player.getGodwarsKillcount()[faction.id]), 16216 + faction.id);
		return true;
	}

	/**
	 * Refreshes the godwars killcount interface.
	 * @param player the player to refresh this for.
	 */
	public static void refreshInterface(Player player) {
		for(int i = 0; i <= 3; i++) {
			player.getMessages().sendString(Integer.toString(player.getGodwarsKillcount()[i]), 16216 + i);
		}
	}

	/**
	 * Attempts to enter the godwars faction chamber.
	 * @param player   the player attempting to enter the chamber.
	 * @param objectId the object id interacted with.
	 * @return {@code true} if the player entered the chamber, {@code false} otherwise.
	 */
	public static boolean enterChamber(Player player, int objectId) {
		GodwarsFaction faction = VALUES.stream().filter(t -> t.objectId == objectId).findAny().orElse(null);

		if(faction == null) {
			return false;
		}

		if(player.getPosition().equals(faction.chamberPosition)) {
			player.move(faction.startPosition);
			return false;
		}

		if(!World.getAreaManager().inArea(player, "GODWARS")) {
			return false;
		}
		
		if(player.getPosition().equals(faction.startPosition) && faction.equals(GodwarsFaction.ZAMORAK)) {
			player.message("This door seems stuck...");//TODO
			//make zamorak boss visible.
			//add zamorak boss combat strategy and for it's minions.
			return false;
		}

		if(player.getGodwarsKillcount()[faction.id] < 20) {
			player.message("You need a killcount of 20 to get through the chamber");
			return false;
		}

		player.getGodwarsKillcount()[faction.id] -= 20;
		player.move(faction.chamberPosition);
		player.getMessages().sendString(Integer.toString(player.getGodwarsKillcount()[faction.id]), 16216 + faction.id);
		return true;
	}

	/**
	 * Prompts this npc to attack a random entity.
	 * @param npc the controller of this attack session.
	 * @return {@code true} if the attack was successful, {@code false} otherwise.
	 */
	public static boolean attack(Npc npc) {
		List<EntityNode> targets = getTargets(npc);
		if(targets.isEmpty()) {
			return false;
		}
		if(RandomUtils.inclusive(100) < 20) {
			return false;
		}
		npc.getCombatBuilder().attack(RandomUtils.random(targets));
		return true;
	}

	/**
	 * Checks if the {@cod npc} to attack the specified {@code target}.
	 * @param npc the npc that will attack.
	 * @return {@code true} if the npc made an attack, {@code false} otherwise.
	 */
	private static boolean canAttack(Npc npc, EntityNode target) {
		GodwarsFaction faction = GodwarsFaction.getFaction(npc.getId()).orElse(null);

		if(faction == null) {
			return false;
		}

		//some prerequisite checks.
		if(npc.getCombatBuilder().inCombat() || (target.isNpc() && target.getCombatBuilder().inCombat()) || npc.isDead() || npc.getState() != NodeState.ACTIVE || target.isDead() || target.getState() != NodeState.ACTIVE) {
			return false;
		}

		//if the difference is greater then 5 tiles block
		if(!npc.getPosition().withinDistance(target.getPosition(), 5)) {
			return false;
		}

		//if both entities are not in the godwars area block.
		if(!World.getAreaManager().inArea(npc, "GODWARS") || !World.getAreaManager().inArea(target, "GODWARS")) {
			return false;
		}

		Optional<GodwarsFaction> otherFaction = !target.isNpc() ? Optional.empty() : GodwarsFaction.getFaction(target.toNpc().getId());

		//if npc's faction and targets faction are equal they will not attack each other.
		if(!target.isPlayer() && (!otherFaction.isPresent() || (otherFaction.isPresent() && otherFaction.get().equals(faction)))) {
			return false;
		}

		//if the target is a player but hes wearing items which will protect him they will not attack.
		if(target.isPlayer() && GodwarsFaction.isProtected(target.toPlayer(), faction)) {
			return false;
		}

		return true;
	}

	/**
	 * Gets a list of targets this npc can attack.
	 * @param npc the npc to retrieve the targets for.
	 * @return a list of entities that can be attacked.
	 */
	private static List<EntityNode> getTargets(Npc npc) {
		List<EntityNode> targets = new ArrayList<>();

		World.getLocalNpcs(npc).forEachRemaining(n -> {
			if(n != null && canAttack(npc, n)) {
				targets.add(n);
			}
		});

		World.getLocalPlayers(npc).forEachRemaining(p -> {
			if(p != null && canAttack(npc, p)) {
				targets.add(p);
			}
		});

		return targets;
	}

	/**
	 * Determines if the character is an aviansie and can be attacked by a player
	 * using the melee combat style.
	 * @param character the character to check for.
	 * @param attacker  the attacker attempting to make an attack on character.
	 * @return {@code true} if the character can be attacked, {@code false} otherwise.
	 */
	public static boolean canBeAttacked(EntityNode character, EntityNode attacker) {
		if(!character.isNpc() || !attacker.isPlayer()) {
			return true;
		}

		GodwarsFaction faction = GodwarsFaction.getFaction(character.toNpc().getId()).orElse(null);

		if(faction == null) {
			return true;
		}

		if(faction.equals(GodwarsFaction.ARMADYL)) {
			if(attacker.isPlayer() && attacker.getCombatBuilder().getCombatType().equals(CombatType.MELEE)) {
				attacker.toPlayer().message("The aviansie is flying too high for you to attack using melee.");
				if(!character.getCombatBuilder().isAttacking()) {
					character.getCombatBuilder().attack(attacker);
				}
				//if the aviansie was attempted to be attacked it will attack back.
				return false;
			}
		}
		return true;
	}

	/**
	 * Searches the godwars faction enumerator dependant of the specified {@code id}.
	 * @param npcId the npc id to return the enumerator from.
	 * @return a godwars faction wrapped in an Optional, {@link Optional#empty()} otherwise.
	 */
	public static Optional<GodwarsFaction> getFaction(int npcId) {
		return VALUES.stream().filter(faction -> npcId >= faction.getStartId() && npcId <= faction.getEndId()).findAny();
	}

	/**
	 * Checks if the player is protected from this faction.
	 * @param player  the player to check for.
	 * @param faction the faction to check if the player is protected from.
	 * @return {@code true} if the player is protected, {@code false} otherwise.
	 */
	public static boolean isProtected(Player player, GodwarsFaction faction) {
		return player.getEquipment().containsAny(faction.protectionItems);
	}

	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the startId.
	 * @return The startId.
	 */
	public int getStartId() {
		return startId;
	}

	/**
	 * Gets the endId.
	 * @return The endId.
	 */
	public int getEndId() {
		return endId;
	}

	/**
	 * Gets the protectionItems.
	 * @return The protectionItems.
	 */
	public int[] getProtectionItems() {
		return protectionItems;
	}
}
