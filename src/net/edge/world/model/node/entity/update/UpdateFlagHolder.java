package net.edge.world.model.node.entity.update;

import net.edge.world.model.node.entity.EntityNode;

import java.util.EnumSet;

/**
 * A container backed by an {@link EnumSet} that manages all of the {@link UpdateFlag}s for {@link EntityNode}s.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class UpdateFlagHolder {
	
	/**
	 * An {@link EnumSet} that will contain all active {@link UpdateFlag}s.
	 */
	private final EnumSet<UpdateFlag> flags = EnumSet.noneOf(UpdateFlag.class);
	
	/**
	 * Adds {@code flag} to the backing {@link EnumSet}.
	 * @param flag The {@link UpdateFlag} to add.
	 */
	public void flag(UpdateFlag flag) {
		flags.add(flag);
	}
	
	/**
	 * Removes {@code flag} from the backing {@link EnumSet}.
	 * @param flag The {@link UpdateFlag} to remove.
	 */
	public void unflag(UpdateFlag flag) {
		flags.remove(flag);
	}
	
	/*
	 * @return {@code true} if the backing {@link EnumSet} contains {@code flag}, false otherwise.
	 */
	public boolean get(UpdateFlag flag) {
		return flags.contains(flag);
	}
	
	/**
	 * @return {@code true} if the backing {@link EnumSet} is empty.
	 */
	public boolean isEmpty() {
		return flags.isEmpty();
	}
	
	/**
	 * Clears the backing {@link EnumSet} of all elements.
	 */
	public void clear() {
		flags.clear();
	}
}
