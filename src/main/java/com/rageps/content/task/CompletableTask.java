package com.rageps.content.task;

import com.rageps.world.entity.actor.player.Player;

/**
 * Represents a task that is completable ingame by a player. This task
 * could be any type activity, or accomplishment in-game. And may be used for
 * A multitude of purposes.
 *
 * This can be extended by things like, Achievements, Daily tasks, Battle-pass tasks, etc.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public interface CompletableTask {

    void oncomplete(Player player);

    int getProgress(Player player);

    void progress(Player player);

    void complete(Player player);


}
