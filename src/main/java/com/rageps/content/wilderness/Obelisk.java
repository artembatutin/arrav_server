package com.rageps.content.wilderness;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rageps.world.World;
import com.rageps.action.impl.ObjectAction;
import com.rageps.task.Task;
import com.rageps.util.rand.RandomUtils;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Position;
import com.rageps.world.locale.loc.SquareArea;

import java.util.EnumSet;

import static com.rageps.content.teleport.TeleportType.OBELISK;

/**
 * Handles the wilderness obelisks.
 */
public enum Obelisk {
	LEVEL_FIFTY(14831, new SquareArea(3307, 3916, 0, 2)),
	LEVEL_SEVENTEEN(14830, new SquareArea(3219, 3656, 0, 2)),
	LEVEL_THIRTEEN(14829, new SquareArea(3156, 3620, 0, 2)),
	LEVEL_THIRTY_FIVE(14828, new SquareArea(3106, 3794, 0, 2)),
	LEVEL_TWENTY_SEVEN(14827, new SquareArea(3035, 3732, 0, 2)),
	LEVEL_FORTY_FOUR(14826, new SquareArea(2980, 3866, 0, 2));
	
	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<Obelisk> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Obelisk.class));
	
	/**
	 * The obelisk object id.
	 */
	private final int object;
	
	/**
	 * The obelisk's boundary in a {@link SquareArea}.
	 */
	private final SquareArea boundary;
	
	Obelisk(int object, SquareArea boundary) {
		this.object = object;
		this.boundary = boundary;
	}
	
	public int getObject() {
		return object;
	}
	
	public static void action() {
		for(Obelisk ob : VALUES) {
			ObjectAction a = new ObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, int click) {
					Region reg = player.getRegion();
					if(reg != null) {
						World.get().submit(new ObeliskTask(ob, reg));
					}
					return true;
				}
			};
			a.registerFirst(ob.getObject());
		}
	}
	
	/**
	 * Handles the main obelisk.
	 */
	private static class ObeliskTask extends Task {
		
		/**
		 * The obelisk data toggled.
		 */
		private final Obelisk data;
		
		/**
		 * The {@link Region} instance in which the obelisk is located.
		 */
		private final Region reg;
		
		ObeliskTask(Obelisk data, Region reg) {
			super(8, false);
			this.data = data;
			this.reg = reg;
		}
		
		@Override
		protected void onSubmit() {
			reg.interactAction(data.object, o -> {
				o.setId(14825);
				o.publish();
			});
		}
		
		@Override
		protected void execute() {
			reg.interactAction(data.object, o -> {
				o.setId(14825);
				o.publish();
			});
			Obelisk dest = null;
			while(dest == null || dest == data) {
				dest = RandomUtils.random(VALUES.asList());
			}
			int x = dest.boundary.getSwX();
			int y = dest.boundary.getSwY();
			reg.getPlayers().forEach(p -> {
				if(data.boundary.inArea(p.getPosition())) {
					p.teleport(new Position(x + RandomUtils.inclusive(1, 3), y + RandomUtils.inclusive(1, 3)), OBELISK);
					p.message("Ancient magic teleports you somewhere in the wilderness...");
				}
			});
			this.cancel();
		}
	}
}
