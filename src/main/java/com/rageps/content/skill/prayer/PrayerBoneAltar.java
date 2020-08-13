package com.rageps.content.skill.prayer;

import com.rageps.action.impl.ItemOnObjectAction;
import com.rageps.content.skill.SkillData;
import com.rageps.content.skill.action.impl.DestructionSkillAction;
import com.rageps.net.packet.out.SendGraphic;
import com.rageps.task.Task;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.object.GameObject;

import java.util.Optional;

public final class PrayerBoneAltar extends DestructionSkillAction {
	
	private final Bone bone;
	
	private final boolean prayLoc;
	
	public PrayerBoneAltar(Player player, GameObject object, Bone bone) {
		super(player, Optional.of(object.getPosition()));
		this.bone = bone;
		prayLoc = object.getId() == 15050;
	}
	
	public static void action() {
		for(Bone bone : Bone.values()) {
			ItemOnObjectAction a = new ItemOnObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, Item item, int container, int slot) {
					if(object.getId() == 409 || object.getId() == 15050) {
						if(item.getDefinition().isNoted()) {
							player.message("You can't use noted bones.");
							return true;
						}
						PrayerBoneAltar altarAction = new PrayerBoneAltar(player, object, bone);
						altarAction.start();
						return true;
					}
					return false;
				}
			};
			a.registerItem(bone.getId());
		}
	}
	
	@Override
	public boolean init() {
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
	
	@Override
	public Item destructItem() {
		return new Item(bone.getId());
	}
	
	@Override
	public void onDestruct(Task t, boolean success) {
		if(success) {
			getPlayer().animation(new Animation(713));
			position.ifPresent(c -> SendGraphic.local(getPlayer(), 624, c, 0));
			getPlayer().message("You offer the " + bone + " to the gods... they seem pleased.");
		}
	}
	
	@Override
	public int delay() {
		return 6;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public double experience() {
		return (bone.getExperience() * (prayLoc ? 2 : 1.4));
	}
	
	@Override
	public SkillData skill() {
		return SkillData.PRAYER;
	}
}
