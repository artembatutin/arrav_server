package net.edge.content.item;

import net.edge.content.skill.Skills;
import net.edge.task.LinkedTaskSequence;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.Hit;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.area.AreaManager;

import java.util.stream.IntStream;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 9-6-2017.
 */
public final class OverloadEffectTask extends Task {

    /**
     * The player this effect is for.
     */
    private final Player player;

    /**
     * Creates a new {@link Task}.
     */
    public OverloadEffectTask(Player player) {
        super(25, false);
        this.player = player;
    }

    /**
     * The task cycles 40 times which makes up for 6 minutes.
     * <p>The task is ran every 25 ticks (15 seconds)</p>
     */
    private int cycle = 10;

    private int damageCycle;

    @Override
    protected void onSubmit() {
        LinkedTaskSequence seq = new LinkedTaskSequence(true);
        seq.connect(1, () -> {
            if(damageCycle == 5) {
                seq.cancel();
                return;
            }
            player.damage(new Hit(100));
            player.animation(new Animation(3170, Animation.AnimationPriority.HIGH));
            damageCycle++;
        });
        seq.start();
        updateSkills();
    }
    /**
     * A function executed when the {@code counter} reaches the {@code delay}.
     */
    @Override
    protected void execute() {
        if(player.inWilderness() || cycle < 1) {
            player.message("The effects of overload have ran out.");
            this.cancel();
            return;
        }
        updateSkills();
        cycle--;
    }

    @Override
    public void onCancel() {
        restoreSkills();
        player.resetOverloadEffect(false);
    }

    /**
     * Applies the overload effect for the player.
     */
    private void updateSkills() {
        PotionConsumable.onOverloadEffect(player);
    }

    /**
     * Removes the overload effect and restores the skills back to their real state.
     */
    private void restoreSkills() {
        IntStream.range(0, 6).filter(i -> i != Skills.PRAYER).forEach(i -> Skills.restore(player, i));
    }
}
