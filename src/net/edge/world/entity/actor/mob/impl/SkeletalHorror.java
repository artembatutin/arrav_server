package net.edge.world.entity.actor.mob.impl;

import net.edge.action.impl.ObjectAction;
import net.edge.content.minigame.hororis.Hororis;
import net.edge.world.Hit;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.mob.strategy.impl.SkeletalHorrorStrategy;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;
import net.edge.world.object.GameObject;

import java.util.Optional;

/**
 * The class which represents the skeletal horror.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class SkeletalHorror extends Mob {
	
	/**
	 * The horroris minigame of this skeletal horror.
	 */
	public static Hororis horroris;
	
	/**
	 * The stage of the skeletal horror.
	 */
	private int stage;
	
	/**
	 * Constructs a new {@link SkeletalHorror}.
	 */
	public SkeletalHorror() {
		super(9177, new Position(3386, 3517));
		setStrategy(Optional.of(new SkeletalHorrorStrategy(this)));
		horroris = new Hororis(this);
		stage = 9177;
	}
	
	@Override
	public Mob create() {
		return new SkeletalHorror();
	}
	
	@Override
	public void appendDeath() {
		super.appendDeath();
		horroris.over();
		horroris = null;
	}
	
	@Override
	public Hit decrementHealth(Hit hit) {
		if(hit.getDamage() > getCurrentHealth()) {
			hit.setDamage(getCurrentHealth());
			if(hit.getType() == Hit.HitType.CRITICAL) {
				hit.setType(Hit.HitType.NORMAL);
			}
		}
		setCurrentHealth(getCurrentHealth() - hit.getDamage());
		if(getCurrentHealth() <= 0) {
			setCurrentHealth(0);
		}
		if(stage == 9177 && getCurrentHealth() < 3000) {
			stage = 9178;
			transform(stage);
			horroris.message("The skeletal horror lost his arm!");
			horroris.drop(9181);
		} else if(stage == 9178 && getCurrentHealth() < 2000) {
			stage = 9179;
			transform(stage);
			horroris.message("The skeletal horror lost his other arm!");
			horroris.drop(9182);
		} else if(stage == 9179 && getCurrentHealth() < 1000) {
			stage = 9180;
			transform(stage);
			horroris.message("The skeletal horror lost his tail!");
			horroris.drop(9183);
		}
		return hit;
	}
	
	public static void action() {
		ObjectAction join = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				if(horroris == null)
					return true;
				horroris.onEnter(player);
				return true;
			}
		};
		join.registerFirst(86381);
		
	}
	
}
