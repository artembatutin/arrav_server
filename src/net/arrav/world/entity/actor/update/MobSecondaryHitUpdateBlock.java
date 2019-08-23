package net.arrav.world.entity.actor.update;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.entity.actor.player.Player;

/**
 * An {@link MobUpdateBlock} implementation that handles the {@code SECONDARY_HIT} update block.
 * @author Artem Batutin
 */
public final class MobSecondaryHitUpdateBlock extends MobUpdateBlock {
	
	/**
	 * Creates a new {@link MobSecondaryHitUpdateBlock}.
	 */
	public MobSecondaryHitUpdateBlock() {
		super(0x20, UpdateFlag.SECONDARY_HIT);
	}
	
	@Override
	public int write(Player player, Mob mob, GamePacket buf) {
		Hit hit = mob.getSecondaryHit();
		//		System.out.println("Second hit: " + hit.getDamage());
		buf.putShort(hit.getDamage());
		buf.put(hit.getHitsplat().getId()); // TODO: add (hit.hasSource() && hit.getSource() != player.getSlot() ? 5 : 0)
		buf.put(hit.getHitIcon().getId());
		buf.putShort((int) (mob.getCurrentHealth() * 100.0 / mob.getMaxHealth()));
		buf.put(mob.getSpecial().isPresent() ? mob.getSpecial().getAsInt() : 101);
		return -1;
	}
}