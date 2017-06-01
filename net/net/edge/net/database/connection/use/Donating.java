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
			Statement st = con.createStatement();
			String sql = ("SELECT amount FROM donations WHERE username='" + player.getUsername().toLowerCase().replace(" ", "_") + "'  and claimed=0");
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				int amount = rs.getInt("amount");
				if(amount > 0) {
					player.getBank().add(0, new Item(7478, amount));
					player.message("We added " + amount + " edge tokens to your bank, thank you for donating!");
					PreparedStatement stmt1 = con.prepareStatement("UPDATE donations SET claimed = 1 WHERE username=? and amount=? and claimed=0");
					stmt1.setString(1, player.getFormatUsername());
					stmt1.setInt(2, amount);
					stmt1.execute();
					received = true;
				}
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
