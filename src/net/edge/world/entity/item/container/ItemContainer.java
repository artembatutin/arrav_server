package net.edge.world.entity.item.container;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.edge.net.packet.out.SendContainer;
import net.edge.net.packet.out.SendItemOnInterfaceSlot;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.IndexedItem;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.ItemDefinition;

import java.util.*;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static net.edge.world.entity.item.container.ItemContainer.StackPolicy.ALWAYS;
import static net.edge.world.entity.item.container.ItemContainer.StackPolicy.STANDARD;

/**
 * An abstraction model representing a group of {@link Item}s.
 * @author lare96 <http://github.com/lare96>
 */
public class ItemContainer implements Iterable<Item> {
	
	/**
	 * An {@link Iterator} implementation for this container.
	 */
	private static final class ItemContainerIterator implements Iterator<Item> {
		
		/**
		 * The container instance to iterate over.
		 */
		private final ItemContainer container;
		
		/**
		 * The current index being iterated over.
		 */
		private int index;
		
		/**
		 * The last index that was iterated over.
		 */
		private int lastIndex = -1;
		
		/**
		 * Creates a new {@link ItemContainerIterator}.
		 * @param container The container instance to iterate over.
		 */
		public ItemContainerIterator(ItemContainer container) {
			this.container = container;
		}
		
		@Override
		public boolean hasNext() {
			return (index + 1) <= container.capacity;
		}
		
		@Override
		public Item next() {
			checkState(index < container.capacity, "no more elements left to iterate");
			lastIndex = index;
			index++;
			return container.items[lastIndex];
		}
		
		@Override
		public void remove() {
			checkState(lastIndex != -1, "can only be called once after 'next'");
			Item oldItem = container.items[lastIndex];
			container.items[lastIndex] = null;
			container.updateSingle(oldItem, null, lastIndex, true);
			index = lastIndex;
			lastIndex = -1;
		}
	}
	
	/**
	 * An enumerated type defining policies for stackable {@link Item}s.
	 */
	public enum StackPolicy {
		
		/**
		 * The {@code STANDARD} policy, items are only stacked if they are defined as stackable in their {@link
		 * ItemDefinition} table.
		 */
		STANDARD,
		
		/**
		 * The {@code ALWAYS} policy, items are always stacked regardless of their {@link ItemDefinition} table.
		 */
		ALWAYS,
		
		/**
		 * The {@code NEVER} policy, items are never stacked regardless of their {@link ItemDefinition} table.
		 */
		NEVER
	}
	
	/**
	 * An {@link ArrayList} of {@link ItemContainerListener}s listening for various events.
	 */
	private final ObjectArrayList<ItemContainerListener> listeners = new ObjectArrayList<>();
	
	/**
	 * The capacity of this container.
	 */
	private final int capacity;
	
	/**
	 * The policy of this container.
	 */
	private final StackPolicy policy;
	
	/**
	 * The {@link Item}s within this container.
	 */
	private final Item[] items;
	
	/**
	 * If events are currently being fired.
	 */
	private boolean firingEvents = true;
	
	/**
	 * If the container is immutable in test mode.
	 */
	private boolean test;
	
	/**
	 * The size of this container, counting occupied slots.
	 */
	private int size;
	
	/**
	 * Condition if packets are non-queued.
	 */
	private boolean nonQueued = false;
	
	/**
	 * Creates a new {@link ItemContainer}.
	 * @param capacity {@link #capacity()}.
	 * @param policy   {@link #policy}.
	 * @param items    {@link #items}.
	 */
	public ItemContainer(int capacity, StackPolicy policy, Item[] items) {
		this.capacity = capacity;
		this.policy = policy;
		this.items = items;
	}
	
	/**
	 * Creates a new {@link ItemContainer}.
	 * @param capacity {@link #capacity()}.
	 * @param policy   {@link #policy}.
	 */
	public ItemContainer(int capacity, StackPolicy policy) {
		this(capacity, policy, new Item[capacity]);
	}
	
	/**
	 * Iterates through all of the {@link Item}s within this container and performs {@code action} on them, skipping empty
	 * indexes ({@code null} values) as they are encountered.
	 */
	@Override
	public final void forEach(Consumer<? super Item> action) {
		Objects.requireNonNull(action);
		for(Item item : items) {
			if(item == null) {
				continue;
			}
			action.accept(item);
		}
	}
	
	@Override
	public final Spliterator<Item> spliterator() {
		return Spliterators.spliterator(items, Spliterator.ORDERED);
	}
	
	@Override
	public final Iterator<Item> iterator() {
		return new ItemContainerIterator(this);
	}
	
	/**
	 * Attempts to add {@code item} into this container.
	 * @param item The {@link Item} to add.
	 * @return amount of slots filled, -1 if failed, 0 if added stackable portion.
	 */
	public int add(Item item) {
		return add(item, -1, true);
	}
	
	/**
	 * Attempts to add {@code item} into this container, preferably at {@code preferredIndex}.
	 * @param item           The {@link Item} to add.
	 * @param preferredIndex The preferable index to add {@code item} to.
	 * @param refresh        The condition if we will be refreshing our container.
	 * @return amount of slots filled, -1 if failed, 0 if added stackable portion.
	 */
	public int add(Item item, int preferredIndex, boolean refresh) {
		checkArgument(preferredIndex >= -1, "invalid index identifier");
		if(item == null)
			return -1;
		ItemDefinition def = item.getDefinition();
		if(def == null)
			return -1;
		boolean stackable = (policy == STANDARD && def.isStackable()) || policy == ALWAYS;
		if(stackable) {
			preferredIndex = computeIndexForId(item.getId());
		} else if(preferredIndex != -1) {
			preferredIndex = items[preferredIndex] != null ? -1 : preferredIndex;
		}
		
		preferredIndex = preferredIndex == -1 ? computeFreeIndex() : preferredIndex;
		if(preferredIndex == -1) { // Not enough space in container.
			fireCapacityExceededEvent();
			return -1;
		}
		
		int filled = 0;
		if(stackable) {
			Item current = items[preferredIndex];
			if(current == null) {
				if(!test) {
					items[preferredIndex] = item;
					size++;
				}
				filled++;
			} else if(!test) {
				if(current.getId() == item.getId())
					items[preferredIndex] = current.createAndIncrement(item.getAmount());
			}
			updateSingle(current, items[preferredIndex], preferredIndex, refresh);
		} else {
			int remaining = remaining();
			int until = (remaining > item.getAmount()) ? item.getAmount() : remaining;
			for(int index = 0; index < until; index++) {
				if(preferredIndex >= capacity) {
					break;
				}
				preferredIndex = (preferredIndex > capacity || preferredIndex < 0 || items[preferredIndex] == null) ? preferredIndex : computeFreeIndex();
				if(preferredIndex == -1) {//Couldn't find an empty spot.
					fireCapacityExceededEvent();
					return filled;
				}
				if(!test) {
					Item newItem = new Item(item.getId());
					items[preferredIndex] = newItem;
					size++;
					updateSingle(null, newItem, preferredIndex++, refresh);
				}
				filled++;
			}
		}
		return filled;
	}
	
	/**
	 * Attempts to add {@code items} in bulk into this container.
	 * @param items The {@link Item}s to add.
	 * @return amount of slots filled.
	 */
	public int addAll(Item... items) {
		if(items == null)
			return 0;
		if(items.length == 1) {
			if(!Item.valid(items[0]))
				return 0;
			if(!items[0].getDefinition().isStackable() && items[0].getAmount() == 1)
				return add(items[0]);
		}
		firingEvents = false;
		int added = 0;
		try {
			for(Item item : items) {
				if(item == null)
					continue;
				int filled = add(item, -1, false);
				if(filled != -1)
					added += filled;
			}
		} finally {
			firingEvents = true;
		}
		updateBulk();
		return added;
	}
	
	/**
	 * Looks if all of the items can be added.
	 * @param items items to be added for the check.
	 * @return {@code true} if successful, otherwise {@code false}.
	 */
	public boolean canAdd(Item... items) {
		test();
		firingEvents = false;
		int slots = slotCount(false, false, items);
		int added = 0;
		for(Item item : items) {
			if(item == null)
				continue;
			int filled = add(item, -1, false);
			if(filled == -1) {
				firingEvents = true;
				untest();
				return false;
			}
			added += filled;
		}
		firingEvents = true;
		untest();
		return slots == added;
	}
	
	/**
	 * Attempts to add {@code items} in bulk into this container.
	 * @param items The {@link Item}s to add.
	 * @return amount of slots filled.
	 */
	public int addAll(ItemContainer items) {
		return addAll(items.items);
	}
	
	/**
	 * Attempts to remove {@code item} from this container.
	 * @param item The {@link Item} to remove.
	 * @return amount of slots cleared, -1 if failed, 0 if removed stackable portion.
	 */
	public int remove(Item item) {
		return remove(item, -1, true);
	}
	
	/**
	 * Attempts to remove {@code item} from this container, preferably from {@code preferredIndex}.
	 * @param item           The {@link Item} to remove.
	 * @param preferredIndex The preferable index to remove {@code item} from.
	 * @return amount of slots cleared, -1 if failed, 0 if removed stackable portion.
	 */
	public int remove(Item item, int preferredIndex) {
		return remove(item, preferredIndex, true);
	}
	
	/**
	 * Attempts to remove {@code item} from this container, preferably from {@code preferredIndex}.
	 * @param item           The {@link Item} to remove.
	 * @param preferredIndex The preferable index to remove {@code item} from.
	 * @param refresh        The condition if we will be refreshing our container.
	 * @return amount of slots cleared, -1 if failed, 0 if removed stackable portion.
	 */
	public int remove(Item item, int preferredIndex, boolean refresh) {
		checkArgument(preferredIndex >= -1, "invalid index identifier");
		
		ItemDefinition def = item.getDefinition();
		if(def == null) {
			items[preferredIndex] = null;
			updateSingle(items[preferredIndex], null, preferredIndex, refresh);
			size--;
			return -1;
		}
		boolean stackable = (policy == STANDARD && def.isStackable()) || policy == ALWAYS;
		if(stackable) {
			preferredIndex = computeIndexForId(item.getId());
		} else {
			preferredIndex = preferredIndex == -1 ? computeIndexForId(item.getId()) : preferredIndex;
			if(preferredIndex != -1 && items[preferredIndex] == null) {
				preferredIndex = -1;
			}
		}
		
		if(preferredIndex == -1) { // Item isn't present within this container.
			return -1;
		}
		
		int cleared = 0;
		if(stackable) {
			Item current = items[preferredIndex];
			if(current.getAmount() > item.getAmount()) {
				if(!test && item.getId() == current.getId())
					items[preferredIndex] = current.createAndDecrement(item.getAmount());
			} else {
				if(!test && item.getId() == current.getId()) {
					items[preferredIndex] = null;
					size--;
				}
				cleared++;
			}
			updateSingle(current, items[preferredIndex], preferredIndex, refresh);
		} else {
			int until = computeAmountForId(item.getId());
			until = (item.getAmount() > until) ? until : item.getAmount();
			
			for(int index = 0; index < until && index < capacity; index++) {
				if(preferredIndex < 0 || preferredIndex >= capacity) {
					preferredIndex = computeIndexForId(item.getId());
				} else if(items[preferredIndex] == null) {
					preferredIndex = computeIndexForId(item.getId());
				} else if(items[preferredIndex].getId() != item.getId()) {
					preferredIndex = computeIndexForId(item.getId());
				}
				if(preferredIndex == -1) {
					break;
				}
				if(!test) {
					Item oldItem = items[preferredIndex];
					items[preferredIndex] = null;
					updateSingle(oldItem, null, preferredIndex++, refresh);
					size--;
				}
				cleared++;
			}
		}
		return cleared;
	}
	
	/**
	 * Attempts to remove {@code items} in bulk from this container.
	 * @param items The {@link Item}s to remove.
	 * @return amount of slots cleared.
	 */
	public int removeAll(Item... items) {
		if(items == null)
			return 0;
		if(items.length == 1) {
			if(!Item.valid(items[0]))
				return 0;
			if(!items[0].getDefinition().isStackable() && items[0].getAmount() == 1)
				return remove(items[0]);
		}
		firingEvents = false;
		int removed = 0;
		try {
			for(Item item : items) {
				if(item == null)
					continue;
				int cleared = remove(item, -1, false);
				removed += cleared;
			}
		} finally {
			firingEvents = true;
		}
		updateBulk();
		return removed;
	}
	
	/**
	 * Looks if all of the items can be removed.
	 * @param items items to be removed for the check.
	 * @return {@code true} if successful, otherwise {@code false}.
	 */
	public boolean canRemove(Item... items) {
		test();
		firingEvents = false;
		int slots = slotCount(true, false, items);
		int removed = 0;
		for(Item item : items) {
			if(item == null)
				continue;
			int cleared = remove(item, -1, false);
			if(cleared == 0)
				cleared = 1;//can be removed.
			if(cleared == -1) {
				firingEvents = true;
				untest();
				return false;
			}
			removed += cleared;
		}
		firingEvents = true;
		untest();
		return slots == removed;
	}
	
	/**
	 * Attempts to remove {@code items} in bulk from this container.
	 * @param items The {@link Item}s to remove.
	 * @return amount of slots cleared.
	 */
	public int removeAll(ItemContainer items) {
		return removeAll(items.items);
	}
	
	/**
	 * Computes the next free ({@code null}) index in this container.
	 * @return The free index, {@code -1} if no free indexes could be found.
	 */
	public final int computeFreeIndex() {
		for(int index = 0; index < capacity; index++) {
			if(items[index] == null) {
				return index;
			}
		}
		return -1;
	}
	
	/**
	 * Computes the first index found that {@code id} is in.
	 * @param id The identifier to compute for.
	 * @return The first index found, {@code -1} if no {@link Item} with {@code id} is in this container.
	 */
	public final int computeIndexForId(int id) {
		int found = 0;
		for(int index = 0; index < capacity; index++) {
			if(items[index] == null)
				continue;
			found++;
			if(items[index].getId() == id) {
				return index;
			}
			if(found == size)
				break;
		}
		return -1;
	}
	
	/**
	 * Computes the total quantity of the {@link Item}s in this container with {@code id}.
	 * @param id The identifier of the {@code Item} to determine the total quantity of.
	 * @return The total quantity.
	 */
	public final int computeAmountForId(int id) {
		int amount = 0;
		int found = 0;
		for(Item item : items) {
			if(item == null)
				continue;
			if(item.getId() == id)
				amount += item.getAmount();
			found++;
			if(found == size)
				break;
		}
		return amount;
	}
	
	/**
	 * Computes the identifier of the {@link Item} on {@code index}.
	 * @param index The index to compute the identifier for.
	 * @return The identifier wrapped in an optional.
	 */
	public final int computeIdForIndex(int index) {
		return computeOptionalIdForIndex(index).orElse(-1);
	}
	
	public final Optional<Integer> computeOptionalIdForIndex(int index) {
		return retrieve(index).map(Item::getId);
	}
	
	/**
	 * Replaces the first occurrence of the {@link Item} having the identifier {@code oldId} with {@code newId}.
	 * @param oldId   The old identifier to replace.
	 * @param newId   The new identifier to replace.
	 * @param refresh The condition if the coontainer will be refreshed.
	 * @return {@code true} if the replace operation was successful, {@code false otherwise}.
	 */
	public final boolean replace(int oldId, int newId, boolean refresh) {
		if(test)
			return false;
		int index = computeIndexForId(oldId);
		if(index == -1) {
			return false;
		}
		Item oldItem = items[index];
		Item newItem = oldItem.createWithId(newId);
		return slotCount(false, false, oldItem) == remove(oldItem, index, refresh) && slotCount(false, false, newItem) == add(newItem, index, refresh);
	}
	
	/**
	 * Replaces all occurrences of {@link Item}s having the identifier {@code oldId} with {@code newId}.
	 * @param oldId The old identifier to replace.
	 * @param newId The new identifier to replace.
	 * @return {@code true} if the replace operation was successful at least once, {@code false otherwise}.
	 */
	public final boolean replaceAll(int oldId, int newId) {
		if(test)
			return false;
		boolean replaced = false;
		firingEvents = false;
		try {
			while(replace(oldId, newId, false)) {
				replaced = true;
			}
		} finally {
			firingEvents = true;
		}
		updateBulk();
		return replaced;
	}
	
	/**
	 * Computes the amount of indexes required to hold {@code items} in this container.
	 * @param raw      The flag if container beholds aren't considered.
	 * @param negate   if the stacking changes must be negated.
	 * @param forItems The items to compute the index count for.
	 * @return The slot count the items would take in this container.
	 */
	public final int slotCount(boolean raw, boolean negate, Item... forItems) {
		if(forItems == null)
			return 0;
		int indexCount = 0;
		for(Item item : forItems) {
			if(item == null)
				continue;
			ItemDefinition def = item.getDefinition();
			if(def == null)
				continue;
			boolean stackable = (policy == STANDARD && def.isStackable()) || policy == ALWAYS;
			if(stackable) {
				if(raw) {
					indexCount++;
					continue;
				}
				int index = computeIndexForId(item.getId());
				if(index == -1) {
					indexCount++;
					continue;
				}
				Item existing = items[index];
				if((existing.getAmount() + (negate ? -item.getAmount() : item.getAmount())) <= 0) {
					indexCount++;
				}
			} else {
				indexCount += item.getAmount();
			}
		}
		return indexCount;
	}
	
	/**
	 * Determines if this container has the capacity for {@code item}.
	 * @param item The {@link Item} to determine this for.
	 * @return {@code true} if {@code item} can be added, {@code false} otherwise.
	 */
	public final boolean hasCapacityFor(Item... item) {
		return hasCapacityFor(0, item);
	}
	
	/**
	 * Determines if this container has the capacity for {@code item}.
	 * @param cleared The amount of items cleared taken in consideration.
	 * @param item    The {@link Item} to determine this for.
	 * @return {@code true} if {@code item} can be added, {@code false} otherwise.
	 */
	public final boolean hasCapacityFor(int cleared, Item... item) {
		int indexCount = slotCount(false, false, item);
		return remaining() >= indexCount - cleared;
	}
	
	/**
	 * Creates a copy of the underlying container and removes the items specified from it
	 * and after tries to deposit the specified items to it.
	 * @param add    the items to deposit to this container.
	 * @param remove the items that should be removed before adding.
	 * @return {@code true} if {@code item} can be added, {@code false} otherwise.
	 */
	public final boolean hasCapacityAfter(Item[] add, Item... remove) {
		test();
		int cleared = removeAll(remove);
		untest();
		return hasCapacityFor(cleared, add);
	}
	
	/**
	 * Determines if this container contains {@code id}.
	 * @param id The identifier to check this container for.
	 * @return {@code true} if this container has {@code id}, {@code false} otherwise.
	 */
	public final boolean contains(int id) {
		return computeIndexForId(id) != -1;
	}
	
	/**
	 * Determines if this container contains all {@code identifiers}.
	 * @param identifiers The identifiers to check this container for.
	 * @return {@code true} if this container has all {@code identifiers}, {@code false} otherwise.
	 */
	public final boolean containsAll(int... identifiers) {
		for(int id : identifiers) {
			boolean found = false;
			for(Item item : items) {
				if(item == null)
					continue;
				if(item.getId() == id) {
					found = true;
					break;
				}
			}
			if(!found)
				return false;
		}
		return true;
	}
	
	/**
	 * Determines if this container contains all {@code items}.
	 * @param check The items to check this container for.
	 * @return {@code true} if this container has all {@code identifiers}, {@code false} otherwise.
	 */
	public final boolean containsAll(Item... check) {
		for(Item it : check) {
			if(it == null)
				continue;
			int am = it.getAmount();
			for(Item item : items) {
				if(item == null)
					continue;
				if(item.getId() == it.getId()) {
					am -= item.getAmount();
					if(am <= 0)
						break;
				}
			}
			if(am > 0)
				return false;
		}
		return true;
	}
	
	/**
	 * Determines if this container contains any {@code identifiers}.
	 * @param identifiers The identifiers to check this container for.
	 * @return {@code true} if this container has any {@code identifiers}, {@code false} otherwise.
	 */
	public final boolean containsAny(int... identifiers) {
		for(int id : identifiers) {
			for(Item item : items) {
				if(item == null)
					continue;
				if(item.getId() == id)
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Determines if this container contains any {@code check}.
	 * @param check The items to check this container for.
	 * @return {@code true} if this container has any {@code identifiers}, {@code false} otherwise.
	 */
	public final boolean containsAny(Item... check) {
		for(Item it : check) {
			if(it == null)
				continue;
			for(Item item : items) {
				if(item == null)
					continue;
				if(item.getId() == it.getId() && item.getAmount() >= it.getAmount())
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Determines if this container contains {@code item}.
	 * @param item The {@link Item} to check this container for.
	 * @return {@code true} if this container has {@code item}, {@code false} otherwise.
	 */
	public final boolean contains(Item item) {
		int amount = item.getAmount();
		for(Item i : items) {
			if(i != null && i.getId() == item.getId()) {
				amount -= i.getAmount();
			}
			if(amount <= 0)
				return true;
		}
		return false;
	}
	
	/**
	 * Sends the items on the interface.
	 * @param player The player sending the update to.
	 * @param widget The widget to send the {@code Item}s on.
	 */
	public final void refreshBulk(Player player, int widget) {
		player.out(new SendContainer(widget, this));
	}
	
	/**
	 * Sends one item on the interface at a specific slot.
	 * @param player The player sending the update to.
	 * @param widget The widget to send the {@code Item}s on.
	 * @param slot   The slot id of the new item to be sent.
	 */
	public final void refreshSingle(Player player, int widget, int slot) {
		player.out(new SendItemOnInterfaceSlot(widget, items[slot], slot));
	}
	
	/**
	 * Swaps the {@link Item}s on {@code oldIndex} and {@code newIndex}.
	 * @param oldIndex The old index.
	 * @param newIndex The new index.
	 */
	public final void swap(int oldIndex, int newIndex) {
		swap(false, oldIndex, newIndex, true);
	}
	
	/**
	 * Swaps the {@link Item}s on {@code oldIndex} and {@code newIndex}.
	 * @param insert   If the {@code Item} should be inserted.
	 * @param oldIndex The old index.
	 * @param newIndex The new index.
	 * @param refresh  The condition that determines if we will refresh the container.
	 */
	public final void swap(boolean insert, int oldIndex, int newIndex, boolean refresh) {
		checkArgument(oldIndex >= 0 && oldIndex < capacity, "oldIndex out of range");
		checkArgument(newIndex >= 0 && oldIndex < capacity, "newIndex out of range");
		
		if(insert) {
			if(newIndex > oldIndex) {
				for(int index = oldIndex; index < newIndex; index++) {
					swap(false, index, index + 1, false);
				}
			} else if(oldIndex > newIndex) {
				for(int index = oldIndex; index > newIndex; index--) {
					swap(false, index, index - 1, false);
				}
			}
			if(refresh)
				updateBulk();
		} else {
			Item itemOld = items[oldIndex];
			Item itemNew = items[newIndex];
			
			//precaution, dragging same stackable item.
			if(itemOld != null && itemNew != null && oldIndex != newIndex) {
				if(itemNew.getId() == itemOld.getId()) {
					if(itemOld.getDefinition() != null && itemNew.getDefinition() != null) {
						if(itemOld.getDefinition().isStackable() && itemNew.getDefinition().isStackable()) {
							itemNew = new Item(itemNew.getId(), itemNew.getAmount() + itemOld.getAmount());
							itemOld = null;
						}
					}
				}
			}
			
			items[oldIndex] = itemNew;
			items[newIndex] = itemOld;
			
			updateSingle(itemOld, items[oldIndex], oldIndex, refresh);
			updateSingle(itemNew, items[newIndex], newIndex, refresh);
		}
	}
	
	/**
	 * Transfers the item in {@code slot} to {@code newSlot}. If an item already
	 * exists in the new slot, the items in this container will be shifted to
	 * accommodate for the transfer.
	 * @param slot    the slot of the item to transfer.
	 * @param newSlot the slot to transfer the item to.
	 * @return {@code true} if the transfer was successful, {@code false}
	 * otherwise.
	 */
	public boolean transfer(int slot, int newSlot) {
		Item from = items[slot];
		if(from == null) {
			return false;
		}
		items[slot] = null;
		if(slot > newSlot) {
			int shiftTo = slot;
			for(int i = (newSlot + 1); i < slot; i++) {
				if(items[i] == null) {
					shiftTo = i;
					break;
				}
			}
			Item[] slice = new Item[shiftTo - newSlot];
			System.arraycopy(items, newSlot, slice, 0, slice.length);
			System.arraycopy(slice, 0, items, newSlot + 1, slice.length);
		} else {
			int sliceStart = slot + 1;
			for(int i = (newSlot - 1); i >= sliceStart; i--) {
				if(items[i] == null) {
					sliceStart = i;
					break;
				}
			}
			Item[] slice = new Item[newSlot - sliceStart + 1];
			System.arraycopy(items, sliceStart, slice, 0, slice.length);
			System.arraycopy(slice, 0, items, sliceStart - 1, slice.length);
		}
		items[newSlot] = from;
		return true;
	}
	
	/**
	 * Shifts {@link Item}s to the left to fill any empty ({@code null}) indexes.
	 */
	public final void shift() {
		Item[] newItems = new Item[items.length];
		if(size() == 0) {
			return;
		}
		int count = 0;
		boolean update = false;
		for(Item item : items) {
			if(item == null) {
				update = true;
				continue;
			}
			newItems[count++] = item.copy();
		}
		if(update)
			reformat(newItems);
	}
	
	/**
	 * Determines if this item container is empty.
	 * @return {@code true} if this container is, {@code false} otherwise.
	 */
	public final boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Determines if this item container is full.
	 * @return {@code true} if this container is, {@code false} otherwise.
	 */
	public final boolean isFull() {
		return remaining() == 0;
	}
	
	/**
	 * Adds an {@link ItemContainerListener} to this container.
	 * @param listener The listener to add to this container.
	 * @return {@code true} if the listener was added, {@code false} otherwise.
	 */
	public final boolean addListener(ItemContainerListener listener) {
		return listeners.add(listener);
	}
	
	/**
	 * Removes an {@link ItemContainerListener} from this container.
	 * @param listener The listener to remove from this container.
	 * @return {@code true} if the listener was removed, {@code false} otherwise.
	 */
	public final boolean removeListener(ItemContainerListener listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Fires the {@code ItemContainerListener.bulkUpdate(ItemContainer)} event.
	 */
	public final void updateBulk() {
		if(firingEvents) {
			listeners.forEach(evt -> {
				evt.bulkUpdate(this);
			});
		}
	}
	
	/**
	 * Fires the {@code ItemContainerListener.singleUpdate(ItemContainer, int)} event.
	 */
	public final void updateSingle(Item oldItem, Item newItem, int index, boolean refresh) {
		if(firingEvents && !test) {
			listeners.forEach(evt -> {
				evt.singleUpdate(this, oldItem, newItem, index, refresh);
			});
		}
	}
	
	/**
	 * Fires the {@code ItemContainerListener.capacityExceeded(ItemContainer)} event.
	 */
	public final void fireCapacityExceededEvent() {
		if(firingEvents) {
			listeners.forEach(evt -> evt.capacityExceeded(this));
		}
	}
	
	/**
	 * Removes all of the items from this container.
	 */
	public final void clear() {
		clear(true);
	}
	
	/**
	 * Removes all of the items from this container.
	 */
	public final void clear(boolean refresh) {
		for(int i = 0; i < items.length; i++) {
			items[i] = null;
		}
		size = 0;
		if(refresh)
			updateBulk();
	}
	
	/**
	 * @return The item array in this container.
	 */
	public Item[] getItems() {
		return items;
	}
	
	/**
	 * Fills the container of items to {@code items}. The container will not hold
	 * any references to the array, nor the item instances in the array.
	 * @param items the new array of items, the capacities of this must be equal
	 *              to or lesser than the container.
	 */
	public final void fillItems(Item[] items) {
		Preconditions.checkArgument(items.length <= capacity);
		clear(false);
		for(int i = 0; i < items.length; i++) {
			if(items[i] != null)
				size++;
			this.items[i] = items[i] == null ? null : items[i].copy();
		}
		updateBulk();
	}
	
	/**
	 * Reformatting the container, keeping same item array but different indexing.
	 * @param items the new array item setting.
	 */
	public final void reformat(Item[] items) {
		System.arraycopy(items, 0, this.items, 0, items.length);
		updateBulk();
	}
	
	/**
	 * Retrieves the item located on {@code index}.
	 * @param index the index to get the item on.
	 * @return the item on the index, or {@code null} if no item exists on the index.
	 */
	public final Optional<Item> retrieve(int index) {
		if(index == -1 || index >= items.length)
			return Optional.empty();
		return Optional.ofNullable(items[index]);
	}
	
	/**
	 * Gets the {@link Item} located on {@code index}.
	 * @param index The index to get the {@code Item} on.
	 * @return The {@code Item} instance, {@code null} if the index is empty.
	 */
	public final Item get(int index) {
		return retrieve(index).orElse(null);
	}
	
	/**
	 * Gets the item id located on {@code index}.
	 * @param index The index to get the {@code Item} on.
	 * @return The {@code Item} instance, {@code null} if the index is empty.
	 */
	public final int getId(int index) {
		if(items[index] == null) {
			return -1;
		}
		return items[index].getId();
	}
	
	/**
	 * Gets the slot index by the item id.
	 * @param id item id searching the {@code Item} for.
	 * @return the slot id found, otherwise -1.
	 */
	public final int getSlot(int id) {
		for(int i = 0; i < items.length; i++) {
			if(items[i] != null && items[i].getId() == id)
				return i;
		}
		return -1;
	}
	
	/**
	 * Sets the {@code index} to {@code item}.
	 * @param index   The index to set.
	 * @param item    The {@link Item} to set on the index.
	 * @param refresh The condition if the container must be refreshed.
	 */
	public final void set(int index, Item item, boolean refresh) {
		Item oldItem = items[index];
		items[index] = item;
		if(item == null)
			size--;
		updateSingle(oldItem, items[index], index, refresh);
	}
	
	/**
	 * Sets the container of items to {@code newItems}. The only difference between this and {@code fillItems(Item[])} is that
	 * this method takes a wrapper class named {@link IndexedItem} that holds the index of items.
	 * @param newItems The new array of items.
	 */
	public final void setIndexedItems(IndexedItem[] newItems) {
		Arrays.fill(items, null);
		for(IndexedItem item : newItems) {
			items[item.getIndex()] = new Item(item.getId(), item.getAmount());
		}
	}
	
	/**
	 * Returns an array of {@link IndexedItem}s describing the contents of the backing array. Changes made to the returned
	 * array will, of course, not be reflected on the backing array.
	 * @return An array of {@code IndexedItem}s describing the contents of the backing array.
	 */
	public final IndexedItem[] toIndexedArray() {
		ObjectList<IndexedItem> indexedItems = new ObjectArrayList<>();
		for(int index = 0; index < capacity; index++) {
			Item item = items[index];
			if(item == null) {
				continue;
			}
			indexedItems.add(new IndexedItem(item, index));
		}
		return indexedItems.toArray(new IndexedItem[indexedItems.size()]);
	}
	
	/**
	 * @return {@code true} if events are currnetly being fired, {@code false otherwise}.
	 */
	public boolean isFiringEvents() {
		return firingEvents;
	}
	
	/**
	 * Sets the value for {@link #firingEvents}.
	 */
	public void setFiringEvents(boolean firingEvents) {
		this.firingEvents = firingEvents;
	}
	
	/**
	 * @return The amount of remaining free indexes.
	 */
	public final int remaining() {
		return capacity - size();
	}
	
	/**
	 * @return The amount of used indexes.
	 */
	public final int size() {
		return size;
	}
	
	/**
	 * @return The total amount of used and free indexes.
	 */
	public final int capacity() {
		return capacity;
	}
	
	/**
	 * @return The policy this container follows.
	 */
	public final StackPolicy policy() {
		return policy;
	}
	
	/**
	 * A widget id to refresh this container.
	 * @return -1 for no widgets.
	 */
	public int widget() {
		return -1;
	}
	
	/**
	 * @return {@link #test}.
	 */
	public boolean isTest() {
		return test;
	}
	
	/**
	 * Sets the test flag to true.
	 */
	public void test() {
		test = true;
	}
	
	/**
	 * Sets the tast flag to false.
	 */
	public void untest() {
		test = false;
	}
	
}