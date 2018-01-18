package net.edge.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code PRIMARY_HIT} update block.
 * @author lare96 <http://github.org/lare96>
 */
public final class MobPrimaryHitUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobPrimaryHitUpdateBlock}.
	 */
	public MobPrimaryHitUpdateBlock() {
		super(2, UpdateFlag.PRIMARY_HIT);
	}
	
	@Override
	public int write(Player player, Mob mob, ByteBuf buf) {
		Hit hit = mob.getPrimaryHit();
		//		System.out.println("First hit: " + hit.getDamage());
		buf.putShort(hit.getDamage());
		buf.put(hit.getHitsplat().getId()); // TODO: add local hits (hit.hasSource() && hit.getSource() != player.getSlot() ? 5 : 0)
		buf.put(hit.getHitIcon().getId());
		buf.putShort((int) (mob.getCurrentHealth() * 100.0 / mob.getMaxHealth()));
		buf.put(mob.getSpecial().isPresent() ? mob.getSpecial().getAsInt() : 101);
		return -1;
	}
}