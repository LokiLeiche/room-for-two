package sircow.roomfortwo.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import sircow.roomfortwo.Constants;

import java.util.HashMap;
import java.util.Map;

public record BedOccupancySyncPayload(Map<Integer, Integer> entitySlots) implements CustomPacketPayload {
    public static final Type<BedOccupancySyncPayload> TYPE = new Type<>(Constants.id("bed_occupancy_sync"));

    public static final StreamCodec<FriendlyByteBuf, BedOccupancySyncPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeVarInt(payload.entitySlots().size());
                payload.entitySlots().forEach((entityId, slot) -> {
                    buf.writeVarInt(entityId);
                    buf.writeVarInt(slot);
                });
            },
            buf -> {
                int size = buf.readVarInt();
                Map<Integer, Integer> map = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    map.put(buf.readVarInt(), buf.readVarInt());
                }
                return new BedOccupancySyncPayload(map);
            }
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
