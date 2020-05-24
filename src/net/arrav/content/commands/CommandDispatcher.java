package net.arrav.content.commands;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.arrav.util.LoggerUtils;
import net.arrav.util.Utility;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.container.session.ExchangeSessionManager;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
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
	private static final Object2ObjectArrayMap<CommandSignature, Command> COMMANDS = new Object2ObjectArrayMap<>();
	
	/**
	 * The logger that will print important information.
	 */
	private static Logger logger = LoggerUtils.getLogger(CommandDispatcher.class);
	
	/**
	 * Executes the specified {@code string} if it's a command.
	 * @param player the player executing the command.
	 * @param parts the string which represents a command.
	 */
	public static void execute(Player player, String[] parts, String command) {
		if(ExchangeSessionManager.get().inAnySession(player)) {
			ExchangeSessionManager.get().reset(player);
		}
		
		Optional<Command> cmd = getCommand(parts[0]);
		
		if(!cmd.isPresent()) {
			player.message("Command [::" + parts[0] + "] does not exist!");
			return;
		}
		
		if(!hasPrivileges(player, cmd.get())) {
			player.message("You don't have the privileges required to use this command.");
			return;
		}
		
		try {
			cmd.get().execute(player, parts, command);
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
	private static Optional<Command> getCommand(String identifier) {
		for(Entry<CommandSignature, Command> command : COMMANDS.entrySet()) {
			for(String s : command.getKey().alias()) {
				if(s.equalsIgnoreCase(identifier)) {
					return Optional.of(command.getValue());
				}
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Sends the correct syntax of usage to the player for the
	 * specified {@code command}.
	 * @param player the player to send this syntax for.
	 * @param command the command that was misinterpreted.
	 */
	private static void sendSyntax(Player player, Command command) {
		Annotation annotation = command.getClass().getAnnotation(CommandSignature.class);
		CommandSignature sig = (CommandSignature) annotation;
		player.message("[COMMAND SYNTAX] " + sig.syntax());
	}
	
	/**
	 * Checks if the player has the privileges to execute this command.
	 * @param player the player executing this command.
	 * @param command the command that was executed.
	 * @return <true> if the command was executed, <false> otherwise.
	 */
	private static boolean hasPrivileges(Player player, Command command) {
		Annotation annotation = command.getClass().getAnnotation(CommandSignature.class);
		CommandSignature sig = (CommandSignature) annotation;
		return Arrays.stream(sig.rights()).anyMatch(right -> player.getRights().equals(right));
	}
	
	/**
	 * Loads all the commands into the {@link #COMMANDS} list.
	 * <p></p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		logger.info("Loading commands...");
		Set<Class<?>> clazzSet = new Reflections(CommandDispatcher.class.getPackage().getName()).getTypesAnnotatedWith(CommandSignature.class);
		List<Command> commands = Utility.getClassesInSet(clazzSet).stream().map(clazz -> (Command) clazz).collect(Collectors.toList());


		for(Command command : commands) {
			if(command.getClass().getAnnotation(CommandSignature.class) == null) {
				throw new IncompleteAnnotationException(CommandSignature.class, command.getClass().getName() + " has no annotation.");
			}
			COMMANDS.put(command.getClass().getAnnotation(CommandSignature.class), command);
		}
		logger.info("Successfully loaded " + COMMANDS.size() + " commands.");
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
	
}
