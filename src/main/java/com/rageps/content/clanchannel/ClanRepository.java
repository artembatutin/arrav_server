package com.rageps.content.clanchannel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rageps.content.clanchannel.channel.ClanChannel;
import com.rageps.content.clanchannel.content.ClanViewer;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The repository containing all the clans and their corresponding data.
 *
 * @author Daniel
 */
public class ClanRepository {

	/** Map of all clan chat channels. */
	private static Map<String, ClanChannel> CHANNELS = new HashMap<>();

	/** Set of all active clan chat channels. */
	public static Set<ClanChannel> ACTIVE_CHANNELS = new HashSet<>();

	/** Set of all active clan chat tags. */
	public static Set<String> ACTIVE_TAGS = new HashSet<>();

	/** Set of all active clan chat names. */
	public static Set<String> ACTIVE_NAMES = new HashSet<>();

	/** Set of all active clan chat names. */
	public static Set<ClanChannel> ALLTIME = new TreeSet<>();

	/** Set of all active clan chat names. */
	private static Set<ClanChannel> TOP_PVP = new TreeSet<>();

	/** Set of all active clan chat names. */
	private static Set<ClanChannel> TOP_PVM = new TreeSet<>();

	/** Set of all active clan chat names. */
	private static Set<ClanChannel> TOP_SKILLING = new TreeSet<>();

	/** Set of all active clan chat names. */
	private static Set<ClanChannel> TOP_IRON_MAN = new TreeSet<>();

	/** Returns the channel. */
	public static Optional<ClanChannel> getChannel(String channel) {
		if(CHANNELS.containsKey(channel.toLowerCase().trim())) {
			return Optional.of(CHANNELS.get(channel.toLowerCase().trim()));
		}
		return Optional.empty();
	}

	/** Adds the channel. */
	public static void addChannel(ClanChannel channel) {
		CHANNELS.put(channel.getOwner().toLowerCase().trim(), channel);
	}

	/** Handles removing a channel from the clan lists. */
	public static void removeChannel(ClanChannel channel) {
		CHANNELS.remove(channel.getOwner().toLowerCase().trim());
		ACTIVE_CHANNELS.remove(channel);
		saveClan(channel);
	}

	public static void setActive(ClanChannel channel) {
		ACTIVE_CHANNELS.add(channel);
	}

	public static void setInactive(ClanChannel channel) {
		if (ACTIVE_CHANNELS.remove(channel)) {
			saveClan(channel);
		}
	}

	public static boolean nameExist(String name) {
		return ACTIVE_TAGS.contains(name.toLowerCase().trim());
	}

	public static boolean tagExist(String tag) {
		for (ClanChannel channel : CHANNELS.values()) {
			if (channel.getTag().equalsIgnoreCase(tag))
				return true;
		}
		return false;
	}

	public static void saveAllActiveClans() {
		for (ClanChannel channel : ClanRepository.ACTIVE_CHANNELS) {
			if (channel.activeSize() <= 0) {
				continue;
			}
			saveClan(channel);
		}
	}

	private static void saveClan(ClanChannel channel) {
		new Thread(() -> {
			final File dir = Paths.get("data", "content", "clan").toFile();

			if (!dir.exists()) {
				dir.mkdirs();
			}

			try (FileWriter fw = new FileWriter(dir.toPath().resolve(channel.getOwner() + ".json").toFile())) {
				fw.write(new Gson().toJson(channel.toJson()));
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	/** Loads all clans and puts them into the map. */
	public static void loadChannels() {
		Path path = Paths.get("./data/content/clan/");
		File[] files = path.toFile().listFiles();

		if (files == null) {
			return;
		}

		for (File file : files) {
			/*
			 * ClanChannel channel = ClanChannel.fromJson(file.getName().replace(".json",
			 * "")); if (channel != null) { if (!channel.getTag().isEmpty())
			 * ACTIVE_TAGS.add(channel.getTag()); if (!channel.getName().isEmpty())
			 * ACTIVE_NAMES.add(channel.getName()); ALLTIME.add(channel);
			 * CHANNELS.put(channel.getOwner().toLowerCase().trim(), channel); }
			 */

			ClanChannel.load(file.getName().replace(".json", ""));
		}
	}

	public static Optional<Set<ClanChannel>> getTopChanels(ClanType type) {
		if (type.equals(ClanType.PVP)) {
			return Optional.of(TOP_PVP);
		}
		if (type.equals(ClanType.PVM)) {
			return Optional.of(TOP_PVM);
		}
		if (type.equals(ClanType.SKILLING)) {
			return Optional.of(TOP_SKILLING);
		}
		if (type.equals(ClanType.IRON_MAN)) {
			return Optional.of(TOP_IRON_MAN);
		}
		return Optional.empty();
	}

	public static Optional<Set<ClanChannel>> getTopChanels(ClanViewer.Filter filter) {
		if (filter.equals(ClanViewer.Filter.ALL_TIME)) {
			return Optional.of(ALLTIME);
		}
		if (filter.equals(ClanViewer.Filter.TOP_PVM)) {
			return Optional.of(TOP_PVM);
		}
		if (filter.equals(ClanViewer.Filter.TOP_PVP)) {
			return Optional.of(TOP_PVP);
		}
		if (filter.equals(ClanViewer.Filter.TOP_SKILLING)) {
			return Optional.of(TOP_SKILLING);
		}
		if (filter.equals(ClanViewer.Filter.TOP_IRON_MAN)) {
			return Optional.of(TOP_IRON_MAN);
		}
		return Optional.empty();
	}
}
