package net.edge.content.skill.construction.room;

import net.edge.content.skill.construction.*;
import net.edge.content.skill.construction.Palette.PaletteTile;
import net.edge.content.skill.construction.data.Constants;
import net.edge.content.skill.construction.furniture.Furniture;
import net.edge.content.skill.construction.furniture.HotSpots;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Iterator;

import static net.edge.content.skill.construction.Construction.getMyChunk;
import static net.edge.content.skill.construction.Construction.getXTilesOnTile;
import static net.edge.content.skill.construction.Construction.getYTilesOnTile;

/**
 * Handles room manipulations.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class RoomManipulation {
	
	public static void createRoom(RoomData data, Player p, int toHeight) {
		House house = p.getHouse();
		if(!p.getInventory().contains(new Item(995, data.getCost()))) {
			p.message("You need " + data.getCost() + " coins to build this");
			return;
		}
		boolean isDungeonRoom = Constants.isDungeonRoom(data.getId());
		if(!house.get().isDungeon()) {
			if(isDungeonRoom && toHeight != 102 && toHeight != 103) {
				p.message("You can only build this room in your dungeon.");
				return;
			}
		} else {
			if(!isDungeonRoom) {
				p.message("You can only build this room on the surface");
				return;
			}
		}
		int[] myTiles = getMyChunk(p);
		if(myTiles == null)
			return;
		int xOnTile = getXTilesOnTile(myTiles, p);
		int yOnTile = getYTilesOnTile(myTiles, p);
		int direction = 0;
		final int LEFT = 0, DOWN = 1, RIGHT = 2, UP = 3, SAME = 4;
		if(xOnTile == 0)
			direction = LEFT;
		if(yOnTile == 0)
			direction = DOWN;
		if(xOnTile == 7)
			direction = RIGHT;
		if(yOnTile == 7)
			direction = UP;
		int rotation = RoomData.getFirstElegibleRotation(data, direction);
		if(toHeight == 100) {
			direction = SAME;
			toHeight = 1;
			rotation = house.get().getRooms()[0][myTiles[0] - 1][myTiles[1] - 1].getRotation();
			int stairId = 0;
			for(HouseFurniture furn : house.get().getFurniture()) {
				if(furn.getRoomX() == myTiles[0] - 1 && furn.getRoomY() == myTiles[1] - 1 && furn.getRoomZ() == 0) {
					if(furn.getStandardXOff() == 3 && furn.getStandardYOff() == 3) {
						stairId = furn.getFurnitureId() + 1;
					}
				}
			}
			Construction.doFurniturePlace(HotSpots.SKILL_HALL_STAIRS_1, Furniture.forFurnitureId(stairId), null, myTiles, Constants.BASE_X + (myTiles[0] * 8) + 3, Constants.BASE_Y + (myTiles[1] * 8) + 3, rotation, p, false, 1);
			HouseFurniture pf = new HouseFurniture(myTiles[0] - 1, myTiles[1] - 1, 1, 37, stairId, 3, 3);
			house.get().getFurniture().add(pf);
		}
		if(toHeight == 101) {
			direction = SAME;
			toHeight = 0;
			rotation = house.get().getRooms()[1][myTiles[0] - 1][myTiles[1] - 1].getRotation();
			int stairId = 0;
			for(HouseFurniture furn : house.get().getFurniture()) {
				if(furn.getRoomX() == myTiles[0] - 1 && furn.getRoomY() == myTiles[1] - 1 && furn.getRoomZ() == 1) {
					if(furn.getStandardXOff() == 3 && furn.getStandardYOff() == 3) {
						stairId = furn.getFurnitureId() + 1;
					}
				}
			}
			Construction.doFurniturePlace(HotSpots.SKILL_HALL_STAIRS, Furniture.forFurnitureId(stairId), null, myTiles, Constants.BASE_X + (myTiles[0] * 8) + 3, Constants.BASE_Y + (myTiles[1] * 8) + 3, rotation, p, false, 1);
			HouseFurniture pf = new HouseFurniture(myTiles[0] - 1, myTiles[1] - 1, 0, 36, stairId, 3, 3);
			house.get().getFurniture().add(pf);
			house.createPalette();
		}
		
		/*
		 * Create dungeon room from entrance
		 */
		if(toHeight == 102 || toHeight == 103) {
			direction = SAME;
			toHeight = 4;
			rotation = house.get().getRooms()[0][myTiles[0] - 1][myTiles[1] - 1].getRotation();
			HouseFurniture pf;
			if(toHeight == 102) {
				int stairId = 13497;
				pf = new HouseFurniture(myTiles[0] - 1, myTiles[1] - 1, 4, 36, stairId, 3, 3);
			} else {
				pf = new HouseFurniture(myTiles[0] - 1, myTiles[1] - 1, 4, 88, 13328, 1, 6);
			}
			house.get().getFurniture().add(pf);
		}
		
		Room room = new Room(data, rotation, 0);
		PaletteTile tile = new PaletteTile(room.getX(), room.getY(), room.getZ(), room.getRotation());
		
		int xOff = 0, yOff = 0;
		if(direction == LEFT) {
			xOff = -1;
		}
		if(direction == DOWN) {
			yOff = -1;
		}
		if(direction == RIGHT) {
			xOff = 1;
		}
		if(direction == UP) {
			yOff = 1;
		}
		if(toHeight == 1) {
			Room r = house.get().getRooms()[0][(myTiles[0] - 1) + xOff][(myTiles[1] - 1) + yOff];
			if(r.data().getId() == Constants.EMPTY || r.data().getId() == Constants.BUILDABLE ||r.data().getId() == Constants.GARDEN || r.data().getId() == Constants.FORMAL_GARDEN) {
				p.message("You need a foundation to build there");
				return;
			}
		}
		if(toHeight == 4 || house.get().isDungeon()) {
			house.getSecondaryPalette().setTile((myTiles[0] - 1) + xOff, (myTiles[1] - 1) + yOff, 0, tile);
		} else {
			house.getPalette().setTile((myTiles[0] - 1) + xOff, (myTiles[1] - 1) + yOff, toHeight, tile);
		}
		house.get().getRooms()[house.get().isDungeon() ? 4 : toHeight][(myTiles[0] - 1) + xOff][(myTiles[1] - 1) + yOff] = new Room(data, rotation, 0);
		house.get().setBuildPosition(p.getPosition().copy());
		house.createPalette();
		house.refresh();
	}
	
	public static void rotateRoom(int wise, Player p) {
		House house = p.getHouse();
		int[] myTiles = getMyChunk(p);
		int xOnTile = getXTilesOnTile(myTiles, p);
		int yOnTile = getYTilesOnTile(myTiles, p);
		int direction = 0;
		final int LEFT = 0, DOWN = 1, RIGHT = 2, UP = 3;
		if(xOnTile == 0)
			direction = LEFT;
		if(yOnTile == 0)
			direction = DOWN;
		if(xOnTile == 7)
			direction = RIGHT;
		if(yOnTile == 7)
			direction = UP;
		int xOff = 0, yOff = 0;
		if(direction == LEFT) {
			xOff = -1;
		}
		if(direction == DOWN) {
			yOff = -1;
		}
		if(direction == RIGHT) {
			xOff = 1;
		}
		if(direction == UP) {
			yOff = 1;
		}
		int chunkX = (myTiles[0] - 1) + xOff;
		int chunkY = (myTiles[1] - 1) + yOff;
		Room r = house.get().getRooms()[house.get().isDungeon() ? 4 : p.getPosition().getZ()][chunkX][chunkY];
		RoomData rd = r.data();
		int toRot = (wise == 0 ? RoomData.getNextEligibleRotationClockWise(rd, direction, r.getRotation()) : RoomData.getNextEligibleRotationCounterClockWise(rd, direction, r.getRotation()));
		PaletteTile tile = new PaletteTile(rd.getX(), rd.getY(), 0, toRot);
		//p.getMessages().sendObjectsRemoval(chunkX, chunkY, p.getPosition().getZ());
		if(house.get().isDungeon()) {
			house.getSecondaryPalette().setTile(chunkX, chunkY, 0, tile);
		} else {
			house.getPalette().setTile(chunkX, chunkY, p.getPosition().getZ(), tile);
		}
		house.get().getRooms()[house.get().isDungeon() ? 4 : p.getPosition().getZ()][chunkX][chunkY].setRotation(toRot);
		house.get().setBuildPosition(p.getPosition().copy());
		house.createPalette();
		house.refresh();
	}
	
	public static void deleteRoom(Player p, int toHeight) {
		House house = p.getHouse();
		int[] myTiles = getMyChunk(p);
		int xOnTile = getXTilesOnTile(myTiles, p);
		int yOnTile = getYTilesOnTile(myTiles, p);
		int direction = 0;
		final int LEFT = 0, DOWN = 1, RIGHT = 2, UP = 3;
		if(xOnTile == 0)
			direction = LEFT;
		if(yOnTile == 0)
			direction = DOWN;
		if(xOnTile == 7)
			direction = RIGHT;
		if(yOnTile == 7)
			direction = UP;
		
		Room room = new Room(house.get().isDungeon() ? RoomData.DUNGEON_EMPTY : RoomData.EMPTY, 0, 0);
		PaletteTile tile = new PaletteTile(room.getX(), room.getY(), room.getZ(), room.getRotation());
		
		int xOff = 0, yOff = 0;
		if(direction == LEFT) {
			xOff = -1;
		}
		if(direction == DOWN) {
			yOff = -1;
		}
		if(direction == RIGHT) {
			xOff = 1;
		}
		if(direction == UP) {
			yOff = 1;
		}
		int chunkX = (myTiles[0] - 1) + xOff;
		int chunkY = (myTiles[1] - 1) + yOff;
		Room r = house.get().getRooms()[house.get().isDungeon()? 4 : toHeight][chunkX][chunkY];
		if(r.data().getId() == Constants.GARDEN || r.data().getId() == Constants.FORMAL_GARDEN) {
			int gardenAmt = 0;
			for(int z = 0; z < house.get().getRooms().length; z++) {
				for(int x = 0; x < house.get().getRooms()[z].length; x++) {
					for(int y = 0; y < house.get().getRooms()[z][x].length; y++) {
						Room r1 = house.get().getRooms()[z][x][y];
						if(r1 == null)
							continue;
						if(r1.data().getId() == Constants.GARDEN || r1.data().getId() == Constants.FORMAL_GARDEN) {
							gardenAmt++;
						}
					}
				}
			}
			if(gardenAmt < 2) {
				p.message("You need atleast 1 garden or formal garden");
				p.getMessages().sendCloseWindows();
				return;
			}
		}
		//p.getMessages().sendObjectsRemoval(chunkX, chunkY, p.getPosition().getZ());
		if(p.getPosition().getZ() == 0) {
			if(house.get().isDungeon()) {
				house.getSecondaryPalette().setTile(chunkX, chunkY, 0, tile);
			} else {
				house.getPalette().setTile(chunkX, chunkY, toHeight, tile);
			}
			house.get().getRooms()[house.get().isDungeon() ? 4 : toHeight][chunkX][chunkY] = new Room(house.get().isDungeon() ? RoomData.DUNGEON_EMPTY : RoomData.EMPTY, 0, 0);
		} else {
			if(house.get().isDungeon()) {
				house.getSecondaryPalette().setTile(chunkX, chunkY, 0, null);
			} else {
				house.getPalette().setTile(chunkX, chunkY, toHeight, null);
			}
			house.get().getRooms()[house.get().isDungeon() ? 4 : toHeight][chunkX][chunkY] = null;
		}
		house.get().setBuildPosition(p.getPosition().copy());
		house.createPalette();
		Iterator<HouseFurniture> iterator = house.get().getFurniture().iterator();
		while(iterator.hasNext()) {
			HouseFurniture pf = iterator.next();
			if(pf.getRoomX() == chunkX && pf.getRoomY() == chunkY && pf.getRoomZ() == toHeight)
				iterator.remove();
		}
		for(Portal port : house.get().getPortals()) {
			if(port.getRoomX() == chunkX && port.getRoomY() == chunkY && port.getRoomZ() == toHeight)
				iterator.remove();
		}
		house.createPalette();
		house.refresh();
	}
	
	public static boolean roomExists(Player p) {
		House house = p.getHouse();
		int[] myTiles = getMyChunk(p);
		int xOnTile = getXTilesOnTile(myTiles, p);
		int yOnTile = getYTilesOnTile(myTiles, p);
		int direction = 0;
		final int LEFT = 0, DOWN = 1, RIGHT = 2, UP = 3;
		if (xOnTile == 0)
			direction = LEFT;
		if (yOnTile == 0)
			direction = DOWN;
		if (xOnTile == 7)
			direction = RIGHT;
		if (yOnTile == 7)
			direction = UP;
		int xOff = 0, yOff = 0;
		if (direction == LEFT) {
			xOff = -1;
		}
		if (direction == DOWN) {
			yOff = -1;
		}
		if (direction == RIGHT) {
			xOff = 1;
		}
		if (direction == UP) {
			yOff = 1;
		}
		Room room = house.get().getRooms()[house.get().isDungeon() ? 4 : p.getPosition().getZ()][myTiles[0] - 1 + xOff][myTiles[1] - 1 + yOff];
		if (room == null)
			return false;
		if(room.data().getId() == Constants.BUILDABLE || room.data().getId() == Constants.EMPTY || room.data().getId() == Constants.DUNGEON_EMPTY)
			return false;
		return true;
	}
	
}
