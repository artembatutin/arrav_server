package net.arrav.util.log.impl;

import net.arrav.util.TextUtils;
import net.arrav.util.log.LogDetails;
import net.arrav.world.entity.actor.mob.MobDefinition;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;

import java.util.Optional;

/**
 * The class which represents a drop log.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class MobDropLog extends LogDetails {

	/**
	 * The npc whom dropped this item for this drop log.
	 */
	private final MobDefinition npc;

	/**
	 * The item dropped for this drop log.
	 */
	private final Item item;

	/**
	 * Constructs a new {@link MobDropLog}.
	 * @param npc  {@link #npc}.
	 * @param item {@link #item}.
	 */
	public MobDropLog(Player player, MobDefinition npc, Item item) {
		super(player.getFormatUsername(), "Drop");
		this.npc = npc;
		this.item = item;
	}

	@Override
	public Optional<String> formatInformation() {
		StringBuilder builder = new StringBuilder();
		builder.append("Got " + item.getAmount() + "x " + item.getDefinition().getName() + (item.getAmount() > 1 ? TextUtils.determinePluralCheck(item.getDefinition().getName()) : "") + (npc != null ? (" from " + npc.getName() + ".") : ""));
		return Optional.of(builder.toString());
	}
}
