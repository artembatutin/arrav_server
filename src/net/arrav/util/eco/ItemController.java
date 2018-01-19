package net.arrav.util.eco;

import net.arrav.world.entity.item.Item;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class ItemController {

	/**
	 * The report writer.
	 */
	public BufferedWriter out;

	/**
	 * The name of the player file being processed.
	 */
	public String player;

	/**
	 * Flag when we create a new line.
	 */
	private boolean line;

	/**
	 * Starting the buffered writer.
	 */
	public void start() throws IOException {
		if(out == null)
			out = new BufferedWriter(new FileWriter("./data/eco/" + getName() + ".txt"));
		out.write(getDesc());
		out.newLine();
	}

	/**
	 * Sets the report writer.
	 */
	public void player(String player) throws IOException {
		this.player = player;
		newPlayer();
		if(line) {
			out.newLine();
			line = false;
		}
	}

	/**
	 * Flushes the report to the file.
	 */
	void flush() throws IOException {
		if(out != null) {
			out.flush();
			out.close();
		}
	}

	/**
	 * Writes the player start syntax.
	 */
	protected void pOut() throws IOException {
		if(!line && out != null) {
			out.write(player + " : ");
			line = true;
		}
	}

	/**
	 * Gets the writer.
	 */
	public BufferedWriter out() {
		return out;
	}

	/**
	 * Changes the item thrown in.
	 */
	public abstract Item change(Item item) throws IOException;

	/**
	 * When a new player line has been started.
	 */
	public abstract void newPlayer() throws IOException;

	/**
	 * Gets the name of this report file.
	 */
	public abstract String getName();

	/**
	 * Gets the report file start description.
	 */
	public abstract String getDesc();
}
