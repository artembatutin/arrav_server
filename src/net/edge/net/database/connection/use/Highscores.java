package net.edge.net.database.connection.use;

import net.edge.net.database.connection.ConnectionUse;
import net.edge.net.database.pool.ConnectionPool;
import net.edge.world.node.entity.player.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Handles the highscores updating for a specific player.
 */
public class Highscores extends ConnectionUse {
	
	private final Player player;
	
	public Highscores(ConnectionPool pool, Player player) {
		super(pool);
		this.player = player;
	}
	
	@Override
	public void append(Connection con) throws SQLException {
		PreparedStatement stmt1 = con.prepareStatement("DELETE FROM hs_users WHERE username=?");
		stmt1.setString(1, player.getFormatUsername());
		stmt1.execute();
		
		PreparedStatement stmt2 = con.prepareStatement(generateQuery());
		stmt2.setString(1, player.getFormatUsername());
		stmt2.setInt(2, player.getRights().getProtocolValue());
		stmt2.setInt(3, player.determineCombatLevel());
		long totalExperience = 0;
		for(int i = 0; i < 25; i++) {
			int experience = (int) player.getSkills()[i].getExperience();
			totalExperience += experience;
			stmt2.setInt(5 + i, experience);
		}
		stmt2.setLong(4, totalExperience);
		stmt2.setLong(30, player.getCurrentKillstreak().get());
		stmt2.setLong(31, player.getPlayerKills().get());
		stmt2.setLong(32, player.getDeathsByPlayer().get());
		stmt2.execute();
	}
	
	@Override
	public void onError() {
		System.out.println("Error updating hiscores for " + player.getFormatUsername());
	}
	
	private static String generateQuery() {
		return "INSERT INTO hs_users (username, rights, combat_level, overall_xp, attack_xp, defence_xp, strength_xp, constitution_xp, ranged_xp, prayer_xp, magic_xp, cooking_xp, woodcutting_xp, fletching_xp, fishing_xp, firemaking_xp, crafting_xp, smithing_xp, mining_xp, herblore_xp, agility_xp, thieving_xp, slayer_xp, farming_xp, runecrafting_xp, hunter_xp, construction_xp, summoning_xp, dungeoneering_xp, killstreak, kills, deaths) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}
	
}
