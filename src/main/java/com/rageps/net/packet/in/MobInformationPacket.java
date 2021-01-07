package com.rageps.net.packet.in;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.util.rand.Chance;
import com.rageps.world.entity.actor.mob.MobAttributes;
import com.rageps.world.entity.actor.mob.MobDefinition;
import com.rageps.world.entity.actor.mob.drop.DropManager;
import com.rageps.world.entity.actor.mob.drop.DropTable;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.item.ItemDefinition;


/**
 * The message sent from the client which depends on the Mob Information panel integration.
 * @author Artem Batutin
 */
public final class MobInformationPacket implements IncomingPacket {//todo - reimplement this when i do drop interface
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(opcode == 19) {
			Chance chance = Chance.values()[buf.get()];
			int mob = player.getAttributeMap().getInt(MobAttributes.NPC_INFORMATION);
			int item = buf.getShort();
			int min = buf.getShort();
			int max = buf.getShort();
			if(min != 99 && min != 88 && max > 2) {
				ItemDefinition def = ItemDefinition.get(item);
				if(def != null) {
					if(!def.isStackable()) {
						item = def.getNoted();
					}
				}
			}
			/*SuggestedDrop suggested = new SuggestedDrop(mob, item, min, max, chance);
			if(player.getRights() == Rights.ADMINISTRATOR) {
				int tableId = mob;
				if(DropManager.getRedirects().containsKey(mob)) {
					tableId = DropManager.getRedirects().get(mob);
				}
				DropTable table = DropManager.getTables().get(tableId);
				if(table == null) {
					table = new DropTable();
					DropManager.getTables().put(tableId, table);
				}
				//to share table
				if(min == 88) {
					table = DropManager.getTables().get(max);
					if(table != null) {
						DropManager.getTables().put(mob, table);
					}
					player.send(new MobDrop(mob, table));
					player.message("Shared with: " + MobDefinition.DEFINITIONS[max].getName());
					return;
				}
				//to remove item
				if(min == 99) {
					int index = 0;
					String itemName = ItemDefinition.get(item).getName().toLowerCase().replaceAll(" ", "_");
					if(itemName != null) {
						for(Drop d : table.getDrops()) {
							if(d != null) {
								String name = ItemDefinition.get(d.getId()).getName().toLowerCase().replaceAll(" ", "_");
								if(itemName.contains(name.toLowerCase()) || name.contains(itemName.toLowerCase())) {
									table.getDrops().remove(index);
									table.sort();
									player.message("Removed: " + d.toString());
									player.send(new MobDrop(mob, table));
									return;
								}
							}
							index++;
						}
						index = 0;
						for(Drop d : table.getRare()) {
							if(d != null) {
								String name = ItemDefinition.get(d.getId()).getName().toLowerCase().replaceAll(" ", "_");
								if(itemName.contains(name.toLowerCase()) || name.contains(itemName.toLowerCase())) {
									table.getRare().remove(index);
									table.sort();
									player.message("Removed: " + d.toString());
									player.send(new MobDrop(mob, table));
									return;
								}
							}
							index++;
						}
					}
					player.message("Couldn't remove any drop.");
					return;
				}
				if(suggested.isRare())
					table.getRare().add(suggested.toDrop());
				else
					table.getDrops().add(suggested.toDrop());
				table.sort();
				player.message("Added " + suggested.toString());
				player.send(new MobDrop(mob, table));
			} else {
				DROP_A_SUG.inc(player);
				SUGGESTED.add(suggested);
				player.message("Your suggestion has been submitted.");
			}*/
		} else {
			int id = buf.getShort();
			if(id < 0 || id > MobDefinition.DEFINITIONS.length) {
				player.message("No information found.");
				return;
			}
			player.getAttributeMap().set(MobAttributes.NPC_INFORMATION, id);
			DropTable drop = DropManager.TABLES.get(id);
			if(drop == null && !player.getRights().equals(Rights.ADMINISTRATOR)) {
				player.message("This monster doesn't have any drop table.");
				return;
			}
			//player.send(new MobDrop(id, drop));
			player.widget(-11);
		}
	}
}