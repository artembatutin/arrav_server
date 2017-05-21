package net.edge.world.object;

/**
 * Represents the definition of some game object.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class ObjectDefinition {
	
	/**
	 * The game object definitions.
	 */
	public final static ObjectDefinition[] DEFINITIONS = new ObjectDefinition[104356];
	
	/**
	 * The object id.
	 */
	private final int id;
	
	/**
	 * The name.
	 */
	private final String name;
	
	/**
	 * The description.
	 */
	private final String description;
	
	/**
	 * The horizontal size.
	 */
	private final int sizeX;
	
	/**
	 * The vertical size.
	 */
	private final int sizeY;
	
	/**
	 * The actions.
	 */
	private final String[] actions;
	
	/**
	 * The actions flag.
	 */
	private final boolean hasActions;
	
	/**
	 * The 'solid object' flag, {@code false} by default.
	 */
	private final boolean solid;
	
	/**
	 * The walkable flag, {@code false} by default.
	 */
	private final boolean walkable;
	
	/**
	 * The wall flag, {@code false} by default.
	 */
	private final boolean wall;
	
	/**
	 * The decoration flag, {@code false} by default.
	 */
	private final boolean decoration;
	
	/**
	 * Creates a new object definition.
	 */
	public ObjectDefinition(int id, String name, String description, int sizeX, int sizeY, String[] actions, boolean hasActions, boolean solid, boolean walkable, boolean wall, boolean decoration) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.actions = actions;
		this.hasActions = hasActions;
		this.solid = solid;
		this.walkable = walkable;
		this.wall = wall;
		this.decoration = decoration;
	}
	
	/**
	 * Gets the object id.
	 * @return The object id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the description.
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the object size, in tiles.
	 * @return The size.
	 */
	public int getSize() {
		return sizeX + sizeY;
	}
	
	/**
	 * Gets the object's horizontal X size.
	 * @return The X size.
	 */
	public int getSizeX() {
		return sizeX;
	}
	
	/**
	 * Gets the object's horizontal Y size.
	 * @return The Y size.
	 */
	public int getSizeY() {
		return sizeY;
	}
	
	/**
	 * Gets the object's solid flag.
	 * @return The solid flag.
	 */
	public boolean isSolid() {
		return solid;
	}
	
	/**
	 * Gets the object's walkable flag.
	 */
	public boolean isWalkable() {
		return walkable;
	}
	
	/**
	 * Gets the object's wall flag.
	 */
	public boolean isWall() {
		return wall;
	}
	
	/**
	 * Gets the object's decoration flag.
	 */
	public boolean isDecoration() {
		return decoration;
	}
	
	/**
	 * Gets the actions flag.
	 * @return The actions flag.
	 */
	public boolean hasActions() {
		return hasActions;
	}
	
	/**
	 * Determines if the specified action exists in the object's action array.
	 * @return {@code true} if the action exists in the object's action array, otherwise {@code false}.
	 */
	public boolean hasAction(String action) {
		for(String a : actions) {
			if(a != null && a.toLowerCase().equals(action.toLowerCase()))
				return true;
		}
		return false;
	}
	
}