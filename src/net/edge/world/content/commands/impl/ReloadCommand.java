package net.edge.world.content.commands.impl;

import java.util.HashSet;
import java.util.Set;

import net.edge.utils.json.impl.*;
import net.edge.world.World;
import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.content.shootingstar.ShootingStarData;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.object.ObjectNode;
import net.edge.world.model.node.region.Region;
import net.edge.world.content.market.MarketCounter;
import net.edge.world.content.skill.firemaking.pits.FirepitData;
import net.edge.world.model.node.entity.player.assets.Rights;
import net.edge.task.Task;

@CommandSignature(alias = {"reload"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as just ::reload")
public final class ReloadCommand implements Command {
	
	private final Set<ObjectNode> getRegisteredObjects(Region r) {
		Set<ObjectNode> obj = new HashSet<>();
		
		for(ObjectNode object : r.getRegisteredObjects()) {
			if(ShootingStarData.VALUES.stream().anyMatch(t -> t.getObjectId() == object.getId())) {
				continue;
			}
			
			if(FirepitData.VALUES.stream().anyMatch(t -> t.getObjectId() == object.getId())) {
				continue;
			}
			
			obj.add(object);
		}
		
		return obj;
	}
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		World.getTaskManager().submit(new Task(2, false) {
			
			boolean removed;
			
			@Override
			protected void execute() {
				if(!removed) {
					//Removing npcs.
					World.getNpcs().clear();
					World.getPlayers().forEach(p -> p.getLocalNpcs().clear());
					//Removing objects.
					World.getRegions().getRegions().forEach((c, r) -> getRegisteredObjects(r).forEach(r::unregister));
					
					//clearing areas
					World.getAreaManager().getAreas().clear();
					
					//Deleting shops
					MarketCounter.getShops().clear();
					player.message("Successfully removed the world nodes [Objects, Npcs, Shops, Areas].");
					removed = true;
				} else {
					new ObjectNodeRemoveLoader();
					new ObjectNodeLoader().load();
					new ItemDefinitionLoader().load();
					new NpcDefinitionLoader().load();
					new NpcNodeLoader().load();
					new ShopLoader().load();
					new AreaLoader().load();
					this.cancel();
					player.message("Successfully reloaded the world [Objects, Npcs, Shops, Defs, Areas].");
				}
			}
		});
	}
}
