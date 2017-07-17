package net.edge.world.node.actor.player.assets;

import net.edge.task.Task;
import net.edge.content.TabInterface;
import net.edge.locale.Position;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.World;
import net.edge.world.node.actor.player.Player;

/**
 * The enumerated type whose elements represent a spellbook type.
 * @author lare96 <http://github.com/lare96>
 */
public enum Spellbook {
	NORMAL(1151) {
		@Override
		public void execute(Player player, Position position) {
			player.animation(new Animation(714));
			World.get().submit(new Task(1, false) {
				@Override
				public void execute() {
					if(player.getTeleportStage() == 1) {
						player.graphic(new Graphic(308));
						player.setTeleportStage(2);
					} else if(player.getTeleportStage() == 2) {
						player.setTeleportStage(3);
					} else if(player.getTeleportStage() == 3) {
						player.move(position);
						player.animation(new Animation(715));
						player.setTeleportStage(0);
						this.cancel();
					}
				}
			}.attach(player));
		}
	},
	ANCIENT(12855) {
		@Override
		public void execute(Player player, Position position) {
			player.animation(new Animation(1979));
			World.get().submit(new Task(1, false) {
				@Override
				public void execute() {
					if(player.getTeleportStage() == 1) {
						player.graphic(new Graphic(392));
						player.setTeleportStage(2);
					} else if(player.getTeleportStage() == 2) {
						player.setTeleportStage(3);
					} else if(player.getTeleportStage() == 3) {
						player.setTeleportStage(4);
					} else if(player.getTeleportStage() == 4) {
						player.move(position);
						player.setTeleportStage(0);
						this.cancel();
					}
				}
			}.attach(player));
		}
	},
	LUNAR(29999) {
		@Override
		public void execute(Player player, Position position) {//TODO replace this to correct lunar home teleport
			player.animation(new Animation(714));
			World.get().submit(new Task(1, false) {
				@Override
				public void execute() {
					if(player.getTeleportStage() == 1) {
						player.graphic(new Graphic(308));
						player.setTeleportStage(2);
					} else if(player.getTeleportStage() == 2) {
						player.setTeleportStage(3);
					} else if(player.getTeleportStage() == 3) {
						player.move(position);
						player.animation(new Animation(715));
						player.setTeleportStage(0);
						this.cancel();
					}
				}
			}.attach(player));
		}
	};
	
	/**
	 * The identifier for this spellbook interface.
	 */
	private final int id;
	
	/**
	 * Creates a new {@link Spellbook}.
	 * @param id the identifier for this spellbook interface.
	 */
	Spellbook(int id) {
		this.id = id;
	}
	
	@Override
	public final String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}
	
	/**
	 * The method executed when {@code player} teleports to {@code position}
	 * while converted to this spellbook type.
	 * @param player   the player that is teleporting.
	 * @param position the position the player is teleporting to.
	 */
	public abstract void execute(Player player, Position position);
	
	/**
	 * Gets the identifier for this spellbook interface.
	 * @return the identifier for the interface.
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Attempts to convert the spellbook for {@code player} to {@code book}.
	 * @param player the player to convert the spellbook for.
	 * @param book   the type of spellbook to convert to.
	 */
	public static void convert(Player player, Spellbook book) {
		if(player.getSpellbook() == book) {
			player.message("You have already converted to " + book.toString().toLowerCase() + " magics!");
			return;
		}
		if(player.getAttr().get("lunar_spellbook_swap").getBoolean()) {
			player.getAttr().get("lunar_spellbook_swap").set(false);
			;
			player.message("Your spellbook swap spell has been deactivated...");
		}
		TabInterface.MAGIC.sendInterface(player, book.id);
		player.setSpellbook(book);
	}
}
