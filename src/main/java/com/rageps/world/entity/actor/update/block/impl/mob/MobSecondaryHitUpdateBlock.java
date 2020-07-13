package com.rageps.world.entity.actor.update.block.impl.mob;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.block.MobUpdateBlock;
import com.rageps.world.entity.actor.update.UpdateFlag;

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
		//Hit hit = mob.getSecondaryHit();
		////		System.out.println("Second hit: " + hit.getDamage());
		//buf.putShort(hit.getDamage());
		//buf.put(hit.getHitsplat().getId()); // TODO: add (hit.hasSource() && hit.getSource() != player.getSlot() ? 5 : 0)
		//buf.put(hit.getHitIcon().getId());
		//buf.putShort((int) (mob.getCurrentHealth() * 100.0 / mob.getMaxHealth()));
		//buf.put(mob.getSpecial().isPresent() ? mob.getSpecial().getAsInt() : 101);

		Hit hit = mob.getSecondaryHit();
		boolean local = (hit.hasSource() && hit.getSource() == player.getSlot());
		buf.putInt(hit.getDamage());
		buf.put(hit.getHitsplat().getId() + (!local ? (hit.getHitsplat() != Hitsplat.NORMAL_LOCAL ? 5 : 0) : 0));
		buf.put(hit.getHitIcon().getId());
		buf.putInt(hit.getSoak());
		buf.putInt(mob.getMaxHealth()/ 10);
		buf.putInt(mob.getCurrentHealth() / 10);
		buf.put(mob.getSpecial().isPresent() ? mob.getSpecial().getAsInt() : 101);
		return -1;
	}
}