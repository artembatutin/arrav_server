package net.edge.net.packet.in;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.net.packet.out.SendNpcDrop;
import net.edge.util.rand.Chance;
import net.edge.world.node.entity.npc.NpcDefinition;
import net.edge.world.node.entity.npc.drop.NpcDrop;
import net.edge.world.node.entity.npc.drop.NpcDropManager;
import net.edge.world.node.entity.npc.drop.NpcDropTable;
import net.edge.world.node.entity.npc.drop.SuggestedDrop;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.item.ItemDefinition;

/**
 * The message sent from the client which depends on the Npc Information panel integration.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class NpcInformationPacket implements IncomingPacket {
	
	public static final ObjectList<SuggestedDrop> SUGGESTED = new ObjectArrayList<>();
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(opcode == 19) {
			Chance chance = Chance.values()[payload.get()];
			int npc = player.getAttr().get("npcInformation").getInt();
			int item = payload.getShort();
			int min = payload.getShort();
			int max = payload.getShort();
			SuggestedDrop suggested = new SuggestedDrop(npc, item, min, max, chance);
			if(player.getRights() == Rights.ADMINISTRATOR) {
				NpcDropTable table = NpcDropManager.getTables().get(npc);
				if(table == null) {
					player.message("No table found.");
					return;
				}
				//to remove item
				if(min == 99) {
					int index = 0;
					String itemName = ItemDefinition.get(item).getName().toLowerCase().replaceAll(" ", "_");
					for(NpcDrop d : table.getUnique()) {
						if(d != null) {
							String name = ItemDefinition.get(d.getId()).getName().toLowerCase().replaceAll(" ", "_");
							if(itemName.equals(name)) {
								table.getUnique().remove(index);
								table.sort();
								player.message("Removed: " + d.toString());
								player.out(new SendNpcDrop(npc, table));
								return;
							}
						}
						index++;
					}
					player.message("Couldn't remove any drop.");
					return;
				}
				if(table.getUnique().isEmpty())
					table.getCommon().clear();
				table.getUnique().add(suggested.toDrop());
				table.sort();
				player.message("Added " + suggested.toString());
				player.out(new SendNpcDrop(npc, table));
			} else {
				SUGGESTED.add(suggested);
				player.message("Your suggestion has been submitted.");
			}
		} else {
			int id = payload.getShort();
			if(id < 0 || id > NpcDefinition.DEFINITIONS.length) {
				player.message("No information found.");
				return;
			}
			player.getAttr().get("npcInformation").set(id);
			NpcDropTable drop = NpcDropManager.getTables().get(id);
			if(drop == null) {
				player.message("This monster doesn't have any drop table.");
				return;
			}
			player.out(new SendNpcDrop(id, drop));
			player.widget(-11);
		}
	}
}