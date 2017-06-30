package net.edge.content.minigame.pestcontrol.defence;

import net.edge.content.door.Door;
import net.edge.content.skill.Skills;
import net.edge.locale.Position;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.object.ObjectNode;


public class PestGate {
	
	/**
	 * The door states.
	 */
	private final static int[][] STATES = {
			{14233, 14237, 14241},
			{14235, 14239, 14244},
	};
	
	/**
	 * The door instance.
	 */
	private final Door door;
	
	/**
	 * The damage of the door.
	 */
	private int damage = 0;
	
	/**
	 * The two positions of the two gates.
	 */
	private Position[] positions;
	
	public PestGate(ObjectNode clicked) {
		door = new Door(clicked);
		positions = new Position[]{door.getCurrentOne(), door.getCurrentSecond()};
	}
	
	public void repair(Player player) {
		if(!player.getInventory().containsAny(2347, 2949)) {
			player.message("You need a hammer to repair the gates.");
			return;
		}
		if(!player.getInventory().contains(new Item(1511))) {
			player.message("You do not have any logs to fix the gates.");
			return;
		}
		damage--;
		if(damage < 0)
			damage = 0;
		door.setAppendId(STATES[0][damage]);
		door.setAppendedSecondId(STATES[1][damage]);
		door.setOriginalId(STATES[0][damage]);
		door.setOriginalSecondId(STATES[1][damage]);
		door.publish();
		player.animation(new Animation(3684));
		player.getInventory().remove(new Item(1511));
		player.getSkills()[Skills.CONSTRUCTION].increaseExperience(20);
		player.getAttr().get("participation").set(player.getAttr().get("participation").getInt() + 25);
		player.getMessages().sendString("" + player.getAttr().get("participation").getInt(), 21116);
	}
	
	public void damage() {
		//20% chance to brake stage.
		if(RandomUtils.inclusive(5) == 1) {
			damage++;
			if(damage > 2)
				damage = 2;
			door.setAppendId(STATES[0][damage]);
			door.setAppendedSecondId(STATES[1][damage]);
			door.setOriginalId(STATES[0][damage]);
			door.setOriginalSecondId(STATES[1][damage]);
			if(damage == 2 && !door.isAppend()) {
				door.append(null);//opening door if broken.
				positions = new Position[]{door.getCurrentOne(), door.getCurrentSecond()};
			} else
				door.publish();
		}
	}
	
	public void click(Player player) {
		if(damage == 2) {
			player.message("The gates are broken, fix them first.");
			return;
		}
		door.append(player);
		positions = new Position[]{door.getCurrentOne(), door.getCurrentSecond()};
	}
	
	public void reset() {
		door.setAppendId(STATES[0][0]);
		door.setAppendedSecondId(STATES[1][0]);
		door.setOriginalId(STATES[0][0]);
		door.setOriginalSecondId(STATES[1][0]);
		if(door.isAppend()) {
			//closing doors.
			door.append(null);
			positions = new Position[]{door.getCurrentOne(), door.getCurrentSecond()};
		} else {
			door.publish();
		}
		damage = 0;
	}
	
	public boolean clicked(Position position) {
		return position.same(positions[0]) || position.same(positions[1]);
	}
	
	public Position getPos() {
		return positions[0];
	}
	
	public boolean destroyed() {
		return damage == 2;
	}
}
