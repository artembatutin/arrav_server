package net.edge.content.minigame.rfd;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.content.market.MarketItem;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * The enumerated type whose elements hold functionality for the data
 * required for the RFD Minigame.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum RFDData {
	WAVE_ONE(1, 3493, 7453),
	WAVE_TWO(2, 3494, 7453, 7454, 7455),
	WAVE_THREE(3, 3495, 7453, 7454, 7455, 7456, 7457),
	WAVE_FOUR(4, 3496, 7453, 7454, 7455, 7456, 7457, 7458, 7459),
	WAVE_FIVE(5, 3491, 7453, 7454, 7455, 7456, 7457, 7458, 7459, 7460, 7461),
	WAVE_SIX(6, -1, 7453, 7454, 7455, 7456, 7457, 7458, 7459, 7460, 7461, 7462);//indicates the rfd minigame has been completed.
	
	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<RFDData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(RFDData.class));
	
	/**
	 * The index of this wave.
	 */
	private final int index;
	
	/**
	 * The npc id summoned for this wave.
	 */
	private final int npcId;

	/**
	 * The items unlocked for completing this wave.
	 */
	private final Item[] ids;
	
	/**
	 * Constructs a new {@link RFDData}.
	 * @param index {@link #index}.
	 * @param npcId {@link #npcId}.
	 * @param ids   {@link #ids}.
	 */
	RFDData(int index, int npcId, int... ids) {
		this.index = index;
		this.npcId = npcId;
		this.ids = Item.convert(ids);
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}
	
	/**
	 * @return the items
	 */
	public Item[] getItems() {
		return ids;
	}
	
	public static Optional<RFDData> valueOf(int index) {
		return VALUES.stream().filter(rfd -> rfd.index == index).findFirst();
	}

	public RFDData getNextOrLast() {
		return valueOf(index + 1).orElse(WAVE_FIVE);
	}
	
	public static boolean isValidNpc(int npcId) {
		return VALUES.stream().anyMatch(rfd -> rfd.npcId == npcId);
	}
	
	private static boolean isValidItem(int itemId) {
		return IntStream.rangeClosed(7453, 7462).anyMatch(id -> id == itemId);
	}
	
	public static boolean canBuy(Player player, MarketItem item) {
		if(!isValidItem(item.getId())) {
			return true;
		}
		
		List<Item> items = Arrays.asList(((RFDData) player.getAttr().get("rfd_wave").get()).ids);
		
		return items.stream().anyMatch(t -> t.getId() == item.getId());
	}
}
