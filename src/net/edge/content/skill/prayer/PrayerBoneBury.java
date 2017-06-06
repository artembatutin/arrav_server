package net.edge.content.skill.prayer;

import net.edge.event.impl.ItemEvent;
import net.edge.task.Task;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.impl.DestructionSkillAction;
import net.edge.world.Animation;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

public final class PrayerBoneBury extends DestructionSkillAction {
	
	private final Bone bone;

	private final int itemId;
	
	public PrayerBoneBury(Player player, int itemId, Bone bone) {
		super(player, Optional.empty());
		this.bone = bone;
		this.itemId = itemId;
	}
	
	public static void event() {
		for(Bone b : Bone.values()) {
			ItemEvent e = new ItemEvent() {
				@Override
				public boolean click(Player player, Item item, int container, int slot, int click) {
					PrayerBoneBury buryAction = new PrayerBoneBury(player, item.getId(), b);
					buryAction.start();
					return true;
				}
			};
			e.registerInventory(b.getId());
		}
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
	
	@Override
	public Item destructItem() {
		return new Item(itemId);
	}
	
	@Override
	public void onDestruct(Task t, boolean success) {
		if(success) {
			getPlayer().animation(new Animation(827));
			getPlayer().message("You bury the " + bone + ".");
			getPlayer().getBuryTimer().reset();
		}
		t.cancel();
	}
	
	@Override
	public int delay() {
		return 0;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public boolean init() {
		return getPlayer().getBuryTimer().elapsed(1200);
	}
	
	@Override
	public double experience() {
		return bone.getExperience();
	}
	
	@Override
	public SkillData skill() {
		return SkillData.PRAYER;
	}
}