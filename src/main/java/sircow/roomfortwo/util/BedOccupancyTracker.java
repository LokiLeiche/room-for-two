package sircow.roomfortwo.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import sircow.roomfortwo.network.BedOccupancySyncPayload;
import sircow.roomfortwo.network.RoomForTwoNetwork;

import java.util.*;

public final class BedOccupancyTracker {
    private static final Map<Integer, Integer> clientSlotCache = new HashMap<>();
    private static final Map<BlockPos, List<Integer>> serverBedOrders = new HashMap<>();

    private BedOccupancyTracker() {}

    public static int getSlot(int entityId) {
        Integer slot = clientSlotCache.get(entityId);
        return slot == null ? 0 : slot;
    }

    public static void updateClientCache(Map<Integer, Integer> map) {
        clientSlotCache.putAll(map);
    }

    public static void cleanClientEntity(int entityId) {
        clientSlotCache.remove(entityId);
    }

    public static void updateBedOccupancy(WorldServer world, BlockPos bedPos, int leavingEntityId) {
        if (bedPos == null) {
            return;
        }

        List<EntityPlayer> sleepers = world.getEntitiesWithinAABB(
                EntityPlayer.class,
                new AxisAlignedBB(bedPos).grow(4.0D),
                player ->
                        player.isPlayerSleeping()
                                && player.getEntityId() != leavingEntityId
                                && bedPos.equals(player.bedLocation)
        );

        HashSet<Integer> currentSleeperIds = new HashSet<>();
        for (EntityPlayer sleeper : sleepers) {
            currentSleeperIds.add(sleeper.getEntityId());
        }

        List<Integer> bedOrder = serverBedOrders.computeIfAbsent(bedPos, k -> new ArrayList<>());
        for (int i = bedOrder.size() - 1; i >= 0; i--) {
            int id = bedOrder.get(i);

            if (id == leavingEntityId || !currentSleeperIds.contains(id)) {
                bedOrder.remove(i);
            }
        }

        for (EntityPlayer sleeper : sleepers) {
            int id = sleeper.getEntityId();

            if (!bedOrder.contains(id)) {
                bedOrder.add(id);
            }
        }

        if (bedOrder.isEmpty()) {
            serverBedOrders.remove(bedPos);
            return;
        }

        Map<Integer, Integer> syncMap = new HashMap<>();

        for (int i = 0; i < bedOrder.size(); i++) {
            syncMap.put(bedOrder.get(i), i);
        }

        RoomForTwoNetwork.sendToAllTracking(new BedOccupancySyncPayload(syncMap), bedPos, world.provider.getDimension());
    }
}
