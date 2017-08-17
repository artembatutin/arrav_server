package net.edge.content.commands;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.edge.util.LoggerUtils;
import net.edge.util.Utility;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.item.container.session.ExchangeSessionManager;

import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The manager class of commands which will dispatch executable commands.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class CommandDispatcher {
	
	/**
	 * The object map which contains all the commands on the world.
	 */
	private static final Object2ObjectArrayMap<String, CommandSet> COMMANDS = new Object2ObjectArrayMap<>();
	
	/**
	 * The logger that will print important information.
	 */
	private static Logger logger = LoggerUtils.getLogger(CommandDispatcher.class);
	
	/**
	 * Executes the specified {@code string} if it's a command.
	 * @param player the player executing the command.
	 * @param parts  the string which represents a command.
	 */
	public static void execute(Player player, String[] parts, String command) {
		if(ExchangeSessionManager.get().inAnySession(player)) {
			ExchangeSessionManager.get().reset(player);
		}
		
		Optional<CommandSet> cmd = getCommand(parts[0]);
		
		if(!cmd.isPresent()) {
			player.message("Command [::" + parts[0] + "] does not exist!");
			return;
		}
		
		if(!hasPrivileges(player, cmd.get())) {
			player.message("You don't have the privileges required to use this command.");
			return;
		}
		
		try {
			cmd.get().command.execute(player, parts, command);
		} catch(Exception e) {
			e.printStackTrace();
			sendSyntax(player, cmd.get());
		}
	}
	
	/**
	 * Gets a command which matches the {@code identifier}.
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<CommandSet> getCommand(String identifier) {
		return Optional.ofNullable(COMMANDS.get(identifier));
	}
	
	/**
	 * Sends the correct syntax of usage to the player for the
	 * specified {@code command}.
	 * @param player  the player to send this syntax for.
	 * @param command the command that was misinterpreted.
	 */
	private static void sendSyntax(Player player, CommandSet command) {
		player.message("[COMMAND SYNTAX] " + command.syntax);
	}
	
	/**
	 * Checks if the player has the privileges to execute this command.
	 * @param player  the player executing this command.
	 * @param command the command that was executed.
	 * @return <true> if the command was executed, <false> otherwise.
	 */
	private static boolean hasPrivileges(Player player, CommandSet command) {
		return Arrays.stream(command.required).anyMatch(right -> player.getRights().equals(right));
	}
	
	/**
	 * Loads all the commands into the {@link #COMMANDS} list.
	 * <p></p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		logger.info("Loading commands...");
		int count = 0;
		
		for(String directory : Utility.getSubDirectories(CommandDispatcher.class)) {
			List<Command> commands = Utility.getClassesInDirectory(CommandDispatcher.class.getPackage().getName() + "." + directory).stream().map(clazz -> (Command) clazz).collect(Collectors.toList());
			
			for(Command command : commands) {
				CommandSignature sig = command.getClass().getAnnotation(CommandSignature.class);
				if(sig == null) {
					throw new IncompleteAnnotationException(CommandSignature.class, command.getClass().getName() + " has no annotation.");
				}
				Arrays.stream(sig.alias()).forEach(s -> COMMANDS.put(s, new CommandSet(s, command, sig.rights(), sig.syntax())));
				count += 1;
			}
		}
		logger.info("Successfully loaded " + count + " commands.");
	}
	
	/**
	 * Reloads all the commands into the {@link #COMMANDS} list.
	 * <p></p>
	 * <b>This method can be invoked on run-time to clear all the commands in the list
	 * and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		COMMANDS.clear();
		load();
	}

	public static final class CommandSet {

		public final String alias;

		public final Command command;

		public final Rights[] required;

		public final String syntax;

		CommandSet(String alias, Command command, Rights[] required, String syntax) {
			this.alias = alias;
			this.command = command;
			this.required = required;
			this.syntax = syntax;
		}
	}
}
