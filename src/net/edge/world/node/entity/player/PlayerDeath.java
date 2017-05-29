package net.edge.world.node.entity.player;

import com.google.common.collect.Ordering;
import net.edge.utils.rand.RandomUtils;
import net.edge.GameConstants;
import net.edge.world.World;
import net.edge.world.content.PlayerPanel;
import net.edge.world.content.combat.CombatConstants;
import net.edge.world.content.combat.weapon.WeaponInterface;
import net.edge.world.content.container.impl.Equipment;
import net.edge.world.content.container.impl.Inventory;
import net.edge.world.content.minigame.Minigame;
import net.edge.world.content.minigame.MinigameHandler;
import net.edge.world.content.scoreboard.PlayerScoreboardStatistic;
import net.edge.world.content.skill.Skill;
import net.edge.world.content.skill.Skills;
import net.edge.world.content.skill.prayer.Prayer;
import net.edge.world.locale.loc.Location;
import net.edge.world.locale.Position;
import net.edge.world.node.entity.EntityDeath;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Hit;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.entity.update.UpdateFlag;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNode;
import net.edge.world.region.Region;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * The {@link EntityDeath} implementation that is dedicated to managing the
 * death process for all {@link Player}s.
 * @author lare96 <http://github.com/lare96>
 */
public final class PlayerDeath extends EntityDeath<Player> {
	
	/**
	 * Determines if the death message is send.
	 */
	private boolean deathMessage;
	
	/**
	 * Creates a new {@link PlayerDeath}.
	 * @param player the player who has died and needs the death process.
	 */
	public PlayerDeath(Player player) {
		super(player);
	}
	
	@Override
	public void preDeath() {
		getCharacter().getActivityManager().disable();
		getCharacter().animation(new Animation(0x900, Animation.AnimationPriority.HIGH));
		getCharacter().setSkillAction(Optional.empty());
		World.getExchangeSessionManager().reset(getCharacter());

		if(!MinigameHandler.getMinigame(getCharacter()).isPresent()) {
			deathMessage = true;
		}

		if(Prayer.isActivated(getCharacter(), Prayer.RETRIBUTION)) {
			getCharacter().graphic(new Graphic(437));
			final int hit = RandomUtils.inclusive(CombatConstants.MAXIMUM_RETRIBUTION_DAMAGE);
			if(Location.inMultiCombat(getCharacter())) {
				getCharacter().getLocalNpcs().stream().filter(n -> n.getPosition().withinDistance(getCharacter().getPosition(), 2)).forEach(h -> h.damage(new Hit(hit)));
				if(Location.inWilderness(getCharacter())) {
					getCharacter().getLocalPlayers().stream().filter(p -> p.getPosition().withinDistance(getCharacter().getPosition(), 2)).forEach(h -> h.damage(new Hit(hit)));
				}
			} else {
				EntityNode victim = getCharacter().getCombatBuilder().getVictim();
				if(victim != null && victim.getPosition().withinDistance(getCharacter().getPosition(), 2)) {
					victim.damage(new Hit(RandomUtils.inclusive(hit)));
				}
			}
		}

		if(Prayer.isActivated(getCharacter(), Prayer.WRATH)) {
			getCharacter().graphic(new Graphic(2259));
			int x = getCharacter().getPosition().getX() - 3;
			int y = getCharacter().getPosition().getY() - 2;
			for(int i = 0; i < 25; i++) {
				x++;
				if(i == 5 || i == 10 || i == 15 || i == 20) {
					x -= 5;
					y++;
				}
				if(i % 2 == 1)
					continue;
				getCharacter().getMessages().sendLocalGraphic(2260, new Position(x, y, getCharacter().getPosition().getZ()), 25);
			}
			int maxHit = (int) ((getCharacter().getSkills()[Skills.PRAYER].getLevel() / 100.D) * 25);
			if(Location.inMultiCombat(getCharacter())) {
				getCharacter().getLocalNpcs().stream().filter(n -> n.getPosition().withinDistance(getCharacter().getPosition(), 3)).forEach(h -> h.damage(new Hit(RandomUtils.inclusive(maxHit))));
				if(Location.inWilderness(getCharacter())) {
					getCharacter().getLocalPlayers().stream().filter(p -> p.getPosition().withinDistance(getCharacter().getPosition(), 3)).forEach(h -> h.damage(new Hit(RandomUtils.inclusive(maxHit))));
				}
			} else {
				EntityNode victim = getCharacter().getCombatBuilder().getVictim();
				if(victim != null && victim.getPosition().withinDistance(getCharacter().getPosition(), 3)) {
					victim.damage(new Hit(RandomUtils.inclusive(maxHit)));
				}
			}
		}
		
		if(Location.inFunPvP(getCharacter())) {
			getCharacter().move(new Position(3086, 3508, 1));
			setCounter(6);
		}
	}
	
	@Override
	public void death() {
		Optional<Player> killer = getCharacter().getCombatBuilder().getDamageCache().getPlayerKiller();
		Optional<Minigame> optional = MinigameHandler.getMinigame(getCharacter());
		if(optional.isPresent()) {
			optional.get().onDeath(getCharacter());
			if(optional.get().getSafety().equals(Minigame.MinigameSafety.DEFAULT)) {
				if(getCharacter().getRights().less(Rights.ADMINISTRATOR)) {
					calculateDropItems(getCharacter(), killer, false);
				}
			} else if(optional.get().getSafety().equals(Minigame.MinigameSafety.DANGEROUS)) {
				if(getCharacter().getRights().less(Rights.ADMINISTRATOR)) {
					calculateDropItems(getCharacter(), killer, true);
				}
			}
			killer.ifPresent(k -> optional.get().onKill(k, getCharacter()));
			getCharacter().move(optional.get().deathPosition(getCharacter()));
			return;
		}
		if(getCharacter().getRights().less(Rights.ADMINISTRATOR)) {
			calculateDropItems(getCharacter(), killer, false);
			getCharacter().move(new Position(3084, 3514));
		}
		
		killer.ifPresent(k -> {
			//deaths
			PlayerScoreboardStatistic characterStatistic = World.getScoreboardManager().getPlayerScoreboard().putIfAbsent(getCharacter().getFormatUsername(), new PlayerScoreboardStatistic(getCharacter().getFormatUsername()));
			
			if(characterStatistic == null) {
				characterStatistic = new PlayerScoreboardStatistic(getCharacter().getFormatUsername());
			}
			
			PlayerPanel.INDIVIDUAL_DEATHS.refresh(getCharacter(), "@or2@ - Current Player deaths: @yel@" + characterStatistic.getDeaths().incrementAndGet());
			
			PlayerPanel.TOTAL_PLAYER_DEATHS.refresh(getCharacter(), "@or2@ - Total Player deaths: @yel@" + getCharacter().getDeathsByPlayer().incrementAndGet());
			
			//kills
			PlayerScoreboardStatistic killerStatistic = World.getScoreboardManager().getPlayerScoreboard().putIfAbsent(k.getFormatUsername(), new PlayerScoreboardStatistic(k.getFormatUsername()));
			
			if(killerStatistic == null) {
				killerStatistic = new PlayerScoreboardStatistic(k.getFormatUsername());
			}
			
			PlayerPanel.INDIVIDUAL_KILLS.refresh(k, "@or2@ - Current Players killed: @yel@" + killerStatistic.getKills().incrementAndGet());
			
			PlayerPanel.TOTAL_PLAYER_KILLS.refresh(k, "@or2@ - Total Players killed: @yel@" + k.getPlayerKills().incrementAndGet());

			//killstreak
			if(k.getCurrentKillstreak().incrementAndGet() > k.getHighestKillstreak().get()) {
				k.getHighestKillstreak().set(k.getCurrentKillstreak().get());
				PlayerPanel.HIGHEST_KILLSTREAK.refresh(k, "@or2@ - Highest Killstreak: @yel@" + k.getHighestKillstreak());
			}
			
			PlayerPanel.CURRENT_KILLSTREAK.refresh(k, "@or2@ - Current Killstreak: @yel@" + k.getCurrentKillstreak().get());
			
			if(killerStatistic.getCurrentKillstreak().incrementAndGet() > killerStatistic.getHighestKillstreak().get()) {
				killerStatistic.getHighestKillstreak().set(killerStatistic.getCurrentKillstreak().get());
				PlayerPanel.INDIVIDUAL_HIGHEST_KILLSTREAKS.refresh(k, "@or2@ - Highest Killstreak: @yel@" + killerStatistic.getHighestKillstreak().get());
			}
			PlayerPanel.INDIVIDUAL_CURRENT_KILLSTREAKS.refresh(k, "@or2@ - Current Killstreak: @yel@" + killerStatistic.getCurrentKillstreak().get());

			k.message(RandomUtils.random(GameConstants.DEATH_MESSAGES).replaceAll("-victim-", getCharacter().getFormatUsername()).replaceAll("-killer-", k.getFormatUsername()));
		});
		
		getCharacter().getCombatBuilder().getDamageCache().calculateProperKiller().ifPresent(e -> {
			if(e.isNpc()) {
				getCharacter().getDeathsByNpc().incrementAndGet();
				PlayerPanel.TOTAL_NPC_DEATHS.refresh(getCharacter(), "@or2@ - Total Npc deaths: @yel@" + getCharacter().getDeathsByNpc().get());
			}
		});
	}
	
	@Override
	public void postDeath() {
		getCharacter().getMessages().sendCloseWindows();
		getCharacter().getCombatBuilder().reset();
		getCharacter().getCombatBuilder().getDamageCache().clear();
		getCharacter().getTolerance().reset();
		getCharacter().getSpecialPercentage().set(100);
		getCharacter().getMessages().sendConfig(301, 0);
		getCharacter().setSpecialActivated(false);
		getCharacter().getSkullTimer().set(0);
		getCharacter().setAntifireDetail(Optional.empty());
		//character.setSkullIcon(FightCavesHandler.isChampion(character) ? Player.RED_SKULL : -1);
		getCharacter().getTeleblockTimer().set(0);
		getCharacter().animation(new Animation(65535));
		WeaponInterface.execute(getCharacter(), getCharacter().getEquipment().get(Equipment.WEAPON_SLOT));
		if(deathMessage) {
			getCharacter().message(getCharacter().getRights().less(Rights.ADMINISTRATOR) ? "Oh dear, you're dead!" : "You are unaffected by death because of your rank.");
		}

		getCharacter().getMessages().sendWalkable(-1);
		Prayer.deactivateAll(getCharacter());

		if(getCharacter().isNightmareMode()) {
			for(int index = 0; index < getCharacter().getSkills().length; index++) {
				Skill skill = getCharacter().getSkills()[index];

				int newLevel = (int) (skill.getRealLevel() * 0.75);

				if(index == Skills.HITPOINTS && newLevel < 10) {
					newLevel = 10;
				}

				skill.setRealLevel(newLevel);
			}
		}

		Skills.restoreAll(getCharacter());

		getCharacter().getActivityManager().enable();
		getCharacter().getFlags().flag(UpdateFlag.APPEARANCE);
	}
	
	/**
	 * Calculates and drops all of the items from {@code character} for
	 * {@code killer}.
	 * @param character the character whose items are being dropped.
	 * @param killer    the killer who the items are being dropped for.
	 * @param dropAll   indicates no-items will be kept except the untradables.
	 */
	private void calculateDropItems(Player character, Optional<Player> killer, boolean dropAll) {
		List<Item> keep = new LinkedList<>();
		List<Item> items = new LinkedList<>();
		Region region = character.getRegion();
		killer.ifPresent(player -> items.add(new Item(19000, RandomUtils.inclusive(10, 50) * (GameConstants.DOUBLE_BLOOD_MONEY_EVENT ? 2 : 1))));
		character.getEquipment().forEach($it -> {
			if($it.getDefinition().isTradable()) {
				items.add($it);
			} else {
				keep.add($it);
			}
		});
		character.getInventory().forEach($it -> {
			if($it.getDefinition().isTradable()) {
				items.add($it);
			} else {
				keep.add($it);
			}
		});
		if(items.size() > 0) {
			character.getEquipment().clear();
			character.getInventory().clear();
			character.getEquipment().refresh(character, Equipment.EQUIPMENT_DISPLAY_ID);
			character.getInventory().refresh(character, Inventory.INVENTORY_DISPLAY_ID);
			int amount = dropAll ? 0 : character.getSkullTimer().get() > 0 ? 0 : 3;
			if(Prayer.isActivated(character, Prayer.PROTECT_ITEM) || Prayer.isActivated(character, Prayer.CURSES_PROTECT_ITEM)) {
				if(!dropAll) {
					amount++;
				}
			}
			if(amount > 0) {
				items.sort(new Ordering<Item>() {
					@Override
					public int compare(Item left, Item right) {
						return Integer.compare(left.getValue().getValue(), right.getValue().getValue());
					}
				}.reverse());
				for(Iterator<Item> it = items.iterator(); it.hasNext(); ) {
					Item next = it.next();
					if(amount == 0) {
						break;
					}
					character.getInventory().add(new Item(next.getId()));
					if(next.getDefinition().isStackable() && next.getAmount() > 1) {
						next.decrementAmountBy(1);
					} else {
						it.remove();
					}
					amount--;
				}
			}
			if(region != null) {
				region.register(!killer.isPresent() ? new ItemNode(new Item(526), character.getPosition(), character) : new ItemNode(new Item(526), character.getPosition(), killer.get()));
				items.forEach(item -> region.register(!killer.isPresent() ? new ItemNode(item, character.getPosition(), character) : new ItemNode(item, character.getPosition(), killer.get())));
			}
			character.getInventory().addAll(keep);
		} else {
			region.register(!killer.isPresent() ? new ItemNode(new Item(526), character.getPosition(), character) : new ItemNode(new Item(526), character.getPosition(), killer.get()));
		}
	}
}
