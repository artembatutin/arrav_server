package net.edge.world.entity.actor.update;

import net.edge.world.entity.actor.Actor;

import java.util.EnumSet;

/**
 * A container backed by an {@link EnumSet} that manages all of the {@link UpdateFlag}s for {@link Actor}s.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class UpdateFlagHolder {

	/**
	 * A boolean array defining flagged update flags.
	 */
	private boolean[] flagged = new boolean[11];

	/**
	 * Condition if none are flagged.
	 */
	private boolean empty = true;

	/**
	 * Adds {@code flag} to the backing {@link EnumSet}.
	 *
	 * @param flag The {@link UpdateFlag} to add.
	 */
	public void flag(UpdateFlag flag) {
		flagged[flag.getIndex()] = true;
		empty = false;
	}

	/**
	 * @return {@code true} if the backing {@link EnumSet} contains {@code flag}, false otherwise.
	 */
	public boolean get(UpdateFlag flag) {
		return flagged[flag.getIndex()];
	}

	/**
	 * @return {@code true} if the backing {@link EnumSet} is empty.
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * Clears the backing {@link EnumSet} of all elements.
	 */
	public void clear() {
		flagged = new boolean[11];
		empty = true;
	}
}
