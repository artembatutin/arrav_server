package com.rageps.world.entity.sync.task;

import com.rageps.net.refactor.packet.out.model.update.PlayerSynchronizationPacket;
import com.rageps.world.entity.EntityState;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.region.Region;
import com.rageps.world.entity.sync.block.AppearanceBlock;
import com.rageps.world.entity.sync.block.ChatBlock;
import com.rageps.world.entity.sync.block.SynchronizationBlock;
import com.rageps.world.entity.sync.block.SynchronizationBlockSet;
import com.rageps.world.entity.sync.seg.*;
import com.rageps.world.locale.Position;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A {@link SynchronizationTask} which synchronizes the specified {@link Player} .
 *
 * @author Graham
 */
public final class PlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The maximum amount of local players.
	 */
	private static final int MAXIMUM_LOCAL_PLAYERS = 255;

	/**
	 * The maximum number of players to load per cycle. This prevents the update packet from becoming too large (the
	 * client uses a 5000 byte buffer) and also stops old spec PCs from crashing when they login or teleport.
	 */
	private static final int NEW_PLAYERS_PER_CYCLE = 20;

	/**
	 * The Player.
	 */
	private final Player player;

	/**
	 * Creates the {@link PlayerSynchronizationTask} for the specified {@link Player}.
	 *
	 * @param player The Player.
	 */
	public PlayerSynchronizationTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {


		player.update();

		Position lastKnownRegion = player.getLastRegion();
		boolean regionChanged = player.isNeedsPlacement();
		int[] appearanceTickets = player.getAppearanceTickets();

		SynchronizationBlockSet blockSet = player.getBlockSet();

		if (blockSet.contains(ChatBlock.class)) {
			blockSet = blockSet.clone();
			blockSet.remove(ChatBlock.class);
		}

		Position position = player.getPosition();

		SynchronizationSegment segment = (player.isTeleporting() || regionChanged) ? new TeleportSegment(blockSet, position) : new MovementSegment(blockSet, player.getDirections());

		Set<Player> localPlayers = player.getLocalPlayers();
		int oldCount = localPlayers.size();

		List<SynchronizationSegment> segments = new ArrayList<>();
		int distance = player.getViewingDistance();

		for (Iterator<Player> iterator = localPlayers.iterator(); iterator.hasNext(); ) {
			Player other = iterator.next();

			if (removeable(position, distance, other)) {
				iterator.remove();
				segments.add(new RemoveMobSegment());
			} else {
				segments.add(new MovementSegment(other.getBlockSet(), other.getDirections()));
			}
		}




		int added = 0;
		Region r = player.getRegion();
		if(r != null) {
			processPlayers(r, player, added, appearanceTickets, segments);
			ObjectList<Region> surrounding = r.getSurroundingRegions();
			if(surrounding != null) {
				for(Region s : surrounding) {
					processPlayers(s, player, added, appearanceTickets, segments);
				}
			}
		}

		PlayerSynchronizationPacket message = new PlayerSynchronizationPacket(lastKnownRegion, position, regionChanged, segment, oldCount, segments);
		player.send(message);
	}


	/**
	 * Processing the addition of player from a region.
	 */
	private void processPlayers(Region region, Player player, int added, int[] appearanceTickets, List<SynchronizationSegment> segments) {
		if(region != null) {
			if(!region.getPlayers().isEmpty()) {
				SynchronizationBlockSet blockSet;
				for(Player other : region.getPlayers()) {
					if(added == 15 || player.getLocalPlayers().size() >= 255)
						break;
					if(other == null || other.same(player))
						continue;
					if(other.getState() != EntityState.ACTIVE)
						continue;
					if(other.getInstance() != player.getInstance())
						continue;
					if(other.isVisible() && other.getPosition().isViewableFrom(player.getPosition())) {
						if(player.getLocalPlayers().add(other)) {
							added++;
							blockSet = other.getBlockSet();

							int index = other.getSlot();

							if (!blockSet.contains(AppearanceBlock.class) && !hasCachedAppearance(appearanceTickets, index - 1, other.getAppearanceTicket())) {
								blockSet = blockSet.clone();
								blockSet.add(SynchronizationBlock.createAppearanceBlock(other));
							}

							segments.add(new AddPlayerSegment(blockSet, index, other.getPosition()));
						}
					}
				}
			}
		}
	}

	/**
	 * Tests whether or not the specified Player has a cached appearance within
	 * the specified appearance ticket array.
	 * 
	 * @param appearanceTickets The appearance tickets.
	 * @param index The index of the Player.
	 * @param appearanceTicket The current appearance ticket for the Player.
	 * @return {@code true} if the specified Player has a cached appearance
	 *         otherwise {@code false}.
	 */
	private boolean hasCachedAppearance(int[] appearanceTickets, int index, int appearanceTicket) {
		if (appearanceTickets[index] != appearanceTicket) {
			appearanceTickets[index] = appearanceTicket;
			return false;
		}

		return true;
	}

	/**
	 * Returns whether or not the specified {@link Player} should be removed.
	 *
	 * @param position The {@link Position} of the Player being updated.
	 * @param other The Player being tested.
	 * @return {@code true} iff the specified Player should be removed.
	 */
	private boolean removeable(Position position, int distance, Player other) {
		if (other.isTeleporting() || !other.active()) {
			return true;
		}

		Position otherPosition = other.getPosition();
		return otherPosition.getLongestDelta(position) > distance || !otherPosition.withinDistance(position, distance);
	}

}