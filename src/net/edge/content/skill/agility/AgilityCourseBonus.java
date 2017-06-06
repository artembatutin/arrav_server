package net.edge.content.skill.agility;

import net.edge.content.skill.agility.impl.barb.BarbarianOutpostAgility;
import net.edge.content.skill.agility.impl.gnome.GnomeStrongholdAgility.GnomeAgilityData;
import net.edge.content.skill.agility.impl.wild.WildernessAgility;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * Holds functionality for checking if a player has completed all the labs of
 * an agility obstacle course.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class AgilityCourseBonus {
	
	/**
	 * The set of obstacles for the gnome agility course.
	 */
	private final EnumSet<GnomeAgilityData> gnome_course = EnumSet.noneOf(GnomeAgilityData.class);
	
	/**
	 * The set of obstacles for the barbarian agility course.
	 */
	private final EnumSet<BarbarianOutpostAgility.BarbarianAgilityData> barbarian_course = EnumSet.noneOf(BarbarianOutpostAgility.BarbarianAgilityData.class);
	
	/**
	 * The set of obstacles for the wilderness agility course.
	 */
	private final EnumSet<WildernessAgility.WildernessAgilityData> wilderness_course = EnumSet.noneOf(WildernessAgility.WildernessAgilityData.class);
	
	/**
	 * Attempts to add a gnome agility obstacle to the underlying {@code gnome_course} set.
	 * @param data the data to attempt to add.
	 */
	public void addGnomeObstacle(GnomeAgilityData data) {
		if(gnome_course.contains(data)) {
			return;
		}
		
		gnome_course.add(data);
	}
	
	/**
	 * Clears the gnome obstacles completed.
	 */
	public void clearGnomeObstacles() {
		gnome_course.clear();
	}
	
	/**
	 * Determines if the player has completed the gnome agility course.
	 * @return {@code true} if the player has, {@code false} otherwise.
	 */
	public boolean hasCompletedGnomeAgilityCourse() {
		return gnome_course.size() >= 7;//whether they do advanced or regular, it's 7 obstacles.
	}
	
	/**
	 * Attempts to add a barbarian agility obstacle to the underlying {@code barbarian_course} set.
	 * @param data the data to attempt to add.
	 */
	public void addBarbarianObstacle(BarbarianOutpostAgility.BarbarianAgilityData data) {
		if(barbarian_course.contains(data)) {
			return;
		}
		
		barbarian_course.add(data);
	}
	
	/**
	 * Clears the barbarian obstacles completed.
	 */
	public void clearBarbarianObstacles() {
		barbarian_course.clear();
	}
	
	/**
	 * Determines if the player has completed the barbarian agility course.
	 * @return {@code true} if the player has, {@code false} otherwise.
	 */
	public boolean hasCompletedBarbarianAgilityCourse() {
		return gnome_course.size() >= 7;//whether they do advanced or regular, it's 7 or 8 obstacles.
	}
	
	/**
	 * Clears the wilderness obstacles completed.
	 */
	public void clearWildernessObstacles() {
		wilderness_course.clear();
	}
	
	/**
	 * Determines if the player has completed the wilderness agility course.
	 * @return {@code true} if the player has, {@code false} otherwise.
	 */
	public boolean hasCompletedWildernessAgilityCourse() {
		return wilderness_course.size() >= 5;
	}
	
	/**
	 * Attempts to add a wilderness agility obstacle to the underlying {@code wilderness_course} set.
	 * @param data the data to attempt to add.
	 */
	public void addWildernessObstacle(WildernessAgility.WildernessAgilityData data) {
		if(wilderness_course.contains(data) || Arrays.stream(new WildernessAgility.WildernessAgilityData[]{WildernessAgility.WildernessAgilityData.GATE, WildernessAgility.WildernessAgilityData.GATE_BACK}).anyMatch(p -> p.equals(data))) {
			return;
		}
		
		wilderness_course.add(data);
	}
}
