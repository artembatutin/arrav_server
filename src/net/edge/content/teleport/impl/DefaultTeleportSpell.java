package net.edge.content.teleport.impl;

import net.edge.content.dialogue.impl.OptionDialogue;
import net.edge.content.minigame.MinigameHandler;
import net.edge.content.skill.construction.Construction;
import net.edge.content.teleport.TeleportSpell;
import net.edge.content.teleport.TeleportType;
import net.edge.task.Task;
import net.edge.util.ActionListener;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.content.skill.magic.Spellbook;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager.ActivityType;
import net.edge.world.locale.Position;
import net.edge.world.locale.loc.Location;

import java.util.Optional;

import static net.edge.content.achievements.Achievement.TELEPORTER;
import static net.edge.content.teleport.TeleportType.OBELISK;

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
		}
		
		DefaultTeleportSpell spell = new DefaultTeleportSpell(destination, type);
		if(!force && !spell.canTeleport(player)) {
			return false;
		}
		
		if(type != OBELISK && Location.inWilderness(destination)) {
			player.getDialogueBuilder().append(new OptionDialogue(t -> {
				if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
					teleport(player, spell);
				}
				player.closeWidget();
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
			Construction.remove(player);
			MinigameHandler.executeVoid(player, m -> m.onTeleportBefore(player, spell.getDestination()));
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
				player.setTeleportStage(-1);
				this.cancel();
			}
		}
		
		@Override
		public void onCancel() {
			TELEPORTER.inc(player);
			player.getActivityManager().enable();
			spell.onDestination().ifPresent(ActionListener::execute);
			spell.attach(Optional.empty());
			player.animation(null);
			player.setTeleportStage(0);
		}
	}
	
}
