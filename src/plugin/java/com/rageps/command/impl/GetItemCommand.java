package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.mob.MobDefinition;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.item.ItemDefinition;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

@CommandSignature(alias = {"getitem", "find"}, rights = {Rights.ADMINISTRATOR}, syntax = "Get's an item id by name, ::find <item_name>")
public final class GetItemCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(cmd.length < 1) {
			player.message("@red@Please supply a name.");
			return;
		}

		System.out.println(command);
		String queryName = cmd[1].toLowerCase().replaceAll("_", " ");//this might be an issue with finding it.
		ObjectArrayList<ItemDefinition> found = new ObjectArrayList<>();
		for(ItemDefinition definition : ItemDefinition.DEFINITIONS) {
			if(definition.getName().toLowerCase().contains(queryName)) {
				found.add(definition);
			}
		}
		if(found.isEmpty())
			player.message("No results found.");
		else {
			player.message("@red@"+found.size()+" results.");
			found.forEach(result -> player.message("->" + result.getName() + " " + result.getId()));
		}
	}
	
}
