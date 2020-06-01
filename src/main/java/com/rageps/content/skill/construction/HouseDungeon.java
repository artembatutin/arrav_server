package com.rageps.content.skill.construction;

/**
 * Represents a player's {@link Construction} house dungeon.
 * @author Artem Batutin
 */
public class HouseDungeon {
	
	private House house;
	
	public House getHouse() {
		return house;
	}
	
	public void setHouse(House house) {
		this.house = house;
	}

	/*public void playerKilled(Player p)
	{
		house.members.remove(p);
		removePlayer(p);
		p.getPacketSender().sendInteractionOption("null", 3, true);
		
	}
	
	@Override
	public void remove(final Character p)
	{
		/*if(p.loggingOut)
		{
			members.remove(p);
			house.members.remove(p);
			if(members.isEmpty() && house.members.isEmpty())
				house.destroy();
			return;
		}
		p.getPacketSender().sendInteractionOption("null", 3, true);
		house.addMember(p);
		members.remove(p);
		int[] myTiles = Construction.getMyChunk(p);
		if(myTiles == null)
			return;
		Room room = getOwner().houseRooms[0][myTiles[0] - 1][myTiles[1] - 1];
		int[] converted = Construction.getConvertedCoords(3, 2, myTiles, room);
		p.toConsCoords = converted;
		Arrav.getTaskScheduler().schedule(new Task(1, p, false) {
			@Override
			protected void execute() {
				p.getPacketSender().sendObjectsRemoval(0, 0, 10);
				if(p.getFields().mapInstance != null)
				Construction.enterHouse(p, p.getFields().mapInstance.getOwner(),
						p.inBuildingMode);
				this.stop();
			}
		});
	}
	@Override
	public void add(Character character)
	{
	/*	members.add(p);
		p.getFields().mapInstance = this;
		final int[] myTiles_ = Construction.getMyChunk(p);
		final Room room_ = getOwner().houseRooms[4][myTiles_[0] - 1][myTiles_[1] - 1];
		p.getPacketSender().sendInterface(28640);
		p.getPacketSender().sendMapState(2);
		final Player p_ = p;
		Arrav.getTaskScheduler().schedule(new Task(1, p, false) {
			int ticks;
			@Override
			public void execute() {
				if (ticks == 0) {
					p_.getPacketSender().sendObjectsRemoval(0, 0, 10);
				}
				int[] converted = Construction.getConvertedCoords(p_.toConsCoords == null ? 1 : p_.toConsCoords[0],
						p_.toConsCoords == null ? 5 : p_.toConsCoords[1],
						myTiles_, room_);
				p_.moveTo(new Position(converted[0],
						converted[1], 0));
				p_.toConsCoords = null;
				if (ticks == 1) {
					p_.getPacketSender().constructMapRegionForConstruction(getHouse()
									.getSecondaryPalette());
					Construction.placeAllFurniture(p_, 4);
					p_.getPacketSender().sendInterfaceRemoval();
					p_.getPacketSender().sendMapState(0);
					this.stop();
				}
				ticks++;
			}
		});*/
	
}
