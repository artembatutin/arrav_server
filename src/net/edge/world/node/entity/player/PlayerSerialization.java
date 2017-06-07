package net.edge.world.node.entity.player;

import com.google.gson.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.edge.net.codec.login.LoginResponse;
import net.edge.util.MutableNumber;
import net.edge.util.json.GsonUtils;
import net.edge.content.clanchat.ClanMember;
import net.edge.content.combat.weapon.FightType;
import net.edge.content.container.ItemContainer;
import net.edge.content.container.impl.Equipment;
import net.edge.content.container.impl.Inventory;
import net.edge.content.container.impl.bank.Bank;
import net.edge.content.minigame.barrows.BarrowsData;
import net.edge.content.pets.Pet;
import net.edge.content.pets.PetManager;
import net.edge.content.pets.PetProgress;
import net.edge.content.quest.Quest;
import net.edge.content.quest.QuestManager.Quests;
import net.edge.content.skill.Skill;
import net.edge.content.skill.Skills;
import net.edge.content.skill.slayer.Slayer;
import net.edge.content.skill.summoning.Summoning;
import net.edge.content.skill.summoning.familiar.Familiar;
import net.edge.content.skill.summoning.familiar.FamiliarContainer;
import net.edge.locale.Position;
import net.edge.world.World;
import net.edge.world.node.entity.attribute.AttributeKey;
import net.edge.world.node.entity.attribute.AttributeValue;
import net.edge.world.node.entity.move.MovementQueue;
import net.edge.world.node.entity.player.assets.AntifireDetails;
import net.edge.world.node.entity.player.assets.PrayerBook;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.entity.player.assets.Spellbook;
import net.edge.world.node.item.Item;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

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
	 * The linked hash collection of tokens that will be serialized and
	 * deserialized. A linked hash set is used here to ensure that there is only
	 * one of each token, and to preserve order.
	 */
	private final Set<TokenSerializer> tokens = new LinkedHashSet<>();
	
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
		this.cf = Paths.get("./data/players/" + player.getUsername() + ".json").toFile();
		createTokens();
	}
	
	/**
	 * The function where all of the tokens are added to the linked hash
	 * collection. Add as many tokens here as needed but keep in mind tokens
	 * cannot have the same name.
	 * <p>
	 * The token serialization format is as follows:
	 * <p>
	 * <p>
	 * <p>
	 * <pre>
	 * tokens.add(new TokenSerializer(NAME_OF_TOKEN, SERIALIZATION, DESERIALIZATION));
	 * </pre>
	 * <p>
	 * For those who are still confused, here is an example. Lets say we want
	 * "deathCount" to be saved to and loaded from the character file:
	 * <p>
	 * <p>
	 * <p>
	 * <pre>
	 * private int deathCount;
	 * public void setDeathCount(int deathCount) {
	 *     this.deathCount = deathCount;
	 * }
	 * public int getDeathCount() {
	 *     return deathCount;
	 * }
	 * </pre>
	 * <p>
	 * We would be able to do it like this:
	 * <p>
	 * <p>
	 * <p>
	 * <pre>
	 * tokens.add(new TokenSerializer(&quot;death-count&quot;, player.getDeathCount(), n -&gt; player.setDeathCount(n.getAsInt())));
	 * </pre>
	 */
	private void createTokens() {
		Gson b = new GsonBuilder().create();
		Player p = player;
		
		//Player information
		tokens.add(new TokenSerializer("username", p.getUsername(), n -> p.setUsername(n.getAsString()), TokeType.LOGIN));
		tokens.add(new TokenSerializer("password", p.getPassword(), n -> p.setPassword(n.getAsString()), TokeType.LOGIN));
		tokens.add(new TokenSerializer("banned", p.isBanned(), n -> p.setBanned(n.getAsBoolean()), TokeType.LOGIN));
		tokens.add(new TokenSerializer("muted", p.isMuted(), n -> p.setMuted(n.getAsBoolean()), TokeType.LOGIN));
		tokens.add(new TokenSerializer("position", p.getPosition(), n -> p.setPosition(b.fromJson(n, Position.class)), TokeType.LOGIN));
		tokens.add(new TokenSerializer("rights", p.getRights(), n -> p.setRights(Rights.valueOf(n.getAsString())), TokeType.LOGIN));
		tokens.add(new TokenSerializer("nightmare", p.getNightMode(), n -> p.setNight(n.getAsInt())));
		tokens.add(new TokenSerializer("vote", p.getVote(), n -> p.setVote(n.getAsInt())));
		
		Optional<ClanMember> clan = p.getClan();
		tokens.add(new TokenSerializer("clan", clan.isPresent() ? clan.get().getClan().getOwner() : "", n -> World.getClanManager().join(p, n.getAsString())));
		PlayerAppearance appearance = p.getAppearance();
		tokens.add(new TokenSerializer("appearance", appearance.getValues(), n -> appearance.setValues(b.fromJson(n, int[].class))));
		MovementQueue movement = p.getMovementQueue();
		
		//Behaviour
		tokens.add(new TokenSerializer("prayer-type", p.getPrayerBook().name(), n -> p.setPrayerBook(PrayerBook.valueOf(n.getAsString()))));
		tokens.add(new TokenSerializer("spell-book", p.getSpellbook().name(), n -> p.setSpellbook(Spellbook.valueOf(n.getAsString()))));
		tokens.add(new TokenSerializer("fight-type", p.getFightType().name(), n -> p.setFightType(FightType.valueOf(n.getAsString()))));
		tokens.add(new TokenSerializer("poison-damage", p.getPoisonDamage().get(), n -> p.getPoisonDamage().set(n.getAsInt())));
		tokens.add(new TokenSerializer("auto-retaliate", p.isAutoRetaliate(), n -> p.setAutoRetaliate(n.getAsBoolean())));
		tokens.add(new TokenSerializer("running", movement.isRunning(), n -> movement.setRunning(n.getAsBoolean())));
		
		Optional<AntifireDetails> antifire = player.getAntifireDetails();
		tokens.add(new TokenSerializer("antifire-details", antifire.isPresent() ? antifire.get() : null, n -> p.setAntifireDetail(n.isJsonNull() ? Optional.empty() : Optional.of(b.fromJson(n.getAsJsonObject(), AntifireDetails.class)))));
		
		//Timers
		tokens.add(new TokenSerializer("run-energy", p.getRunEnergy(), n -> p.setRunEnergy(n.getAsDouble())));
		MutableNumber percentage = p.getSpecialPercentage();
		tokens.add(new TokenSerializer("special-amount", percentage.get(), n -> percentage.set(n.getAsInt())));
		MutableNumber teleblocked = p.getTeleblockTimer();
		tokens.add(new TokenSerializer("teleblock-timer", teleblocked.get(), n -> teleblocked.set(n.getAsInt())));
		MutableNumber skulled = p.getSkullTimer();
		tokens.add(new TokenSerializer("skull-timer", skulled.get(), n -> skulled.set(n.getAsInt())));
		boolean vengeance = player.isVenged();
		tokens.add(new TokenSerializer("vengeance", vengeance, n -> p.setVenged(n.getAsBoolean())));
		
		//Containers and arrays
		Skill[] skills = p.getSkills();
		tokens.add(new TokenSerializer("skills", skills, n -> System.arraycopy(b.fromJson(n, Skill[].class), 0, skills, 0, skills.length)));
		
		Inventory inventory = p.getInventory();
		tokens.add(new TokenSerializer("inventory", inventory.toArray(), n -> inventory.setItems(b.fromJson(n, Item[].class))));
		
		Bank bank = p.getBank();
		for(int i = 0; i < Bank.SIZE; i++) {
			int index = i;
			tokens.add(new TokenSerializer("bank" + index, bank.items(index), n -> bank.setItems(index, b.fromJson(n, Item[].class))));
		}
		
		Equipment equipment = p.getEquipment();
		tokens.add(new TokenSerializer("equipment", equipment.toArray(), n -> equipment.setItems(b.fromJson(n, Item[].class))));
		Set<Long> f = p.getFriends();
		tokens.add(new TokenSerializer("friends", f.toArray(), n -> Collections.addAll(f, b.fromJson(n, Long[].class))));
		Set<Long> i = p.getIgnores();
		tokens.add(new TokenSerializer("ignores", i.toArray(), n -> Collections.addAll(i, b.fromJson(n, Long[].class))));
		
		//general information
		tokens.add(new TokenSerializer("total-player-kills", player.getPlayerKills().get(), n -> player.getPlayerKills().set(n.getAsInt())));
		tokens.add(new TokenSerializer("total-player-deaths", player.getDeathsByPlayer().get(), n -> player.getDeathsByPlayer().set(n.getAsInt())));
		tokens.add(new TokenSerializer("total-npc-kills", player.getNpcKills().get(), n -> player.getNpcKills().set(n.getAsInt())));
		tokens.add(new TokenSerializer("total-npc-deaths", player.getDeathsByNpc().get(), n -> player.getDeathsByNpc().set(n.getAsInt())));
		tokens.add(new TokenSerializer("total-player-kills", player.getPlayerKills().get(), n -> player.getPlayerKills().set(n.getAsInt())));
		tokens.add(new TokenSerializer("current-killstreak", player.getCurrentKillstreak().get(), n -> player.getCurrentKillstreak().set(n.getAsInt())));
		tokens.add(new TokenSerializer("highest-killstreak", player.getHighestKillstreak().get(), n -> player.getHighestKillstreak().set(n.getAsInt())));
		
		//godwars killcount
		int[] killcount = player.getGodwarsKillcount();
		tokens.add(new TokenSerializer("godwars-killcount", killcount, n -> player.setGodwarsKillcount(b.fromJson(n, int[].class))));
		
		//Minigames
		EnumSet<BarrowsData> barrows = player.getMinigameContainer().getBarrowsContainer().getKilledBrothers();
		tokens.add(new TokenSerializer("killed-brothers", barrows.toArray(), n -> Collections.addAll(barrows, b.fromJson(n, BarrowsData[].class))));
		
		//Slayer
		Optional<Slayer> slayer = p.getSlayer();
		tokens.add(new TokenSerializer("slayerPoints", p.getSlayerPoints(), n -> p.updateSlayers(n.getAsInt())));
		tokens.add(new TokenSerializer("slayer", slayer.isPresent() ? slayer.get() : null, n -> p.setSlayer(n.isJsonNull() ? Optional.empty() : Optional.of(b.fromJson(n, Slayer.class)))));
		tokens.add(new TokenSerializer("blocked-tasks", p.getBlockedTasks(), n -> p.setBlockedTasks(b.fromJson(n, String[].class))));
		
		//Pets
		PetManager pets = player.getPetManager();
		tokens.add(new TokenSerializer("pets", pets.getProgress().toArray(), n -> Collections.addAll(pets.getProgress(), b.fromJson(n, PetProgress[].class))));
		tokens.add(new TokenSerializer("pet", pets.getPet().isPresent() ? pets.getPet().get().getProgress() : null, n -> pets.put(n.isJsonNull() ? null : new Pet((b.fromJson(n, PetProgress.class)), new Position(0, 0)))));
		
		//Summoning
		Familiar familiar = player.getFamiliar().orElse(null);
		ItemContainer storage = familiar != null ? ((FamiliarContainer) familiar.getAbilityType()).getContainer() : new ItemContainer(30, ItemContainer.StackPolicy.STANDARD);
		
		tokens.add(new TokenSerializer("familiar", familiar != null ? familiar.getId() : 0, n -> p.setFamiliar(Summoning.FAMILIARS.stream().filter(ffs -> ffs.getId() == n.getAsInt()).findFirst())));
		tokens.add(new TokenSerializer("familiarLife", familiar != null ? familiar.getCurrentHealth() : 0, n -> p.getFamiliar().ifPresent(ffs -> ffs.setCurrentHealth(n.getAsInt()))));
		tokens.add(new TokenSerializer("familiarDuration", familiar != null ? familiar.getDuration() : 0, n -> p.getFamiliar().ifPresent(ffs -> ffs.setDuration(n.getAsInt()))));
		tokens.add(new TokenSerializer("familiarStorage", storage.toArray(), n -> p.getFamiliar().ifPresent(ffs -> ((FamiliarContainer) ffs.getAbilityType()).getContainer().setItems((b.fromJson(n, Item[].class))))));
		tokens.add(new TokenSerializer("validated", p.isValidated(), n -> p.setValidated(n.getAsBoolean())));
		
		tokens.add(new TokenSerializer("familiar", familiar != null ? familiar.getId() : 0, n -> p.setFamiliar(Summoning.FAMILIARS.stream().filter(ffs -> ffs.getId() == n.getAsInt()).findFirst())));
		tokens.add(new TokenSerializer("familiarLife", familiar != null ? familiar.getCurrentHealth() : 0, n -> p.getFamiliar().ifPresent(ffs -> ffs.setCurrentHealth(n.getAsInt()))));
		tokens.add(new TokenSerializer("familiarDuration", familiar != null ? familiar.getDuration() : 0, n -> p.getFamiliar().ifPresent(ffs -> ffs.setDuration(n.getAsInt()))));
		tokens.add(new TokenSerializer("familiarStorage", storage.toArray(), n -> p.getFamiliar().ifPresent(ffs -> ((FamiliarContainer) ffs.getAbilityType()).getContainer().setItems((b.fromJson(n, Item[].class))))));
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
					throw new IllegalStateException("Unable to register " + "directory for character files!");
				}
			}
			try(FileWriter out = new FileWriter(cf)) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonObject obj = new JsonObject();
				tokens.forEach(t -> obj.add(t.getName(), gson.toJsonTree(t.getToJson())));
				Object2ObjectArrayMap<String, Object> quests = new Object2ObjectArrayMap<>();
				for(Map.Entry<Quests, Quest> it : player.getQuestManager().getStartedQuests().entrySet()) {
					Quests key = it.getKey();
					Quest value = it.getValue();
					
					Object2ObjectLinkedOpenHashMap<String, Object> attributeEntry = new Object2ObjectLinkedOpenHashMap<>();
					attributeEntry.put("quest", value);
					
					quests.put(key.name(), attributeEntry);
				}
				obj.add("quests", gson.toJsonTree(quests));
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
				obj.add("attributes", gson.toJsonTree(attributes));
				out.write(gson.toJson(obj));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deserializes the player from a {@code JSON} file.
	 * @return the login response determined by what happened before, during,
	 * and after deserialization.
	 */
	public LoginResponse deserialize(String password, boolean login) {
		try {
			File file = Paths.get("./data/players/" + player.getUsername() + ".json").toFile();
			if(!file.exists()) {
				if(login)
					Skills.create(player);
				return LoginResponse.NORMAL;
			}
			cf.setReadable(true);
			try(FileReader in = new FileReader(cf)) {
				JsonElement readed = new JsonParser().parse(in);
				if(readed.isJsonNull()) {
					return LoginResponse.NORMAL;//corrupted.
				}
				JsonObject reader = (JsonObject) readed;
				if(reader.has("bank")) {
					Gson b = new GsonBuilder().create();
					player.getBank().setItems(0, b.fromJson(reader.get("bank"), Item[].class));
				}
				tokens.stream().filter(t -> reader.has(t.getName()) && (!login || t.getType() == TokeType.LOGIN)).forEach(t -> t.getFromJson().accept(reader.get(t.getName())));
				if(reader.has("attributes") && login) {
					JsonObject attr = reader.get("attributes").getAsJsonObject();
					for(Map.Entry<String, JsonElement> it : attr.entrySet()) {
						JsonObject obj = it.getValue().getAsJsonObject();
						String old = obj.get("type").getAsString();
						Class<?> type = Class.forName(old);
						Object data = GsonUtils.getAsType(obj.get("value"), type);
						if(AttributeKey.ALIASES.keySet().stream().anyMatch(s -> s.equals(it.getKey())))
							player.getAttr().get(it.getKey()).set(data);
					}
				}
			}
			if(!password.equals(player.getPassword()) && password.length() > 0)
				return LoginResponse.INVALID_CREDENTIALS;
			if(player.isBanned())
				return LoginResponse.ACCOUNT_DISABLED;
		} catch(Exception e) {
			e.printStackTrace();
			return LoginResponse.COULD_NOT_COMPLETE_LOGIN;
		}
		return LoginResponse.NORMAL;
	}
	
	/**
	 * The container that represents a token that can be both serialized and
	 * deserialized.
	 * @author lare96 <http://github.com/lare96>
	 */
	private static final class TokenSerializer {
		
		/**
		 * The name of this serializable token.
		 */
		private final String name;
		
		/**
		 * The {@code Object} being serialized by this token.
		 */
		private final Object toJson;
		
		/**
		 * The deserialization consumer for this token.
		 */
		private final Consumer<JsonElement> fromJson;
		
		/**
		 * The type of this token.
		 */
		private final TokeType type;
		
		/**
		 * Creates a new {@link TokenSerializer}.
		 * @param name     the name of this serializable token.
		 * @param toJson   the {@code Object} being serialized by this token.
		 * @param fromJson the deserialization consumer for this token.
		 */
		TokenSerializer(String name, Object toJson, Consumer<JsonElement> fromJson) {
			this.name = name;
			this.toJson = toJson;
			this.fromJson = fromJson;
			this.type = TokeType.GAME;
		}
		
		/**
		 * Creates a new {@link TokenSerializer}.
		 * @param name     the name of this serializable token.
		 * @param toJson   the {@code Object} being serialized by this token.
		 * @param fromJson the deserialization consumer for this token.
		 */
		TokenSerializer(String name, Object toJson, Consumer<JsonElement> fromJson, TokeType type) {
			this.name = name;
			this.toJson = toJson;
			this.fromJson = fromJson;
			this.type = type;
		}
		
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
			if(!(obj instanceof TokenSerializer))
				return false;
			TokenSerializer other = (TokenSerializer) obj;
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
		
		/**
		 * Gets the {@code Object} being serialized by this token.
		 * @return the serializable object.
		 */
		Object getToJson() {
			return toJson;
		}
		
		/**
		 * Gets the deserialization consumer for this token.
		 * @return the deserialization consumer.
		 */
		public Consumer<JsonElement> getFromJson() {
			return fromJson;
		}
		
		/**
		 * Gets the deserialization consumer for this token.
		 * @return the deserialization consumer.
		 */
		public TokeType getType() {
			return type;
		}
		
	}
	
	/**
	 * The type of token there is.
	 */
	private enum TokeType {
		LOGIN,
		GAME
	}
	
}