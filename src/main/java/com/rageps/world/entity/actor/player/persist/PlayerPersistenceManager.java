package com.rageps.world.entity.actor.player.persist;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.rageps.combat.listener.CombatListenerDispatcher;
import com.rageps.content.item.pets.Pet;
import com.rageps.content.item.pets.PetManager;
import com.rageps.content.item.pets.PetProgress;
import com.rageps.content.minigame.barrows.BarrowsData;
import com.rageps.content.skill.Skill;
import com.rageps.content.skill.construction.room.Room;
import com.rageps.content.skill.summoning.SummoningData;
import com.rageps.content.skill.summoning.familiar.Familiar;
import com.rageps.content.skill.summoning.familiar.FamiliarAbility;
import com.rageps.content.skill.summoning.familiar.FamiliarContainer;
import com.rageps.net.codec.login.LoginCode;
import com.rageps.util.json.GsonUtils;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAppearance;
import com.rageps.world.entity.actor.player.PlayerCredentials;
import com.rageps.world.entity.actor.player.assets.PlayerData;
import com.rageps.world.entity.actor.player.persist.impl.PlayerPersistDB;
import com.rageps.world.entity.actor.player.persist.impl.PlayerPersistFile;
import com.rageps.world.entity.actor.player.persist.property.PlayerPersistanceProperty;
import com.rageps.world.entity.item.Item;
import com.rageps.world.locale.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
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
	private final Logger LOGGER = LogManager.getLogger();

	/**
	 * The method of persistence, will use {@link PlayerPersistDB} if SQL is enabled, otherwise {@link PlayerPersistFile}.
	 */
		public PlayerPersistable persistanceType() {
			return World.get().getEnvironment().isSqlEnabled() ? new PlayerPersistDB() : new PlayerPersistFile();
		}




	/**
	 * Attempts to save a player.
	 * @param player The player being saved.
	 */
	public void save(Player player) {
		//if (player.isBot) {
		//	return;
		//}
		persistanceType().save(player);
	}

	public PlayerLoaderResponse load(Player player) {
		return load(player.credentials);
	}

	/**
	 * Attempts to load a players save.
	 * @param player The player being loaded.
	 * @return The {@link LoginCode} which will be sent to the player.
	 */
	public PlayerLoaderResponse load(PlayerCredentials player) {
		//if (player.isBot) {
		//	return LoginCode.COULD_NOT_COMPLETE_LOGIN;
		//}
		return persistanceType().load(player);
	}

	/**
	 * Loads a players account, used for when a player isn't online and needs
	 * modifications.
	 * @param name The name of the player being loaded.
	 * @return The loaded player.
	 */
	public Player loadPlayer(String name) {
		PlayerCredentials credentials = new PlayerCredentials(name, null);
			Optional<Player> other;
		try {
			other = load(credentials).getPlayer();
		} catch (Exception ex) {
			LOGGER.error("Error using player loader to get another player {}", name);
			ex.printStackTrace();
			return null;
		}
		return other.orElse(null);
	}

	private static final Gson GSON = GsonUtils.JSON_ALLOW_NULL;

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
			new PlayerPersistanceProperty("skills", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					player.setSkills(GSON.fromJson(property.getAsString(), Skill[].class));
				}

				@Override
				public Object write(Player player) {
					return GSON.toJson(player.getSkills());
				}
			},
			new PlayerPersistanceProperty("player_data", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					player.playerData = GSON.fromJson(property.getAsString(), PlayerData.class);
				}

				@Override
				public Object write(Player player) {
					return GSON.toJson(player.playerData);
				}
			},
			new PlayerPersistanceProperty("inventory", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					player.getInventory().fillItems(GSON.fromJson(property.getAsString(), Item[].class));
				}

				@Override
				public Object write(Player player) {
					return GSON.toJson(player.getInventory().getItems());
				}
			},
			new PlayerPersistanceProperty("equipment", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					player.getEquipment().fillItems(GSON.fromJson(property.getAsString(), Item[].class));
					for(Item item : player.getEquipment().getItems()) {
						if(item != null) {
							CombatListenerDispatcher.CombatListenerSet listenerSet = CombatListenerDispatcher.ITEM_LISTENERS.get(item.getId());
							if(listenerSet != null && player.getEquipment().containsAll(listenerSet.set)) {
								player.getCombat().addListener(listenerSet.listener);
							}
						}
					}
				}

				@Override
				public Object write(Player player) {
					return GSON.toJson(player.getEquipment().getItems());
				}
			},
			new PlayerPersistanceProperty("auto_retaliate", BOOLEAN) {
				@Override
				public void read(Player player, JsonElement property) {
					player.setAutoRetaliate(property.getAsBoolean());
				}

				@Override
				public Object write(Player player) {
					return player.isAutoRetaliate();
				}
			},
			new PlayerPersistanceProperty("friends", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					long[] list = GSON.fromJson(property.getAsString(), long[].class);
					Arrays.stream(list).forEach(player.relations.getFriendList()::add);
				}

				@Override
				public Object write(Player player) {
					return GSON.toJson(player.relations.getFriendList());
				}
			},
			new PlayerPersistanceProperty("ignores", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					long[] list = GSON.fromJson(property.getAsString(), long[].class);
					Arrays.stream(list).forEach(player.relations.getIgnoreList()::add);
				}

				@Override
				public Object write(Player player) {
					return GSON.toJson(player.relations.getIgnoreList());
				}
			},
			new PlayerPersistanceProperty("barrows_kills", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
				EnumSet<BarrowsData> barrows = player.getMinigameContainer().getBarrowsContainer().getKilledBrothers();
				Collections.addAll(barrows, GSON.fromJson(property.getAsString(), BarrowsData[].class));
				}

				@Override
				public Object write(Player player) {
				EnumSet<BarrowsData> barrows = player.getMinigameContainer().getBarrowsContainer().getKilledBrothers();
					return GSON.toJson(barrows.toArray());
				}
			},
			new PlayerPersistanceProperty("pets", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					Collections.addAll(player.getPetManager().getProgress(), GSON.fromJson(property.getAsString(), PetProgress[].class));
				}

				@Override
				public Object write(Player player) {
					return GSON.toJson(player.getPetManager().getProgress().toArray());
				}
			},
			new PlayerPersistanceProperty("pet", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
 					PetProgress progress = GSON.fromJson(property.getAsString(), PetProgress.class);
 					if(progress != null) {
						PetManager pets = player.getPetManager();
						pets.put(new Pet(progress, new Position(0, 0)));
					}
				}

				@Override
				public Object write(Player player) {
					PetManager pets = player.getPetManager();
					return GSON.toJson(pets.getPet().isPresent() ? pets.getPet().get().getProgress() : null);
				}
			},
			new PlayerPersistanceProperty("familiar", STRING) {
				@Override
				public void read(Player player, JsonElement property) {
					String fam = property.getAsString();
					if(fam == null || fam.equals("null")) {
						player.setFamiliar(Optional.empty());
					} else {
						SummoningData data = SummoningData.valueOf(fam.toUpperCase());
						Familiar familiar = data.create();
						player.setFamiliar(Optional.of(familiar));
					}
				}

				@Override
				public Object write(Player player) {
				Familiar familiar = player.getFamiliar().orElse(null);
				return GSON.toJson(familiar != null ? familiar.getData() : null);
				}
			},
			new PlayerPersistanceProperty("familiar_life", INT) {
				@Override
				public void read(Player player, JsonElement property) {
					player.getFamiliar().ifPresent(ffs -> ffs.setCurrentHealth(property.getAsInt()));
				}

				@Override
				public Object write(Player player) {
					Familiar familiar = player.getFamiliar().orElse(null);
					return familiar != null ? familiar.getCurrentHealth() : 0;
				}
			},
			new PlayerPersistanceProperty("familiar_duration", INT) {
				@Override
				public void read(Player player, JsonElement property) {
					player.getFamiliar().ifPresent(ffs -> ffs.setDuration(property.getAsInt()));
				}

				@Override
				public Object write(Player player) {
					Familiar familiar = player.getFamiliar().orElse(null);
					return familiar != null ? familiar.getDuration() : 0;
				}
			},
			new PlayerPersistanceProperty("bob", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					player.getFamiliar().ifPresent(ffs -> {
						FamiliarAbility ability = ffs.getAbilityType();
						if(ability.getType() == FamiliarAbility.FamiliarAbilityType.BEAST_OF_BURDEN) {
							((FamiliarContainer) ability).getContainer().fillItems((GSON.fromJson(property.getAsString(), Item[].class)));
						}
					});
				}

				@Override
				public Object write(Player player) {
					Familiar familiar = player.getFamiliar().orElse(null);
					return GSON.toJson(familiar != null && familiar.getAbilityType().getType() == FamiliarAbility.FamiliarAbilityType.BEAST_OF_BURDEN ? ((FamiliarContainer) familiar.getAbilityType()).getContainer().getItems() : null);
				}
			},
			new PlayerPersistanceProperty("house_rooms", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					player.getHouse().get().setRooms(GSON.fromJson(property.getAsString(), Room[][][].class));
				}

				@Override
				public Object write(Player player) {
					return GSON.toJson(player.getHouse().get().getRooms());
				}
			},
			new PlayerPersistanceProperty("patches", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					//player.patches = GSON.fromJson(property.getAsString(), Tuple[].class);
				//if(patches != null) {
				//	patches.forEach((k, v) -> player.patches.put(PatchType.valueOf(k), v.as(Patch.class)));
				//}
				}

				@Override
				public Object write(Player player) {
					return "null";//GSON.toJson(player.patches,Tuple[].class);
				}
			},
		/*	new PlayerPersistanceProperty("attributes", JSON) {
				@Override
				public void read(Player player, JsonElement property) {
					JsonArray array = property.getAsJsonArray();
					for (int i = 0; i < array.size(); i++) {
						JsonObject object = array.get(i).getAsJsonObject();

						String name = object.get("name").getAsString();
						AttributeType type = AttributeType.valueOf(object.get("type").getAsInt());
						JsonElement value = object.get("value");

						Attribute<?> attribute;
						switch (type) {
							case BOOLEAN:
								attribute = new BooleanAttribute(value.getAsBoolean());
								break;
							case DOUBLE:
								attribute = new NumericalAttribute(value.getAsDouble());
								break;
							case LONG:
								attribute = new NumericalAttribute(value.getAsLong());
								break;
							case STRING:
								attribute = new StringAttribute(value.getAsString());
								break;
							case OBJECT:
								throw new UnsupportedOperationException("Cannot do dis");
							default:
								throw new IllegalArgumentException("Undefined attribute type: " + type + ".");
						}

						player.getAttributeMap().set(Attributes.getAttributeKey(name), attribute);
					}
				}

				@Override
				public Object write(Player player) {
					Set<Map.Entry<AttributeKey, Attribute<?>>> attributes = new HashSet<>(player.getAttributeMap().getAttributes().entrySet());
					attributes.removeIf(e -> Attributes.getDefinition(e.getKey()).getPersistence() != AttributePersistence.PERSISTENT);
					JsonArray attrs = new JsonArray();

					for (Map.Entry<AttributeKey, Attribute<?>> entry : attributes) {
						JsonObject attr = new JsonObject();
						attr.addProperty("name", entry.getKey().getName());

						Attribute<?> attribute = entry.getValue();
						attr.addProperty("type", attribute.getType().name());
						attr.add("value", GSON.toJsonTree(attribute.getValue()));

						attrs.add(attr);
					}
					return attrs;
				}
			}
*/



	};

	/**
	 * Cached names of all the properties in a save.
	 */
	public static final String[] PROPERTY_NAMES = Stream.of(PROPERTIES).map(PlayerPersistanceProperty::getName).toArray(String[]::new);
}
