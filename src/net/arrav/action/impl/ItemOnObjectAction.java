package net.arrav.action.impl;

import net.arrav.action.Action;
import net.arrav.content.item.refillable.RefillableAction;
import net.arrav.content.skill.action.TransformableObject;
import net.arrav.content.skill.cooking.Cooking;
import net.arrav.content.skill.crafting.JewelleryMoulding;
import net.arrav.content.skill.crafting.Spinning;
import net.arrav.content.skill.farming.FarmingAction;
import net.arrav.content.skill.firemaking.Bonfire;
import net.arrav.content.skill.prayer.PrayerBoneAltar;
import net.arrav.net.packet.in.ItemOnObjectPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.object.GameObject;

/**
 * Action handling item on object actions.
 * @author Artem Batutin
 */
public abstract class ItemOnObjectAction extends Action {
	
	public abstract boolean click(Player player, GameObject object, Item item, int container, int slot);
	
	public void registerObj(int object) {
		ItemOnObjectPacket.OBJECTS.register(object, this);
	}
	
	public void registerItem(int item) {
		ItemOnObjectPacket.ITEMS.register(item, this);
	}
	
	public static void init() {
		FarmingAction.action();
		Cooking.action();
		JewelleryMoulding.action();
		Bonfire.action();
		PrayerBoneAltar.action();
		Spinning.action();
		RefillableAction.action();

		ItemOnObjectAction a = new ItemOnObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, Item item, int container, int slot) {
				object.toDynamic();
				if(object.getId() != 65612) {
					return false;
				}
				TransformableObject obj = new TransformableObject(65612, 65613);
				object = object.setId(obj.getTransformable());
				object.publish(30, n -> {
					n.setId(65612);
					n.publish();
				});
				player.message("You hang the rope into the tunnel.");
				return true;
			}
		};
		a.registerObj(65612);
		a.registerItem(954);
	}
	
}
