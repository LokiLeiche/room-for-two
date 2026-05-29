package sircow.roomfortwo.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import sircow.roomfortwo.platform.services.IPlatformNetwork;

import java.util.Map;

public class FabricNetworkImpl implements IPlatformNetwork {
    @Override
    public void broadcastBedOccupancy(ServerLevel level, BlockPos bedPos, Map<Integer, Integer> entitySlots) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(entitySlots.size());
        entitySlots.forEach((entityId, slot) -> {
            buf.writeVarInt(entityId);
            buf.writeVarInt(slot);
        });

        for (ServerPlayer player : level.players()) {
            if (player.blockPosition().closerThan(bedPos, 128)) {
                ServerPlayNetworking.send(player, BedOccupancySyncPayload.ID, buf);
            }
        }
    }
}