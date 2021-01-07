package com.rageps.content.skill.prayer;

import com.rageps.action.impl.ItemAction;
import com.rageps.content.achievements.Achievement;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.action.impl.DestructionSkillAction;
import com.rageps.task.Task;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Inventory;
import com.rageps.world.model.Animation;

import java.util.Optional;

public final class PrayerBoneBury extends DestructionSkillAction {
	
	private final Bone bone;
	private final int itemId;
	
	public PrayerBoneBury(Player player, int itemId, Bone bone) {
		super(player, Optional.empty());
		this.bone = bone;
		this.itemId = itemId;
	}
	
	public static void action() {
		for(Bone b : Bone.values()) {
			ItemAction e = new ItemAction() {
				@Override
				public boolean click(Player player, Item item, int container, int slot, int click) {
					if(container != Inventory.INVENTORY_DISPLAY_ID) {
						return true;
					}
					if(item.getDefinition().isNoted()) {
						player.message("You can't use noted bones.");
						return true;
					}
					PrayerBoneBury buryAction = new PrayerBoneBury(player, item.getId(), b);
					buryAction.start();
					return true;
				}
			};
			e.register(b.getId());
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
			Achievement.BURY_BONES.inc(player);
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