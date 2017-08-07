package net.edge.content.skill.agility.test;

import net.edge.action.impl.ObjectAction;
import net.edge.content.skill.SkillData;
import net.edge.content.skill.Skills;
import net.edge.content.skill.action.SkillAction;
import net.edge.content.skill.agility.test.obstacle.Obstacle;
import net.edge.task.Task;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.object.GameObject;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-8-2017.
 */
public abstract class Agility extends SkillAction {

    /**
     * Determines if the player was running.
     */
    public final boolean running;

    /**
     * The obstacle the player is crossing.
     */
    public final Obstacle crossing;

    /**
     * The object that we're crossing.
     */
    public final GameObject object;

    /**
     * Constructs a new {@link SkillAction}.
     * @param player   {@link #player}.
     * @param object   {@link #object}.
     * @param crossing {@link #crossing}.
     */
    public Agility(Player player, GameObject object, Obstacle crossing) {
        super(player, Optional.empty());
        this.crossing = crossing;
        this.running = player.getMovementQueue().isRunning();
        this.object = object;
    }

    /**
     * The delay intervals of this skill action in ticks.
     * @return the delay intervals.
     */
    @Override
    public final int delay() {
        return crossing.delay;
    }

    /**
     * The priority determines if this skill can be overriden by another skill.
     * @return <true> if the skill can be overriden by other skills, <false> otherwise.
     */
    @Override
    public final boolean isPrioritized() {
        return true;
    }

    /**
     * The skill that this skill action is for.
     * @return the skill data.
     */
    @Override
    public final SkillData skill() {
        return SkillData.AGILITY;
    }

    @Override
    public final boolean instant() {
        return true;
    }

    @Override
    public final boolean canExecute() {
        //Method shouldn't be used since this checks each cycle, and with agility you only check on initialisation.
        return true;
    }

    @Override
    public final boolean init() {
        if(!getPlayer().getSkills()[Skills.AGILITY].reqLevel(crossing.requirement)) {
            getPlayer().message("You need an agility level of " + crossing.requirement + " to cross this obstacle.");
            return false;
        }
        return crossing.crossable(player);
    }

    @Override
    public final void onSubmit() {
        crossing.initialize(player);

        if(running) {
            player.getMovementQueue().setRunning(false);
        }

        // ARTEM TODO DO THE SILLY WALK TO SPECIFIC POSITION

        player.getActivityManager().setAllExcept(ActivityManager.ActivityType.LOG_OUT);
    }

    /**
     * The method executed when the delay has elapsed.
     * @param t the task executing this skill action.
     */
    @Override
    public final void execute(Task t) {
        if(getPlayer().getPosition().same(crossing.end)) {
            t.cancel();
            return;
        }
        crossing.execute(player, t);
    }

    @Override
    public void onStop() {
        if(getPlayer().getState() == EntityState.INACTIVE) {
            getPlayer().move(crossing.start[0]);
            return;
        }

        crossing.onStop(player);
        Skills.experience(getPlayer(), crossing.experience, skill().getId());

        if(running) {
            player.getMovementQueue().setRunning(true);
        }

        player.getActivityManager().enable();
    }
}
