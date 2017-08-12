package net.edge.content.skill.fletching;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.action.impl.ItemOnItemAction;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.Skills;
import net.edge.content.skill.action.impl.ProducingSkillAction;
import net.edge.task.Task;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

import java.util.EnumSet;
import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 12-8-2017.
 */
public final class DartCreation extends ProducingSkillAction {

    public final DartData definition;

    /**
     * Creates a new {@link ProducingSkillAction}.
     * @param player        the player this skill action is for.
     * @param definition    the dart this skill action gathers data from.
     */
    public DartCreation(Player player, DartData definition) {
        super(player, Optional.empty());
        this.definition = definition;
    }

    public static void action() {
        for(DartData dart : DartData.values()) {
            ItemOnItemAction action = new ItemOnItemAction() {
                @Override
                public boolean click(Player player, Item secondItem, Item firstItem, int slotFirst, int slotSecond) {
                    if(firstItem.getId() == dart.tips && secondItem.getId() == FEATHERS.getId() || firstItem.getId() == FEATHERS.getId() && secondItem.getId() == dart.tips) {
                        DartCreation creation = new DartCreation(player, dart);
                        creation.start();
                        return true;
                    }
                    return false;
                }
            };
            action.register(dart.tips);
        }
    }

    /**
     * The definition for a headless arrow.
     */
    private static final Item FEATHERS = new Item(314, 15);

    /**
     * The delay intervals of this skill action in ticks.
     *
     * @return the delay intervals.
     */
    @Override
    public int delay() {
        return 3;
    }

    /**
     * Determines if this skill action should be executed instantly rather than
     * after the delay.
     *
     * @return <true> if this skill action should be instant, <false> otherwise.
     */
    @Override
    public boolean instant() {
        return true;
    }

    /**
     * Initializes this skill action and performs any pre-checks, <b>this method is only executed
     * one<b>.
     *
     * @return <true> if the skill action can proceed, <false> otherwise.
     */
    @Override
    public boolean init() {
        return checkFletching();
    }

    /**
     * Determines if this skill can be executed, <b>this method is executed
     * every tick</b>
     *
     * @return <true> if this skill can be executed, <false> otherwise.
     */
    @Override
    public boolean canExecute() {
        return checkFletching();
    }

    /**
     * The experience given from this skill action.
     *
     * @return the experience given.
     */
    @Override
    public double experience() {
        return definition.experience;
    }

    /**
     * The skill that this skill action is for.
     *
     * @return the skill data.
     */
    @Override
    public SkillData skill() {
        return SkillData.FLETCHING;
    }

    /**
     * The item that will be removed upon production.
     *
     * @return the item that will be removed.
     */
    @Override
    public Optional<Item[]> removeItem() {
        return Optional.of(new Item[]{new Item(definition.tips, 15), FEATHERS});
    }

    /**
     * The item that will be added upon production.
     *
     * @return the item that will be added.
     */
    @Override
    public Optional<Item[]> produceItem() {
        return Optional.of(new Item[]{new Item(definition.itemId, 15)});
    }

    @Override
    public void onProduce(Task t, boolean success) {
        if(success) {
            t.cancel();
        }
    }

    private boolean checkFletching() {
        if(!player.getSkills()[Skills.FLETCHING].reqLevel(definition.requirement)) {
            player.message("You need a fletching level of " + definition.requirement + " to fletch this arrow.");
            return false;
        }
        return true;
    }

    private enum DartData {
        BRONZE_DART(806, 819, 10, 18),
        IRON_DART(807, 820, 22, 38),
        STEEL_DART(808, 821, 37, 75),
        MITHRIL_DART(809, 822, 52, 112),
        ADAMANT_DART(810, 823, 67, 150),
        RUNE_DART(811, 824, 81, 188),
        DRAGON_DART(11230, 11232, 95, 250);

        private static final ImmutableSet<DartData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(DartData.class));

        public final int itemId;

        public final int tips;

        public final int requirement;

        public final double experience;

        DartData(int itemId, int tips, int requirement, double experience) {
            this.itemId = itemId;
            this.tips = tips;
            this.requirement = requirement;
            this.experience = experience;
        }

        public static Optional<DartData> getDefinition(int firstItem, int secondItem) {
            return VALUES.stream().filter(def -> def.tips == firstItem || def.tips == secondItem).findAny();
        }

    }
}
