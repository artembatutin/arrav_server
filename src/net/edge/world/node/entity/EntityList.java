package net.edge.world.node.entity;

import net.edge.net.database.connection.use.Highscores;
import net.edge.world.World;
import net.edge.world.content.PlayerPanel;
import net.edge.world.node.NodeState;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.region.Region;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.*;

/**
 * An {@link Iterable} implementation acting as a repository that holds instances of {@link EntityNode}s. Indexes are
 * cached to avoid expensive lookups whenever a new entity is added.
 * @param <E> The specific type of {@code EntityNode} being managed within this list.
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author lare96 <http://github.org/lare96>
 */
public final class EntityList<E extends EntityNode> implements Iterable<E> {
	
	/**
	 * An {@link Iterator} implementation designed specifically {@link EntityList}s.
	 * @param <E> The specific type of {@link EntityNode} being managed within this {@code Iterator}.
	 * @author lare96 <http://github.org/lare96>
	 */
	public static final class EntityListIterator<E extends EntityNode> implements Iterator<E> {
		
		/**
		 * The {@link EntityList} this {@link Iterator} is dedicated to.
		 */
		private final EntityList<E> list;
		
		/**
		 * The current index.
		 */
		private int curr;
		
		/**
		 * The previous index.
		 */
		private int prev = -1;
		
		/**
		 * Creates a new {@link EntityListIterator}.
		 * @param list The {@link EntityList} this {@link Iterator} is dedicated to.
		 */
		public EntityListIterator(EntityList<E> list) {
			this.list = list;
		}
		
		@Override
		public boolean hasNext() {
			return curr < list.capacity() && skipNullIndexes();
		}
		
		@Override
		public E next() {
			skipNullIndexes();
			checkPositionIndex(curr, list.capacity(), "No elements left");
			E entity = list.element(curr);
			prev = curr++;
			return entity;
		}
		
		@Override
		public void remove() {
			checkState(prev != -1, "remove() can only be called once after each call to next()");
			
			list.remove(list.get(prev));
			prev = -1;
		}
		
		/**
		 * Forwards the {@code curr} marker until a {@code non-null} element is found.
		 * @return {@code true} if a non-null element is found, {@code false} otherwise.
		 */
		private boolean skipNullIndexes() {
			while(list.get(curr) == null) {
				if(++curr >= list.capacity()) {
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * The entities contained within this list.
	 */
	private final E[] entities;
	
	/**
	 * A queue that acts as a cache for indexes.
	 */
	private final Queue<Integer> indexes;
	
	/**
	 * The internal size of this list.
	 */
	private int size;
	
	/**
	 * Creates a new {@link EntityList}.
	 * @param capacity The length of the backing array plus {@code 1}.
	 */
	@SuppressWarnings("unchecked")
	public EntityList(int capacity) {
		entities = (E[]) new EntityNode[++capacity];
		
		Stream<Integer> indexStream = IntStream.rangeClosed(1, entities.length).boxed();
		indexes = new ArrayDeque<>(indexStream.collect(Collectors.toList()));
	}
	
	@Override
	public Iterator<E> iterator() {
		return new EntityListIterator<>(this);
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
	public List<E> findAll(Predicate<? super E> filter) {
		List<E> list = new ArrayList<>();
		for(E e : this) {
			if(filter.test(e)) {
				list.add(e);
			}
		}
		return list;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * As a rule of thumb, {@code stream()} and {@code parallelStream()} should always be used instead unless absolutely
	 * needed.
	 */
	@Override
	public Spliterator<E> spliterator() {
		return Spliterators.spliterator(entities, Spliterator.ORDERED | Spliterator.DISTINCT);
	}
	
	/**
	 * Adds {@code entity} to this list. Will throw an exception if this list is full, or if the entity being added has a state of
	 * {@code ACTIVE}.
	 * @param entity The entity to add to this list.
	 */
	public boolean add(E entity) {
		checkArgument(entity.getState() != NodeState.ACTIVE, "state == ACTIVE");
		checkState(!isFull(), "isFull() == true");
		int index = indexes.remove();
		entities[index] = entity;
		entity.setSlot(index);
		entity.setState(NodeState.ACTIVE);
		if(entity.isPlayer())
			PlayerPanel.PLAYERS_ONLINE.refreshAll("@or2@ - Players online: @yel@" + World.getPlayers().size());
		if(entity.isNpc()) {
			Region reg = entity.getRegion();
			if(reg != null && reg.getState() == NodeState.ACTIVE)
				entity.toNpc().setActive(true);
		}
		size++;
		return true;
	}
	
	/**
	 * Removes {@code entity} from this list. Will throw an exception if the entity being removed does not have a state of {@code
	 * ACTIVE}.
	 * @param entity The entity to remove from this list.
	 */
	public boolean remove(E entity) {
		if(entity.getState() != NodeState.ACTIVE)
			return true;
		checkArgument(entity.getSlot() != -1, "index == -1");
		indexes.add(entity.getSlot());
		entity.setState(NodeState.INACTIVE);
		entities[entity.getSlot()] = null;
		size--;
		if(entity.isPlayer()) {
			Player player = entity.toPlayer();
			if(player.getRights() != Rights.DEVELOPER && player.getRights() != Rights.ADMINISTRATOR)
				new Highscores(World.getScore(), player).submit();
			player.getSession().flushQueue();
			player.getSession().getChannel().close();
			
		}
		return true;
	}
	
	/**
	 * Removes a {@link EntityNode} from this list at {@code index}. Will throw an exception if the entity being removed does
	 * not have a state of {@code ACTIVE}.
	 * @param index The index to remove the {@link EntityNode} at.
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
		checkArgument(entity != null, "index -> null EntityNode");
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
		return capacity() - size();
	}
	
	/**
	 * @return The length of the backing array.
	 */
	public int capacity() {
		return entities.length;
	}
	
	/**
	 * <strong>Please note that this function does not give direct access to the backing array but instead creates a shallow
	 * copy.</strong>
	 * @return The shallow copy of the backing array.
	 */
	public E[] toArray() {
		return Arrays.copyOf(entities, entities.length);
	}
	
	/**
	 * Calls {@code remove()} on every single {@link EntityNode} in this list.
	 */
	public void clear() {
		forEach(this::remove);
	}
	
	/**
	 * @return The {@link Stream} that will traverse over this list. Automatically excludes {@code null} values.
	 */
	public Stream<E> stream() {
		return StreamSupport.stream(spliterator(), false).filter(Objects::nonNull);
	}
	
	/**
	 * @return The {@link Stream} that will traverse over this list in parallel. Automatically excludes {@code null} values.
	 */
	public Stream<E> parallelStream() {
		Spliterator<E> split = Spliterators.spliterator(entities, spliterator().characteristics() | Spliterator.IMMUTABLE);
		return StreamSupport.stream(split, true).filter(Objects::nonNull);
	}
}