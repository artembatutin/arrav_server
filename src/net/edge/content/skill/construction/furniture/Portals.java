package net.edge.content.skill.construction.furniture;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.content.skill.Skills;
import net.edge.content.skill.construction.Construction;
import net.edge.content.skill.construction.House;
import net.edge.content.skill.construction.room.Room;
import net.edge.content.skill.construction.room.RoomFurniture;
import net.edge.content.skill.construction.Portal;
import net.edge.content.skill.construction.data.Constants;
import net.edge.world.locale.Position;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.EnumSet;
import java.util.Iterator;

import static net.edge.content.skill.construction.data.Constants.FORMAL_GARDEN;
import static net.edge.content.skill.construction.data.Constants.GARDEN;

public enum Portals {
	VARROCK(1, new Position(Constants.VARROCK_X, Constants.VARROCK_Y), 25, new Item[]{new Item(563, 100), new Item(554, 100), new Item(556, 300)}, new int[]{13615, 13622, 13629}),
	LUMBRIDGE(2, new Position(Constants.LUMBY_X, Constants.LUMBY_Y), 31, new Item[]{new Item(563, 100), new Item(557, 100), new Item(556, 300)}, new int[]{13616, 13623, 13630}),
	FALADOR(3, new Position(Constants.FALADOR_X, Constants.FALADOR_Y), 37, new Item[]{new Item(563, 100), new Item(555, 100), new Item(556, 300)}, new int[]{13617, 13624, 13631}),
	CAMELOT(4, new Position(Constants.CAMELOT_X, Constants.CAMELOT_Y), 45, new Item[]{new Item(563, 200), new Item(556, 500)}, new int[]{13618, 13625, 13632}),
	ARDOUGNE(5, new Position(Constants.ARDOUGNE_X, Constants.ARDOUGNE_Y), 51, new Item[]{new Item(563, 200), new Item(555, 200)}, new int[]{13619, 13626, 13633}),
	YANILLE(6, new Position(Constants.YANILLE.getX(), Constants.YANILLE.getY()), 58, new Item[]{new Item(563, 200), new Item(557, 200)}, new int[]{13620, 13627, 13634}),
	KHARYLL(7, new Position(Constants.KHARYRLL_X, Constants.KHARYRLL_Y), 66, new Item[]{new Item(563, 200), new Item(565, 100)}, new int[]{13621, 13628, 13635}),
	EMPTY(-1, null, -1, null, null);
	
	public static final ImmutableSet<Portals> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Portals.class));
	
	private Position destination;
	private Item[] requiredItems;
	private int[] objects;
	private int magicLevel, type;
	
	Portals(int id, Position destination, int magicLevel, Item[] requiredItems, int[] objects) {
		this.type = id;
		this.destination = destination;
		this.requiredItems = requiredItems;
		this.objects = objects;
		this.magicLevel = magicLevel;
	}
	
	public Position getDestination() {
		return destination;
	}
	
	public static Portals forType(int type) {
		for(Portals p : VALUES)
			if(p.type == type)
				return p;
		return null;
	}
	
	public static Portals forObjectId(int objectId) {
		for(Portals p : VALUES) {
			for(int i : p.objects)
				if(i == objectId)
					return p;
		}
		return null;
	}
	
	public int[] getObjects() {
		return objects;
	}
	
	public boolean canBuild(Player p) {
		if(requiredItems == null) {
			boolean found = false;
			int[] myTiles = Construction.getMyChunk(p);
			House house = p.getHouse();
			Iterator<Portal> it = house.get().getPortals().iterator();
			while(it.hasNext()) {
				Portal portal = it.next();
				if(portal.getRoomX() == myTiles[0] - 1 && portal.getRoomY() == myTiles[1] - 1 && portal.getRoomZ() == (house.get().isDungeon() ? 4 : p.getPosition().getZ())
						//&& portal.getId() == house.get().getPortal()
						) {
					it.remove();
					found = true;
					break;
				}
			}
			if(!found) {
				p.closeWidget();
				p.message("Can't remove that, doesn't exist.");
				return false;
			} else {
				//p.getPacketSender().sendObjectsRemoval(myTiles[0]-1, myTiles[1]-1, p.inConstructionDungeon() ? 4 : p.getPosition().getZ());
				//Construction.placeAllFurniture(p, myTiles[0]-1, myTiles[1]-1, p.inConstructionDungeon() ? 4 : p.getPosition().getZ());
				p.closeWidget();
				return true;
			}
		}
		if(!p.getInventory().containsAll(requiredItems)) {
			p.message("You don't have the required items to build this.");
			return false;
		}
		if(p.getSkills()[Skills.MAGIC].getRealLevel() < magicLevel) {
			p.message("You need a magic level of " + magicLevel + " to build this");
			return false;
		}
		build(p);
		return true;
	}
	
	public void build(Player p) {
		/*House house = p.getHouse();
		p.getInventory().removeAll(requiredItems);
		int[] myTiles = Construction.getMyChunk(p);
		boolean found = false;
		for(Portal portal : house.get().getPortals()) {
			if(portal.getRoomX() == myTiles[0] - 1 && portal.getRoomY() == myTiles[1] - 1 && portal.getRoomZ() == (house
					.get()
					.isDungeon() ? 4 : p.getPosition().getZ()) && portal.getId() == house.get().getPortal()) {
				portal.setType(type);
				found = true;
			}
		}
		if(!found) {
			Portal portal = new Portal();
			portal.setId(house.get().getPortal());
			portal.setRoomX(myTiles[0] - 1);
			portal.setRoomY(myTiles[1] - 1);
			portal.setRoomZ(house.get().isDungeon() ? 4 : p.getPosition().getZ());
			portal.setType(type);
			house.get().getPortals().add(portal);
		}*/
		//p.getPacketSender().sendObjectsRemoval(myTiles[0]-1, myTiles[1]-1, p.inConstructionDungeon() ? 4 : p.getPosition().getZ());
		//Construction.placeAllFurniture(p, myTiles[0]-1, myTiles[1]-1, p.inConstructionDungeon() ? 4 : p.getPosition().getZ());
		p.closeWidget();
	}
	
	public static Position findNearestPortal(House house) {
		for(int x = 0; x < house.get().getRooms()[0].length; x++) {
			for(int y = 0; y < house.get().getRooms()[0][x].length; y++) {
				Room room = house.get().getRooms()[0][x][y];
				if(room.getFurniture() != null && (room.data().getId() == GARDEN || room.data().getId() == FORMAL_GARDEN)) {
					for(RoomFurniture pf : house.get().getRooms()[0][x][y].getFurniture()) {
						if(pf == null)
							continue;
						if(pf.getFurniture().getId() != 13405)
							continue;
						return new Position(x, y);
					}
				}
			}
		}
		return null;
	}
}