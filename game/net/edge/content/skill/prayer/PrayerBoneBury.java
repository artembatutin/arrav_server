package net.edge.content.skill.prayer;

import net.edge.task.Task;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.action.impl.DestructionSkillAction;
import net.edge.world.Animation;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Optional;

public final class PrayerBoneBury extends DestructionSkillAction {
	
	private final Bone bone;
	
	public PrayerBoneBury(Player player, Bone bone) {
		super(player, Optional.empty());
		this.bone = bone;
	}
	
	public static boolean produce(Player player, Item item) {
		Optional<Bone> bone = Bone.getBone(item.getId());
		
		if(!bone.isPresent()) {
			return false;
		}
		
		PrayerBoneBury buryAction = new PrayerBoneBury(player, bone.get());
		buryAction.start();
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