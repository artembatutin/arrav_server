package com.rageps.content.battle_pass.task;

import com.rageps.content.task.CompletableTask;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public interface BattlePassTask extends CompletableTask {

    @Override
    void oncomplete(Player player);

    @Override
    int getProgress(Player player);

    @Override
    void complete(Player player);

    @Override
    void progress(Player player);

    BattlePassTaskType type();
}
