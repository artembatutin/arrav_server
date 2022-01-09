package com.rageps.world.entity.actor.update.block.impl.mob;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.world.entity.actor.combat.hit.Hitsplat;
import com.rageps.world.entity.actor.mob.Mob;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.update.UpdateFlag;
import com.rageps.world.entity.actor.update.block.MobUpdateBlock;

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
	public int write(Player player, Mob mob, GamePacket buf) {
		//Hit hit = mob.getPrimaryHit();
		////		System.out.println("First hit: " + hit.getDamage());
		//buf.putShort(hit.getDamage());
		//buf.put(hit.getHitsplat().getId()); // TODO: add local hits (hit.hasSource() && hit.getSource() != player.getSlot() ? 5 : 0)
		//buf.put(hit.getHitIcon().getId());
		//buf.putShort((int) (mob.getCurrentHealth() * 100.0 / mob.getMaxHealth()));
		//buf.put(mob.getSpecial().isPresent() ? mob.getSpecial().getAsInt() : 101);

		Hit hit = mob.getPrimaryHit();
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