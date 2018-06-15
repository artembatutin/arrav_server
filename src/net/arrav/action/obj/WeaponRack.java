package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.object.GameObject;

public class WeaponRack extends ActionInitializer {
	
	@Override
	public void init() {
		ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.message("The weapon seems stuck...");
				/*player.getDialogueBuilder().append(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getInventory().add(new Item(14486));
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.getInventory().add(new Item(13450));
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.getInventory().add(new Item(13444));
					} else {
						player.getInventory().add(new Item(13405));
						player.getInventory().add(new Item(11212, 200));
					}
					player.closeWidget();
				}, "Dragon claws", " Armadyl godsword", "Whip", "Dark bow"));
				player.message("Lended items will be removed when you get out of here.");*/
				return true;
			}
		};
		l.registerFirst(6069);
	}
}
