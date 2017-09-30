package net.edge.content.teleport;

import net.edge.world.Animation;
import net.edge.world.Graphic;

import java.util.Optional;

/**
 * Possible teleportation types enumeration.
 * @author Artem Batutin
 */
public enum TeleportType {
	NORMAL(3, 6, Optional.of(new Animation(8939)), Optional.of(new Animation(8941)), Optional.of(new Graphic(1576)), Optional.of(new Graphic(1577))),
	ANCIENT(2, 3, Optional.of(new Animation(9599)), Optional.empty(), Optional.of(new Graphic(1681)), Optional.empty()),
	LUNAR(5, 5, Optional.of(new Animation(8939)), Optional.empty(), Optional.of(new Graphic(1685)), Optional.empty()),
	TABLET(4, 5, Optional.of(new Animation(4731)), Optional.empty(), Optional.of(new Graphic(678)), Optional.empty()),
	LEVER(4, 4, Optional.of(new Animation(8939)), Optional.of(new Animation(8941)), Optional.of(new Graphic(1576)), Optional.of(new Graphic(1577))),
	LADDER(1, 2, Optional.of(new Animation(828)), Optional.empty(), Optional.empty(), Optional.empty()),
	DOOR(1, 2, Optional.of(new Animation(9105)), Optional.empty(), Optional.empty(), Optional.empty()),
	//TODO: Fix animation.
	OBELISK(3, 6, Optional.of(new Animation(8939)), Optional.of(new Animation(8941)), Optional.of(new Graphic(661)), Optional.empty()),
	VOID_FAMILIAR(3, 2, Optional.of(new Animation(8136)), Optional.of(new Animation(8137)), Optional.of(new Graphic(1503)), Optional.of(new Graphic(1502))),
	FREEZE(5, 8, Optional.of(new Animation(11044)), Optional.empty(), Optional.of(new Graphic(1973)), Optional.empty());
	/**
	 * The start delay for this teleport.
	 */
	private final int startDelay;
	
	/**
	 * The ending delay for this teleport.
	 */
	private final int endDelay;
	
	/**
	 * The start animation of this teleport.
	 */
	private final Optional<Animation> startAnimation;
	
	/**
	 * The end animation of this teleport.
	 */
	private final Optional<Animation> endAnimation;
	
	/**
	 * The start graphic of this teleport.
	 */
	private final Optional<Graphic> startGraphic;
	
	/**
	 * The end graphic of this teleport.
	 */
	private final Optional<Graphic> endGraphic;
	
	/**
	 * Constructs a new {@link TeleportType}.
	 * @param startDelay     {@link #startDelay}.
	 * @param endDelay       {@link #endDelay}.
	 * @param startAnimation {@link #startAnimation}.
	 * @param endAnimation   {@link #endAnimation}.
	 * @param startGraphic   {@link #startGraphic}.
	 * @param endGraphic     {@link #endGraphic}.
	 */
	TeleportType(int startDelay, int endDelay, Optional<Animation> startAnimation, Optional<Animation> endAnimation, Optional<Graphic> startGraphic, Optional<Graphic> endGraphic) {
		this.startDelay = startDelay;
		this.endDelay = endDelay;
		this.startAnimation = startAnimation;
		this.endAnimation = endAnimation;
		this.startGraphic = startGraphic;
		this.endGraphic = endGraphic;
	}
	
	/**
	 * The delay before the teleport ends.
	 * @return The delay before the teleport ends.
	 */
	public int getStartDelay() {
		return startDelay;
	}
	
	/**
	 * The delay before the player can walk after the teleport.
	 * @return The delay before the player can walk after the teleport.
	 */
	public int getEndDelay() {
		return endDelay;
	}
	
	/**
	 * Gets the start animation for the teleport.
	 * @return The animation to display at the start of the teleport.
	 */
	public Optional<Animation> getStartAnimation() {
		return startAnimation;
	}
	
	/**
	 * Gets the end animation for the teleport.
	 * @return The animation to display at the end of the teleport.
	 */
	public Optional<Animation> getEndAnimation() {
		return endAnimation;
	}
	
	/**
	 * Gets the start graphic for the teleport.
	 * @return The graphic to display at the beginning of the teleport.
	 */
	public Optional<Graphic> getStartGraphic() {
		return startGraphic;
	}
	
	/**
	 * Gets the end graphic for the teleport.
	 * @return The graphic to display at the end of the teleport.
	 */
	public Optional<Graphic> getEndGraphic() {
		return endGraphic;
	}
}
