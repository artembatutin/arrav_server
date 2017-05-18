package net.edge.world.node.entity.model;

import net.edge.world.node.entity.EntityNode;

/**
 * The container class that represents a hit that can be dealt on a
 * {@link EntityNode}.
 * @author lare96 <http://github.com/lare96>
 */
public final class Hit {
	
	/**
	 * The amount of damage within this hit.
	 */
	private int damage;
	
	/**
	 * The soaked damage within this hit.
	 */
	private int soak;
	
	/**
	 * The flag that determines whether this hit is accurate.
	 */
	private boolean accurate;
	
	/**
	 * The hit icon represented by this hit.
	 */
	private HitIcon icon;
	
	/**
	 * The hit type represented by this hit.
	 */
	private HitType type;
	
	/**
	 * The hit delay
	 */
	private final int hitDelay;
	
	/**
	 * The source of the hit.
	 */
	private final int source;
	
	/**
	 * Creates a new {@link Hit} with a {@link #hitDelay} of 0 and {@link #accurate} set to {@code true}.
	 * @param damage the amount of damage within this hit.
	 * @param type   the hit type represented by this hit.
	 * @param icon   the hit icon represented by this hit.
	 */
	public Hit(int damage, HitType type, HitIcon icon) {
		this.damage = damage;
		this.type = type;
		this.icon = icon;
		this.hitDelay = 0;
		this.accurate = true;
		this.source = -1;
	}
	
	/**
	 * Creates a new {@link Hit} with a {@link #hitDelay} of 0 and {@link #accurate} set to {@code true}.
	 * @param damage the amount of damage within this hit.
	 * @param type   the hit type represented by this hit.
	 * @param icon   the hit icon represented by this hit.
	 * @param source the source of this hit.
	 */
	public Hit(int damage, HitType type, HitIcon icon, int source) {
		this.damage = damage;
		this.type = type;
		this.icon = icon;
		this.hitDelay = 0;
		this.accurate = true;
		this.source = source;
	}
	
	/**
	 * Creates a new {@link Hit}.
	 * @param damage   the amount of damage within this hit.
	 * @param type     the hit type represented by this hit.
	 * @param icon     the hit icon represented by this hit.
	 * @param delay    the hit delay represented by this hit.
	 * @param accurate the condition if the hit is being accurate.
	 * @param source   the source of this hit.
	 */
	public Hit(int damage, HitType type, HitIcon icon, int delay, boolean accurate, int source) {
		this.damage = damage;
		this.type = type;
		this.icon = icon;
		this.hitDelay = delay;
		this.accurate = accurate;
		this.source = source;
	}
	
	/**
	 * Creates a new {@link Hit} with a {@code type} of {@code NORMAL}.
	 * @param damage the amount of damage within this hit.
	 */
	public Hit(int damage) {
		this(damage, HitType.NORMAL, HitIcon.NONE, -1);
	}
	
	/**
	 * A substitute for {@link Object#clone()} that creates another 'copy' of
	 * this instance. The created copy is <i>safe</i> meaning it does not hold
	 * <b>any</b> references to the original instance.
	 * @return a reference-free copy of this instance.
	 */
	public Hit copy() {
		return new Hit(damage, type, icon, hitDelay, accurate, source);
	}
	
	/**
	 * Gets the amount of damage within this hit.
	 * @return the amount of damage within this hit.
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * Sets the amount of damage to deal within this hit.
	 * @param damage the amount of damage to hit.
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	/**
	 * Decreases the amount of damage to deal within this hit.
	 * @param damage the amount of damage to hit.
	 */
	public void decreaseDamage(int damage) {
		this.damage -= damage;
		if(this.damage < 0) {
			this.damage = 0;
		}
	}
	
	/**
	 * Gets the soaking within this hit.
	 * @return the soaking within this hit.
	 */
	public int getSoak() {
		return soak;
	}
	
	/**
	 * Sets the amount of soak within this hit.
	 * @param soak the amount of soak to set.
	 */
	public void setSoak(int soak) {
		this.soak = soak;
	}
	
	/**
	 * Determines if this hit is accurate.
	 * @return {@code true} if this hit is accurate, {@code false} otherwise.
	 */
	public boolean isAccurate() {
		return accurate;
	}
	
	/**
	 * Sets the value for {@link #accurate}.
	 * @param accurate the new value to set.
	 */
	public void setAccurate(boolean accurate) {
		this.accurate = accurate;
	}
	
	/**
	 * Gets the hit icon represented by this hit.
	 * @return the hit icon represented by this hit.
	 */
	public HitIcon getIcon() {
		return icon;
	}
	
	/**
	 * Sets the hit icon within this hit.
	 * @param icon the hit icon to set.
	 */
	public void setIcon(HitIcon icon) {
		this.icon = icon;
	}
	
	/**
	 * Gets the hit type represented by this hit.
	 * @return the hit type represented by this hit.
	 */
	public HitType getType() {
		return type;
	}
	
	/**
	 * Sets the value for {@link #type}.
	 * @param type the new value to set.
	 */
	public void setType(HitType type) {
		this.type = type;
	}
	
	/**
	 * @return {@link #hitDelay}.
	 */
	public int getHitdelay() {
		return hitDelay;
	}
	
	/**
	 * Gets the source of this hit.
	 * @return the source of this hit.
	 */
	public int getSource() {
		return source;
	}
	
	/**
	 * Determines if the hit has a source.
	 * @return {@code true} if the {@link Hit} has a source, {@code false} otherwise.
	 */
	public boolean hasSource() {
		return source != -1;
	}
	
	/**
	 * The enumerated type whose elements represent the hit icon of a {@link Hit}.
	 * @author Artem Batutin <artembatutin@gmail.com>
	 */
	public enum HitIcon {
		
		/**
		 * Represents no hit icon at all.
		 */
		NONE(255),
		
		/**
		 * Represents the melee sword hit icon.
		 */
		MELEE(0),
		
		/**
		 * Represents the magic hat hit icon.
		 */
		MAGIC(1),
		
		/**
		 * Represents the ranged bow hit icon.
		 */
		RANGED(2),
		
		/**
		 * Represents the leech hit icon.
		 */
		LEECH(3),
		
		/**
		 * Represents the canon hit icon.
		 */
		CANON(4);
		
		/**
		 * The identification for this hit type.
		 */
		private final int id;
		
		/**
		 * Create a new {@link HitType}.
		 * @param id the identification for this hit type.
		 */
		HitIcon(int id) {
			this.id = id;
		}
		
		/**
		 * Gets the identification for this hit type.
		 * @return the identification for this hit type.
		 */
		public final int getId() {
			return id;
		}
		
	}
	
	/**
	 * The enumerated type whose elements represent the hit type of a {@link Hit}.
	 * @author Artem Batutin <artembatutin@gmail.com>
	 * @author lare96 <http://github.com/lare96>
	 */
	public enum HitType {
		
		/**
		 * Represents a normal hit type.
		 */
		NORMAL(0),
		
		/**
		 * Represents a critical hit type.
		 */
		CRITICAL(1),
		
		/**
		 * Represents a poison hit type.
		 */
		POISON(2),
		
		/**
		 * Represents a disease hit type.
		 */
		DISEASE(3),
		
		/**
		 * Represents a heal hit type.
		 */
		HEAL(4),
		
		/**
		 * Represents a local normal hit type.
		 */
		NORMAL_LOCAL(5),
		
		/**
		 * Represents a local critical hit type.
		 */
		CRITICAL_LOCAL(6),
		
		/**
		 * Represents a local poison hit type.
		 */
		POISON_LOCAL(7),
		
		/**
		 * Represents a local disease hit type.
		 */
		DISEASE_LOCAL(8),
		
		/**
		 * Represents a local heal hit type.
		 */
		HEAL_LOCAL(9);
		
		/**
		 * The identification for this hit type.
		 */
		private final int id;
		
		/**
		 * Create a new {@link HitType}.
		 * @param id the identification for this hit type.
		 */
		HitType(int id) {
			this.id = id;
		}
		
		/**
		 * Gets the identification for this hit type.
		 * @return the identification for this hit type.
		 */
		public final int getId() {
			return id;
		}
	}
}
