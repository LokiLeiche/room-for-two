package sircow.roomfortwo.network;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import sircow.roomfortwo.util.BedOccupancyTracker;

import java.util.HashMap;
import java.util.Map;

public class FabricNetworkRegistry implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {}

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(BedOccupancySyncPayload.ID, (client, handler, buf, responseSender) -> {
            int size = buf.readVarInt();
            Map<Integer, Integer> map = new HashMap<>();

            for (int i = 0; i < size; i++) {
                map.put(buf.readVarInt(), buf.readVarInt());
            }
            client.execute(() -> BedOccupancyTracker.updateClientCache(map));
        });
    }
}
