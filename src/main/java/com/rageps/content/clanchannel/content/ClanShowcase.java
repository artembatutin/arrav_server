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

import java.util.ArrayList;
import java.util.List;

public class ClanShowcase {
	private final ClanChannel channel;
	public int[] showcase = new int[3];
	public List<Integer> showcaseItems = new ArrayList<>(28);
	private int currentItem = -1, currentSlot = -1;
	private int showcaseSlot;

	public ClanShowcase(ClanChannel channel) {
		this.channel = channel;
	}

	public void openShowcase(Player player, int slot) {
		Item[] showcase = new Item[28];
		for (int index = 0; index < showcaseItems.size(); index++) {
			showcase[index] = new Item(showcaseItems.get(index));
		}
		player.out(new SendText(channel.getName() + "'s Showcase", 57702));
		player.out(new SendText(showcaseItems.size() + "/28", 57718));
		player.out(new SendItemsOnInterface(57716, showcase));
		player.out(new SendItemsOnInterface(57717));
		showcaseSlot = slot;
		currentSlot = -1;
		currentItem = -1;
		player.getInterfaceManager().open(57700);
	}

	public void select(Player player, int item, int slot) {
		if (!player.getInterfaceManager().isInterfaceOpen(57700) || slot < 0 || slot >= showcaseItems.size())
			return;

		int id = showcaseItems.get(slot);
		if (item == id) {
			currentSlot = slot;
			currentItem = id;
			player.out(new SendItemsOnInterface(57717, new Item(id)));
		}
	}

	public void set(Player player) {
		if (!player.getInterfaceManager().isInterfaceOpen(57700) || showcaseSlot < 0 || showcaseSlot >= 3)
			return;

		if (currentSlot == -1 || currentItem == -1) {
			player.message("You should select an item first.");
			return;
		}

		showcase[showcaseSlot] = currentItem;
		ClanChannelHandler.manage(player);
		Item[] showcase = new Item[28];
		for (int index = 0; index < showcaseItems.size(); index++) {
			showcase[index] = new Item(showcaseItems.get(index));
		}
		player.out(new SendItemsOnInterface(57716));
		player.out(new SendItemsOnInterface(57716, showcase));
		player.message("You have successfully changed your showcase.");
	}

	public void remove(Player player) {
		if (!player.getInterfaceManager().isInterfaceOpen(57700))
			return;

		if (currentSlot == -1 || currentItem == -1) {
			player.message("You should select an item first.");
			return;
		}

		if (showcaseItems.size() <= 3) {
			player.message("You need a minimum of 3 showcase items. This action can not be performed.");
			return;
		}

		player.getDialogueBuilder().append(new StatementDialogue("Are you sure you want to delete <col=225>" + ItemDefinition.get(currentItem).getName() + "</col>?",
				"Once this action is performed it can not be undone!"),
				new OptionDialogue(optionType -> {
					if(optionType == OptionDialogue.OptionType.FIRST_OPTION) {
						showcaseItems.remove(currentSlot);
						openShowcase(player, currentItem);
					}
					player.closeWidget();
				}, "Yes", "Nevermind"));
	}
}
