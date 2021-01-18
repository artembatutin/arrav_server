package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.dialogue.impl.OptionDialogue;
import com.rageps.content.skill.Skills;
import com.rageps.content.skill.magic.Spellbook;
import com.rageps.content.skill.prayer.PrayerBook;
import com.rageps.action.ActionInitializer;
import com.rageps.world.model.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;

public class HomeAltar extends ActionInitializer {
	@Override
	public void init() {
		//prayer altar
		ObjectAction pray = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				int level = player.getSkills()[Skills.PRAYER].getRealLevel();
				if(player.getSkills()[Skills.PRAYER].getCurrentLevel() < level) {
					player.animation(new Animation(645));
					player.getSkills()[Skills.PRAYER].setLevel(level, true);
					player.message("You recharge your prayer points.");
					Skills.refresh(player, Skills.PRAYER);
				} else {
					player.message("You already have full prayer points.");
				}
				return true;
			}
		};
		pray.registerFirst(39842);
		pray.registerFirst(409);

		//prayer/magic switcher
		ObjectAction change = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.getDialogueBuilder().append(new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						if(player.getPrayerBook().equals(PrayerBook.CURSES)) {
							PrayerBook.convert(player, PrayerBook.NORMAL);
							player.getPrayerActive().forEach(p -> {
								if(p.getType() == PrayerBook.CURSES) {
									p.deactivate(player);
								}
							});
						} else if(player.getPrayerBook().equals(PrayerBook.NORMAL)) {
							PrayerBook.convert(player, PrayerBook.CURSES);
							player.getPrayerActive().forEach(p -> {
								if(p.getType() == PrayerBook.NORMAL) {
									p.deactivate(player);
								}
							});
						}
						player.closeWidget();
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						player.getDialogueBuilder().advance();
					}
				}, "Prayer switch", "Spell book switch"), new OptionDialogue(t -> {
					if(t.equals(OptionDialogue.OptionType.FIRST_OPTION)) {
						Spellbook.convert(player, Spellbook.NORMAL);
					} else if(t.equals(OptionDialogue.OptionType.SECOND_OPTION)) {
						Spellbook.convert(player, Spellbook.ANCIENT);
					} else if(t.equals(OptionDialogue.OptionType.THIRD_OPTION)) {
						Spellbook.convert(player, Spellbook.LUNAR);
					}
					player.closeWidget();
				}, "Normal", "Ancient", "Lunar"));
				return true;
			}
		};
		change.registerFirst(61);
		
	}
}
