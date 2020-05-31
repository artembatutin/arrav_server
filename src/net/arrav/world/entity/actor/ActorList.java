package net.arrav.world.entity.actor;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.arrav.content.PlayerPanel;
import net.arrav.net.database.connection.use.Hiscores;
import net.arrav.world.World;
import net.arrav.world.entity.EntityState;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.entity.region.Region;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * An {@link Iterable} implementation acting as a repository that holds instances of {@link Actor}s. Indexes are
 * cached to avoid expensive lookups whenever a new entity is added.
 * @param <E> The specific type of {@code Actor} being managed within this list.
 * @author Artem Batutin
 * @author lare96 <http://github.org/lare96>
 */
public class ActorList<E extends Actor> implements Iterable<E> {
	
	/**
	 * An {@link Iterator} implementation designed specifically {@link ActorList}s.
	 * @param <E> The specific type of {@link Actor} being managed within this {@code Iterator}.
	 * @author Artem Batutin
	 */
	public final class EntityListIterator<E extends Actor> implements Iterator<E> {
		
		/**
		 * The {@link ActorList} this {@link Iterator} is dedicated to.
		 */
		private final ActorList<E> list;
		
		/**
		 * The cursor.
		 */
		private int cursor;
		
		/**
		 * The last index found.
		 */
		private int last = -1;
		
		/**
		 * Creates a new {@link EntityListIterator}.
		 * @param list The {@link ActorList} this {@link Iterator} is dedicated to.
		 */
		public EntityListIterator(ActorList<E> list) {
			this.list = list;
		}
		
		public void reset() {
			cursor = 0;
		}
		
		@Override
		public boolean hasNext() {
			return size > 0 && cursor <= limit;
		}
		
		@Override
		public E next() {
			return list.entities[cursor++];
		}
		
		@Override
		public void remove() {
			if(last == -1) {
				throw new IllegalStateException("remove() may only be called once per call to next()");
			}
			list.remove(last);
			last = -1;
		}
	}
	
	/**
	 * A {@link IntArrayFIFOQueue} of available indices.
	 */
	private final Queue<Integer> indices = new PriorityQueue<>();
	
	/**
	 * The entities contained within this list.
	 */
	private final E[] entities;
	
	/**
	 * The capacity of this repository.
	 */
	private final int capacity;
	
	/**
	 * The iterator limit.
	 */
	private int limit;
	
	/**
	 * The internal size of this list.
	 */
	private int size = 0;
	
	/**
	 * Creates a new {@link ActorList}.
	 * @param capacity The length of the backing array plus {@code 1}.
	 */
	@SuppressWarnings("unchecked")
	public ActorList(int capacity) {
		this.capacity = capacity;
		entities = (E[]) Array.newInstance(Actor.class, capacity);
		IntStream.rangeClosed(0, capacity).boxed().forEachOrdered(indices::offer);
	}
	
	@Override
	public Iterator<E> iterator() {
		return new EntityListIterator<>(this);
	}


	/**
	 * Returns a sequential stream with this collection as its source.
	 *
	 * @return a sequential stream over the elements in this collection.
	 */
	public Stream<E> stream() {
		return Arrays.stream(entities);
	}

	/**
	 * Finds the first element that matches {@code filter}.
	 * @param filter The filter to apply to the elements of this sequence.
	 * @return An {@link Optional} containing the element, or an empty {@code Optional} if no element was found.
	 */
	public Optional<E> findFirst(Predicate<? super E> filter) {
		for(E e : this) {
			if(filter.test(e)) {
				return Optional.of(e);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Finds the last element that matches {@code filter}.
	 * @param filter The filter to apply to the elements of this sequence.
	 * @return An {@link Optional} containing the element, or an empty {@code Optional} if no element was found.
	 */
	public Optional<E> findLast(Predicate<? super E> filter) {
		for(int index = capacity(); index > 1; index--) {
			E entity = entities[index];
			if(entity == null) {
				continue;
			}
			if(filter.test(entity)) {
				return Optional.of(entity);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Finds all elements that match {@code filter}.
	 * @param filter The filter to apply to the elements of this sequence.
	 * @return An {@link ArrayList} containing the elements.
	 */
	public ObjectList<E> findAll(Predicate<? super E> filter) {
		ObjectList<E> list = new ObjectArrayList<>();
		for(E e : this) {
			if(filter.test(e)) {
				list.add(e);
			}
		}
		return list;
	}
	
	/**
	 * Adds {@code actor} to this list. Will throw an exception if this list is full, or if the actor being added has a state of
	 * {@code ACTIVE}.
	 * @param actor The actor to add to this list.
	 */
	public boolean add(E actor) {
		if(actor.getState() == EntityState.ACTIVE)
			return false;
		if(size == capacity())
			return false;
		Integer index = indices.poll();
		if(entities[index] != null)
			return false;
		if(index >= limit) {
			limit++;
		}
		entities[index] = actor;
		actor.setSlot(index + 1);
		try {
			actor.setState(EntityState.ACTIVE);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			if(actor.isPlayer()) {//thread safe
				Region r = actor.getRegion();
				if(r != null) {
					r.add(actor);
				}
			} else {
				World.get().add(actor);
			}
			//Activating npc if region active.
			if(actor.isMob()) {
				Region r = actor.getRegion();
				if(r != null) {
					if(r.getState() == EntityState.ACTIVE)
						actor.toMob().setActive(true);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		size++;
		return true;
	}
	
	/**
	 * Removes {@code actor} from this list.
	 * @param actor The actor to remove from this list.
	 */
	public boolean remove(E actor) {
		if(actor.getState() != EntityState.ACTIVE && actor.getState() != EntityState.AWAITING_REMOVAL) {
			//System.out.println("Couldn't remove: " + actor.toString() + " because not active.");
			return true;
		}
		if(actor.getSlot() == -1) {
			//System.out.println("Couldn't remove: " + actor.toString() + " because of slot.");
			return true;
		}
		int index = actor.getSlot();
		int normal = index - 1;
		if(actor.getSlot() != -1) {
			indices.offer(normal);
			entities[normal] = null;
			if(size > -1)
				size--;
		}
		if(index >= limit) {
			limit--;
		}
		try {
			actor.setState(EntityState.INACTIVE);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			World.get().remove(actor);
			if(actor.isPlayer()) {
				Player player = actor.toPlayer();
				if(player.getRights() != Rights.ADMINISTRATOR)
					new Hiscores(World.getScore(), player).submit();
				if(player.getRights().isStaff()) {
					World.get().setStaffCount(World.get().getStaffCount() - 1);
					PlayerPanel.STAFF_ONLINE.refreshAll("@or3@ - Staff online: @yel@" + World.get().getStaffCount());
				}
				player.getSession().getChannel().close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Removes a {@link Actor} from this list at {@code index}. Will throw an exception if the entity being removed does
	 * not have a state of {@code ACTIVE}.
	 * @param index The index to remove the {@link Actor} at.
	 */
	public void remove(int index) {
		remove(entities[index]);
	}
	
	/**
	 * Retrieves the element on {@code index}.
	 * @param index The index.
	 * @return The retrieved element, possibly {@code null}.
	 */
	public E get(int index) {
		return entities[index];
	}
	
	/**
	 * Retrieves the element on {@code index}, the only difference between this and {@code get(int)} is that this method
	 * throws an exception if no entity is found on {@code index}.
	 * @param index The index.
	 * @return The retrieved element, will never be {@code null}.
	 */
	public E element(int index) {
		E entity = get(index);
		checkArgument(entity != null, "index -> null Actor");
		return entity;
	}
	
	/**
	 * Determines if this list contains {@code entity}.
	 * @param entity The entity to check for.
	 * @return {@code true} if {@code entity} is contained, {@code false} otherwise.
	 */
	public boolean contains(E entity) {
		return get(entity.getSlot()) != null;
	}
	
	/**
	 * @return {@code true} if this list is full, {@code false} otherwise.
	 */
	public boolean isFull() {
		return size() == capacity();
	}
	
	/**
	 * @return {@code true} if this list is empty, {@code false} otherwise.
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * @return The amount of entities in this list.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * @return The amount of free spaces remaining in this list.
	 */
	public int remaining() {
		return capacity() - size() - 1;
	}
	
	/**
	 * @return The length of the backing array.
	 */
	public int capacity() {
		return capacity;
	}
	
	/**
	 * <strong>Please note that this function does not give direct access to the backing array but instead creates a shallow
	 * copy.</strong>
	 * @return The shallow copy of the backing array.
	 */
	public E[] toArray() {
		return Arrays.copyOf(entities, capacity);
	}
	
	/**
	 * Calls {@code remove()} on every single {@link Actor} in this list.
	 */
	public void clear() {
		forEach(this::remove);
	}
	
}