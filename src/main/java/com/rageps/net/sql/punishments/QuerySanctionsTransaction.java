package com.rageps.net.sql.punishments;

import com.rageps.content.moderation.PunishmentPolicy;
import com.rageps.content.moderation.PunishmentType;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class QuerySanctionsTransaction {

	public ArrayList<PunishmentType> getPunishments(Player player) {
		ArrayList<PunishmentType> punishmentTypes = new ArrayList<>();
		try (Connection connection = TableRepresentation.SANCTIONS.getWrapper().open()) {
			String GET_PUNISHMENT = "SELECT expireDate, punishmentType, punishmentPolicy, user_name FROM punishment WHERE user_name=? OR macAddress=? OR hostAddress=? OR serialNumber =?  ";
			try (NamedPreparedStatement statement = NamedPreparedStatement.create(connection, GET_PUNISHMENT)) {
			statement.setString(1, player.credentials.username);
			statement.setString(2, player.getSession().getMacAddress());
			statement.setString(3, player.getSession().getHost());
			statement.setString(4, player.getSession().getUid());

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				long time = resultSet.getLong("expireDate");
				PunishmentType type = PunishmentType.valueOf(resultSet.getString("punishmentType"));
				String punished_name = resultSet.getString("user_name");
				boolean host = PunishmentPolicy.valueOf(resultSet.getString("punishmentPolicy")) == PunishmentPolicy.HOST;
				boolean expired = time > 0 && time < System.currentTimeMillis();
				boolean valid = !expired && (host && !player.credentials.username.equals(punished_name) || punished_name.equals(player.credentials.username));
				if (valid) {
					punishmentTypes.add(type);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return punishmentTypes;
	}
}
