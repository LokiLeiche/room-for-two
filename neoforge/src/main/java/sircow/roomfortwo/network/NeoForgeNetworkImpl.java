package sircow.roomfortwo.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.PacketDistributor;
import sircow.roomfortwo.platform.services.IPlatformNetwork;

import java.util.Map;

public class NeoForgeNetworkImpl implements IPlatformNetwork {
    @Override
    public void broadcastBedOccupancy(ServerLevel level, BlockPos bedPos, Map<Integer, Integer> entitySlots) {
        BedOccupancySyncPayload payload = new BedOccupancySyncPayload(entitySlots);

        double x = bedPos.getX();
        double y = bedPos.getY();
        double z = bedPos.getZ();
        double radius = 128.0;

        PacketDistributor.NEAR.with(new PacketDistributor.TargetPoint(x, y, z, radius, level.dimension())).send(payload);
    }
}
