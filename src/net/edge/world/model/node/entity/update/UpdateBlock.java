package net.edge.world.model.node.entity.update;

import com.google.common.base.MoreObjects;
import net.edge.net.codec.ByteMessage;
import net.edge.world.model.node.entity.EntityNode;
import net.edge.world.model.node.entity.player.Player;

import java.util.Objects;

/**
 * A single update block contained within an {@link UpdateBlockSet} and sent within a {@link Player} or {@link Npc} update
 * message. Implementations <strong>must be stateless</strong> so instances can be shared concurrently.
 * @author lare96 <http://github.org/lare96>
 */
public abstract class UpdateBlock<E extends EntityNode> {
	
	/**
	 * The bit mask for this update block.
	 */
	private final int mask;
	
	/**
	 * The update flag associated with this update block.
	 */
	private final UpdateFlag flag;
	
	/**
	 * Creates a new {@link UpdateBlock}.
	 * @param mask The bit mask for this update block.
	 * @param flag The update flag associated with this update block.
	 */
	public UpdateBlock(int mask, UpdateFlag flag) {
		this.mask = mask;
		this.flag = flag;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("name", flag.name()).toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(flag);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj instanceof UpdateBlock) {
			UpdateBlock<?> other = (UpdateBlock<?>) obj;
			return flag == other.flag;
		}
		return false;
	}
	
	/**
	 * Writes the data for this update block into {@code buf}.
	 * @param player The player for whom we are updating.
	 * @param mob    The {@link EntityNode} this update block is being written for.
	 * @param msg    The buffer to write the data to.
	 */
	public abstract int write(Player player, E mob, ByteMessage msg);
	
	/**
	 * @return The bit mask for this update block.
	 */
	public int getMask() {
		return mask;
	}
	
	/**
	 * @return The update flag associated with this update block.
	 */
	public UpdateFlag getFlag() {
		return flag;
	}
}
