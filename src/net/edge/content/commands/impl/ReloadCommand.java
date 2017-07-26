package net.edge.content.commands.impl;

import net.edge.task.Task;
import net.edge.util.json.impl.*;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.market.MarketCounter;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.locale.area.AreaManager;

@CommandSignature(alias = {"reload"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as just ::reload")
public final class ReloadCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		World.get().getTask().submit(new Task(2, false) {
			
			boolean removed;
			
			@Override
			protected void execute() {
				if(!removed) {
					//Removing npcs.
					World.get().getMobs().clear();
					World.get().getPlayers().forEach(p -> p.getLocalMobs().clear());
					//clearing areas
					AreaManager.get().getAreas().clear();
					//Deleting shops
					MarketCounter.getShops().clear();
					player.message("Successfully removed the world nodes [Objects, Mobs, Shops, Areas].");
					removed = true;
				} else {
					new ItemDefinitionLoader().load();
					new MobDefinitionLoader().load();
					new MobNodeLoader().load();
					new ShopLoader().load();
					new AreaLoader().load();
					this.cancel();
					player.message("Successfully reloaded the world [Objects, Mobs, Shops, Defs, Areas].");
				}
			}
		});
	}
}
