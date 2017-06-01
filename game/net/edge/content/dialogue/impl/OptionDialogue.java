package net.edge.content.dialogue.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.content.dialogue.Dialogue;
import net.edge.content.dialogue.DialogueBuilder;
import net.edge.content.dialogue.DialogueType;
import net.edge.world.node.entity.player.Player;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The dialogue chain entry that sends the player an option dialogue.
 * @author lare96 <http://github.com/lare96>
 */
public final class OptionDialogue extends Dialogue {
	
	/**
	 * Retrieves the option listener for this option dialogue entry.
	 */
	private final Consumer<OptionType> optionListener;
	
	/**
	 * Creates a new {@link OptionDialogue}.
	 * @param options the different options that will be displayed.
	 */
	public OptionDialogue(Consumer<OptionType> optionListener, String... options) {
		super(options);
		this.optionListener = optionListener;
	}
	
	@Override
	public void accept(DialogueBuilder dialogue) {
		send(dialogue.getPlayer());
		dialogue.appendOptionListener(getOptionListener());
	}
	
	@Override
	public DialogueType type() {
		return DialogueType.OPTION_DIALOGUE;
	}
	
	public void send(Player player) {
		switch(getText().length) {
			case 2:
				player.getMessages().sendString(getText()[0], 14445);
				player.getMessages().sendString(getText()[1], 14446);
				player.getMessages().sendChatInterface(14443);
				break;
			case 3:
				player.getMessages().sendString(getText()[0], 2471);
				player.getMessages().sendString(getText()[1], 2472);
				player.getMessages().sendString(getText()[2], 2473);
				player.getMessages().sendChatInterface(2469);
				break;
			case 4:
				player.getMessages().sendString(getText()[0], 8209);
				player.getMessages().sendString(getText()[1], 8210);
				player.getMessages().sendString(getText()[2], 8211);
				player.getMessages().sendString(getText()[3], 8212);
				player.getMessages().sendChatInterface(8207);
				break;
			case 5:
				player.getMessages().sendString(getText()[0], 8221);
				player.getMessages().sendString(getText()[1], 8222);
				player.getMessages().sendString(getText()[2], 8223);
				player.getMessages().sendString(getText()[3], 8224);
				player.getMessages().sendString(getText()[4], 8225);
				player.getMessages().sendChatInterface(8219);
				break;
			default:
				throw new IllegalArgumentException("Illegal dialogue option length: " + getText().length);
		}
	}
	
	/**
	 * The enumerated type whose elements represent the option types for option
	 * dialogue listeners.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum OptionType {
		FIRST_OPTION(56109, 9167, 32017, 32029),
		SECOND_OPTION(56110, 9168, 32018, 32030),
		THIRD_OPTION(9169, 32019, 32031),
		FOURTH_OPTION(32020, 32032),
		FIFTH_OPTION(32033);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<OptionType> VALUES = Sets.immutableEnumSet(EnumSet.allOf(OptionType.class));
		
		/**
		 * The buttons which identify this option.
		 */
		private final int[] buttons;
		
		/**
		 * Constructs a new {@link OptionType}.
		 * @param buttons {@link #buttons}.
		 */
		OptionType(int... buttons) {
			this.buttons = buttons;
		}
		
		public static Optional<OptionType> getOptions(int buttonId) {
			for(OptionType type : VALUES) {
				for(int button : type.buttons) {
					if(buttonId == button) {
						return Optional.of(type);
					}
				}
			}
			return Optional.empty();
		}
	}
	
	/**
	 * @return {@link #optionListener}.
	 */
	public final Consumer<OptionType> getOptionListener() {
		return optionListener;
	}
}
