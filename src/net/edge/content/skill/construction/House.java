package net.edge.content.skill.construction;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.content.skill.construction.data.Butlers;
import net.edge.content.skill.construction.data.Constants;
import net.edge.content.skill.construction.room.Room;
import net.edge.content.skill.construction.room.RoomFurniture;
import net.edge.world.World;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.InstanceManager;

import static net.edge.content.skill.construction.HouseController.State.*;

/**
 * Represents a {@link Construction} player's house.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class House {
	
	/**
	 * The instance of this house.
	 */
	private int instance;
	
	/**
	 * The owner of this {@link House}.
	 */
	private Player owner;
	
	/**
	 * The servant serving this house.
	 */
	private Servant servant;
	
	/**
	 * The two {@link Palette} used to display the map.
	 */
	private Palette palette, secondaryPalette;
	
	/**
	 * The dungeon of this house.
	 */
	private HouseDungeon dungeon;
	
	/**
	 * The player visitors, and owner that are in the house.
	 */
	private final ObjectList<Player> visitors = new ObjectArrayList<>();
	
	/**
	 * The active mobs in this house.
	 */
	private final ObjectList<Mob> mobs = new ObjectArrayList<>();
	
	/**
	 * The house controller of the {@link #owner}.
	 */
	private final HouseController controller = new HouseController();
	
	/**
	 * Creates the {@link House} for this {@link #owner}.
	 * @param owner the owner of this house.
	 */
	public House(Player owner) {
		this.owner = owner;
		dungeon = new HouseDungeon();
		dungeon.setHouse(this);
	}
	
	public Servant getServant() {
		return servant;
	}
	
	public void process() {
		House house = owner.getHouse();
		int[] myTiles = Construction.getMyChunk(owner);
		if(myTiles == null)
			return;
		if(myTiles[0] == -1 || myTiles[1] == -1)
			return;
		Room r = house.get().getRooms()[house.get().isDungeon() ? 4 : owner.getPosition().getZ()][myTiles[0] - 1][myTiles[1] - 1];
		if(r == null)
			return;
		if(r.data().getId() == Constants.OUBLIETTE) {
			int xOnTile = Construction.getXTilesOnTile(myTiles, owner.getPosition().getX());
			int yOnTile = Construction.getYTilesOnTile(myTiles, owner.getPosition().getY());
			if(xOnTile >= 2 && xOnTile <= 5 && yOnTile >= 2 && yOnTile <= 5 && r.getFurniture() != null) {
				for(RoomFurniture f : r.getFurniture()) {
					if(f == null)
						continue;
					if(f.getFurniture().getHotSpotId() == 85) {
						if(f.getFurniture().getId() == 13334 || f.getFurniture().getId() == 13337) {
							//if(player.getConstitution() > 0) {
							//p.getCombat().appendHit(p, 20, 0, 2, false);
							//	p.setDamage(new Damage(new Hit(20, CombatIcon.NONE, Hitmask.NONE)));
							//}
						}
						break;
					}
				}
			}
		}
		
		if(r.data().getId() == Constants.CORRIDOR) {
			int[] converted = Construction.getConvertedCoords(3, 2, myTiles, r);
			int[] converted_1 = Construction.getConvertedCoords(4, 2, myTiles, r);
			int[] converted_2 = Construction.getConvertedCoords(3, 5, myTiles, r);
			int[] converted_3 = Construction.getConvertedCoords(4, 5, myTiles, r);
			if(r.getFurniture() != null && (owner.getPosition().getX() == converted[0] && owner.getPosition()
					.getY() == converted[1] || owner.getPosition().getX() == converted_1[0] && owner.getPosition()
					.getY() == converted_1[1] || owner.getPosition().getX() == converted_2[0] && owner.getPosition()
					.getY() == converted_2[1] || owner.getPosition().getX() == converted_3[0] && owner.getPosition()
					.getY() == converted_3[1])) {
				for(RoomFurniture f : r.getFurniture()) {
					if(f == null)
						continue;
					if(f.getFurniture().getHotSpotId() == 91) {
						int[] coords = Construction.getConvertedCoords(f.getStandardXOff(), f.getStandardYOff(), myTiles, r);
						if(coords[0] == myTiles[0] && coords[1] == myTiles[1]) {
							if(f.getFurniture().getId() >= 13356 || f.getFurniture().getId() <= 13360) {
								//if(player.getConstitution() > 0) {
								//p.getCombat().appendHit(p, 20, 0, 2, false);
								//p.setDamage(new Damage(new Hit(20, CombatIcon.NONE, Hitmask.NONE)));
								//}
							}
							break;
						}
					}
				}
			}
		}
	}
	
	public void refresh() {
		for(Player player : visitors) {
			Construction.enterHouse(player, this.owner, controller.isBuilding());
		}
	}
	
	public void addPlayer(Player visitor) {
		if(instance == 0)
			instance = InstanceManager.get().closeNext();
		if(visitors.contains(visitor))
			return;
		if(visitors.isEmpty()) {
			servant = new Servant(Butlers.BUTLER.getNpcId(), visitor.getPosition().move(-2, -1).copy());
			servant.setOriginalRandomWalk(true);
			servant.getMovementCoordinator().setRadius(30);
			World.get().getMobs().add(servant);
			addNpc(servant);
		}
		visitor.setInstance(instance);
		visitors.add(visitor);
		if(visitor.same(owner))
			visitor.getHouse().get().setState(HOME);
		else
			visitor.getHouse().get().setState(VISITING);
		visitor.getHouse().get().setActive(this);
	}
	
	public void removePlayer(Player player) {
		player.setInstance(0);
		visitors.remove(player);
		player.getHouse().get().setActive(null);
		player.getHouse().get().setState(AWAY);
		if(visitors.isEmpty()) {
			InstanceManager.get().open(instance);
			instance = 0;
			for(Mob n : mobs) {
				removeNpc(n);
			}
		}
	}
	
	public void addNpc(Mob mob) {
		if(instance == 0)
			instance = InstanceManager.get().closeNext();
		if(mobs.contains(mob))
			return;
		mob.setInstance(instance);
		mobs.add(mob);
	}
	
	public void removeNpc(Mob mob) {
		mob.setInstance(0);
		mobs.remove(mob);
	}
	
	public HouseDungeon getDungeon() {
		return dungeon;
	}
	
	public void setDungeon(HouseDungeon dungeon) {
		this.dungeon = dungeon;
	}
	
	public void setPalette(Palette palette) {
		this.palette = palette;
	}
	
	public Palette getPalette() {
		return this.palette;
	}
	
	public void createPalette() {
		palette = new Palette();
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 13; x++) {
				for (int y = 0; y < 13; y++) {
					if (controller.getRooms()[z][x][y] == null)
						continue;
					if (controller.getRooms()[z][x][y].getX() == 0)
						continue;
					Room room = controller.getRooms()[z][x][y];
					Palette.PaletteTile tile = new Palette.PaletteTile(room.getX(), room.getY(), room.getZ() + 1, room.getRotation());
					palette.setTile(x, y, z, tile);
				}
			}
		}
	}
	
	public void setSecondaryPalette(Palette secondaryPalette) {
		this.secondaryPalette = secondaryPalette;
	}
	
	public Palette getSecondaryPalette() {
		return this.secondaryPalette;
	}
	
	public HouseController get() {
		return controller;
	}
	
	public boolean isOwnerHome() {
		return owner.getHouse().get().getState() == HOME || owner.getHouse().get().getState() == HOME_DUNGEON;
	}
}