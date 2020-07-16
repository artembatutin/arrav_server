package com.rageps.world.entity.actor.player.persist;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.rageps.content.skill.magic.Spellbook;
import com.rageps.content.skill.prayer.PrayerBook;
import com.rageps.net.codec.login.LoginCode;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAppearance;
import com.rageps.world.entity.actor.player.PlayerCredentials;
import com.rageps.world.entity.actor.player.assets.AntifireDetails;
import com.rageps.world.entity.actor.player.persist.impl.PlayerPersistDB;
import com.rageps.world.entity.actor.player.persist.impl.PlayerPersistFile;
import com.rageps.world.entity.actor.player.persist.property.PlayerPersistanceProperty;
import com.rageps.world.locale.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Stream;

import static com.rageps.world.entity.actor.player.persist.property.PersistancePropertyType.*;

/**
 * Handles all interactions with player persistence and can also load details of players who are not online.
 *
 * If a new property is added to a players account you can just add it's handling to the #PROPERTIES array.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public final class PlayerPersistenceManager {

	/**
	 * Logging for this class.
	 */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The method of persistence, will use {@link PlayerPersistDB} if SQL is enabled, otherwise {@link PlayerPersistFile}.
	 */
	private static final PlayerPersistable PERSISTABLE =  World.get().getEnvironment().isSqlEnabled() ? new PlayerPersistDB() : new PlayerPersistFile();

	/**
	 * Attempts to save a player.
	 * @param player The player being saved.
	 */
	public static void save(Player player) {
		//if (player.isBot) {
		//	return;
		//}
		PERSISTABLE.save(player);
	}

	/**
	 * Attempts to load a players save.
	 * @param player The player being loaded.
	 * @return The {@link LoginCode} which will be sent to the player.
	 */
	public static LoginCode load(Player player) {
		//if (player.isBot) {
		//	return LoginCode.COULD_NOT_COMPLETE_LOGIN;
		//}
		return PERSISTABLE.load(player);
	}

	/**
	 * Loads a players account, used for when a player isn't online and needs
	 * modifications.
	 * @param name The name of the player being loaded.
	 * @return The loaded player.
	 */
	public static Player loadPlayer(String name) {
		PlayerCredentials credentials = new PlayerCredentials(name, null);

		final Player other = new Player(credentials);

		try {
			load(other);
		} catch (Exception ex) {
			LOGGER.error("Error using player loader to get another player {}", name);
			ex.printStackTrace();
			return null;
		}
		return other;
	}

	private static final Gson GSON = new Gson();


	/**
	 * Represents all of the properties present in a save file, along with their handling
	 * for loading/saving.
	 */
	public static final PlayerPersistanceProperty[] PROPERTIES = {

			new PlayerPersistanceProperty("position", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					player.setPosition(GSON.fromJson(property, Position.class));
				}

				@Override
				public Object write(Player player) {
					return player.getPosition();
				}
			},

			new PlayerPersistanceProperty("xp_lock", BOOLEAN) {
				@Override
				public void read(Player player, JsonElement property) {
					player.lockedXP = property.getAsBoolean();
				}

				@Override
				public Object write(Player player) {
					return player.lockedXP;
				}
			},

			new PlayerPersistanceProperty("total_donated", INT) {
				@Override
				public void read(Player player, JsonElement property) {
					player.totalDonated = property.getAsInt();
				}

				@Override
				public Object write(Player player) {
					return player.getTotalDonated(false);
				}
			},
			new PlayerPersistanceProperty("total_votes", INT) {
				@Override
				public void read(Player player, JsonElement property) {
					player.totalVotes = property.getAsInt();
				}

				@Override
				public Object write(Player player) {
					return player.totalVotes;
				}
			},
			new PlayerPersistanceProperty("vote_points", INT) {
				@Override
				public void read(Player player, JsonElement property) {
					player.votePoints = property.getAsInt();
				}

				@Override
				public Object write(Player player) {
					return player.votePoints;
				}
			},
			new PlayerPersistanceProperty("clan", STRING) {
				@Override
				public void read(Player player, JsonElement property) {
					player.clan = property.getAsString();
				}

				@Override
				public Object write(Player player) {
					return player.clan;
				}
			},
			new PlayerPersistanceProperty("last_clan", STRING) {
				@Override
				public void read(Player player, JsonElement property) {
					player.lastClan = property.getAsString();
				}

				@Override
				public Object write(Player player) {
					return player.lastClan;
				}
			},
			new PlayerPersistanceProperty("appearance", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					player.setAppearance(GSON.fromJson(property, PlayerAppearance.class));
				}

				@Override
				public Object write(Player player) {
					return player.getAppearance();
				}
			},
			new PlayerPersistanceProperty("prayer_type", ENUM) {
				@Override
				public void read(Player player, JsonElement property) {
					player.setPrayerBook(PrayerBook.valueOf(property.getAsString()));
				}

				@Override
				public Object write(Player player) {
					return player.getPrayerBook();
				}
			},
			new PlayerPersistanceProperty("spell_book", ENUM) {
				@Override
				public void read(Player player, JsonElement property) {
					player.setSpellbook(Spellbook.valueOf(property.getAsString()));
				}

				@Override
				public Object write(Player player) {
					return player.getSpellbook();
				}
			},
			new PlayerPersistanceProperty("last-killer", STRING) {
				@Override
				public void read(Player player, JsonElement property) {
					player.lastKiller = property.getAsString();
				}

				@Override
				public Object write(Player player) {
					return player.lastKiller;
				}
			},
			new PlayerPersistanceProperty("fight_type", ENUM) {
				@Override
				public void read(Player player, JsonElement property) {
					player.getCombat().setFightType(FightType.valueOf(property.getAsString()));
				}

				@Override
				public Object write(Player player) {
					return player.getCombat().getFightType();
				}
			},
			new PlayerPersistanceProperty("poison-damage", INT) {
				@Override
				public void read(Player player, JsonElement property) {
					player.getPoisonDamage().set(property.getAsInt());
				}

				@Override
				public Object write(Player player) {
					return player.getPoisonDamage().get();
				}
			},
			new PlayerPersistanceProperty("running", BOOLEAN) {
				@Override
				public void read(Player player, JsonElement property) {
					player.getMovementQueue().setRunning(property.getAsBoolean());
				}

				@Override
				public Object write(Player player) {
					return player.getMovementQueue().isRunning();
				}
			},
			new PlayerPersistanceProperty("antifire_details", JSON) {//todo test this one
				@Override
				public void read(Player player, JsonElement property) {
					player.playerData.setAntifireDetail(GSON.fromJson(property.getAsString(), AntifireDetails.class));
				}

				@Override
				public Object write(Player player) {
					return player.playerData.getAntifireDetails().orElse(null);
				}
			},
			new PlayerPersistanceProperty("run_energy", INT) {
				@Override
				public void read(Player player, JsonElement property) {
					player.setRunEnergy(property.getAsInt());
				}

				@Override
				public Object write(Player player) {
					return ((int) player.playerData.getRunEnergy());
				}
			},
			new PlayerPersistanceProperty("special_amount", INT) {
				@Override
				public void read(Player player, JsonElement property) {
					player.playerData.getSpecialPercentage().set(property.getAsInt());
				}

				@Override
				public Object write(Player player) {
					return null;
				}
			}



	};

	/**
	 * Cached names of all the properties in a save.
	 */
	public static final String[] PROPERTY_NAMES = Stream.of(PROPERTIES).map(PlayerPersistanceProperty::getName).toArray(String[]::new);
}
