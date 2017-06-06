package net.edge.content.item;

import net.edge.content.skill.hunter.Hunter;
import net.edge.content.skill.hunter.trap.impl.BirdSnare;
import net.edge.content.skill.hunter.trap.impl.BoxTrap;
import net.edge.task.LinkedTaskSequence;
import net.edge.world.Animation;
import net.edge.content.Dice;
import net.edge.content.combat.magic.lunars.impl.spells.HunterKit;
import net.edge.content.container.impl.Inventory;
import net.edge.content.dialogue.impl.GiveItemDialogue;
import net.edge.content.minigame.barrows.BarrowsMinigame;
import net.edge.content.skill.herblore.Herb;
import net.edge.content.skill.prayer.PrayerBoneBury;
import net.edge.content.skill.slayer.Slayer;
import net.edge.content.skill.summoning.Summoning;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.item.Item;

import java.util.Optional;

public class ItemAction {
	
	public static boolean third(Player player, Item item, int container, int slot) {
		
		return false;
	}
}
