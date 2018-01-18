package net.edge.net.packet.in;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.net.packet.IncomingPacket;
import net.edge.net.packet.out.SendMobDrop;
import net.edge.util.rand.Chance;
import net.edge.world.entity.actor.mob.MobDefinition;
import net.edge.world.entity.actor.mob.drop.Drop;
import net.edge.world.entity.actor.mob.drop.DropManager;
import net.edge.world.entity.actor.mob.drop.DropTable;
import net.edge.world.entity.actor.mob.drop.SuggestedDrop;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.item.ItemDefinition;

import static net.edge.content.achievements.Achievement.DROP_A_SUG;

/**
 * The message sent from the client which depends on the Mob Information panel integration.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MobInformationPacket implements IncomingPacket {
	
	public static final ObjectList<SuggestedDrop> SUGGESTED = new ObjectArrayList<>();
	
	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
		if(opcode == 19) {
			Chance chance = Chance.values()[buf.get()];
			int mob = player.getAttr().get("npcInformation").getInt();
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
			SuggestedDrop suggested = new SuggestedDrop(mob, item, min, max, chance);
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
					player.out(new SendMobDrop(mob, table));
					player.message("Shared with: " + MobDefinition.DEFINITIONS[max].getName());
					return;
				}
				//to remove item
				if(min == 99) {
					int index = 0;
					String itemName = ItemDefinition.get(item).getName().toLowerCase().replaceAll(" ", "_");
					if(itemName != null) {
						for(Drop d : table.getCommon()) {
							if(d != null) {
								String name = ItemDefinition.get(d.getId()).getName().toLowerCase().replaceAll(" ", "_");
								if(itemName.contains(name.toLowerCase()) || name.contains(itemName.toLowerCase())) {
									table.getCommon().remove(index);
									table.sort();
									player.message("Removed: " + d.toString());
									player.out(new SendMobDrop(mob, table));
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
									player.out(new SendMobDrop(mob, table));
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
					table.getCommon().add(suggested.toDrop());
				table.sort();
				player.message("Added " + suggested.toString());
				player.out(new SendMobDrop(mob, table));
			} else {
				DROP_A_SUG.inc(player);
				SUGGESTED.add(suggested);
				player.message("Your suggestion has been submitted.");
			}
		} else {
			int id = buf.getShort();
			if(id < 0 || id > MobDefinition.DEFINITIONS.length) {
				player.message("No information found.");
				return;
			}
			player.getAttr().get("npcInformation").set(id);
			DropTable drop = DropManager.getTables().get(id);
			if(drop == null && !player.getRights().equals(Rights.ADMINISTRATOR)) {
				player.message("This monster doesn't have any drop table.");
				return;
			}
			player.out(new SendMobDrop(id, drop));
			player.widget(-11);
		}
	}
}