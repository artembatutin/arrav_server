package net.edge.content.teleport.impl;

import net.edge.task.Task;
import net.edge.util.ActionListener;
import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.teleport.TeleportSpell;
import net.edge.locale.loc.Location;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Spellbook;
import net.edge.world.node.entity.player.assets.activity.ActivityManager.ActivityType;
import net.edge.world.node.item.Item;

import java.util.Optional;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.OBELISK;

/**
 * Holds functionality for default teleport spells.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class DefaultTeleportSpell extends TeleportSpell {
	
	/**
	 * The teleport type of this teleport spell.
	 */
	private final TeleportType type;
	
	/**
	 * Constructs a new {@link DefaultTeleportSpell}.
	 * @param destination {@link #getDestination()}.
	 */
	public DefaultTeleportSpell(Position destination, TeleportType type) {
		super(destination);
		this.type = type;
	}
	
	/**
	 * Attempts to start teleporting to the specified {@code destination} and {@code type}.
	 * @param player      the player whom is teleporting.
	 * @param destination {@link #getDestination()}.
	 * @return <true> if the player can teleport, <false> otherwise.
	 */
	public static boolean startTeleport(Player player, Position destination) {
		TeleportType type = player.getSpellbook().equals(Spellbook.LUNAR) ? TeleportType.LUNAR : player.getSpellbook().equals(Spellbook.ANCIENT) ? TeleportType.ANCIENT : TeleportType.NORMAL;
		return startTeleport(player, destination, type);
	}
	
	/**
	 * Attempts to start teleporting to the specified {@code destination}.
	 * @param player      the player whom is teleporting.
	 * @param destination {@link #getDestination()}.
	 * @param type        {@link #type}.
	 * @return <true> if the player can teleport, <false> otherwise.
	 */
	public static boolean startTeleport(Player player, Position destination, TeleportType type) {
		return startTeleport(player, destination, type, false);
	}
	
	/**
	 * Attempts to start teleporting to the specified {@code destination}.
	 * @param player      the player whom is teleporting.
	 * @param destination {@link #getDestination()}.
	 * @param type        {@link #type}.
	 * @param force       the condition if the teleport is being forced.
	 * @return <true> if the player can teleport, <false> otherwise.
	 */
	public static boolean startTeleport(Player player, Position destination, TeleportType type, boolean force) {
		if(!force && type == OBELISK) {
			if(player.getTeleblockTimer().get() > 0) {
				int time = player.getTeleblockTimer().get() * 600;
				if(time >= 1000 && time <= 60000) {
					player.message("You must wait approximately " + ((time) / 1000) + " seconds in order to teleport!");
					return false;
				} else if(time > 60000) {
					player.message("You must wait approximately " + ((time) / 60000) + " minutes in order to teleport!");
					return false;
				}
			}
			teleport(player, new DefaultTeleportSpell(destination, OBELISK));
			return true;
		}
		
		if(!force) {
			if(player.getActivityManager().contains(ActivityType.TELEPORT)) {
				player.message("You may not teleport!");
				return false;
			}
			if(player.getTeleportStage() > 0) {
				player.message("You are already teleporting.");
				return false;
			}
			if(player.getForcedMovement() != null) {
				if(player.getForcedMovement().isActive()) {
					player.message("You currently can't do this...");
					return false;
				}
			}
			if(player.getTeleportStage() > 0) {
				player.message("You are already teleporting.");
				return false;
			}
			
			if(player.getCombatBuilder().inCombat() && player.getCombatBuilder().getVictim() != null && player.getCombatBuilder().getVictim().isPlayer()) {
				player.message("You can't teleport while being in combat.");
				return false;
			}
		}
		
		DefaultTeleportSpell spell = new DefaultTeleportSpell(destination, type);
		
		if(!force && !spell.canTeleport(player)) {
			return false;
		}
		
		if(type != OBELISK && Location.inWilderness(destination)) {
			player.getDialogueBuilder().append(new OptionDialogue(t -> {
				if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
					teleport(player, spell);
				} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
					return;
				}
				player.getMessages().sendCloseWindows();
			}, "I don't mind going to the @red@Wilderness@bla@.", "I'd rather stay here."));
			return false;
		} else {
			teleport(player, spell);
			return true;
		}
	}
	
	/**
	 * Finalize the teleport.
	 * @param player the player being teleported.
	 * @param spell  the spell being used to teleport.
	 */
	public static void teleport(Player player, DefaultTeleportSpell spell) {
		spell.resetPlayerFlags(player);
		World.get().submit(new DefaultTeleportSpellTask(player, spell));
	}
	
	@Override
	public int levelRequired() {
		return 0;
	}
	
	@Override
	public double baseExperience() {
		return 0;
	}
	
	@Override
	public Optional<Item[]> itemsRequired(Player player) {
		return Optional.empty();
	}
	
	@Override
	public Optional<Item[]> equipmentRequired(Player player) {
		return Optional.empty();
	}
	
	@Override
	public final void startCast(EntityNode cast, EntityNode castOn) {
		// This class holds no support for teleport other.
	}
	
	/**
	 * The task chained to this teleport.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class DefaultTeleportSpellTask extends Task {
		
		/**
		 * The player this task is running for.
		 */
		private final Player player;
		
		/**
		 * The spell this task is running for.
		 */
		private final DefaultTeleportSpell spell;
		
		/**
		 * Constructs a new {@link DefaultTeleportSpellTask}.
		 * @param player {@link #player}.
		 * @param spell  {@link #spell}.
		 */
		DefaultTeleportSpellTask(Player player, DefaultTeleportSpell spell) {
			super(1, false);
			this.player = player;
			this.spell = spell;
		}
		
		/**
		 * The initial delay which is the start delay + end delay.
		 */
		private int initialDelay;
		
		@Override
		public void onSubmit() {
			player.getMovementQueue().reset();
			player.getActivityManager().disable();
			initialDelay = spell.type.getStartDelay() + spell.type.getEndDelay();
			player.setTeleportStage(initialDelay);
			spell.type.getStartAnimation().ifPresent(player::animation);
			spell.type.getStartGraphic().ifPresent(player::graphic);
		}
		
		@Override
		public void execute() {
			player.setTeleportStage(player.getTeleportStage() - 1);
			if(player.getTeleportStage() == initialDelay - spell.type.getStartDelay() - 1) {
				player.move(spell.getDestination());
			}
			if(player.getTeleportStage() == initialDelay - spell.type.getStartDelay() - 3) {
				spell.type.getEndAnimation().ifPresent(player::animation);
				spell.type.getEndGraphic().ifPresent(player::graphic);
			}
			if(player.getTeleportStage() < 1) {
				this.cancel();
				return;
			}
		}
		
		@Override
		public void onCancel() {
			player.getActivityManager().enable();
			spell.onDestination().ifPresent(ActionListener::execute);
			spell.attach(Optional.empty());
			player.animation(null);
			player.setTeleportStage(0);
		}
	}
	
	public enum TeleportType {
		NORMAL(3, 6, Optional.of(new Animation(8939)), Optional.of(new Animation(8941)), Optional.of(new Graphic(1576)), Optional.of(new Graphic(1577))),
		ANCIENT(2, 3, Optional.of(new Animation(9599)), Optional.empty(), Optional.of(new Graphic(1681)), Optional.empty()),
		LUNAR(5, 5, Optional.of(new Animation(9606)), Optional.empty(), Optional.of(new Graphic(1685)), Optional.empty()),
		TABLET(4, 5, Optional.of(new Animation(4731)), Optional.empty(), Optional.of(new Graphic(678)), Optional.empty()),
		LEVER(4, 4, Optional.of(new Animation(714)), Optional.of(new Animation(715)), Optional.of(new Graphic(111, 65535)), Optional.empty()),
		LADDER(1, 2, Optional.of(new Animation(828)), Optional.empty(), Optional.empty(), Optional.empty()),
		DOOR(1, 2, Optional.of(new Animation(9105)), Optional.empty(), Optional.empty(), Optional.empty()),
		//TODO: Fix animation.
		OBELISK(3, 6, Optional.of(new Animation(8939)), Optional.of(new Animation(8941)), Optional.of(new Graphic(661)), Optional.empty()),
		VOID_FAMILIAR(3, 2, Optional.of(new Animation(8136)), Optional.of(new Animation(8137)), Optional.of(new Graphic(1503)), Optional.of(new Graphic(1502))),
		FREEZE(5, 8, Optional.of(new Animation(11044)), Optional.empty(), Optional.of(new Graphic(1973)), Optional.empty()),
		TRAINING_PORTAL(5, 3, Optional.of(new Animation(10100)), Optional.of(new Animation(9013)), Optional.of(new Graphic(606)), Optional.empty()),
		BOSS_PORTAL(4, 3, Optional.of(new Animation(10100)), Optional.of(new Animation(9013)), Optional.of(new Graphic(2128)), Optional.empty()),
		PVP_PORTAL(2, 5, Optional.of(new Animation(9602)), Optional.of(new Animation(9013)), Optional.empty(), Optional.empty());
		
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
}
