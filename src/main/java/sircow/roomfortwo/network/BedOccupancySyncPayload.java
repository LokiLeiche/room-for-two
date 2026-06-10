package sircow.roomfortwo.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.HashMap;
import java.util.Map;

public class BedOccupancySyncPayload implements IMessage {
    private Map<Integer, Integer> entitySlots;

    public BedOccupancySyncPayload() {
    }

    public BedOccupancySyncPayload(Map<Integer, Integer> entitySlots) {
        this.entitySlots = entitySlots;
    }

    public Map<Integer, Integer> getEntitySlots() {
        return entitySlots;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();

        entitySlots = new HashMap<>();

        for (int i = 0; i < size; i++) {
            entitySlots.put(buf.readInt(), buf.readInt());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entitySlots.size());

        for (Map.Entry<Integer, Integer> entry : entitySlots.entrySet()) {
            buf.writeInt(entry.getKey());
            buf.writeInt(entry.getValue());
        }
    }
}
