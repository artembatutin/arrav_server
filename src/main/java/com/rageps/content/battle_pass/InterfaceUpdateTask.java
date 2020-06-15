package com.rageps.content.battle_pass;

import com.rageps.task.Task;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceUpdateTask extends Task {


    public InterfaceUpdateTask(Player key) {
        super(1, key, true);
    }

    @Override
    protected void execute() {
        Player player = (Player) getAttachment().get();

        if(!player.getInterfaceManager().isInterfaceOpen(0))
            this.cancel();
    }
}