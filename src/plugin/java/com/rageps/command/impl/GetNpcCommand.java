package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.World;
import com.rageps.world.entity.actor.mob.MobDefinition;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.region.TraversalMap;
import com.rageps.world.locale.Position;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

@CommandSignature(alias = {"getnpc"}, rights = {Rights.ADMINISTRATOR}, syntax = "Get's an npc id by name, ::getnpc <npc_name>")
public final class GetNpcCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(cmd.length < 1) {
			player.message("@red@Please supply a name.");
			return;
		}

		String queryName = cmd[1].toLowerCase().replaceAll("_", " ");
		ObjectArrayList<MobDefinition> found = new ObjectArrayList<>();
		for(MobDefinition definition : MobDefinition.DEFINITIONS) {
			if(definition.getName().toLowerCase().contains(queryName))
				found.add(definition);
		}
		if(found.isEmpty())
			player.message("No results found.");
		else {
			player.message("@red@"+found.size()+" results.");
			found.forEach(result -> player.message(result.getName() + " " + result.getId()));
		}
	}
	
}
