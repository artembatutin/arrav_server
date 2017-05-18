package net.edge.world.content.commands.impl;

import net.edge.utils.TextUtils;
import net.edge.world.World;
import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"cam", "camera"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as just ::cam or ::camera")
public final class CameraCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		String[] names = {"Hailikor", "Ybirewiel", "Hairebard", "Rhoethiel", "Glardoseth", "Forecien", "Thendarid", "Onoadia", "Elaocan", "Chorewen", "Asterran", "Nearien", "Adirag", "Galaleveth", "Laroaa", "Ulendamwen", "Sevedribaen", "Uneiven", "Sirelith", "Afang", "Qoik", "Fien", "Drohanwan", "Sdaaga"};
		for(String usr : names) {
			Player play = new Player(TextUtils.nameToHash(usr));
			play.setUsername(usr);
			World.getClanManager().join(play, "avro");
		}
		//new IntroductionCutscene(player).submit();
		/*for(int i = 0 ; i < 10; i++) {
			RandomUtils random = new RandomUtils();
			NpcDropTable table = NpcDropManager.TABLES.get(-1);
			int expected = random.inclusive(4, 8);
			List<Item> loot = new ArrayList<>();
			int items = 0;
			while(items < expected) {
				NpcDropCache cache = random.random(table.getCommon());
				NpcDrop drop = random.random(NpcDropManager.COMMON.get(cache));
				if(drop.getChance().successful(random)) {
					loot.add(new Item(drop.getId(), random.inclusive(drop.getMinimum(), drop.getMaximum())));
					items++;
				}
			}
			
			System.out.println("barrow trip and again...");
			for(Item item : loot) {
				System.out.println("recieved: " + item.getAmount() + " " + item.getDefinition().getName());
			}
			System.out.println();
		}*/
		/*for(Chance chance : Chance.values()) {
			double avarage = 0;
			for(int i = 0; i < 100; i++) {
				int succeed = chance.debug(1000000);
				double percentage = (succeed / 1000000.0) * 100.0;
				if(i == 0)
					avarage = percentage;
				else
					avarage = (percentage + avarage) / 2.D;
				
			}
			System.out.println("Average for chance " + chance.toString().toLowerCase() + " is " + avarage + " %");
		}*/
		//player.damage(new Hit(player.getCurrentHealth()));
		//player.getMessages().sendItemOnInterface(34002, 4151);
		//System.out.println(player.getPosition().getLocalX() + " - " + player.getPosition().getLocalY());
		//player.getMovementQueue().smartWalk(player.getPosition().move(0, 10));
		/*if(cmd[0].equals("camera")) {
		        player.getMessages().sendResetCameraPosition();
				return;
			}
			player.getMessages().sendResetCameraPosition();
			World.getTaskManager().submit(new Task(3, false) {

				@Override
				public void execute() {
					player.getMessages().sendCameraMovement(new Position(3201, 3431), 250, 5, 100);
					player.getMessages().sendCameraAngle(new Position(3203, 3434), 250, 1, 100);
					this.cancel();
				}
			});*/
	}
	
}
