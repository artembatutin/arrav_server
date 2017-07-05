package net.edge.world.node.entity.attribute;

import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.edge.content.combat.weapon.FightType;
import net.edge.content.minigame.rfd.RFDData;
import net.edge.content.skill.cooking.CookingData;
import net.edge.content.skill.crafting.Spinning.SpinningData;
import net.edge.content.skill.fletching.BowCarving;
import net.edge.locale.Position;
import net.edge.world.Direction;
import net.edge.world.node.item.Item;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectType;

import java.util.IdentityHashMap;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * A {@link String} wrapper also known as an alias that defines behavior and functionality for attributes. {@code String}
 * keys are forcibly interned and aliased on startup into an {@link IdentityHashMap} to be easily accessible as well as high
 * performing.
 * <p>
 * <p>
 * The naming convention for all {@code String} keys is {@code lower_underscore}. Spaces and uppercase letters are not
 * allowed.
 * @param <T> The {@link Object} type represented by this key.
 * @author lare96 <http://github.org/lare96>
 */
public final class AttributeKey<T> {
	
	/**
	 * An {@link IdentityHashMap} of {@link String} keys mapped to their {@code AttributeKey} aliases. All {@code String}s
	 * added to this tool.mapviewer are forcibly interned so we can compare them by their identity for faster performance.
	 */
	public static final Object2ObjectArrayMap<String, AttributeKey> ALIASES = new Object2ObjectArrayMap<>();
	
	/**
	 * Aliases all attributes that will be used while Main is online. This is called eagerly on startup.
	 */
	public static void init() {
		//Speciality
		AttributeKey.forPersistent("free_spec_change", false);
		
		//Common
		AttributeKey.forPersistent("accept_aid", true);
		AttributeKey.forPersistent("introduction_stage", 0);
		
		//Minigame
		AttributeKey.forTransient("participation", 0);
		AttributeKey.forPersistent("fight_caves_advanced", false);
		AttributeKey.forPersistent("rfd_wave", RFDData.WAVE_ONE);
		
		//Combat
		AttributeKey.forPersistent("fight_type", FightType.UNARMED_PUNCH);
		AttributeKey.forTransient("master_archery", false);
		
		//Availability
		AttributeKey.forTransient("destroy_item_slot", -1);
		AttributeKey.forTransient("bob", false);
		AttributeKey.forTransient("banking", false);
		AttributeKey.forTransient("fishing", false);
		AttributeKey.forTransient("fletching_bows", false);
		AttributeKey.forTransient("crafting_pots", false);
		AttributeKey.forTransient("crafting_hides", false);
		AttributeKey.forTransient("crafting_glass", false);
		AttributeKey.forTransient("crafting_spin", false);
		
		//Banking
		AttributeKey.forTransient("enter_x_item_tab", -1);
		AttributeKey.forTransient("enter_x_item_slot", -1);
		AttributeKey.forTransient("insert_item", false);
		AttributeKey.forTransient("withdraw_as_note", false);
		
		//Shopping
		AttributeKey.forTransient("shop_item", -1);
		
		//Prayer
		AttributeKey.forPersistent("quick_pray_on", false);
		
		//lunars
		AttributeKey.forTransient("lunar_dream", false);
		AttributeKey.forTransient("lunar_spellbook_swap", false);
		
		//Skills
		AttributeKey.forTransient("goalSettingSkill", -1);
		AttributeKey.forPersistent("slayer_tasks", 0);
		AttributeKey.forTransient("cooking_data", CookingData.SHRIMP);
		AttributeKey.forTransient("cooking_object", new DynamicObject(-1, new Position(0, 0), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, 0));
		AttributeKey.forTransient("cooking_usingStove", false);
		AttributeKey.forTransient("creating_dough", false);
		AttributeKey.forTransient("crafting_potfired", false);
		AttributeKey.forTransient("crafting_spinning", SpinningData.WOOL);
		AttributeKey.forTransient("crafting_hide", -1);
		AttributeKey.forTransient("fletching_bowcarving", new BowCarving(null, null, false));
		AttributeKey.forTransient("smithing_equipment", new Item(-1, -1));
		AttributeKey.forTransient("smithing_position", new Position(0, 0));
		
		//Npc
		AttributeKey.forTransient("npc_facing", Direction.NONE);
		AttributeKey.forTransient("isRetreating", false);
		AttributeKey.forTransient("npcInformation", 0);
	}
	
	/**
	 * Aliases {@code name} with an initial value of {@code initialValue} that will be written to and read from the character
	 * file.
	 * @param name         The name of this key.
	 * @param initialValue The initial value of this key.
	 */
	public static <T> void forPersistent(String name, T initialValue) {
		ALIASES.put(name, new AttributeKey<>(name, initialValue, true));
	}
	
	/**
	 * Aliases {@code name} with an initial value of {@code initialValue}.
	 * @param name         The name of this key.
	 * @param initialValue The initial value of this key.
	 */
	public static <T> void forTransient(String name, T initialValue) {
		ALIASES.put(name, new AttributeKey<>(name, initialValue, false));
	}
	
	/**
	 * The name of this alias.
	 */
	private final String name;
	
	/**
	 * The initial value of this alias.
	 */
	private final T initialValue;
	
	/**
	 * If the value of this alias should be serialized.
	 */
	private final boolean isPersistent;
	
	/**
	 * The fully-qualified class name of this attribute type.
	 */
	private final String typeName;
	
	/**
	 * Creates a new {@link AttributeKey}.
	 * @param name         The name of this alias.
	 * @param initialValue The initial value of this alias.
	 * @param isPersistent If the value of this alias should be serialized.
	 */
	private AttributeKey(String name, T initialValue, boolean isPersistent) {
		checkArgument(!name.isEmpty(), "attribute name length <= 0");
		checkState(!ALIASES.containsKey(name.intern()), "attribute already aliased");
		
		this.name = name.intern();
		this.initialValue = initialValue;
		this.isPersistent = isPersistent;
		typeName = initialValue.getClass().getName();
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("name", name).add("persistent", isPersistent).add("type", typeName).toString();
	}
	
	/**
	 * @return The name of this alias.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The initial value of this alias.
	 */
	public T getInitialValue() {
		return initialValue;
	}
	
	/**
	 * @return {@code true} if the value of this alias should be serialized, {@code false} otherwise.
	 */
	public boolean isPersistent() {
		return isPersistent;
	}
	
	/**
	 * @return The fully-qualified class name of this attribute type.
	 */
	public String getTypeName() {
		return typeName;
	}
}