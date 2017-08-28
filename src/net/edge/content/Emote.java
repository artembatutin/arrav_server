package net.edge.content;

import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.entity.actor.player.Player;

/**
 * An enumeration of emotes.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum Emote {

	YES(168, 855, -1),
	NO(169, 856, -1),
	BOW(164, 858, -1),
	ANGRY(167, 864, -1),
	THINK(162, 857, -1),
	WAVE(163, 863, -1),
	SHRUG(52058, 2113, -1),
	CHEER(171, 862, -1),
	BECKON(165, 859, -1),
	LAUGH(170, 861, -1),
	JUMP_FOR_JOY(52054, 2109, -1),
	YAWN(52056, 2111, -1),
	DANCE(166, 866, -1),
	JIG(52051, 2106, -1),
	TWIRL(52052, 2107, -1),
	HEADBANG(52053, 2108, -1),
	CRY(161, 860, -1),
	BLOW_KISS(43092, 1374, 574),
	PANIC(52050, 2105, -1),
	RASBERRY(52055, 2110, -1),
	CLAP(172, 865, -1),
	SAUTE(52057, 2112, -1),
	GOBLIN_BOW(52071, 2127, -1),
	GOBLIN_SALUTE(52072, 2128, -1),
	GLASS_BOX(2155, 1131, -1),
	CLIMB_ROPE(25103, 1130, -1),
	LEAN(25106, 1129, -1),
	GLASS_WALL(2154, 1128, -1),
	IDEA(88060, 4276, 712),
	STOMP(88061, 4278, -1),
	FLAP(88062, 4280, -1),
	SLAP_HEAD(88063, 4275, -1),
	ZOMBIE_WALK(73032, 3544, -1),
	ZOMBIE_DANCE(73033, 3543, -1),
	ZOMBIE_HAND(88065, 7272, 1244),
	SCARED(59062, 2836, -1),
	BUNY_HOP(72254, 6111, -1),
	//SKILLCAPE(18686, 1, 1),
	SNOW_MAN(89048, 7531, -1),
	AIR_GUITAR(89049, 2414, 1537),
	SAFETY_FIRST(89050, 8770, 1553);

	/**
	 * The initialization button id of the emote.
	 */
	private final int button;

	/**
	 * The animation id of the emote.
	 */
	private final int animation;

	/**
	 * The graphic id of the emote.
	 */
	private final int graphic;

	Emote(int button, int animation, int graphic) {
		this.button = button;
		this.animation = animation;
		this.graphic = graphic;
	}

	/**
	 * Handles the emotes on each button click.
	 *
	 * @param player The playing clicking the button.
	 * @param button The button id.
	 * @return {@code true} if emote is handled, {@code false} otherwise.
	 */
	public static boolean handle(Player player, int button) {
		for(Emote emote : Emote.values()) {
			if(button == emote.button) {
				if(emote.animation != -1)
					player.animation(new Animation(emote.animation));
				if(emote.graphic != -1)
					player.graphic(new Graphic(emote.graphic));
				return true;
			}
		}
		return false;
	}

}
