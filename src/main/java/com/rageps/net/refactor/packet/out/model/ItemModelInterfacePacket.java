package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemModelInterfacePacket extends Packet {

    private final int id, zoom, model;

    public ItemModelInterfacePacket(int id, int zoom, int model) {
        this.id = id;
        this.zoom = zoom;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public int getModel() {
        return model;
    }

    public int getZoom() {
        return zoom;
    }
}