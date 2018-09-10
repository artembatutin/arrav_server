package net.arrav.content.object;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.update.UpdateFlag;
import net.arrav.world.entity.region.Region;
import net.arrav.world.locale.Position;
import net.arrav.world.entity.object.DynamicObject;
import net.arrav.world.entity.object.GameObject;
import net.arrav.world.entity.object.StaticObject;

public class BarChair extends DynamicObject {
	
	public static final int SIT = 1351;
	public static final int SIT_THINK = 1352;
	public static final int SIT_SMOKE = 1353;
	
	public static final int TABLE_ID = 4651;
	public static final int CHAIR_ID = 4656;
	
	public static Object2ObjectArrayMap<Position, BarChair> CHAIRS = new Object2ObjectArrayMap<>();
	
	private Player sitter;
	private GameObject table;
	
	public BarChair(GameObject o) {
		super(o.toDynamic());
	}
	
	public boolean occupied() {
		return sitter != null;
	}
	
	public void sit(Player player) {
		if(occupied()) {
			player.message("This chair is already taken by someone else.");
			return;
		}
		final Position cp = getPosition();
		if(table == null) {
			Region r = getRegion();
			if(r != null) {
				r.interactAction(TABLE_ID, d -> {
					Position p = d.getPosition();
					if(cp.move(-1, 0).same(p) || cp.move(1, 0).same(p) || cp.move(0, 1).same(p) || cp.move(0, -1).same(p)) {
						table = d;
					}
				});
			}
		}
		player.move(cp);
		if(table != null) {
			player.facePosition(table.getPosition());
		}
		this.sitter = player;
		player.sitting = this;
		player.setStandIndex(SIT);
		player.getFlags().flag(UpdateFlag.APPEARANCE);
	}
	
	public void unsit() {
		sitter.setStandIndex(-1);
		sitter.getFlags().flag(UpdateFlag.APPEARANCE);
		sitter.sitting = null;
		sitter = null;
	}
	
	public static void action() {
		ObjectAction a = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				BarChair c = CHAIRS.get(object.getPosition());
				if(c != null)
					c.sit(player);
				return true;
			}
		};
		a.registerFirst(CHAIR_ID);
	}
	
	public static void register(StaticObject o) {
		CHAIRS.put(o.getPosition(), new BarChair(o));
	}
}
