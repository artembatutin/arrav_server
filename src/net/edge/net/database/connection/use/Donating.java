package net.edge.net.database.connection.use;

import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.io.IOException;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the claim donate process for a specific player.
 */
public class Donating {
	
	/**
	 * Amount of tokens receivable.
	 */
	private static final int[] TOKENS = {
			500,
			1050,
			2100,
			3150,
			4200,
			5250,
			7350,
			10500,
			15750,
			21000
	};
	
	/**
	 * The logger to log the donating errors just in-case we will need them later.
	 */
	private static final Logger logger = Logger.getLogger(Donating.class.getName());
	
	static {
		try {
			FileHandler fn = new FileHandler("./data/logs/donate.log");
			logger.addHandler(fn);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructing a donating method.
	 * @param player the player donating.
	 * @param con    the database connection to be parsed with.
	 */
	public Donating(Player player, Connection con) {
		if(con == null)
			player.message("Cannot gather donation information at this moment.");
		else
			append(player, con);
	}
	
	/**
	 * We append the donating process using the Sql Connection.
	 * @param player the player attempting to donate.
	 * @param con    the database connection.
	 */
	public void append(Player player, Connection con) {
		try {
			boolean received = false;
			PreparedStatement st = con.prepareStatement("SELECT * FROM purchase_history WHERE LOWER(username) = ? and claimed = 0 and payment_status = 'Completed'");
			st.setString(1, player.getCredentials().getUsername().toLowerCase());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				int product = rs.getInt("tokens");
				player.getBank().add(0, new Item(7478, TOKENS[product]));
				player.message("We added " + TOKENS[product] + " edge tokens to your bank, thank you for donating!");
				PreparedStatement stmt1 = con.prepareStatement("UPDATE purchase_history SET claimed = 1 WHERE id= ?");
				stmt1.setInt(1, id);
				stmt1.execute();
				received = true;
			}
			if(!received) {
				player.message("We could not find any donation under your name");
			}
		} catch(SQLException e) {
			logger.log(Level.WARNING, e.getMessage());
			player.message("Cannot gather donation information at this moment.");
		}
	}
	
}
