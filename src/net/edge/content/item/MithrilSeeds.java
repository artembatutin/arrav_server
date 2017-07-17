package net.edge.content.item;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import net.edge.event.impl.ItemEvent;
import net.edge.event.impl.ObjectEvent;
import net.edge.locale.Position;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Direction;
import net.edge.world.World;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;
import net.edge.world.node.item.Item;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectNode;
import net.edge.world.object.ObjectType;

/**
 * The class which is responsible for mithril seeds.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @author Artem Batutin <artembatutin@gmail.com>
 * @since 27-6-2017.
 */
public final class MithrilSeeds {

    /**
     * The id's that represent flower objects with their item ids.
     */
    private static final Int2IntArrayMap FLOWER_OBJECT_IDS = new Int2IntArrayMap(ImmutableMap.<Integer, Integer>builder()
            .put(2980, 2460)
            .put(2981, 2462)
            .put(2982, 2464)
            .put(2983, 2466)
            .put(2984, 2468)
            .put(2985, 2470)
            .put(2986, 2472)
            .put(2987, 2474).build());

    public static void event() {
        for(int objectId : FLOWER_OBJECT_IDS.keySet()) {
            ObjectEvent objEvent = new ObjectEvent() {
                @Override
                public boolean click(Player player, ObjectNode object, int click) {
                    if(player.getInventory().remaining() < 1) {
                        player.message("You don't have enough inventory slots to pick these flowers up.");
                        return true;
                    }
                    if(object.toDynamic().getElements() != player.getSlot()) {
                        player.message("You didn't plant these flowers.");
                        return true;
                    }
                    int itemId = FLOWER_OBJECT_IDS.get(objectId);
    
                    player.animation(new Animation(827, Animation.AnimationPriority.HIGH));
                    player.getInventory().add(new Item(itemId));
                    object.remove();
                    return true;
                }
            };

            objEvent.registerFirst(objectId);
        }

        ItemEvent itemEvent = new ItemEvent() {
            @Override
            public boolean click(Player player, Item item, int container, int slot, int click) {
                if(player.getCombatBuilder().inCombat()) {
                    player.message("You are currently in combat.");
                    return true;
                }
    
                if(player.getRights().less(Rights.EXTREME_DONATOR)) {
                    player.message("You need to be an extreme donator to do plant this.");
                    return true;
                }
    
                if(!player.getDiceTimer().elapsed(1800)) {
                    return true;
                }
                player.getDiceTimer().reset();
    
                player.getInventory().remove(new Item(299), slot);
                player.animation(new Animation(827, Animation.AnimationPriority.HIGH));
                getRandomFlowerObject(player).publish(120, ObjectNode::remove);
                Position p = player.getPosition();
                if(World.getTraversalMap().isTraversable(p, Direction.WEST, player.size())) {
                    player.getMovementQueue().walk(Direction.WEST.getX(), Direction.WEST.getY());
                } else if(World.getTraversalMap().isTraversable(p, Direction.EAST, player.size())) {
                    player.getMovementQueue().walk(Direction.EAST.getX(), Direction.EAST.getY());
                }
                player.facePosition(p);
                return true;
            }
        };

        itemEvent.register(299);//mithril seed item id.
    }

    private static DynamicObject getRandomFlowerObject(Player player) {
        return new DynamicObject(RandomUtils.random(FLOWER_OBJECT_IDS.keySet().toIntArray()), player.getPosition(), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, player.getSlot(), 0);
    }
}
