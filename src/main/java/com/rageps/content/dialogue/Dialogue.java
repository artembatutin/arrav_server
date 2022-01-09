package com.rageps.content.dialogue;

import com.rageps.util.ActionListener;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * The class which represents a single dialogue.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class Dialogue implements Consumer<DialogueBuilder> {
	
	/**
	 * The method which holds extra functionality when the dialogue is passed.
	 */
	private Optional<ActionListener> function = Optional.empty();
	
	/**
	 * The method which holds extra functionality when the dialogue is continued.
	 */
	private Optional<ActionListener> secondFunction = Optional.empty();
	
	/**
	 * The array of text in this dialogue.
	 */
	private final String[] text;
	
	/**
	 * Constructs a new {@link Dialogue}.
	 * @param text {@link #text}.
	 */
	public Dialogue(String... text) {
		this.text = text;
	}
	
	/**
	 * The type of dialogue this dialogue is.
	 * @return the {@link DialogueType}.
	 */
	public abstract DialogueType type();
	
	/**
	 * @return the text
	 */
	public String[] getText() {
		return text;
	}
	
	/**
	 * Gets the function for this dialogue.
	 * @return the function.
	 */
	public Optional<ActionListener> getFunction() {
		return function;
	}
	
	/**
	 * Attaches a function to this dialogue.
	 * @param function the function to attach.
	 * @return an instance of this dialogue for chaining.
	 */
	public Dialogue attach(ActionListener function) {
		this.function = Optional.of(function);
		return this;
	}
	
	/**
	 * Gets the function for this dialogue.
	 * @return the function.
	 */
	public Optional<ActionListener> getAfterFunction() {
		return secondFunction;
	}
	
	/**
	 * Attaches a function to this dialogue.
	 * @param function the function to attach.
	 * @return an instance of this dialogue for chaining.
	 */
	public Dialogue attachAfter(ActionListener function) {
		this.secondFunction = Optional.of(function);
		return this;
	}
}
