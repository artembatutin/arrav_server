package com.rageps.net.refactor.packet.out.model;

import com.rageps.content.dialogue.DialogueBuilder;
import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CloseInterfacePacket extends Packet {

    private final DialogueBuilder dialogueBuilder;

    public CloseInterfacePacket(DialogueBuilder dialogueBuilder) {
        this.dialogueBuilder = dialogueBuilder;
    }

    public DialogueBuilder getDialogueBuilder() {
        return dialogueBuilder;
    }
}