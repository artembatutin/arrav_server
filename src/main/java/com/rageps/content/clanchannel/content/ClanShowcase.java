package com.rageps.content.clanchannel.content;

import com.rageps.content.clanchannel.channel.ClanChannel;
import com.rageps.content.clanchannel.channel.ClanChannelHandler;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.dialogue.impl.StatementDialogue;
import com.rageps.net.packet.out.SendItemsOnInterface;
import com.rageps.net.packet.out.SendText;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.List;

public class ClanShowcase {
	private final ClanChannel channel;
	public Item[] showcase;
	public ObjectArrayList<Item> showcaseItems;


	public ClanShowcase(ClanChannel channel) {
		this.channel = channel;
		showcaseItems = new ObjectArrayList<>();
	}

	public void openShowcase(Player player) {
		//Item[] showcase = new Item[28];
		//for (int index = 0; index < showcaseItems.size(); index++) {
		//	showcase[index] = new Item(showcaseItems.get(index));
		//}
		player.out(new SendText(channel.getName() + "'s Showcase", 57702));
		player.out(new SendText(showcaseItems.size() + "/28", 57718));
		player.out(new SendItemsOnInterface(57716, 28, showcase));
		player.out(new SendItemsOnInterface(57717));
		player.getInterfaceManager().open(57700);
	}

	public void select(Player player, int item, int slot) {
		if (!player.getInterfaceManager().isInterfaceOpen(57700) || slot < 0 || slot >= showcaseItems.size())
			return;

		Item selected = showcaseItems.get(slot);
		if (item == selected.getId()) {
			//currentSlot = slot;
			//currentItem = selected;
			player.out(new SendItemsOnInterface(57717, selected));
		}
	}

	public void set(Player player) {
		//if (!player.getInterfaceManager().isInterfaceOpen(57700) || showcaseSlot < 0 || showcaseSlot >= 3)
		//	return;

		//if (currentSlot == -1 || currentItem == null) {
		//	player.message("You should select an item first.");
		//	return;
		//}

		//showcase[showcaseSlot] = currentItem;
		ClanChannelHandler.manage(player);
		Item[] showcase = new Item[28];
		for (int index = 0; index < showcaseItems.size(); index++) {
		//	showcase[index] = new Item(showcaseItems.get(index));
		}
		player.out(new SendItemsOnInterface(57716));
		player.out(new SendItemsOnInterface(57716, showcase));
		player.message("You have successfully changed your showcase.");
	}

	public void remove(Player player) {
		if (!player.getInterfaceManager().isInterfaceOpen(57700))
			return;

		//if (currentSlot == -1 || currentItem == null) {
		//	player.message("You should select an item first.");
		//	return;
		//}

		if (showcaseItems.size() <= 3) {
			player.message("You need a minimum of 3 showcase items. This action can not be performed.");
			return;
		}

		//player.getDialogueBuilder().append(new StatementDialogue("Are you sure you want to delete <col=225>" + ItemDefinition.get(currentItem).getName() + "</col>?",
		//		"Once this action is performed it can not be undone!"),
		//		new OptionDialogue(optionType -> {
		//			if(optionType == OptionDialogue.OptionType.FIRST_OPTION) {
			//		//	showcaseItems.remove(currentSlot);
						//openShowcase(player, currentItem);
			//		}
			//		player.closeWidget();
			//	}, "Yes", "Nevermind"));
	}
}
