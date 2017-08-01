package net.edge.content.skill.prayer;

import net.edge.action.impl.ItemOnObjectAction;
import net.edge.net.packet.out.SendGraphic;
import net.edge.task.Task;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.impl.DestructionSkillAction;
import net.edge.world.Animation;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.object.GameObject;

import java.util.Optional;

public final class PrayerBoneAltar extends DestructionSkillAction {
	
	private final Bone bone;

	private final int itemId;
	
	public PrayerBoneAltar(Player player, int itemId, GameObject object, Bone bone) {
		super(player, Optional.of(object.getGlobalPos()));
		this.bone = bone;
		this.itemId = itemId;
	}
	
	public static void action() {
		for(Bone bone : Bone.values()) {
			ItemOnObjectAction a = new ItemOnObjectAction() {
				@Override
				public boolean click(Player player, GameObject object, Item item, int container, int slot) {
					PrayerBoneAltar altarAction = new PrayerBoneAltar(player, item.getId(), object, bone);
					altarAction.start();
					return true;
				}
			};
			a.registerObj(409);
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
		return new Item(itemId);
	}
	
	@Override
	public void onDestruct(Task t, boolean success) {
		if(success) {
			getPlayer().animation(new Animation(713));
			SendGraphic.local(getPlayer(), 624, position.get(), 0);
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
		return (bone.getExperience() * 2);
	}
	
	@Override
	public SkillData skill() {
		return SkillData.PRAYER;
	}
}
