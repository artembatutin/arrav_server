package net.edge.world.model.node.entity.model;

import net.edge.world.model.node.entity.EntityNode;

import java.util.Optional;

/**
 * Holds functionality for combining an animation with a graphic.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Visualize {
	
	/**
	 * The animation played.
	 */
	private final Optional<Animation> animation;
	
	/**
	 * The graphic played.
	 */
	private final Optional<Graphic> graphic;
	
	/**
	 * Constructs a new {@link Visualize}.
	 * @param animation {@link #animation}.
	 * @param graphic   {@link #graphic}.
	 */
	public Visualize(Optional<Animation> animation, Optional<Graphic> graphic) {
		this.animation = animation;
		this.graphic = graphic;
	}
	
	/**
	 * Constructs a new {@link Visualize}.
	 * @param animation {@link #animation}.
	 * @param graphic   {@link #graphic}.
	 */
	public Visualize(Animation animation, Graphic graphic) {
		this(Optional.of(animation), Optional.of(graphic));
	}
	
	/**
	 * Attempts to play the visualization for the specified {@code character}.
	 * @param character the character to start the visualization for.
	 */
	public <E extends EntityNode> void play(E character) {
		getAnimation().ifPresent(character::animation);
		getGraphic().ifPresent(character::graphic);
	}
	
	/**
	 * @return {@link #animation}.
	 */
	public Optional<Animation> getAnimation() {
		return animation;
	}
	
	/**
	 * @return {@link #graphic}.
	 */
	public Optional<Graphic> getGraphic() {
		return graphic;
	}
}
