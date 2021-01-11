package com.rageps.content.battle_pass.task.impl;


import com.rageps.content.battle_pass.task.BattlePassTask;
import com.rageps.content.battle_pass.task.BattlePassTaskType;
import com.rageps.world.entity.actor.player.Player;

public class ZoneBattlePassTask implements BattlePassTask {
    @Override
    public void oncomplete(Player player) {

    }

    @Override
    public int getProgress(Player player) {
        return 0;
    }

    @Override
    public void progress(Player player) {

    }

    @Override
    public BattlePassTaskType type() {
        return BattlePassTaskType.PVM;
    }

    @Override
    public void complete(Player player) {

    }
}
