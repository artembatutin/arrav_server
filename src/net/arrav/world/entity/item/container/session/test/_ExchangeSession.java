package net.arrav.world.entity.item.container.session.test;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.ItemContainer;
import net.arrav.world.entity.item.container.session.*;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 8-2-2018.
 */
public abstract class _ExchangeSession {

    public final Player player;

    public final Player other;

    public final ExchangeSessionType type;

    private final Object2ObjectArrayMap<Player, ItemContainer> exchangeSession = new Object2ObjectArrayMap<>();

    public _ExchangeSession(Player player, Player other, ExchangeSessionType type) {
        this.player = player;
        this.other = other;
        this.type = type;
        forEach(p -> this.exchangeSession.put(p, new ItemContainer(28, ItemContainer.StackPolicy.STANDARD)));
    }

    public boolean offerStage;

    public boolean confirmed;

    public boolean finalized;

    public int stage;

    /**
     * Attempts to add an item to the container.
     * @param player the player we're adding this item for.
     * @param slot   the inventory slot being added onto it.
     * @param amount the amount being added.
     * @return <true> if the item was added, <false> otherwise.
     */
    public final boolean add(Player player, int slot, int amount) {
        Item invItem = player.getInventory().get(slot);

        if(invItem == null) return false;
        if(!Item.valid(invItem) || !player.getInventory().contains(invItem.getId())) return false;
        if(!offerStage) return false;
        if(player.getInventory().get(slot) == null) return false;
       // if(!ExchangeSessionManager.get().inAnySession(player)) return false;
       // if(ExchangeSessionManager.get().containsSessionInconsistancies(player)) return false;
        if(!canAddItem(player, invItem, slot)) return false;

        if(!invItem.getDefinition().isTradable() && player.getRights().less(Rights.ADMINISTRATOR)) {
            player.message("You can't trade this item.");
            return false;
        }

        Item item = new Item(invItem.getId(), amount);

        int count = player.getInventory().computeAmountForId(item.getId());

        if(item.getAmount() > count) {
            item.setAmount(count);
        }

        if(exchangeSession.get(player).canAdd(item) && player.getInventory().canRemove(item)) {
            exchangeSession.get(player).add(item);
            player.getInventory().remove(item, slot);
            this.confirmed = false;
            updateOfferComponents();
            return true;
        }
        return false;
    }

    /**
     * Attempts to remove an item from the container.
     * @param player the player we're removing this item for.
     * @param item   the item being removed.
     * @return <true> if the item was removed, <false> otherwise.
     */
    public final boolean remove(Player player, Item item) {
        if(player == null || item == null) return false;
        if(!Item.valid(item) || !this.exchangeSession.get(player).contains(item.getId())) return false;
        if(!offerStage) return false;
        //if(!ExchangeSessionManager.get().inAnySession(player)) return false;
        //if(ExchangeSessionManager.get().containsSessionInconsistancies(player)) return false;
        if(!canRemoveItem(player, item)) return false;
        ItemContainer container = this.exchangeSession.get(player);
        int amount = container.computeAmountForId(item.getId());
        if(item.getAmount() > amount) {
            item = item.createWithAmount(amount);
        }
        if(container.canRemove(item) && player.getInventory().canAdd(item)) {
            container.remove(item);
            player.getInventory().add(item);
            this.confirmed = false;
            container.shift();
            updateOfferComponents();
            return true;
        }
        return false;
    }

    public void forEach(Consumer<Player> action) {
        Arrays.asList(player, other).forEach(action);
    }

    public void forEachSession(Consumer<_ExchangeSession> action) {
        Arrays.asList(player.exchange_manager.session, other.exchange_manager.session).forEach(action);
    }

    /**
     * Finalises the exchange session, the premise for receiving items is depicted by the {@code type}.
     * @param type the type of finalising the session.
     */
    public final void finalize(ExchangeSessionActionType type) {
        if(finalized) {
            return;
        }

        finalized = true;

        switch(type) {
            case DISPOSE_ITEMS:
                forEach(player -> exchangeSession.get(player).clear());
                break;
            case RESTORE_ITEMS:
                forEach(player -> {
                    ItemContainer items = exchangeSession.get(player);
                    player.getInventory().addAll(items);
                    items.clear();
                });
                break;
            case HALT:
                //nothing happens with the items in the container when halted.
                break;
        }
        onReset();
    }
    /**
     * Checks if the item can be added to the container.
     * @param player the player who's attempting to add an item.
     * @param item   the item that was attempted to being added.
     * @param slot   the slot the item is being added from.
     * @return <true> if the item can, <false> otherwise.
     */
    public abstract boolean canAddItem(Player player, Item item, int slot);

    /**
     * Checks if the item can be removed from the container.
     * @param item the item that was attempted to being removed.
     * @return <true> if the item can, <false> otherwise.
     */
    public abstract boolean canRemoveItem(Player player, Item item);

    /**
     * Any functionality that should be dealth with when a player sends a request
     * should be handled in here.
     * @param player    the player who requested the exchange session.
     * @param requested the player who was requested by the {@code player}.
     */
    public abstract void onRequest(Player player, Player requested);

    /**
     * Any functionality that should be dealth when a player clicks a button
     * should be handled in here.
     * @param player the player whom clicked the button.
     * @param button the id that was clicked.
     */
    public abstract void onClickButton(Player player, int button);

    /**
     * Accepts the new {@code stage}.
     * @param player the player who accepted the stage.
     * @param stage  the possible stages.
     */
    public abstract void accept(Player player, int stage);

    /**
     * Updates the main components of the interface.
     */
    public abstract void updateMainComponents();

    /**
     * Updates the offer components of the interface.
     */
    public abstract void updateOfferComponents();

    /**
     * Any functionality that should be handled when the interface closes.
     */
    public abstract void onReset();

}
