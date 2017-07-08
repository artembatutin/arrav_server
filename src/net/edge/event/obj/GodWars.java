package net.edge.event.obj;

import net.edge.content.combat.weapon.WeaponInterface;
import net.edge.content.skill.Skills;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.net.packet.out.SendFade;
import net.edge.task.LinkedTaskSequence;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.move.ForcedMovement;
import net.edge.world.node.entity.move.ForcedMovementDirection;
import net.edge.world.node.entity.move.ForcedMovementManager;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.TRAINING_PORTAL;

public class GodWars extends EventInitializer {
	@Override
	public void init() {
		//jump in water
		ObjectEvent l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.getActivityManager().disable();
				boolean north = player.getPosition().getY() < 5334;
				LinkedTaskSequence seq = new LinkedTaskSequence();
				seq.connect(2, () -> player.getMovementQueue().walk(new Position(2885, north ? 5333 : 5344, 2)));
				seq.connect(2, () -> player.out(new SendFade(130, 80, 120)));
				seq.connect(1, () -> {
					ForcedMovement movement = new ForcedMovement(player);
					movement.setSecond(new Position(2885, north ? 5334 : 5343, 2));
					movement.setSecondSpeed(50);
					movement.setAnimation(3067);
					movement.submit();
				});
				seq.connect(2, () -> {
					player.animation(new Animation(772));
					player.graphic(new Graphic(68));
				});
				seq.connect(4, () -> player.move(new Position(2885, north ? 5345 : 5332, 2)));
				seq.connect(1, () -> {
					player.getActivityManager().enable();
					player.getSkills()[Skills.PRAYER].setLevel(0, false);
					Skills.refresh(player, Skills.PRAYER);
				});
				seq.start();
				return true;
			}
		};
		l.registerFirst(26439);
		
		//graple
		l = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				if(!player.getEquipment().contains(9419)) {
					player.message("You need to wield a mithril grapple to cross this.");
					return true;
				}
				if(!player.getWeapon().equals(WeaponInterface.CROSSBOW)) {
					player.message("You need to wield a crossbow to fire a mithril grapple.");
					return true;
				}
				ForcedMovement movement = new ForcedMovement(player);
				movement.setFirstSpeed(95);
				movement.setAnimation(7081);
				movement.setSecondSpeed(120);
				if(player.getPosition().getY() < object.getY()) {
					movement.setDirection(ForcedMovementDirection.NORTH);
					movement.setFirst(new Position(2872, 5269, 2));
					movement.setSecond(new Position(2872, 5279, 2));
				} else {
					movement.setDirection(ForcedMovementDirection.SOUTH);
					movement.setFirst(new Position(2872, 5279, 2));
					movement.setSecond(new Position(2872, 5269, 2));
				}
				ForcedMovementManager.submit(player, movement);
				return true;
			}
		};
		l.registerFirst(68306);
	}
}
