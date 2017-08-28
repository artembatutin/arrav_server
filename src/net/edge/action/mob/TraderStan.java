package net.edge.action.mob;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.MobAction;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;

public class TraderStan extends ActionInitializer {

	@Override
	public void init() {
		String[] chats = {"Come here come here I have a shiny scimitar for you!", "Discounted prices, come look!", "Only for you, only today! Exceptional offer.", "Don't be shy, I won't bite!", "This sword was King Arthur's! I swear.", "I buy all kinds of nice things.", "Search any item you want, I think I have it.", "All offers are welcome here.", "That would be few coins sir.", "Ladies and gentlemens, gather around.", "Wait let me note it down on my papirus.", "Yes that's an antifier shield on the wall.", "My mom actually did fit in that rune suite.", "Please be quite don't disturb the owner Sir Prysin", "My exchange level is 99", "I love coins, I very do.", "Look at my market! please...", "I'm the wealthiest person in Edgeville.", "I'm rich, very rich. Making Edgeville great again.", "Who you think rebuild Edgeville with all the money?", "My parents once told me to have financial eased friends.", "May the gold be with you.", "I aint gonna say I had a small loan of a million dollars from my Dad."};
		//		Mob.CUSTOM_MOBS.put(4650, s -> new DefaultMob(4650, s) {
		//			private int timer = 0;
		//			@Override
		//			public void update() {
		//				timer++;
		//				if(timer >= 200) {
		//					forceChat(RandomUtils.random(chats));
		//					timer = 0;
		//				}
		//			}
		//		});
		MobAction e = new MobAction() {
			@Override
			public boolean click(Player player, Mob npc, int click) {
				/*DialogueAppender app = new DialogueAppender(player);
				app.chain(new NpcDialogue(5913, "Hello " + player.getFormatUsername() + ", my name is Stan.", "How may I help you today?"));
				app.chain(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						app.getBuilder().advance();
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						app.getBuilder().skip();
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)){
						app.getBuilder().go(3);
					} else {
						app.getBuilder().last();
					}
				}, "Search all shops.", "Search for item", "Open my shop.", "Nevermind"));
				app.chain(new PlayerDialogue("I would like to search for all shops.").attachAfter(() -> {
					player.closeWidget();
					//open all the shops here
				}));
				app.chain(new PlayerDialogue("I would like to search for an item.").attachAfter(() -> {
					player.closeWidget();
					//search for item
				}));
				app.chain(new PlayerDialogue("I would like to open my shop.").attachAfter(() -> {
					player.closeWidget();
					//open own shop
				}));
				app.chain(new PlayerDialogue("Nevermind..."));
				app.start();*/
				player.widget(-13);
				return true;
			}
		};
	}
}
