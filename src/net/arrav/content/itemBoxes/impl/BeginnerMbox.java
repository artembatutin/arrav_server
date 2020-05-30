package net.arrav.content.itemBoxes.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.arrav.content.itemBoxes.BoxLoot;
import net.arrav.content.itemBoxes.ItemBox;
import net.arrav.content.itemBoxes.ItemBoxLootParser;
import net.arrav.util.rand.Chance;
import net.arrav.world.entity.actor.player.Player;

import java.util.stream.Collectors;

public class BeginnerMbox implements ItemBox {

    private ObjectArrayList<BoxLoot> loot;
    private ObjectArrayList<Chance> chances;

    public BeginnerMbox() {
        loot = new ItemBoxLootParser("beginner_dragon_slayer.json").getLoot();
        chances = loot.stream().map(BoxLoot::getChance).distinct().collect(Collectors.toCollection(ObjectArrayList::new));
    }

    @Override
    public int freeSlotsRequired() {
        return 2;
    }

    @Override
    public int lootCount(Player player) {
        return 1;
    }

    @Override
    public ObjectArrayList<Chance> getChances() {
        return chances;
    }

    @Override
    public ObjectArrayList<BoxLoot> getItems() {
        return loot;
    }

    @Override
    public String name() {
        return "Beginner box";
    }
}
