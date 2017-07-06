package net.edge.net.packet.in;

import net.edge.Server;
import net.edge.content.combat.magic.CombatSpells;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.pets.Pet;
import net.edge.content.skill.slayer.Slayer;
import net.edge.content.skill.summoning.Summoning;
import net.edge.event.EventContainer;
import net.edge.event.impl.NpcEvent;
import net.edge.locale.Boundary;
import net.edge.locale.Position;
import net.edge.locale.loc.Location;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.World;
import net.edge.world.node.entity.npc.Npc;
import net.edge.world.node.entity.npc.NpcDefinition;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;

import java.util.Optional;

/**
 * The message sent from the client when a player attacks or clicks on an NPC.
 * @author Artem Batutin <artembatutin@gmail.com
 */
public final class NpcActionPacket implements IncomingPacket {
	
	/*
	 * All of the npc events.
	 */
	public static final EventContainer<NpcEvent> FIRST = new EventContainer<>();
	public static final EventContainer<NpcEvent> SECOND = new EventContainer<>();
	public static final EventContainer<NpcEvent> THIRD = new EventContainer<>();
	public static final EventContainer<NpcEvent> FOURTH = new EventContainer<>();
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.NPC_ACTION))
			return;
		switch(opcode) {
			case 72:
				attackOther(player, payload);
				break;
			case 131:
				attackMagic(player, payload);
				break;
			case 155:
				firstClick(player, payload);
				break;
			case 17:
				secondClick(player, payload);
				break;
			case 21:
				thirdClick(player, payload);
				break;
			case 18:
				fourthClick(player, payload);
				break;
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.NPC_ACTION);
	}
	
	/**
	 * Handles the melee and ranged attacks on an NPC.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void attackOther(Player player, IncomingMsg payload) {
		int index = payload.getShort(false, ByteTransform.A);
		Npc npc = World.get().getNpcs().get(index - 1);
		if(npc == null || !checkAttack(player, npc))
			return;
		player.getTolerance().reset();
		player.getCombatBuilder().attack(npc);
	}
	
	/**
	 * Handles the magic attacks on an NPC.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void attackMagic(Player player, IncomingMsg payload) {
		int index = payload.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
		int spellId = payload.getShort(true, ByteTransform.A);
		Npc npc = World.get().getNpcs().get(index - 1);
		Optional<CombatSpells> spell = CombatSpells.getSpell(spellId);
		if(npc == null || !spell.isPresent() || !checkAttack(player, npc))
			return;
		player.setCastSpell(spell.get().getSpell());
		player.getTolerance().reset();
		player.getCombatBuilder().attack(npc);
	}
	
	/**
	 * Handles the first click NPC slot.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void firstClick(Player player, IncomingMsg payload) {
		int index = payload.getShort(true, ByteOrder.LITTLE);
		Npc npc = World.get().getNpcs().get(index - 1);
		if(npc == null)
			return;
		Position position = npc.getPosition().copy();
		if(npc.getId() == 4650) {
			player.getMovementQueue().smartWalk(new Position(3081, 3508));
		}
		player.getMovementListener().append(() -> {
			if(new Boundary(position, npc.size()).within(player.getPosition(), player.size(), npc.getId() == 4650 ? 3 : 1)) {
				player.facePosition(npc.getPosition());
				npc.facePosition(player.getPosition());
				if(!MinigameHandler.execute(player, m -> m.onFirstClickNpc(player, npc))) {
					return;
				}
				if(Summoning.interact(player, npc, 1)) {
					return;
				}
				if(Pet.pickup(player, npc)) {
					return;
				}
				NpcEvent e = FIRST.get(npc.getId());
				if(e != null) {
					e.click(player, npc, 1);
				}
			}
		});
		if(player.getRights().greater(Rights.ADMINISTRATOR) && Server.DEBUG)
			player.message("[NPC1]:" + npc.toString());
	}
	
	/**
	 * Handles the second click NPC slot.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void secondClick(Player player, IncomingMsg payload) {
		int index = payload.getShort(false, ByteTransform.A, ByteOrder.LITTLE);
		Npc npc = World.get().getNpcs().get(index - 1);
		if(npc == null)
			return;
		Position position = npc.getPosition().copy();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, npc.size()).within(player.getPosition(), player.size(), 1)) {
				player.facePosition(npc.getPosition());
				npc.facePosition(player.getPosition());
				if(!MinigameHandler.execute(player, m -> m.onSecondClickNpc(player, npc))) {
					return;
				}
				if(Summoning.interact(player, npc, 2)) {
					return;
				}
				NpcEvent e = SECOND.get(npc.getId());
				if(e != null) {
					e.click(player, npc, 2);
				}
			}
		});
		if(player.getRights().greater(Rights.ADMINISTRATOR) && Server.DEBUG)
			player.message("[NPC2]:" + npc.toString());
	}
	
	/**
	 * Handles the third click NPC slot.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void thirdClick(Player player, IncomingMsg payload) {
		int index = payload.getShort(true);
		Npc npc = World.get().getNpcs().get(index - 1);
		if(npc == null)
			return;
		Position position = npc.getPosition().copy();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, npc.size()).within(player.getPosition(), player.size(), 1)) {
				player.facePosition(npc.getPosition());
				npc.facePosition(player.getPosition());
				if(Summoning.interact(player, npc, 3)) {
					return;
				}
				NpcEvent e = THIRD.get(npc.getId());
				if(e != null) {
					e.click(player, npc, 3);
				}
			}
		});
		if(player.getRights().greater(Rights.ADMINISTRATOR) && Server.DEBUG)
			player.message("[NPC3]:" + npc.toString());
	}
	
	/**
	 * Handles the fourth click NPC slot.
	 * @param player  the player this will be handled for.
	 * @param payload the payload that will read the sent data.
	 */
	private void fourthClick(Player player, IncomingMsg payload) {
		int index = payload.getShort(true, ByteOrder.LITTLE);
		Npc npc = World.get().getNpcs().get(index - 1);
		if(npc == null)
			return;
		final int id = npc.getId();
		Position position = npc.getPosition();
		player.getMovementListener().append(() -> {
			if(new Boundary(position, npc.size()).within(player.getPosition(), player.size(), 1)) {
				player.facePosition(npc.getPosition());
				npc.facePosition(player.getPosition());
				
				if(Summoning.interact(player, npc, 4)) {
					return;
				}
				NpcEvent e = FOURTH.get(npc.getId());
				if(e != null) {
					e.click(player, npc, 4);
				}
			}
		});
		if(player.getRights().greater(Rights.ADMINISTRATOR) && Server.DEBUG)
			player.message("[NPC4]:" + npc.toString());
	}
	
	/**
	 * Determines if {@code player} can make an attack on {@code npc}.
	 * @param player the player attempting to make an attack.
	 * @param npc    the npc being attacked.
	 * @return {@code true} if the player can make an attack, {@code false}
	 * otherwise.
	 */
	private boolean checkAttack(Player player, Npc npc) {
		if(!NpcDefinition.DEFINITIONS[npc.getId()].isAttackable())
			return false;
		if(!Location.inMultiCombat(player) && player.getCombatBuilder().isBeingAttacked() && !npc.same(player.getCombatBuilder().getAggressor())) {
			player.message("You are already under attack!");
			return false;
		}
		if(!Slayer.canAttack(player, npc)) {
			return false;
		}
		return true;
	}
}
