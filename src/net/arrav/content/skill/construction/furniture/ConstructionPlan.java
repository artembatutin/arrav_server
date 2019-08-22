package net.arrav.content.skill.construction.furniture;

/**
 * Represents a plan to build a furniture, caching values to avoid loops.
 * @author Artem Batutin
 */
public class ConstructionPlan {
	
	/**
	 * The selected furniture.
	 */
	private Furniture selected;
	
	/**
	 * The furniture displayed on the player's panel.
	 */
	private Furniture[] panel = null;
	
	/**
	 * The clicked object coordinates.
	 */
	private int objectX, objectY;
	
	/**
	 * Gets the furniture displayed on the construction panel.
	 * @return displayed furniture.
	 */
	public Furniture[] getPanel() {
		return panel;
	}
	
	/**
	 * Sets a new panel furniture list.
	 * @param panel furniture list displayed.
	 */
	public void setPanel(Furniture[] panel) {
		this.panel = panel;
	}
	
	/**
	 * @return The selected furniture to build.
	 */
	public Furniture getSelected() {
		return selected;
	}
	
	/**
	 * Selects a furniture to build.
	 * @param selected selected furniture.
	 */
	public void setSelected(Furniture selected) {
		this.selected = selected;
	}
	
	/**
	 * @return x object coordinate.
	 */
	public int getObjectX() {
		return objectX;
	}
	
	/**
	 * Sets the x object coordinate.
	 * @param objectX x coord.
	 */
	public void setObjectX(int objectX) {
		this.objectX = objectX;
	}
	
	/**
	 * @return y object coordinate.
	 */
	public int getObjectY() {
		return objectY;
	}
	
	/**
	 * Sets the y object coordinate.
	 * @param objectY y coord.
	 */
	public void setObjectY(int objectY) {
		this.objectY = objectY;
	}
}
