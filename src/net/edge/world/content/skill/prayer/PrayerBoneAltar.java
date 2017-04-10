package net.edge.world.content.skill.prayer;

import net.edge.world.content.skill.action.impl.DestructionSkillAction;
import net.edge.world.model.node.entity.model.Animation;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.world.content.skill.SkillData;
import net.edge.task.Task;

import java.util.Optional;

public final class PrayerBoneAltar extends DestructionSkillAction {
	
	private final Bone bone;
	
	public PrayerBoneAltar(Player player, int itemId, ObjectNode object) {
		super(player, Optional.of(object.getPosition()));
		this.bone = Bone.getBone(itemId).orElse(null);
	}
	
	public static boolean produce(Player player, int itemId, ObjectNode object) {
		if(object.getId() != 409) {
			return false;
		}
		Optional<Bone> bone = Bone.getBone(itemId);
		
		if(!bone.isPresent()) {
			return false;
		}
		
		PrayerBoneAltar altarAction = new PrayerBoneAltar(player, itemId, object);
		altarAction.start();
		return true;
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
			getPlayer().getMessages().sendLocalGraphic(624, position.get(), 0);
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
