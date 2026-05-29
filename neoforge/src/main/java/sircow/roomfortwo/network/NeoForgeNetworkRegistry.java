package sircow.roomfortwo.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import sircow.roomfortwo.util.BedOccupancyTracker;

@Mod.EventBusSubscriber(modid = "roomfortwo", bus = Mod.EventBusSubscriber.Bus.MOD)
public class NeoForgeNetworkRegistry {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar("roomfortwo");

        registrar.play(
                BedOccupancySyncPayload.ID,
                BedOccupancySyncPayload::new,
                handler -> handler.client((payload, context) -> context.workHandler().submitAsync(() ->
                        BedOccupancyTracker.updateClientCache(payload.entitySlots())
                ))
        );
    }
}
