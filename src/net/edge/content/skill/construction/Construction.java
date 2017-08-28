package net.edge.content.skill.construction;

import net.edge.GameConstants;
import net.edge.content.skill.construction.data.Constants;
import net.edge.content.skill.construction.furniture.Furniture;
import net.edge.content.skill.construction.furniture.HotSpots;
import net.edge.content.skill.construction.furniture.Portals;
import net.edge.content.skill.construction.room.Room;
import net.edge.content.skill.construction.room.RoomData;
import net.edge.content.skill.construction.room.RoomFurniture;
import net.edge.net.packet.out.SendFade;
import net.edge.net.packet.out.SendMinimapState;
import net.edge.net.packet.out.SendObject;
import net.edge.net.packet.out.SendPaletteMap;
import net.edge.task.Task;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.locale.Position;

/**
 * Handling the main construction skill.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Construction {
	
	public static boolean hasHouse(Player p) {
		return p.getHouse().get().getRooms()[0][0][0] != null;
	}
	
	public static void onLogout(Player p) {
		if(p.getHouse().get().getState() == HouseController.State.AWAY)
			return;
		remove(p);
		p.move(GameConstants.STARTING_POSITION);
	}
	
	public static void remove(Player p) {
		if(p.getHouse().get().getState() == HouseController.State.AWAY)
			return;
		p.getHouse().get().getActive().removePlayer(p);
	}
	
	public static void buyHouse(Player p) {
		p.closeWidget();
		House house = p.getHouse();
		if(hasHouse(p)) {
			p.message("You already own a house. Use the portal to enter it.");
			return;
		}
		if(!p.getInventory().contains(new Item(995, 5000000))) {
			p.message("You do not have 5,000,000 coins.");
			return;
		}
		p.getInventory().remove(new Item(995, 5000000));
		for(int x = 0; x < 13; x++) {
			for(int y = 0; y < 13; y++) {
				house.get().getRooms()[0][x][y] = new Room(RoomData.EMPTY, 0, 0);
			}
		}
		house.get().getRooms()[0][7][7] = new Room(RoomData.GARDEN, 0, 0);
		house.get().getRooms()[0][7][7].addFurniture(new RoomFurniture(Furniture.EXIT_PORTAL, HotSpots.CENTREPIECE.getXOffset(), HotSpots.CENTREPIECE.getYOffset()));
		p.message("You've purchased a house. Use the portal to enter it.");
	}
	
	public static void enterHouse(Player p, boolean buildingMode) {
		p.closeWidget();
		House house = p.getHouse();
		if(house.get().getRooms()[0][0][0] == null) {
			p.message("You don't own a house. Talk to the Estate agent to buy one.");
			return;
		}
		enterHouse(p, p, buildingMode);
	}
	
	public static void enterHouse(final Player me, final Player owner, boolean building) {
		House house = owner.getHouse();
		if(house.getPalette() == null)
			house.createPalette();
		me.getHouse().get().setBuilding(building);
		me.closeWidget();
		me.out(new SendMinimapState(2));
		me.out(new SendFade(20, 300, 200));
		Task delay = new Task(1) {
			int x = -1, y = -1, tick = 0;
			
			@Override
			protected void execute() {
				tick++;
				switch(tick) {
					case 2:
						me.move(new Position(Constants.MIDDLE_X, Constants.MIDDLE_Y, 0));
						break;
					case 3:
						me.write(new SendPaletteMap(house.getPalette()));
						break;
					case 4:
						placeAllFurniture(me, 0);
						placeAllFurniture(me, 1);
						if(house.get().isDungeon()) {
							placeAllFurniture(me, 4);
						}
						if(house.get().buildPosition() != null) {
							me.move(house.get().buildPosition());
							house.get().setBuildPosition(null);
						} else {
							Position portal = Portals.findNearestPortal(house);
							if(portal != null) {
								x = Constants.BASE_X + ((portal.getX() + 1) * 8);
								y = Constants.BASE_Y + ((portal.getY() + 1) * 8);
								me.move(new Position(x, y).move(2, 3));
							}
						}
						house.addPlayer(me);
						me.out(new SendMinimapState(0));
						cancel();
						break;
				}
			}
		};
		delay.submit();
	}
	
	static int[] getConvertedCoords(int tileX, int tileY, int[] myTiles, Room room) {
		int actualX = Constants.BASE_X + (myTiles[0] * 8);
		actualX += Constants.getXOffsetForObjectId(1, tileX, tileY, room.getRotation(), 0);
		int actualY = Constants.BASE_Y + (myTiles[1] * 8);
		actualY += Constants.getYOffsetForObjectId(1, tileX, tileY, room.getRotation(), 0);
		return new int[]{actualX, actualY};
	}
	
	public static int[] getMyChunk(Player p) {
		for(int x = 0; x < 13; x++) {
			for(int y = 0; y < 13; y++) {
				int minX = ((Constants.BASE_X) + (x * 8));
				int maxX = ((Constants.BASE_X + 7) + (x * 8));
				int minY = ((Constants.BASE_Y) + (y * 8));
				int maxY = ((Constants.BASE_Y + 7) + (y * 8));
				if(p.getPosition().getX() >= minX && p.getPosition().getX() <= maxX && p.getPosition().getY() >= minY && p.getPosition().getY() <= maxY) {
					return new int[]{x, y};
				}
			}
		}
		return null;
	}
	
	public static int getXTilesOnTile(int[] tile, Player p) {
		int baseX = Constants.BASE_X + (tile[0] * 8);
		return p.getPosition().getX() - baseX;
	}
	
	public static int getYTilesOnTile(int[] tile, Player p) {
		int baseY = Constants.BASE_Y + (tile[1] * 8);
		return p.getPosition().getY() - baseY;
	}
	
	static int getXTilesOnTile(int[] tile, int myX) {
		int baseX = Constants.BASE_X + (tile[0] * 8);
		return myX - baseX;
	}
	
	static int getYTilesOnTile(int[] tile, int myY) {
		int baseY = Constants.BASE_Y + (tile[1] * 8);
		return myY - baseY;
	}
	
	private static int[] getMyChunkFor(int xx, int yy) {
		for(int x = 0; x < 13; x++) {
			for(int y = 0; y < 13; y++) {
				int minX = ((Constants.BASE_X) + (x * 8));
				int maxX = ((Constants.BASE_X + 7) + (x * 8));
				int minY = ((Constants.BASE_Y) + (y * 8));
				int maxY = ((Constants.BASE_Y + 7) + (y * 8));
				if(xx >= minX && xx <= maxX && yy >= minY && yy <= maxY) {
					return new int[]{x, y};
				}
			}
		}
		return null;
	}
	
	public static void placeAllFurniture(Player p, int heightLevel) {
		House house = p.getHouse();
		for(int x = 0; x < house.get().getRooms()[heightLevel].length; x++) {
			for(int y = 0; y < house.get().getRooms()[heightLevel][x].length; y++) {
				Room room = house.get().getRooms()[heightLevel][x][y];
				if(room == null)
					continue;
				if(room.getFurniture() == null)
					continue;
				for(RoomFurniture furniture : room.getFurniture()) {
					if(furniture == null)
						continue;
					HotSpots hs = room.data().getSpot(furniture.getStandardXOff(), furniture.getStandardYOff());
					if(hs == null)
						continue;
					int actualX = Constants.BASE_X + (x + 1) * 8;
					actualX += Constants.getXOffsetForObjectId(furniture.getFurniture().getId(), hs, room.getRotation());
					int actualY = Constants.BASE_Y + (y + 1) * 8;
					actualY += Constants.getYOffsetForObjectId(furniture.getFurniture().getId(), hs, room.getRotation());
					HotSpots[] spots = room.data().getSpots();
					doFurniturePlace(hs, furniture.getFurniture(), spots, getMyChunkFor(actualX, actualY), actualX, actualY, room.getRotation(), p, false, heightLevel);
				}
			}
		}
	}
	
	public static void doFurniturePlace(HotSpots s, Furniture f, HotSpots[] spots, int[] myTiles, int actualX, int actualY, int roomRot, Player p, boolean placeBack, int height) {
		int portalId = -1;
		House house = p.getHouse();
		if(s.getHotSpotId() == 72) {
			if(s.getXOffset() == 0) {
				for(Portal portal : house.get().getPortals()) {
					if(portal.getRoomX() == myTiles[0] - 1 && portal.getRoomY() == myTiles[1] - 1 && portal.getRoomZ() == height && portal.getId() == 0) {
						if(Portals.forType(portal.getType()).getObjects() != null)
							portalId = Portals.forType(portal.getType()).getObjects()[f.getId() - 13636];
						
					}
				}
			}
			if(s.getXOffset() == 3) {
				for(Portal portal : house.get().getPortals()) {
					if(portal.getRoomX() == myTiles[0] - 1 && portal.getRoomY() == myTiles[1] - 1 && portal.getRoomZ() == height && portal.getId() == 1) {
						if(Portals.forType(portal.getType()).getObjects() != null)
							portalId = Portals.forType(portal.getType()).getObjects()[f.getId() - 13636];
						
					}
				}
				
			}
			if(s.getXOffset() == 7) {
				for(Portal portal : house.get().getPortals()) {
					if(portal.getRoomX() == myTiles[0] - 1 && portal.getRoomY() == myTiles[1] - 1 && portal.getRoomZ() == height && portal.getId() == 2) {
						if(Portals.forType(portal.getType()).getObjects() != null)
							portalId = Portals.forType(portal.getType()).getObjects()[f.getId() - 13636];
						
					}
				}
			}
		}
		if(height == 4)
			height = 0;
		if(s.getHotSpotId() == 92) {
			int offsetX = Constants.BASE_X + (myTiles[0] * 8);
			int offsetY = Constants.BASE_Y + (myTiles[1] * 8);
			if(s.getObjectId() == 15329 || s.getObjectId() == 15328) {
				SendObject.construction(p, actualX, actualY, s.getObjectId() == 15328 ? (placeBack ? 15328 : f.getId()) : (placeBack ? 15329 : f.getId() + 1), s.getRotation(roomRot), 0, height);
				offsetX += Constants.getXOffsetForObjectId(f.getId(), s.getXOffset() + (s.getObjectId() == 15329 ? 1 : -1), s.getYOffset(), roomRot, s.getRotation(0));
				offsetY += Constants.getYOffsetForObjectId(f.getId(), s.getXOffset() + (s.getObjectId() == 15329 ? 1 : -1), s.getYOffset(), roomRot, s.getRotation(0));
				SendObject.construction(p, offsetX, offsetY, s.getObjectId() == 15329 ? (placeBack ? 15328 : f.getId()) : (placeBack ? 15329 : f.getId() + 1), s.getRotation(roomRot), 0, height);
				
			}
			if(s.getObjectId() == 15326 || s.getObjectId() == 15327) {
				SendObject.construction(p, actualX, actualY, s.getObjectId() == 15327 ? (placeBack ? 15327 : f.getId() + 1) : (placeBack ? 15326 : f.getId()), s.getRotation(roomRot), 0, height);
				offsetX += Constants.getXOffsetForObjectId(f.getId(), s.getXOffset() + (s.getObjectId() == 15326 ? 1 : -1), s.getYOffset(), roomRot, s.getRotation(0));
				offsetY += Constants.getYOffsetForObjectId(f.getId(), s.getXOffset() + (s.getObjectId() == 15326 ? 1 : -1), s.getYOffset(), roomRot, s.getRotation(0));
				SendObject.construction(p, offsetX, offsetY, s.getObjectId() == 15326 ? (placeBack ? 15327 : f.getId() + 1) : (placeBack ? 15326 : f.getId()), s.getRotation(roomRot), 0, height);
				
			}
		} else if(s.getHotSpotId() == 85) {
			actualX = Constants.BASE_X + (myTiles[0] * 8) + 2;
			actualY = Constants.BASE_Y + (myTiles[1] * 8) + 2;
			int type = 22, leftObject = 0, rightObject = 0, upperObject = 0, downObject = 0, middleObject = 0, veryMiddleObject = 0, cornerObject = 0;
			if(f.getId() == 13331) {
				leftObject = rightObject = upperObject = downObject = 13332;
				middleObject = 13331;
				cornerObject = 13333;
			}
			if(f.getId() == 13334) {
				leftObject = rightObject = upperObject = downObject = 13335;
				middleObject = 13334;
				cornerObject = 13336;
			}
			if(f.getId() == 13337) {
				leftObject = rightObject = upperObject = downObject = middleObject = cornerObject = 13337;
				type = 10;
			}
			if(f.getId() == 13373) {
				veryMiddleObject = 13373;
				leftObject = rightObject = upperObject = downObject = middleObject = 6951;
			}
			if(placeBack || f.getId() == 13337) {
				for(int x = 0; x < 4; x++) {
					for(int y = 0; y < 4; y++) {
						SendObject.construction(p, actualX + x, actualY + y, 6951, 0, 10, height);
						SendObject.construction(p, actualX + x, actualY + y, 6951, 0, 22, height);
					}
				}
				
			}
			SendObject.construction(p, actualX, actualY, placeBack ? 15348 : cornerObject, 1, type, height);
			SendObject.construction(p, actualX, actualY + 1, placeBack ? 15348 : leftObject, 1, type, height);
			SendObject.construction(p, actualX, actualY + 2, placeBack ? 15348 : leftObject, 1, type, height);
			SendObject.construction(p, actualX, actualY + 3, placeBack ? 15348 : cornerObject, 2, type, height);
			SendObject.construction(p, actualX + 1, actualY + 3, placeBack ? 15348 : upperObject, 2, type, height);
			SendObject.construction(p, actualX + 2, actualY + 3, placeBack ? 15348 : upperObject, 2, type, height);
			SendObject.construction(p, actualX + 3, actualY + 3, placeBack ? 15348 : cornerObject, 3, type, height);
			SendObject.construction(p, actualX + 3, actualY + 2, placeBack ? 15348 : rightObject, 3, type, height);
			SendObject.construction(p, actualX + 3, actualY + 1, placeBack ? 15348 : rightObject, 3, type, height);
			SendObject.construction(p, actualX + 3, actualY, placeBack ? 15348 : cornerObject, 0, type, height);
			SendObject.construction(p, actualX + 2, actualY, placeBack ? 15348 : downObject, 0, type, height);
			SendObject.construction(p, actualX + 1, actualY, placeBack ? 15348 : downObject, 0, type, height);
			SendObject.construction(p, actualX + 1, actualY + 1, placeBack ? 15348 : middleObject, 0, type, height);
			SendObject.construction(p, actualX + 2, actualY + 1, placeBack ? 15348 : middleObject, 0, type, height);
			if(veryMiddleObject != 0)
				SendObject.construction(p, actualX + 1, actualY + 2, veryMiddleObject, 0, 10, height);
			SendObject.construction(p, actualX + 1, actualY + 2, placeBack ? 15348 : middleObject, 0, type, height);
			SendObject.construction(p, actualX + 2, actualY + 2, placeBack ? 15348 : middleObject, 0, type, height);
			
		} else if(s.getHotSpotId() == 86) {
			actualX = Constants.BASE_X + (myTiles[0] * 8) + 2;
			actualY = Constants.BASE_Y + (myTiles[1] * 8) + 2;
			SendObject.construction(p, actualX + 1, actualY, placeBack ? 15352 : f.getId(), 3, 0, height);
			SendObject.construction(p, actualX + 2, actualY, placeBack ? 15352 : f.getId(), 3, 0, height);
			SendObject.construction(p, actualX + 3, actualY, placeBack ? 15352 : f.getId(), 2, 2, height);
			SendObject.construction(p, actualX + 3, actualY + 1, placeBack ? 15352 : f.getId(), 2, 0, height);
			SendObject.construction(p, actualX + 3, actualY + 2, placeBack ? 15352 : f.getId() + 1, 2, 0, height);
			SendObject.construction(p, actualX + 3, actualY + 3, placeBack ? 15352 : f.getId(), 1, 2, height);
			SendObject.construction(p, actualX + 2, actualY + 3, placeBack ? 15352 : f.getId(), 1, 0, height);
			SendObject.construction(p, actualX + 1, actualY + 3, placeBack ? 15352 : f.getId(), 1, 0, height);
			SendObject.construction(p, actualX, actualY + 3, placeBack ? 15352 : f.getId(), 0, 2, height);
			SendObject.construction(p, actualX, actualY + 2, placeBack ? 15352 : f.getId(), 0, 0, height);
			SendObject.construction(p, actualX, actualY + 1, placeBack ? 15352 : f.getId(), 0, 0, height);
			SendObject.construction(p, actualX, actualY, placeBack ? 15352 : f.getId(), 3, 2, height);
			
		} else if(s.getHotSpotId() == 78) {
			actualX = Constants.BASE_X + (myTiles[0] * 8);
			actualY = Constants.BASE_Y + (myTiles[1] * 8);
			// south walls
			SendObject.construction(p, actualX, actualY, placeBack ? 15369 : f.getId(), 3, 2, height);
			SendObject.construction(p, actualX + 1, actualY, placeBack ? 15369 : f.getId(), 3, 0, height);
			SendObject.construction(p, actualX + 2, actualY, placeBack ? 15369 : f.getId(), 3, 0, height);
			SendObject.construction(p, actualX + 5, actualY, placeBack ? 15369 : f.getId(), 3, 0, height);
			SendObject.construction(p, actualX + 6, actualY, placeBack ? 15369 : f.getId(), 3, 0, height);
			SendObject.construction(p, actualX + 7, actualY, placeBack ? 15369 : f.getId(), 2, 2, height);
			// north walls
			SendObject.construction(p, actualX, actualY + 7, placeBack ? 15369 : f.getId(), 0, 2, height);
			SendObject.construction(p, actualX + 1, actualY + 7, placeBack ? 15369 : f.getId(), 1, 0, height);
			SendObject.construction(p, actualX + 2, actualY + 7, placeBack ? 15369 : f.getId(), 1, 0, height);
			SendObject.construction(p, actualX + 5, actualY + 7, placeBack ? 15369 : f.getId(), 1, 0, height);
			SendObject.construction(p, actualX + 6, actualY + 7, placeBack ? 15369 : f.getId(), 1, 0, height);
			SendObject.construction(p, actualX + 7, actualY + 7, placeBack ? 15369 : f.getId(), 1, 2, height);
			// left walls
			SendObject.construction(p, actualX, actualY + 1, placeBack ? 15369 : f.getId(), 0, 0, height);
			SendObject.construction(p, actualX, actualY + 2, placeBack ? 15369 : f.getId(), 0, 0, height);
			SendObject.construction(p, actualX, actualY + 5, placeBack ? 15369 : f.getId(), 0, 0, height);
			SendObject.construction(p, actualX, actualY + 6, placeBack ? 15369 : f.getId(), 0, 0, height);
			// right walls
			SendObject.construction(p, actualX + 7, actualY + 1, placeBack ? 15369 : f.getId(), 2, 0, height);
			SendObject.construction(p, actualX + 7, actualY + 2, placeBack ? 15369 : f.getId(), 2, 0, height);
			SendObject.construction(p, actualX + 7, actualY + 5, placeBack ? 15369 : f.getId(), 2, 0, height);
			SendObject.construction(p, actualX + 7, actualY + 6, placeBack ? 15369 : f.getId(), 2, 0, height);
		} else if(s.getHotSpotId() == 77) {
			actualX = Constants.BASE_X + (myTiles[0] * 8);
			actualY = Constants.BASE_Y + (myTiles[1] * 8);
			// left down corner
			SendObject.construction(p, actualX, actualY, placeBack ? 15372 : f.getId() + 1, 3, 10, height);
			SendObject.construction(p, actualX + 1, actualY, placeBack ? 15371 : f.getId() + 2, 0, 10, height);
			SendObject.construction(p, actualX + 2, actualY, placeBack ? 15370 : f.getId(), 0, 10, height);
			SendObject.construction(p, actualX, actualY + 1, placeBack ? 15371 : f.getId() + 2, 1, 10, height);
			SendObject.construction(p, actualX, actualY + 2, placeBack ? 15370 : f.getId(), 3, 10, height);
			// right down corner
			SendObject.construction(p, actualX + 7, actualY, placeBack ? 15372 : f.getId() + 1, 2, 10, height);
			SendObject.construction(p, actualX + 6, actualY, placeBack ? 15371 : f.getId() + 2, 0, 10, height);
			SendObject.construction(p, actualX + 5, actualY, placeBack ? 15370 : f.getId(), 2, 10, height);
			SendObject.construction(p, actualX + 7, actualY + 1, placeBack ? 15371 : f.getId() + 2, 3, 10, height);
			SendObject.construction(p, actualX + 7, actualY + 2, placeBack ? 15370 : f.getId(), 3, 10, height);
			// upper left corner
			SendObject.construction(p, actualX, actualY + 7, placeBack ? 15372 : f.getId() + 1, 0, 10, height);
			SendObject.construction(p, actualX + 1, actualY + 7, placeBack ? 15371 : f.getId() + 2, 0, 10, height);
			SendObject.construction(p, actualX + 2, actualY + 7, placeBack ? 15370 : f.getId(), 0, 10, height);
			SendObject.construction(p, actualX, actualY + 6, placeBack ? 15371 : f.getId() + 2, 1, 10, height);
			SendObject.construction(p, actualX, actualY + 5, placeBack ? 15370 : f.getId(), 1, 10, height);
			// upper right corner
			SendObject.construction(p, actualX + 7, actualY + 7, placeBack ? 15372 : f.getId() + 1, 1, 10, height);
			SendObject.construction(p, actualX + 6, actualY + 7, placeBack ? 15371 : f.getId() + 2, 0, 10, height);
			SendObject.construction(p, actualX + 5, actualY + 7, placeBack ? 15370 : f.getId(), 2, 10, height);
			SendObject.construction(p, actualX + 7, actualY + 6, placeBack ? 15371 : f.getId() + 2, 3, 10, height);
			SendObject.construction(p, actualX + 7, actualY + 5, placeBack ? 15370 : f.getId(), 1, 10, height);
		} else if(s.getHotSpotId() == 44) {
			int combatringStrings = 6951;
			int combatringFloorsCorner = 6951;
			int combatringFloorsOuter = 6951;
			int combatringFloorsInner = 6951;
			actualX = Constants.BASE_X + (myTiles[0] * 8) + 1;
			actualY = Constants.BASE_Y + (myTiles[1] * 8) + 1;
			if(!placeBack) {
				if(f.getId() == 13126) {
					combatringStrings = 13132;
					combatringFloorsCorner = 13126;
					combatringFloorsOuter = 13128;
					combatringFloorsInner = 13127;
				}
				if(f.getId() == 13133) {
					combatringStrings = 13133;
					combatringFloorsCorner = 13135;
					combatringFloorsOuter = 13134;
					combatringFloorsInner = 13136;
				}
				if(f.getId() == 13137) {
					combatringStrings = 13137;
					combatringFloorsCorner = 13138;
					combatringFloorsOuter = 13139;
					combatringFloorsInner = 13140;
				}
			}
			
			SendObject.construction(p, actualX + 2, actualY + 3, placeBack ? 15292 : combatringFloorsInner, 0, 22, height);
			SendObject.construction(p, actualX + 3, actualY + 3, placeBack ? 15292 : combatringFloorsInner, 0, 22, height);
			SendObject.construction(p, actualX + 3, actualY + 2, placeBack ? 15292 : combatringFloorsInner, 0, 22, height);
			SendObject.construction(p, actualX + 2, actualY + 2, placeBack ? 15292 : combatringFloorsInner, 0, 22, height);
			SendObject.construction(p, actualX + 2, actualY + 1, placeBack ? 15291 : combatringFloorsOuter, 3, 22, height);
			SendObject.construction(p, actualX + 3, actualY + 1, placeBack ? 15291 : combatringFloorsOuter, 3, 22, height);
			SendObject.construction(p, actualX + 2, actualY + 4, placeBack ? 15291 : combatringFloorsOuter, 1, 22, height);
			SendObject.construction(p, actualX + 3, actualY + 4, placeBack ? 15291 : combatringFloorsOuter, 1, 22, height);
			SendObject.construction(p, actualX + 4, actualY + 3, placeBack ? 15291 : combatringFloorsOuter, 2, 22, height);
			SendObject.construction(p, actualX + 4, actualY + 2, placeBack ? 15291 : combatringFloorsOuter, 2, 22, height);
			SendObject.construction(p, actualX + 1, actualY + 3, placeBack ? 15291 : combatringFloorsOuter, 0, 22, height);
			SendObject.construction(p, actualX + 1, actualY + 2, placeBack ? 15291 : combatringFloorsOuter, 0, 22, height);
			SendObject.construction(p, actualX + 4, actualY + 1, placeBack ? 15289 : combatringFloorsCorner, 3, 22, height);
			SendObject.construction(p, actualX + 4, actualY + 4, placeBack ? 15289 : combatringFloorsCorner, 2, 22, height);
			SendObject.construction(p, actualX + 1, actualY + 4, placeBack ? 15289 : combatringFloorsCorner, 1, 22, height);
			SendObject.construction(p, actualX + 1, actualY + 1, placeBack ? 15289 : combatringFloorsCorner, 0, 22, height);
			SendObject.construction(p, actualX, actualY + 4, placeBack ? 15277 : combatringStrings, 3, 0, height);
			SendObject.construction(p, actualX, actualY + 1, placeBack ? 15277 : combatringStrings, 3, 0, height);
			SendObject.construction(p, actualX + 5, actualY + 4, placeBack ? 15277 : combatringStrings, 3, 0, height);
			SendObject.construction(p, actualX + 5, actualY + 1, placeBack ? 15277 : combatringStrings, 0, 3, height);
			SendObject.construction(p, actualX + 1, actualY, placeBack ? 15277 : combatringStrings, 1, 0, height);
			SendObject.construction(p, actualX + 2, actualY, placeBack ? 15277 : combatringStrings, 1, 0, height);
			SendObject.construction(p, actualX + 3, actualY, placeBack ? 15277 : combatringStrings, 1, 0, height);
			SendObject.construction(p, actualX + 4, actualY, placeBack ? 15277 : combatringStrings, 1, 0, height);
			SendObject.construction(p, actualX + 5, actualY, placeBack ? 15277 : combatringStrings, 0, 3, height);
			SendObject.construction(p, actualX + 1, actualY + 5, placeBack ? 15277 : combatringStrings, 3, 0, height);
			SendObject.construction(p, actualX + 2, actualY + 5, placeBack ? 15277 : combatringStrings, 3, 0, height);
			SendObject.construction(p, actualX + 3, actualY + 5, placeBack ? 15277 : combatringStrings, 3, 0, height);
			SendObject.construction(p, actualX + 4, actualY + 5, placeBack ? 15277 : combatringStrings, 3, 0, height);
			SendObject.construction(p, actualX + 5, actualY + 5, placeBack ? 15277 : combatringStrings, 3, 3, height);
			SendObject.construction(p, actualX, actualY + 5, placeBack ? 15277 : combatringStrings, 2, 3, height);
			SendObject.construction(p, actualX, actualY, placeBack ? 15277 : combatringStrings, 1, 3, height);
			SendObject.construction(p, actualX, actualY + 4, placeBack ? 15277 : combatringStrings, 2, 0, height);
			SendObject.construction(p, actualX, actualY + 3, placeBack ? 15277 : combatringStrings, 2, 0, height);
			SendObject.construction(p, actualX, actualY + 2, placeBack ? 15277 : combatringStrings, 2, 0, height);
			SendObject.construction(p, actualX, actualY + 1, placeBack ? 15277 : combatringStrings, 2, 0, height);
			SendObject.construction(p, actualX + 5, actualY + 4, placeBack ? 15277 : combatringStrings, 0, 0, height);
			SendObject.construction(p, actualX + 5, actualY + 3, placeBack ? 15277 : combatringStrings, 0, 0, height);
			SendObject.construction(p, actualX + 5, actualY + 2, placeBack ? 15277 : combatringStrings, 0, 0, height);
			SendObject.construction(p, actualX + 5, actualY + 1, placeBack ? 15277 : combatringStrings, 0, 0, height);
			
			if(f.getId() == 13145) {
				SendObject.construction(p, actualX + 1, actualY + 1, placeBack ? 6951 : 13145, 0, 0, height);
				SendObject.construction(p, actualX + 2, actualY + 1, placeBack ? 6951 : 13145, 0, 0, height);
				SendObject.construction(p, actualX + 1, actualY, placeBack ? 6951 : 13145, 1, 0, height);
				SendObject.construction(p, actualX + 1, actualY + 2, placeBack ? 6951 : 13145, 3, 0, height);
				if(!placeBack)
					SendObject.construction(p, actualX + 1, actualY + 1, 13147, 0, 22, height);
				
				SendObject.construction(p, actualX + 3, actualY + 3, placeBack ? 6951 : 13145, 0, 0, height);
				SendObject.construction(p, actualX + 4, actualY + 3, placeBack ? 6951 : 13145, 0, 0, height);
				SendObject.construction(p, actualX + 3, actualY + 2, placeBack ? 6951 : 13145, 1, 0, height);
				SendObject.construction(p, actualX + 3, actualY + 4, placeBack ? 6951 : 13145, 3, 0, height);
				if(!placeBack)
					SendObject.construction(p, actualX + 3, actualY + 3, 13147, 0, 22, height);
			}
			if(f.getId() == 13142 && !placeBack) {
				SendObject.construction(p, actualX + 2, actualY + 2, 13142, 0, 22, height);
				SendObject.construction(p, actualX + 2, actualY + 1, 13143, 0, 22, height);
				SendObject.construction(p, actualX + 2, actualY + 3, 13144, 1, 22, height);
				
			}
		} else if(s.getCarpetDim() != null) {
			for(int x = 0; x < s.getCarpetDim().getWidth() + 1; x++) {
				for(int y = 0; y < s.getCarpetDim().getHeight() + 1; y++) {
					boolean isEdge = (x == 0 && y == 0 || x == 0 && y == s.getCarpetDim().getHeight() || y == 0 && x == s.getCarpetDim().getWidth() || x == s.getCarpetDim().getWidth() && y == s.getCarpetDim().getHeight());
					boolean isWall = ((x == 0 || x == s.getCarpetDim().getWidth()) && (y != 0 && y != s.getCarpetDim().getHeight()) || (y == 0 || y == s.getCarpetDim().getHeight()) && (x != 0 && x != s.getCarpetDim().getWidth()));
					int rot = 0;
					if(x == 0 && y == s.getCarpetDim().getHeight() && isEdge)
						rot = 0;
					if(x == s.getCarpetDim().getWidth() && y == s.getCarpetDim().getHeight() && isEdge)
						rot = 1;
					if(x == s.getCarpetDim().getWidth() && y == 0 && isEdge)
						rot = 2;
					if(x == 0 && y == 0 && isEdge)
						rot = 3;
					if(y == 0 && isWall)
						rot = 2;
					if(y == s.getCarpetDim().getHeight() && isWall)
						rot = 0;
					if(x == 0 && isWall)
						rot = 3;
					if(x == s.getCarpetDim().getWidth() && isWall)
						rot = 1;
					int offsetX = Constants.BASE_X + (myTiles[0] * 8);
					int offsetY = Constants.BASE_Y + (myTiles[1] * 8);
					offsetX += Constants.getXOffsetForObjectId(f.getId(), s.getXOffset() + x - 1, s.getYOffset() + y - 1, roomRot, s.getRotation(roomRot));
					offsetY += Constants.getYOffsetForObjectId(f.getId(), s.getXOffset() + x - 1, s.getYOffset() + y - 1, roomRot, s.getRotation(roomRot));
					if(isEdge)
						SendObject.construction(p, offsetX, offsetY, placeBack ? s.getObjectId() + 2 : f.getId(), HotSpots.getRotation_2(rot, roomRot), 22, height);
					else if(isWall)
						SendObject.construction(p, offsetX, offsetY, placeBack ? s.getObjectId() + 1 : f.getId() + 1, HotSpots.getRotation_2(rot, roomRot), s.getObjectType(), height);
					else
						SendObject.construction(p, offsetX, offsetY, placeBack ? s.getObjectId() : f.getId() + 2, HotSpots.getRotation_2(rot, roomRot), s.getObjectType(), height);
				}
			}
		} else if(s.isMutiple()) {
			Room room = house.get().getRooms()[house.get().isDungeon() ? 4 : p.getPosition().getZ()][myTiles[0] - 1][myTiles[1] - 1];
			for(HotSpots find : spots) {
				if(find.getObjectId() != s.getObjectId())
					continue;
				if(room != null)
					if(room.data().getId() != find.getRoomType())
						continue;
				int actualX1 = Constants.BASE_X + (myTiles[0] * 8);
				actualX1 += Constants.getXOffsetForObjectId(find.getObjectId(), find, roomRot);
				int actualY1 = Constants.BASE_Y + (myTiles[1] * 8);
				actualY1 += Constants.getYOffsetForObjectId(find.getObjectId(), find, roomRot);
				SendObject.construction(p, actualX1, actualY1, placeBack ? s.getObjectId() : f.getId(), find.getRotation(roomRot), find.getObjectType(), height);
			}
		} else {
			SendObject.construction(p, actualX, actualY, (portalId != -1 ? portalId : placeBack ? s.getObjectId() : f.getId()), s.getRotation(roomRot), s.getObjectType(), height);
		}
	}
}
