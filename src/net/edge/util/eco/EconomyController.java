package net.edge.util.eco;

import com.google.gson.*;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.PlayerCredentials;
import net.edge.world.entity.actor.player.PlayerSerialization;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Bank;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Optional;

public class EconomyController {

	public static void run(ItemController controller) throws Exception {
		File[] folder = new File("./data/players/").listFiles();
		controller.start();
		for(File f : folder) {
			System.out.println(f.toString());
			if(!f.toString().contains("json"))
				continue;
			f = Paths.get(f.toString()).toFile();
			f.setReadable(true);
			try(FileReader in = new FileReader(f)) {
				Gson gson = new GsonBuilder().create();
				JsonElement read = new JsonParser().parse(in);
				JsonObject obj = (JsonObject) read;
				String username = obj.get("username").getAsString();
				String password = obj.get("password").getAsString();
				controller.player(username);
				Item[] inv = gson.fromJson(obj.get("inventory"), Item[].class);
				for(int i = 0; i < inv.length; i++) {
					if(inv[i] == null)
						continue;
					inv[i] = controller.change(inv[i]);
				}
				Item[] equ = gson.fromJson(obj.get("equipment"), Item[].class);
				for(int i = 0; i < equ.length; i++) {
					if(equ[i] == null)
						continue;
					equ[i] = controller.change(equ[i]);
				}
				Item[][] bank = new Item[Bank.SIZE][];
				for(int i = 0; i < Bank.SIZE; i++) {
					bank[i] = gson.fromJson(obj.get("bank" + i), Item[].class);
					for(int i2 = 0; i2 < bank.length; i2++) {
						if(bank[i][i2] == null)
							continue;
						bank[i][i2] = controller.change(bank[i][i2]);
					}
				}
				Optional<Player> active = World.get().getPlayer(username);
				active.ifPresent(a -> {
					System.out.println(a.getFormatUsername() + " was updated with economy controller.");
					a.getInventory().fillItems(inv);
					a.getEquipment().fillItems(equ);
					for(int i = 0; i < Bank.SIZE; i++) {
						a.getBank().fillItems(i, bank[i]);
					}

				});
				Player p = new Player(new PlayerCredentials(username, password));
				PlayerSerialization serial = new PlayerSerialization(p);
				PlayerSerialization.SerializeResponse resp = serial.loginCheck(password);
				serial.deserialize(resp.getReader());
				serial.serialize();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		controller.flush();
	}

}
