package net.arrav.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.util.json.JsonLoader;
import net.arrav.world.entity.actor.mob.drop.Drop;
import net.arrav.world.entity.actor.mob.drop.DropManager;
import net.arrav.world.entity.actor.mob.drop.DropTable;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * The {@link JsonLoader} implementation that loads all npc drops.
 * @author lare96 <http://github.com/lare96>
 * @author Tamatea <tamateea@gmail.com>
 */
public final class MobDropTableLoader extends JsonLoader {

	/**
	 * Creates a new {@link MobDropTableLoader}.
	 */
	public MobDropTableLoader() {
		super("./data/def/mob/mob_drops.json");
		DropManager.TABLES = new ObjectArrayList<>();
	}

	@Override
	public void load(JsonObject reader, Gson builder) {

		int npcId = reader.get("npcID").getAsInt();
		int remains = reader.get("remainsID").getAsInt();


		boolean empty = !reader.has("primaryDrops");
		ObjectArrayList<Drop> dropTable = new ObjectArrayList<>();
		dropTable.add(new Drop(remains, 1, 1, 1, false, false));//adding it's bones
		if (!empty) {
			JsonArray dropArray = reader.get("primaryDrops").getAsJsonArray();

			if (dropArray.size() != 0)
				dropArray.forEach($it -> {


					try {
						int id = $it.getAsJsonObject().get("itemID").getAsInt();
						int min = $it.getAsJsonObject().get("minimumAmount").getAsInt();
						int max = $it.getAsJsonObject().get("maximumAmount").getAsInt();
						int den = $it.getAsJsonObject().get("denominator").getAsInt();
						boolean beam = $it.getAsJsonObject().get("beam").getAsBoolean();
						boolean announce = $it.getAsJsonObject().get("announce").getAsBoolean();

						dropTable.add(new Drop(id, min, max, den, beam, announce));
					} catch (Exception e) {
						System.out.println(npcId);
					}
				});

		}

		DropTable drop = new DropTable(npcId, dropTable);

		if (drop.getDrops().size() != 1)
			DropManager.TABLES.add(drop);

		//todo - add serializing of drops for client.
	}
}