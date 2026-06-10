package sircow.roomfortwo.network;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class RoomForTwoNetwork {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("roomfortwo");
    private static int packetId;

    public static void init() {
        INSTANCE.registerMessage(BedOccupancySyncHandler.class, BedOccupancySyncPayload.class, packetId++, Side.CLIENT);
    }

    public static void sendToAllTracking(BedOccupancySyncPayload packet, BlockPos pos, int dimension) {
        INSTANCE.sendToAllAround(packet, new NetworkRegistry.TargetPoint(dimension, pos.getX(), pos.getY(), pos.getZ(), 128.0D));
    }
}
