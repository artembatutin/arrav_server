package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.json.JsonLoader;
import net.edge.content.skill.slayer.Slayer;
import net.edge.content.skill.slayer.SlayerKeyPolicy;
import net.edge.content.skill.slayer.SlayerMaster;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * The {@link JsonLoader} implementation that loads all slayer keys.
 * @author <a href="http://www.rune-server.org/members/Stand+Up/">Stan</a>
 */
public final class SlayerDefinitionLoader extends JsonLoader {
	
	public SlayerDefinitionLoader() {
		super("./data/json/slayer/slayer.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		SlayerMaster[] masters = Objects.requireNonNull(builder.fromJson(reader.get("masters").getAsJsonArray(), SlayerMaster[].class));
		SlayerKeyPolicy[] tasks = Objects.requireNonNull(builder.fromJson(reader.get("tasks").getAsJsonArray(), SlayerKeyPolicy[].class));
		for(SlayerKeyPolicy task : tasks) {
			task.setAmount(IntStream.rangeClosed(task.getAmount()[0], task.getAmount()[1]).toArray());
		}
		Arrays.stream(masters).forEach(master -> Slayer.SLAYER_KEYS.put(master, tasks));
	}
	
}
