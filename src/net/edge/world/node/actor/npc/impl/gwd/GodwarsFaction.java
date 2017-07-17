package net.edge.world.node.actor.npc.impl.gwd;

import net.edge.event.impl.ObjectEvent;
import net.edge.content.combat.CombatType;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.NodeState;
import net.edge.world.node.actor.Actor;
import net.edge.world.node.actor.npc.Npc;
import net.edge.world.node.actor.npc.NpcType;
import net.edge.world.node.actor.player.Player;
import net.edge.world.object.ObjectNode;

/**
 * The enumerated type whose elements represent a set of constants used to differ
 * between minions of each godwars faction.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum GodwarsFaction {
	ARMADYL(NpcType.ARMADYL_SOLDIER, 0, 68429, new Position(2839, 5295, 2), new Position(2839, 5296, 2), 6222, 6246, 87, 11694, 11718, 11720, 11722, 12670, 12671),
	BANDOS(NpcType.BANDOS_SOLIDER, 1, 26425, new Position(2863, 5354, 2), new Position(2864, 5354, 2), 6260, 6283, 11061, 11696, 11724, 11726, 11728),
	SARADOMIN(NpcType.SARADOMIN_SOLDIER, 2, 68430, new Position(2908, 5265, 0), new Position(2907, 5265, 0), 6247, 6259, 1718, 2412, 2415, 2661, 2663, 2665, 2667, 3479, 3675, 3489, 3840, 4682, 6762, 8055, 10384, 10386, 10388, 10390, 10440, 10446, 10452, 10458, 10464, 10470, 11181, 11698, 11730),
	ZAMORAK(NpcType.ZAMORAK_SOLDIER, 3, 26428, new Position(2925, 5332, 2), new Position(2925, 5331, 2), 6203, 6221, 11716, 11700, 1724, 2414, 2417, 2653, 2655, 2657, 2659, 3478, 3674, 3841, 3842, 3852, 4683, 6764, 8056, 10368, 10370, 10372, 10374, 10444, 10450, 10456, 10460, 10468, 10474, 10776, 10786, 10790);
	
	/**
	 * The npc type this faction represents.
	 */
	private final NpcType type;

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
	GodwarsFaction(NpcType type, int id, int objectId, Position startPosition, Position chamberPosition, int startId, int endId, int... protectionItems) {
		this.type = type;
		this.id = id;
		this.objectId = objectId;
		this.startPosition = startPosition;
		this.chamberPosition = chamberPosition;
		this.startId = startId;
		this.endId = endId;
		this.protectionItems = protectionItems;
	}
	
	public static void event() {
		for(GodwarsFaction faction : GodwarsFaction.values()) {
			ObjectEvent door = new ObjectEvent() {
				@Override
				public boolean click(Player player, ObjectNode object, int click) {
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
					player.text(16216 + faction.id, Integer.toString(player.getGodwarsKillcount()[faction.id]));
					return true;
				}
			};
			door.registerFirst(faction.objectId);
			door.registerSecond(faction.objectId);
			
			for(int n = faction.startId; n < faction.endId; n++) {
				int npcId = n;
				Npc.CUSTOM_NPCS.put(n, s -> new GodwarsSoldier(npcId, s, faction));
			}
		}
	}

	/**
	 * Attempts to increment the killcount for the specified {@code player}.
	 * @param player the player to increment the killcount for.
	 * @return {@code true} if the killcount was incremented, {@code false} otherwise.
	 */
	public static boolean increment(Player player, Npc npc) {
		NpcType t = npc.getNpcType();
		if(t != NpcType.ARMADYL_SOLDIER && t != NpcType.ZAMORAK_SOLDIER && t != NpcType.SARADOMIN_SOLDIER && t != NpcType.BANDOS_SOLIDER)
			return false;
		GodwarsSoldier s = (GodwarsSoldier) npc;
		if(!World.getAreaManager().inArea(s, "GODWARS") || !World.getAreaManager().inArea(player, "GODWARS")) {
			return false;
		}
		if(player.getGodwarsKillcount()[s.faction.id] == 100) {
			player.text(16216 + s.faction.id, "Max.");
			return true;
		}
		player.getGodwarsKillcount()[s.faction.id]++;
		player.text(16216 + s.faction.id, Integer.toString(player.getGodwarsKillcount()[s.faction.id]));
		return true;
	}

	/**
	 * Refreshes the godwars killcount interface.
	 * @param player the player to refresh this for.
	 */
	public static void refreshInterface(Player player) {
		for(int i = 0; i <= 3; i++) {
			player.text(16216 + i, Integer.toString(player.getGodwarsKillcount()[i]));
		}
	}

	/**
	 * Checks if the {@cod npc} to attack the specified {@code target}.
	 * @param soldier the npc that will attack.
	 * @return {@code true} if the npc made an attack, {@code false} otherwise.
	 */
	private static boolean canAttack(GodwarsSoldier soldier, GodwarsSoldier target) {
		//some prerequisite checks.
		if(soldier.getCombatBuilder().inCombat() || (target.isNpc() && target.getCombatBuilder().inCombat()) || soldier.isDead() || soldier.getState() != NodeState.ACTIVE || target.isDead() || target.getState() != NodeState.ACTIVE) {
			return false;
		}
		//if the difference is greater then 5 tiles block
		if(!soldier.getPosition().withinDistance(target.getPosition(), 3)) {
			return false;
		}
		//if npc's faction and targets faction are equal they will not attack each other.
		if(!target.isPlayer() && (soldier.faction == target.faction)) {
			return false;
		}
		return true;
	}

	/**
	 * Determines if the character is an aviansie and can be attacked by a player
	 * using the melee combat style.
	 * @param character the character to check for.
	 * @param attacker  the attacker attempting to make an attack on character.
	 * @return {@code true} if the character can be attacked, {@code false} otherwise.
	 */
	public static boolean canBeAttacked(Actor character, Actor attacker) {
		if(!character.isNpc() || !attacker.isPlayer()) {
			return true;
		}
		NpcType t = character.toNpc().getNpcType();
		if(t != NpcType.ARMADYL_SOLDIER && t != NpcType.ZAMORAK_SOLDIER && t != NpcType.SARADOMIN_SOLDIER && t != NpcType.BANDOS_SOLIDER)
			return true;
		if(((GodwarsSoldier) character).faction.equals(GodwarsFaction.ARMADYL)) {
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
	
	public static class GodwarsSoldier extends Npc {
		
		private int check = 0;
		
		private final GodwarsFaction faction;
		
		/**
		 * Creates a new {@link Npc}.
		 * @param id       the identification for this NPC.
		 * @param position the position of this character in the world.
		 */
		public GodwarsSoldier(int id, Position position, GodwarsFaction faction) {
			super(id, position);
			this.faction = faction;
		}
		
		@Override
		public Npc create() {
			return new GodwarsSoldier(getId(), getPosition(), faction);
		}
		
		@Override
		public void update() {
			check++;
			if(getCombatBuilder().inCombat())
				return;
			if(check % 20 == 1) {
				for(Player player : getRegion().getPlayers()) {
					//if the target is a player but hes wearing items which will protect him they will not attack.
					if(!GodwarsFaction.isProtected(player, faction)) {
						getCombatBuilder().attack(player);
					}
				}
			}
			if(check >= 150) {
				int count = 0;
				for(Npc npc : getRegion().getNpcs()) {
					count++;
					if(count >= 10)
						break;
					NpcType t = npc.getNpcType();
					if(t != NpcType.ARMADYL_SOLDIER && t != NpcType.ZAMORAK_SOLDIER && t != NpcType.SARADOMIN_SOLDIER && t != NpcType.BANDOS_SOLIDER)
						continue;
					if(canAttack(this, (GodwarsSoldier) npc)) {
						npc.setAutoRetaliate(true);
						getCombatBuilder().attack(npc);
						npc.getCombatBuilder().attack(this);
						break;
					}
				}
				check = 0;
			}
		}
		
		@Override
		public NpcType getNpcType() {
			return faction.type;
		}
	}
	
	
	
}
