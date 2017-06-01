package net.edge.world.node.item;

import net.edge.content.container.impl.EquipmentType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The container that represents an item definition.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemDefinition {
	
	/**
	 * The array that contains all of the item definitions.
	 */
	public static final ItemDefinition[] DEFINITIONS = new ItemDefinition[22322];
	
	/**
	 * The identifier for the item.
	 */
	private final int id;
	
	/**
	 * The proper name of the item.
	 */
	private final String name;
	
	/**
	 * The equipment slot of this item.
	 */
	private final EquipmentType equipmentType;
	
	/**
	 * The flag that determines if the item is a weapon.
	 */
	private final boolean weapon;
	
	/**
	 * The flag that determines if the item is noted.
	 */
	private final boolean noted;
	
	/**
	 * The note transform id of this item.
	 */
	private final int noteId;
	
	/**
	 * The flag that determines if the item is lended.
	 */
	private boolean lended;
	
	/**
	 * The lend transform id of this item.
	 */
	private final int lendId;
	
	/**
	 * The flag that determines if the item is stackable.
	 */
	private final boolean stackable;
	
	/**
	 * The low alch value of this item.
	 */
	private final int lowAlchValue;
	
	/**
	 * The high alch value of this item.
	 */
	private final int highAlchValue;
	
	/**
	 * The weight value of this item.
	 */
	public final double weight;
	
	/**
	 * The flag that determines if this item is two-handed.
	 */
	private final boolean twoHanded;
	
	/**
	 * The flag that determines if this item is tradeable.
	 */
	private final boolean tradable;
	
	/**
	 * The flag that determines if this item is alchable.
	 */
	private final boolean alchable;
	
	/**
	 * The array of bonuses for this item.
	 */
	private final int[] bonus;
	
	/**
	 * The array of inventory actions for this item.
	 */
	private final String[] inventoryActions;
	
	/**
	 * The array of ground actions for this item.
	 */
	private final String[] groundActions;
	
	/**
	 * Creates a new {@link ItemDefinition}.
	 * @param id               the identifier for the item.
	 * @param name             the proper name of the item.
	 * @param equipmentType    the equipment type of this item.
	 * @param tradeable        the flag that determines if this item is tradeable.
	 * @param weapon           the flag that determines if this item is a weapon.
	 * @param twoHanded        the flag that determines if this item is two-handed.
	 * @param stackable        the flag that determines if this item is stackable.
	 * @param alchable         the flag that determines if this item is alchable.
	 * @param noted            the flag that determines if this item is noted.
	 * @param lended           the flag that determines if this item is lended.
	 * @param lowAlchValue     the low alch value of this item.
	 * @param highAlchValue    the high alch value of this item.
	 * @param weight           the weight value of this item.
	 * @param bonus            the array of bonuses for this item.
	 * @param inventoryActions the array of inventory actions for this item.
	 * @param groundActions    the array of ground actions for this item.
	 */
	public ItemDefinition(int id, String name, EquipmentType equipmentType, boolean tradeable, boolean weapon, boolean twoHanded, boolean stackable, boolean alchable, boolean noted, int noteId, boolean lended, int lendId, int lowAlchValue, int highAlchValue, double weight, int[] bonus, String[] inventoryActions, String[] groundActions) {
		this.id = id;
		this.name = name;
		this.equipmentType = equipmentType;
		this.tradable = tradeable;
		this.weapon = weapon;
		this.twoHanded = twoHanded;
		this.stackable = stackable;
		this.alchable = alchable;
		this.noted = noted;
		this.noteId = noteId;
		this.lended = lended;
		this.lendId = lendId;
		this.lowAlchValue = lowAlchValue;
		this.highAlchValue = highAlchValue;
		this.weight = weight;
		this.bonus = bonus;
		this.inventoryActions = inventoryActions;
		this.groundActions = groundActions;
		prayerBonus();
	}
	
	/**
	 * The method that erases the prayer bonus from ranged weapons.
	 */
	private void prayerBonus() {
		if(bonus != null) {
			if(equipmentType == EquipmentType.ARROWS || name.contains("knife") || name.contains("dart") || name.contains("thrownaxe") || name.contains("javelin")) {
				bonus[11] = 0;
			}
		}
	}
	
	public static ItemDefinition get(int id) {
		return DEFINITIONS[id];
	}
	
	public static Optional<ItemDefinition> fromString(String name, boolean contains) {
		return Arrays.stream(DEFINITIONS).filter(Objects::nonNull).filter($it -> contains ? $it.getName().contains(name) : $it.getName().equalsIgnoreCase(name)).findAny();
	}
	
	public static List<ItemDefinition> collect(String name) {
		return Arrays.stream(DEFINITIONS).filter(Objects::nonNull).filter($it -> $it.getName().contains(name)).collect(Collectors.toList());
	}
	
	/**
	 * Gets the identifier for the item.
	 * @return the identifier.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the proper name of the item.
	 * @return the proper name.
	 */
	public String getName() {
		return noted ? name + " (noted)" : name;
	}
	
	/**
	 * Gets the equipment type of this item.
	 * @return the equipment type.
	 */
	public EquipmentType getEquipmentType() {
		return equipmentType;
	}
	
	/**
	 * @return the lendId
	 */
	public int getNoted() {
		return noteId;
	}
	
	/**
	 * Determines if the item is noted or not.
	 * @return {@code true} if the item is noted, {@code false} otherwise.
	 */
	public boolean isNoted() {
		return noted;
	}
	
	/**
	 * Determines if the item is noteable or not.
	 * @return {@code true} if the item is noteable, {@code false} otherwise.
	 */
	public boolean isNoteable() {
		return noteId != -1;
	}
	
	/**
	 * @return the lended
	 */
	public boolean isLended() {
		return lended;
	}
	
	/**
	 * @return the lendId
	 */
	public int getLend() {
		return lendId;
	}
	
	/**
	 * Determines if the item is stackable or not.
	 * @return {@code true} if the item is stackable, {@code false} otherwise.
	 */
	public boolean isStackable() {
		return stackable;
	}
	
	/**
	 * Determines if the item is alchable or not.
	 * @return {@code true} if the item is alchable, {@code false} otherwise.
	 */
	public boolean isAlchable() {
		return alchable;
	}
	
	/**
	 * Gets the low alch value of this item.
	 * @return the low alch value.
	 */
	public int getLowAlchValue() {
		return this.isNoted() ? get(this.getNoted()).lowAlchValue : this.lowAlchValue;
	}
	
	/**
	 * Gets the high alch value of this item.
	 * @return the high alch value.
	 */
	public int getHighAlchValue() {
		return this.isNoted() ? get(this.getNoted()).highAlchValue : this.highAlchValue;
	}
	
	/**
	 * Gets the weight value of this item.
	 * @return the weight value.
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Gets the array of bonuses for this item.
	 * @return the array of bonuses.
	 */
	public int[] getBonus() {
		return bonus;
	}
	
	/**
	 * Determines if this item is two-handed or not.
	 * @return {@code true} if this item is two-handed, {@code false} otherwise.
	 */
	public boolean isTwoHanded() {
		return twoHanded;
	}
	
	/**
	 * Determines if this item is a weapon or not.
	 * @return {@code true} if this item is a weapon, {@code false}
	 * otherwise.
	 */
	public boolean isWeapon() {
		return weapon;
	}
	
	/**
	 * Determines if this item is a full plate body or not.
	 * @return {@code true} if this item is a full plate body, {@code false}
	 * otherwise.
	 */
	public boolean isPlatebody() {
		return equipmentType == EquipmentType.PLATEBODY;
	}
	
	/**
	 * Determines if this item is a full helm or not.
	 * <p>If it is, this method will remove the players hair, but keep his beard.</p>
	 * @return {@code true} if this item is a full helm, {@code false}
	 * otherwise.
	 */
	public boolean isFullHelm() {
		return equipmentType == EquipmentType.FULL_HELMET;
	}
	
	/**
	 * Determines if this item is a full mask or not.
	 * <p>If it is, this method will remove the players hair and beard.</p>
	 * @return {@code true} if this item is a full mask, {@code false}
	 * otherwise.
	 */
	public boolean isFullMask() {
		return equipmentType == EquipmentType.FULL_MASK;
	}
	
	/**
	 * Determines if this item is a hat or not.
	 * <p>If it is, this method will remove nothing.</p>
	 * @return {@code true} if this item is a hat, {@code false}
	 * otherwise.
	 */
	public boolean isHat() {
		return equipmentType == EquipmentType.HAT;
	}
	
	/**
	 * Determines if this item is tradable.
	 * @return {@code true} if this item is tradable, {@code false} otherwise.
	 */
	public boolean isTradable() {
		return tradable;
	}
	
	/**
	 * @return the inventoryActions
	 */
	public String[] getInventoryActions() {
		return inventoryActions;
	}
	
	/**
	 * @return the groundActions
	 */
	public String[] getGroundActions() {
		return groundActions;
	}
	
	public static void dumpDrops() {
		/*try {
			int totald = 0;
			String check = null;
			BufferedReader bufferedReader = new BufferedReader(new FileReader("./items.txt"));
			while((check = bufferedReader.readLine()) != null) {
				int drops = 0;
				//System.out.println(check);
				int index = Integer.parseInt(check.split("-")[0]);
				String itemName = check.split("-")[1];
				if(itemName.equals("null"))
					continue;
				itemName = itemName.toLowerCase();
				
				//if(itemName.contains(" (")) {
				//	itemName = itemName.replaceAll(" \\(", "#(");
				//}
				itemName = itemName.replace(" ", "_");
				try {
					URL u = new URL("http://2007.runescape.wikia.com/wiki/"+itemName+"?action=raw");
					HttpURLConnection huc = (HttpURLConnection) u.openConnection ();
					huc.setRequestMethod ("GET");  //OR  huc.setRequestMethod ("HEAD");
					huc.setInstanceFollowRedirects(true);
					huc.connect () ;
					int code = huc.getResponseCode() ;
					if(code == 404) {
						continue;
					}
					
					Scanner scanner = new Scanner(new InputStreamReader(huc.getInputStream()));
					String line;
					
					while (scanner.hasNextLine()) {
						line = scanner.nextLine();
						if(line.contains("ItemDropsLine")) {
							try {
								String npc = null;
								int[] combat = null;
								int minQuant = 0;
								int maxQuant = 0;
								Chance rarity = null;
								String[] parts = line.split("[|]");
								for(String p : parts) {
									if(p.contains("Monster")) {
										p = p.replaceAll("Monster", "");
										p = p.replaceAll("=", "");
										p = p.replaceAll(" ", "");
										npc = p.toLowerCase().replace(" ", "_");
									}
									if(p.contains("Combat")) {
										p = p.replaceAll("Combat", "");
										p = p.replaceAll("=", "");
										p = p.replaceAll(" ", "");
										p = p.replaceAll("-", ";");
										p = p.replaceAll(",", ";");
										if(p.toLowerCase().contains("n/a")) {
											combat = new int[1];
											combat[0] = -1;
										} else {
											if(p.contains(";")) {
												String[] combats = p.split(";");
												combat = new int[combats.length];
												int count = 0;
												for(String c : combats) {
													combat[count] = Integer.parseInt(c);
													count++;
												}
											} else if(p.contains("–")) {
												String[] combats = p.split("–");
												combat = new int[combats.length];
												int count = 0;
												for(String c : combats) {
													combat[count] = Integer.parseInt(c);
													count++;
												}
											} else {
												String[] combats = p.split("(?<!^)(?=-)");
												combat = new int[combats.length];
												int count = 0;
												for(String c : combats) {
													c = c.replace('?', ' ');
													c = c.replaceAll(" ", "");
													if(c.length() != 0)
														combat[count] = Integer.parseInt(c);
													count++;
												}
											}
										}
									}
									if(p.contains("Quantity")) {
										if(p.contains("(noted")) {
											p = p.replaceAll("(noted)", "");
											p = p.replaceAll("\\)", "");
											p = p.replaceAll("\\(", "");
											index += 1;
										}
										p = p.replaceAll("Quantity", "");
										p = p.replaceAll("=", "");
										p = p.replaceAll(" ", "");
										p = p.replaceAll(",", ";");
										if(p.toLowerCase().contains("unknown")) {
											minQuant = 1;
											maxQuant = 1;
										} else {
											if(p.contains(";")) {
												String[] quants = p.split(";");
												for(String q : quants) {
													if(p.length() == 0)
														continue;
													if(q.contains("–")) {
														String[] quants2 = q.split("–");
														for(String q2 : quants2) {
															if(q2.length() == 0)
																continue;
															int num = Integer.parseInt(q2);
															if(num < minQuant || minQuant == 0) {
																minQuant = num;
															}
															if(num > maxQuant || maxQuant == 0) {
																maxQuant = num;
															}
														}
													} else {
														int num = Integer.parseInt(q);
														if(num < minQuant || minQuant == 0) {
															minQuant = num;
														}
														if(num > maxQuant || maxQuant == 0) {
															maxQuant = num;
														}
													}
												}
											} else if(p.contains("–")) {
												String[] quants = p.split("–");
												for(String q : quants) {
													if(q.length() == 0)
														continue;
													int num = Integer.parseInt(q);
													if(num < minQuant || minQuant == 0) {
														minQuant = num;
													}
													if(num > maxQuant || maxQuant == 0) {
														maxQuant = num;
													}
												}
											} else {
												String[] quants = p.split("(?<!^)(?=-)");
												for(String q : quants) {
													if(q.length() == 0)
														continue;
													int num = Integer.parseInt(q);
													if(num < minQuant || minQuant == 0) {
														minQuant = num;
													}
													if(num > maxQuant || maxQuant == 0) {
														maxQuant = num;
													}
												}
											}
										}
									}
									if(p.contains("Rarity")) {
										p = p.replaceAll("Rarity", "");
										p = p.replaceAll("=", "");
										p = p.replaceAll(" ", "");
										p = p.replaceAll("}", "");
										rarity = Chance.get(p);
									}
								}
								if(npc != null && rarity != null && combat != null) {
									for(NpcDefinition def : NpcDefinition.DEFINITIONS) {
										if(def == null)
											continue;
										if(def.getName() == null)
											continue;
										if(npc.equals(def.getName().toLowerCase().replace(" ", ""))) {
											if(combat[0] != -1) {
												for(int comb : combat) {
													if(comb == 0)
														continue;
													if(comb == def.getCombatLevel()) {
														if(NpcDropManager.getTables().containsKey(index)) {
															NpcDropTable drop = NpcDropManager.getTables().get(def.getId());
															if(!drop.getUnique().contains(new NpcDrop(index, minQuant, maxQuant, rarity))) {
																drop.getUnique().add(new NpcDrop(index, minQuant, maxQuant, rarity));
																drops += 1;
															}
														} else {
															NpcDropTable drop = new NpcDropTable(new NpcDrop[]{new NpcDrop(index, minQuant, maxQuant, rarity)}, new NpcDropCache[]{NpcDropCache.LOW_RUNES});
															NpcDropManager.getTables().put(def.getId(), drop);
															drops += 1;
														}
													}
												}
											} else {
												if(NpcDropManager.getTables().containsKey(index)) {
													NpcDropTable drop = NpcDropManager.getTables().get(def.getId());
													if(!drop.getUnique().contains(new NpcDrop(index, minQuant, maxQuant, rarity))) {
														drop.getUnique().add(new NpcDrop(index, minQuant, maxQuant, rarity));
														drops += 1;
													}
												} else {
													NpcDropTable drop = new NpcDropTable(new NpcDrop[]{new NpcDrop(index, minQuant, maxQuant, rarity)}, new NpcDropCache[]{NpcDropCache.LOW_RUNES});
													NpcDropManager.getTables().put(def.getId(), drop);
													drops += 1;
												}
											}
										}
									}
								}
							} catch(Exception e) {
								
							}
						}
					}
					System.out.println(index + " - " +itemName + " drops: " + drops);
					totald += drops;
					scanner.close();
					//Thread.sleep(3000);
				} catch(Exception e) {
					//e.printStackTrace();
				}
			}
			// Always close files.
			bufferedReader.close();
			System.out.println("added drops: " + totald);
			NpcDropManager.dump();
		} catch(IOException ex) {
			ex.printStackTrace();
		}*/
	}
	
}