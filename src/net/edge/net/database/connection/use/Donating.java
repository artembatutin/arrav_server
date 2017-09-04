package net.edge.net.database.connection.use;

import net.edge.net.database.connection.ConnectionUse;
import net.edge.net.database.pool.ConnectionPool;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.item.Item;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Handles the claim donate process for a specific player.
 */
public class Donating extends ConnectionUse {
	
	/**
	 * Amount of tokens receivable.
	 */
	private static final int[] TOKENS = {500, 1050, 2100, 3150, 4200, 5250, 7350, 10500, 15750, 21000};
	
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
	 * The player attempting to claim.
	 */
	private final Player player;
	
	/**
	 * Constructs a new pool usage.
	 * @param pool the pool being used.
	 */
	public Donating(Player player, ConnectionPool pool) {
		super(pool);
		this.player = player;
	}
	
	@Override
	public void append(Connection con) throws SQLException {
		boolean received = false;
		PreparedStatement st = con.prepareStatement("SELECT * FROM purchase_history WHERE LOWER(username) = ? and claimed = 0");
		st.setString(1, player.credentials.username.toLowerCase());
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			int id = rs.getInt("id");
			int product = rs.getInt("tokens");
			player.getBank().add(0, new Item(7478, TOKENS[product]));
			player.message("We added " + TOKENS[product] + " edge tokens to your bank, thank you for donating!");
			player.totalDonated += TOKENS[product];
			if(!player.getRights().isStaff()) {
				switch(player.getRights()) {
					case PLAYER:
						if(player.getTotalDonated(true) >= 20) {
							player.setRights(Rights.DONATOR);
							player.message("You have donated over 20 dollars, you have received the reg. donator rank.");
						}
						break;
					case DONATOR:
						if(player.getTotalDonated(true) >= 50) {
							player.setRights(Rights.SUPER_DONATOR);
							player.message("You have donated over 50 dollars, you have received the sup. donator rank.");
						}
						break;
					case SUPER_DONATOR:
						if(player.getTotalDonated(true) > 200) {
							player.setRights(Rights.EXTREME_DONATOR);
							player.message("You have donated over 200 dollars, you have received the ext. donator rank.");
						}
						break;
					case EXTREME_DONATOR:
						if(player.getTotalDonated(true) > 1000) {
							player.setRights(Rights.GOLDEN_DONATOR);
							player.message("You have donated over 1000 dollars, you have received the gold. donator rank.");
						}
						break;
				}
			}
			PreparedStatement stmt1 = con.prepareStatement("UPDATE purchase_history SET claimed = 1 WHERE id= ?");
			stmt1.setInt(1, id);
			stmt1.execute();
			received = true;
		}
		if(!received) {
			player.message("We could not find any donation under your name");
		}
	}
	
	@Override
	public void onError() {
		player.message("An error happened when tried to claim.");
	}
}
