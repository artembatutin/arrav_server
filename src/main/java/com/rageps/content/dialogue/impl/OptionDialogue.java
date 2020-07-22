package com.rageps.content.dialogue.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rageps.content.dialogue.Dialogue;
import com.rageps.content.dialogue.DialogueBuilder;
import com.rageps.content.dialogue.DialogueType;
import com.rageps.world.entity.actor.player.Player;

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
				player.text(14445, getText()[0]);
				player.text(14446, getText()[1]);
				player.chatWidget(14443);
				break;
			case 3:
				player.text(2471, getText()[0]);
				player.text(2472, getText()[1]);
				player.text(2473, getText()[2]);
				player.chatWidget(2469);
				break;
			case 4:
				player.text(8209, getText()[0]);
				player.text(8210, getText()[1]);
				player.text(8211, getText()[2]);
				player.text(8212, getText()[3]);
				player.chatWidget(8207);
				break;
			case 5:
				player.text(8221, getText()[0]);
				player.text(8222, getText()[1]);
				player.text(8223, getText()[2]);
				player.text(8224, getText()[3]);
				player.text(8225, getText()[4]);
				player.chatWidget(8219);
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
		FIRST_OPTION(14445, 2473, 32017, 32029),
		SECOND_OPTION(14446, 2472, 32018, 32030),
		THIRD_OPTION(9169, 2471, 32031),
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
		
		/**
		 * Convenient check method.
		 */
		public boolean is(OptionType type) {
			return this == type;
		}
	}
	
	/**
	 * @return {@link #optionListener}.
	 */
	public final Consumer<OptionType> getOptionListener() {
		return optionListener;
	}
}
