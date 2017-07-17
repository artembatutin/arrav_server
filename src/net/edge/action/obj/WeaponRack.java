package net.edge.action.obj;

import net.edge.action.ActionInitializer;

public class WeaponRack extends ActionInitializer {

	@Override
	public void init() {
		/*ObjectAction l = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.getDialogueBuilder().append(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						player.getInventory().add(new Item(14486));
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.getInventory().add(new Item(13450));
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						player.getInventory().add(new Item(13444));
					} else {
						player.getInventory().add(new Item(13405));
						player.getInventory().add(new Item(11212, 200));
					}
					player.closeInterface();
				}, "Dragon claws", " Armadyl godsword", "Whip", "Dark bow"));
				player.message("Lended items will be removed when you get out of here.");
				return true;
			}
		};
		l.registerFirst(6069);
		
		int startX = 3089;
		int startY = 3511;
		int endX = 3095;
		int endY = 3506;
		Position pos = new Position(3089, 3511, 1);
		Region reg = World.getRegions().getRegion(pos);
		World.get().submit(new Task(15, false) {
			@Override
			protected void execute() {
				int x = RandomUtils.inclusive(startX, endX);
				int y = RandomUtils.inclusive(endY, startY);
				Position pos = new Position(x, y, 1);
				reg.getPlayers().forEach((i, p) -> {
					p.getMessages().sendGraphic(405, pos, 0);
					if(p.getPosition().same(pos)) {
						p.damage(new Hit(200, Hit.HitType.CRITICAL, Hit.HitIcon.CANON));
					}
				});
			}
		});*/
	}
}
