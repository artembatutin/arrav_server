package net.arrav.world;


import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.arrav.util.Utility;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.entity.region.Region;
import net.arrav.world.locale.Position;

import java.util.ArrayList;

public class Area {

	private int west;
	private int south;
	private int east;
	private int north;

	private Area() {}
	/**
	 * 
	 * @param west x1 position
	 * @param south y1 position
	 * @param east x2 position
	 * @param north y2 position
	 */
	public Area(int west, int south, int east, int north) {
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
	}

	public Area(int radius) {
		this.north = radius;
		this.east = radius;
		this.south = radius;
		this.west = radius;
	}

	public Area(Position base, Position top) {
		this.west = base.getX();
		this.south = base.getY();
		this.east = top.getX();
		this.north = top.getY();
	}

	public int getWest() {
		return west;
	}

	public void setWest(int west) {
		this.west = west;
	}

	public int getSouth() {
		return south;
	}

	public void setSouth(int south) {
		this.south = south;
	}

	public int getEast() {
		return east;
	}

	public void setEast(int east) {
		this.east = east;
	}

	public int getNorth() {
		return north;
	}

	public void setNorth(int north) {
		this.north = north;
	}

	public static Area region(int regionId) {
		Position base = new Position( ((regionId >> 8) << 6), ((regionId & 0xFF) << 6));
		Position top = base.copy().move(64, 64);
		return new Area(base, top);
	}

	@Override
	public String toString() {
		return "Area [west=" + west + ", south=" + south + ", east=" + east + ", north=" + north + "]";
	}
	
	public boolean hasArea() {
		return west + south + east + north != 0;
	}

	public Position getRandomPosition() {
		int x = west + RandomUtils.inclusive(east - west);
		int y = south + RandomUtils.inclusive(north - south);
		return new Position(x, y, 0);
	}

	/**
	 * Get's a region id associated with this area.
	 * @return The id of the {@link Region} in this area.
	 */
	public int getRegion() {
		if(west - east > 64)
			throw new UnsupportedOperationException("The Area has more than one region. use getRegions()");
		return new Position(west, south).getRegion();
	}

	public ObjectArrayList<Region> getRegions() {
	if((west - east) <= 64)
		throw new UnsupportedOperationException("The Area one region. use getRegion()");

	int regionAmt = ((west - east) / 64) * (north - south) / 64;
	return null;
	}

	public Position getRandomPosition(int maxDistance) {
		int x = west + Utility.clamp(RandomUtils.inclusive(east - west), 0, maxDistance);
		int y = south + Utility.clamp(RandomUtils.inclusive(north - south), 0, maxDistance);
		return new Position(x, y, 0);
	}

	public Position getRandomPosition(int maxDistance, int height) {
		int x = west + Utility.clamp(RandomUtils.inclusive(east - west), 0, maxDistance);
		int y = south + Utility.clamp(RandomUtils.inclusive(north - south), 0, maxDistance);
		return new Position(x, y, height);
	}

	public Area getAbsolute(Position p) {
		Area area = new Area();
		area.west = p.getX() - west;
		area.north = p.getY() + north;
		area.south = p.getY() - south;
		area.east = p.getX() + east;
		return area;
	}

	public ArrayList<Position> getAllpositions() {
		ArrayList<Position> positions = new ArrayList<>();
		for(int x = west; x < east; x++) {
			for(int y = south; y < north; y++) {
				positions.add(new Position(x, y));
			}
		}
		return positions;
	}

	public static Area grow(Position position, int grow) {
		int north = position.getY() + grow;
		int east = position.getX() + grow;
		int west = position.getX() - grow;
		int south = position.getY() - grow;
		return new Area(west, south, east, north);
	}

	public static Area grow(Position position, int x, int y, boolean fromCenter) {
		int north = position.getY() + y;
		int east = position.getX() + x;
		int west = fromCenter ? position.getX() - x : position.getX();
		int south = fromCenter ? position.getY() - y : position.getY();
		return new Area(west, south, east, north);
	}



	public static Area fromAbsolute(Position position, Area walkingArea) {
		int north = walkingArea.getNorth() - position.getY();
		int east = walkingArea.getEast() - position.getX();
		int south = position.getY() - walkingArea.getSouth();
		int west = position.getX() - walkingArea.getWest();
		return new Area(west, south, east, north);
	}
	
	public static Area of(int radius) {
		return new Area(radius);
	}
	
	public static Area of(int west, int south, int east, int north) {
		return new Area(west, south, east, north);
	}
	
	public static Area of(Position p1, Position p2) {
		int north = Math.max(p1.getY(), p2.getY());
		int east = Math.max(p1.getX(), p2.getX());
		int south = Math.min(p1.getY(), p2.getY());
		int west = Math.min(p1.getX(), p2.getX());
		return Area.of(west, south, east, north);
	}
	
	private static Area copy(Area area) {
		return new Area(area.west, area.south, area.east, area.north);
	}
}