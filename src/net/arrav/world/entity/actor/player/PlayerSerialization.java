package net.arrav.world.entity.actor.player;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.arrav.content.PlayerPanel;
import net.arrav.content.achievements.Achievement;
import net.arrav.content.clanchat.ClanManager;
import net.arrav.content.item.pets.Pet;
import net.arrav.content.item.pets.PetManager;
import net.arrav.content.item.pets.PetProgress;
import net.arrav.content.minigame.barrows.BarrowsData;
import net.arrav.content.skill.Skill;
import net.arrav.content.skill.construction.House;
import net.arrav.content.skill.construction.room.Room;
import net.arrav.content.skill.farming.patch.Patch;
import net.arrav.content.skill.farming.patch.PatchType;
import net.arrav.content.skill.magic.Spellbook;
import net.arrav.content.skill.prayer.PrayerBook;
import net.arrav.content.skill.slayer.Slayer;
import net.arrav.content.skill.summoning.SummoningData;
import net.arrav.content.skill.summoning.familiar.Familiar;
import net.arrav.content.skill.summoning.familiar.FamiliarAbility;
import net.arrav.content.skill.summoning.familiar.FamiliarContainer;
import net.arrav.net.codec.login.LoginCode;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.world.entity.actor.attribute.AttributeKey;
import net.arrav.world.entity.actor.attribute.AttributeValue;
import net.arrav.world.entity.actor.combat.attack.FightType;
import net.arrav.world.entity.actor.combat.attack.listener.CombatListenerDispatcher;
import net.arrav.world.entity.actor.combat.attack.listener.other.VengeanceListener;
import net.arrav.world.entity.actor.player.assets.AntifireDetails;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.item.Item;
import net.arrav.world.locale.Position;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

import static net.arrav.content.skill.summoning.familiar.FamiliarAbility.FamiliarAbilityType.BEAST_OF_BURDEN;
import static net.arrav.net.codec.login.LoginCode.*;

/**
 * The serializer that will serialize and deserialize character files for
 * players.
 * <p>
 * <p>
 * Serialization of character files can and should be done on another thread
 * whenever possible to avoid doing disk I/O on the main game thread.
 * @author lare96 <http://github.com/lare96>
 */
public final class PlayerSerialization {
	
	/**
	 * Ensures a minimal character save size.
	 */
	private static final int ENSURE_SIZE = 40960;
	
	/**
	 * The player this serializer is dedicated to.
	 */
	private final Player player;
	
	/**
	 * The character file that corresponds to this player.
	 */
	private final File cf;
	
	/**
	 * Creates a new {@link PlayerSerialization}.
	 * @param player the player this serializer is dedicated to.
	 */
	public PlayerSerialization(Player player) {
		this.player = player;
		this.cf = Paths.get("./data/players/" + player.credentials.username + ".json").toFile();
	}
	
	/**
	 * Serializes the dedicated player into a {@code JSON} file.
	 */
	public void serialize() {
		try {
			cf.getParentFile().setWritable(true);
			if(!cf.getParentFile().exists()) {
				try {
					cf.getParentFile().mkdirs();
				} catch(SecurityException e) {
					throw new IllegalStateException("Unable to register directory for character files!");
				}
			}
			try(FileOutputStream out = new FileOutputStream(cf)) {
				Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
				JsonStream stream = new JsonStream(out, ENSURE_SIZE);
				stream.writeObjectStart();
				for(Token token : TOKENS) {
					stream.writeObjectField(token.getName());
					token.toJson(player, stream);
					stream.writeRaw(",");
				}
				//write achievements
				stream.writeObjectField("achievements");
				stream.writeRaw(gson.toJson(player.achievements, Map.class));
				stream.writeRaw(",");
				//write farming patches
				stream.writeObjectField("patches");
				stream.writeRaw(gson.toJson(player.patches, Map.class));
				stream.writeRaw(",");
				//write quest
				stream.writeObjectField("quests");
				stream.writeRaw(gson.toJson(player.getQuestManager().getStartedQuests(), Map.class));
				stream.writeRaw(",");
				//write attributes
				Object2ObjectArrayMap<String, Object> attributes = new Object2ObjectArrayMap<>();
				for(Map.Entry<String, AttributeValue<?>> it : player.getAttr()) {
					AttributeKey<?> key = AttributeKey.ALIASES.get(it.getKey());
					AttributeValue<?> value = it.getValue();
					
					if(key.isPersistent()) {
						Object2ObjectLinkedOpenHashMap<String, Object> attributeEntry = new Object2ObjectLinkedOpenHashMap<>();
						attributeEntry.put("type", key.getTypeName());
						attributeEntry.put("value", value.get());
						attributes.put(key.getName(), attributeEntry);
					}
				}
				stream.writeObjectField("attributes");
				stream.writeRaw(gson.toJson(attributes));
				
				stream.writeObjectEnd();
				stream.flush();
				stream.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes the decoder and gives out a {@link LoginCode}.
	 * @param password the password used to connect.
	 * @return login response with the encoded json object.
	 */
	public LoginCode loginCheck(String password) {
		try {
			if(!cf.exists()) {
				player.getSession().initGame();
				return NORMAL;
			}
			cf.setReadable(true);
			
			try(FileInputStream in = new FileInputStream(cf)) {
				int size = (int) cf.length();
				byte[] buf = new byte[size];
				int readed = in.read(buf);
				if(readed != size) {
					return COULD_NOT_COMPLETE_LOGIN;
				}
				Any data = JsonIterator.deserialize(buf);
				//password check.
				String pass = data.get("password").toString();
				if(!password.equals(pass) && password.length() > 0) {
					return INVALID_CREDENTIALS;
				}
				//ban check.
				player.banned = data.get("banned").toBoolean();
				if(player.banned) {
					return ACCOUNT_DISABLED;
				}
				player.getSession().initGame();
				for(Token token : TOKENS) {
					Any tokenData = data.get(token.getName());
					if(tokenData != null) {
						token.fromJson(player, tokenData);
					}
				}
				Any attributes = data.get("attributes");
				if(attributes != null) {
					attributes.asMap().forEach((k, v) -> {
						try {
							String old = v.get("type").toString();
							Class<?> type;
							type = Class.forName(old);
							Object obj = v.get("value").as(type);
							if(AttributeKey.ALIASES.keySet().stream().anyMatch(s -> s.equals(k)))
								player.getAttr().get(k).set(obj);
						} catch(ClassNotFoundException e) {
							e.printStackTrace();
						}
					});
				}
				Any achievements = data.get("achievements");
				if(achievements != null) {
					achievements.asMap().forEach((k, v) -> player.achievements.put(Achievement.valueOf(k), v.toInt()));
				}
				Any patches = data.get("patches");
				if(patches != null) {
					patches.asMap().forEach((k, v) -> player.patches.put(PatchType.valueOf(k), v.as(Patch.class)));
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			return COULD_NOT_COMPLETE_LOGIN;
		}
		return NORMAL;
	}
	
	public static Token bank(int index) {
		return new Token("bank" + index) {
			@Override
			public void toJson(Player p, JsonStream s) throws IOException {
				s.writeVal(p.getBank().items(index));
			}
			
			@Override
			public void fromJson(Player p, Any n) {
				p.getBank().setItems(index, n.as(Item[].class));
			}
		};
	}
	
	private static final Token[] TOKENS = {new Token("username") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.credentials.username);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.credentials.setUsername(n.toString());
		}
	}, new Token("password") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.credentials.password);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.credentials.password = n.toString();
		}
	}, new Token("banned") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.banned);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.banned = n.toBoolean();
		}
	}, new Token("muted") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.muted);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.muted = n.toBoolean();
			if(HostManager.contains(p.credentials.username, HostListType.MUTED_IP)) {
				p.ipMuted = true;
			}
		}
	}, new Token("position") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getPosition());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.setPosition(n.as(Position.class));
		}
	}, new Token("rights") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getRights().toString());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.setRights(Rights.valueOf(n.toString().toUpperCase()));
		}
	}, new Token("xp-lock") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.lockedXP);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.lockedXP = n.toBoolean();
		}
	}, new Token("ironman") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getIronMan());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.setIron(n.toInt(), true);
		}
	}, new Token("totalDonated") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getTotalDonated(false));
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.totalDonated += n.toInt();
		}
	}, new Token("totalVotes") {
		
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.totalVotes);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.totalVotes += n.toInt();
			PlayerPanel.TOTAL_VOTES.refresh(p, "@or2@ - Total votes: @yel@" + p.totalVotes);
		}
	}, new Token("vote") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.votePoints);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.votePoints += n.toInt();
		}
	}, new Token("clan") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getClan().map(clanMember -> clanMember.getClan().getOwner()).orElse(""));
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			String clan = n.toString();
			if(clan.length() > 0)
				ClanManager.get().join(p, n.toString());
		}
	}, new Token("appearance") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getAppearance().getValues());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getAppearance().setValues(n.as(int[].class));
		}
	}, new Token("prayer-type") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getPrayerBook().toString());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.setPrayerBook(PrayerBook.valueOf(n.toString().toUpperCase()));
		}
	}, new Token("spell-book") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getSpellbook().toString());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.setSpellbook(Spellbook.valueOf(n.toString().toUpperCase()));
		}
	}, new Token("last-killer") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.lastKiller);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.lastKiller = n.toString();
		}
	}, new Token("fight-type") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getCombat().getFightType().toString());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getCombat().setFightType(FightType.valueOf(n.toString()));
		}
	}, new Token("poison-damage") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getPoisonDamage().get());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getPoisonDamage().set(n.toInt());
		}
	}, new Token("auto-retaliate") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.isAutoRetaliate());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.setAutoRetaliate(n.toBoolean());
		}
	}, new Token("running") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getMovementQueue().isRunning());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getMovementQueue().setRunning(n.toBoolean());
		}
	}, new Token("antifire-details") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getAntifireDetails().orElse(null));
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.setAntifireDetail(n.as(AntifireDetails.class));
		}
	}, new Token("run-energy") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getRunEnergy());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.setRunEnergy(n.toDouble());
		}
	}, new Token("special-amount") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getSpecialPercentage().get());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getSpecialPercentage().set(n.toInt());
		}
	}, new Token("teleblock-timer") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getTeleblockTimer().get());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getTeleblockTimer().set(n.toInt());
		}
	}, new Token("skull-timer") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getSkullTimer().get());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getSkullTimer().set(n.toInt());
		}
	}, new Token("vengeance") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.venged);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.venged = n.toBoolean();
			if(p.venged) {
				p.getCombat().addListener(VengeanceListener.get());
			}
		}
	}, new Token("ringOfRecoil") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.ringOfRecoil);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.ringOfRecoil = n.toInt();
		}
	}, new Token("skills") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getSkills());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			Skill[] skills = p.getSkills();
			System.arraycopy(n.as(Skill[].class), 0, skills, 0, skills.length);
		}
	}, new Token("inventory") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getInventory().getItems());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getInventory().fillItems(n.as(Item[].class));
		}
	}, new Token("equipment") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getEquipment().getItems());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getEquipment().fillItems(n.as(Item[].class));
			for(Item item : p.getEquipment().getItems()) {
				if(item != null) {
					CombatListenerDispatcher.CombatListenerSet listenerSet = CombatListenerDispatcher.ITEM_LISTENERS.get(item.getId());
					
					if(listenerSet != null && p.getEquipment().containsAll(listenerSet.set)) {
						p.getCombat().addListener(listenerSet.listener);
					}
				}
			}
		}
	}, bank(0), bank(1), bank(2), bank(3), bank(4), bank(5), bank(6), bank(7), bank(8), new Token("friends") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getFriends().toArray());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			Collections.addAll(p.getFriends(), n.as(Long[].class));
		}
	}, new Token("ignores") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getIgnores().toArray());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			Collections.addAll(p.getIgnores(), n.as(Long[].class));
		}
	}, new Token("total-player-kills") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getPlayerKills().get());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getPlayerKills().set(n.toInt());
		}
	}, new Token("total-player-deaths") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getDeathsByPlayer().get());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getDeathsByPlayer().set(n.toInt());
		}
	}, new Token("total-npc-kills") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getNpcKills().get());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getNpcKills().set(n.toInt());
		}
	}, new Token("total-npc-deaths") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getDeathsByNpc().get());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getDeathsByNpc().set(n.toInt());
		}
	}, new Token("total-player-kills") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getPlayerKills().get());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getPlayerKills().set(n.toInt());
		}
	}, new Token("current-killstreak") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getCurrentKillstreak().get());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getCurrentKillstreak().set(n.toInt());
		}
	}, new Token("highest-killstreak") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getHighestKillstreak().get());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getHighestKillstreak().set(n.toInt());
		}
	}, new Token("godwars-killcount") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getGodwarsKillcount());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.setGodwarsKillcount(n.as(int[].class));
		}
	}, new Token("killed-brothers") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			EnumSet<BarrowsData> barrows = p.getMinigameContainer().getBarrowsContainer().getKilledBrothers();
			s.writeVal(barrows.toArray());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			EnumSet<BarrowsData> barrows = p.getMinigameContainer().getBarrowsContainer().getKilledBrothers();
			Collections.addAll(barrows, n.as(BarrowsData[].class));
		}
	}, new Token("slayerPoints") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getSlayerPoints());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.updateSlayers(n.toInt());
		}
	}, new Token("slayer") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getSlayer().orElse(null));
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			Slayer s = n.as(Slayer.class);
			p.setSlayer(s == null ? Optional.empty() : Optional.of(s));
		}
	}, new Token("blocked-tasks") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getBlockedTasks());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.setBlockedTasks(n.as(String[].class));
		}
	}, new Token("pets") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			s.writeVal(p.getPetManager().getProgress().toArray());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			Collections.addAll(p.getPetManager().getProgress(), n.as(PetProgress[].class));
		}
	}, new Token("pet") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			PetManager pets = p.getPetManager();
			s.writeVal(pets.getPet().isPresent() ? pets.getPet().get().getProgress() : null);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			PetProgress progress = n.as(PetProgress.class);
			if(progress != null) {
				PetManager pets = p.getPetManager();
				pets.put(new Pet(progress, new Position(0, 0)));
			}
		}
	}, new Token("familiar") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			Familiar familiar = p.getFamiliar().orElse(null);
			s.writeVal(familiar != null ? familiar.getData() : null);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			String fam = n.toString();
			if(fam == null || fam.equals("null")) {
				p.setFamiliar(Optional.empty());
			} else {
				SummoningData data = SummoningData.valueOf(fam.toUpperCase());
				Familiar familiar = data.create();
				p.setFamiliar(Optional.of(familiar));
			}
		}
	}, new Token("familiar-life") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			Familiar familiar = p.getFamiliar().orElse(null);
			s.writeVal(familiar != null ? familiar.getCurrentHealth() : 0);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getFamiliar().ifPresent(ffs -> ffs.setCurrentHealth(n.toInt()));
		}
	}, new Token("familiar-duration") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			Familiar familiar = p.getFamiliar().orElse(null);
			s.writeVal(familiar != null ? familiar.getDuration() : 0);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			p.getFamiliar().ifPresent(ffs -> ffs.setDuration(n.toInt()));
		}
	}, new Token("familiar-storage") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			Familiar familiar = p.getFamiliar().orElse(null);
			s.writeVal(familiar != null && familiar.getAbilityType().getType() == BEAST_OF_BURDEN ? ((FamiliarContainer) familiar.getAbilityType()).getContainer().getItems() : null);
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			//if(!n.isJsonNull()) {
			p.getFamiliar().ifPresent(ffs -> {
				FamiliarAbility ability = ffs.getAbilityType();
				if(ability.getType() == BEAST_OF_BURDEN) {
					((FamiliarContainer) ability).getContainer().fillItems((n.as(Item[].class)));
				}
			});
			//}
		}
	}, new Token("house-rooms") {
		@Override
		public void toJson(Player p, JsonStream s) throws IOException {
			House house = p.getHouse();
			s.writeVal(house.get().getRooms());
		}
		
		@Override
		public void fromJson(Player p, Any n) {
			//if(!n.isJsonNull()) {
			p.getHouse().get().setRooms(n.as(Room[][][].class));
			//}
		}
	}};
	
	/**
	 * The container that represents a token that can be both serialized and deserialized.
	 * @author lare96 <http://github.com/lare96>
	 */
	private static abstract class Token {
		
		/**
		 * The name of this serializable token.
		 */
		private final String name;
		
		/**
		 * Creates a new {@link Token}.
		 * @param name the name of this serializable token.
		 */
		Token(String name) {
			this.name = name;
		}
		
		public abstract void toJson(Player p, JsonStream s) throws IOException;
		
		public abstract void fromJson(Player p, Any n);
		
		@Override
		public String toString() {
			return "TOKEN[name= " + name + "]";
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this == obj)
				return true;
			if(obj == null)
				return false;
			if(!(obj instanceof Token))
				return false;
			Token other = (Token) obj;
			if(name == null) {
				if(other.name != null)
					return false;
			} else if(!name.equals(other.name))
				return false;
			return true;
		}
		
		/**
		 * Gets the name of this serializable token.
		 * @return the name of this token.
		 */
		public String getName() {
			return name;
		}
		
	}
	
}